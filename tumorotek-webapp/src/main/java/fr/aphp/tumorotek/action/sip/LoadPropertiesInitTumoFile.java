/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.sip;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

public abstract class LoadPropertiesInitTumoFile
{
   /* declaration des noms des variables dans le fichier */
   private static final String MAX_PATIENTS = TkParam.MAX_PATIENTS.getKey();

   private static final String LONGUEUR_NIP = TkParam.LONGUEUR_NIP.getKey();

   private static final String SIP = TkParam.MODULE_SIP.getKey();

   /* accesseurs */
   /* getters */
   public static InitTumoFileBean getInitTumoFileBean(){
      InitTumoFileBean initTumoFileBean = null;
      ResourceBundle res;
      if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
         res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);

         // lecture des proprietes du fichier de ressources
         final int maxPatients = Integer.parseInt(res.getString(MAX_PATIENTS));
         final int longueurNip = Integer.parseInt(res.getString(LONGUEUR_NIP));
         final String sip = res.getString(SIP);

         // creation du bean de memorisation des proprietes
         initTumoFileBean = new InitTumoFileBean(maxPatients, longueurNip, sip);
      }

      return initTumoFileBean;
   }
}
