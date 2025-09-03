/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.decorator.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
//NB : ne peut pas hériter de TKDecoratorFactory car gère un cas particulier lié à Gatsbi (modification du libellé de l'entité)
public class EntiteDecoratorFactory
{
   public static EntiteDecoratorForOneToManyComponent createEntiteDecoratorForOneToManyComponentByEEntiteId(EEntiteId eEntiteId) {
      return decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByIdManager(eEntiteId.getId()));
   }
   
   
   /**
    * Decore une liste d'Entites pour obtenir des EntiteForImportDecorator, classe fille de EntiteDecorator.
    * @param entites
    * @return Entites décorés.
    * @since 2.3.0-gatsbi, modifie Maladie -> Visite dans la décoration
    */
   public static List<EntiteDecoratorForOneToManyComponent> decorateListe(final Collection<Entite> entites, final boolean gatsbi){
      final List<EntiteDecoratorForOneToManyComponent> liste = new ArrayList<>();
      final Iterator<Entite> it = entites.iterator();
      while(it.hasNext()){
         liste.add(decorateForOneToManyComponent(it.next(), gatsbi));
      }
      return liste;
   }
   
   /** 
    * crée un EntiteDecoratorForOneToManyComponent à partir d'une entité
    * @param entite
    * @return
    */
   public static EntiteDecoratorForOneToManyComponent decorateForOneToManyComponent(Entite entite){
      return new EntiteDecoratorForOneToManyComponent(entite);
   }
   
   /** 
    * crée un EntiteDecoratorForOneToManyComponent à partir d'une entité avec l'attribut gatsbi valorisé
    */
   public static EntiteDecoratorForOneToManyComponent decorateForOneToManyComponent(Entite entite, boolean gatsbi){
      return new EntiteDecoratorForOneToManyComponent(entite, gatsbi);
   }
   
   /** 
    * crée un EntiteDecoratorForOneToManyComponent à partir d'une entité avec les attributs gatsbi et boutonDeleteVisible valorisés
    */
   public static EntiteDecoratorForOneToManyComponent decorateForOneToManyComponent(Entite entite, boolean gatsbi, boolean boutonDeleteVisible){
      return new EntiteDecoratorForOneToManyComponent(entite, gatsbi, boutonDeleteVisible);
   }
   
   /**
    * Extrait les Contrats d'une liste de Decorator.
    * @param Contrats
    * @return Contrats décorés.
    */
   public static List<Entite> undecorateListe(final List<EntiteDecoratorForOneToManyComponent> entites){
      final List<Entite> liste = new ArrayList<>();
      final Iterator<EntiteDecoratorForOneToManyComponent> it = entites.iterator();
      while(it.hasNext()){
         liste.add(it.next().getEntite());
      }
      return liste;
   }
   
   
}
