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
package fr.aphp.tumorotek.action.sip;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.action.ManagerLocator;

public abstract class LoadPropertiesInitTumoFile {
    /* declaration des noms des variables dans le fichier */
    private static final String LANGUE ="LANGUE";
    private static final String DBMS ="DBMS";
    private static final String HOST ="HOST";
    private static final String PORT ="PORT";
    private static final String DRIVER="DRIVER";
    private static final String DATABASE="DATABASE";
    private static final String LOGIN="LOGIN"; 
    private static final String PASSWORD="PASSWORD";
    private static final String CONNECTIONS_INIT="CONNECTIONS_INIT"; 
    private static final String CONNECTIONS_MAX="CONNECTIONS_MAX";
    private static final String MAX_PATIENTS="MAX_PATIENTS";
	private static final String LONGUEUR_NIP="LONGUEUR_NIP";
	private static final String NUMEROTATION_AUTO="NUMEROTATION_AUTO";
	private static final String DELAI_OUT="DELAI_OUT";
	private static final String RECH_MAX_LIGNES="RECH_MAX_LIGNES";
	private static final String CODES_BARRES="CODES_BARRES";
	private static final String SIP="SIP";	
    
	/* accesseurs */
	/* getters */
    public static InitTumoFileBean getInitTumoFileBean() {
    	ResourceBundle res = null;
    	if (ManagerLocator.getResourceBundleTumo()
    			.doesResourceBundleExists("tumorotek.properties")) {
    		res = ManagerLocator.getResourceBundleTumo()
    			.getResourceBundle("tumorotek.properties");
    	}
        
		// lecture des proprietes du fichier de ressources
        int maxPatients = Integer.parseInt(res.getString(MAX_PATIENTS));
        int longueurNip = Integer.parseInt(res.getString(LONGUEUR_NIP));
        String sip = res.getString(SIP);
        
		// creation du bean de memorisation des proprietes
        InitTumoFileBean initTumoFileBean = new InitTumoFileBean(
        		maxPatients,
        		longueurNip,
        		sip);
		
		return initTumoFileBean;
    }
}