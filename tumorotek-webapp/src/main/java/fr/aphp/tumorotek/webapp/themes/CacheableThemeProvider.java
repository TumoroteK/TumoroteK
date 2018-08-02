package fr.aphp.tumorotek.webapp.themes;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ThemeProvider;

public class CacheableThemeProvider implements ThemeProvider
{
   private static String DEFAULT_WCS = "~./zul/css/zk.wcs";

   @Override
   public Collection<Object> getThemeURIs(final Execution exec, final List<Object> uris){
      //font-size
      final String fsc = getFontSizeCookie(exec);
      if(fsc != null && fsc.length() > 0){
         for(final ListIterator<Object> it = uris.listIterator(); it.hasNext();){
            final String uri = (String) it.next();
            if(uri.startsWith(DEFAULT_WCS)){
               it.set(Aide.injectURI(uri, fsc));
               break;
            }
         }
      }

      //slivergray
      if("silvergray".equals(getSkinCookie(exec))){
         uris.add("~./silvergray/color.css.dsp");
         uris.add("~./silvergray/img.css.dsp");
      }else{
         uris.add("/css/tumo.css.dsp");
         uris.add("/css/form.css.dsp");
         uris.add("/css/tree.css.dsp");
      }
      return uris;
   }

   @Override
   public int getWCSCacheControl(final Execution exec, final String uri){
      return 8760; //safe to cache
   }

   @Override
   public String beforeWCS(final Execution exec, final String uri){
      final String[] dec = Aide.decodeURI(uri);
      if(dec != null){
         if("lg".equals(dec[1])){
            exec.setAttribute("fontSizeM", "15px");
            exec.setAttribute("fontSizeMS", "13px");
            exec.setAttribute("fontSizeS", "13px");
            exec.setAttribute("fontSizeXS", "12px");
         }else if("sm".equals(dec[1])){
            exec.setAttribute("fontSizeM", "10px");
            exec.setAttribute("fontSizeMS", "9px");
            exec.setAttribute("fontSizeS", "9px");
            exec.setAttribute("fontSizeXS", "8px");
         }
         return dec[0];
      }
      return uri;
   }

   @Override
   public String beforeWidgetCSS(final Execution exec, final String uri){
      return uri;
   }

   /** Returns the font size specified in cooke. */
   private static String getFontSizeCookie(final Execution exec){
      final Cookie[] cookies = ((HttpServletRequest) exec.getNativeRequest()).getCookies();
      if(cookies != null){
         for(int i = 0; i < cookies.length; i++){
            if("myfontsize".equals(cookies[i].getName())){
               return cookies[i].getValue();
            }
         }
      }
      return "";
   }

   /** Returns the skin specified in cookie. */
   private static String getSkinCookie(final Execution exec){
      final Cookie[] cookies = ((HttpServletRequest) exec.getNativeRequest()).getCookies();
      if(cookies != null){
         for(int i = 0; i < cookies.length; i++){
            if("myskin".equals(cookies[i].getName())){
               return cookies[i].getValue();
            }
         }
      }
      return "";
   }
}
