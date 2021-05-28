/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsby;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.webapp.gatsby.client.json.Contexte;

/**
 * Formatte l'affichage 'after compose' d'un onglet (static & edit modes) 
 * a partir d'un contexte
 * 
 * @author Mathieu BARTHELEMY
 *
 */
public class DisplayItem {
	
	private static final Log log = LogFactory.getLog(DisplayItem.class);
	
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
}
