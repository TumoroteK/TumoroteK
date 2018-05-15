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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 * Classe 'Decorateur' qui reprend les attributs de Collaborateur.
 * pour les afficher par l'interface dans une liste de Collaborateur:
 *  - Titre Nom et Prenom
 *  - Initiales
 * date: 29/01/10
 *
 * @version 2.0
 * @author GOUSSEAU Maxime
 *
 */
public class CollaborateurDecorator
{

   private Collaborateur collaborateur;

   /**
    * Constructeur.
    * @param col
    */
   public CollaborateurDecorator(final Collaborateur col){
      this.collaborateur = col;
   }

   public void setCollaborateur(final Collaborateur col){
      this.collaborateur = col;
   }

   public Collaborateur getCollaborateur(){
      return this.collaborateur;
   }

   public String getNomPrenom(){
      
      StringBuilder sb = new StringBuilder();
      
      sb.append( this.collaborateur.getTitre() != null ? this.collaborateur.getTitre().getTitre() + " " : "" );
      sb.append( this.collaborateur.getNom().toUpperCase() );
      sb.append( this.collaborateur.getPrenom() != null ? " " + this.collaborateur.getPrenom() : "" );
      
      return sb.toString();
      
   }

   /**
    * Decore une liste de collaborateurs.
    * @param collaborateurs
    * @return collaborateurs d�cor�s.
    */
   public static List<CollaborateurDecorator> decorateListe(final List<Collaborateur> cols){
      final List<CollaborateurDecorator> liste = new ArrayList<>();
      final Iterator<Collaborateur> it = cols.iterator();
      while(it.hasNext()){
         liste.add(new CollaborateurDecorator(it.next()));
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

      final CollaborateurDecorator deco = (CollaborateurDecorator) obj;
      return this.getCollaborateur().equals(deco.getCollaborateur());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashCol = 0;

      if(this.collaborateur != null){
         hashCol = this.collaborateur.hashCode();
      }

      hash = 7 * hash + hashCol;

      return hash;
   }
}
