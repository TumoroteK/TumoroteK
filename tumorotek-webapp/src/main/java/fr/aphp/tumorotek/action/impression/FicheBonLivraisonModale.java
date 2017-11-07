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
package fr.aphp.tumorotek.action.impression;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.BlocPrincipal;
import fr.aphp.tumorotek.manager.impl.xml.CoupleAccordValeur;
import fr.aphp.tumorotek.manager.impl.xml.LigneAccord;
import fr.aphp.tumorotek.manager.impl.xml.ListeSignature;
import fr.aphp.tumorotek.manager.impl.xml.Signatures;
import fr.aphp.tumorotek.manager.impl.xml.ValeursSignatures;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Service;

public class FicheBonLivraisonModale extends AbstractFicheCombineController {
	
	private Log log = LogFactory.getLog(FicheBonLivraisonModale.class);

	private static final long serialVersionUID = -8704338228969541559L;
	
	private String entete;
	private String piedPage;
	private Cession cession;
	private Collaborateur expediteur;
	private Collaborateur destinataire;
	private List<Collaborateur> collabs = new ArrayList<Collaborateur>();
	private String langue = "FR";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// reference vers des boutons non affichés
		editC = new Button();
		validateC = new Button();
		createC = new Button();
		revertC = new Button();
		deleteC = new Button();	
		cancelC = new Button();
		addNewC = new Button();
				
		super.doAfterCompose(comp);
		
		if (winPanel != null) {
			winPanel.setHeight("555px");
		}
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
		});
		
		setObjBoxsComponents(new Component[]{
		});
		
		setRequiredMarks(new Component[]{
		});	
		
		getBinder().loadAll();
	}
	
	/**
	 * Méthode intialisant le composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 */
	public void init(Cession cessionToPrint) {
			// List<CederObjetDecorator> echans,
			// List<CederObjetDecorator> derives) {
		this.cession = cessionToPrint;
		destinataire = cession.getDestinataire();
		expediteur = cession.getBanque().getPlateforme().getCollaborateur();
		collabs = ManagerLocator.getCollaborateurManager()
			.findAllObjectsWithOrderManager();
		collabs.add(0, null);
		// echantillonsCedesDecores = echans;
		// derivesCedesDecores = derives;
		getBinder().loadComponent(self);
	}
	
	public void onOK() {
		onClick$print();
	}
	
	public void onClick$print() {
		Clients.showBusy(Labels.getLabel("impression.encours"));

		Locale loc = null;
		if (langue.equals("EN")) {
			loc = Locale.ENGLISH;
		}
		
		// création du document XML contenant les données à imprimer
		Document document = createDocumentXMLForAccord(loc);
		
		// Transformation du document en fichier
		byte[] dl = null;
		try {
			dl = ManagerLocator.getXmlUtils()
				.creerAccordTransfertPdf(document);
			
		} catch (Exception e) {
			log.error(e);
		}
		
		// ferme wait message
		Clients.clearBusy();
		
		// génération du nom du fichier
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		String date = new SimpleDateFormat("yyyyMMddHHmm")
			.format(cal.getTime());
		sb.append("bon_livraison_");
		sb.append(date);
		sb.append(".pdf");
		
		// envoie du fichier à imprimer à l'utilisateur
		if (dl != null) {
			Filedownload.save(dl, "application/pdf", sb.toString());
			dl = null;
		}
		
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	/**
	 * Appelle la bonne méthode de création du document en fonction
	 * du type d'objet à imprimer.
	 */
	public Document createDocumentXMLForAccord(Locale locale) {
		
		Document document = null;
		Locale oldLocale = null;
		if (locale != null) { // change temporarily locale
			oldLocale = Locales.setThreadLocal(locale);
		}

		try {
			document = ManagerLocator.getXmlUtils()
				.createJDomAccordTranfert();
			Element root = document.getRootElement();
			
			// ajout de la date en pied de page
			StringBuilder sb = new StringBuilder();
			if (piedPage != null
					&& piedPage.equals("")) {
				sb.append(piedPage);
				sb.append(" - ");
			}
			Calendar cal = Calendar.getInstance();
			String dateFoot = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			sb.append(dateFoot);
			ManagerLocator.getXmlUtils().addBasDePage(root, piedPage);
			ManagerLocator.getXmlUtils().addHautDePage(root, entete,
					false, null);

			//Labels.getLabel("impression.accord.transfert.titre")
			Element page = ManagerLocator.getXmlUtils().addPage(root, 
					ObjectTypesFormatters.getLabel(
							"impression.accord.transfert.titre", 
									new String[]{cession.getNumero()}));
			
			// Etablissement destinataire
			StringBuilder etab = new StringBuilder();
			StringBuilder adresse = new StringBuilder();
			if (destinataire != null 
					&& destinataire.getEtablissement() != null) {
				etab.append(destinataire.getEtablissement().getNom());
				if (destinataire.getEtablissement()
						.getCoordonnee() != null) {
					if (destinataire.getEtablissement()
						.getCoordonnee().getAdresse() != null) {
						adresse.append(destinataire.getEtablissement()
								.getCoordonnee().getAdresse());
					} else {
						adresse.append("");
					}
					adresse.append(" - ");
					if (destinataire.getEtablissement()
						.getCoordonnee().getCp() != null) {
						adresse.append(destinataire.getEtablissement()
								.getCoordonnee().getCp());
					} else {
						adresse.append("");
					}
					adresse.append(" ");
					if (destinataire.getEtablissement()
						.getCoordonnee().getVille() != null) {
						adresse.append(destinataire.getEtablissement()
								.getCoordonnee().getVille());
					} else {
						adresse.append("");
					}
				}
			}
			CoupleAccordValeur cv1 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.organisation"), 
					new String[]{etab.toString(), adresse.toString()});
			LigneAccord li1 = new LigneAccord(
					new CoupleAccordValeur[]{cv1});
			
			// services
			StringBuilder sbServDest = new StringBuilder();
			if (destinataire != null) {
				Iterator<Service> it = ManagerLocator
					.getCollaborateurManager()
					.getServicesManager(destinataire).iterator();
				
				while (it.hasNext()) {
					Service serv = it.next();
					sbServDest.append(serv.getNom());
					if (it.hasNext()) {
						sbServDest.append(", ");
					}
				}
			}
			CoupleAccordValeur cv1Bis = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.services"), 
					new String[]{sbServDest.toString()});
			LigneAccord li1Bis = new LigneAccord(
					new CoupleAccordValeur[]{cv1Bis});
			
			// destinataire
			StringBuilder dest = new StringBuilder();
			if (destinataire != null) {
				if (destinataire.getTitre() != null) {
					dest.append(destinataire.getTitre().getTitre());
					dest.append(" ");
				}
				dest.append(destinataire.getNomAndPrenom());
			}
			CoupleAccordValeur cv2 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.nom.titre"), 
					new String[]{dest.toString()});
			LigneAccord li2 = new LigneAccord(
					new CoupleAccordValeur[]{cv2});
			// tel
			StringBuilder tel = new StringBuilder();
			StringBuilder fax = new StringBuilder();
			StringBuilder mail = new StringBuilder();
			if (destinataire != null) {
				Iterator<Coordonnee> it = ManagerLocator.getCollaborateurManager()
					.getCoordonneesManager(destinataire).iterator();
				
				while (it.hasNext()) {
					Coordonnee coord = it.next();
					if (coord.getTel() != null) {
						tel.append(coord.getTel());
						if (it.hasNext()) {
							tel.append(", ");
						}
					}
					if (coord.getFax() != null) {
						fax.append(coord.getFax());
						if (it.hasNext()) {
							fax.append(", ");
						}
					}
					if (coord.getMail() != null) {
						mail.append(coord.getMail());
						if (it.hasNext()) {
							mail.append(", ");
						}
					}
				}
			}
			CoupleAccordValeur cv3 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.telephone"), 
					new String[]{tel.toString(), fax.toString(), mail.toString()});
			LigneAccord li3 = new LigneAccord(
					new CoupleAccordValeur[]{cv3});
			BlocPrincipal bloc1 = new BlocPrincipal(
					Labels.getLabel("impression.accord.transfert.destinataire"), 
					new LigneAccord[]{li1, li1Bis, li2, li3});
			
			
			// Etablissement expéditeur
			etab = new StringBuilder();
			adresse = new StringBuilder();
			if (expediteur != null 
					&& expediteur.getEtablissement() != null) {
				etab.append(expediteur.getEtablissement().getNom());
				if (expediteur.getEtablissement()
						.getCoordonnee() != null) {
					if (expediteur.getEtablissement()
						.getCoordonnee().getAdresse() != null) {
						adresse.append(expediteur.getEtablissement()
							.getCoordonnee().getAdresse());
					} else {
						adresse.append("");
					}
					adresse.append(" - ");
					if (expediteur.getEtablissement()
						.getCoordonnee().getCp() != null) {
						adresse.append(expediteur.getEtablissement()
								.getCoordonnee().getCp());
					} else {
						adresse.append("");
					}
					adresse.append(" ");
					if (expediteur.getEtablissement()
						.getCoordonnee().getVille() != null) {
						adresse.append(expediteur.getEtablissement()
								.getCoordonnee().getVille());
					} else {
						adresse.append("");
					}
				}
			}
			CoupleAccordValeur cv4 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.organisation"), 
					new String[]{etab.toString(), adresse.toString()});
			LigneAccord li4 = new LigneAccord(
					new CoupleAccordValeur[]{cv4});
			// services
			StringBuilder sbExpDest = new StringBuilder();
			if (expediteur != null) {
				Iterator<Service> it = ManagerLocator
					.getCollaborateurManager()
					.getServicesManager(expediteur).iterator();
				
				while (it.hasNext()) {
					Service serv = it.next();
					sbExpDest.append(serv.getNom());
					if (it.hasNext()) {
						sbExpDest.append(", ");
					}
				}
			}
			CoupleAccordValeur cv4Bis = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.services"), 
					new String[]{sbExpDest.toString()});
			LigneAccord li4Bis = new LigneAccord(
					new CoupleAccordValeur[]{cv4Bis});
			// destinataire
			dest = new StringBuilder();
			if (expediteur != null) {
				if (expediteur.getTitre() != null) {
					dest.append(expediteur.getTitre().getTitre());
					dest.append(" ");
				}
				dest.append(expediteur.getNomAndPrenom());
			}
			CoupleAccordValeur cv5 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.nom.titre"), 
					new String[]{dest.toString()});
			LigneAccord li5 = new LigneAccord(
					new CoupleAccordValeur[]{cv5});
			// tel
			tel = new StringBuilder();
			fax = new StringBuilder();
			mail = new StringBuilder();
			if (expediteur != null) {
				Iterator<Coordonnee> it = ManagerLocator.getCollaborateurManager()
					.getCoordonneesManager(expediteur).iterator();
				
				while (it.hasNext()) {
					Coordonnee coord = it.next();
					if (coord.getTel() != null) {
						tel.append(coord.getTel());
						if (it.hasNext()) {
							tel.append(", ");
						}
					}
					if (coord.getFax() != null) {
						fax.append(coord.getFax());
						if (it.hasNext()) {
							fax.append(", ");
						}
					}
					if (coord.getMail() != null) {
						mail.append(coord.getMail());
						if (it.hasNext()) {
							mail.append(", ");
						}
					}
				}
			}
			CoupleAccordValeur cv6 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.telephone"), 
					new String[]{tel.toString(), fax.toString(), mail.toString()});
			LigneAccord li6 = new LigneAccord(
					new CoupleAccordValeur[]{cv6});
			BlocPrincipal bloc2 = new BlocPrincipal(
					Labels.getLabel("impression.accord.transfert.expediteur"), 
					new LigneAccord[]{li4, li4Bis, li5, li6});
			
			Map<String, Number> nbNatures = ManagerLocator.getCessionManager()
					.getTypesAndCountsManager(getCession());
//		
//		for (int i = 0; i < echantillonsCedesDecores.size(); i++) {
//			Echantillon echan = echantillonsCedesDecores.get(i)
//				.getEchantillon();
//			if (!natures.contains(echan.getEchantillonType().getType())) {
//				natures.add(echan.getEchantillonType().getType());
//				nbNatures.put(echan.getEchantillonType().getType(), 1);
//			} else {
//				Integer nb = nbNatures.get(echan.getEchantillonType()
//						.getType());
//				++nb;
//				nbNatures.put(echan.getEchantillonType().getType(), nb);
//			}
//		}
//		
//		for (int i = 0; i < derivesCedesDecores.size(); i++) {
//			ProdDerive derive = derivesCedesDecores.get(i)
//				.getProdDerive();
//			if (!natures.contains(derive.getProdType().getType())) {
//				natures.add(derive.getProdType().getType());
//				nbNatures.put(derive.getProdType().getType(), 1);
//			} else {
//				Integer nb = nbNatures.get(derive.getProdType().getType());
//				++nb;
//				nbNatures.put(derive.getProdType().getType(), nb);
//			}
//		}
			
			// Nature
			StringBuilder nature = new StringBuilder();
			StringBuilder nbNat = new StringBuilder();
			Iterator<Entry<String, Number>> it = nbNatures.entrySet().iterator();
			Entry<String, Number> entry;
			while (it.hasNext()) {
				entry = it.next();
				nature.append(entry.getKey());
				nbNat.append(entry.getValue().intValue());
				
				if (it.hasNext()) {
					nature.append(" / ");
					nbNat.append(" / ");
				}
			}
			CoupleAccordValeur cv7 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.nature"), 
					new String[]{nature.toString()});
			LigneAccord li7 = new LigneAccord(
					new CoupleAccordValeur[]{cv7});
			// Quantité
			CoupleAccordValeur cv8 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.quantite"), 
					new String[]{nbNat.toString()});
			LigneAccord li8 = new LigneAccord(
					new CoupleAccordValeur[]{cv8});
			// date
			String date = "";
			if (this.cession.getDepartDate() != null) {
				date = ObjectTypesFormatters
					.dateRenderer2(cession.getDepartDate());
			}
			CoupleAccordValeur cv9 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.disposition"), 
					new String[]{date});
			LigneAccord li9 = new LigneAccord(
					new CoupleAccordValeur[]{cv9});
			// Température
			String tmp = "";
			if (this.cession.getTemperature() != null) {
				tmp = String.valueOf(this.cession.getTemperature() + "°C");
			} else {
				tmp = "-";
			}
			CoupleAccordValeur cv10 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.temp"), 
					new String[]{tmp});
			LigneAccord li10 = new LigneAccord(
					new CoupleAccordValeur[]{cv10});
			// transporteur
			tmp = "";
			if (this.cession.getTransporteur() != null) {
				tmp = String.valueOf(this.cession.getTransporteur().getNom());
			} else {
				tmp = "-";
			}
			CoupleAccordValeur cv11 = new CoupleAccordValeur(
					Labels.getLabel("impression.accord.transfert.transporteur"), 
					new String[]{tmp});
			LigneAccord li11 = new LigneAccord(
					new CoupleAccordValeur[]{cv11});
			BlocPrincipal bloc3 = new BlocPrincipal(
					Labels.getLabel("impression.accord.transfert.materiel"), 
					new LigneAccord[]{li7, li8, li9, li10, li11});
			
			ManagerLocator.getXmlUtils().addTableau(page, new BlocPrincipal[]{
					bloc1, bloc2, bloc3});
			
			// Signatures
			ValeursSignatures vs1 = new ValeursSignatures(
					Labels.getLabel("impression.accord.transfert.responsable"), 
					Labels.getLabel("impression.accord.transfert.responsable"));
			ValeursSignatures vs2 = new ValeursSignatures(
					Labels.getLabel("impression.accord.transfert.signature"), 
					Labels.getLabel("impression.accord.transfert.signature"));
			ValeursSignatures vs3 = new ValeursSignatures(
					Labels.getLabel("impression.accord.transfert.exp.date"), 
					Labels.getLabel("impression.accord.transfert.recept.date"));
			ListeSignature ligne1 = new ListeSignature(
					new ValeursSignatures[]{vs1});
			ListeSignature ligne2 = new ListeSignature(
					new ValeursSignatures[]{vs2});
			ListeSignature ligne3 = new ListeSignature(
					new ValeursSignatures[]{vs3});
			Signatures sign = new Signatures(
					Labels.getLabel("impression.accord.transfert.expediteur")
						.toUpperCase(), 
					Labels.getLabel("impression.accord.transfert.destinataire")
						.toUpperCase(),
					new ListeSignature[]{ligne1, ligne2, ligne3});
			ManagerLocator.getXmlUtils().addSignatures(page, sign);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oldLocale != null) {
				Locales.setThreadLocal(oldLocale);
			}
		}
		return document;
	}
		

	@Override
	public void cloneObject() {		
	}

	@Override
	public void createNewObject() {
	}

	@Override
	public void onClick$addNewC() {
	}

	@Override
	public void onClick$editC() {
	}

	@Override
	public void setEmptyToNulls() {
	}

	@Override
	public void setFieldsToUpperCase() {
	}

	@Override
	public void setFocusOnElement() {
	}

	@Override
	public void switchToStaticMode() {
	}

	@Override
	public void updateObject() {
	}

	@Override
	public TKdataObject getObject() {
		return null;
	}
	
	/**********************************************************/
	/****************** GETTERS - SETTERS *********************/
	/**********************************************************/

	public String getEntete() {
		return entete;
	}

	public void setEntete(String e) {
		this.entete = e;
	}

	public String getPiedPage() {
		return piedPage;
	}

	public void setPiedPage(String p) {
		this.piedPage = p;
	}

	public Cession getCession() {
		return cession;
	}

	public void setCession(Cession c) {
		this.cession = c;
	}

	public Collaborateur getExpediteur() {
		return expediteur;
	}

	public void setExpediteur(Collaborateur e) {
		this.expediteur = e;
	}

	public Collaborateur getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(Collaborateur d) {
		this.destinataire = d;
	}
	
	public ConstWord getNomNullConstraint() {
		return TemplateConstraints.getNomNullConstraint();
	}

	public List<Collaborateur> getCollabs() {
		return collabs;
	}

	public void setCollabs(List<Collaborateur> c) {
		this.collabs = c;
	}

//	public List<CederObjetDecorator> getEchantillonsCedesDecores() {
//		return echantillonsCedesDecores;
//	}
//
//	public void setEchantillonsCedesDecores(
//			List<CederObjetDecorator> eDecores) {
//		this.echantillonsCedesDecores = eDecores;
//	}
//
//	public List<CederObjetDecorator> getDerivesCedesDecores() {
//		return derivesCedesDecores;
//	}
//
//	public void setDerivesCedesDecores(List<CederObjetDecorator> dDecores) {
//		this.derivesCedesDecores = dDecores;
//	}

	@Override
	public String getDeleteWaitLabel() {
		return null;
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public boolean prepareDeleteObject() {
		return false;
	}

	@Override
	public void removeObject(String comments) {		
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}

	public String getLangue() {
		return langue;
	}

	public void setLangue(String lg) {
		this.langue = lg;
	}
}
