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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.TableAnnotationBanqueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueTableCodageDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurEntiteTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.code.CodeDossierManager;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ExistingAnnotationValuesException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impression.TemplateManager;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.BanqueValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanquePK;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Banque.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class BanqueManagerImpl implements BanqueManager
{

   private final Logger log = LoggerFactory.getLogger(BanqueManager.class);

   private BanqueDao banqueDao;

   private PlateformeDao plateformeDao;

   private ContexteDao contexteDao;

   private BanqueValidator banqueValidator;

   private OperationTypeDao operationTypeDao;

   private OperationManager operationManager;

   private CollaborateurDao collaborateurDao;

   private ServiceDao serviceDao;

   private ConteneurDao conteneurDao;

   private ConteneurManager conteneurManager;

   private BanqueTableCodageDao banqueTableCodageDao;

   private CouleurEntiteTypeDao couleurEntiteTypeDao;

   private TableAnnotationBanqueDao tableAnnotationBanqueDao;

   private CouleurDao couleurDao;

   private TableAnnotationManager tableAnnotationManager;

   private EntiteDao entiteDao;

   private CodeSelectManager codeSelectManager;

   private CodeUtilisateurManager codeUtilisateurManager;

   private PrelevementManager prelevementManager;

   private EchantillonManager echantillonManager;

   private ProdDeriveManager prodDeriveManager;

   private CodeDossierManager codeDossierManager;

   private CessionManager cessionManager;

   private ImportTemplateManager importTemplateManager;

   private AnnotationValeurManager annotationValeurManager;

   private TemplateManager templateManager;

   private UtilisateurManager utilisateurManager;

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   public void setContexteDao(final ContexteDao cDao){
      this.contexteDao = cDao;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setBanqueValidator(final BanqueValidator bV){
      this.banqueValidator = bV;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public CollaborateurDao getCollaborateurDao(){
      return collaborateurDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setConteneurDao(final ConteneurDao cDao){
      this.conteneurDao = cDao;
   }

   public void setBanqueTableCodageDao(final BanqueTableCodageDao btcDao){
      this.banqueTableCodageDao = btcDao;
   }

   public void setCouleurEntiteTypeDao(final CouleurEntiteTypeDao cetDao){
      this.couleurEntiteTypeDao = cetDao;
   }

   public void setConteneurManager(final ConteneurManager cManager){
      this.conteneurManager = cManager;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   public void setTableAnnotationBanqueDao(final TableAnnotationBanqueDao tDao){
      this.tableAnnotationBanqueDao = tDao;
   }

   public void setTableAnnotationManager(final TableAnnotationManager tAManager){
      this.tableAnnotationManager = tAManager;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setCodeSelectManager(final CodeSelectManager cManager){
      this.codeSelectManager = cManager;
   }

   public void setCodeUtilisateurManager(final CodeUtilisateurManager cManager){
      this.codeUtilisateurManager = cManager;
   }

   public void setPrelevementManager(final PrelevementManager pManager){
      this.prelevementManager = pManager;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setCodeDossierManager(final CodeDossierManager cManager){
      this.codeDossierManager = cManager;
   }

   public void setCessionManager(final CessionManager cManager){
      this.cessionManager = cManager;
   }

   public void setImportTemplateManager(final ImportTemplateManager iManager){
      this.importTemplateManager = iManager;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager aManager){
      this.annotationValeurManager = aManager;
   }

   public void setTemplateManager(final TemplateManager tManager){
      this.templateManager = tManager;
   }

   public void setUtilisateurManager(final UtilisateurManager utilisateurManager){
      this.utilisateurManager = utilisateurManager;
   }

   /**
    * Recherche une Banque dont l'identifiant est passé en paramètre.
    * @param banqueId Identifiant de la banque que l'on recherche.
    * @return Une Transformation.
    */
   @Override
   public Banque findByIdManager(final Integer banqueId){
      return banqueDao.findById(banqueId);
   }

   /**
    * Recherche toutes les banques présentes dans la base.
    * @return Liste de Banques.
    */
   @Override
   public List<Banque> findAllObjectsManager(){
      return banqueDao.findByOrder();
   }

   /**
    * Recherche les prélèvements liés à la banque passée en paramètre.
    * @param banque Banque pour laquellle on recherche des prélèvements.
    * @return Liste de Prelevements.
    */
   @Override
   public Set<Prelevement> getPrelevementsManager(Banque banque){
      if(banque != null){
         banque = banqueDao.mergeObject(banque);
         final Set<Prelevement> prlvts = banque.getPrelevements();
         prlvts.size();

         return prlvts;
      }
      return new HashSet<>();
   }

   /**
    * Recherche les échantillons liés à la banque passée en paramètre.
    * @param banque Banque pour laquellle on recherche des échantillons.
    * @return Liste d'Echantillons.
    */
   @Override
   public Set<Echantillon> getEchantillonsManager(Banque banque){
      if(banque != null){
         banque = banqueDao.mergeObject(banque);
         final Set<Echantillon> echans = banque.getEchantillons();
         echans.size();

         return echans;
      }
      return new HashSet<>();
   }

   /**
    * Recherche les produit dérivés liés à la banque passée en paramètre.
    * @param banque Banque pour laquellle on recherche des produits dérivés.
    * @return Liste de ProdDerives.
    */
   @Override
   public Set<ProdDerive> getProdDerivesManager(Banque banque){
      if(banque != null){
         banque = banqueDao.mergeObject(banque);
         final Set<ProdDerive> derives = banque.getProdDerives();
         derives.size();

         return derives;
      }
      return new HashSet<>();
   }

   /**
    * Recherche les Services de stockage liés à la banque passée en paramètre.
    * @param banque Banque pour laquellle on recherche des produits dérivés.
    * @return Liste de Services.
    */
   @Override
   public Set<Service> getServicesStockageManager(Banque banque){
      if(banque != null){
         banque = banqueDao.mergeObject(banque);
         final Set<Service> services = new HashSet<>();
         final Set<Conteneur> conts = banque.getConteneurs();
         final Iterator<Conteneur> itor = conts.iterator();
         while(itor.hasNext()){
            services.add(itor.next().getService());
         }
         return services;
      }
      return new HashSet<>();
   }

   @Override
   public Set<Conteneur> getConteneursManager(Banque banque){
      if(banque != null){
         banque = banqueDao.mergeObject(banque);
         final Set<Conteneur> conts = banque.getConteneurs();
         conts.size();
         return conts;
      }
      return new HashSet<>();
   }

   @Override
   public List<Catalogue> findContexteCataloguesManager(final Integer banqueId){
      return banqueDao.findContexteCatalogues(banqueId);
   }

   @Override
   public List<Banque> findByPlateformeAndArchiveManager(final Plateforme pf, final Boolean archive){
      if(archive != null){
         return banqueDao.findByPlateformeAndArchive(pf, archive);
      }
      final List<Banque> banks = banqueDao.findByPlateformeAndArchive(pf, false);
      banks.addAll(banqueDao.findByPlateformeAndArchive(pf, true));
      Collections.sort(banks);
      return banks;
   }

   @Override
   public List<Banque> findByEntiteConsultByUtilisateurManager(final Utilisateur usr, final Entite entite, final Plateforme pf){
      if(!usr.isSuperAdmin()){
         final Set<Banque> banks = new HashSet<>(banqueDao.findByEntiteConsultByUtilisateur(usr, entite, pf));
         banks.addAll(banqueDao.findByUtilisateurIsAdmin(usr, pf));
         return new ArrayList<>(banks);
      } // since 2.0.13
        // archive = false since 2.1
      return banqueDao.findByPlateformeAndArchive(pf, false);
   }

   @Override
   public List<Banque> findByEntiteModifByUtilisateurManager(final Utilisateur usr, final Entite entite, final Plateforme pf){
      if(!usr.isSuperAdmin()){
         final List<Banque> banks = new ArrayList<>(banqueDao.findByEntiteModifByUtilisateur(usr, entite, pf));
         banks.addAll(banqueDao.findByUtilisateurIsAdmin(usr, pf));
         return new ArrayList<>(banks);
      } // since 2.0.13
        // archive = false since 2.1
      return banqueDao.findByPlateformeAndArchive(pf, false);
   }

   @Override
   public List<Banque> findByAutoriseCrossPatientManager(final boolean autoriseCross){
      return banqueDao.findByAutoriseCrossPatient(autoriseCross);
   }

   @Override
   public List<Banque> findByUtilisateurIsAdminManager(final Utilisateur usr, final Plateforme pf){

      if(usr.isSuperAdmin()){
         // archive = false since 2.1
         return banqueDao.findByPlateformeAndArchive(pf, false);
      }

      return banqueDao.findByUtilisateurIsAdmin(usr, pf);
   }

   @Override
   public List<Banque> findByUtilisateurAndPFManager(final Utilisateur usr, final Plateforme pf){
      if(usr != null && pf != null){
         return banqueDao.findByUtilisateurAndPF(usr, pf);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final Banque banque){
      if(banque.getBanqueId() == null){
         return banqueDao.findByNom(banque.getNom()).contains(banque);
      }
      return banqueDao.findByExcludedId(banque.getBanqueId()).contains(banque);
   }

   @Override
   public void createOrUpdateObjectManager(final Banque banque, final Plateforme pf, final Contexte contexte,
      final Service service, final Collaborateur responsable, final Collaborateur contact, final List<Conteneur> conteneurs,
      final List<BanqueTableCodage> codifications, final List<TableAnnotation> tablesPatient,
      final List<TableAnnotation> tablesPrlvt, final List<TableAnnotation> tablesEchan, final List<TableAnnotation> tablesDerive,
      final List<TableAnnotation> tablesCess, final List<CouleurEntiteType> coulTypes, final Couleur couleurEchan,
      final Couleur couleurDerive, final Utilisateur utilisateur, final Set<Utilisateur> utilisateursList, final String operation,
      final String basedir){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Plateforme required
      if(pf != null){
         banque.setPlateforme(plateformeDao.mergeObject(pf));
      }else if(banque.getPlateforme() == null){
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la " + operation + " d'une Banque");
         throw new RequiredObjectIsNullException("Banque", operation, "Plateforme");
      }
      if(contexte != null){
         banque.setContexte(contexteDao.mergeObject(contexte));
      }else if(banque.getContexte() == null){
         log.warn("Objet obligatoire Contexte manquant" + " lors de la " + operation + " d'une Banque");
         throw new RequiredObjectIsNullException("Banque", operation, "Contexte");
      }

      //Validation
      BeanValidator.validateObject(banque, new Validator[] {banqueValidator});

      //Doublon
      if(!findDoublonManager(banque)){
         if(service != null){
            banque.setProprietaire(serviceDao.mergeObject(service));
         }else{
            banque.setProprietaire(null);
         }
         if(responsable != null){
            banque.setCollaborateur(collaborateurDao.mergeObject(responsable));
         }else{
            banque.setCollaborateur(null);
         }
         if(contact != null){
            banque.setContact(collaborateurDao.mergeObject(contact));
         }else{
            banque.setContact(null);
         }
         if(couleurEchan != null){
            banque.setEchantillonCouleur(couleurDao.mergeObject(couleurEchan));
         }else{
            banque.setEchantillonCouleur(null);
         }
         if(couleurDerive != null){
            banque.setProdDeriveCouleur(couleurDao.mergeObject(couleurDerive));
         }else{
            banque.setProdDeriveCouleur(null);
         }

         if(operation.equals("creation") || operation.equals("modification")){

            OperationType oType;

            try{
               if(operation.equals("creation")){
                  banqueDao.createObject(banque);
                  log.info("Enregistrement objet Banque " + banque.toString());

                  oType = operationTypeDao.findByNom("Creation").get(0);

                  // creation filesystem
                  manageFileSystemForBanque(basedir, banque, false);
               }else{
                  banqueDao.updateObject(banque);
                  log.info("Modification objet Banque " + banque.toString());

                  oType = operationTypeDao.findByNom("Modification").get(0);
               }

               CreateOrUpdateUtilities.createAssociateOperation(banque, operationManager, oType, utilisateur);

               // ajout association vers conteneurs
               if(conteneurs != null){
                  updateConteneurs(banque, conteneurs);
               }
               // ajout association vers codifications
               if(codifications != null){
                  updateCodifications(banque, codifications);
               }
               // ajout association vers tableAnnotations
               if(tablesPatient != null){
                  updateTablesAnnot(banque, tablesPatient,
                     tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), banque));
               }
               if(tablesPrlvt != null){
                  updateTablesAnnot(banque, tablesPrlvt,
                     tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Prelevement").get(0), banque));
               }
               if(tablesEchan != null){
                  updateTablesAnnot(banque, tablesEchan,
                     tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Echantillon").get(0), banque));
               }
               if(tablesDerive != null){
                  updateTablesAnnot(banque, tablesDerive,
                     tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("ProdDerive").get(0), banque));
               }
               if(tablesCess != null){
                  updateTablesAnnot(banque, tablesCess,
                     tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Cession").get(0), banque));
               }
               // ajout association vers couleurEntiteTypes
               if(coulTypes != null){
                  updateCoulTypes(banque, coulTypes);
               }

               //Mise à jour des profils utilisateur sur la banque
               if(utilisateursList != null && !utilisateursList.isEmpty()){

                  for(final Utilisateur userToUpdate : utilisateursList){

                     final Set<ProfilUtilisateur> profilsUtilisateur = userToUpdate.getProfilUtilisateurs();

                     //Pas idéal mais l'Utilisateur contient un set de ProfilUtilisateur qui lui-même contient l'Utilisateur,
                     //on se retrouve donc avec une référence circulaire qu'on est obligé de briser avant de pouvoir sauvegarder
                     //TODO Revoir la gestion des profils
                     final List<ProfilUtilisateur> newProfils = new ArrayList<>(userToUpdate.getProfilUtilisateurs());
                     userToUpdate.setProfilUtilisateurs(new HashSet<>());

                     utilisateurManager.updateObjectManager(userToUpdate, userToUpdate.getCollaborateur(), newProfils,
                        new ArrayList<>(userToUpdate.getPlateformes()), utilisateur, oType);

                     userToUpdate.setProfilUtilisateurs(profilsUtilisateur);

                  }

               }

            }catch(final RuntimeException re){
               // rollback du a erreur dans creation systeme fichier
               if(operation.equals("creation")){
                  banque.setBanqueId(null);
               }
               throw re;
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet Banque " + banque.toString());
         throw new DoublonFoundException("Banque", operation);
      }
   }

   /**
    * Cette méthode met à jour les associations entre une banque et
    * une liste de Conteneurs.
    * @param banque pour laquelle on veut mettre à jour
    * les associations.
    * @param conteneurs Liste de conteneurs
    * que l'on veut associer à la banque.
    * @return la banque dont les associations sont modifiées
    */
   private void updateConteneurs(final Banque banque, final List<Conteneur> conteneurs){

      final Banque bank = banqueDao.mergeObject(banque);

      final Iterator<Conteneur> it = bank.getConteneurs().iterator();
      final List<Conteneur> contsToRemove = new ArrayList<>();
      // on parcourt les conteneurs qui sont actuellement associées
      // à la banque
      while(it.hasNext()){
         final Conteneur tmp = it.next();
         // si un conteneur n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!conteneurs.contains(tmp)){
            contsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des conteneurs à retirer de
      // l'association
      for(int i = 0; i < contsToRemove.size(); i++){
         final Conteneur cont = conteneurDao.mergeObject(contsToRemove.get(i));
         // on retire le conteneur de l'association
         // en prenant de soin de supprimer les réservations au niveau
         // des enceintes
         //cont.getBanques().remove(bank);
         conteneurManager.removeBanqueFromContAndEncManager(cont, bank);

         log.debug("Suppression de l'association entre la banque : " + bank.toString() + " et le conteneur : " + cont.toString());
      }

      // on parcourt la nouvelle liste de conteneurs
      for(int i = 0; i < conteneurs.size(); i++){
         // si un conteneur n'était pas associé à la banque
         if(!bank.getConteneurs().contains(conteneurs.get(i))){
            // on ajoute le conteneur dans l'association
            bank.getConteneurs().add(conteneurDao.mergeObject(conteneurs.get(i)));

            log.debug("Ajout de l'association entre la banque : " + bank.toString() + " et le conteneur : "
               + conteneurs.get(i).toString());
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre une banque et
    * une liste de TableCodage.
    * @param banque pour laquelle on veut mettre à jour
    * les associations.
    * @param codifications Liste de TableCodage
    * que l'on veut associer à la banque.
    */
   private void updateCodifications(final Banque banque, final List<BanqueTableCodage> codifications){

      final Banque bank = banqueDao.mergeObject(banque);

      final Set<BanqueTableCodage> btc = bank.getBanqueTableCodages();
      final Iterator<BanqueTableCodage> it = btc.iterator();
      final List<BanqueTableCodage> tableToRemove = new ArrayList<>();
      // on parcourt les tables qui sont actuellement associées
      // à la banque
      while(it.hasNext()){
         final BanqueTableCodage tmp = it.next();
         // si une table n'est pas dans la nouvelle liste, on
         // la conserve afin de le retirer par la suite
         if(!codifications.contains(tmp)){
            tableToRemove.add(tmp);
         }
      }

      // on parcourt la liste des tables à retirer de
      // l'association
      for(int i = 0; i < tableToRemove.size(); i++){
         final BanqueTableCodage tab = banqueTableCodageDao.mergeObject(tableToRemove.get(i));
         // on retire la table de l'association
         bank.getBanqueTableCodages().remove(tab);
         banqueTableCodageDao.removeObject(tab.getPk());

         log.debug(
            "Suppression de l'association entre la banque : " + bank.toString() + " et la codification : " + tab.toString());
      }

      // on parcourt la nouvelle liste de tables
      for(int i = 0; i < codifications.size(); i++){
         codifications.get(i).setBanque(bank);
         // si une table n'était pas associée à la banque
         if(!bank.getBanqueTableCodages().contains(codifications.get(i))){
            // on ajoute la table dans l'association
            bank.getBanqueTableCodages().add(banqueTableCodageDao.mergeObject(codifications.get(i)));

            log.debug("Ajout de l'association entre la banque : " + banque.toString() + " et la codification : "
               + codifications.get(i).toString());
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre une banque et
    * une liste de TableAnnotation.
    * @param banque pour laquelle on veut mettre à jour
    * les associations.
    * @param tableAnnotation Liste ordonnée des tables
    * @param liste de tables à l'origine associée à la banque (avant modif)
    * que l'on veut associer à la banque.
    */
   private void updateTablesAnnot(final Banque banque, final List<TableAnnotation> tables, final List<TableAnnotation> inits){

      final Banque bank = banqueDao.mergeObject(banque);

      final Iterator<TableAnnotation> it = inits.iterator();
      final List<TableAnnotationBanque> tabsToRemove = new ArrayList<>();
      // on parcourt les tables qui sont actuellement associées
      // à la banque
      while(it.hasNext()){
         final TableAnnotation tmp = it.next();
         // si une table n'est pas dans la nouvelle liste, on
         // la conserve afin de la retirer par la suite
         if(!tables.contains(tmp)){
            tabsToRemove.add(tableAnnotationBanqueDao.findById(new TableAnnotationBanquePK(bank, tmp)));
         }
      }

      // on parcourt la liste des tables à retirer de
      // l'association
      Long annoCount;
      for(int i = 0; i < tabsToRemove.size(); i++){
         annoCount =
            annotationValeurManager.findCountByTableAnnotationBanqueManager(tabsToRemove.get(i).getTableAnnotation(), banque);
         // lance une erreur si une table devant être supprimée
         // contient des valeurs
         if(annoCount != null && annoCount > 0){
            throw new ExistingAnnotationValuesException(tabsToRemove.get(i).getTableAnnotation(), banque);
         }

         final TableAnnotationBanque tab = tableAnnotationBanqueDao.mergeObject(tabsToRemove.get(i));
         // on retire la table de l'association et on le supprime
         bank.getTableAnnotationBanques().remove(tab);
         tableAnnotationBanqueDao.removeObject(tab.getPk());

         log.debug("Suppression de l'association entre la banque : " + bank.toString() + " et suppression de la table : "
            + tab.toString());
      }

      // on parcourt la nouvelle liste de tables
      for(int i = 0; i < tables.size(); i++){
         TableAnnotationBanque tAb = new TableAnnotationBanque();
         final TableAnnotationBanquePK pk = new TableAnnotationBanquePK(bank, tables.get(i));
         tAb.setPk(pk);
         tAb.setOrdre(i + 1);
         // si une table n'était pas associée à la banque
         if(!bank.getTableAnnotationBanques().contains(tAb)){
            // on ajoute la table dans l'association dans le bon ordre
            bank.getTableAnnotationBanques().add(tableAnnotationBanqueDao.mergeObject(tAb));

            log.debug(
               "Ajout de l'association entre la banque : " + bank.toString() + " et la table : " + tables.get(i).toString());
         }else{ // on modifie l'ordre de la table present avec la liste
            tAb = tableAnnotationBanqueDao.findById(pk);
            tAb.setOrdre(i + 1);
            tableAnnotationBanqueDao.mergeObject(tAb);
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre une banque et
    * une liste de CouleurEntiteTypes.
    * @param banque pour laquelle on veut mettre à jour
    * les associations.
    * @param coulTypes Liste des couleurs entite types
    * que l'on veut associer à la banque.
    */
   private void updateCoulTypes(final Banque banque, final List<CouleurEntiteType> coulTypes){

      final Banque bank = banqueDao.mergeObject(banque);

      final Iterator<CouleurEntiteType> it = bank.getCouleurEntiteTypes().iterator();
      final List<CouleurEntiteType> coulsToRemove = new ArrayList<>();
      // on parcourt les couleurs qui sont actuellement associées
      // à la banque
      while(it.hasNext()){
         final CouleurEntiteType tmp = it.next();
         // si une couleur n'est pas dans la nouvelle liste, on
         // la conserve afin de la retirer par la suite
         if(!coulTypes.contains(tmp)){
            coulsToRemove.add(tmp);
         }
      }

      // on parcourt la liste des couleurs à retirer de
      // l'association
      for(int i = 0; i < coulsToRemove.size(); i++){
         final CouleurEntiteType coul = couleurEntiteTypeDao.mergeObject(coulsToRemove.get(i));
         // on retire la couleur de l'association et on supprime l'assoce
         bank.getCouleurEntiteTypes().remove(coul);
         couleurEntiteTypeDao.removeObject(coul.getCouleurEntiteTypeId());

         log.debug("Suppression de l'association entre la banque : " + bank.toString() + " et suppression de la couleur "
            + " entite type : " + coul.toString());
      }

      // on parcourt la nouvelle liste de couleurs
      for(int i = 0; i < coulTypes.size(); i++){
         // si une table n'était pas associée à la banque
         if(!bank.getCouleurEntiteTypes().contains(coulTypes.get(i))){
            // on ajoute la couleur dans l'association
            coulTypes.get(i).setBanque(bank);
            bank.getCouleurEntiteTypes().add(couleurEntiteTypeDao.mergeObject(coulTypes.get(i)));

            log.debug("Ajout de l'association entre la banque : " + bank.toString() + " et la couleur entite type : "
               + coulTypes.get(i).toString());
         }else{ // on modifie la couleur
            couleurEntiteTypeDao.mergeObject(coulTypes.get(i));
         }
      }
   }

   @Override
   public void removeObjectManager(Banque banque, final String comments, final Utilisateur user, final String basedir,
      final boolean force){
      if(banque != null){
         if(!isReferencedObjectManager(banque) || force){

            final List<File> filesToDelete = new ArrayList<>();

            banque = banqueDao.mergeObject(banque);

            //remove cascade codes
            final Iterator<CodeSelect> codeSelIt = banque.getCodeSelects().iterator();
            while(codeSelIt.hasNext()){
               codeSelectManager.removeObjectManager(codeSelIt.next());
            }
            final Iterator<CodeUtilisateur> codeUIt = codeUtilisateurManager.findByRootDossierManager(banque).iterator();

            while(codeUIt.hasNext()){
               codeUtilisateurManager.removeObjectManager(codeUIt.next());
            }
            final List<CodeDossier> dossiers = new ArrayList<>();
            dossiers.addAll(codeDossierManager.findByRootDossierBanqueManager(banque, true));
            dossiers.addAll(codeDossierManager.findByRootDossierBanqueManager(banque, false));

            final Iterator<CodeDossier> dosIt = dossiers.iterator();
            while(dosIt.hasNext()){
               codeDossierManager.removeObjectManager(dosIt.next());
            }

            final Iterator<ImportTemplate> impIt = banque.getImportTemplate().iterator();
            while(impIt.hasNext()){
               importTemplateManager.removeObjectManager(impIt.next());
            }

            final Iterator<Template> tempIt = banque.getTemplates().iterator();
            while(tempIt.hasNext()){
               templateManager.removeObjectManager(tempIt.next());
            }

            // suppression totale de la banque et son contenu
            if(force){
               final Iterator<Cession> cesIt = banque.getCessions().iterator();
               while(cesIt.hasNext()){
                  cessionManager.removeObjectManager(cesIt.next(), "Cascade depuis suppression banque " + banque.getNom(), user,
                     filesToDelete);
               }
               banque.getCessions().clear();

               final Iterator<ProdDerive> derIt = banque.getProdDerives().iterator();
               while(derIt.hasNext()){
                  prodDeriveManager.removeObjectCascadeManager(derIt.next(),
                     "Cascade depuis suppression banque " + banque.getNom(), user, filesToDelete);
               }
               banque.getProdDerives().clear();

               final Iterator<Echantillon> echanIt = banque.getEchantillons().iterator();

               while(echanIt.hasNext()){
                  echantillonManager.removeObjectCascadeManager(echanIt.next(),
                     "Cascade depuis suppression banque " + banque.getNom(), user, filesToDelete);
               }
               banque.getEchantillons().clear();

               final Iterator<Prelevement> prelIt = banque.getPrelevements().iterator();
               while(prelIt.hasNext()){
                  prelevementManager.removeObjectCascadeManager(prelIt.next(),
                     "Cascade depuis suppression banque " + banque.getNom(), user, filesToDelete);
               }
               banque.getPrelevements().clear();
            }

            // suppr valeurs annotations PATIENT pour cette banque
            final Iterator<TableAnnotation> tablesPatIt =
               tableAnnotationManager.findByEntiteAndBanqueManager(entiteDao.findByNom("Patient").get(0), banque).iterator();
            while(tablesPatIt.hasNext()){
               annotationValeurManager.removeAnnotationValeurListManager(
                  annotationValeurManager.findByTableAndBanqueManager(tablesPatIt.next(), banque), filesToDelete);
            }

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(banque, operationManager, comments, user);

            banqueDao.removeObject(banque.getBanqueId());
            log.info("Suppression objet Banque " + banque.toString());

            for(final SModele mod : banque.getSModeles()){
               mod.getBanques().remove(banque);
            }

            for(final File f : filesToDelete){
               f.delete();
            }

            // deletion filesystem
            manageFileSystemForBanque(basedir, banque, true);
         }else{
            throw new ObjectReferencedException("banque.deletion.isReferenced", false);
         }
      }
   }

   @Override
   public List<Banque> findByProfilUtilisateurManager(final Utilisateur u){
      return banqueDao.findByProfilUtilisateur(u);
   }

   @Override
   public List<BanqueTableCodage> getBanqueTableCodageByBanqueManager(final Banque banque){
      return banqueTableCodageDao.findByBanque(banque);
   }

   @Override
   public Set<Imprimante> getImprimantesManager(final Banque banque){
      if(banque != null){
         //banque = banqueDao.mergeObject(banque);
         //Set<Imprimante> imps = banque.getImprimantes();
         //imps.size();
         return new HashSet<>();
      }
      return new HashSet<>();
   }

   @Override
   public boolean isReferencedObjectManager(final Banque bank){
      final Banque banque = banqueDao.mergeObject(bank);

      return !banque.getPrelevements().isEmpty() || !banque.getEchantillons().isEmpty() || !banque.getProdDerives().isEmpty()
         || !banque.getCessions().isEmpty();

   }

   /**
    * Construit ou detruit tout le filesystem qui permet l'enregistrement
    * de fichiers conjointement aux banques.
    * @param baseDir
    * @param bank
    * @param delete
    * @throws FileNotFoundException
    */
   private void manageFileSystemForBanque(final String basedir, final Banque bank, final boolean delete){
      final String path = Utils.writeAnnoFilePath(basedir, bank, null, null);
      if(!delete){
         if(!new File(path).exists()){
            if(new File(path + "/anno").mkdirs()){
               log.info("Creation file system " + path + "/anno");
            }else{
               log.error("Erreur dans la creation du systeme de fichier anno pour la banque " + bank.toString());
               throw new RuntimeException("banque.filesystem.anno.error");
            }
            if(new File(path + "/cr_anapath").mkdirs()){
               log.info("Creation file system " + path + "/cr_anapath");
            }else{
               log.error("Erreur dans la creation du systeme de fichier anapath pour la banque " + bank.toString());
               throw new RuntimeException("banque.filesystem.anapath.error");
            }
         }else{
            log.error("Le systeme de fichier pour la banque " + bank.toString() + "existes deja");
            throw new RuntimeException("banque.filesystem.exists.error");
         }
      }else{
         if(Utils.deleteDirectory(new File(path))){
            log.info("Filesystem complet supprimé pour la banque " + bank.toString());
         }else{
            log.error("Erreur dans la suppression du systeme de fichier anapath pour la banque " + bank.toString());
            throw new RuntimeException("banque.filesystem.delete.error");
         }
      }
   }

   @Override
   public List<Banque> findBanqueForSwitchManager(final Prelevement p, final Utilisateur u){

      final List<Banque> banks = new ArrayList<>();

      if(p != null && u != null){
         final List<Banque> adminBanks = new ArrayList<>();

         // premiere restriction sur les banques de la plateforme
         // TK-254 et stream filter restriction sur le contexte
         adminBanks.addAll(findByUtilisateurIsAdminManager(u, p.getBanque().getPlateforme()).stream()
            .filter(b -> b.getContexte().equals(p.getBanque().getContexte())).collect(Collectors.toList()));

         // @since 2.3.0-gatsbi
         // filtre gatsbi même étude
         // banque de contexte non gatsbi sont déja filtrées
         if(p.getBanque().getEtude() != null){
            adminBanks.removeIf(b -> !p.getBanque().getEtude().equals(b.getEtude()));
         }

         // deuxieme restriction sur les banques - conteneurs.
         final List<TKAnnotableObject> children = prelevementManager.getPrelevementChildrenManager(p);
         final Set<Banque> contBanks = new HashSet<>();

         Conteneur cont;
         for(int i = 0; i < children.size(); i++){
            cont = null;
            if(children.get(i) instanceof Echantillon){
               if(((Echantillon) children.get(i)).getEmplacement() != null){
                  cont = conteneurManager.findFromEmplacementManager(((Echantillon) children.get(i)).getEmplacement());
               }
            }else{
               if(((ProdDerive) children.get(i)).getEmplacement() != null){
                  cont = conteneurManager.findFromEmplacementManager(((ProdDerive) children.get(i)).getEmplacement());
               }
            }
            if(cont != null){
               contBanks.addAll(conteneurManager.getBanquesManager(cont));
            }
         }

         if(!contBanks.isEmpty()){
            contBanks.remove(p.getBanque());
            banks.addAll(CollectionUtils.intersection(new ArrayList<>(contBanks), adminBanks));
         }else{
            adminBanks.remove(p.getBanque());
            banks.addAll(adminBanks);
         }
         Collections.sort(banks);
      }
      return banks;
   }

   @Override
   public List<Banque> findByConteneurManager(final Conteneur c1){
      if(c1 != null && c1.getConteneurId() != null){
         return banqueDao.findByConteneur(c1);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Banque> findByEtudeManager(final Etude e){
      return banqueDao.findByEtude(e);
   }
}