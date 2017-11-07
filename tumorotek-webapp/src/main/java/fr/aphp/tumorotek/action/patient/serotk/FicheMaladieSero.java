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
package fr.aphp.tumorotek.action.patient.serotk;

import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller gérant la fiche maladie.
 * CONTEXTE SEROTK
 * Date 23/02/12
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class FicheMaladieSero extends FicheMaladie {
	
	private static final long serialVersionUID = 7781723391910786070L;
	
	private Label diagSeroLabel;
	private Listbox diagSeroBox;
	
	private LabelCodeItem selectedDiag;
	
	private MaladieSero delegate;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception { 
		
		// recoit le renderer en argument
		setPrelevementRenderer(new PrelevementSeroItemRenderer());
		
		super.doAfterCompose(comp);
		
		Component[] comps = new Component[getObjLabelsComponents().length + 1];
		for (int i = 0; i < getObjLabelsComponents().length; i++) {
			comps[i] = getObjLabelsComponents()[i];
		}
		comps[comps.length - 1] = diagSeroLabel;
		setObjLabelsComponents(comps);
		
		
		comps = new Component[getObjBoxsComponents().length + 1];
		for (int i = 0; i < getObjBoxsComponents().length; i++) {
			comps[i] = getObjBoxsComponents()[i];
		}
		comps[comps.length - 1] = diagSeroBox;
		setObjBoxsComponents(comps);
	}
	
	public MaladieSero getDelegate() {
		return delegate;
	}

	public void setDelegate(MaladieSero d) {
		this.delegate = d;
	}

	public void setSelectedDiag(LabelCodeItem s) {
		this.selectedDiag = s;
	}

	public LabelCodeItem getSelectedDiag() {
		return selectedDiag;
	}

	public List<LabelCodeItem> getDiagnostics() {
		return PatientUtils.getDiagsSero();
	}
	
	public String getDiagnosticFormatted() {
		return PatientUtils.setDiagSeroFromDBValue(getObject());
	}
	
	
	/**
	 * Setter appelé par fichePatient lors du dessin des panels.
	 * Cette méthode est donc appelée avec des maladies existantes et 
	 * une nouvelle maladie (empty) potentiellement.
	 * Récupère les prélèvements pour la banque courante et les prèlèvements
	 * pour une autre banque dans deux listes séparées.
	 * @param maladie Maladie backing bean du panel
	 */
	@Override
	public void setObject(TKdataObject mal) {
		super.setObject(mal);
		this.selectedDiag = setDiagItemFromDBValue(getObject());
		
		delegate = (MaladieSero) getObject().getDelegate();
		if (delegate == null) {
			delegate = new MaladieSero();
			delegate.setContexte(SessionUtils
					.getSelectedBanques(sessionScope).get(0).getContexte());
		}
		
	}

	@Override
	public void setEmptyToNulls() {
		super.setEmptyToNulls();
		
		delegate.setDiagnostic(null);
		if (this.selectedDiag != null) {
			delegate.setDiagnostic(this.selectedDiag.getCode());
		}

		if (delegate.isEmpty()) {
			getObject().setDelegate(null);
			delegate.setMaladie(null);
		} else if (delegate.getMaladie() == null) { //create
			delegate.setMaladie(getObject());
			getObject().setDelegate(delegate);
		}
	}
	
	/**
	 * Transforme la valeur 'diagnostic' maladie seroTK 
	 * récupérée de la base de données
	 * en un objet utilisable par l'interface.
	 * @param pat
	 * @return LabelCodeItem diagnostic seroTK
	 */
	public static LabelCodeItem setDiagItemFromDBValue(Maladie mal) {
		if (mal != null && mal.getDelegate() != null 
				&& ((MaladieSero) mal.getDelegate())
								.getDiagnostic() != null) {
			String diag = ((MaladieSero) mal.getDelegate())
													.getDiagnostic();
			if (diag.equals("C")) {
				return PatientUtils.DIAG_C;
			} else if (diag.equals("P")) {
				return PatientUtils.DIAG_P;
			} else if (diag.equals("S")) {
				return PatientUtils.DIAG_S;
			} 
		} 
		return PatientUtils.DIAG_EMPTY;
	}
}
