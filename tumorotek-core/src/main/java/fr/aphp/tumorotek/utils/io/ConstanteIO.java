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
package fr.aphp.tumorotek.utils.io;

public class ConstanteIO
{

   //CONDITION
   public static final String CONDITION_ET = "and";
   public static final String CONDITION_OU = "or";

   //ENTITE
   /*public static final int ENTITE_PATIENT = 1;
   public static final int ENTITE_MALADIE = 2;
   public static final int ENTITE_PRELEVEMENT = 3;
   public static final int ENTITE_ECHANTILLON = 4;
   public static final int ENTITE_DERIVE = 5;
   public static final int ENTITE_CESSION = 6;
   public static final int ENTITE_TRANSFORMATION = 7;*/

   //SOUS ENTITE PATIENT
   /*public static final int SOUSENTITE_PATIENT_NIP = 1;
   public static final int SOUSENTITE_PATIENT_NOM = 2;
   public static final int SOUSENTITE_PATIENT_NOM_NAISSANCE = 3;
   public static final int SOUSENTITE_PATIENT_PRENOM = 4;
   public static final int SOUSENTITE_PATIENT_SEXE = 5;
   public static final int SOUSENTITE_PATIENT_DATE_NAISSANCE = 6;
   public static final int SOUSENTITE_PATIENT_VILLE_NAISSANCE = 7;
   public static final int SOUSENTITE_PATIENT_PAYS_NAISSANCE = 8;
   public static final int SOUSENTITE_PATIENT_PATIENT_ETAT = 9;
   public static final int SOUSENTITE_PATIENT_DATE_ETAT = 10;
   public static final int SOUSENTITE_PATIENT_DATE_DECES = 11;
   public static final int SOUSENTITE_PATIENT_ETAT_INCOMPLET = 12;
   public static final int SOUSENTITE_PATIENT_ARCHIVE = 13;*/

   //SOUS ENTITE MALADIE
   /*public static final int SOUSENTITE_MALADIE_PATIENT_ID = 1;
   public static final int SOUSENTITE_MALADIE_LIBELLE = 2;
   public static final int SOUSENTITE_MALADIE_CODE  = 3;
   public static final int SOUSENTITE_MALADIE_DATE_DIAGNOSTIC  = 4;
   public static final int SOUSENTITE_MALADIE_DATE_DEBUT  = 5;*/

   //SOUS ENTITE PRELEVEMENT
   /*public static final int SOUSENTITE_PRELEVEMENT_BANQUE_ID = 1;
   public static final int SOUSENTITE_PRELEVEMENT_CODE = 2;
   public static final int SOUSENTITE_PRELEVEMENT_NATURE_ID = 3;
   public static final int SOUSENTITE_PRELEVEMENT_MALADIE_ID = 4;
   public static final int SOUSENTITE_PRELEVEMENT_CONSENT_TYPE_ID = 5;
   public static final int SOUSENTITE_PRELEVEMENT_CONSENT_DATE = 6;
   public static final int SOUSENTITE_PRELEVEMENT_PRELEVEUR_ID = 7;
   public static final int SOUSENTITE_PRELEVEMENT_SERVICE_PRELEVEUR_ID = 8;
   public static final int SOUSENTITE_PRELEVEMENT_DATE_PRELEVEMENT = 9;
   public static final int SOUSENTITE_PRELEVEMENT_PRELEVEMENT_TYPE_ID = 10;
   public static final int SOUSENTITE_PRELEVEMENT_CONDIT_TYPE_ID = 11;
   public static final int SOUSENTITE_PRELEVEMENT_CONDIT_MILIEU_ID = 12;
   public static final int SOUSENTITE_PRELEVEMENT_CONDIT_NBR = 13;
   public static final int SOUSENTITE_PRELEVEMENT_DATE_DEPART = 14;
   public static final int SOUSENTITE_PRELEVEMENT_TRANSPORTEUR_ID = 15;
   public static final int SOUSENTITE_PRELEVEMENT_TRANSPORT_TEMP = 16;
   public static final int SOUSENTITE_PRELEVEMENT_DATE_ARRIVEE = 17;
   public static final int SOUSENTITE_PRELEVEMENT_OPERATEUR_ID = 18;
   public static final int SOUSENTITE_PRELEVEMENT_QUANTITE = 19;
   public static final int SOUSENTITE_PRELEVEMENT_QUANTITE_UNITE_ID = 20;
   public static final int SOUSENTITE_PRELEVEMENT_VOLUME = 21;
   public static final int SOUSENTITE_PRELEVEMENT_VOLUME_UNITE_ID = 22;
   public static final int SOUSENTITE_PRELEVEMENT_PATIENT_NDA = 23;
   public static final int SOUSENTITE_PRELEVEMENT_NUMERO_LABO = 24;
   public static final int SOUSENTITE_PRELEVEMENT_DATE_CONGELATION = 25;
   public static final int SOUSENTITE_PRELEVEMENT_STERILE = 26;
   public static final int SOUSENTITE_PRELEVEMENT_ETAT_INCOMPLET = 27;
   public static final int SOUSENTITE_PRELEVEMENT_ARCHIVE = 28;*/

   //SOUS ENTITE ECHANTILLON
   /*public static final int SOUSENTITE_ECHANTILLON_OBJET_STATUT_ID = 1;
   public static final int SOUSENTITE_ECHANTILLON_CONTENEUR_ID = 2;
   public static final int SOUSENTITE_ECHANTILLON_PRELEVEMENT_ID = 3;
   public static final int SOUSENTITE_ECHANTILLON_COLLABORATEUR_ID = 4;
   public static final int SOUSENTITE_ECHANTILLON_CODE = 5;
   public static final int SOUSENTITE_ECHANTILLON_DATE_STOCK = 6;
   public static final int SOUSENTITE_ECHANTILLON_ADRP_STOCK = 7;
   public static final int SOUSENTITE_ECHANTILLON_EMPLACEMENT_ID = 8;
   public static final int SOUSENTITE_ECHANTILLON_ECHANTILLON_TYPE_ID = 9;
   public static final int SOUSENTITE_ECHANTILLON_ADICAP_TOPO_ID = 10;
   public static final int SOUSENTITE_ECHANTILLON_LATERALITE_ID = 11;
    public static final int SOUSENTITE_ECHANTILLON_QUANTITE = 12;
    public static final int SOUSENTITE_ECHANTILLON_QUANTITE_INIT = 13;
   public static final int SOUSENTITE_ECHANTILLON_QUANTITE_UNITE_ID = 14;
   public static final int SOUSENTITE_ECHANTILLON_VOLUME = 15;
   public static final int SOUSENTITE_ECHANTILLON_VOLUME_INIT = 16;
   public static final int SOUSENTITE_ECHANTILLON_VOLUME_UNITE_ID = 17;
   public static final int SOUSENTITE_ECHANTILLON_DELAI_CGL = 18;
   public static final int SOUSENTITE_ECHANTILLON_ECHAN_QUALITE_ID = 19;
   public static final int SOUSENTITE_ECHANTILLON_TUMORAL = 20;
   public static final int SOUSENTITE_ECHANTILLON_MODE_PREPA_ID = 21;
   public static final int SOUSENTITE_ECHANTILLON_CR_ANAPATH_ID = 22;
   public static final int SOUSENTITE_ECHANTILLON_STERILE = 23;
   public static final int SOUSENTITE_ECHANTILLON_RESERVATION_ID = 24;
   public static final int SOUSENTITE_ECHANTILLON_ETAT_INCOMPLET = 25;
   public static final int SOUSENTITE_ECHANTILLON_ARCHIVE = 26;*/

   //SOUS ENTITE PRODUIT DERIVE
   /*public static final int SOUSENTITE_DERIVE_PROD_TYPE_ID = 1;
   public static final int SOUSENTITE_DERIVE_COLLABORATEUR_ID = 2;
   public static final int SOUSENTITE_DERIVE_CODE = 3;
   public static final int SOUSENTITE_DERIVE_CODE_LABO = 4;
   public static final int SOUSENTITE_DERIVE_VOLUME = 5;
   public static final int SOUSENTITE_DERIVE_CONC = 6;
   public static final int SOUSENTITE_DERIVE_DATE_STOCK = 7;
   public static final int SOUSENTITE_DERIVE_EMPLACEMENT_ID = 8;
   public static final int SOUSENTITE_DERIVE_ADRP_STOCK = 9;
   public static final int SOUSENTITE_DERIVE_VOL_UNITE_ID = 10;
   public static final int SOUSENTITE_DERIVE_CONC_UNITE_ID = 11;
   public static final int SOUSENTITE_DERIVE_PROD_STATUT_ID = 12;
   public static final int SOUSENTITE_DERIVE_QUANTITE = 13;
   public static final int SOUSENTITE_DERIVE_QUANTITE_UNITE_ID = 14;
   public static final int SOUSENTITE_DERIVE_PROD_QUALITE_ID = 15;
   public static final int SOUSENTITE_DERIVE_DATE_TRANSFORMATION = 16;
   public static final int SOUSENTITE_DERIVE_OBJET_STATUT_ID = 17;
   public static final int SOUSENTITE_DERIVE_RESERVATION_ID = 18;
   public static final int SOUSENTITE_DERIVE_ETAT_INCOMPLET = 19;
   public static final int SOUSENTITE_DERIVE_ARCHIVE = 20;*/

   //SOUS ENTITE CESSION
   /*public static final int SOUSENTITE_CESSION_NUMERO = 1;
   public static final int SOUSENTITE_CESSION_BANQUE_ID = 2;
   public static final int SOUSENTITE_CESSION_CESSION_TYPE_ID = 3;
   public static final int SOUSENTITE_CESSION_DEMANDE_DATE = 4;
   public static final int SOUSENTITE_CESSION_CESSION_EXAMEN_ID = 5;
   public static final int SOUSENTITE_CESSION_MTA_ID = 6;
   public static final int SOUSENTITE_CESSION_DESTINATAIRE_ID = 7;
   public static final int SOUSENTITE_CESSION_SERVICE_DEST_ID = 8;
   public static final int SOUSENTITE_CESSION_DESCRIPTION = 9;
   public static final int SOUSENTITE_CESSION_DEMANDEUR_ID = 10;
   public static final int SOUSENTITE_CESSION_CESSION_STATUT_ID = 11;
   public static final int SOUSENTITE_CESSION_VALIDATION_DATE = 12;
   public static final int SOUSENTITE_CESSION_EXECUTANT_ID = 13;
   public static final int SOUSENTITE_CESSION_TRANSPORTEUR_ID = 14;
   public static final int SOUSENTITE_CESSION_DEPART_DATE = 15;
   public static final int SOUSENTITE_CESSION_ARRIVEE_DATE = 16;
   public static final int SOUSENTITE_CESSION_OBSERVATIONS = 17;
   public static final int SOUSENTITE_CESSION_TEMPERATURE = 18;
   public static final int SOUSENTITE_CESSION_DESTRUCTION_MOTIF = 19;
   public static final int SOUSENTITE_CESSION_DESTRUCTION_DATE = 20;
   public static final int SOUSENTITE_CESSION_STERILE = 21;
   public static final int SOUSENTITE_CESSION_ETAT_INCOMPLET = 22;
   public static final int SOUSENTITE_CESSION_ARCHIVE = 23;*/

   //TYPE
   public static final int TYPE_VARCHAR = 1;
   public static final int TYPE_DATETIME = 2;
   public static final int TYPE_FLOAT = 3;
   public static final int TYPE_BOOLEAN = 4;
   public static final int TYPE_INTEGER = 5;
   public static final int TYPE_NUMBER = 6;
   public static final int TYPE_THESAURUS = 7;

   //OPERATEUR
   public static final int OPERATEUR_INFERIEUR = 1;
   public static final int OPERATEUR_SUPERIEUR = 2;
   public static final int OPERATEUR_INFEGALE = 3;
   public static final int OPERATEUR_SUPEGALE = 4;
   public static final int OPERATEUR_DIFFERENT = 5;
   public static final int OPERATEUR_EGALE = 6;
   public static final int OPERATEUR_DANS = 7;
   public static final int OPERATEUR_PASDANS = 8;

   public static final int OPERATEUR_MOINS = 9;
   public static final int OPERATEUR_PLUS = 10;
   public static final int OPERATEUR_MULTIPLIE = 11;
   public static final int OPERATEUR_DIVISE = 12;

   //TRI
   public static final boolean TRI_ASC = true;
   public static final boolean TRI_DESC = false;

}
