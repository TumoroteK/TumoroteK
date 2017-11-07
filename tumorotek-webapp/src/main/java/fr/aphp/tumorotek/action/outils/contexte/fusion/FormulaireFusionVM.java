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
package fr.aphp.tumorotek.action.outils.contexte.fusion;

import java.awt.Color;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.Scope;
import org.zkoss.bind.annotation.ScopeParam;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.contexte.DuoEntites;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Messagebox.Button;

public class FormulaireFusionVM {

	private ListModelList<DuoEntites> duoModel;
	private DuoEntites duoSelectedEntites;

	FusionDetailsBarChartEngine engine1;
	FusionDetailsBarChartEngine engine2;
	CategoryModel model;
	CategoryModel model2;

	ChartDataFusion cdf = new ChartDataFusion();

	@Wire
	Window affichageFusion;

	PatientManager patientManager;

	private int idASelected;
	private int idBSelected;
	private String entiteRecherche;

	private String commentaires;

	private Utilisateur utilisateurTest;

	CollaborateurManager collaborateurManager;

	// générique
	private String nomA;
	private String nomB;

	private String prenomA;
	private String prenomB;

	private boolean visibleCollaborateur = false;
	// private boolean visibleService = false;
	// private boolean visibleEtablissement = false;

	private Collaborateur cA;
	private Collaborateur cB;

	private Service sA;
	private Service sB;

	private Etablissement eA;
	private Etablissement eB;

	private String nomChamp1;
	private String champ1A;
	private String champ1B;

	private String nomChamp2;
	private String champ2A;
	private String champ2B;

	private String nomChamp3;
	private String champ3A;
	private String champ3B;
	
	private String dateCreationA;
	private String dateCreationB;
	private String operateurCreationA;
	private String operateurCreationB;
	private String dateLastModificationA;
	private String dateLastModificationB;
	private String operateurLastModificationA;
	private String operateurLastModificationB;

	// info details pour chart
	private String nomInfoChartChamp1;
	private String nomInfoChartChamp2;
	private String nomInfoChartChamp3;
	private Long infoChartChamp1A;
	private long infoChartChamp2A;
	private long infoChartChamp3A;
	private long infoChartChamp1B;
	private long infoChartChamp2B;
	private long infoChartChamp3B;

	// private boolean sensFusion = false;

	@NotifyChange({ "idASelected", "idBSelected", "nomA", "nomB", "prenomA",
			"prenomB", "champ1A", "champ1B", "nomChamp2", "champ2A", "champ2B",
			"nomChamp3", "champ3A", "champ3B",
			"dateCreationA", "dateCreationB", "operateurCreationA",
			"operateurCreationB", "dateLastModificationA",
			"dateLastModificationB", "operateurLastModificationA",
			"operateurLastModificationB", "model", "model2" })
	@Command
	public void switchEntities() {
		initData(idBSelected, idASelected);
	}

	public void initData(int id1, int id2) {
		cdf.emptyIt();

		this.idASelected = id1;
		this.idBSelected = id2;

		switch (entiteRecherche) {
		case ("Collaborateur"):
			this.cA = ManagerLocator.getCollaborateurManager().findByIdManager(
					id1);
			this.cB = ManagerLocator.getCollaborateurManager().findByIdManager(
					id2);

			this.nomA = cA.getNom();
			this.nomB = cB.getNom();
			setNomChamp1(Labels.getLabel("Champ.Patient.Prenom"));
			this.champ1A = cA.getPrenom();
			this.champ1B = cB.getPrenom();
					
			setNomChamp2(Labels.getLabel("Champ.Collaborateur.Etablissement"));
			this.champ2A = cA.getEtablissement() != null ? 
					cA.getEtablissement().getNom() : "";
			this.champ2B = cB.getEtablissement() != null ? 
					cB.getEtablissement().getNom() : "";

			setNomChamp3(Labels.getLabel("Champ.Collaborateur.Services"));				

			this.champ3A = "";
			for (Service s : ManagerLocator.getCollaborateurManager().getServicesManager(cA))
				{ if(champ3A =="") this.champ3A = this.champ3A + s.getNom();
				else this.champ3A = this.champ3A + ", " + s.getNom();
				}
			this.champ3B = "";
			for (Service s : ManagerLocator.getCollaborateurManager().getServicesManager(cB))
				{ if(champ3B =="")  this.champ3B = this.champ3B + s.getNom();
				else this.champ3B = this.champ3B + ", " + s.getNom();}
	
			cdf.addValueTo1(
					(cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.echantillons.operes"),
					ManagerLocator.getEchantillonManager()
							.findCountByOperateurManager(cA));
			cdf.addValueTo1(
					(cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.prelevements.preleves"),
					ManagerLocator.getPrelevementManager()
							.findCountByPreleveurManager(cA));
			cdf.addValueTo1(
					(cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.prodderives.operes"),
					ManagerLocator.getProdDeriveManager()
							.findCountByOperateurManager(cA));
			cdf.addValueTo1((cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.maladies.referent"),
					ManagerLocator.getMaladieManager()
							.findCountByReferentManager(cA));
			cdf.addValueTo1((cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.patients.referent"),
					ManagerLocator.getPatientManager()
							.findCountByReferentManager(cA));
			cdf.addValueTo1((cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.demandees"),
					ManagerLocator.getCessionManager()
							.findCountByDemandeurManager(cA));
			cdf.addValueTo1((cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.executees"),
					ManagerLocator.getCessionManager()
							.findCountByExecutantManager(cA));
			cdf.addValueTo1((cA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.destinees"),
					ManagerLocator.getCessionManager()
							.findCountByDestinataireManager(cA));		

			cdf.addValueTo2(
					(cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.echantillons.operes"),
					ManagerLocator.getEchantillonManager()
							.findCountByOperateurManager(cB));
			cdf.addValueTo2(
					(cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.prelevements.preleves"),
					ManagerLocator.getPrelevementManager()
							.findCountByPreleveurManager(cB));
			cdf.addValueTo2(
					(cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.prodderives.operes"),
					ManagerLocator.getProdDeriveManager()
							.findCountByOperateurManager(cB));
			cdf.addValueTo2((cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.maladies.referent"),
					ManagerLocator.getMaladieManager()
							.findCountByReferentManager(cB));
			cdf.addValueTo2((cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.patients.referent"),
					ManagerLocator.getPatientManager()
							.findCountByReferentManager(cB));
			cdf.addValueTo2((cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.demandees"),
					ManagerLocator.getCessionManager()
							.findCountByDemandeurManager(cB));
			cdf.addValueTo2((cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.executees"),
					ManagerLocator.getCessionManager()
							.findCountByExecutantManager(cB));
			cdf.addValueTo2((cB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.collaborateur.nb.cessions.destinees"),
					ManagerLocator.getCessionManager()
							.findCountByDestinataireManager(cB));	

			/*
			 * this.nomInfoChartChamp1="nb d'echantillons crees";
			 * 
			 * this.infoChartChamp1A =
			 * ManagerLocator.getEchantillonManager().findCountByCollaborateur
			 * (cA); this.infoChartChamp2A =
			 * ManagerLocator.getEchantillonManager
			 * ().findCountByCollaborateur(cB);
			 */
			fillGenericData(cA, cB);
			/*
			 * Calendar dateA =
			 * ManagerLocator.getOperationManager().findDateCreationManager(cA);
			 * Calendar dateB =
			 * ManagerLocator.getOperationManager().findDateCreationManager(cB);
			 * 
			 * if (dateA != null) { this.dateCreationA =
			 * ObjectTypesFormatters.dateRenderer2(dateA);
			 * this.operateurCreationA =
			 * ManagerLocator.getOperationManager().findOperationCreationManager
			 * (cA).getUtilisateur().toString(); } else{ this.dateCreationA =
			 * "Non disponible"; this.operateurCreationA = "Non disponible"; }
			 * 
			 * if (dateB != null) { this.dateCreationB =
			 * ObjectTypesFormatters.dateRenderer2(dateB);
			 * this.operateurCreationB =
			 * ManagerLocator.getOperationManager().findOperationCreationManager
			 * (cB).getUtilisateur().toString(); } else{ this.dateCreationB =
			 * "Non disponible"; this.operateurCreationB = "Non disponible"; }
			 */break;

		case ("Service"):
			this.sA = ManagerLocator.getServiceManager().findByIdManager(id1);
			this.sB = ManagerLocator.getServiceManager().findByIdManager(id2);

			this.nomA = sA.getNom();
			this.nomB = sB.getNom();
			setNomChamp1(Labels.getLabel("Champ.Collaborateur.Etablissement"));
			this.champ1A = sA.getEtablissement().getNom();
			this.champ1B = sB.getEtablissement().getNom();

			cdf.addValueTo1(
					(sA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.service.nb.collaborateurs.affectes"),
					ManagerLocator.getCollaborateurManager()
							.findCountByServicedIdManager(sA));
			cdf.addValueTo1((sA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.service.nb.prelevements.affectes"),
					ManagerLocator.getPrelevementManager()
							.findCountByServiceManager(sA));

			cdf.addValueTo2(
					(sB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.service.nb.collaborateurs.affectes"),
					ManagerLocator.getCollaborateurManager()
							.findCountByServicedIdManager(sB));
			cdf.addValueTo2((sB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.service.nb.prelevements.affectes"),
					ManagerLocator.getPrelevementManager()
							.findCountByServiceManager(sB));

			/*
			 * this.nomInfoChartChamp1="nb de collaboratteurs affectés";
			 * this.infoChartChamp1A =
			 * ManagerLocator.getCollaborateurManager().findCountByServicedIdManager
			 * (sA); this.infoChartChamp1B =
			 * ManagerLocator.getCollaborateurManager
			 * ().findCountByServicedIdManager(sB);
			 */

			fillGenericData(sA, sB);

			break;

		case ("Etablissement"):

			this.eA = ManagerLocator.getEtablissementManager().findByIdManager(
					id1);
			this.eB = ManagerLocator.getEtablissementManager().findByIdManager(
					id2);

			this.nomA = eA.getNom();
			this.nomB = eB.getNom();

			cdf.addValueTo1((eA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.etablissement.nb.services.lies"),
					ManagerLocator.getServiceManager()
							.findCountByEtablissementIdManager(eA));
			cdf.addValueTo1(
					(eA.getNom() + " " + getIdASelectedString()),
					Labels.getLabel("fusion.etablissement.nb.collaborateurs.lies"),
					ManagerLocator.getCollaborateurManager()
							.findCountByEtablissementManager(eA));

			cdf.addValueTo2((eB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.etablissement.nb.services.lies"),
					ManagerLocator.getServiceManager()
							.findCountByEtablissementIdManager(eB));
			cdf.addValueTo2(
					(eB.getNom() + " " + getIdBSelectedString()),
					Labels.getLabel("fusion.etablissement.nb.collaborateurs.lies"),
					ManagerLocator.getCollaborateurManager()
							.findCountByEtablissementManager(eB));

			/*
			 * this.nomInfoChartChamp1="nb de services"; this.infoChartChamp1A =
			 * ManagerLocator
			 * .getServiceManager().findCountByEtablissementIdManager(eA);
			 * this.infoChartChamp1B =
			 * ManagerLocator.getServiceManager().findCountByEtablissementIdManager
			 * (eB);
			 */

			fillGenericData(eA, eB);

			break;
		}
	}

	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("idASelected") int v1,
			@ExecutionArgParam("idBSelected") int v2,
			@ExecutionArgParam("entiteRecherche") String er,
			@ExecutionArgParam("duoModel") ListModelList<DuoEntites> duoList,
			@ExecutionArgParam("duoEntite") DuoEntites duoEnt,
			@ScopeParam(scopes = Scope.SESSION, value = "User") Utilisateur u) {

		Selectors.wireComponents(view, this, false);

		this.entiteRecherche = er;

		this.utilisateurTest = u;

		initData(v1, v2);

		// this.entiteRecherche = er;
		this.duoModel = duoList;
		this.duoSelectedEntites = duoEnt;

		// prepare chart data
		engine1 = new FusionDetailsBarChartEngine(Color.BLUE);
		engine2 = new FusionDetailsBarChartEngine(Color.RED);
		model = ChartDataFusion.getModel();
		model2 = ChartDataFusion.getModel2();
	}

	public void fillGenericData(Object objA, Object objB) {

		Calendar dateA = ManagerLocator.getOperationManager()
				.findDateCreationManager(objA);
		Calendar dateB = ManagerLocator.getOperationManager()
				.findDateCreationManager(objB);

		if (dateA != null) {
			this.dateCreationA = ObjectTypesFormatters.dateRenderer2(dateA);
			this.operateurCreationA = ManagerLocator.getOperationManager()
					.findOperationCreationManager(objA).getUtilisateur()
					.getLogin();
		} else {
			this.dateCreationA = Labels.getLabel("fusion.info.non.disponible");
			this.operateurCreationA = Labels
					.getLabel("fusion.info.non.disponible");
		}

		if (dateB != null) {
			this.dateCreationB = ObjectTypesFormatters.dateRenderer2(dateB);
			this.operateurCreationB = ManagerLocator.getOperationManager()
					.findOperationCreationManager(objB).getUtilisateur()
					.getLogin();
		} else {
			this.dateCreationB = Labels.getLabel("fusion.info.non.disponible");
			this.operateurCreationB = Labels
					.getLabel("fusion.info.non.disponible");
		}
	}

	/*
	 * public void doAfterCompose(Component window) throws Exception {
	 * 
	 * if (hm.containsKey("username ")) { String s = (String)
	 * arg.get("username")); } }
	 */

	@Command
	public void close() {
		affichageFusion.detach();
	}

	@Command
	@NotifyChange("duoModel")
	public void fusion() {

		if (Messagebox.show(Labels.getLabel("fusion.confirmation.question"),
				Labels.getLabel("fusion.confirm"), new Messagebox.Button[] {
						Messagebox.Button.YES, Messagebox.Button.NO },
				new String[] { Labels.getLabel("fusion.fuse.confirm") },
				Messagebox.QUESTION, null, null) == Button.YES) {

			switch (entiteRecherche) {
			case ("Collaborateur"):
				ManagerLocator.getCollaborateurManager()
						.fusionCollaborateurManager(idASelected, idBSelected,
								commentaires, utilisateurTest);//
				break;
			case ("Service"):
				ManagerLocator.getServiceManager()
						.fusionServiceManager(idASelected, idBSelected,
								commentaires, utilisateurTest);// SessionUtils.getLoggedUser(sessionScope));
				break;
			case ("Etablissement"):
				ManagerLocator.getEtablissementManager()
						.fusionEtablissementManager(idASelected, idBSelected,
								commentaires, utilisateurTest);// SessionUtils.getLoggedUser(sessionScope));
				break;
			default:
			}
			removeFusedFromList();
			Messagebox.show(Labels.getLabel("fusion.fusion.done"));
			close();
		}

		else {
			Messagebox.show(Labels.getLabel("fusion.fusion.not.done"));
		}
	}

	public void removeFusedFromList() {
		Set<DuoEntites> toRemove = new HashSet<DuoEntites>();
		toRemove.add(duoSelectedEntites);
		
		
		for (DuoEntites duo : duoModel) {
			if (duo.getIdEntiteA() == idBSelected || 
					duo.getIdEntiteB() == idBSelected) {
				toRemove.add(duo);
			}
		}
		duoModel.removeAll(toRemove);
	}

	public FormulaireFusionVM() {

	}

	public int getIdASelected() {
		return idASelected;
	}

	public String getIdASelectedString() {
		return Integer.toString(idASelected);
	}

	public int getIdBSelected() {
		return idBSelected;
	}

	public String getIdBSelectedString() {
		return Integer.toString(idBSelected);
	}

	public String getEntiteRecherche() {
		return entiteRecherche;
	}

	public void setIdASelected(int idASelected) {
		this.idASelected = idASelected;
	}

	public void setIdBSelected(int idBSelected) {
		this.idBSelected = idBSelected;
	}

	public void setEntiteRecherche(String entiteRecherche) {
		this.entiteRecherche = entiteRecherche;
	}

	public String getNomA() {
		return nomA;
	}

	public String getNomB() {
		return nomB;
	}

	public void setNomA(String nomA) {
		this.nomA = nomA;
	}

	public void setNomB(String nomB) {
		this.nomB = nomB;
	}

	public String getPrenomA() {
		return prenomA;
	}

	public String getPrenomB() {
		return prenomB;
	}

	public boolean isVisibleCollaborateur() {
		return visibleCollaborateur;
	}

	public void setPrenomA(String prenomA) {
		this.prenomA = prenomA;
	}

	public void setPrenomB(String prenomB) {
		this.prenomB = prenomB;
	}

	public void setVisibleCollaborateur(boolean visibleCollaborateur) {
		this.visibleCollaborateur = visibleCollaborateur;
	}

	public String getNomChamp1() {
		return nomChamp1;
	}

	public String getChamp1A() {
		return champ1A;
	}

	public void setNomChamp1(String nomChamp1) {
		this.nomChamp1 = nomChamp1;
	}

	public void setChamp1A(String champ1a) {
		champ1A = champ1a;
	}

	public String getChamp1B() {
		return champ1B;
	}

	public void setChamp1B(String champ1b) {
		champ1B = champ1b;
	}

	public String getNomInfoChartChamp1() {
		return nomInfoChartChamp1;
	}

	public String getNomInfoChartChamp2() {
		return nomInfoChartChamp2;
	}

	public String getNomInfoChartChamp3() {
		return nomInfoChartChamp3;
	}

	public long getInfoChartChamp1A() {
		return infoChartChamp1A;
	}

	public long getInfoChartChamp2A() {
		return infoChartChamp2A;
	}

	public long getInfoChartChamp3A() {
		return infoChartChamp3A;
	}

	public long getInfoChartChamp1B() {
		return infoChartChamp1B;
	}

	public long getInfoChartChamp2B() {
		return infoChartChamp2B;
	}

	public long getInfoChartChamp3B() {
		return infoChartChamp3B;
	}

	public void setNomInfoChartChamp1(String nomInfoChartChamp1) {
		this.nomInfoChartChamp1 = nomInfoChartChamp1;
	}

	public void setNomInfoChartChamp2(String nomInfoChartChamp2) {
		this.nomInfoChartChamp2 = nomInfoChartChamp2;
	}

	public void setNomInfoChartChamp3(String nomInfoChartChamp3) {
		this.nomInfoChartChamp3 = nomInfoChartChamp3;
	}

	public void setInfoChartChamp1A(long infoChartChamp1A) {
		this.infoChartChamp1A = infoChartChamp1A;
	}

	public void setInfoChartChamp2A(long infoChartChamp2A) {
		this.infoChartChamp2A = infoChartChamp2A;
	}

	public void setInfoChartChamp3A(long infoChartChamp3A) {
		this.infoChartChamp3A = infoChartChamp3A;
	}

	public void setInfoChartChamp1B(long infoChartChamp1B) {
		this.infoChartChamp1B = infoChartChamp1B;
	}

	public void setInfoChartChamp2B(long infoChartChamp2B) {
		this.infoChartChamp2B = infoChartChamp2B;
	}

	public void setInfoChartChamp3B(long infoChartChamp3B) {
		this.infoChartChamp3B = infoChartChamp3B;
	}

	public String getOperateurCreationA() {
		return operateurCreationA;
	}

	public String getOperateurCreationB() {
		return operateurCreationB;
	}

	public void setOperateurCreationA(String operateurCreationA) {
		this.operateurCreationA = operateurCreationA;
	}

	public void setOperateurCreationB(String operateurCreationB) {
		this.operateurCreationB = operateurCreationB;
	}

	public String getDateCreationA() {
		return dateCreationA;
	}

	public String getDateCreationB() {
		return dateCreationB;
	}

	public void setDateCreationA(String dateCreationA) {
		this.dateCreationA = dateCreationA;
	}

	public void setDateCreationB(String dateCreationB) {
		this.dateCreationB = dateCreationB;
	}

	/*
	 * public BarChartEngine getEngine() { return engine; }
	 */
	public CategoryModel getModel() {
		return model;
	}

	public CategoryModel getModel2() {
		return model2;
	}

	public String getDateLastModificationA() {
		return dateLastModificationA;
	}

	public String getDateLastModificationB() {
		return dateLastModificationB;
	}

	public String getOperateurLastModificationA() {
		return operateurLastModificationA;
	}

	public String getOperateurLastModificationB() {
		return operateurLastModificationB;
	}

	public void setDateLastModificationA(String dateLastModificationA) {
		this.dateLastModificationA = dateLastModificationA;
	}

	public void setDateLastModificationB(String dateLastModificationB) {
		this.dateLastModificationB = dateLastModificationB;
	}

	public void setOperateurLastModificationA(String operateurLastModificationA) {
		this.operateurLastModificationA = operateurLastModificationA;
	}

	public void setOperateurLastModificationB(String operateurLastModificationB) {
		this.operateurLastModificationB = operateurLastModificationB;
	}

	public String getNomChamp2() {
		return nomChamp2;
	}

	public String getChamp2A() {
		return champ2A;
	}

	public String getChamp2B() {
		return champ2B;
	}

	public String getNomChamp3() {
		return nomChamp3;
	}

	public void setNomChamp3(String nomChamp3) {
		this.nomChamp3 = nomChamp3;
	}

	public String getChamp3A() {
		return champ3A;
	}

	public void setChamp3A(String champ3a) {
		champ3A = champ3a;
	}

	public String getChamp3B() {
		return champ3B;
	}

	public void setChamp3B(String champ3b) {
		champ3B = champ3b;
	}

	public void setNomChamp2(String nomChamp2) {
		this.nomChamp2 = nomChamp2;
	}

	public void setChamp2A(String champ2a) {
		champ2A = champ2a;
	}

	public void setChamp2B(String champ2b) {
		champ2B = champ2b;
	}

	public FusionDetailsBarChartEngine getEngine2() {
		return engine2;
	}

	public FusionDetailsBarChartEngine getEngine1() {
		return engine1;
	}
}
