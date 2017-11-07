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

import java.util.Iterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 * 
 * Controller gérant la fiche static d'un prélèvement.
 * CONTEXTE SEROTK
 * Controller créé le 19/01/2012.
 * 
 * @author mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class FichePrelevementStaticSero extends fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic {
	
	//private Log log = LogFactory.getLog(FichePrelevementStaticSero.class);
	
	private static final long serialVersionUID = -7612780578022559022L;
	
	protected Vbox protocolesBox;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public String getComplementDiagnostic() {
		String label = "";
		if (getObject() != null && getObject().getDelegate() != null) {
			if (((PrelevementSero) getObject()
								.getDelegate()).getLibelle() != null) {
				label = ((PrelevementSero) getObject()
										.getDelegate()).getLibelle();
			}
		}
		return label;
	}
	
	@Override
	public void setObject(TKdataObject p) {
		super.setObject(p);
		drawProtocolesFormatted();
	}
	
	/**
	 * Dessine les protocoles associés au prélèvement.
	 */
	private void drawProtocolesFormatted() {
		Components.removeAllChildren(protocolesBox);
		if (getObject().getPrelevementId() != null
			&& getObject().getDelegate() != null) {
			Iterator<Protocole> protosIt = 
				((PrelevementSero) getObject().getDelegate())
								.getProtocoles().iterator();
			Protocole pt;
			Label lab;
			while (protosIt.hasNext()) {
				pt = protosIt.next();
				lab = new Label(pt.getNom());
				lab.setSclass("formValue");
				protocolesBox.appendChild(lab);
			}
		}
	}

	
}
