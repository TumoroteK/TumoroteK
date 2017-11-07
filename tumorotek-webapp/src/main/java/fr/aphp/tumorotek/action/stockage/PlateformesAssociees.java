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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateformePK;

public class PlateformesAssociees extends OneToManyComponent {

	private static final long serialVersionUID = 1L;
	
	private List<ConteneurPlateforme> objects = new ArrayList<ConteneurPlateforme>();
	private Conteneur conteneur;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		objLinkLabel = new Label();		
		super.doAfterCompose(comp);
	}
	
	@Override
	public List<ConteneurPlateforme> getObjects() {
		return this.objects;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setObjects(List< ? extends Object> objs) {
		this.objects = (List<ConteneurPlateforme>) objs;
		updateComponent();
	}
	
	@Override
	public void addToListObjects(Object obj) {
		ConteneurPlateforme cp = new ConteneurPlateforme();
		ConteneurPlateformePK cpk = new ConteneurPlateformePK(getConteneur(), (Plateforme) obj);
		cp.setPk(cpk);
		getObjects().add(cp);
	}
	
	@Override
	public void removeFromListObjects(Object obj) {
		getObjects().remove((ConteneurPlateforme) obj);
	}
	
	@Override
	public String getGroupHeaderValue() {
		StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("conteneur.plateformes.accessibles"));
		sb.append(" (");
		sb.append(getObjects().size());
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public List< ? extends Object> findObjectsAddable() {
		// Banques ajoutables
		List<Plateforme> pfs = ManagerLocator
			.getPlateformeManager().findAllObjectsManager();
		if (conteneur != null) {
			pfs.remove(conteneur.getPlateformeOrig());
		}
		// retire les ConteneurPlateformes deja assignées
		for (int i = 0; i < getObjects().size(); i++) {
			pfs.remove(getObjects().get(i).getPlateforme());
		}
		return pfs;
	}

	@Override
	public void drawActionForComponent() {		
	}

	@Override
	public void onClick$objLinkLabel(Event event) {	
	}
	
	/**
	 * Vérifies qu'aucun référencement sur ce conteneur, impliquant un 
	 * probable stockage de matériel, n'a été établi à partir de la 
	 * ConteneurPlateforme.	 
	 **/
	@Override
	public void onClick$deleteImage(Event event) {
		
		fr.aphp.tumorotek.model.stockage.ConteneurPlateforme cur = (ConteneurPlateforme) AbstractListeController2
				.getBindingData((ForwardEvent) event, false); 
		
		if (ManagerLocator.getConteneurManager()
				.findByPartageManager(cur.getPlateforme(), true)
				.contains(cur.getConteneur())) {
			Messagebox.show(Labels
				.getLabel("conteneur.plateforme.remove.error"), 
					Labels.getLabel("general.warning"), 
					Messagebox.OK, Messagebox.ERROR);
		} else {
			super.onClick$deleteImage(event);
		}
	}
	
	/**
	 * Surcharge pour ne pas appliquer clear sur la liste.
	 */
	@Override
	public void switchToCreateMode() {
		getBinder().loadComponent(objectsList);
		switchToEditMode(true);
	}
	
	/**
	 * Surcharge pour imposer que la dernière pf ne puisse jamais
	 * être éffacée laissant une table orpheline.
	 */
	@Override
	public void switchToEditMode(boolean b) {
		super.switchToEditMode(b);
		// deleteHeader.setVisible(getObjects().size() > 1);
	}
	
	public List<Plateforme> getPlateformes() {
		List<Plateforme> pfs = new ArrayList<Plateforme>();
		
		for (ConteneurPlateforme cp : getObjects()) {
			pfs.add(cp.getPlateforme());
		}
		
		return pfs;
	}

	public Conteneur getConteneur() {
		return conteneur;
	}

	public void setConteneur(Conteneur c) {
		this.conteneur = c;
	}
}
