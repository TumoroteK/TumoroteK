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
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * strategy dédiée au patient (hors collection Gatsbi) pour les contrôles en amont du traitement de modification des annotations par import.
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 *
 */
public class ImportChampAnnotationPatientStrategy extends AbstractImportChampAnnotationEntiteStrategy
{
   
   public ImportChampAnnotationPatientStrategy() {
      setNomChampForControle("Nom");
   }
   
   //Pour le patient, la clé fonctionnelle est le Nip sauf dans le cas d'un contexte Gatsbi ou c'est l'identifiant (cf ImportChampAnnotationPatientGatsbiStrategy)
   @Override
   public ChampEntite retrieveChampForCleFonctionnelle(Entite entite){
      return getChampEntiteFindManager().findByEntiteAndNomManager(entite, "Nip");
   }
   
   /**
    * Recherche les ids et les noms des patients dont les nips sont passés en paramètre pour une banque donnée.
    * @param nips
    * @param banque
    * @param nomChampForControle pour plus de généricité, le champ de la valeur de contrôle est récupéré du modèle d'import. Mais celui-ci est créé à partir de cette classe. Donc nomChampForControle vaut Nom
    * @return liste de CodeIdPair contenant également le nom
    * @since 2.3.1 (TK-538)
    */
   ///!\ cette requête est volontairement mise ici et non dans PatientManagerImpl car du fait de l'héritage, il ne faut pas gérer de transaction (cf commentaire TK-538 dans applicationContextManager.xml)
   @Override
   public List<CodeIdPair> findIdAndDataForControleByCodesAndBanque(List<String> nips, Banque banque, String nomChampForControle) {
      String hqlPartForControle = "";
      if(nomChampForControle != null) {
         String nomChampWithFirstCharInMinuscule = nomChampForControle.replaceFirst(".", (nomChampForControle.charAt(0) + "").toLowerCase());
         hqlPartForControle = new StringBuilder( ", pat.").append(nomChampWithFirstCharInMinuscule).toString();
      }
      StringBuilder hql = new StringBuilder();
      hql.append("SELECT new ").append(CodeIdPair.class.getName()).append("(pat.nip, pat.patientId").append(hqlPartForControle).append(") ");
      hql.append("FROM Patient pat WHERE pat.nip in (:listNip) AND exists (from Prelevement prel where prel.maladie.patient = pat and prel.banque = :banque )");
      
      TypedQuery<CodeIdPair> query = getEntityManagerFactory().createEntityManager().createQuery(hql.toString(), CodeIdPair.class);
      query.setParameter("listNip", nips);
      query.setParameter("banque", banque);
      
      return query.getResultList();
   }

}
