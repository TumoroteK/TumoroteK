package fr.aphp.tumorotek.unused.model.bundles;

/**
 * Classe permettant de récupérer l'emplacement des fichiers de
 * configuration pour le module d'impression Mbio. L'emplacement de
 * ces fichiers est défini via une variable JNDI.
 * @author Pierre Ventadour.
 *
 */
public interface ResourceBundleMbio
{

   String getMbioConfDirectory();

}
