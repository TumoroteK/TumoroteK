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
package fr.aphp.tumorotek.model.utilisateur;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Objet persistant mappant la table MESSAGE.
 *
 * Date: 17/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
@Entity
@Table(name = "MESSAGE")
@NamedQueries(
   value = {@NamedQuery(name = "Message.findByExpediteur", query = "SELECT m FROM Message m WHERE " + "m.expediteur = ?1"),
      @NamedQuery(name = "Message.findByDestinataire", query = "SELECT m FROM Message m WHERE " + "m.destinataire = ?1"),
      @NamedQuery(name = "Message.findDoublon", query = "SELECT m FROM Message m WHERE " + "m.objet = ?1 AND m.expediteur = ?2"
         + " AND m.destinataire = ?3" + " AND m.texte = ?4")})
public class Message implements Serializable
{

   private Integer messageId;
   private String objet;
   private String texte;
   private Integer importance;
   private Utilisateur destinataire;
   private Utilisateur expediteur;

   private static final long serialVersionUID = 1L;

   /** Constructeur par défaut. */
   public Message(){}

   @Id
   @Column(name = "MESSAGE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getMessageId(){
      return this.messageId;
   }

   public void setMessageId(final Integer id){
      this.messageId = id;
   }

   @Column(name = "OBJET", nullable = false, length = 100)
   public String getObjet(){
      return this.objet;
   }

   public void setObjet(final String obj){
      this.objet = obj;
   }

   @Column(name = "TEXTE", nullable = true)
   public String getTexte(){
      return this.texte;
   }

   public void setTexte(final String tex){
      this.texte = tex;
   }

   @Column(name = "IMPORTANCE", nullable = true)
   public Integer getImportance(){
      return this.importance;
   }

   public void setImportance(final Integer imp){
      this.importance = imp;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "DESTINATAIRE_ID", nullable = false)
   public Utilisateur getDestinataire(){
      return this.destinataire;
   }

   public void setDestinataire(final Utilisateur dest){
      this.destinataire = dest;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "EXPEDITEUR_ID", nullable = false)
   public Utilisateur getExpediteur(){
      return this.expediteur;
   }

   public void setExpediteur(final Utilisateur exp){
      this.expediteur = exp;
   }

   /**
    * Deux messages sont considerees egaux s'ils ont le
    * meme objet, le meme texte, le meme destinataire
    * et envoyes par le meme utilisateur. 
    * @param obj est le message à tester.
    * @return true si les messages sont egaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final Message test = (Message) obj;
      if(this.messageId != null){ //l'objet a id assigne
         return this.messageId.equals(test.messageId);
      }else{ //l'objet a ete nouvellement cree
         return ((this.objet == test.objet || (this.objet != null && this.objet.equals(test.objet)))
            && (this.expediteur == test.expediteur || (this.expediteur != null && this.expediteur.equals(test.expediteur)))
            && (this.destinataire == test.destinataire
               || (this.destinataire != null && this.destinataire.equals(test.destinataire)))
            && (this.texte == test.texte || (this.texte != null && this.texte.equals(test.texte))));
      }
   }

   /**
    * Le hashcode est calculé sur le contenu, l'objet
    * l'expediteur et le destinataire du message.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashObjet = 0;
      int hashExpediteur = 0;
      int hashDestinataire = 0;
      int hashTexte = 0;

      if(this.objet != null){
         hashObjet = this.objet.hashCode();
      }
      if(this.expediteur != null){
         hashExpediteur = this.expediteur.hashCode();
      }
      if(this.destinataire != null){
         hashDestinataire = this.destinataire.hashCode();
      }
      if(this.texte != null){
         hashTexte = this.texte.hashCode();
      }

      hash = 31 * hash + hashObjet;
      hash = 31 * hash + hashExpediteur;
      hash = 31 * hash + hashDestinataire;
      hash = 31 * hash + hashTexte;

      return hash;

   }

}
