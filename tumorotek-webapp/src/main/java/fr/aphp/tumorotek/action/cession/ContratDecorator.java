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
package fr.aphp.tumorotek.action.cession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;

/**
 * Classe 'Decorateur' qui reprend les attributs de Contrat.
 * pour les afficher dans la liste associée.
 * Decorator créé le 03/02/2010.
 *
 * @version 2.0
 * @author Pierre Ventadour
 *
 */
public class ContratDecorator implements TKdataObject
{

   private Contrat contrat;

   public ContratDecorator(final Contrat c){
      this.contrat = c;
   }

   public Contrat getContrat(){
      return contrat;
   }

   public void setContrat(final Contrat c){
      this.contrat = c;
   }

   public String getNumero(){
      return this.contrat.getNumero();
   }

   public String getTitreProjet(){
      return this.contrat.getTitreProjet();
   }

   public String getDemandeur(){
      if(this.contrat.getCollaborateur() != null){
         return this.contrat.getCollaborateur().getNomAndPrenom();
      }
      return "";
   }

   public String getDateValidation(){
      return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateValidation());
   }

   public String getDateDemandeCessionFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateDemandeCession());
   }

   public String getDateSignatureFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateSignature());
   }

   public Integer getNbCessions(){
      if(this.contrat.getContratId() != null){
         return ManagerLocator.getContratManager().getCessionsManager(contrat).size();
      }
      return 0;
   }

   /**
    * Decore une liste de Contrats.
    * @param contrats
    * @return Contrats décorés.
    */
   public static List<ContratDecorator> decorateListe(final List<Contrat> contrats){
      final List<ContratDecorator> liste = new ArrayList<>();
      final Iterator<Contrat> it = contrats.iterator();
      while(it.hasNext()){
         liste.add(new ContratDecorator(it.next()));
      }
      return liste;
   }

   /**
    * Extrait les Contrats d'une liste de Decorator.
    * @param Contrats
    * @return Contrats décorés.
    */
   public static List<Contrat> extractListe(final List<ContratDecorator> contrats){
      final List<Contrat> liste = new ArrayList<>();
      final Iterator<ContratDecorator> it = contrats.iterator();
      while(it.hasNext()){
         liste.add(it.next().getContrat());
      }
      return liste;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ContratDecorator deco = (ContratDecorator) obj;
      return this.getContrat().equals(deco.getContrat());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashContrat = 0;

      if(this.contrat != null){
         hashContrat = this.contrat.hashCode();
      }

      hash = 7 * hash + hashContrat;

      return hash;
   }

   @Override
   public Integer listableObjectId(){
      return getContrat().getContratId();
   }

   @Override
   public ContratDecorator clone(){
      final ContratDecorator deco = new ContratDecorator(getContrat());
      return deco;
   }

}
