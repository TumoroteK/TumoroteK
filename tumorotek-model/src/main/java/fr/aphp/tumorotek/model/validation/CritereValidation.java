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
package fr.aphp.tumorotek.model.validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.Champ;

/**
 * @author Gille Chapelot
 *
 */
@Entity
@Table(name = "CRITERE_VALIDATION")
public class CritereValidation
{

   private Integer id;
   
   private Champ champ;
   private OperateursComparaison operateur;
   private String valeurRef;
   private Champ champRef;

   public CritereValidation(){
      super();
   }

   /**
    * @return the id
    */
   @Id
   @Column(name = "CRITERE_VALIDATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getId(){
      return id;
   }

   /**
    * @param id the validationId to set
    */
   public void setId(Integer id){
      this.id = id;
   }

   /**
    * @return the champTeste
    */
   @OneToOne
   @JoinColumn(name = "CHAMP_ID", nullable=false)
   public Champ getChamp(){
      return champ;
   }

   /**
    * @param champTeste the champTeste to set
    */
   public void setChamp(Champ champ){
      this.champ = champ;
   }

   /**
    * @return the operateur
    */
   @Column(name = "OPERATEUR", nullable=false)
   @Enumerated(EnumType.STRING)
   public OperateursComparaison getOperateur(){
      return operateur;
   }

   /**
    * @param operateur the operateur to set
    */
   public void setOperateur(OperateursComparaison operateur){
      this.operateur = operateur;
   }

   /**
    * @return the valeurRef
    */
   @Column(name = "VALEUR_REF")
   public String getValeurRef(){
      return valeurRef;
   }

   /**
    * @param valeurRef the valeurRef to set
    */
   public void setValeurRef(String valeurRef){
      this.valeurRef = valeurRef;
   }

   /**
    * @return the champRef
    */
   @OneToOne
   @JoinColumn(name = "CHAMP_REF_ID")
   public Champ getChampRef(){
      return champRef;
   }

   /**
    * @param champRef the champRef to set
    */
   public void setChampRef(Champ champRef){
      this.champRef = champRef;
   }

   @Override
   public String toString() {
      
      String ref = champRef != null ? champRef.toString() : valeurRef;
      
      return "Critere = [" + champ + " " + operateur.getLabel() + " " + ref + "]";
      
   }
   
}
