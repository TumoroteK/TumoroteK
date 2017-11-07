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
package fr.aphp.tumorotek.action.interfacage.scan;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.interfacage.scan.ScanDevice;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;

/**
 * Date: 10/06/2016
 * @since 2.1
 * @version 2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class SelectScanModaleVM {
	
	private ScanDevice device;
	private Component main;
	private ListModelList<ScanTerminale> scansModel = new ListModelList<ScanTerminale>();
	
	private Boolean deleteOnSelect = true;
	
	@Wire
	private Window selectScanModale;
	
	@AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }
	
	@Init
	public void init(@ExecutionArgParam("ScanDevice") ScanDevice _d, 
			@ExecutionArgParam("scans") List<ScanTerminale> _s,
			@ExecutionArgParam("main") Component _m) {		
		scansModel.addAll(_s);
		// test only
		if (_d == null) {
			_d = new ScanDevice();
			_d.setName("local");
		}
		device = _d;
		main = _m;
	}
	
	@Command
	@NotifyChange("scans")
	public void onDeleteSelectedScan() {
		if (!scansModel.isSelectionEmpty()) {
			ScanTerminale selected = scansModel.getSelection().iterator().next();
			int idx = scansModel.indexOf(selected);
			
			ManagerLocator.getScanTerminaleManager().removeObjectManager(selected);
			
			scansModel.remove(selected);
			// select first
			scansModel.clearSelection();
			if (!scansModel.isEmpty() && idx < scansModel.size()) {
				scansModel.addToSelection(scansModel.get(idx));
			}
		}
	}
	
	@Command
	@NotifyChange("scans")
	public void onDeleteSelectedAllScans() {	
		
		for (ScanTerminale sT : scansModel) {
			ManagerLocator.getScanTerminaleManager().removeObjectManager(sT);
		}
		
		scansModel.clear();
		scansModel.clearSelection();
	}
	
	@Command
	public void onSelectScan() {	
		if (!scansModel.isSelectionEmpty()) {
			ScanTerminale selected = scansModel.getSelection().iterator().next();
			if (deleteOnSelect) {
				Events.postEvent("onSelectScanAndDelete", main, selected);
			} else {
				Events.postEvent("onSelectScan", main, selected);
			}
			Events.postEvent("onClose", selectScanModale, null);
		}
	}

	public ScanDevice getDevice() {
		return device;
	}

	public void setDevice(ScanDevice _d) {
		this.device = _d;
	}

	public List<ScanTerminale> getScansModel() {
		return scansModel;
	}

	public Boolean getDeleteOnSelect() {
		return deleteOnSelect;
	}

	public void setDeleteOnSelect(Boolean _d) {
		this.deleteOnSelect = _d;
	}
}