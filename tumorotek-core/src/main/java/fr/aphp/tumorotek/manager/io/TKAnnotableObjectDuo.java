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
package fr.aphp.tumorotek.manager.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Interface representant un duo d'objets TK annotables. 
 * Cette classe sert de base à la comparaison entre deux objets 
 * annotables (au préalable à un imports, une injection depuis interfacage).
 * Le premier objet est l'objet nouvellement peuplé avec les valeurs extérieures 
 * à injecter en base (ne peut donc être null)
 * Le deuxième objet est celui éventuellement présent en base avec les mêmes 
 * identifiants naturels (celui renvoyé par equals()).
 * Quel que soit le mode d'injection (import, interfacage), les identifiants 
 * naturels ne doivent pas être sujets à modification... sinon le système les 
 * considerera les objets que de nouveaux objets à créer. 
 * 
 * Classe créée le 12/06/2015.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 */
public class TKAnnotableObjectDuo {
	
	private Entite entite;
	private TKAnnotableObject firstObj;
	private TKAnnotableObject secondObj;
	private Map<ChampAnnotation, List<AnnotationValeur>> firstAnnoValsMap = 
			new HashMap<ChampAnnotation, List<AnnotationValeur>>();
	private Map<ChampAnnotation, List<AnnotationValeur>> secondAnnoValsMap = 
			new HashMap<ChampAnnotation, List<AnnotationValeur>>();
	private List<NonConformite> firstNoConfs = new ArrayList<NonConformite>();
	private List<NonConformite> secondNoConfs = new ArrayList<NonConformite>();
	
	public Entite getEntite() {
		return entite;
	}
	
	public TKAnnotableObject getFirstObj() {
		return firstObj;
	}
	
	public TKAnnotableObject getSecondObj() {
		return secondObj;
	}
	
	public void setEntite(Entite e) {
		this.entite = e;
	}
	
	public void setFirstObj(TKAnnotableObject f) {
		this.firstObj = f;
	}
	
	public void setSecondObj(TKAnnotableObject s) {
		this.secondObj = s;
	}

	public List<NonConformite> getFirstNoConfs() {
		return firstNoConfs;
	}

	public List<NonConformite> getSecondNoConfs() {
		return secondNoConfs;
	}

	public  Map<ChampAnnotation, List<AnnotationValeur>> getFirstAnnoValsMap() {
		return firstAnnoValsMap;
	}

	public  Map<ChampAnnotation, List<AnnotationValeur>> getSecondAnnoValsMap() {
		return secondAnnoValsMap;
	}
	
	public List<AnnotationValeur> getFirstAnnoVals() {
		List<AnnotationValeur> vals = new ArrayList<AnnotationValeur>();
		for (List<AnnotationValeur> annoAr : getFirstAnnoValsMap().values()) {
			vals.addAll(annoAr);
		}
		return vals;
	}
	
	public List<AnnotationValeur> getSecondAnnoVals() {
		List<AnnotationValeur> vals = new ArrayList<AnnotationValeur>();
		for (List<AnnotationValeur> annoAr : getSecondAnnoValsMap().values()) {
			vals.addAll(annoAr);
		}
		return vals;
	}
}
