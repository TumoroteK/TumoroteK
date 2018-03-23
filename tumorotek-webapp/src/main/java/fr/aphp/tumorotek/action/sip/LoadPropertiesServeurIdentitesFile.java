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
package fr.aphp.tumorotek.action.sip;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.action.ManagerLocator;

public abstract class LoadPropertiesServeurIdentitesFile
{
   /* declaration des noms des variables dans le fichier */
   private static final String DBMS = "DBMS";
   private static final String HOST = "HOST";
   private static final String DRIVER = "DRIVER";
   private static final String DATABASE = "DATABASE";
   private static final String LOGIN = "LOGIN";
   private static final String PASSWORD = "PASSWORD";

   private static final String TABLE_PATIENT = "TABLE_PATIENT";
   private static final String NIP = "NIP";
   private static final String NIP_TYPE = "NIP_TYPE";
   private static final String NOM = "NOM";
   private static final String NOM_PATRON = "NOM_PATRON";
   private static final String PRENOM = "PRENOM";
   private static final String SEXE = "SEXE";
   private static final String SEXE_TYPE = "SEXE_TYPE";
   private static final String SEXE_FEMME = "SEXE_FEMME";
   private static final String SEXE_HOMME = "SEXE_HOMME";
   private static final String DATE_NAISS = "DATE_NAISS";

   private static final String NUM_DOSSIER = "NUM_DOSSIER";

   /* accesseurs */
   /* getters */
   public static ServeurIdentitesFileBean getServeurIdentitesFileBean(final String identificationPlateforme){

      ResourceBundle res = null;
      if(ManagerLocator.getResourceBundleSip().doesResourceBundleExists(identificationPlateforme)){
         res = ManagerLocator.getResourceBundleSip().getResourceBundle(identificationPlateforme);
      }

      // lecture des proprietes du fichier de ressources
      final String dbms = res.getString(DBMS);
      final String host = res.getString(HOST);
      final String driver = res.getString(DRIVER);
      final String database = res.getString(DATABASE);
      final String login = res.getString(LOGIN);
      final String password = res.getString(PASSWORD);

      final String tablePatient = res.getString(TABLE_PATIENT);
      final String nip = res.getString(NIP);
      final String nipType = res.getString(NIP_TYPE);
      final String nom = res.getString(NOM);
      final String nomPatron = res.getString(NOM_PATRON);
      final String prenom = res.getString(PRENOM);
      final String sexe = res.getString(SEXE);
      final String sexeType = res.getString(SEXE_TYPE);
      final String sexeFemme = res.getString(SEXE_FEMME);
      final String sexeHomme = res.getString(SEXE_HOMME);
      final String dateNaiss = res.getString(DATE_NAISS);

      final String numDossier = res.getString(NUM_DOSSIER);

      // creation du bean de memorisation des proprietes
      final ServeurIdentitesFileBean serveurIdentitesFileBean =
         new ServeurIdentitesFileBean(dbms, host, driver, database, login, password,

            tablePatient, nip, nipType, nom, nomPatron, prenom, sexe, sexeType, sexeFemme, sexeHomme, dateNaiss, numDossier);

      return serveurIdentitesFileBean;
   }
}
