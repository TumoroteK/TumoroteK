/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.dao.test;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * Classe permettant de remplir un bean du domaine à partir d'un
 * fichier de propriétés.
 *
 */
public final class PopulateBeanForTest
{

 PopulateBeanForTest(){}

   /**
    * Remplit un bean du domaine à partir d'un fichier.
    * @param o est le bean à remplir.
    * @param resource est l'emplacement du fichier.
    * @throws IllegalAccessException lance une exception.
    * @throws InvocationTargetException lance une exception.
    */
   public static void populateBean(final Object o, final String resource)
      throws IllegalAccessException, InvocationTargetException{

      final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

      final ResourceBundle bundle = ResourceBundle.getBundle(resource);

      //Field[] fields = o.getClass().getFields();

      final Enumeration<String> keys = bundle.getKeys();
      while(keys.hasMoreElements()){
         final String key = keys.nextElement();
         //map.put(resource.getString(key)); 

         try{
            if(o.getClass().getDeclaredField(key).getType().getName().equals("java.util.Date")){
               BeanUtils.setProperty(o, key, format.parse(bundle.getString(key)));
            }else{
               BeanUtils.setProperty(o, key, bundle.getString(key));
            }
         }catch(final SecurityException e){
            e.printStackTrace();
         }catch(final NoSuchFieldException e){
            e.printStackTrace();
         }catch(final ParseException e){
            e.printStackTrace();
         }
      }
   }

   //	/**
   //	 * Convert ResourceBundle into a Map object.
   //	 *
   //	 * @param resource a resource bundle to convert.
   //	 * @return Map a map version of the resource bundle.
   //	 */
   //	@Autowired
   // static Map<String, String> convertResourceBundleToMap(
   //			ResourceBundle resource) {
   //		Map<String, String> map = new HashMap<String, String>();
   //		Enumeration<String> keys = resource.getKeys();
   //		while (keys.hasMoreElements()) {
   //			String key = keys.nextElement();
   //			map.put(key, resource.getString(key));           
   //		}
   //		return map;
   //	}

}
