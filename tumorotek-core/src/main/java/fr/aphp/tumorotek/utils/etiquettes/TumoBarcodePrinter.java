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
package fr.aphp.tumorotek.utils.etiquettes;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.Imprimante;

public class TumoBarcodePrinter {

	private Banque banqueDb;
	private Imprimante imprimante;
	private String texte;
	
	/** 
     * Constructeur value d une imprimante d etiquettes code a barre pour le module tumo
	 * @param _banqueDb
	 */
	public TumoBarcodePrinter(Banque bDb, Imprimante ieBean, String txt){
		banqueDb = bDb;
		imprimante = ieBean;
		texte = txt;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste de patients. 
	 */
	public int printPatient(List<Patient> patients, int nb) {
		return 0;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste de prelevements. 
	 */
	public int printPrelevement(List<Prelevement> prelevements, int nb) {
		return 0;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste d'echantillons. 
     * @param List la liste des echantillons a imprimer
     * @param int nb le nombre d impressions a effectuer par echantillon
     * @return int le code retour pour savoir si l impression s est correctement deroulee
     *         si codeRetour = 0, l impression s est mal passee
     *         si codeRetour = 1, l impression s est bien passee
	 */
	public int printEchantillon(List<Echantillon> echantillons, int nb) {
		return 0;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste de derives. 
	 */
	public int printDerive(List<ProdDerive> derives, int nb) {
		
		return 0;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste de cessions. 
	 */
	public int printCession(List cessions, int nb) {
		return 0;
	}
	
	/**
	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une liste de (6) parametres. 
	 */
	public int printData(Vector<String> data, int nb) {
		int codeRetour = 0;
		
		List<Vector<String>> datas = new ArrayList<Vector<String>>();
		datas.add(data);

		codeRetour = ComponentPrinter.print(datas, nb, imprimante, texte);
		
		datas = null;
		
		return codeRetour;
	}
	
}
