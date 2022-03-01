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
package fr.aphp.tumorotek.model.coeur.patient.serotk;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.contexte.Diagnostic;

/**
 * Classe deleguee à la maladie qui contient le mapping vers
 * les donnes propres aux serotheques.
 * Date creation 19/02/12.
 *
 * @author Mathieu BARTHELEMY
 * @since 2.0.6
 * @version 2.2.0
 * @see http://www.devx.com/Java/Article/33906
 */
@Entity
@Table(name = "MALADIE_SERO")
// @DiscriminatorValue("2")
public class MaladieSero extends AbstractMaladieDelegate
{

   private Diagnostic diagnostic;

   // Constructeur
   public MaladieSero(){}

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "DIAGNOSTIC_ID")
   public Diagnostic getDiagnostic(){
      return diagnostic;
   }

   public void setDiagnostic(final Diagnostic diagnostic){
      this.diagnostic = diagnostic;
   }

   @Override
   @Transient
   public boolean isEmpty(){
      return (getDiagnostic() == null);
   }
   
   @Override
	public TKDelegateObject<Maladie> clone() {
		MaladieSero clone = new MaladieSero();
		clone.setDiagnostic(getDiagnostic());
		return clone;
	}
   
	@Override
	public void removeAssociations() {	
	}
}
