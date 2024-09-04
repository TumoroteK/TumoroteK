/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.systeme;

public enum EEntiteId
{
   PATIENT(1),    
   PRELEVEMENT(2),      
   ECHANTILLON(3),      
   STOCKAGE(4),      
   CESSION(5),
   ADMINISTRATION(6),
   MALADIE(7),
   PROD_DERIVE(8),
   BOITE(9),
   CONTENEUR(10),
   INDICATEUR(11),
   CONFORMITE(12),
   UTILISATEUR(13),
   PROFIL(14),
   ANNOTATION(15),
   CODE_ASSIGNE(16),
   PROTOCOLE(17),
   CONTRAT(18),
   RETOUR(19),
   MODELE(20),
   INCIDENT(21),
   REQUETE(22),
   FILTRE_IMPORT(23),
   AFFICHAGE_SYNTH(24),
   ETABLISSEMENT(25),
   SERVICE(26),
   COLLABORATEUR(27),
   TRANSPORTEUR(28),
   TABLE_ANNOTATION(29),
   CHAMP_ANNOTATION(30),
   ANNOTATION_VALEUR(31),
   CODE_SELECT(32),
   CODE_UTILISATEUR(33),
   BANQUE(34),
   NATURE(35),
   CONSENT_TYPE(36),
   PRELEVEMENT_TYPE(37),
   CONDIT_MILIEU(38),
   UNITE(39),
   OBJET_STATUT(40),
   CODE_ORGANE(41),
   ECHAN_QUALITE(42),
   MODE_PREPA(43),
   RESERVATION(44),
   PROD_TYPE(45),
   PROD_QUALITE(46),
   CONDIT_TYPE(47),
   CESSION_TYPE(48),
   CESSION_EXAMEN(49),
   CESSION_STATUT(50),
   DESTRUCTION_MOTIF(51),
   ECHANTILLON_TYPE(52),
   CODE_DOSSIER(53),
   CODE_MORPHO(54),
   PATIENT_MEDECIN(55),
   TERMINALE(56),
   ENCEINTE(57),
   FANTOME(58),
   MODE_PREPA_DERIVE(59),
   TRANSFORMATION(60),
   PLATEFORME(61),
   RISQUE(62),
   NON_CONFORMITE(63),
   CONFORMITE_TYPE(64),
   DIAGNOSTIC(65),
   IMPORT_TEMPLATE(66);
     
   private EEntiteId(Integer id) {
      this.id = id;
   }
      
   private Integer id;

   public Integer getId(){
      return id;
   }

}
