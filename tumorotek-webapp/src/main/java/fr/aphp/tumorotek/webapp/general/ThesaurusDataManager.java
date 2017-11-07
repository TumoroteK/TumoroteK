package fr.aphp.tumorotek.webapp.general;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.qualite.NonConformite;

public class ThesaurusDataManager {

	// THESAURUS
	// Table de correlation
	public static final Hashtable<String, String> thesaurusTable = new Hashtable<String, String>();
	static {
		// Catégorie
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.categorie"),
				"Categorie");
		// examen
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.examen.cession"),
				"CessionExamen");
		// Milieu de conditionnement
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.milieu.conditionnement"),
				"ConditMilieu");
		// Mode de préparation
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.mode.preparation"),
				"ModePrepa");
		// Mode de préparation dérivés
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.mode.preparation.derive"),
				"ModePrepaDerive");
		// Motif de destruction
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.motif.destruction"),
				"DestructionMotif");
		// nature des prlvts
		thesaurusTable
				.put(Labels.getLabel("thesaurus.liste.nature.prelevement"),
						"Nature");
		// Non conformite a l'arrivee
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.nonConformite.arrivee"),
				"NonConformiteArrivee");
		// Non conformite apres traitement
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.nonConformite.traitement.echan"),
				"NonConformiteTraitementEchan");
		// Non conformite a la cession
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.nonConformite.cession.echan"),
				"NonConformiteCessionEchan");
		// Non conformite apres traitement
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.nonConformite.traitement.derive"),
				"NonConformiteTraitementDerive");
		// Non conformite a la cession
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.nonConformite.cession.derive"),
				"NonConformiteCessionDerive");
		// Protocole SerotK
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.serotk.protocole"),
				"Protocole");
		// Qualité de l'échantillon
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.qualite.echantillon"),
				"EchanQualite");
		// Qualité du dérivé
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.qualite.prodDerive"),
				"ProdQualite");
		// Risque
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.risque"),
				"Risque");
		// Spécialité
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.specialite"),
				"Specialite");
		// Statut juridique
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.type.consentement"),
				"ConsentType");
		// Type d'échantillon
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.type.echantillon"),
				"EchantillonType");
		// Type d'enceinte
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.type.enceinte"),
				"EnceinteType");
		// Type de conditionnement
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.type.conditionnement"),
				"ConditType");
		// Type de conteneur
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.type.conteneur"),
				"ConteneurType");
		// Type de prélèvement
		thesaurusTable.put("PrelevementType",
				"PrelevementType");
		// Type de dérivé
		thesaurusTable.put(
				Labels.getLabel("thesaurus.liste.type.prodDerive"), "ProdType");
		// Type de protocole
		thesaurusTable.put(Labels.getLabel("thesaurus.liste.type.protocole"),
				"ProtocoleType");
	}
	
	public static List<String> getThesaurusList() {
		List<String> listThesaurus = new ArrayList<String>();
		// Catégorie
		listThesaurus.add(Labels.getLabel("thesaurus.liste.categorie"));
		// examen
		listThesaurus.add(Labels.getLabel("thesaurus.liste.examen.cession"));
		// Milieu de conditionnement
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.milieu.conditionnement"));
		// Mode de préparation
		listThesaurus.add(Labels.getLabel("thesaurus.liste.mode.preparation"));
		// Mode de préparation dérivés
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.mode.preparation.derive"));
		// Motif de destruction
		listThesaurus.add(Labels.getLabel("thesaurus.liste.motif.destruction"));
		// nature des prlvts
		listThesaurus.add(Labels.getLabel("thesaurus.liste.nature.prelevement"));
		// Non conformite a l'arrivee
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.nonConformite.arrivee"));
		// Non conformite apres traitement
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.nonConformite.traitement.echan"));
		// Non conformite a la cession
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.nonConformite.cession.echan"));
		
		// Non conformite apres traitement
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.nonConformite.traitement.derive"));
		// Non conformite a la cession
		listThesaurus.add(Labels
				.getLabel("thesaurus.liste.nonConformite.cession.derive"));
		// Protocole SerotK
		listThesaurus.add(Labels.getLabel("thesaurus.liste.serotk.protocole"));
		// Qualité de l'échantillon
		listThesaurus.add(Labels.getLabel("thesaurus.liste.qualite.echantillon"));
		// Qualité du dérivé
		listThesaurus.add(Labels.getLabel("thesaurus.liste.qualite.prodDerive"));
		// Risque
		listThesaurus.add(Labels.getLabel("thesaurus.liste.risque"));
		// Spécialité
		listThesaurus.add(Labels.getLabel("thesaurus.liste.specialite"));
		// Statut juridique
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.consentement"));
		// Type d'échantillon
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.echantillon"));
		// Type d'enceinte
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.enceinte"));
		// Type de conditionnement
		listThesaurus
				.add(Labels.getLabel("thesaurus.liste.type.conditionnement"));
		// Type de conteneur
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.conteneur"));
		// Type de prélèvement
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.prelevement"));
		// Type de dérivé
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.prodDerive"));
		// Type de protocole
		listThesaurus.add(Labels.getLabel("thesaurus.liste.type.protocole"));

		return listThesaurus;
	}
	
	/**
	 * Convertie la valeur en Object en String 
	 * en fonction de la classe Object
	 *
	 */
	public static String getStringValueObject(Object value) {		
		if (value.getClass().getSimpleName().equals("Specialite")) {
			return ((Specialite) value).getNom();
		} else if (value.getClass().getSimpleName().equals("Categorie")) {
			return ((Categorie) value).getNom();
		} else if (value.getClass().getSimpleName().equals("NonConformite")) {
			return ((NonConformite) value).getNom();
		} else {
			return ((TKThesaurusObject) value).getNom();
		}
	}

}
