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
package fr.aphp.tumorotek.action.contexte;

import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
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
public final class ContexteConstraints {
	
	private ContexteConstraints() {
	}
	
	private static ConstWord banqueNomConstraint = new ConstWord();
	static {
		banqueNomConstraint.setNullable(false);
		banqueNomConstraint.setSize(100);
	}
	
	private static ConstWord identificationConstraint = new ConstWord();
	static {
		identificationConstraint.setNullable(true);
		identificationConstraint.setSize(50);
	}
	
	private static ConstText descrConstraint = new ConstText();
	static {
		descrConstraint.setNullable(true);
		descrConstraint.setSize(250);
	}
	
	private static ConstWord plateformeNomConstraint = new ConstWord();
	static {
		plateformeNomConstraint.setNullable(false);
		plateformeNomConstraint.setSize(50);
	}
	
	private static ConstCode plateformeAliasConstraint = new ConstCode();
	static {
		plateformeAliasConstraint.setNullable(true);
		plateformeAliasConstraint.setSize(5);
	}
	
	private static ConstCode finessConstraint = new ConstCode();
	static {
		finessConstraint.setNullable(true);
		finessConstraint.setSize(20);
	}
	
	private static ConstWord nomConstraint = new ConstWord();
	static {
		nomConstraint.setNullable(false);
		nomConstraint.setSize(100);
	}
	
	private static ConstWord nomCollabConstraint = new ConstWord();
	static {
		nomCollabConstraint.setNullable(false);
		nomCollabConstraint.setSize(30);
	}
	
	private static ConstWord prenomCollabConstraint = new ConstWord();
	static {
		prenomCollabConstraint.setNullable(true);
		prenomCollabConstraint.setSize(30);
	}
	
	private static ConstText addrConstraint = new ConstText();
	static {
		addrConstraint.setNullable(true);
		addrConstraint.setSize(250);
	}
	
	private static ConstWord villePaysConstraint = new ConstWord();
	static {
		villePaysConstraint.setNullable(true);
		villePaysConstraint.setSize(100);
	}
	
	private static ConstCode cpConstraint = new ConstCode();
	static {
		cpConstraint.setNullable(true);
		cpConstraint.setSize(10);
	}
	
	private static ConstCode tefFaxConstraint = new ConstCode();
	static {
		tefFaxConstraint.setNullable(true);
		tefFaxConstraint.setSize(15);
	}
	
	private static ConstEmail emailConstraint = new ConstEmail();
	static {
		emailConstraint.setNullable(true);
		emailConstraint.setSize(100);
	}
	
	private static ConstWord nomTransporteurConstraint = new ConstWord();
	static {
		nomTransporteurConstraint.setNullable(false);
		nomTransporteurConstraint.setSize(50);
	}
	
	private static ConstWord prenomTransporteurConstraint = new ConstWord();
	static {
		prenomTransporteurConstraint.setNullable(true);
		prenomTransporteurConstraint.setSize(50);
	}
	
	private static ConstCode codeMaladieConstraint = new ConstCode();
	static {
		codeMaladieConstraint.setNullable(true);
		codeMaladieConstraint.setSize(20);
	}
	
	public static ConstWord getBanqueNomConstraint() {
		return banqueNomConstraint;
	}

	public static ConstWord getIdentificationConstraint() {
		return identificationConstraint;
	}
	
	public static ConstText getDescrConstraint() {
		return descrConstraint;
	}

	public static ConstCode getFinessConstraint() {
		return finessConstraint;
	}
	
	public static ConstWord getNomConstraint() {
		return nomConstraint;
	}
	
	public static ConstWord getNomCollabConstraint() {
		return nomCollabConstraint;
	}
	
	public static ConstWord getPrenomCollabConstraint() {
		return prenomCollabConstraint;
	}
	
	public static ConstWord getVillePaysConstraint() {
		return villePaysConstraint;
	}

	public static ConstText getAddrConstraint() {
		return addrConstraint;
	}

	public static ConstCode getCpConstraint() {
		return cpConstraint;
	}

	public static ConstCode getTefFaxConstraint() {
		return tefFaxConstraint;
	}

	public static ConstEmail getEmailConstraint() {
		return emailConstraint;
	}

	public static ConstWord getNomTransporteurConstraint() {
		return nomTransporteurConstraint;
	}

	public static ConstWord getPrenomTransporteurConstraint() {
		return prenomTransporteurConstraint;
	}

	public static ConstWord getPlateformeNomConstraint() {
		return plateformeNomConstraint;
	}

	public static ConstCode getPlateformeAliasConstraint() {
		return plateformeAliasConstraint;
	}

	public static ConstCode getCodeMaladieConstraint() {
		return codeMaladieConstraint;
	}
	
}
