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

import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstDateLimit;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;

/**
 * Utility class fournissant les contraintes qui seront appliquées dans 
 * l'interface par zk.
 * Date: 26/07/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public final class CessionConstraints {
	
	private CessionConstraints() {
	}
	
	private static ConstCode numeroCessionConstraint = new ConstCode();
	static {
		numeroCessionConstraint.setNullable(false);
		numeroCessionConstraint.setSize(100);
	}
	
	public static ConstCode getNumeroCessionConstraint() {
		return numeroCessionConstraint;
	}
	
	private static ConstCode numeroConstraint = new ConstCode();
	static {
		numeroConstraint.setNullable(false);
		numeroConstraint.setSize(50);
	}
	
	public static ConstCode getNumeroConstraint() {
		return numeroConstraint;
	}
	
	private static ConstWord titreProjetConstraint = new ConstWord();
	static {
		titreProjetConstraint.setNullable(true);
		titreProjetConstraint.setSize(50);
	}
	
	public static ConstWord getTitreProjetConstraint() {
		return titreProjetConstraint;
	}
	
	private static ConstText descrConstraint = new ConstText();
	static {
		descrConstraint.setNullable(true);
	}
	
	public static ConstText getDescrConstraint() {
		return descrConstraint;
	}
	
	private static ConstWord titreEtudeConstraint = new ConstWord();
	static {
		titreEtudeConstraint.setNullable(true);
		titreEtudeConstraint.setSize(100);
	}
	
	public static ConstWord getTitreEtudeConstraint() {
		return titreEtudeConstraint;
	}
	
	private static ConstWord cessionDescrConstraint = new ConstWord();
	static {
		cessionDescrConstraint.setNullable(true);
		cessionDescrConstraint.setSize(65535);
	}
	
	public static ConstText getCessionDescrConstraint() {
		return descrConstraint;
	}
	
	private static ConstDateLimit dateConstraint = new ConstDateLimit();
	static {
		dateConstraint.setNullable(true);
	}
	
	public static ConstDateLimit getDateConstraint() {
		return dateConstraint;
	}
}
