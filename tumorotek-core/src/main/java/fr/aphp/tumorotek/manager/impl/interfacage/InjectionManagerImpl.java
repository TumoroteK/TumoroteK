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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.InjectionManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.utils.Utils;

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

   
   @Override
   public Object extractValueForOneThesaurus(final ChampEntite champEntite, final Banque banque, final Object valeur){
      Object resultat = null;
      List<Object> objets = new ArrayList<>();

      if(champEntite != null && valeur != null && banque != null){

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
            final String nomChamp = champEntite.getNom().replaceFirst(".", (champEntite.getNom().charAt(0) + "").toLowerCase());

            final StringBuffer sql = new StringBuffer();
            sql.append("SELECT e FROM ");
            sql.append(champEntite.getEntite().getNom());
            sql.append(" as e where e.");
            sql.append(nomChamp);
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
                           value = extractValueForOneThesaurus(champ.getQueryChamp(), banque, valeurExterne.getValeur());
                        }else{
                           value = valeurExterne.getValeur();
                        }
                     }
                     // set de la valeur
                     setPropertyValueForObject(value, champ, obj);
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
               }else if(entite.getNom().equals("Echantillon")){ // since 2.0.13						
                  // on récupère la liste des valeurs du bloc
                  // @since 2.1 blocExterneId may be null 
                  // if view query
                  final List<ValeurExterne> valeurs = new ArrayList<>();
                  valeurs.addAll(blocs.get(i).getBlocExterneId() != null
                     ? valeurExterneManager.findByBlocExterneManager(blocs.get(i)) : blocs.get(i).getValeurs());

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
                           codeAs.setCode(codes[j]);
                           // codeAs.setLibelle(codes[j]);
                           codeAs.setTableCodage(null);
                           codeAs.setIsMorpho(ce.getNom().equals("CodeMorphos"));
                           codeAs.setIsOrgane(ce.getNom().equals("CodeOrganes"));
                           if(ce.getNom().equals("CodeMorphos")){
                              codeAs.setOrdre(ordreMorpho);
                              if(ordreMorpho == 1){
                                 codeAs.setExport(true);
                              }
                              ++ordreMorpho;
                              if(!resultat.getCodesMorpho().contains(codeAs)){
                                 resultat.getCodesMorpho().add(codeAs);
                              }
                           }else{
                              codeAs.setOrdre(ordreOrgane);
                              if(ordreOrgane == 1){
                                 codeAs.setExport(true);
                              }
                              ++ordreOrgane;
                              if(!resultat.getCodesOrgane().contains(codeAs)){
                                 resultat.getCodesOrgane().add(codeAs);
                              }
                           }
                        }

                     }else if(isCR){
                        final Fichier crAnapath = new Fichier();
                        crAnapath.setNom(val.getValeur());
                        crAnapath.setMimeType("application/pdf");
                        resultat.setCrAnapath(crAnapath);
                        resultat.setStream(new ByteArrayInputStream(val.getContenu()));
                     }
                  }
               }
            }
         }

         // doublon patient
         // on regarde si le patient existe deja en base
         if(patientManager.findDoublonManager(patient)){
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
            if(maladieManager.findDoublonManager(maladie)){
               final List<Maladie> mals = maladieManager.findByLibelleLikeManager(maladie.getLibelle(), true);

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
}
