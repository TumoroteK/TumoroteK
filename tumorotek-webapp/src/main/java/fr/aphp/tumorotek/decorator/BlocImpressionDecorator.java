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
import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampEntiteBloc;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

public class BlocImpressionDecorator {
	
	/** Objets principaux. **/
	private Template template;
	private BlocImpression blocImpression;
	private TableAnnotation tableAnnotation;
	private List<ChampEntite> champEntites = new ArrayList<ChampEntite>();
	
	private Boolean imprimer = true;
	private String description = "";
	
	public BlocImpressionDecorator(BlocImpression bloc,
			TableAnnotation table, Template temp) {
		this.blocImpression = bloc;
		this.tableAnnotation = table;
		this.template = temp;
		
		if (this.blocImpression != null) {
			generateContenuForBlocInConsulation();
		} else if (tableAnnotation != null) {
			generateContenuForAnnotationInConsulation();
		}
	}
	
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template t) {
		this.template = t;
	}
	public BlocImpression getBlocImpression() {
		return blocImpression;
	}
	public void setBlocImpression(BlocImpression bImpression) {
		this.blocImpression = bImpression;
	}
	public TableAnnotation getTableAnnotation() {
		return tableAnnotation;
	}
	public void setTableAnnotation(TableAnnotation tAnnotation) {
		this.tableAnnotation = tAnnotation;
	}
	public Boolean getImprimer() {
		return imprimer;
	}
	public void setImprimer(Boolean imp) {
		this.imprimer = imp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public String getNomForBloc() {
		StringBuffer nom = new StringBuffer();
		
		if (blocImpression != null) {
			nom.append(Labels.getLabel(blocImpression.getNom()));
			if (blocImpression.getIsListe()) {
				nom.append(" (");
				nom.append(Labels.getLabel("blocImpression.isListe"));
				nom.append(")");
			}
		} else if (tableAnnotation != null) {
			nom.append(tableAnnotation.getNom());
			nom.append(" (");
			nom.append(Labels.getLabel("blocImpression.annotation"));
			nom.append(")");
		}
		
		return nom.toString();
	}

	/**
	 * Méthode générant le contenu d'un bloc.
	 * @param bloc.
	 * @return Le contenu du bloc.
	 */
	public void generateContenuForBlocInConsulation() {
		StringBuffer sb = new StringBuffer();
		List<ChampEntite> champs = new ArrayList<ChampEntite>();
		
		// si c'est une liste en mode consulation
		if (blocImpression.getIsListe()
				&& template.getTemplateId() != null) {
			champEntites = new ArrayList<ChampEntite>();
			// on récupère les champsimprimes
			List<ChampImprime> champsI = ManagerLocator.getChampImprimeManager()
				.findByTemplateAndBlocManager(template, blocImpression);

			for (int i = 0; i < champsI.size(); i++) {
				champs.add(champsI.get(i).getChampEntite());
				champEntites.add(champsI.get(i).getChampEntite());
			}
		} else {
			// on récupère les champs contenus dans le bloc
			champEntites = new ArrayList<ChampEntite>();
			List<ChampEntiteBloc> cebs = ManagerLocator
				.getChampEntiteBlocManager().findByBlocManager(blocImpression);
			for (int i = 0; i < cebs.size(); i++) {
				champs.add(cebs.get(i).getChampEntite());
				champEntites.add(cebs.get(i).getChampEntite());
			}
		}
		
		Iterator<ChampEntite> it = champs.iterator();
		int i = 0;
		while (it.hasNext()) {
			++i;
			// pour chaque champ on va construire le code contenu dans
			// le fichier i3-label.properties
			ChampEntite champ = it.next();
			StringBuffer iProperty = new StringBuffer();
			iProperty.append("Champ.");
			iProperty.append(champ.getEntite().getNom());
			iProperty.append(".");
			
			String champOk = "";
			// si le nom du champ finit par "Id", on le retire
			if (champ.getNom().endsWith("Id")) {
				champOk = champ.getNom().substring(
						0, champ.getNom().length() - 2);
			} else {
				champOk = champ.getNom();
			}
			iProperty.append(champOk);
			
			// on ajoute la valeur du champ
			sb.append(Labels.getLabel(iProperty.toString()));
			if (i < champs.size()) {
				sb.append(", ");
			} else {
				sb.append(".");
			}
		}
		
		description = sb.toString();
	}
	
	/**
	 * Méthode générant le contenu d'un bloc.
	 * @param bloc.
	 * @return Le contenu du bloc.
	 */
	public void generateContenuForAnnotationInConsulation() {
		StringBuffer sb = new StringBuffer();
		
		Set<ChampAnnotation> champs = ManagerLocator
			.getTableAnnotationManager()
			.getChampAnnotationsManager(tableAnnotation);
		
		Iterator<ChampAnnotation> it = champs.iterator();
		int i = 0;
		while (it.hasNext()) {
			++i;
			sb.append(it.next().getNom());
			
			if (i < champs.size()) {
				sb.append(", ");
			} else {
				sb.append(".");
			}
		}
		
		description = sb.toString();
	}
	
	/**
	 * Mets à jour la description du bloc en se basant sur la liste des
	 * champs entités.
	 */
	public void updateListeChamps() {
		StringBuffer sb = new StringBuffer();
		if (champEntites != null) {
			int j = 0;
			for (int i = 0; i < champEntites.size(); i++) {
				++j;
				// pour chaque champ on va construire le code contenu dans
				// le fichier i3-label.properties
				ChampEntite champ = champEntites.get(i);
				StringBuffer iProperty = new StringBuffer();
				iProperty.append("Champ.");
				iProperty.append(champ.getEntite().getNom());
				iProperty.append(".");
				
				String champOk = "";
				// si le nom du champ finit par "Id", on le retire
				if (champ.getNom().endsWith("Id")) {
					champOk = champ.getNom().substring(
							0, champ.getNom().length() - 2);
				} else {
					champOk = champ.getNom();
				}
				iProperty.append(champOk);
				
				// on ajoute la valeur du champ
				sb.append(Labels.getLabel(iProperty.toString()));
				if (j < champEntites.size()) {
					sb.append(", ");
				} else {
					sb.append(".");
				}
			}
			
			description = sb.toString();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		BlocImpressionDecorator test = (BlocImpressionDecorator) obj;
		return ((this.template == test.template || (this.template != null 
				&& this.template.equals(test.template))) 
				&& (this.blocImpression == test.blocImpression 
						|| (this.blocImpression != null 
						&& this.blocImpression.equals(test.blocImpression)))
				&& (this.tableAnnotation == test.tableAnnotation 
						|| (this.tableAnnotation != null 
						&& this.tableAnnotation.equals(test.tableAnnotation))));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		int hashTemplate = 0;
		int hashBloc = 0;
		int hashTable = 0;
		
		if (this.template != null) {
			hashTemplate = this.template.hashCode();
		}
		if (this.blocImpression != null) {
			hashBloc = this.blocImpression.hashCode();
		}
		if (this.tableAnnotation != null) {
			hashTable = this.tableAnnotation.hashCode();
		}
		
		hash = 7 * hash + hashTemplate;
		hash = 7 * hash + hashBloc;
		hash = 7 * hash + hashTable;
		
		return hash;
	}

	public List<ChampEntite> getChampEntites() {
		return champEntites;
	}

	public void setChampEntites(List<ChampEntite> champE) {
		this.champEntites = champE;
	}

}
