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
package fr.aphp.tumorotek.param;

/**
 * Paramètres de configuration de l'application
 * @author GCH
 * @version 2.2.3-genno
 */
public enum TkParam
{

	FILESYSTEM("tk.filesystem"),
	CONF_DIR("tk.conf.dir"),
	MBIO_CONF_DIR("tk.mbio.system"),
	SIP_CONF_DIR("tk.sip.system"),
	CAMEL_CONF_DIR("camel.conf.dir"),
	PORTAL_PROPERTIES_PATH("portal.properties"),
	DUREE_VALIDITE_MDP("NB_MOIS_VALIDITE_MDP"),
	SAUVER_CONNEXION("SAUVER_CONNEXION_TK"),
	MSG_ACCUEIL("msg.accueil"),
	LDAP_AUTHENTICATION("ldap.authentication"),
	LDAP_URL("ldap.url"),
	LDAP_USER("ldap.userdn"),
	LDAP_PASSWORD("ldap.password"),
	ACTIVE_DIRECTORY_DOMAIN("activedirectory.domain"),
	ACTIVE_DIRECTORY_URL("activedirectory.url"),
	TK_DATABASE_DRIVER("db.driver"),
	TK_DATABASE_DIALECT("db.dialect"),
	TK_DATABASE_URL("db.url"),
	TK_DATABASE_USER("db.user"),
	TK_DATABASE_PASSWORD("db.password"),
	CODES_DATABASE_DRIVER("db.codes.driver"),
	CODES_DATABASE_DIALECT("db.codes.dialect"),
	CODES_DATABASE_URL("db.codes.url"),
	CODES_DATABASE_USER("db.codes.user"),
	CODES_DATABASE_PASSWORD("db.codes.password"),
	INTERFACAGES_DATABASE_DRIVER("db.interfacages.driver"),
	INTERFACAGES_DATABASE_DIALECT("db.interfacages.dialect"),
	INTERFACAGES_DATABASE_URL("db.interfacages.url"),
	INTERFACAGES_DATABASE_USER("db.interfacages.user"),
	INTERFACAGES_DATABASE_PASSWORD("db.interfacages.password"),
	MODULE_SIP("SIP"),
	MAX_PATIENTS("MAX_PATIENTS"),
	LONGUEUR_NIP("LONGUEUR_NIP"),
	INTERFACAGES("INTERFACAGES"),
	INTERFACAGES_OUT("INTERFACAGES_OUT"),
	INTERFACAGES_INBOXES("INTERFACAGES_INBOXES"),
	SIP_MAX_TABLE_SIZE("sip.max.table.size"),
	SGL_MAX_TABLE_SIZE("sgl.max.table.size"),
	CONNEXION_CRF("CONNEXION_CRF"),	
	// @since 2.2.3-genno
	GENNO_DERIVES_NATURES("genno.derives.natures"),
	// @since 2.3.0-gatsbi
	GATSBI_URL_BASE("gatsbi.url.base"),
	GATSBI_URL_ETUDE_PATH("gatsbi.url.etude.path"),
	GATSBI_URL_CONTEXTE_PATH("gatsbi.url.contexte.path"),
	GATSBI_URL_PARAMETRAGE_PATH("gatsbi.url.parametrage.path");

	private String paramKey;

	/**
	 * Constructeur
	 * @param paramKey clé du paramètre dans le fichier tumorotek.properties
	 */
	private TkParam(final String paramKey){
		this.paramKey = paramKey;
	}

	/**
	 * Renvoie la clé du paramètre dans le fihcier tumorotek.properties
	 */
	public String getKey(){
		return paramKey;
	}

	/**
	 * Renvoie la valeur du paramètre
	 * @return
	 */
	public String getValue(){
		return TumorotekProperties.getValue(paramKey);
	}

	@Override
	public String toString(){
		return paramKey;
	}

}
