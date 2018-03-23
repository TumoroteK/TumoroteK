package fr.aphp.tumorotek.action.cas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class TumoCasAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{

   @Override
   public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
      final Authentication authentication) throws ServletException, IOException{

      if(!request.getRequestURL().toString().contains("/services/inlcusion")){
         getRedirectStrategy().sendRedirect(request, response, "/login/SelectBanque.zul");
      }else{
         super.onAuthenticationSuccess(request, response, authentication);
      }
   }
}
