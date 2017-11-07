package fr.aphp.tumorotek.model.bundles;

import java.util.ResourceBundle;

/**
 * Classe permettant de générer le bundle de paramétrage de TK en
 * utilisant le fichier de propriétés. L'emplacement de ce fichier 
 * est défini via une variable JNDI.
 * @author Pierre Ventadour.
 *
 */
public interface ResourceBundleTumo {
	
	boolean doesResourceBundleExists(String baseName);
	
	ResourceBundle getResourceBundle(String baseName);

}
