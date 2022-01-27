package fr.aphp.tumorotek.unused.model.bundles;

import java.util.ResourceBundle;

/**
 * Classe permettant de générer le bundle de connection au SIP en
 * utilisant le fichier de propriétés. L'emplacement de ce fichier
 * est défini via une variable JNDI.
 * @author Pierre Ventadour.
 *
 */
public interface ResourceBundleSip
{

   boolean doesResourceBundleExists(String baseName);

   ResourceBundle getResourceBundle(String baseName);

}
