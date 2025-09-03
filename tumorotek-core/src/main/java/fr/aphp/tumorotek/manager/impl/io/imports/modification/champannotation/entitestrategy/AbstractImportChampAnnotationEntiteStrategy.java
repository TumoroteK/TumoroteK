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

import javax.persistence.EntityManagerFactory;

import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationEntiteStrategy;
import fr.aphp.tumorotek.model.CodeIdPair;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * classe abstraite "chapeau" des "entité strategy". Elle regroupe les règles de gestion communes aux différentes entités
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public abstract class AbstractImportChampAnnotationEntiteStrategy implements ImportChampAnnotationEntiteStrategy
{
   private ChampEntiteManager champEntiteManager;// /!\ instance sans transaction pour éviter un problème avec l'héritage (cf mapping spring)
   
   private EntityManagerFactory entityManagerFactory;

   //nomChampForControle correspond au nom du champ associé à la colonne de contrôle.
   //par défaut, il n'y a pas de colonne de contrôle donc l'attribut restera à null (sera valorisé pour Patient)
   private String nomChampForControle;

   protected EntityManagerFactory getEntityManagerFactory(){
      return entityManagerFactory;
   }

   public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
      this.entityManagerFactory = entityManagerFactory;
   }
   
   protected ChampEntiteManager getChampEntiteManager(){
      return champEntiteManager;
   }

   public void setChampEntiteManager(ChampEntiteManager champEntiteManager){
      this.champEntiteManager = champEntiteManager;
   } 
   
   protected void setNomChampForControle(String nomColonneForControle){
      this.nomChampForControle = nomColonneForControle;
   }
   
   
   @Override
   public ChampEntite retrieveChampForCleFonctionnelle(Entite entite){
      return champEntiteManager.findCleFonctionelleForEntite(entite);
   }
   
   
   //va chercher l'objet ChampEntite correspondant à la colonne de contrôle définies par nomColonneForControle
   @Override
   public ChampEntite retrieveChampForContole(Entite entite){
      if(nomChampForControle != null) {
         return getChampEntiteManager().findByEntiteAndNomManager(entite, nomChampForControle).get(0);//la méthode findByEntiteAndNomManager renvoie une liste mais ne peut contenir en réalité qu'un élément
      }

      return null;
   }
   
   @Override
   abstract public List<CodeIdPair> findIdAndDataForControleByCodesAndBanque(List<String> listCodeForSelect, Banque banque, String nomChampForControle);
}
