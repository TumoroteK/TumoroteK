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
package fr.aphp.tumorotek.manager.impl.io.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementQueryManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementRequeteManager;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.utils.io.ConstanteIO;

public class TraitementRequeteManagerImpl implements TraitementRequeteManager
{

   /** Bean Manager. */
   private GroupementManager groupementManager = null;

   /** Bean Manager. */
   private CorrespondanceManager correspondanceManager = null;

   /** Bean Manager. */
   private TraitementQueryManager traitementQueryManager = null;

   /**
    * Setter du bean CritereManager.
    * @param crManager est le bean Manager.
    */
   public void setCritereManager(final CritereManager crManager){}

   /**
    * Setter du bean GroupementManager.
    * @param grManager est le bean Manager.
    */
   public void setGroupementManager(final GroupementManager grManager){
      this.groupementManager = grManager;
   }

   /**
    * Setter du bean CorrespondanceManager.
    * @param cManager est le bean Manager.
    */
   public void setCorrespondanceManager(final CorrespondanceManager cManager){
      this.correspondanceManager = cManager;
   }

   /**
    * Setter du bean TraitementQueryManager.
    * @param tqManager est le bean Manager.
    */
   public void setTraitementQueryManager(final TraitementQueryManager tqManager){
      this.traitementQueryManager = tqManager;
   }

   @Override
   public void executeManager(final Requete requete, final Affichage affichage){

      /** On doit traiter l'arbre de la Requete par Groupement. */

      /** Une fois l'arbre de Requete traité, on utilise la liste des Entites
       * de l'Affichage pour fusionner les listes.
       */
   }

   /**
    * Retourne une liste d'objets correspondant au résultat de la requête.
    * @param requete Requete à exécuter.
    * @return Liste d'objets correspondant au résultat de la requête.
    * (objets correspondant au niveau le plus bas (-> ProdDerive) des critères
    * de la requête)
    */
   @Override
   public List<Object> traitementRequeteManager(final Requete requete, final List<Banque> banques,
      final Hashtable<Integer, Object> criteresValues, final String jdbcDialect){

      /** On récupère le Groupement racine. */
      final Groupement racine = requete.getGroupementRacine();
      /** On parcourt le Groupement racine en profondeur d'abord pour filtrer
       * les objets récupérés.
       */
      final List<Object> listeObjets = traitementArbreGroupementManager(racine, banques, criteresValues, jdbcDialect);
      return listeObjets;
   }

   /**
    * Retourne une liste d'objets correspondant au résultat de l'exécution de
    * l'arbre de groupement dont la racine est fournie en paramètre.
    *
    * @param noeud
    *            Groupement racine de l'arbre à exécuter.
    * @return Liste d'objets correspondant au résultat de l'arbre de groupement
    *         issu du noeud racine en paramètre. (objets correspondant au
    *         niveau le plus haut (-> ProdDerive) des critères de la requête)
    */
   @Override
   public List<Object> traitementArbreGroupementManager(final Groupement noeud, final List<Banque> banques,
      final Hashtable<Integer, Object> criteresValues, final String jdbcDialect){
      List<Object> listeResultante = new ArrayList<>();
      /** On vérifie que l'opérateur est correct. */

      List<Object> liste1 = new ArrayList<>();
      List<Object> liste2 = new ArrayList<>();
      /** On traite les Criteres. */
      // On traite les enfants d'abord.
      if(noeud.getCritere1() == null || noeud.getCritere2() == null){
         final List<Groupement> enfants = groupementManager.findEnfantsManager(noeud);
         if(enfants == null || enfants.size() == 0){
            /*
             * On met l'opérateur ou pour avoir les résultats du critère non
             * null.
             */
            noeud.setOperateur(ConstanteIO.CONDITION_OU);
         }else if(enfants.size() > 0){
            if(enfants.get(0) != null){
               final List<Object> listeObjets =
                  traitementArbreGroupementManager(enfants.get(0), banques, criteresValues, jdbcDialect);
               if(listeObjets != null && listeObjets.size() > 0){
                  liste1.addAll(listeObjets);
               }
            }else{
               throw new RequiredObjectIsNullException("Groupement", "traitement", "enfant recquis");
            }
            if(enfants.size() > 1 && enfants.get(1) != null){
               final List<Object> listeObjets2 =
                  traitementArbreGroupementManager(enfants.get(1), banques, criteresValues, jdbcDialect);
               if(listeObjets2 != null && listeObjets2.size() > 0){
                  liste2.addAll(listeObjets2);
               }
            }
         }
      }
      // On traite le Critere1
      if(noeud.getCritere1() != null){
         Object value = null;
         if(criteresValues.containsKey(noeud.getCritere1().getCritereId())){
            value = criteresValues.get(noeud.getCritere1().getCritereId());
         }
         if(liste1.size() == 0){
            liste1 = traitementCritereManager(noeud.getCritere1(), banques, value, jdbcDialect);
         }else if(liste2.size() == 0){
            liste2 = traitementCritereManager(noeud.getCritere1(), banques, value, jdbcDialect);
         }
      }
      // On traite le Critere2
      if(noeud.getCritere2() != null){
         Object value = null;
         if(criteresValues.containsKey(noeud.getCritere2().getCritereId())){
            value = criteresValues.get(noeud.getCritere2().getCritereId());
         }
         if(liste1.size() == 0){
            liste1 = traitementCritereManager(noeud.getCritere2(), banques, value, jdbcDialect);
         }else if(liste2.size() == 0){
            liste2 = traitementCritereManager(noeud.getCritere2(), banques, value, jdbcDialect);
         }
      }
      /** On traite le Groupement noeud */
      if(noeud.getOperateur().equals(ConstanteIO.CONDITION_ET)){
         listeResultante = fusionnerListesManager(liste1, liste2);
      }else if(noeud.getOperateur().equals(ConstanteIO.CONDITION_OU)){
         listeResultante = additionnerListesManager(liste1, liste2);
      }
      return listeResultante;
   }

   /**
    * Retourne une liste d'objets correspondant à un critère.
    * @param critère Critère à exécuter.
    * @return Liste d'objets correspondant au critère.
    */
   @Override
   public List<Object> traitementCritereManager(final Critere critere, final List<Banque> banques, final Object value,
      final String jdbcDialect){
      /**
       * On doit retourner une liste d'objets qui correspondent au Critere
       * dans la base de données.
       */
      List<Object> listeResultante = null;
      listeResultante = traitementQueryManager.findObjetByCritereManager(critere, banques, value, jdbcDialect);
      return listeResultante;
   }

   /**
    * Additionne deux listes d'objets et renvoie une liste d'objets d'entités
    * la plus haute des deux (-> Cession).
    * @param liste1 Première liste d'objets à additionner.
    * @param liste2 Seconde liste d'objets à additionner.
    * @return Liste d'objets d'entité la plus haute des deux listes.
    */
   @Override
   public List<Object> additionnerListesManager(final List<Object> liste1, final List<Object> liste2){
      /**
       * On doit retourner la liste dont le type d'objet (Entite) est la plus
       * haute: Pour cela on compare les deux types d'objet, on filtre au plus
       * bas: On récupère les Entites basses (1 = Patient) en partant des
       * Entites hautes (6 = Cession).
       */
      List<Object> listeResultante = null;
      if(liste1 != null){
         if(liste1.size() > 0){
            String entiteBasse = null;
            if(liste1.get(0).getClass().getSimpleName().equals("Patient")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     entiteBasse = "Patient";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute tous les patients de la liste 1 dans la
                      * liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute tous les patients de la liste 2 dans la
                      * liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")
                     || liste2.get(0).getClass().getSimpleName().equals("Prelevement")
                     || liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     /* On récupère les patients des objets. */
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 1 dans la
                      * liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste1.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute tous les patients de la liste 1 dans la
                   * liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "Patient";
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Maladie")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des maladies. */
                     entiteBasse = "Maladie";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     entiteBasse = "Maladie";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute toutes les maladies de la liste 1
                      * dans la liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute toutes les maladies de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")
                     || liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     /* On récupère les maladies des objets. */
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 1
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste1.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute tous les maladies de la liste 1 dans la
                   * liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "Maladie";
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Prelevement")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des prélèvements. */
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des prélèvements. */
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute tous les prélèvements de la liste 1
                      * dans la liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute tous les prélèvements de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Prelevement prelevement = (Prelevement) it.next();
                        if(!listeResultante.contains(prelevement)){
                           listeResultante.add(prelevement);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     /* On récupère les prélèvements des objets. */
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 1
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste1.iterator();
                     while(it.hasNext()){
                        final Prelevement prelevement = (Prelevement) it.next();
                        if(!listeResultante.contains(prelevement)){
                           listeResultante.add(prelevement);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute tous les prélèvements de la liste 1
                   * dans la liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "Prelevement";
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Echantillon")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des échantillons. */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des échantillons. */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /*
                      * On récupère les prélèvements des
                      * échantillons.
                      */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Prelevement prelevement = (Prelevement) it.next();
                        if(!listeResultante.contains(prelevement)){
                           listeResultante.add(prelevement);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute tous les échantillons de la liste 1
                      * dans la liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute tous les échantillons de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Echantillon echantillon = (Echantillon) it.next();
                        if(!listeResultante.contains(echantillon)){
                           listeResultante.add(echantillon);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     /* On récupère les échantillons des objets. */
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 1
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste1.iterator();
                     while(it.hasNext()){
                        final Echantillon echantillon = (Echantillon) it.next();
                        if(!listeResultante.contains(echantillon)){
                           listeResultante.add(echantillon);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute tous les échantillons de la liste 1
                   * dans la liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "Echantillon";
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("ProdDerive")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /* On récupère les prélèvements des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Prelevement prelevement = (Prelevement) it.next();
                        if(!listeResultante.contains(prelevement)){
                           listeResultante.add(prelevement);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     /* On récupère les échantillons des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 2
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Echantillon echantillon = (Echantillon) it.next();
                        if(!listeResultante.contains(echantillon)){
                           listeResultante.add(echantillon);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")){
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute tous les dérivés de la liste 1 dans
                      * la liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute tous les dérivés de la liste 2 dans
                      * la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final ProdDerive derive = (ProdDerive) it.next();
                        if(!listeResultante.contains(derive)){
                           listeResultante.add(derive);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     /* On récupère les échantillons des objets. */
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "ProdDerive"));
                     /*
                      * On ajoute tous les prodDerives de la liste 1
                      * dans la liste résultante s'ils n'y sont pas
                      * déjà.
                      */
                     final Iterator<Object> it = liste1.iterator();
                     while(it.hasNext()){
                        final ProdDerive prodDerive = (ProdDerive) it.next();
                        if(!listeResultante.contains(prodDerive)){
                           listeResultante.add(prodDerive);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute tous les dérivés de la liste 1 dans la
                   * liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "ProdDerive";
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Cession")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 2 dans la
                      * liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Patient patient = (Patient) it.next();
                        if(!listeResultante.contains(patient)){
                           listeResultante.add(patient);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 2 dans
                      * la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Maladie maladie = (Maladie) it.next();
                        if(!listeResultante.contains(maladie)){
                           listeResultante.add(maladie);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /* On récupère les prélèvements des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 2
                      * dans la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Prelevement prelevement = (Prelevement) it.next();
                        if(!listeResultante.contains(prelevement)){
                           listeResultante.add(prelevement);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     /* On récupère les échantillons des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 2
                      * dans la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Echantillon echantillon = (Echantillon) it.next();
                        if(!listeResultante.contains(echantillon)){
                           listeResultante.add(echantillon);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")){
                     /* On récupère les dérivés des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     listeResultante.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "ProdDerive"));
                     /*
                      * On ajoute tous les dérivés de la liste 2
                      * dans la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final ProdDerive derive = (ProdDerive) it.next();
                        if(!listeResultante.contains(derive)){
                           listeResultante.add(derive);
                        }
                     }
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     /*
                      * On ajoute toutes les cessions de la liste 1 dans
                      * la liste résultante.
                      */
                     listeResultante.addAll(liste1);
                     /*
                      * On ajoute toutes les cessions de la liste 2 dans
                      * la liste résultante s'ils n'y sont pas déjà.
                      */
                     final Iterator<Object> it = liste2.iterator();
                     while(it.hasNext()){
                        final Cession cession = (Cession) it.next();
                        if(!listeResultante.contains(cession)){
                           listeResultante.add(cession);
                        }
                     }
                  }
               }else if(null != liste2 && liste2.size() == 0){
                  /* Requête uni critère sur le critère 1. */
                  listeResultante = new ArrayList<>();
                  /*
                   * On ajoute toutes les cessions de la liste 1 dans la
                   * liste résultante.
                   */
                  listeResultante.addAll(liste1);
                  entiteBasse = "Cession";
               }
            }
            /* On récupère les entités au plus bas. */
            if(listeResultante != null && listeResultante.size() > 0
               && !listeResultante.get(0).getClass().getSimpleName().equals(entiteBasse)){
               listeResultante = correspondanceManager.recupereEntitesViaDAutres(listeResultante, entiteBasse);
            }
         }
      }else{
         /* En cas de requête uni critère sur le critère 2. */
         if(liste2 != null){
            listeResultante = new ArrayList<>();
            listeResultante.addAll(liste2);
         }
      }
      return listeResultante;
   }

   /**
    * Fusionne deux listes d'objets et renvoie une liste d'objets d'entités
    * la plus haute des deux (-> Cession).
    * @param liste1 Première liste d'objets à fusionner.
    * @param liste2 Seconde liste d'objets à fusionner.
    * @return Liste d'objets d'entité la plus haute des deux listes.
    */
   @Override

   public List<Object> fusionnerListesManager(final List<Object> liste1, final List<Object> liste2){
      /**
       * On doit retourner la liste dont le type d'objet (Entite) est la plus
       * haute: Pour cela on compare les deux types d'objet, on filtre au plus
       * bas: On récupère les Entites basses (1 = Patient) en partant des
       * Entites hautes (6 = Cession).
       */
      List<Object> listeResultante = null;
      if(liste1 != null){
         if(liste1.size() > 0){
            String entiteBasse = null;
            if(liste1.get(0).getClass().getSimpleName().equals("Patient")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     entiteBasse = "Patient";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")
                     || liste2.get(0).getClass().getSimpleName().equals("Prelevement")
                     || liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     /* On récupère les patients des objets. */
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3 dans la
                      * liste résultante s'ils sont dans la liste 1.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste1);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								Patient patient = (Patient) it.next();
                     //								if (liste1.contains(patient)) {
                     //									listeResultante.add(patient);
                     //								}
                     //							}
                  }
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Maladie")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des maladies. */
                     entiteBasse = "Maladie";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Patient patient = (Patient) it.next();
                     //									if (liste2.contains(patient)) {
                     //										listeResultante.add(patient);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     entiteBasse = "Maladie";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")
                     || liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     /* On récupère les maladies des objets. */
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 1.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste1);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Maladie maladie = (Maladie) it.next();
                     //									if (liste1.contains(maladie)) {
                     //										listeResultante.add(maladie);
                     //									}
                     //								}
                  }
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Prelevement")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des prélèvements. */
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Patient patient = (Patient) it.next();
                     //									if (liste2.contains(patient)) {
                     //										listeResultante.add(patient);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des prélèvements. */
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Maladie maladie = (Maladie) it.next();
                     //									if (liste2.contains(maladie)) {
                     //										listeResultante.add(maladie);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     entiteBasse = "Prelevement";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")
                     || liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     /* On récupère les prélèvements des objets. */
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 1.
                      */
                     listeResultante = ListUtils.retainAll(liste3, liste1);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Prelevement prelevement = (Prelevement) it
                     //											.next();
                     //									if (liste1.contains(prelevement)) {
                     //										listeResultante.add(prelevement);
                     //									}
                     //								}
                  }
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Echantillon")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des échantillons. */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Patient patient = (Patient) it.next();
                     //									if (liste2.contains(patient)) {
                     //										listeResultante.add(patient);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des échantillons. */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Maladie maladie = (Maladie) it.next();
                     //									if (liste2.contains(maladie)) {
                     //										listeResultante.add(maladie);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /*
                      * On récupère les prélèvements des
                      * échantillons.
                      */
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Prelevement prelevement = (Prelevement) it
                     //											.next();
                     //									if (liste2.contains(prelevement)) {
                     //										listeResultante.add(prelevement);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     entiteBasse = "Echantillon";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")
                     || liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     /* On récupère les échantillons des objets. */
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 1.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste1);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Echantillon echantillon = (Echantillon) it
                     //											.next();
                     //									if (liste1.contains(echantillon)) {
                     //										listeResultante.add(echantillon);
                     //									}
                     //								}
                  }
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("ProdDerive")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Patient patient = (Patient) it.next();
                     //									if (liste2.contains(patient)) {
                     //										listeResultante.add(patient);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Maladie maladie = (Maladie) it.next();
                     //									if (liste2.contains(maladie)) {
                     //										listeResultante.add(maladie);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /* On récupère les prélèvements des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Prelevement prelevement = (Prelevement) it
                     //											.next();
                     //									if (liste2.contains(prelevement)) {
                     //										listeResultante.add(prelevement);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     /* On récupère les échantillons des dérivés. */
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									Echantillon echantillon = (Echantillon) it
                     //											.next();
                     //									if (liste2.contains(echantillon)) {
                     //										listeResultante.add(echantillon);
                     //									}
                     //								}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")){
                     entiteBasse = "ProdDerive";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = liste2.get(0).getClass().getSimpleName();
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     /* On récupère les échantillons des objets. */
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste2, "Echantillon"));
                     /*
                      * On ajoute tous les dérivés de la liste 3
                      * dans la liste résultante s'ils sont dans la
                      * liste 1.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste1);
                     //								Iterator<Object> it = liste3.iterator();
                     //								while (it.hasNext()) {
                     //									ProdDerive derive = (ProdDerive) it
                     //											.next();
                     //									if (liste1.contains(derive)) {
                     //										listeResultante.add(derive);
                     //									}
                     //								}
                  }
               }
            }else if(liste1.get(0).getClass().getSimpleName().equals("Cession")){
               if(liste2 != null && liste2.size() > 0){
                  if(liste2.get(0).getClass().getSimpleName().equals("Patient")){
                     /* On récupère les patients des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Patient"));
                     /*
                      * On ajoute tous les patients de la liste 3 dans la
                      * liste résultante s'ils sont dans la liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								Patient patient = (Patient) it.next();
                     //								if (liste2.contains(patient)) {
                     //									listeResultante.add(patient);
                     //								}
                     //							}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Maladie")){
                     /* On récupère les maladies des cession. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Maladie"));
                     /*
                      * On ajoute toutes les maladies de la liste 3 dans
                      * la liste résultante s'ils sont dans la liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								Maladie maladie = (Maladie) it.next();
                     //								if (liste2.contains(maladie)) {
                     //									listeResultante.add(maladie);
                     //								}
                     //							}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Prelevement")){
                     /* On récupère les prélèvements des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Prelevement"));
                     /*
                      * On ajoute tous les prélèvements de la liste 3
                      * dans la liste résultante s'ils sont dans la liste
                      * 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								Prelevement prelevement = (Prelevement) it
                     //										.next();
                     //								if (liste2.contains(prelevement)) {
                     //									listeResultante.add(prelevement);
                     //								}
                     //							}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Echantillon")){
                     /* On récupère les échantillons des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "Echantillon"));
                     /*
                      * On ajoute tous les échantillons de la liste 3
                      * dans la liste résultante s'ils sont dans la liste
                      * 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								Echantillon echantillon = (Echantillon) it
                     //										.next();
                     //								if (liste2.contains(echantillon)) {
                     //									listeResultante.add(echantillon);
                     //								}
                     //							}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("ProdDerive")){
                     /* On récupère les dérivés des cessions. */
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();
                     final List<Object> liste3 = new ArrayList<>();
                     liste3.addAll(correspondanceManager.recupereEntitesViaDAutres(liste1, "ProdDerive"));
                     /*
                      * On ajoute tous les dérivés de la liste 3 dans la
                      * liste résultante s'ils sont dans la liste 2.
                      */
                     listeResultante = ListUtils.intersection(liste3, liste2);
                     //							Iterator<Object> it = liste3.iterator();
                     //							while (it.hasNext()) {
                     //								ProdDerive derive = (ProdDerive) it.next();
                     //								if (liste2.contains(derive)) {
                     //									listeResultante.add(derive);
                     //								}
                     //							}
                  }else if(liste2.get(0).getClass().getSimpleName().equals("Cession")){
                     entiteBasse = "Cession";
                     listeResultante = new ArrayList<>();

                     listeResultante = ListUtils.intersection(liste1, liste2);
                  }
               }
            }
            /* On récupère les entités au plus bas. */
            if(listeResultante != null && listeResultante.size() > 0
               && !listeResultante.get(0).getClass().getSimpleName().equals(entiteBasse) && null != entiteBasse
               && !entiteBasse.equals("Cession")){
               listeResultante = correspondanceManager.recupereEntitesViaDAutres(listeResultante, entiteBasse);
               if(liste1.size() > 0 && liste1.get(0).getClass().getSimpleName().equals(entiteBasse)){
                  listeResultante = fusionnerListesManager(liste1, listeResultante);
               }else if(liste2 != null && liste2.size() > 0 && liste2.get(0).getClass().getSimpleName().equals(entiteBasse)){
                  listeResultante = fusionnerListesManager(liste2, listeResultante);
               }

            }
         }
      }
      return listeResultante;
   }
}
