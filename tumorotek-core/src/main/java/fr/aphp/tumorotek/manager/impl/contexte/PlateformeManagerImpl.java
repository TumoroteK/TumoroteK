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
package fr.aphp.tumorotek.manager.impl.contexte;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurPlateformeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.PlateformeValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateformePK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Plateforme.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 *
 */
public class PlateformeManagerImpl implements PlateformeManager
{

	private final Log log = LogFactory.getLog(PlateformeManager.class);

	private PlateformeDao plateformeDao;
	private CollaborateurDao collaborateurDao;
	private UtilisateurDao utilisateurDao;
	private ConteneurPlateformeDao conteneurPlateformeDao;
	private PlateformeValidator plateformeValidator;
	private OperationTypeDao operationTypeDao;
	private OperationManager operationManager;

	/**
	 * Setter du bean PlateformeDao.
	 * @param bDao est le bean Dao.
	 */
	public void setPlateformeDao(final PlateformeDao pDao){
		this.plateformeDao = pDao;
	}

	public void setCollaborateurDao(final CollaborateurDao cDao){
		this.collaborateurDao = cDao;
	}

	public void setUtilisateurDao(final UtilisateurDao uDao){
		this.utilisateurDao = uDao;
	}

	public void setConteneurPlateformeDao(final ConteneurPlateformeDao cDao){
		this.conteneurPlateformeDao = cDao;
	}

	public void setPlateformeValidator(final PlateformeValidator pValidator){
		this.plateformeValidator = pValidator;
	}

	public void setOperationTypeDao(final OperationTypeDao oDao){
		this.operationTypeDao = oDao;
	}

	public void setOperationManager(final OperationManager oManager){
		this.operationManager = oManager;
	}

	/**
	 * Recherche une Plateforme dont l'identifiant est passé en paramètre.
	 * @param plateformeId Identifiant de la Plateforme que l'on recherche.
	 * @return Une Plateforme.
	 */
	@Override
	public Plateforme findByIdManager(final Integer plateformeId){
		return plateformeDao.findById(plateformeId);
	}

	/**
	 * Recherche toutes les Plateforme présentes dans la base.
	 * @return Liste de Plateforme.
	 */
	@Override
	public List<Plateforme> findAllObjectsManager(){
		return plateformeDao.findByOrder();
	}

	/**
	 * Recherche les banque liées à la plateforme passée en paramètre.
	 * @param plateforme Plateforme pour laquelle on recherche des
	 * banques.
	 * @return Liste de banques.
	 */
	@Override
	public Set<Banque> getBanquesManager(Plateforme plateforme){
		plateforme = plateformeDao.mergeObject(plateforme);
		final Set<Banque> banques = plateforme.getBanques();
		banques.size();

		return banques;
	}

	@Override
	public Set<Utilisateur> getUtilisateursManager(Plateforme plateforme){
		plateforme = plateformeDao.mergeObject(plateforme);
		final Set<Utilisateur> utilisateurs = plateforme.getUtilisateurs();
		utilisateurs.size();

		return utilisateurs;
	}

	@Override
	public Boolean findDoublonManager(final Plateforme plateforme){
		if(plateforme != null){
			if(plateforme.getPlateformeId() == null){
				return plateformeDao.findAll().contains(plateforme);
			}else{
				return plateformeDao.findByExcludedId(plateforme.getPlateformeId()).contains(plateforme);
			}
		}else{
			return false;
		}
	}

	@Override
	public Plateforme createObjectManager(Plateforme plateforme, final Collaborateur collaborateur,
			final List<Utilisateur> utilisateurs, final Utilisateur admin, 
			final String baseDir){

		if (plateforme != null) {
			//Doublon
			if(!findDoublonManager(plateforme)){

				plateforme.setCollaborateur(collaborateur);

				// validation due la pf
				BeanValidator.validateObject(plateforme, new Validator[] {plateformeValidator});

				// cette methode appelle mergeObject(plateforme)
				plateforme = updateAdministrateurs(plateforme, utilisateurs);
				log.info("Enregistrement objet Plateforme " + plateforme.toString());

				// filesystem
				String path = baseDir.concat("/pt_").concat(plateforme.getPlateformeId().toString());
				if(new File(path).mkdirs()){
					log.info("Creation file system " + path);
				}else{
					log.error("Erreur dans la creation du systeme de fichier pour la pf " + plateforme.toString());
					throw new RuntimeException("plateforme.filesystem.error");
				}

				//Enregistrement de l'operation associee
				final Operation creationOp = new Operation();
				creationOp.setDate(Utils.getCurrentSystemCalendar());
				
				// refresh admin si on vient de lui ajouter une PF
				Utilisateur currAdmin = admin;
				if (plateforme.getUtilisateurs().contains(admin)) {
					currAdmin = plateforme.getUtilisateurs().stream()
							.filter(u -> u.equals(admin)).findFirst().orElse(admin);
				}
				
				operationManager.createObjectManager(creationOp, currAdmin, operationTypeDao.findByNom("Creation").get(0), plateforme);

			}else{
				log.warn("Doublon lors modification objet Plateforme " + plateforme.toString());
				throw new DoublonFoundException("Plateforme", "modification");
			}
		}
		
		return plateforme;
	}

	@Override
	public Plateforme updateObjectManager(Plateforme plateforme, final Collaborateur collaborateur,
			final List<Utilisateur> utilisateurs, final List<Conteneur> conteneurs, final Utilisateur admin){
		//Doublon
		if(!findDoublonManager(plateforme)){
			plateforme.setCollaborateur(collaborateurDao.mergeObject(collaborateur));

			// validation due la pf
			BeanValidator.validateObject(plateforme, new Validator[] {plateformeValidator});

			plateforme = plateformeDao.mergeObject(plateforme);
			log.info("Enregistrement objet Plateforme " + plateforme.toString());

			//Enregistrement de l'operation associee
			final Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, admin, operationTypeDao.findByNom("Modification").get(0), plateforme);

			// enregistrements des admins
			updateAdministrateurs(plateforme, utilisateurs);
			// enregistrement des conteneurs
			updateConteneurs(plateforme, conteneurs);

		}else{
			log.warn("Doublon lors modification objet Plateforme " + plateforme.toString());
			throw new DoublonFoundException("Plateforme", "modification");
		}
		return plateforme;
	}

	/**
	 * Cette méthode met à jour les associations entre un utilisateur,
	 * une liste de profils et une liste de plateformes.
	 * @param utilisateur Utilisateur pour lequel on veut mettre à jour
	 * les associations.
	 * @param profils Liste de tous les profils de l'utilisateur : ceux 
	 * déjà existant et ceux a creer.
	 * @param profilsToCreate Liste des profils a creer.
	 * @param plateformes Liste des plateformes que l'on veut associer a 
	 * l'utilisateur.
	 */
	public Plateforme updateAdministrateurs(final Plateforme plateforme, final List<Utilisateur> utilisateurs){
		final Plateforme pf = plateformeDao.mergeObject(plateforme);

		if(utilisateurs != null){
			final Iterator<Utilisateur> it = pf.getUtilisateurs().iterator();
			final List<Utilisateur> utilsToRemove = new ArrayList<>();
			// on parcourt les Utilisateurs qui sont actuellement associés
			// a la plateforme
			while(it.hasNext()){
				final Utilisateur tmp = it.next();
				// si un utilisateur n'est pas dans la nouvelle liste, on
				// le conserve afin de le retirer par la suite
				if(!utilisateurs.contains(tmp)){
					utilsToRemove.add(tmp);
				}
			}

			// on parcourt la liste des Utilisateurs à retirer de
			// l'association
			for(int i = 0; i < utilsToRemove.size(); i++){
				final Utilisateur u = utilisateurDao.mergeObject(utilsToRemove.get(i));
				// on retire l'utilisateur de chaque coté de l'association
				pf.getUtilisateurs().remove(u);
				u.getPlateformes().remove(pf);

				log.debug("Suppression de l'association entre " + "l'utilisateur : " + u.toString() + " et la plateforme : "
						+ pf.toString());
			}

			// on parcourt la nouvelle liste d'utilisateurs
			for(int i = 0; i < utilisateurs.size(); i++){
				// si un utilisateur n'était pas associé a la pf
				if(!pf.getUtilisateurs().contains(utilisateurs.get(i))){
					// on ajoute l'utilisateur des deux cotés de l'association
					pf.getUtilisateurs().add(utilisateurDao.mergeObject(utilisateurs.get(i)));
					utilisateurDao.mergeObject(utilisateurs.get(i)).getPlateformes().add(pf);

					log.debug("Ajout de l'association entre " + "l'utilisateur : " + utilisateurs.get(i).toString()
							+ " et la plateforme : " + pf.toString());
				}
			}
		}
		return pf; // merged object
	}

	/**
	 * Cette méthode met à jour les associations entre une pf et
	 * une liste de Conteneurs.
	 * @param plateforme pour laquelle on veut mettre à jour
	 * les associations.
	 * @param conteneurs Liste de conteneurs 
	 * que l'on veut associer à la pf.
	 */
	private void updateConteneurs(final Plateforme plateforme, final List<Conteneur> conteneurs){

		if(conteneurs != null){
			final Plateforme pf = plateformeDao.mergeObject(plateforme);

			final Iterator<ConteneurPlateforme> it = pf.getConteneurPlateformes().iterator();
			final List<ConteneurPlateforme> contsToRemove = new ArrayList<>();
			// on parcourt les conteneurs qui sont actuellement associées
			// à la plateforme
			while(it.hasNext()){
				final ConteneurPlateforme tmp = it.next();
				// si un conteneur n'est pas dans la nouvelle liste, on
				// le conserve afin de le retirer par la suite
				if(!conteneurs.contains(tmp)){
					contsToRemove.add(tmp);
				}
			}

			// on parcourt la liste des conteneurs à retirer de
			// l'association
			for(int i = 0; i < contsToRemove.size(); i++){
				contsToRemove.get(i).setPartage(false);
				conteneurPlateformeDao.mergeObject(contsToRemove.get(i));
			}

			// on parcourt la nouvelle liste de conteneurs
			for(int i = 0; i < conteneurs.size(); i++){
				ConteneurPlateforme cp = new ConteneurPlateforme();
				final ConteneurPlateformePK pk = new ConteneurPlateformePK(conteneurs.get(i), pf);
				cp.setPk(pk);
				cp.setPartage(true);
				// si un conteneur n'était pas associé à la plateforme
				if(!pf.getConteneurPlateformes().contains(cp)){
					// on ajoute le conteneur dans l'association
					pf.getConteneurPlateformes().add(conteneurPlateformeDao.mergeObject(cp));

					log.debug("Ajout de l'association entre la plateforme : " + pf.toString() + " et le conteneur : "
							+ conteneurs.get(i).toString());
				}else{ // sinon on passe le partage a true
					cp = conteneurPlateformeDao.findById(pk);
					cp.setPartage(true);
					conteneurPlateformeDao.mergeObject(cp);
				}
			}
		}
	}

	@Override
	public void removeObjectManager(Plateforme pf, final String comments, final Utilisateur user, 
			final String basedir){
		if(pf != null){
			if(!isReferencedObjectManager(pf)) {
								
				//Supprime operations associes
				CreateOrUpdateUtilities.removeAssociateOperations(pf, operationManager, comments, user);
	
				updateAdministrateurs(pf, new ArrayList<Utilisateur>());
				
				plateformeDao.removeObject(pf.getPlateformeId());
				log.info("Suppression objet Plateforme " + pf.toString());
	
				// delete file system
				new File(basedir.concat("/pt_").concat(pf.getPlateformeId().toString()))
				.delete();
			}else{
				throw new ObjectReferencedException("plateforme.deletion.isReferenced", false);
			}
		}
	}

	@Override
	public boolean isReferencedObjectManager(final Plateforme pf){
		final Plateforme plateforme = plateformeDao.mergeObject(pf);

		return !plateforme.getBanques().isEmpty() || !plateforme.getConteneurPlateformes().isEmpty();

	}
}