package fr.aphp.tumorotek.action.prelevement.gatsby;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.webapp.gatsby.DisplayItem;
import fr.aphp.tumorotek.webapp.gatsby.client.json.ChampEntite;
import fr.aphp.tumorotek.webapp.gatsby.client.json.Contexte;

public class GatsbyController {
	
	private static final Log log = LogFactory.getLog(GatsbyController.class);

	private static final String[] divIds = new String[] {
		"identifiantBlockDiv",
		"codeDiv",
		"codeLaboDiv",
		"natureDiv",
		"patientBlockDiv",
		"nipDiv",
		"ndaDiv",
		"nomDiv",
		"prenomDiv",
		"dateNaisDiv",
		"sexeDiv",
		"libelleDiv",
		"codeMaladieDiv",
		"infoPrelBlockDiv",
		"datePrelDiv",
		"typeDiv",
		"sterileDiv",
		"risquesDiv",
		"etabPreleveurDiv",
		"servicePreleveurDiv",
		"preleveurDiv",
		"conditBlockDiv",
		"conditTypeDiv",
		"conditNbrDiv",
		"conditMilieuDiv",
		"consentBlockDiv",
		"consentTypeDiv",
		"consentDateDiv",
		"departBlockDiv",
		"dateDepartDiv",
		"transporteurDiv",
		"tempTranspDiv",
		"congPrelDiv",
		"arriveeBlockDiv",
		"dateArriveeDiv",
		"operateurDiv",
		"quantiteDiv",
		"conformeArriveeDiv",
		"congBiothequeDiv"
	};
	
	public GatsbyController() {
	}
	
	public static List<Div> wireDivsFromMainComponent(Div main) {
		return Arrays.stream(divIds)
			.map(id -> (Div) main.getFellowIfAny(id))
			.filter(d -> d != null)
			.collect(Collectors.toList());
	}
	
	public static void showOrhideItems(List<Div> items, Contexte c) {
		log.debug("showing or hiding items");
		if (items != null && c != null) {
			
			List<Integer> hidden = c.getHiddenChamps();
			
			for (Div div : items) {
				div.setVisible(!div.hasAttribute("champId") || !hidden.contains(Integer.valueOf((String) div.getAttribute("champId"))));
				log.debug((div.isVisible() ? "showing " : "hiding ").concat(div.getId()));
			}
		}
	}
	
	private void specificRules() {
		
		// Si service preleveur est invisible 
		
	}
	
	public void hideParentOrGroupIfEmpty(List<Div> blocks) {}
	
	
	// test only
	public static Contexte mockOneContexte() {
		   Contexte cont = new Contexte();
		   ChampEntite code = new ChampEntite();
		   code.setChampId(23);
		   code.setVisible(true);
		   cont.getChampEntites().add(code);
		   ChampEntite codeLabo = new ChampEntite();
		   codeLabo.setChampId(45);
		   codeLabo.setVisible(false);
		   cont.getChampEntites().add(codeLabo);
		   ChampEntite nature = new ChampEntite();
		   nature.setChampId(24);
		   nature.setVisible(false);
		   cont.getChampEntites().add(nature);
		   return cont;
	   }

}
