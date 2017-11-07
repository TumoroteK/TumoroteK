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
package fr.aphp.tumorotek.action.prelevement.serotk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche en édition d'un prélèvement.
 * Controller créé le 23/06/2010.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FichePrelevementEditSero extends FichePrelevementEdit {
	
	//private Log log = LogFactory.getLog(FichePrelevementEditSero.class);

	private static final long serialVersionUID = 2627927168895414292L;
	
	private Listbox protocolesBox;
	private List<Protocole> protocoles = new ArrayList<Protocole>();
	private Set<Listitem> selectedProtocoles = new HashSet<Listitem>();
	
	private PrelevementSero delegate = null;
		

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public PrelevementSero getDelegate() {
		return delegate;
	}

	public void setDelegate(PrelevementSero del) {
		this.delegate = del;
	}
	
	public List<Protocole> getProtocoles() {
		return protocoles;
	}

	public Set<Listitem> getSelectedProtocoles() {
		return selectedProtocoles;
	}

	public void setSelectedProtocoles(Set<Listitem> sR) {
		this.selectedProtocoles = sR;
	}
	
	@Override
	public void setObject(TKdataObject obj) {
		super.setObject(obj);
		
		delegate  = (PrelevementSero) getObject().getDelegate();
		if (delegate == null) {
			delegate = new PrelevementSero();
			delegate.setContexte(SessionUtils
				.getSelectedBanques(sessionScope).get(0).getContexte());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initLists() {
		super.initLists();	
		protocoles.addAll((List<Protocole>) ManagerLocator.getProtocoleManager()
				.findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
	}
	
	private Set<Protocole> findSelectedProtocoles() {
		Set<Protocole> rs = new HashSet<Protocole>();
		Iterator<Listitem> its = protocolesBox.getSelectedItems().iterator();
		while (its.hasNext()) {
			rs.add(protocoles.get(protocolesBox.getItems().indexOf(its.next())));
		}
		return rs;
	}
	
	/**
	 * Select les risques dans la dropdown list.
	 * @param protos liste à selectionner
	 */
	public void selectProtocoles(List<Protocole> protos) {
		
		selectedProtocoles.clear();
		
		if (protos != null) {
			for (int i = 0; i < protos.size(); i++) {
				if (protocoles.indexOf(protos.get(i)) >= 0) {
					selectedProtocoles
						.add(protocolesBox
							.getItemAtIndex(protocoles
										.indexOf(protos.get(i))));
				}
			}
			protocolesBox.setSelectedItems(selectedProtocoles);
			getBinder().loadAttribute(protocolesBox, "selectedItems");
		} else {
			getBinder().loadComponent(protocolesBox);
		}	
	}
	
	@Override
	public void initSelectedInLists() {
		super.initSelectedInLists();
		// sauf dans le cas ou create another prelevement
		List<Protocole> sels = new ArrayList<Protocole>();
		sels.addAll(getDelegate().getProtocoles());
		
		selectProtocoles(sels);
	}
	
	@Override
	protected void setEmptyToNulls() {
		super.setEmptyToNulls();
		
		delegate.getProtocoles().clear();
		delegate.getProtocoles().addAll(findSelectedProtocoles());
		if (delegate.isEmpty()) {
			getObject().setDelegate(null);
		} else if (delegate.getPrelevement() == null) { //create
			delegate.setPrelevement(getObject());
			getObject().setDelegate(delegate);
		}
	}
	
	@Override
	public void clearProtocoles() {
		selectedProtocoles.clear();
		getBinder().loadComponent(protocolesBox);
	}	
	
	/*************************************************************************/
	/************************** VALIDATION ***********************************/
	/*************************************************************************/
	public ConstWord getLibelleSeroConstraint() {
		return PrelevementSeroConstraints.getLibelleSeroConstraint();
	}
	
	
}
