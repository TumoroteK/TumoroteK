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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.entitestrategy;

import java.util.List;

import javax.persistence.TypedQuery;

import fr.aphp.tumorotek.model.CodeIdPair;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * strategy dédiée au prelevement pour faire les contrôles en amont du traitement de modification des annotations par import.
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 *
 */
public class ImportChampAnnotationPrelevementStrategy extends AbstractImportChampAnnotationEntiteStrategy
{
   //nomChampForControle vaut null dans le cas des prélèvement car aucun champ de contrôle n'est prévu à date
   @Override
   public List<CodeIdPair> findIdAndDataForControleByCodesAndBanque(List<String> listCodeForSelect, Banque banque, String nomChampForControle) {
      return findIdByCodesAndBanque(listCodeForSelect, banque);
   }
   
   /**
    * Recherche l'id associé aux codes passés en paramètre pour la banque également passée en paramètre 
    * @param codes, codes des prélèvements
    * @param banque, banque à prendre en compte
    * @return Liste des codes existants et de l'id associé.
    * @since 2.3.1
    */
   ///!\ cette requête est volontairement mis ici et non dans PrelevementManagerImpl car du fait de l'héritage, il ne faut pas gérer de transaction (cf commentaire TK-538 dans applicationContextManager.xml)
   private List<CodeIdPair> findIdByCodesAndBanque(List<String> codes, Banque banque) {
      String hql = "SELECT new "+CodeIdPair.class.getName() + "(p.code, p.prelevementId) FROM Prelevement p WHERE p.code in (:listCode) AND p.banque = :banque ";
      TypedQuery<CodeIdPair> query = getEntityManagerFactory().createEntityManager().createQuery(hql, CodeIdPair.class);
      query.setParameter("listCode", codes);
      query.setParameter("banque", banque);
      
      return query.getResultList();
   }
}
