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
package fr.aphp.tumorotek.manager.code;

import java.sql.SQLException;
import java.util.List;

import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeAssigne.
 * Concerne la creation de CodeAssigne représentant les codes
 * assignes pour l'organe, le type lésionnel/morphologique et
 * le diagnostic.
 * Interface créée le 26/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.6
 *
 */
public interface CodeAssigneManager
{

   /**
    * Recherche toutes les instances de codes présentes dans la codification.
    * @return List contenant les codes.
    */
   List<CodeAssigne> findAllObjectsManager();

   /**
    * Recherche les codes assignes dont le code like celui au paramètre.
    * @param code Code pour lequel on recherche des codes assignes.
    * @param exactMatch
    * @return une liste de codes assignes.
    */
   List<CodeAssigne> findByCodeLikeManager(String code, boolean exactMatch);

   /**
    * Recherche les codes assignes dont le libelle like celui au paramètre.
    * @param libelle Libelle pour lequel on recherche des codes assignes.
    * @param exactMatch
    * @return une liste de codes assignes.
    */
   List<CodeAssigne> findByLibelleLikeManager(String libelle, boolean exactMatch);

   /**
    * Recherche les codes qui sont assignes comme type lésionnel/morpho 
    * pour l'échantillon.
    * @param echantillon.
    * @return une liste de codes assignes.
    */
   List<CodeAssigne> findCodesMorphoByEchantillonManager(Echantillon echan);

   /**
    * Recherche les codes qui sont assignes pour definir l'organe 
    * dont est issu l'échantillon.
    * @param echantillon.
    * @return une liste de codes assignes.
    */
   List<CodeAssigne> findCodesOrganeByEchantillonManager(Echantillon echan);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param table CodeAssigne dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(CodeAssigne code);

   /**
    * Enregistre ou modifie un code assigne. 
    * @param code
    * @param echantillon
    * @param TableCodage code referent
    * @param utilisateur
    * @param String operation creation/modification
    */
   void createOrUpdateManager(CodeAssigne code, Echantillon echantillon, TableCodage table, Utilisateur utilisateur,
      String operation);

   /**
    * Supprime un objet de la base de données.
    * @param code CodeAssigne à supprimer de la base de données.
    */
   void removeObjectManager(CodeAssigne code);

   /**
    * Recherche les codes lésionels assignes exportes pour 
    * chacun des echantillons issus du prélèvement passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param prel Prelevement
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findCodesLesExportedByPrelevementManager(Prelevement prel);

   /**
    * Recherche les codes organes assignes exportes pour 
    * chacun des echantillons issus du prélèvement passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param prel Prelevement
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findCodesOrgExportedByPrelevementManager(Prelevement prel);

   /**
    * Recherche les codes organes assignes exportes pour 
    * chacun des echantillons issus du patient passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param pat Patient
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findCodesOrgExportedByPatientManager(Patient pat);

   /**
    * Recherche le premier code lésionel assigne pour 
    * chacun des echantillons issus du prélèvement passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param prel Prelevement
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findFirstCodesLesByPrelevementManager(Prelevement prel);

   /**
    * Recherche le premier code organe assigne pour 
    * chacun des echantillons issus du prélèvement passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param prel Prelevement
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findFirstCodesOrgByPrelevementManager(Prelevement prel);

   /**
    * Recherche le premier code organe assigne pour 
    * chacun des echantillons issus du patient passé en paramètre.
    * Ordonne les codes suivant id des échantillons.
    * @param pat Patient
    * @return une liste de CodeAssignes.
    */
   List<CodeAssigne> findFirstCodesOrgByPatientManager(Patient pat);

   /**
    * Transforme une liste de codeAssigne en une liste de String 
    * obtenue à partir du code ou du libelle si l'association entre 
    * la banque et la table de codage le mentionne.
    * @param codes
    * @return liste String, utilisée pour l'affichage.
    */
   List<String> formatCodesAsStringsManager(List<CodeAssigne> codes);

   /**
    * Préparations batch statements pour full JDBC inserts d'une liste de codes assignes associes 
    * à l'echantillon passe en parametre.
    * Les validations à la création sont identiques à celles lancées en JPA 
    * (doublons, syntaxe).
    * @param jdbcSuite contenant les ids et statements permettant 
    * la creation des objets en full JDBC
    * @param echantillon
    * @param codes
    * @param usr
    * @since 2.0.10.6
    */
   void prepareListJDBCManager(EchantillonJdbcSuite jdbcSuite, Echantillon echantillon, List<CodeAssigne> codes, Utilisateur usr)
      throws SQLException;
}
