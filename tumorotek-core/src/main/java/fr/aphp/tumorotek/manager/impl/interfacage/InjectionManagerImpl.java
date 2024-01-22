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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.InjectionManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public class InjectionManagerImpl implements InjectionManager
{

   private final Log log = LogFactory.getLog(InjectionManager.class);

   private EntityManagerFactory entityManagerFactory;

   private ChampEntiteDao champEntiteDao;

   private ChampAnnotationDao champAnnotationDao;

   private ValeurExterneManager valeurExterneManager;

   private BlocExterneManager blocExterneManager;

   private EntiteDao entiteDao;

   private PatientManager patientManager;

   private PatientDao patientDao;

   private MaladieManager maladieManager;

   private MaladieDao maladieDao;

   private DossierExterneManager dossierExterneManager;

   private PrelevementManager prelevementManager;

   private ProdDeriveManager prodDeriveManager;

   private ObjetStatutDao objetStatutDao;

   private DossierExterneDao dossierExterneDao;

   private BanqueManager banqueManager;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setChampEntiteDao(final ChampEntiteDao cDao){
      this.champEntiteDao = cDao;
   }

   public void setChampAnnotationDao(final ChampAnnotationDao cDao){
      this.champAnnotationDao = cDao;
   }

   public void setValeurExterneManager(final ValeurExterneManager vManager){
      this.valeurExterneManager = vManager;
   }

   public void setBlocExterneManager(final BlocExterneManager bManager){
      this.blocExterneManager = bManager;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setPatientManager(final PatientManager pManager){
      this.patientManager = pManager;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setMaladieManager(final MaladieManager mManager){
      this.maladieManager = mManager;
   }

   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   public void setDossierExterneManager(final DossierExterneManager _d){
      this.dossierExterneManager = _d;
   }

   public void setPrelevementManager(final PrelevementManager _p){
      this.prelevementManager = _p;
   }

   public void setProdDeriveManager(final ProdDeriveManager _p){
      this.prodDeriveManager = _p;
   }

   public void setObjetStatutDao(final ObjetStatutDao _o){
      this.objetStatutDao = _o;
   }

   public void setDossierExterneDao(final DossierExterneDao _d){
      this.dossierExterneDao = _d;
   }

   public void setBanqueManager(final BanqueManager _b){
      this.banqueManager = _b;
   }

   public void setOperationManager(final OperationManager _o){
      this.operationManager = _o;
   }

   public void setOperationTypeDao(final OperationTypeDao _o){
      this.operationTypeDao = _o;
   }

   @Override
   public Object extractValueForOneThesaurus(final ChampEntite champEntite, final Banque banque, final Object valeur){
      Object resultat = null;
      List<Object> objets = new ArrayList<>();

      if(champEntite != null && valeur != null && banque != null){

         //gestion du système de transcodage nécessaire pour certains champs dont la valeur ne peut pas être prise telle quelle
         if(champEntite.getEntite().getNom().equals("Etablissement") || champEntite.getEntite().getNom().equals("Collaborateur")
            || champEntite.getEntite().getNom().equals("Service")){

            Integer id = 0;
            if(valeur instanceof String){
               id = Integer.parseInt((String) valeur);
            }else{
               id = (Integer) valeur;
            }

            // création de la requête
            final String nomChampId =
               champEntite.getEntite().getNom().replaceFirst(".", (champEntite.getEntite().getNom().charAt(0) + "").toLowerCase())
                  + "Id";

            final StringBuffer sql = new StringBuffer();
            sql.append("SELECT e FROM ");
            sql.append(champEntite.getEntite().getNom());
            sql.append(" as e where e.");
            sql.append(nomChampId);
            sql.append(" = :valeur");

            final EntityManager em = entityManagerFactory.createEntityManager();
            final TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);
            query.setParameter("valeur", id);

            objets = query.getResultList();
            em.close();

         }else{
            // creation de la requête permettant de récupérer
            // les valeurs du thésaurus
            // on formate le champ du thésaurus à extraire
            // final String nomChamp = champEntite.getNom().replaceFirst(".", (champEntite.getNom().charAt(0) + "").toLowerCase());

            final StringBuffer sql = new StringBuffer();
            sql.append("SELECT e FROM ");
            sql.append(champEntite.getEntite().getNom());
            sql.append(" as e where e.nom");
            //            sql.append(nomChamp);
            sql.append(" = :valeur");

            if(!champEntite.getEntite().getNom().equals("Transporteur") && !champEntite.getEntite().getNom().equals("Unite")
               && !champEntite.getEntite().getNom().equals("ObjetStatut")){
               sql.append(" and e.plateforme = :pf");
            }

            final EntityManager em = entityManagerFactory.createEntityManager();
            final TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);
            query.setParameter("valeur", valeur);
            if(!champEntite.getEntite().getNom().equals("Transporteur") && !champEntite.getEntite().getNom().equals("Unite")
               && !champEntite.getEntite().getNom().equals("ObjetStatut")){
               query.setParameter("pf", banque.getPlateforme());
            }

            objets = query.getResultList();
            em.close();

         }

         if(objets.size() > 0){
            resultat = objets.get(0);
         }
      }

      return resultat;
   }

   @Override
   public Object extractValueForOneAnnotationThesaurus(final ChampAnnotation champAnnotation, final Banque banque,
      final String valeur){
      Object resultat = null;

      if(champAnnotation != null && valeur != null && banque != null){
         // creation de la requête permettant de récupérer
         // l'item
         final StringBuffer sql = new StringBuffer();
         sql.append("SELECT i FROM Item as i");
         sql.append(" where i.champAnnotation = :champ");
         sql.append(" and i.label = :valeur");

         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);
         query.setParameter("champ", champAnnotation);
         query.setParameter("valeur", valeur);

         final List<Object> objets = query.getResultList();
         em.close();

         if(objets.size() > 0){
            resultat = objets.get(0);
         }
      }
      return resultat;
   }

   @Override
   public void setPropertyValueForObject(final Object value, final ChampEntite attribut, final Object obj){
      if(attribut != null && obj != null){
         // on formate l'attribut pour qu'il corresponde à celui
         // de l'objet
         String nomChamp = attribut.getNom().replaceFirst(".", (attribut.getNom().charAt(0) + "").toLowerCase());
         if(nomChamp.endsWith("Id")){
            nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
         }

         try{
            if(value != null){
               // si la valeur à setter n'est pas un thésaurus
               if(value.getClass().getSimpleName().equals("String")){
                  final String type = PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType().getSimpleName();
                  // set d'un string
                  if(type.equals("String")){
                     PropertyUtils.setSimpleProperty(obj, nomChamp, value);
                  }else if(type.equals("Integer")){
                     // si l'attibut est un integer, on caste
                     // la valeur issue du fichier
                     Integer tmp = null;
                     try{
                        tmp = Integer.parseInt((String) value);
                     }catch(final NumberFormatException n){
                        //throw new WrongImportValueException(
                        //		colonne, "Integer");
                     }
                     PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
                  }else if(type.equals("Float")){
                     // si l'attibut est un float, on caste
                     // la valeur issue du fichier
                     Float tmp = null;
                     try{
                        tmp = Float.parseFloat((String) value);
                        tmp = Utils.floor(tmp, 3);
                     }catch(final NumberFormatException n){
                        //throw new WrongImportValueException(
                        //		colonne, "Float");
                     }
                     PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
                  }else if(type.equals("Boolean")){
                     // si l'attibut est un boolean, on caste
                     // la valeur issue du fichier
                     Boolean tmp = null;
                     if(value.equals("1")){
                        tmp = true;
                     }else if(value.equals("0")){
                        tmp = false;
                     }else{
                        //throw new WrongImportValueException(
                        //		colonne, "Boolean");
                     }
                     PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
                  }else if(type.equals("Date")){
                     // si l'attibut est une date, on caste
                     // la valeur issue du fichier
                     Date date = null;
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                     try{
                        date = sdf.parse((String) value);
                     }catch(final ParseException e){
                        sdf = new SimpleDateFormat("yyyyMMddHHmm");
                        try{
                           date = sdf.parse((String) value);
                        }catch(final ParseException e1){
                           sdf = new SimpleDateFormat("yyyyMMdd");
                           try{
                              date = sdf.parse((String) value);
                           }catch(final ParseException e2){}
                        }
                     }

                     PropertyUtils.setSimpleProperty(obj, nomChamp, date);
                  }else if(type.equals("Calendar")){
                     // si l'attibut est un calendar, on caste
                     // la valeur issue du fichier
                     Date date = null;
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                     try{
                        date = sdf.parse((String) value);
                     }catch(final ParseException e){
                        sdf = new SimpleDateFormat("yyyyMMddHHmm");
                        try{
                           date = sdf.parse((String) value);
                        }catch(final ParseException e1){
                           sdf = new SimpleDateFormat("yyyyMMdd");
                           try{
                              date = sdf.parse((String) value);
                           }catch(final ParseException e2){}
                        }
                     }
                     Calendar cal = Calendar.getInstance();
                     if(date != null){
                        cal.setTime(date);
                     }else{
                        cal = null;
                     }
                     PropertyUtils.setSimpleProperty(obj, nomChamp, cal);
                  }
               }else{
                  // si l'attibut est un thésaurus, on ajoute
                  // la valeur directement
                  PropertyUtils.setSimpleProperty(obj, nomChamp, value);
               }
            }else{
               // sinon on set un null
               PropertyUtils.setSimpleProperty(obj, nomChamp, null);
            }
         }catch(final IllegalAccessException e){
            log.error(e);
         }catch(final InvocationTargetException e){
            log.error(e);
         }catch(final NoSuchMethodException e){
            log.error(e);
         }
      }
   }

   @Override
   public AnnotationValeur setPropertyValueForAnnotationValeur(final Object value, final ChampAnnotation annotation,
      final Banque banque){
      AnnotationValeur annoValeur = new AnnotationValeur();

      if(value != null && annotation != null){
         // si la valeur à setter n'est pas un thésaurus
         if(value.getClass().getSimpleName().equals("String")){
            final DataType dt = annotation.getDataType();

            if(dt.getType().equals("alphanum") || dt.getType().equals("num") || dt.getType().equals("hyperlien")){
               annoValeur.setAlphanum((String) value);
            }else if(dt.getType().equals("boolean")){
               // si l'attibut est un boolean, on caste
               // la valeur issue du fichier
               Boolean tmp = null;
               if(value.equals("1")){
                  tmp = true;
               }else if(value.equals("0")){
                  tmp = false;
               }else{
                  //throw new WrongImportValueException(
                  //		colonne, "Boolean");
               }
               if(tmp != null){
                  annoValeur.setBool(tmp);
               }else{
                  annoValeur = null;
               }
            }else if(dt.getType().matches("date.*")){
               // si l'attibut est un calendar, on caste
               // la valeur issue du fichier
               Date date = null;
               SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
               try{
                  date = sdf.parse((String) value);
               }catch(final ParseException e){
                  sdf = new SimpleDateFormat("yyyyMMddHHmm");
                  try{
                     date = sdf.parse((String) value);
                  }catch(final ParseException e1){
                     sdf = new SimpleDateFormat("yyyyMMdd");
                     try{
                        date = sdf.parse((String) value);
                     }catch(final ParseException e2){}
                  }
               }
               Calendar cal = Calendar.getInstance();
               if(date != null){
                  cal.setTime(date);
               }else{
                  cal = null;
               }
               if(cal != null){
                  annoValeur.setDate(cal);
               }else{
                  annoValeur = null;
               }
            }else if(dt.getType().equals("texte")){
               annoValeur.setTexte((String) value);
            }
         }else{
            annoValeur.setItem((Item) value);
         }
      }

      if(annoValeur != null){
         annoValeur.setChampAnnotation(annotation);
         annoValeur.setBanque(banque);
      }

      return annoValeur;
   }

   @Override
   public void injectValeurExterneInObject(final Object obj, final Banque banque, final ValeurExterne valeurExterne,
      final List<AnnotationValeur> annoValeurs){
      if(obj != null && banque != null && valeurExterne != null){
         // si la valeur concerne un champentite
         if(valeurExterne.getChampEntiteId() != null){
            final ChampEntite champ = champEntiteDao.findById(valeurExterne.getChampEntiteId());
            if(champ != null){
               // on va extraire le type de l'attibut à remplir
               String nomChamp = champ.getNom().replaceFirst(".", (champ.getNom().charAt(0) + "").toLowerCase());
               if(nomChamp.endsWith("Id")){
                  nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
               }

               try{
                  // on vérifie que le champ se trouve bien dans
                  // l'objet
                  if(PropertyUtils.getPropertyDescriptor(obj, nomChamp) != null){
                     Object value = null;
                     if(valeurExterne.getValeur() != null && !valeurExterne.getValeur().equals("")){
                        // si le champ de la colonne est un thésaurus
                        if(champ.getQueryChamp() != null){
                           if(champ.getDataType().getDataTypeId() != 10){ // tous types sauf liste multiple
                              value = extractValueForOneThesaurus(champ.getQueryChamp(), banque, valeurExterne.getValeur());
                           }else{
                              final Set<Object> values = new HashSet<>();
                              for(final String val : valeurExterne.getValeur().split(";")){
                                 values.add(extractValueForOneThesaurus(champ.getQueryChamp(), banque, val));
                              }
                              value = values;
                           }
                        }else{
                           value = valeurExterne.getValeur();
                        }
                     }
                     // set de la valeur
                     setPropertyValueForObject(value, champ, obj);
                  }else{ // champs particuliers
                     // codes organes / morphos
                     if(Arrays.asList(229, 230).contains(valeurExterne.getChampEntiteId())){
                        injectCodeAssignes(valeurExterne, champ, null, (Echantillon) obj);
                     }
                  }
               }catch(final IllegalAccessException e){
                  log.error(e);
               }catch(final InvocationTargetException e){
                  log.error(e);
               }catch(final NoSuchMethodException e){
                  log.error(e);
               }
            }
         }else if(valeurExterne.getChampAnnotationId() != null){
            // si la valeur concerne une annotation
            final ChampAnnotation champ = champAnnotationDao.findById(valeurExterne.getChampAnnotationId());
            if(champ != null){
               Object value = null;
               if(valeurExterne.getValeur() != null && !valeurExterne.getValeur().equals("")){
                  // on vérifie que l'annotation correspond bien
                  // au type de l'objet
                  if(champ.getTableAnnotation().getEntite().getNom().equals(obj.getClass().getSimpleName())){
                     // si le champ de la colonne est un thésaurus
                     if(champ.getDataType().getType().equals("thesaurus") || champ.getDataType().getType().equals("thesaurusM")){
                        value = extractValueForOneAnnotationThesaurus(champ, banque, valeurExterne.getValeur());
                     }else{
                        value = valeurExterne.getValeur();
                     }
                  }
               }

               // s'il y a une valeur pour l'annotation
               if(value != null){
                  final AnnotationValeur av = setPropertyValueForAnnotationValeur(value, champ, banque);
                  if(av != null){
                     annoValeurs.add(av);
                  }
               }
            }
         }
      }
   }

   @Override
   public void injectBlocExterneInObject(final Object obj, final Banque banque, final BlocExterne blocExterne,
      final List<AnnotationValeur> annoValeurs){
      if(obj != null && banque != null && blocExterne != null){
         // on récupère la liste des valeurs du bloc
         // @since 2.1 blocExterneId may be null 
         // if view query
         final List<ValeurExterne> valeurs = new ArrayList<>();
         valeurs.addAll(blocExterne.getBlocExterneId() != null ? valeurExterneManager.findByBlocExterneManager(blocExterne)
            : blocExterne.getValeurs());

         // injection de chaque valeur
         for(int i = 0; i < valeurs.size(); i++){
            injectValeurExterneInObject(obj, banque, valeurs.get(i), annoValeurs);
         }
      }
   }

   //TODO Refactorer
   @Override
   public ResultatInjection injectDossierManager(final DossierExterne dossier, final Banque banque){
      ResultatInjection resultat = null;
      if(dossier != null && banque != null){
         resultat = new ResultatInjection();
         // création des objets
         Patient patient = new Patient();
         Maladie maladie = new Maladie();
         if(banque.getDefautMaladie() != null){
            maladie.setLibelle(banque.getDefautMaladie());
         }else{
            maladie.setLibelle("INDETERMINEE");
         }
         if(banque.getDefautMaladieCode() != null){
            maladie.setCode(banque.getDefautMaladieCode());
         }
         final Prelevement prelevement = new Prelevement();
         prelevement.setCode(dossier.getIdentificationDossier());
         prelevement.setBanque(banque);
         final Echantillon echantillon = new Echantillon();
         echantillon.setBanque(banque);
         // création des listes d'annotations
         final List<AnnotationValeur> annosPatient = new ArrayList<>();
         final List<AnnotationValeur> annosPrelevement = new ArrayList<>();
         final List<AnnotationValeur> annosEchantillon = new ArrayList<>();

         // Récupération des blocs
         // @since 2.1 dossierExterneId may be null 
         // if view query
         final List<BlocExterne> blocs = new ArrayList<>();
         blocs.addAll(dossier.getDossierExterneId() != null ? blocExterneManager.findByDossierExterneManager(dossier)
            : dossier.getBlocExternes());

         for(int i = 0; i < blocs.size(); i++){
            final Entite entite = entiteDao.findById(blocs.get(i).getEntiteId());
            if(entite != null){
               if(entite.getNom().equals("Patient")){
                  injectBlocExterneInObject(patient, banque, blocs.get(i), annosPatient);
               }else if(entite.getNom().equals("Maladie")){
                  injectBlocExterneInObject(maladie, banque, blocs.get(i), null);
               }else if(entite.getNom().equals("Prelevement")){
                  injectBlocExterneInObject(prelevement, banque, blocs.get(i), annosPrelevement);
               }else if(entite.getNom().equals("Echantillon")){
                  // on récupère la liste des valeurs du bloc
                  // @since 2.1 blocExterneId may be null 
                  // if view query
                  final List<ValeurExterne> valeurs = new ArrayList<>();
                  valeurs.addAll(blocs.get(i).getBlocExterneId() != null
                     ? valeurExterneManager.findByBlocExterneManager(blocs.get(i)) : blocs.get(i).getValeurs());

                  if(!valeurs.isEmpty()){
                     // @since 2.2.2-diamic, certains blocs correspondent à un échantillon complet
                     // la première valeur = code échantillon
                     if(!valeurs.get(0).getChampEntiteId().equals(54)){ // bloc échantillon générique

                        boolean isCode = false;
                        boolean isCR = false;
                        ChampEntite ce = null;

                        // injection de chaque valeur
                        for(final ValeurExterne val : valeurs){
                           isCode = false;
                           isCR = false;
                           if(val.getChampEntiteId() != null){
                              ce = champEntiteDao.findById(val.getChampEntiteId());

                              if(ce.getNom().equals("CodeOrganes") || ce.getNom().equals("CodeMorphos")){
                                 isCode = true;
                              }else if(ce.getNom().equalsIgnoreCase("CrAnapath")){
                                 isCR = true;
                              }

                           }

                           if(!isCode && !isCR){
                              injectValeurExterneInObject(echantillon, banque, val, annosEchantillon);
                           }else if(isCode){

                              injectCodeAssignes(val, ce, resultat, null);

                           }else if(isCR){
                              final Fichier crAnapath = new Fichier();
                              crAnapath.setNom(val.getValeur());
                              crAnapath.setMimeType("application/pdf");
                              resultat.setCrAnapath(crAnapath);
                              resultat.setStream(new ByteArrayInputStream(val.getContenu()));
                           }
                        }
                     }else{ // un échantillon complet

                        final Echantillon echanComplet = echantillon.clone();

                        String adrl = null;

                        // injection de chaque valeur
                        for(final ValeurExterne val : valeurs){
                           adrl = null;

                           if(val.getChampEntiteId() != 57){ // Emplacement
                              injectValeurExterneInObject(echanComplet, banque, val, annosEchantillon);
                           }else{ // emplacement
                              adrl = val.getValeur();
                           }
                        }

                        resultat.getEchanAdrls().put(echanComplet, adrl);
                     }
                  }
               }
            }
         }

         // doublon patient
         // on regarde si le patient existe deja en base
         if(patientManager.findDoublonManager(patient).isPresent()){
            
            // TODO Gatsbi patient getNom peut être null ! Ou PAS si interfacagae ?!
            // Faut-il pouvoir injecter par identifiant ?
            final List<Patient> liste = patientManager.findByNomLikeManager(patient.getNom(), true);

            for(int i = 0; i < liste.size(); i++){
               Patient p = liste.get(i);
               if(!p.getClass().getSimpleName().equals("Patient")){
                  p = patientDao.mergeObject(liste.get(i));
               }
               if(patient.equals(p)){
                  patient = p;
               }
            }

            maladie.setPatient(patient);
            // on regarde si la maladie existe deja en base
            if(maladieManager.findDoublonManager(maladie, patient)){
               // @since 2.2.3-genno optimisation = recherche la maladie par patient
               final List<Maladie> mals = maladieManager.findByLibelleAndPatientManager(maladie.getLibelle(), patient);

               for(int i = 0; i < mals.size(); i++){
                  Maladie m = mals.get(i);
                  if(!m.getClass().getSimpleName().equals("Maladie")){
                     m = maladieDao.mergeObject(mals.get(i));
                  }
                  if(maladie.equals(m)){
                     maladie = m;
                  }
               }
            }
         }else{
            maladie.setPatient(patient);
         }
         prelevement.setMaladie(maladie);
         resultat.setPrelevement(prelevement);
         resultat.setAnnosPatient(annosPatient);
         resultat.setAnnosPrelevement(annosPrelevement);
         resultat.setEchantillon(echantillon);
         resultat.setAnnosEchantillon(annosEchantillon);
      }

      return resultat;
   }

   /**
    * Factorisation de la méthode d'injection des codes organes / lésionnels 
    * vers un objet RésultatInjection OU un Echantillon à partir d'une ValeurExterne.
    * @param val ValeurExterne
    * @param ce ChampEntite
    * @param resultat ResultatInjection 
    * @param echan Echantillon
    */
   private void injectCodeAssignes(final ValeurExterne val, final ChampEntite ce, final ResultatInjection resultat,
      final Echantillon echantillon){
      String[] codes = null;
      // DIAMIC Hack
      if(val.getValeur().contains("~")){
         codes = val.getValeur().split("~");
      }else{
         codes = val.getValeur().split(";");
      }

      int ordreOrgane = 1;
      int ordreMorpho = 1;
      for(int j = 0; j < codes.length; j++){
         final CodeAssigne codeAs = new CodeAssigne();
         codeAs.setCodeRefId(null);
         if(!codes[j].contains("&")){
            codeAs.setCode(codes[j].trim());
         }else{ // code & libelle
            final String[] codeAndLibelle = codes[j].split("&");
            if(codeAndLibelle.length > 0){
               codeAs.setCode(codeAndLibelle[0].trim());
            }
            if(codeAndLibelle.length > 1){
               codeAs.setLibelle(codeAndLibelle[1].trim());
            }
         }
         codeAs.setTableCodage(null);
         codeAs.setIsMorpho(ce.getNom().equals("CodeMorphos"));
         codeAs.setIsOrgane(ce.getNom().equals("CodeOrganes"));
         if(ce.getNom().equals("CodeMorphos")){
            codeAs.setOrdre(ordreMorpho);
            if(ordreMorpho == 1){
               codeAs.setExport(true);
            }
            ++ordreMorpho;

            if(resultat != null){
               if(!resultat.getCodesMorpho().contains(codeAs)){
                  resultat.getCodesMorpho().add(codeAs);
               }
            }else if(echantillon != null){
               echantillon.getCodesAssignes().add(codeAs);
            }
         }else{
            codeAs.setOrdre(ordreOrgane);
            if(ordreOrgane == 1){
               codeAs.setExport(true);
            }
            ++ordreOrgane;
            if(resultat != null){
               if(!resultat.getCodesOrgane().contains(codeAs)){
                  resultat.getCodesOrgane().add(codeAs);
               }
            }else if(echantillon != null){
               echantillon.getCodesAssignes().add(codeAs);
            }
         }
      }
   }

   @Override
   public ResultatInjection injectDossierDeriveManager(final DossierExterne dossier, final Banque banque){
      ResultatInjection resultat = null;
      if(dossier != null && banque != null){

         // au moins un bloc derive attendu
         final List<BlocExterne> blocs = new ArrayList<>();
         blocs.addAll(dossier.getDossierExterneId() != null ? blocExterneManager.findByDossierExterneManager(dossier)
            : dossier.getBlocExternes());

         final BlocExterne blocDerive = blocs.stream().filter(b -> b.getEntiteId() == 8).findFirst().orElse(null);

         // breaks si pas de données dérivés
         // ne doit pas arriver si le mapping est correct
         if(blocDerive == null){
            return null;
         }

         resultat = new ResultatInjection();
         final ProdDerive derive = new ProdDerive();
         final List<AnnotationValeur> annosDerive = new ArrayList<>();

         final List<ValeurExterne> valeurs = new ArrayList<>();
         valeurs.addAll(valeurExterneManager.findByBlocExterneManager(blocDerive));

         // injection de chaque valeur
         for(final ValeurExterne val : valeurs){

            injectValeurExterneInObject(derive, banque, val, annosDerive);

            // throws prod type not null post validation
            if(val.getChampEntiteId() == 78 && derive.getProdType() == null){
               throw new TKException("prod.type.undeclared.value", val.getValeur());
            }
         }

         // doublon derive -> check UI
         resultat.setProdDerive(derive);
         resultat.setAnnosDerive(annosDerive);
      }

      return resultat;
   }

   @Override
   public void saveDossierAndChildrenManager(final DossierExterne dossier, final Banque banque, final Utilisateur u,
      final String baseDir){
      if(dossier != null & banque != null && u != null && baseDir != null){

         //			try {
         final ResultatInjection patPrelDos = injectDossierManager(dossier, banque);

         final Prelevement newPrel = patPrelDos.getPrelevement();

         // patient etat = V par défaut pour nouveau patient
         if(newPrel.getMaladie() != null && newPrel.getMaladie().getPatient().getPatientId() == null){
            newPrel.getMaladie().getPatient().setPatientEtat("V");
         }

         prelevementManager.createObjectWithNonConformitesManager(newPrel, banque, newPrel.getNature(), newPrel.getMaladie(),
            newPrel.getConsentType(), newPrel.getPreleveur(), newPrel.getServicePreleveur(), newPrel.getPrelevementType(),
            newPrel.getConditType(), newPrel.getConditMilieu(), newPrel.getTransporteur(), newPrel.getOperateur(),
            newPrel.getQuantiteUnite(), null, // labo inter forcément nulls par transmission !?
            patPrelDos.getAnnosPrelevement(), u, true, baseDir, false, null); // pas de non conformité par transmission !?

         // dérivés enfants
         // GENNO jointure par VALEUR code_prélevement (champ_entite_id = 23)
         final List<DossierExterne> derivesDos =
            dossierExterneManager.findChildrenByEmetteurValeurManager(dossier.getEmetteur(), 23, newPrel.getCode());

         saveDeriveChildrenManager(newPrel, derivesDos, banque, u, baseDir);

         // si tout s'est bien passé = suppression
         // car ces transactions ne seront pas rollbackées 
         // si une erreur survient (car pas le même entityManager)
         dossierExterneManager.removeObjectManager(dossier);
         for(final DossierExterne dE : derivesDos){
            dossierExterneManager.removeObjectManager(dE);
         }
      }
   }

   @Override
   public void saveDeriveChildrenManager(final Prelevement prel, final List<DossierExterne> derivesDos, final Banque banque,
      final Utilisateur u, final String baseDir){

      if(prel != null && banque != null && derivesDos != null && !derivesDos.isEmpty()){
         final Transformation transfo = new Transformation();
         // transfo.setQuantite(qteTransfo);
         // transfo.setQuantiteUnite(uniteTransfo); // est-ce transmissible par interfacage ?
         transfo.setEntite(entiteDao.findById(2)); // prelevement
         transfo.setObjetId(prel.getPrelevementId());
         ResultatInjection resDer;
         for(final DossierExterne dE : derivesDos){
            if((new Integer(8)).equals(dE.getEntiteId())){ // derive dossier
               resDer = injectDossierDeriveManager(dE, banque);

               if(resDer != null){
                  // le stockage peut etre transmis
                  final ObjetStatut statut = resDer.getProdDerive().getEmplacement() != null
                     ? objetStatutDao.findByStatut("STOCKE").get(0) : objetStatutDao.findByStatut("NON STOCKE").get(0);

                  prodDeriveManager.createObjectWithNonConformitesManager(resDer.getProdDerive(), banque,
                     resDer.getProdDerive().getProdType(), statut, resDer.getProdDerive().getCollaborateur(),
                     resDer.getProdDerive().getEmplacement(), resDer.getProdDerive().getVolumeUnite(),
                     resDer.getProdDerive().getConcUnite(), resDer.getProdDerive().getQuantiteUnite(),
                     resDer.getProdDerive().getModePrepaDerive(), resDer.getProdDerive().getProdQualite(), transfo,
                     resDer.getAnnosDerive(), u, true, baseDir, false, null, null); // pas de non conformité par transmission !?
               }
            }
         }
      }
   }

   @Override
   public List<DossierExterne> findExistingPrelevementByEmetteurManager(final Emetteur emet, final Plateforme pf){

      final List<DossierExterne> alreadyExistsAsPrel = new ArrayList<>();
      if(pf != null){

         final List<DossierExterne> dosPrelExt = dossierExterneDao.findByEmetteurAndEntiteNull(emet);

         final List<Banque> banks = banqueManager.findByPlateformeAndArchiveManager(pf, false);

         for(final DossierExterne dos : dosPrelExt){
            if(!prelevementManager
               .findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(dos.getIdentificationDossier(), banks, true).isEmpty()){
               alreadyExistsAsPrel.add(dos);
            }
         }
      }

      return alreadyExistsAsPrel;
   }

   @Override
   public List<DossierExterne> findExistingParentByEmetteurAndEntiteManager(final Emetteur emet, final Integer _id,
      final Plateforme pf){

      final List<DossierExterne> parentsAlreadyExistsAsPrel = new ArrayList<>();

      if(pf != null){
         final List<DossierExterne> dosPrelExt = dossierExterneDao.findByEmetteurAndEntite(emet, 8);

         final List<Banque> banks = banqueManager.findByPlateformeAndArchiveManager(pf, false);

         for(final DossierExterne dos : dosPrelExt){
            if(!getParentPrelevementIdsForDossierExterne(dos, banks, _id).isEmpty()){
               parentsAlreadyExistsAsPrel.add(dos);
            }
         }
      }

      return parentsAlreadyExistsAsPrel;
   }

   /**
    * Renvoie les ids prélèvements correspondant au code du prélèvement parent 
    * pour un dossier externe représentant en enfant (prélèvement primaire)
    * @param dos
    * @param banks
    * @param champ entite id correspondant au code parent (= 23 code prélèvement)
    * @return ids prelevements
    */
   private List<Integer> getParentPrelevementIdsForDossierExterne(final DossierExterne dos, final List<Banque> banks,
      final Integer pChpId){

      // champ entite id = 23 entite id = 2
      final List<ValeurExterne> vals = valeurExterneManager.findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, pChpId, 2);

      final ValeurExterne codePrelValeur = vals.size() > 0 ? vals.get(0) : null;

      if(codePrelValeur != null){
         return prelevementManager.findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(codePrelValeur.getValeur(), banks,
            true);
      }
      return new ArrayList<>();
   }

   @Override
   public List<DossierExterne> findExistingChildByEmetteurAndEntiteManager(final Emetteur emet, final Plateforme pf){

      final List<DossierExterne> childAlreadyExistsAsDerive = new ArrayList<>();

      if(pf != null){
         final List<DossierExterne> dosPrelExt = dossierExterneDao.findByEmetteurAndEntite(emet, 8);

         final List<Banque> banks = banqueManager.findByPlateformeAndArchiveManager(pf, false);

         for(final DossierExterne dos : dosPrelExt){
            if(!prodDeriveManager.findByCodeOrLaboBothSideWithBanqueReturnIdsManager(dos.getIdentificationDossier(), banks, true)
               .isEmpty()){
               childAlreadyExistsAsDerive.add(dos);
            }
         }
      }

      return childAlreadyExistsAsDerive;
   }

   @Override
   public boolean synchronizeDeriveChildrenManager(final Emetteur emet, final Plateforme pf, final Utilisateur u,
      final String baseDir){

      boolean isAnyParentPrelSynced = false;

      if(pf != null && u != null && baseDir != null){

         // recherche tous les dossiers enfants (prels secondaires) dont 
         // le parent (prel primaire) existe mais ne sont pas déja enregistrés
         // pour les ajouter
         final Collection<DossierExterne> childrenDossierToSync = CollectionUtils.subtract(
            findExistingParentByEmetteurAndEntiteManager(emet, 23, pf), findExistingChildByEmetteurAndEntiteManager(emet, pf));

         List<Integer> prelParentIds;
         Prelevement parent;
         final Map<Prelevement, List<DossierExterne>> dosToAddToParent = new HashMap<>();

         // prépare une map pour chaque parent, une liste de dossier 
         // enfants à synchroniser
         for(final DossierExterne dos : childrenDossierToSync){

            prelParentIds =
               getParentPrelevementIdsForDossierExterne(dos, banqueManager.findByPlateformeAndArchiveManager(pf, false), 23); // 23 = champEntiteId = code prel

            // skips synchronisation à cause de doublon !!
            if(prelParentIds.size() != 1){ // doublon prelevement !!
               log.warn("doublon de prélèvement parent détecté: "
                  + valeurExterneManager.findByDossierChampEntiteIdAndBlocEntiteIdManager(dos, 23, 2));
               continue;
            }else{
               parent = prelevementManager.findByIdManager(prelParentIds.get(0));
               if(dosToAddToParent.containsKey(parent)){
                  dosToAddToParent.get(parent).add(dos);
               }else{ // premier element à être ajouter
                  final List<DossierExterne> dosToAdd = new ArrayList<>();
                  dosToAdd.add(dos);
                  dosToAddToParent.put(parent, dosToAdd);
               }
            }
         }

         // une transformation pour chaque parent
         for(final Prelevement prel : dosToAddToParent.keySet()){
            final Transformation transfo = new Transformation();
            // transfo.setQuantite(qteTransfo);
            // transfo.setQuantiteUnite(uniteTransfo); // est-ce transmissible par interfacage ?
            transfo.setEntite(entiteDao.findById(2)); // prelevement
            transfo.setObjetId(prel.getPrelevementId());
            for(final DossierExterne dos : dosToAddToParent.get(prel)){
               final ResultatInjection resDer = injectDossierDeriveManager(dos, prel.getBanque()); // synchro banque prel

               if(resDer != null){
                  // le stockage peut etre transmis
                  final ObjetStatut statut = resDer.getProdDerive().getEmplacement() != null
                     ? objetStatutDao.findByStatut("STOCKE").get(0) : objetStatutDao.findByStatut("NON STOCKE").get(0);

                  prodDeriveManager.createObjectWithNonConformitesManager(resDer.getProdDerive(), prel.getBanque(),
                     resDer.getProdDerive().getProdType(), statut, resDer.getProdDerive().getCollaborateur(),
                     resDer.getProdDerive().getEmplacement(), resDer.getProdDerive().getVolumeUnite(),
                     resDer.getProdDerive().getConcUnite(), resDer.getProdDerive().getQuantiteUnite(),
                     resDer.getProdDerive().getModePrepaDerive(), resDer.getProdDerive().getProdQualite(), transfo,
                     resDer.getAnnosDerive(), u, true, baseDir, false, null, null); // pas de non conformité par transmission !?
               }
            }

            // operation synchronisation
            CreateOrUpdateUtilities.createAssociateOperation(prel, operationManager,
               operationTypeDao.findByNom("Synchronisation").get(0), u);

            isAnyParentPrelSynced = true;
         }

         // si tout s'est bien passé = suppression
         for(final DossierExterne dos : childrenDossierToSync){
            dossierExterneManager.removeObjectManager(dos);
         }
      }
      return isAnyParentPrelSynced;
   }
}
