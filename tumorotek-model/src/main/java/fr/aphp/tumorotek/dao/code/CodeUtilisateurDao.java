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
package fr.aphp.tumorotek.dao.code;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine CodeUtilisateur.
 * Interface créée le 24/09/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface CodeUtilisateurDao extends GenericDaoJpa<CodeUtilisateur, Integer>
{

   /**
    * Recherche les codes utilisateurs dont le code est like celui
    * passé en paramètre.
    * @param code Code pour lequel on recherche des codes utilisateurs.
    * @param liste de banques
    * @return Liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByCodeLike(String code, List<Banque> banks);

   /**
    * Recherche les codes utilisateurs dont le libelle est like celui
    * passé en paramètre.
    * @param libelle pour lequel on recherche des codes utilisateurs.
    * @param banque
    * @return Liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByLibelleLike(String libelle, List<Banque> banks);

   /**
    * Recherche les codes utilisateurs pour l'utilisateur et la banque
    * passées en paramètres.
    * @param l'utilisateur pour lequel on recherche des codes.
    * @param la banque
    * @return une liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByUtilisateurAndBanque(Utilisateur u, Banque b);

   /**
    * Recherche tous les codes utilisateurs contenu dans un dossier.
    * @param codeDossier
    * @return une liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByCodeDossier(CodeDossier codeDossier);

   /**
    * Recherche tous les codes utilisateurs non contenu dans un dossier
    * pour la banque spécifiée.
    * @param banque
    * @return une liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByRootDossier(Banque bank);

   /**
    * Recherche tous les codes utilisateurs heritant d'un code parent.
    * @param Codeutilisateur parent
    * @return une liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByCodeParent(CodeUtilisateur parent);

   /**
    * Recherche tous les codes utilisateurs sauf celui dont l'id est passé
    * en paramètre.
    * @param codeUtilisateurId Identifiant du code que l'on souhaite
    * exclure de la liste retournée.
    * @return une liste de CodeUtilisateurs.
    */
   List<CodeUtilisateur> findByExcludedId(Integer codeUtilisateurId);

   /**
    * Recherche les codes utilisateurs issus du transcodage
    * en passant la table et le codeId du code dont on cherche
    * les transcodes enregistrés dans les
    * codes utilisateurs.
    * @param table
    * @param codeId
    * @param list banques auxquelles doivent appartenir les codes utilisateurs.
    * @return liste de codes utilisateurs
    */
   List<CodeUtilisateur> findByTranscodage(TableCodage table, Integer codeId, List<Banque> banks);
}
