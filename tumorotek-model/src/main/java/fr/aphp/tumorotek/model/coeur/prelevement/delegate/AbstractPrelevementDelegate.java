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
package fr.aphp.tumorotek.model.coeur.prelevement.delegate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * Classe parente deleguee au prelevement qui permet par heritage d'associer
 * le bon delegate en fonction du contexte.
 * Date creation 19/01/12.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 * @see http://www.devx.com/Java/Article/33906
 */
@Entity
@Table(name = "PRELEVEMENT_DELEGATE")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPrelevementDelegate extends TKDelegateObject<Prelevement>
{

   private Integer prelevementDelegateId;
   private Prelevement delegator;

   public AbstractPrelevementDelegate(){}

   @Id
   @Column(name = "PRELEVEMENT_DELEGATE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPrelevementDelegateId(){
      return prelevementDelegateId;
   }

   public void setPrelevementDelegateId(final Integer prelevementDelegateId){
      this.prelevementDelegateId = prelevementDelegateId;
   }

   @Override
   @OneToOne(optional = false)
   @JoinColumn(name = "PRELEVEMENT_ID", unique = true)
   public Prelevement getDelegator(){
      return this.delegator;
   }

   @Override
   public void setDelegator(final Prelevement _d){
      this.delegator = _d;
   }

   @Transient
   @Override
   public boolean isEmpty(){
      return false;
   }
}