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
package fr.aphp.tumorotek.webapp.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Session;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiController;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Regroupe les méthodes utilisées après la connexion de l'utilisateur.
 * @author pierre
 *
 */
public final class ConnexionUtils
{
	/**
	 * Cree la table de catalogues passée en variable de session
	 * qui sera utilisée lors de l'affichage des tooltips.
	 * @param banques
	 * @param sess
	 */
	public static void setSessionCatalogues(final List<Banque> banques, final Map<String, Object> sess){

		final Map<String, Catalogue> catsMap = new HashMap<>();
		Collection<Catalogue> cats = ManagerLocator.getCatalogueManager().findAllObjectsManager();

		if(banques != null){

			for(final Banque bank : banques){
				cats = CollectionUtils.intersection(cats, ManagerLocator.getCatalogueManager().findByAssignedBanqueManager(bank));
			}
			final Iterator<Catalogue> catsIt = cats.iterator();
			Catalogue cat;
			while(catsIt.hasNext()){
				cat = catsIt.next();
				catsMap.put(cat.getNom(), cat);
			}
		}

		sess.put("catalogues", catsMap);
	}

	/**
	 * Cette méthode va placer les droits de l'utilisateur pour la
	 * banque sélectionnée en variables de la session.
	 */
	public static void generateDroitsForSelectedBanque(final Banque bk, final Plateforme pf, final Utilisateur user, final Map<String, Object> sess){
		// on regarde si l'utilisateur est admin de la plateforme
		// de la banque sélectionnée
		final Set<Plateforme> pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(user);
		if(user.isSuperAdmin() || pfs.contains(pf)){
			sess.put("Admin", true);
			sess.put("AccesAdmin", true);
			sess.put("AdminPF", true);
			sess.put("Anonyme", false);
		}else{
			sess.remove("AdminPF");
			// on récupère le profil du user pour la banque
			// sélectionnée
			final List<ProfilUtilisateur> profs =
					ManagerLocator.getProfilUtilisateurManager().findByUtilisateurBanqueManager(user, bk);

			if(!profs.isEmpty()){
				final Profil profil = profs.get(0).getProfil();

				// si l'utilisateur est admin pour la banque
				if(profil.getAdmin()){
					sess.put("Admin", true);
				}else{
					sess.put("Admin", false);
					sess.put("Droits", getOperationsForProfil(profil));
				}

				// gestion de l'export
				// @since 2.2.3-rc1
				//            if(profil.getProfilExport() != null){
				//               if(profil.getProfilExport() == 2){
				//                  sess.put("Export", "Export");
				//               }else if(profil.getProfilExport() == 1){
				//                  sess.put("Export", "ExportAnonyme");
				//               }else{
				//                  sess.put("Export", "Non");
				//               }
				//            }else{
				//               sess.put("Export", "Non");
				//            }

				sess.put("Export", ExportUtils.getProfilExportFromValue(profil.getProfilExport())); 

				// si l'utilisateur a accès à l'onglet admin
				if(profil.getAccesAdministration()){
					sess.put("AccesAdmin", true);
				}else{
					sess.put("AccesAdmin", false);
				}

				// si l'utilisateur est en Anonyme
				if(profil.getAnonyme() != null && profil.getAnonyme()){
					sess.put("Anonyme", true);
				}else{
					sess.put("Anonyme", false);
				}
			}else{
				throw new RuntimeException(Labels.getLabel("error.resource.notallowed"));
			}
		}
	}



	/**
	 * Cette méthode créee une hashtable contenant, pour chaque entité, la
	 * liste des types d'operations possibles pour le profil du user.
	 * @param profil Profil pour la banque sélectionnée et l'utilisateur.
	 * @return Hashtable contenant les OperationType.
	 */
	public static Hashtable<String, List<OperationType>> getOperationsForProfil(final Profil profil){
		final Hashtable<String, List<OperationType>> operations = new Hashtable<>();

		// droits sur les patients
		operations.put("Patient", ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Patient"));

		// droits sur les Prelevements
		operations.put("Prelevement",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Prelevement"));

		// droits sur les Echantillons
		operations.put("Echantillon",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Echantillon"));

		// droits sur les ProdDerives
		operations.put("ProdDerive",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "ProdDerive"));

		// droits sur les Cessions
		operations.put("Cession", ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Cession"));

		// droits sur les Stockages
		operations.put("Stockage", ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Stockage"));

		// droits sur les Annotations
		/*operations.put("Annotation", ManagerLocator.getDroitObjetManager()
      		.getOperationsByProfilEntiteManager(profil, "Annotation"));*/

		// droits sur les Collaborateurs
		operations.put("Collaborateur",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Collaborateur"));

		// droits sur les Requetes
		operations.put("Requete", ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Requete"));

		return operations;
	}

	public static void initInterfacages(final Plateforme pf, final Map<String, Object> sess){
		// on récupère le bundle de paramétrage de l'application
		ResourceBundle res = null;
		if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
			res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
		}

		// EMETTEURS
		// on récupère la propriété définissant les interfaçages entrant
		String interfacages = null;
		if(null != res && res.containsKey(TkParam.INTERFACAGES.getKey())){
			interfacages = res.getString(TkParam.INTERFACAGES.getKey());
		}
		Hashtable<Integer, List<Integer>> hashInterfacages = null;
		if(interfacages != null && !interfacages.equals("") && !interfacages.equals("false")){
			// extraction des interfaçages
			hashInterfacages = Utils.extractAssosPlateformesEmetteursRecepteurs(interfacages);
		}
		// si des interfaçages sont définis pour la PF sélectionnée
		if(hashInterfacages != null && hashInterfacages.containsKey(pf.getPlateformeId())){
			final List<Integer> ids = hashInterfacages.get(pf.getPlateformeId());
			final List<Emetteur> emetteurs = ManagerLocator.getEmetteurManager().findByIdinListManager(ids);
			// on place les emetteurs dans la session
			if(emetteurs.size() > 0){
				sess.remove("Emetteurs");
				sess.put("Emetteurs", emetteurs);
			}else{
				sess.remove("Emetteurs");
			}
		}

		// RECEPTEURS
		// on récupère la propriété définissant les interfaçages sortant
		interfacages = null;
		hashInterfacages = null;
		if(null != res && res.containsKey(TkParam.INTERFACAGES_OUT.getKey())){
			interfacages = res.getString(TkParam.INTERFACAGES_OUT.getKey());
		}
		if(interfacages != null && !interfacages.equals("") && !interfacages.equals("false")){
			// extraction des interfaçages
			hashInterfacages = Utils.extractAssosPlateformesEmetteursRecepteurs(interfacages);
		}
		// si des interfaçages sont définis pour la PF sélectionnée
		if(hashInterfacages != null && hashInterfacages.containsKey(pf.getPlateformeId())){
			final List<Integer> ids = hashInterfacages.get(pf.getPlateformeId());
			final List<Recepteur> recepteurs = ManagerLocator.getRecepteurManager().findByIdinListManager(ids);
			// on place les emetteurs dans la session
			if(recepteurs.size() > 0){
				sess.remove("Recepteurs");
				sess.put("Recepteurs", recepteurs);
			}else{
				sess.remove("Recepteurs");
			}
		}
	}

	/**
	 * Méthode renvoyant l'utilisateur loggué.
	 * @return Utilisateur loggué.
	 */
	public static Utilisateur getLoggedUtilisateur(){
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if(principal instanceof UserDetails){
			username = ((UserDetails) principal).getUsername();
		}else{
			username = principal.toString();
		}
		List<Utilisateur> list = new ArrayList<>();
		// if (us != null) {
		list = ManagerLocator.getUtilisateurManager().findByLoginManager(username);
		// }

		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * Méthode renvoyant le login de l'utilisateur.
	 * @return String login.
	 */
	public static String getLoggedLogin(){
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if(principal instanceof UserDetails){
			username = ((UserDetails) principal).getUsername();
		}else{
			username = principal.toString();
		}
		return username;
	}

	public static void initConnection(final Utilisateur user, final Plateforme pf, final Banque bank, final List<Banque> banques,
			final Session session){
		final Map<String, Object> sessionScp = session.getAttributes();
		sessionScp.put("User", user);
		sessionScp.put("Plateforme", pf);
		if(bank != null){	
			
			// gatsbi si bank est liée à une étude	
			if (bank.getEtude() != null) {
				try {
					GatsbiController.doGastbiContexte(bank);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
			session.setAttribute("Banque", bank);
			final List<Banque> banks = new ArrayList<>();
			banks.add(bank);
			ConnexionUtils.setSessionCatalogues(banks, sessionScp);
			ConnexionUtils.generateDroitsForSelectedBanque(bank, pf, user, sessionScp);
			sessionScp.remove("ToutesCollections");
		}else{ // toutes collections
			// suppose que l'utilisateur ne peut pas être admin
			// sur banques de différentes plateformes
			sessionScp.put("ToutesCollections", banques);
			ConnexionUtils.setSessionCatalogues(banques, sessionScp);
			ConnexionUtils.generateDroitsForSelectedBanque(banques.get(0), pf, user, sessionScp);
			sessionScp.remove("Banque");
		}

		// gestion des interfaçages
		ConnexionUtils.initInterfacages(pf, sessionScp);
	} 
}
