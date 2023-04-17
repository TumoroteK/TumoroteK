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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampCalculeManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceIdManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementQueryManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Entite;

public class TraitementQueryManagerImpl implements TraitementQueryManager
{

   private final Logger log = LoggerFactory.getLogger(TraitementQueryManagerImpl.class);

   private EntityManagerFactory entityManagerFactory;

   private PrelevementDao prelevementDao;

   private PrelevementManager prelevementManager;

   private NonConformiteDao nonConformiteDao;

   private ObjetNonConformeManager objetNonConformeManager;

   private EntiteManager entiteManager;

   private CorrespondanceIdManager correspondanceIdManager;

   private ChampCalculeManager champCalculeManager;

   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setPrelevementManager(final PrelevementManager pM){
      this.prelevementManager = pM;
   }

   public void setNonConformiteDao(final NonConformiteDao c){
      this.nonConformiteDao = c;
   }

   public void setObjetNonConformeManager(final ObjetNonConformeManager o){
      this.objetNonConformeManager = o;
   }

   public void setEntiteManager(final EntiteManager e){
      this.entiteManager = e;
   }

   public void setCorrespondanceIdManager(final CorrespondanceIdManager cM){
      this.correspondanceIdManager = cM;
   }

   public void setChampCalculeManager(final ChampCalculeManager champCalculeManager){
      this.champCalculeManager = champCalculeManager;
   }

   /**
    * Retourne les objets correspondant à un critère. Méthode utilisée
    * dans la recherche réalisée par Maxime.
    * @param critere Critere à exécuter.
    * @return Liste d'objets correspondant au critère.
    */
   @Override
   public List<Object> findObjetByCritereManager(final Critere critere, final List<Banque> banks, final Object value,
      final String jdbcDialect){
      List<Object> objets = null;
      if(critere.getChamp() != null){
         final StringBuffer sb = new StringBuffer("");
         final Champ champ = critere.getChamp();

         //Cas des champs annotation
         if(champ.getChampAnnotation() != null){
            if(champ.getChampAnnotation().getChampCalcule() != null){
               final List<Critere> criteresChampsCalcules = new ArrayList<>();
               criteresChampsCalcules.add(critere);
               final List<Object> valuesChampsCalcules = new ArrayList<>();
               valuesChampsCalcules.add(value);
               return traitementChampsCalcules(criteresChampsCalcules, valuesChampsCalcules, critere.getChamp().entite().getNom(),
                  banks);
            }
            final ChampAnnotation ca = champ.getChampAnnotation();
            if(ca.getTableAnnotation() != null && ca.getTableAnnotation().getEntite() != null){
               final String nomEntite = ca.getTableAnnotation().getEntite().getNom();
               if(nomEntite != null){
                  final String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                  final String nomEntiteMinFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toLowerCase());
                  if(!critere.getOperateur().equals("is null")){
                     sb.append("SELECT e From " + nomEntiteMajFirst + " as e, AnnotationValeur av"
                        + " join av.champAnnotation as ca" + " WHERE" + " e." + nomEntiteMinFirst + "Id = av.objetId"
                        + " and ca.id = " + ca.getId() + " and ca.tableAnnotation.entite.nom = '" + nomEntiteMajFirst + "'");

                     if(ca.getDataType().getType().equals("num") || ca.getDataType().getType().equals("duree")){
                        sb.append(" and av.alphanum ");
                        sb.append(critere.getOperateur());
                     }else if(ca.getDataType().getType().equals("alphanum") || ca.getDataType().getType().equals("date")){
                        sb.append(" and av." + ca.getDataType().getType() + " " + critere.getOperateur());
                     }else if(ca.getDataType().getType().equals("datetime")){
                        sb.append(" and av.date " + critere.getOperateur());
                     }else if(ca.getDataType().getType().equals("thesaurus") || ca.getDataType().getType().equals("thesaurusM")){
                        if(!critere.getOperateur().equals("is null")){
                           sb.append(" and av.item.label " + critere.getOperateur());
                        }else{
                           sb.append(" and av.item " + critere.getOperateur());
                        }
                     }else if(ca.getDataType().getType().equals("boolean")){
                        sb.append(" and av.bool " + critere.getOperateur());
                     }else{
                        sb.append(" and av." + ca.getDataType().getType() + " " + critere.getOperateur());
                     }

                     if(ca.getDataType().getType().equals("num") || ca.getDataType().getType().equals("duree")){
                        sb.append(" " + value);
                     }else{
                        sb.append(" :valeur");
                     }
                  }else{ // is null champ annotation
                     sb.append("SELECT e From " + nomEntiteMajFirst + " as e WHERE" + " e." + nomEntiteMinFirst + "Id not in ("
                        + "select av.objetId from AnnotationValeur av" + " join av.champAnnotation as ca" + " where ca.id = "
                        + ca.getId() + " and ca.tableAnnotation.entite.nom = '" + nomEntiteMajFirst + "')");
                  }

                  // si l'entité recherchée n'est pas un patient
                  // ou une maladie, on ajoute un critère sur
                  // la banque
                  if(!nomEntiteMajFirst.equals("Patient") && !nomEntiteMajFirst.equals("Maladie")){
                     sb.append(" AND ");
                     sb.append("e.banque in (:list)");
                  }
               }
            }

            // Cas des champs entité
         }else if(champ.getChampEntite() != null){

            final ChampEntite ce = critere.getChamp().getChampEntite();

            if(ce != null && ce.getEntite() != null){
               final String nomEntite = ce.getEntite().getNom();
               if(nomEntite != null){

                  String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                  String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
                  boolean delegate = false;

                  List<String> joins = new ArrayList<>();

                  if(nomChampMinFirst.endsWith("Id")){
                     nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
                  }

                  joins = (buildJoinsList(champ, joins));

                  Champ parent = champ.getChampParent();
                  if(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
                     nomEntiteMajFirst = getNomEntiteAncetre(champ);
                  }

                  while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){

                     final AbstractTKChamp ceParent;
                     final String nomEntiteParent;

                     if(parent.getChampEntite() != null){
                        ceParent = parent.getChampEntite();
                        nomEntiteParent = parent.getChampEntite().getEntite().getNom();
                     }else{
                        ceParent = parent.getChampDelegue();
                        nomEntiteParent = parent.getChampDelegue().getEntite().getNom();
                        delegate = true;
                     }

                     String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                     // On enlève le suffixe "Id"
                     nomParent = nomParent.replaceFirst("Id$", "");

                     if(!critere.getOperateur().equals("is null")){
                        nomChampMinFirst = nomParent + "." + nomChampMinFirst;
                     }else{
                        nomChampMinFirst = nomParent;
                     }

                     // On change le nom de l'entiteMajFirst
                     nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                     parent = parent.getChampParent();
                  }

                  // exception impliquant l'appel de méthodes de requêtes

                  if(nomChampMinFirst.contains("etablissement")){
                     // hack etablissement preleveur
                     if(!critere.getOperateur().equals("is null")){
                        objets = new ArrayList<>();
                        objets.addAll(prelevementDao.findByEtablissementNom((String) value, banks));
                        return objets;
                     }
                     objets = new ArrayList<>();
                     objets.addAll(prelevementDao.findByEtablissementVide(banks));
                     return objets;
                  }else if(nomChampMinFirst.equals("risques")){
                     if(!critere.getOperateur().equals("is null")){
                        sb.append("SELECT DISTINCT e From " + "Prelevement as e " + "JOIN e.risques r " + "WHERE r.nom "
                           + critere.getOperateur());

                        sb.append(" :valeur");
                     }else{
                        sb.append("SELECT DISTINCT e From " + "Prelevement as e " + "LEFT JOIN e.risques r " + "WHERE r is null");
                     }
                  }else if(nomChampMinFirst.equals("ageAuPrelevement")){
                     objets = new ArrayList<>();
                     // hack age au prelevement
                     if(!critere.getOperateur().equals("is null")){
                        final List<Integer> ids = new ArrayList<>();
                        if(critere.getOperateur().equals(">=")){
                           ids.addAll(findPrelevementsByAgePatientWithBanquesManager("=", (Integer) value, banks, jdbcDialect));
                           ids.addAll(findPrelevementsByAgePatientWithBanquesManager(">", (Integer) value, banks, jdbcDialect));
                        }else if(critere.getOperateur().equals("<=")){
                           ids.addAll(findPrelevementsByAgePatientWithBanquesManager("=", (Integer) value, banks, jdbcDialect));
                           ids.addAll(findPrelevementsByAgePatientWithBanquesManager(">", (Integer) value, banks, jdbcDialect));
                        }else{
                           ids.addAll(findPrelevementsByAgePatientWithBanquesManager(critere.getOperateur(), (Integer) value,
                              banks, jdbcDialect));
                        }

                        if(!ids.isEmpty()){
                           objets.addAll(prelevementManager.findByIdsInListManager(ids));
                        }
                     }
                     return objets;

                  }else if(nomChampMinFirst.equals("codeOrganes") || nomChampMinFirst.equals("codeMorphos")){
                     if(!critere.getOperateur().equals("is null")){
                        sb.append("SELECT DISTINCT e From " + "Echantillon as e " + "JOIN e.codesAssignes c " + "WHERE c.code "
                           + critere.getOperateur());

                        sb.append(" :valeur");

                        if(nomChampMinFirst.equals("codeMorphos")){
                           sb.append(" AND c.isMorpho = 1");
                        }else{
                           sb.append(" AND c.isOrgane = 1");
                        }
                     }else{
                        sb.append("SELECT DISTINCT e From " + "Echantillon as e " + "WHERE e not in (" + "select ca.echantillon "
                           + "FROM CodeAssigne as ca");
                        if(nomChampMinFirst.equals("codeMorphos")){
                           sb.append(" WHERE ca.isMorpho = 1)");
                        }else{
                           sb.append(" WHERE ca.isOrgane = 1)");
                        }
                     }
                  }else if(nomChampMinFirst.matches("conforme.*Raison")){
                     appendNonConformitesSb(critere, sb, nomEntiteMajFirst);
                  }else if(nomChampMinFirst.matches("count.*")){
                     sb.append("SELECT DISTINCT e From " + nomEntiteMajFirst + " as e " + "JOIN e.echantillons z "
                        + "having count(z) " + critere.getOperateur());

                     if(!critere.getOperateur().equals("is null")){
                        sb.append(" :valeur");
                     }
                     // since 2.0.13 temp stockage
                  }else if(nomChampMinFirst.equals("tempStock")){
                     objets = new ArrayList<>();
                     objets.addAll(findTKStockableObjectsByTempStockWithBanquesManager(ce.getEntite(), value,
                        critere.getOperateur(), banks, false));
                     return objets;
                  }else{

                     final StringBuffer query = new StringBuffer("SELECT DISTINCT e FROM " + nomEntiteMajFirst + " ");

                     for(int j = 0; j < joins.size(); j++){
                        query.append(joins.get(j));
                     }

                     query.append("WHERE e." + nomChampMinFirst);

                     //Construction de la requête pour un champ délégué
                     if(delegate){

                        if("is null".equals(critere.getOperateur())){
                           sb.append("SELECT DISTINCT e From " + nomEntiteMajFirst + " e LEFT JOIN e.delegate d WHERE d."
                              + nomChampMinFirst + " " + critere.getOperateur() + " OR e."
                              + StringUtils.uncapitalize(nomEntiteMajFirst) + "Id NOT IN (SELECT d.delegator."
                              + StringUtils.uncapitalize(nomEntiteMajFirst) + "Id FROM Abstract" + nomEntiteMajFirst
                              + "Delegate d" + ")");
                        }else{

                           //On crée un champ pour représenter l'objet délégué
                           final ChampEntite chpEntiteDelegate = new ChampEntite();
                           chpEntiteDelegate.setNom("Delegate");
                           final Champ champDelegate = new Champ(chpEntiteDelegate);

                           //On ajoute de champ représentant le délégué comme cahmp ancêtre de la hiérarchie du champ
                           //sur lequel on fait la recherche afin d'avoir le délégué dans la liste des jointures
                           Champ ancetre = null;
                           if(champ.getChampParent() != null){
                              ancetre = champ.getChampParent();
                              while(ancetre.getChampParent() != null){
                                 ancetre = ancetre.getChampParent();
                              }
                              ancetre.setChampParent(champDelegate);
                           }

                           champ.getChampParent().setChampParent(champDelegate);

                           final List<String> delegateJoins = buildJoinsList(champ, null);

                           //Constructin de la requête
                           sb.append("SELECT DISTINCT e FROM " + nomEntiteMajFirst + " e ");

                           for(final String join : delegateJoins){
                              sb.append(join);
                           }

                           sb.append("WHERE p" + delegateJoins.size() + ".nom " + critere.getOperateur());

                           //On remet la hiérarchie du champ dans son état initial
                           ancetre.setChampParent(null);

                        }

                        //Construction de la requête pour un champ entité
                     }else{
                        sb.append("SELECT DISTINCT e From " + nomEntiteMajFirst + " as e WHERE" + " e." + nomChampMinFirst + " "
                           + critere.getOperateur());
                     }

                     if(!critere.getOperateur().equals("is null")){
                        sb.append(" :valeur");
                     }

                  }
                  //+ critere.getValeur() + "'");
                  // si l'entité recherchée n'est pas un patient
                  // ou une maladie, on ajoute un critère sur
                  // la banque
                  if(!nomEntiteMajFirst.equals("Patient") && !nomEntiteMajFirst.equals("Maladie")
                     && !nomChampMinFirst.contains("etablissement") && banks != null && !banks.isEmpty()){
                     sb.append(" AND ");
                     sb.append("e.banque in (:list)");
                  }
               }
            }
         }else if(champ.getChampDelegue() != null){

            final long nbContextes = banks.stream().map(b -> b.getContexte()).distinct().count();
            if(nbContextes != 1){
               throw new TKException(
                  "Impossible d'effectuer une recherche sur plusieurs contextes avec un paramètre de recherche lié au contexte");
            }

            final String entiteNom = StringUtils.uncapitalize(champ.getChampDelegue().getEntite().getNom());
            final String capitalizedEntiteNom = StringUtils.capitalize(champ.getChampDelegue().getEntite().getNom());
            final String nomChamp = StringUtils.uncapitalize(champ.getChampDelegue().getNom());

            //Cas particulier des thésauri
            if("thesaurusM".equals(champ.getChampDelegue().getDataType().getType())){

               sb.append("SELECT DISTINCT e FROM " + capitalizedEntiteNom + " e LEFT JOIN e.delegate d JOIN d." + nomChamp
                  + " t WHERE t.nom " + critere.getOperateur() + " :valeur");

            }else if(!"is null".equals(critere.getOperateur())){

               sb.append("SELECT DISTINCT e FROM " + capitalizedEntiteNom + " e JOIN e.delegate d WHERE d." + nomChamp + " "
                  + critere.getOperateur() + " :valeur");

            }else{

               sb.append("SELECT DISTINCT e From " + capitalizedEntiteNom + " e LEFT JOIN e.delegate d WHERE d." + nomChamp + " "
                  + critere.getOperateur() + " OR e." + entiteNom + "Id NOT IN (SELECT d.delegator." + entiteNom
                  + "Id FROM Abstract" + capitalizedEntiteNom + "Delegate d" + ")");

            }

         }else{
            throw new IllegalArgumentException();
         }

         /* On exécute la requête. */
         log.info("findObjetByCritereManager : Exécution de la requête : \n" + sb.toString());
         final EntityManager em = entityManagerFactory.createEntityManager();
         // si la liste n'est pas vide et que l'entité
         final TypedQuery<Object> query = em.createQuery(sb.toString(), Object.class);
         // recherchée n'est pas un patient
         if(sb.toString().contains("(:list)")){
            query.setParameter("list", banks);
         }
         if(sb.toString().contains(":valeur")){
            query.setParameter("valeur", value);
         }
         objets = query.getResultList();
         log.info("findObjetByCritereManager : requête terminée");
      }

      return objets;
   }

   private void appendNonConformitesSb(final Critere critere, final StringBuffer sb, final String nomEntiteMajFirst){

      String cNom = null;
      final Pattern p = Pattern.compile("Conforme(.*)\\.Raison");
      final Matcher m = p.matcher(critere.getChamp().getChampEntite().getNom());
      final boolean b = m.matches();
      if(b && m.groupCount() > 0){
         cNom = m.group(1);
      }

      if(!critere.getOperateur().equals("is null")){

         sb.append("SELECT DISTINCT e From " + nomEntiteMajFirst + " as e, ObjetNonConforme r " + "WHERE  r.objetId = e."
            + nomEntiteMajFirst.replaceFirst(".", (nomEntiteMajFirst.charAt(0) + "").toLowerCase()) + "Id "
            + "AND r.entite.entiteId = " + critere.getChamp().getChampEntite().getEntite().getEntiteId().toString()
            + " AND r.nonConformite.conformiteType.conformiteType = '" + cNom + "'"
            + " AND r.nonConformite.conformiteType.entite.entiteId = "
            + critere.getChamp().getChampEntite().getEntite().getEntiteId().toString() + " AND r.nonConformite.nom "
            + critere.getOperateur());

         sb.append(" :valeur");
      }else{
         sb.append("SELECT DISTINCT e From " + nomEntiteMajFirst + " as e WHERE e."
            + nomEntiteMajFirst.replaceFirst(".", (nomEntiteMajFirst.charAt(0) + "").toLowerCase()) + "Id "
            + "NOT IN (SELECT r.objetId  FROM ObjetNonConforme r " + "WHERE r.entite.entiteId = "
            + critere.getChamp().getChampEntite().getEntite().getEntiteId().toString()
            + " AND r.nonConformite.conformiteType.conformiteType = '" + cNom + "'"
            + " AND r.nonConformite.conformiteType.entite.entiteId = "
            + critere.getChamp().getChampEntite().getEntite().getEntiteId().toString() + ")");
      }
   }

   @Override
   public List<Integer> findObjetByCriteresWithBanquesManager(final List<Critere> criteres, final List<Banque> banques,
      final List<Object> values){ //TODO Refactorer
      List<Integer> objets = null;

      final StringBuffer sql = new StringBuffer();
      String nomEntiteMajFirst = "";
      final List<String> joins = new ArrayList<>();
      final Map<Champ, String> allParents = new Hashtable<>();
      final List<String> wheres = new ArrayList<>();
      final List<Critere> criteresChampsCalcules = new ArrayList<>();
      final List<Object> valuesChampsCalcules = new ArrayList<>();
      int cpt = 1;
      int nbWhere = 1;
      int nbBanquesInCriteres = 1;
      int nbChampAnnotations = 0;
      boolean withAnno = false;
      for(int k = 0; k < criteres.size(); k++){
         final Champ champ = criteres.get(k).getChamp();
         if(champ.getChampAnnotation() != null){
            if("calcule".equals(champ.dataType().getType())){
               criteresChampsCalcules.add(criteres.get(k));
               valuesChampsCalcules.add(values.get(k));
               final ChampAnnotation ca = champ.getChampAnnotation();
               if(ca != null && ca.getTableAnnotation() != null && ca.getTableAnnotation().getEntite() != null){
                  final String nomEntite = ca.getTableAnnotation().getEntite().getNom();
                  if(nomEntite != null){
                     withAnno = true;
                     nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                  }
               }
               continue;
            }
            ++nbChampAnnotations;
            final ChampAnnotation ca = champ.getChampAnnotation();
            if(ca != null && ca.getTableAnnotation() != null && ca.getTableAnnotation().getEntite() != null){
               final String nomEntite = ca.getTableAnnotation().getEntite().getNom();
               if(nomEntite != null){
                  withAnno = true;
                  nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                  final String nomEntiteMinFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toLowerCase());

                  joins.add("join av" + nbChampAnnotations + ".champAnnotation as ca" + nbChampAnnotations + " ");

                  Champ parent = champ.getChampParent();

                  final List<Champ> parents = new ArrayList<>();
                  while(parent != null && parent.getChampEntite() != null){
                     parents.add(0, parent);

                     final ChampEntite ceParent = parent.getChampEntite();
                     String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                     // On enlève le suffixe "Id"
                     if(nomParent.endsWith("Id")){
                        nomParent = nomParent.substring(0, nomParent.length() - 2);
                     }
                     final String nomEntiteParent = ceParent.getEntite().getNom();
                     nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                     parent = parent.getChampParent();
                  }

                  String prefixe = "e";
                  for(int i = 0; i < parents.size(); i++){
                     parent = parents.get(i);

                     if(!allParents.containsKey(parent)){
                        allParents.put(parent, "p" + cpt);
                        final ChampEntite ceParent = parent.getChampEntite();
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }

                        // Création des joins
                        final StringBuffer join = new StringBuffer();
                        if(i == 0){
                           join.append("JOIN e.");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(cpt);
                           join.append(" ");
                        }else{
                           join.append("JOIN p");
                           join.append(i);
                           join.append(".");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(cpt);
                           join.append(" ");
                        }
                        ++cpt;
                        joins.add(join.toString());
                     }

                     if(i == parents.size() - 1){
                        prefixe = allParents.get(parent);
                     }
                  }

                  String clause = "";
                  if(nbWhere == 1){
                     clause = "WHERE ";
                  }else{
                     clause = " AND ";
                  }
                  // si des jointures ont été faites
                  final StringBuffer where = new StringBuffer();
                  where.append(clause);

                  where.append("ca");
                  where.append(nbChampAnnotations);
                  where.append(".id = ");
                  where.append(ca.getId());
                  where.append(" AND " + prefixe + "." + nomEntiteMinFirst + "Id = av" + nbChampAnnotations + ".objetId");

                  /*
                   * where.append("e." + nomEntiteMinFirst +
                   * "Id = av.objetId" +
                   * " and ca.tableAnnotation.entite.nom = '" +
                   * nomEntiteMajFirst + "'");
                   */
                  if(ca.getDataType().getType().equals("alphanum") || ca.getDataType().getType().equals("date")){
                     where.append(" and av" + nbChampAnnotations + "." + ca.getDataType().getType() + " "
                        + criteres.get(k).getOperateur() + " :valeur");
                  }else if(ca.getDataType().getType().equals("datetime")){
                     if(((Calendar) values.get(k)).get(Calendar.HOUR_OF_DAY) == 0
                        && ((Calendar) values.get(k)).get(Calendar.MINUTE) == 0 && criteres.get(k).getOperateur().equals("=")){
                        final String[] calOps = new String[] {"year", "month", "day"};
                        for(final String op : calOps){
                           where.append(" and " + op + "(av" + nbChampAnnotations + ".date) = ");
                           where.append(op + "(:valeur" + nbWhere + ")");

                           // if (!op.equals("day")) {
                           //	where.append(" AND ");
                           //}
                        }
                     }else{
                        where
                           .append(" and av" + nbChampAnnotations + "." + "date " + criteres.get(k).getOperateur() + " :valeur");
                     }

                  }else if(ca.getDataType().getType().equals("hyperlien")){
                     where.append(" and " + nbChampAnnotations + ".alphanum " + criteres.get(k).getOperateur() + " :valeur");
                  }else if(ca.getDataType().getType().equals("num") || ca.getDataType().getType().equals("duree")){
                     where.append(" and cast(av" + nbChampAnnotations + ".alphanum as big_decimal) "
                        + criteres.get(k).getOperateur() + " :valeur");
                  }else if(ca.getDataType().getType().equals("thesaurus") || ca.getDataType().getType().equals("thesaurusM")){
                     where.append(" and av" + nbChampAnnotations + ".item " + criteres.get(k).getOperateur() + " :valeur");
                  }else if(ca.getDataType().getType().equals("boolean")){
                     where.append(" and av" + nbChampAnnotations + ".bool " + criteres.get(k).getOperateur() + " :valeur");
                  }else{
                     where.append(" and av" + nbChampAnnotations + "." + ca.getDataType().getType() + " "
                        + criteres.get(k).getOperateur() + " :valeur");
                  }
                  if(!ca.getDataType().getType().equals("datetime") || ((Calendar) values.get(k)).get(Calendar.HOUR_OF_DAY) > 0
                     || ((Calendar) values.get(k)).get(Calendar.MINUTE) > 0 || !criteres.get(k).getOperateur().equals("=")){
                     where.append(nbWhere);
                  }
                  wheres.add(where.toString());
                  ++nbWhere;
               }
            }
         }else if(champ.getChampEntite() != null || null != champ.getChampDelegue()){
            AbstractTKChamp ce = null;
            Entite entite = null;
            ChampEntite queryChamp = null;
            if(null != champ.getChampEntite()){
               ce = champ.getChampEntite();
               entite = champ.getChampEntite().getEntite();
               queryChamp = champ.getChampEntite().getQueryChamp();
            }else if(null != champ.getChampDelegue()){
               ce = champ.getChampDelegue();
               entite = champ.getChampDelegue().getEntite();
            }
            if(ce != null && entite != null){

               final String nomEntite = entite.getNom();

               if(nomEntite != null){

                  nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                  String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());

                  if(nomChampMinFirst.endsWith("Id")){
                     nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
                  }else if(nomChampMinFirst.endsWith("s") && queryChamp != null){
                     // ne concerne donc que les champ entite Maladie.Collaborateurs .
                     nomChampMinFirst = "";
                  }

                  Champ parent = champ.getChampParent();

                  final List<Champ> parents = new ArrayList<>();
                  while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
                     parents.add(0, parent);

                     AbstractTKChamp ceParent = null;
                     String nomEntiteParent = null;
                     if(null != parent.getChampEntite()){
                        ceParent = parent.getChampEntite();
                        nomEntiteParent = parent.getChampEntite().getEntite().getNom();
                     }else if(null != parent.getChampDelegue()){
                        ceParent = parent.getChampDelegue();
                        nomEntiteParent = parent.getChampDelegue().getEntite().getNom();
                     }

                     String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                     // On enlève le suffixe "Id"
                     if(nomParent.endsWith("Id")){
                        nomParent = nomParent.substring(0, nomParent.length() - 2);
                     }
                     nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                     parent = parent.getChampParent();
                  }
                  // quand un parent est trouvé
                  // incrémente z pour pouvoir assigner 
                  // p + cpt au bon niveau de joins
                  String correspParent = "";

                  String prefixe = "e";

                  for(int i = 0; i < parents.size(); i++){
                     parent = parents.get(i);
                     if(!allParents.containsKey(parent)){
                        allParents.put(parent, "p" + cpt);
                        AbstractTKChamp ceParent = null;
                        if(null != parent.getChampEntite()){
                           ceParent = parent.getChampEntite();
                        }else if(null != parent.getChampDelegue()){
                           ceParent = parent.getChampDelegue();
                        }
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }

                        // Création des joins
                        final StringBuffer join = new StringBuffer();
                        if(i == 0){
                           join.append("JOIN e.");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(cpt);
                           join.append(" ");

                        }else{
                           //HERE A TESTER
                           if(!correspParent.equals("")){
                              join.append("JOIN " + correspParent);
                              correspParent = "";
                           }else{
                              join.append("JOIN p");
                              join.append(cpt - 1);

                           }
                           join.append(".");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(cpt);
                           join.append(" ");
                        }
                        ++cpt;
                        joins.add(join.toString());
                     }else{
                        correspParent = allParents.get(parent);
                     }
                  }

                  String clause = "";
                  if(nbWhere == 1){
                     clause = "WHERE ";
                  }else{
                     clause = " AND ";
                  }

                  // si des jointures ont été faites
                  final StringBuffer where = new StringBuffer();
                  where.append(clause);

                  if(values.get(k) instanceof Calendar && ((Calendar) values.get(k)).get(Calendar.HOUR_OF_DAY) == 0
                     && ((Calendar) values.get(k)).get(Calendar.MINUTE) == 0 && criteres.get(k).getOperateur().equals("=")){
                     final String[] calOps = new String[] {"year", "month", "day"};
                     for(final String op : calOps){
                        where.append(op + "(");
                        if(parents.size() > 0){
                           prefixe = allParents.get(parents.get(parents.size() - 1));
                           where.append(prefixe);
                        }else{
                           where.append("e");
                        }
                        where.append("." + nomChampMinFirst + ") " + criteres.get(k).getOperateur() + " " + op + "(:valeur"
                           + nbWhere + ")");
                        if(!op.equals("day")){
                           where.append(" AND ");
                        }
                     }
                  }else{
                     if(parents.size() > 0){
                        prefixe = allParents.get(parents.get(parents.size() - 1));
                        where.append(prefixe);
                     }else{
                        where.append("e");
                     }

                     if(!nomChampMinFirst.equals("")){
                        where.append("." + nomChampMinFirst + " " + criteres.get(k).getOperateur() + " :valeur" + nbWhere);
                     }else{
                        where.append(" " + criteres.get(k).getOperateur() + " :valeur" + nbWhere);
                     }
                  }
                  // 2.0.10.2 Recherche patient implique 
                  if(parents.size() > 0 && nomEntiteMajFirst.equals("Patient") && !nomEntite.equals("Patient")
                     && !nomEntite.equals("Maladie") && null != parents.get(parents.size() - 1).getChampEntite()
                     && null == parents.get(parents.size() - 1).getChampDelegue()
                     && !parents.get(parents.size() - 1).getChampEntite().getNom().equals("PatientMedecins")
                     && !parents.get(parents.size() - 1).getChampEntite().getNom().equals("Collaborateurs")
                     && !parents.get(parents.size() - 1).getChampEntite().getNom().equals("ServicePreleveurId")
                     && !parents.get(parents.size() - 1).getChampEntite().getNom().equals("Risques")){

                     Champ parentTest = parents.get(parents.size() - 1);
                     boolean isFromDelegate = false;
                     while(!isFromDelegate && null != parentTest){
                        parentTest = parentTest.getChampParent();
                        if(null != parentTest && null != parentTest.getChampDelegue()){
                           isFromDelegate = true;
                           break;
                        }
                     }
                     if(!isFromDelegate){
                        where.append(" AND ");
                        prefixe = allParents.get(parents.get(parents.size() - 1));
                        where.append(prefixe);
                        where.append(".banque in (:list");
                        where.append(nbBanquesInCriteres);
                        where.append(")");
                        ++nbBanquesInCriteres;
                     }
                  }
                  wheres.add(where.toString());
                  ++nbWhere;
               }
            }
         }
      }

      // création de la requête
      String idAttribut = "";
      if(!nomEntiteMajFirst.equals("ProdDerive")){
         idAttribut = nomEntiteMajFirst.toLowerCase() + "Id";
      }else{
         idAttribut = "prodDeriveId";
      }
      sql.append("SELECT DISTINCT e.");
      sql.append(idAttribut);
      sql.append(" From " + nomEntiteMajFirst + " as e ");
      if(withAnno){
         for(int i = 1; i <= nbChampAnnotations; i++){
            sql.append(", AnnotationValeur av");
            sql.append(i);
            sql.append(" ");
         }

      }
      for(int j = 0; j < joins.size(); j++){
         sql.append(joins.get(j));
      }
      for(int j = 0; j < wheres.size(); j++){
         sql.append(wheres.get(j));
      }
      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
         if(wheres.isEmpty()){
            sql.append(" WHERE e.banque in (:list)");
         }else{
            sql.append(" and e.banque in (:list)");
         }
      }

      /* On exécute la requête. */
      log.debug("findObjetByCritereManager : Exécution de la requête : \n" + sql.toString());

      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> query = em.createQuery(sql.toString(), Integer.class);
      values.removeAll(valuesChampsCalcules);
      for(int i = 0; i < values.size(); i++){
         final StringBuffer sb = new StringBuffer("valeur");
         sb.append(i + 1);
         query.setParameter(sb.toString(), values.get(i));
      }

      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      if(sql.toString().contains("(:list)")){
         query.setParameter("list", banques);
      }
      for(int i = 1; i < nbBanquesInCriteres; i++){
         final StringBuffer sb = new StringBuffer("list");
         sb.append(i);
         query.setParameter(sb.toString(), banques);
      }
      objets = query.getResultList();

      traitementChampsCalcules(criteresChampsCalcules, valuesChampsCalcules, objets, nomEntiteMajFirst);

      log.debug("Fin de la requête");

      return objets;
   }

   private List<Object> traitementChampsCalcules(final List<Critere> criteresChampsCalcules,
      final List<Object> valuesChampsCalcules, final String entite, final List<Banque> banks){ //FIXME ChampCalcule Recherche - Traitement lourd
      List<Object> result = null;
      if(!criteresChampsCalcules.isEmpty() && !valuesChampsCalcules.isEmpty()){
         final StringBuilder sql = new StringBuilder();
         sql.append("SELECT e FROM ");
         sql.append(entite);
         sql.append(" as e");
         if(banks != null && !banks.isEmpty() && !entite.equals("Patient")){
            sql.append(" WHERE e.banque in (:list)");
         }
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);
         if(banks != null && !banks.isEmpty() && !entite.equals("Patient")){
            query.setParameter("list", banks);
         }
         final List<Object> objects = query.getResultList();
         if(!objects.isEmpty()){
            result = new ArrayList<>();
            result.addAll(objects);
            for(final Object obj : objects){
               for(int i = 0; i < criteresChampsCalcules.size(); i++){
                  final Object val = champCalculeManager.getValueForObjectManager(
                     criteresChampsCalcules.get(i).getChamp().getChampAnnotation().getChampCalcule(), obj);
                  if(null == val /*FIXME ChampCalcule Recherche - Comparaison avec valeur nulle ? */ || !compareValues(val,
                     criteresChampsCalcules.get(i).getOperateur(), valuesChampsCalcules.get(i))){
                     result.remove(obj);
                     break;
                  }
               }
            }
         }
      }
      return result;
   }

   private void traitementChampsCalcules(final List<Critere> criteresChampsCalcules, final List<Object> valuesChampsCalcules,
      final List<Integer> objetsIds, final String entite){ //FIXME ChampCalcule Recherche - Traitement lourd
      if(!criteresChampsCalcules.isEmpty() && !valuesChampsCalcules.isEmpty() && !objetsIds.isEmpty()){
         final StringBuilder sql = new StringBuilder();
         sql.append("SELECT e FROM ");
         sql.append(entite);
         sql.append(" as e WHERE e.");
         sql.append(entite.replaceFirst(".", (entite.charAt(0) + "").toLowerCase()));
         sql.append("Id in (:list)");
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<TKdataObject> query = em.createQuery(sql.toString(), TKdataObject.class);
         query.setParameter("list", objetsIds);
         final List<TKdataObject> objs = query.getResultList();
         for(final TKdataObject obj : objs){
            for(int i = 0; i < criteresChampsCalcules.size(); i++){
               final Object val = champCalculeManager
                  .getValueForObjectManager(criteresChampsCalcules.get(i).getChamp().getChampAnnotation().getChampCalcule(), obj);
               if(null == val /*FIXME ChampCalcule Recherche - Comparaison avec valeur nulle ? */ || !compareValues(val,
                  criteresChampsCalcules.get(i).getOperateur(), valuesChampsCalcules.get(i))){
                  for(int j = 0; j < objetsIds.size(); j++){
                     if(objetsIds.get(j) == obj.listableObjectId()){
                        objetsIds.remove(j);
                     }
                  }
                  break;
               }
            }
         }
      }
   }

   private Boolean compareValues(final Object val1, final String operateur, final Object val2){ //FIXME ChampCalcule Recherche - meilleure méthode
      if(val1 instanceof Number){
         final Double valNum1 = new Double(val1.toString());
         final Double valNum2 = new Double(val2.toString());
         return compareValues(valNum1, operateur, valNum2);
      }
      if(val1 instanceof Date || val2 instanceof Date){
         final Calendar cal1 = Calendar.getInstance();
         cal1.setTime((Date) val1);
         final Calendar cal2 = Calendar.getInstance();
         cal2.setTime((Date) val2);
         return compareValues(cal1, operateur, cal2);
      }
      if(val1 instanceof Calendar || val2 instanceof Calendar){
         return compareValues((Calendar) val1, operateur, (Calendar) val2);
      }
      return false;
   }

   private Boolean compareValues(final Double val1, final String operateur, final Double val2){ //FIXME ChampCalcule Recherche - meilleure méthode
      Boolean result = false;
      switch(operateur){
         case "=":
            result = val1.equals(val2);
            break;
         case "!=":
            result = !val1.equals(val2);
            break;
         case ">":
            result = val1 > val2;
            break;
         case ">=":
            result = val1 >= val2;
            break;
         case "<":
            result = val1 <= val2;
            break;
         case "<=":
            result = val1 < val2 || val1.equals(val2);
            break;
         default:
            break;

      }
      return result;
   }

   private Boolean compareValues(final Calendar val1, final String operateur, final Calendar val2){ //FIXME ChampCalcule Recherche - meilleure méthode
      boolean result = false;
      switch(operateur){
         case "=":
            result = (0 == val1.compareTo(val2));
            break;
         case "!=":
            result = (0 != val1.compareTo(val2));
            break;
         case ">":
            result = (0 < val1.compareTo(val2));
            break;
         case ">=":
            result = (0 <= val1.compareTo(val2));
            break;
         case "<":
            result = (0 > val1.compareTo(val2));
            break;
         case "<=":
            result = (0 >= val1.compareTo(val2));
            break;
         default:
            break;

      }
      return result;
   }

   /**
    * Retourne le nom de l'entité correspondant au champ 
    * @param champ
    * @return
    */
   private String getNomEntiteAncetre(final Champ champ){

      String nomEntiteAncetre = null;
      Champ parent = champ.getChampParent();

      while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){

         if(null != parent.getChampEntite()){
            nomEntiteAncetre = parent.getChampEntite().getEntite().getNom();
         }else if(null != parent.getChampDelegue()){
            nomEntiteAncetre = parent.getChampDelegue().getEntite().getNom();
         }

         parent = parent.getChampParent();
      }

      return StringUtils.capitalize(nomEntiteAncetre);

   }

   /**
    * Retourne l'ensemble des champs ancêtres d'un champ
    * @param champ
    * @return
    */
   private List<Champ> getChampAncestors(final Champ champ){

      final List<Champ> ancestors = new ArrayList<>();

      Champ parent = champ.getChampParent();

      while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
         ancestors.add(0, parent);
         parent = parent.getChampParent();
      }

      return ancestors;

   }

   /**
    * Retourne la liste de critères SQL de type JOIN pour un champ
    * @param champ
    * @return
    */
   private List<String> buildJoinsList(final Champ champ, final List<String> joinsList){

      final List<String> joins = new ArrayList<>();

      if(joinsList != null){
         joins.addAll(joinsList);
      }

      Champ parent = champ.getChampParent();
      final Hashtable<Champ, String> allParents = new Hashtable<>();

      final List<Champ> parents = new ArrayList<>();
      while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
         parents.add(0, parent);

         AbstractTKChamp ceParent = null;
         if(null != parent.getChampEntite()){
            ceParent = parent.getChampEntite();
         }else if(null != parent.getChampDelegue()){
            ceParent = parent.getChampDelegue();
         }

         String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
         // On enlève le suffixe "Id"
         if(nomParent.endsWith("Id")){
            nomParent = nomParent.substring(0, nomParent.length() - 2);
         }

         parent = parent.getChampParent();
      }

      String correspParent = "";

      for(int i = 0; i < parents.size(); i++){

         final int idx = joins.size() + 1;

         parent = parents.get(i);
         if(!allParents.containsKey(parent)){
            allParents.put(parent, "p" + idx);
            AbstractTKChamp ceParent = null;
            if(null != parent.getChampEntite()){
               ceParent = parent.getChampEntite();
            }else if(null != parent.getChampDelegue()){
               ceParent = parent.getChampDelegue();
            }
            String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
            // On enlève le suffixe "Id"
            if(nomParent.endsWith("Id")){
               nomParent = nomParent.substring(0, nomParent.length() - 2);
            }

            // Création des joins
            final StringBuffer join = new StringBuffer();
            if(i == 0){
               join.append("JOIN e.");
               join.append(nomParent);
               join.append(" as p");
               join.append(idx);
               join.append(" ");

            }else{
               //HERE A TESTER
               if(!correspParent.equals("")){
                  join.append("JOIN " + correspParent);
                  correspParent = "";
               }else{
                  join.append("JOIN p");
                  join.append(idx - 1);

               }
               join.append(".");
               join.append(nomParent);
               join.append(" as p");
               join.append(idx);
               join.append(" ");
            }
            joins.add(join.toString());
         }else{
            correspParent = allParents.get(parent);
         }
      }

      return joins;

   }

   @Override
   public List<Object> findObjetByCritereWithBanquesManager(final Critere critere, final List<Banque> banques, final Object value,
      final boolean idSearch){
      List<Object> objets = new ArrayList<>();
      if(!idSearch || (null != value && value instanceof Collection && !((Collection<?>) value).isEmpty())){
         if(critere.getChamp() != null){
            final StringBuffer sb = new StringBuffer("");
            final Champ champ = critere.getChamp();
            if(champ.getChampAnnotation() != null){
               final ChampAnnotation ca = champ.getChampAnnotation();
               if(ca != null && ca.getTableAnnotation() != null && ca.getTableAnnotation().getEntite() != null){
                  final String nomEntite = ca.getTableAnnotation().getEntite().getNom();
                  if(nomEntite != null){
                     final String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                     final String nomEntiteMinFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toLowerCase());
                     sb.append("SELECT e From " + nomEntiteMajFirst + " as e, AnnotationValeur av"
                        + " join av.champAnnotation as ca" + " WHERE" + " e." + nomEntiteMinFirst + "Id = av.objetId"
                        + " and ca.tableAnnotation.entite.nom = '" + nomEntiteMajFirst + "'");

                     if(ca.getDataType().getType().equals("num") || ca.getDataType().getType().equals("hyperlien")
                        || ca.getDataType().getType().equals("duree")){
                        sb.append(" and av.alphanum " + critere.getOperateur() + " :valeur");
                     }else if(ca.getDataType().getType().equals("alphanum") || ca.getDataType().getType().equals("date")){
                        sb.append(" and av." + ca.getDataType().getType() + " " + critere.getOperateur() + " :valeur");
                     }else if(ca.getDataType().getType().equals("datetime")){
                        sb.append(" and av.date " + critere.getOperateur() + " :valeur");
                     }else if(ca.getDataType().getType().equals("thesaurus") || ca.getDataType().getType().equals("thesaurusM")){
                        sb.append(" and av.item " + critere.getOperateur() + " :valeur");
                     }else if(ca.getDataType().getType().equals("boolean")){
                        sb.append(" and av.bool " + critere.getOperateur() + " :valeur");
                     }else{
                        sb.append(" and av." + ca.getDataType().getType() + " " + critere.getOperateur() + " :valeur");
                     }

                     // si la liste n'est pas vide et que l'entité
                     // recherchée n'est pas un patient
                     if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
                        sb.append(" and e.banque in (:list)");
                     }
                  }
               }
            }else if(champ.getChampEntite() != null){
               final ChampEntite ce = critere.getChamp().getChampEntite();
               if(ce != null && ce.getEntite() != null){
                  final String nomEntite = ce.getEntite().getNom();
                  if(nomEntite != null){
                     String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                     String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
                     // recherche à partir des ids implique
                     // ne pas retirer le suffixe Id
                     if(nomChampMinFirst.endsWith("Id") && !idSearch){
                        nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
                     }
                     Champ parent = champ.getChampParent();
                     final List<String> joins = new ArrayList<>();
                     final List<Champ> parents = new ArrayList<>();
                     while(parent != null && parent.getChampEntite() != null){
                        parents.add(0, parent);

                        final ChampEntite ceParent = parent.getChampEntite();
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }
                        final String nomEntiteParent = ceParent.getEntite().getNom();
                        nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                        parent = parent.getChampParent();
                     }

                     for(int i = 0; i < parents.size(); i++){
                        parent = parents.get(i);
                        final ChampEntite ceParent = parent.getChampEntite();
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }

                        // Création des joins
                        final StringBuffer join = new StringBuffer();
                        if(i == 0){
                           join.append("JOIN e.");
                           join.append(nomParent);
                           join.append(" as p1 ");
                        }else{
                           join.append("JOIN p");
                           join.append(i);
                           join.append(".");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(i + 1);
                           join.append(" ");
                        }
                        joins.add(join.toString());
                     }
                     // création de la requête
                     sb.append("SELECT DISTINCT e");
                     if(idSearch){
                        sb.append(
                           "." + nomEntiteMajFirst.replaceFirst(".", (nomEntiteMajFirst.charAt(0) + "").toLowerCase()) + "Id");
                     }
                     sb.append(" From " + nomEntiteMajFirst + " as e ");
                     for(int i = 0; i < joins.size(); i++){
                        sb.append(joins.get(i));
                     }

                     // si des jointures ont été faites
                     if(parents.size() > 0){
                        sb.append("WHERE p");
                        sb.append(parents.size());
                     }else{
                        sb.append("WHERE e");
                     }
                     sb.append("." + nomChampMinFirst + " " + critere.getOperateur() + " :valeur");

                     // si la liste n'est pas vide et que l'entité
                     // recherchée n'est pas un patient
                     if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
                        sb.append(" and e.banque in (:list)");
                     }
                  }
               }
            }else{
               throw new IllegalArgumentException();
            }
            /* On exécute la requête. */
            log.info("findObjetByCritereManager : Exécution de la requête : \n" + sb.toString());
            final EntityManager em = entityManagerFactory.createEntityManager();
            final TypedQuery<Object> query = em.createQuery(sb.toString(), Object.class).setParameter("valeur", value);
            // si la liste n'est pas vide et que l'entité
            // recherchée n'est pas un patient
            if(sb.toString().contains("(:list)")){
               query.setParameter("list", banques);
            }

            objets = query.getResultList();
         }
      }
      return objets;
   }

   @Override
   public List<Integer> findObjetByCriteresWithBanquesDeriveVersionManager(final List<Critere> criteres,
      final List<Banque> banques, final List<Object> values, final boolean searchForDerives){
      List<Integer> objets = null;

      final StringBuffer sql = new StringBuffer();
      String nomEntiteMajFirst = "";
      final List<String> joins = new ArrayList<>();
      final Hashtable<Champ, String> allParents = new Hashtable<>();
      final List<String> wheres = new ArrayList<>();
      Entite entiteTransformation = null;
      int cpt = 1;
      int nbWhere = 1;
      // int nbBanquesInCriteres = 1;
      for(int k = 0; k < criteres.size(); k++){
         if(criteres.get(k) != null){
            final Champ champ = criteres.get(k).getChamp();
            if(champ != null){
               if(champ.getChampEntite() != null || null != champ.getChampDelegue()){
                  AbstractTKChamp ce = null;
                  Entite entite = null;
                  ChampEntite queryChamp = null;
                  if(null != champ.getChampEntite()){
                     ce = champ.getChampEntite();
                     entite = champ.getChampEntite().getEntite();
                     queryChamp = champ.getChampEntite().getQueryChamp();
                  }else if(null != champ.getChampDelegue()){
                     ce = champ.getChampDelegue();
                     entite = champ.getChampDelegue().getEntite();
                  }
                  if(ce != null && entite != null){
                     final String nomEntite = entite.getNom();
                     if(nomEntite != null){
                        nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                        String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
                        if(nomChampMinFirst.endsWith("Id")){
                           nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
                        }else if(nomChampMinFirst.endsWith("s") && queryChamp != null){
                           // ne concerne donc que les champ entite Maladie.Collaborateurs .
                           nomChampMinFirst = "";
                        }
                        Champ parent = champ.getChampParent();
                        final List<Champ> parents = new ArrayList<>();
                        while(parent != null && (parent.getChampEntite() != null || parent.getChampDelegue() != null)){
                           AbstractTKChamp ceParent = null;
                           String nomEntiteParent = null;
                           if(null != parent.getChampEntite()){
                              ceParent = parent.getChampEntite();
                              nomEntiteParent = parent.getChampEntite().getEntite().getNom();
                           }else if(null != parent.getChampDelegue()){
                              ceParent = parent.getChampDelegue();
                              nomEntiteParent = parent.getChampDelegue().getEntite().getNom();
                           }

                           if(!ceParent.getNom().contains("ProdDerives")){
                              parents.add(0, parent);
                           }else{
                              entiteTransformation = parent.getChampEntite().getEntite();
                           }

                           String nomParent =
                              ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                           // On enlève le suffixe "Id"
                           if(nomParent.endsWith("Id")){
                              nomParent = nomParent.substring(0, nomParent.length() - 2);
                           }
                           nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                           parent = parent.getChampParent();
                        }

                        // quand un parent est trouvé
                        // incrémente z pour pouvoir assigner 
                        // p + cpt au bon niveau de joins
                        String correspParent = "";

                        for(int i = 0; i < parents.size(); i++){
                           parent = parents.get(i);
                           if(!allParents.containsKey(parent)){
                              allParents.put(parent, "p" + cpt);
                              AbstractTKChamp ceParent = null;
                              if(null != parent.getChampEntite()){
                                 ceParent = parent.getChampEntite();
                              }else if(null != parent.getChampDelegue()){
                                 ceParent = parent.getChampDelegue();
                              }
                              String nomParent =
                                 ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                              // On enlève le suffixe "Id"
                              if(nomParent.endsWith("Id")){
                                 nomParent = nomParent.substring(0, nomParent.length() - 2);
                              }

                              // Création des joins
                              final StringBuffer join = new StringBuffer();
                              if(i == 0){
                                 join.append("JOIN e.");
                                 join.append(nomParent);
                                 join.append(" as p1 ");
                              }else{
                                 if(!correspParent.equals("")){
                                    join.append("JOIN " + correspParent);
                                    correspParent = "";
                                 }else{
                                    join.append("JOIN p");
                                    join.append(cpt - 1);
                                 }

                                 join.append(".");
                                 join.append(nomParent);
                                 join.append(" as p");
                                 join.append(cpt);
                                 join.append(" ");

                              }
                              if(!join.toString().contains("prodDerive")){
                                 joins.add(join.toString());
                              }
                              ++cpt;
                           }else{
                              correspParent = allParents.get(parent);
                           }
                        }

                        // si des jointures ont été faites
                        String prefixe = "e";
                        StringBuffer where;

                        if(values.get(k) instanceof Calendar && ((Calendar) values.get(k)).get(Calendar.HOUR_OF_DAY) == 0
                           && ((Calendar) values.get(k)).get(Calendar.MINUTE) == 0 && criteres.get(k).getOperateur().equals("=")){
                           final String[] calOps = new String[] {"year", "month", "day"};
                           for(final String op : calOps){
                              where = new StringBuffer();
                              if(parents.size() > 0){
                                 if(searchForDerives){
                                    prefixe = allParents.get(parents.get(parents.size() - 1));
                                    where.append("AND " + op + "(");
                                    where.append(prefixe);
                                 }else{
                                    where.append("AND " + op + "(prod");
                                 }
                              }else{
                                 if(searchForDerives){
                                    where.append("AND " + op + "(e");
                                 }else{
                                    where.append("AND " + op + "(prod");
                                 }
                              }

                              where.append("." + nomChampMinFirst + ") " + criteres.get(k).getOperateur() + " " + op + "(:valeur"
                                 + nbWhere + ") ");
                              wheres.add(where.toString());
                           }
                           ++nbWhere;
                        }else{
                           where = new StringBuffer();
                           if(parents.size() > 0){
                              if(searchForDerives){
                                 prefixe = allParents.get(parents.get(parents.size() - 1));
                                 where.append("AND ");
                                 where.append(prefixe);
                              }else{
                                 where.append("AND prod");
                              }
                           }else{
                              if(searchForDerives){
                                 where.append("AND e");
                              }else{
                                 where.append("AND prod");
                              }
                           }

                           if(!nomChampMinFirst.equals("")){
                              where.append(
                                 "." + nomChampMinFirst + " " + criteres.get(k).getOperateur() + " :valeur" + nbWhere + " ");
                           }else{
                              where.append(" " + criteres.get(k).getOperateur() + " :valeur" + nbWhere + " ");
                           }
                           wheres.add(where.toString());
                           ++nbWhere;
                        }
                     }
                  }
               }
            }
         }
      }
      // création de la requête
      if(searchForDerives){
         sql.append("SELECT DISTINCT prod.prodDeriveId " + "From ProdDerive as prod, ");
         sql.append(entiteTransformation.getNom());
         sql.append(" as e ");
      }else{
         sql.append("SELECT DISTINCT e.");
         sql.append(nomEntiteMajFirst.toLowerCase() + "Id");
         sql.append(" From " + nomEntiteMajFirst + " as e, ProdDerive as prod ");
      }
      final StringBuffer where = new StringBuffer();
      if(searchForDerives){
         String ent = entiteTransformation.getNom();
         ent = ent.replace(ent.charAt(0) + "", (ent.charAt(0) + "").toLowerCase());
         where.append("WHERE prod.transformation.objetId = e." + ent + "Id");
         where.append(" AND prod.transformation.entite " + "= :entite ");
      }else{
         String ent = entiteTransformation.getNom();
         ent = ent.replace(ent.charAt(0) + "", (ent.charAt(0) + "").toLowerCase());
         where.append("WHERE prod.transformation.objetId = ");
         if(joins.size() > 0){
            where.append("p" + joins.size());
         }else{
            where.append("e");
         }
         where.append("." + ent + "Id");
         where.append(" AND prod.transformation.entite " + "= :entite ");
      }
      wheres.add(0, where.toString());

      for(int j = 0; j < joins.size(); j++){
         sql.append(joins.get(j));
      }
      for(int j = 0; j < wheres.size(); j++){
         sql.append(wheres.get(j));
      }
      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      sql.append(" and prod.banque in (:list)");
      if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
         if(!searchForDerives){
            sql.append(" and e.banque in (:list)");
         }
      }

      /* On exécute la requête. */
      log.info("findObjetByCritereManager : Exécution de la requête : \n" + sql.toString());
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> query = em.createQuery(sql.toString(), Integer.class);
      query.setParameter("entite", entiteTransformation);
      for(int i = 0; i < values.size(); i++){
         final StringBuffer sb = new StringBuffer("valeur");
         sb.append(i + 1);
         query.setParameter(sb.toString(), values.get(i));
      }

      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      if(sql.toString().contains("(:list)")){
         query.setParameter("list", banques);
      }

      objets = query.getResultList();

      return objets;
   }

   @Override
   public List<Object> findObjetByCritereWithBanquesDeriveVersionManager(final Critere critere, final List<Banque> banques,
      final Object value, final boolean searchForDerives, final boolean idSearch){
      List<Object> objets = new ArrayList<>();
      Entite entiteTransformation = null;
      if(!idSearch || (value != null && value instanceof Collection && !((Collection<?>) value).isEmpty())){
         if(critere.getChamp() != null){
            final StringBuffer sb = new StringBuffer("");
            final Champ champ = critere.getChamp();
            if(champ.getChampEntite() != null){
               final ChampEntite ce = critere.getChamp().getChampEntite();
               if(ce != null && ce.getEntite() != null){
                  final String nomEntite = ce.getEntite().getNom();
                  if(nomEntite != null){
                     String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
                     String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
                     if(nomChampMinFirst.endsWith("Id") && !idSearch){
                        nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
                     }
                     Champ parent = champ.getChampParent();
                     final List<String> joins = new ArrayList<>();
                     final List<Champ> parents = new ArrayList<>();
                     while(parent != null && parent.getChampEntite() != null){
                        if(!parent.getChampEntite().getNom().contains("ProdDerives")){
                           parents.add(0, parent);
                        }else{
                           entiteTransformation = parent.getChampEntite().getEntite();
                        }

                        final ChampEntite ceParent = parent.getChampEntite();
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }
                        final String nomEntiteParent = ceParent.getEntite().getNom();
                        nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                        parent = parent.getChampParent();
                     }

                     for(int i = 0; i < parents.size(); i++){
                        parent = parents.get(i);
                        final ChampEntite ceParent = parent.getChampEntite();
                        String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                        // On enlève le suffixe "Id"
                        if(nomParent.endsWith("Id")){
                           nomParent = nomParent.substring(0, nomParent.length() - 2);
                        }

                        // Création des joins
                        final StringBuffer join = new StringBuffer();
                        if(i == 0){
                           join.append("JOIN e.");
                           join.append(nomParent);
                           join.append(" as p1 ");
                        }else{
                           join.append("JOIN p");
                           join.append(i);
                           join.append(".");
                           join.append(nomParent);
                           join.append(" as p");
                           join.append(i + 1);
                           join.append(" ");
                        }
                        if(!join.toString().contains("prodDerive")){
                           joins.add(join.toString());
                        }
                     }
                     // création de la requête
                     if(searchForDerives){
                        if(idSearch){
                           sb.append("SELECT DISTINCT prod.prodDeriveId ");
                        }else{
                           sb.append("SELECT DISTINCT prod ");
                        }

                        sb.append("From ProdDerive as prod, ");
                        sb.append(entiteTransformation.getNom());
                        sb.append(" as e ");
                     }else{
                        sb.append("SELECT DISTINCT e");
                        if(idSearch){
                           sb.append(
                              "." + nomEntiteMajFirst.replaceFirst(".", (nomEntiteMajFirst.charAt(0) + "").toLowerCase()) + "Id");
                        }
                        sb.append(" From " + nomEntiteMajFirst + " as e");
                        sb.append(", ProdDerive as prod ");
                     }
                     for(int i = 0; i < joins.size(); i++){
                        sb.append(joins.get(i));
                     }

                     if(searchForDerives){
                        String ent = entiteTransformation.getNom();
                        ent = ent.replace(ent.charAt(0) + "", (ent.charAt(0) + "").toLowerCase());
                        sb.append("WHERE prod.transformation.objetId = e." + ent + "Id");
                        sb.append(" AND prod.transformation.entite " + "= :entite ");
                     }else{
                        String ent = entiteTransformation.getNom();
                        ent = ent.replace(ent.charAt(0) + "", (ent.charAt(0) + "").toLowerCase());
                        sb.append("WHERE prod.transformation.objetId = ");
                        if(joins.size() > 0){
                           sb.append("p" + joins.size());
                        }else{
                           sb.append("e");
                        }
                        sb.append("." + ent + "Id");
                        sb.append(" AND prod.transformation.entite " + "= :entite ");
                     }
                     // si des jointures ont été faites
                     if(joins.size() > 0){
                        if(searchForDerives){
                           sb.append("AND p");
                           sb.append(parents.size());
                        }else{
                           sb.append("AND prod");
                        }
                     }else{
                        if(searchForDerives){
                           sb.append("AND e");
                        }else{
                           sb.append("AND prod");
                        }
                     }
                     sb.append("." + nomChampMinFirst + " " + critere.getOperateur() + " :valeur");

                     // si la liste n'est pas vide et que l'entité
                     // recherchée n'est pas un patient
                     if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
                        if(searchForDerives){
                           sb.append(" and prod.banque in (:list)");
                        }else{
                           sb.append(" and e.banque in (:list)");
                        }
                     }
                  }
               }
            }else{
               throw new IllegalArgumentException();
            }
            // On exécute la requête.
            final EntityManager em = entityManagerFactory.createEntityManager();
            final TypedQuery<Object> query = em.createQuery(sb.toString(), Object.class).setParameter("valeur", value);
            query.setParameter("entite", entiteTransformation);
            // si la liste n'est pas vide et que l'entité
            // recherchée n'est pas un patient
            if(sb.toString().contains("(:list)")){
               query.setParameter("list", banques);
            }
            objets = query.getResultList();
         }
      }
      return objets;
   }

   @Override
   public List<Integer> findObjetByCritereInListWithBanquesManager(final Critere critere, final List<Banque> banques,
      final List<Object> values, final boolean cumulative){
      List<Integer> objets = null;
      if(critere.getChamp() != null){
         String nomEntite = null;
         String nomEntiteMajFirst = null;
         // String nomEntiteMinFirst = null;
         String nomChampMinFirst = null;
         final StringBuffer sb = new StringBuffer("");
         final Champ champ = critere.getChamp();
         if(champ.getChampAnnotation() != null && champ.getChampAnnotation().getDataType().getType().equals("thesaurusM")){

            sb.append("SELECT DISTINCT av.objetId From AnnotationValeur av");
            sb.append(" JOIN av.champAnnotation ca ");
            sb.append(" WHERE ca.id = :champId ");

            if(!cumulative || values.isEmpty()){
               sb.append("AND av.item in (:valeurs)");
            }else{
               // first round val:1
               sb.append("AND av.item = :val1");
            }

            // banques restriction
            if(banques != null){
               sb.append(" AND av.banque in (:list)");
            }

            if(cumulative && !values.isEmpty()){
               final String sbPart = sb.toString();
               for(int i = 2; i < values.size() + 1; i++){
                  sb.append(" AND av.objetId in ");
                  sb.append("(" + sbPart.replaceFirst(":val1", ":val" + i).replaceAll(" av", " av" + i) + ") ");
               }
            }
         }else if(champ.getChampEntite() != null){
            final ChampEntite ce = critere.getChamp().getChampEntite();
            if(ce != null && ce.getEntite() != null){
               nomEntite = ce.getEntite().getNom();
               nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
               if(nomChampMinFirst.endsWith("Id")){
                  nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
               }
            }

            nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());

            Champ parent = champ.getChampParent();
            final List<String> joins = new ArrayList<>();
            final List<Champ> parents = new ArrayList<>();
            while(parent != null && parent.getChampEntite() != null){
               parents.add(0, parent);

               final ChampEntite ceParent = parent.getChampEntite();
               String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
               // On enlève le suffixe "Id"
               if(nomParent.endsWith("Id")){
                  nomParent = nomParent.substring(0, nomParent.length() - 2);
               }
               final String nomEntiteParent = ceParent.getEntite().getNom();
               nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

               parent = parent.getChampParent();
            }

            for(int i = 0; i < parents.size(); i++){
               parent = parents.get(i);
               final ChampEntite ceParent = parent.getChampEntite();
               String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
               // On enlève le suffixe "Id"
               if(nomParent.endsWith("Id")){
                  nomParent = nomParent.substring(0, nomParent.length() - 2);
               }

               // Création des joins
               final StringBuffer join = new StringBuffer();
               if(i == 0){
                  join.append("JOIN e.");
                  join.append(nomParent);
                  join.append(" as p1 ");
               }else{
                  join.append("JOIN p");
                  join.append(i);
                  join.append(".");
                  join.append(nomParent);
                  join.append(" as p");
                  join.append(i + 1);
                  join.append(" ");
               }
               joins.add(join.toString());
            }
            // création de la requête
            sb.append("SELECT DISTINCT e.");
            if(!nomEntiteMajFirst.equals("ProdDerive")){
               sb.append(nomEntiteMajFirst.toLowerCase() + "Id");
            }else{
               sb.append("prodDeriveId");
            }
            sb.append(" From " + nomEntiteMajFirst + " as e ");
            for(int i = 0; i < joins.size(); i++){
               sb.append(joins.get(i));
            }

            // si des jointures ont été faites
            if(parents.size() > 0){
               sb.append("WHERE p");
               sb.append(parents.size());
            }else{
               sb.append("WHERE e");
            }

            if(!cumulative || values.isEmpty()){
               sb.append("." + nomChampMinFirst + " in (:valeurs)");
            }else{
               // first round val:1
               sb.append("." + nomChampMinFirst + " = :val1");
            }

            // si la liste n'est pas vide et que l'entité
            // recherchée n'est pas un patient
            if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
               sb.append(" and e.banque in (:list)");
            }

            // cumulative par ajout select in (:val2) and in (:val3)
            if(cumulative && !values.isEmpty()){
               final String sbPart = sb.toString();
               final String joinCol = nomEntiteMajFirst.toLowerCase() + "Id";

               for(int i = 2; i < values.size() + 1; i++){
                  sb.append(" AND e." + joinCol + " in ");
                  sb.append("(" + sbPart.replaceFirst(":val1", ":val" + i) + ") ");
               }
            }
         }else{
            throw new IllegalArgumentException();
         }
         /* On exécute la requête. */
         log.info("findObjetByCritereManager : Exécution de la requête : \n" + sb.toString() + " avec les paramètres " + values);
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         if(!cumulative || values.isEmpty()){
            query.setParameter("valeurs", values);
         }else{
            for(int i = 0; i < values.size(); i++){
               query.setParameter("val" + (i + 1), values.get(i));
            }
         }

         // si annotation 
         if(sb.toString().contains(":champId")){
            query.setParameter("champId", champ.getChampAnnotation().getId());
         }

         // si la liste n'est pas vide et que l'entité
         // recherchée n'est pas un patient
         if(sb.toString().contains("(:list)")){
            query.setParameter("list", banques);
         }

         objets = query.getResultList();
      }
      return objets;
   }

   @Override
   public List<Integer> findObjetByCritereOnCodesWithBanquesManager(final Critere critere, final List<Banque> banques,
      final List<String> codes, final List<String> libelles, final String value, final boolean isMorpho){
      List<Integer> objets = null;
      if(critere.getChamp() != null){
         final StringBuffer sb = new StringBuffer("");
         final Champ champ = critere.getChamp();
         final ChampEntite ce = critere.getChamp().getChampEntite();
         if(ce != null && ce.getEntite() != null){
            final String nomEntite = ce.getEntite().getNom();
            if(nomEntite != null){
               String nomEntiteMajFirst = nomEntite.replaceFirst(".", (nomEntite.charAt(0) + "").toUpperCase());
               String nomChampMinFirst = ce.getNom().replaceFirst(".", (ce.getNom().charAt(0) + "").toLowerCase());
               if(nomChampMinFirst.endsWith("Id")){
                  nomChampMinFirst = nomChampMinFirst.substring(0, nomChampMinFirst.length() - 2);
               }
               Champ parent = champ.getChampParent();
               final List<String> joins = new ArrayList<>();
               final List<Champ> parents = new ArrayList<>();
               while(parent != null && parent.getChampEntite() != null){
                  parents.add(0, parent);

                  final ChampEntite ceParent = parent.getChampEntite();
                  String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                  // On enlève le suffixe "Id"
                  if(nomParent.endsWith("Id")){
                     nomParent = nomParent.substring(0, nomParent.length() - 2);
                  }
                  final String nomEntiteParent = ceParent.getEntite().getNom();
                  nomEntiteMajFirst = nomEntiteParent.replaceFirst(".", (nomEntiteParent.charAt(0) + "").toUpperCase());

                  parent = parent.getChampParent();
               }

               for(int i = 0; i < parents.size(); i++){
                  parent = parents.get(i);
                  final ChampEntite ceParent = parent.getChampEntite();
                  String nomParent = ceParent.getNom().replaceFirst(".", (ceParent.getNom().charAt(0) + "").toLowerCase());
                  // On enlève le suffixe "Id"
                  if(nomParent.endsWith("Id")){
                     nomParent = nomParent.substring(0, nomParent.length() - 2);
                  }

                  // Création des joins
                  final StringBuffer join = new StringBuffer();
                  if(i == 0){
                     join.append("JOIN e.");
                     join.append(nomParent);
                     join.append(" as p1 ");
                  }else{
                     join.append("JOIN p");
                     join.append(i);
                     join.append(".");
                     join.append(nomParent);
                     join.append(" as p");
                     join.append(i + 1);
                     join.append(" ");
                  }
                  joins.add(join.toString());
               }
               // création de la requête
               sb.append("SELECT DISTINCT e.");
               if(!nomEntiteMajFirst.equals("ProdDerive")){
                  sb.append(nomEntiteMajFirst.toLowerCase() + "Id");
               }else{
                  sb.append("prodDeriveId");
               }
               sb.append(" From " + nomEntiteMajFirst + " as e ");
               for(int i = 0; i < joins.size(); i++){
                  sb.append(joins.get(i));
               }

               // si des jointures ont été faites
               String prefixe = "";
               if(parents.size() > 0){
                  prefixe = "p" + parents.size();
               }else{
                  prefixe = "e";
               }

               sb.append("WHERE (");
               if(codes != null && codes.size() > 0){
                  sb.append(prefixe);
                  sb.append(".code in (:codes) OR ");
               }
               if(libelles != null && libelles.size() > 0){
                  sb.append(prefixe);
                  sb.append(".libelle in (:libelles) OR ");
               }
               sb.append(prefixe);
               sb.append(".code like :code OR ");
               sb.append(prefixe);
               sb.append(".libelle like :libelle) AND ");
               sb.append(prefixe);
               // sb.append(".isMorpho = :morpho ");
               if(isMorpho){
                  sb.append(".isMorpho = 1 ");
               }else{
                  sb.append(".isOrgane = 1 ");
               }

               if(parents.size() > 1){
                  sb.append(" AND p");
                  sb.append(parents.size() - 1);
                  sb.append(".banque in (:listEchans)");
               }

               // si la liste n'est pas vide et que l'entité
               // recherchée n'est pas un patient
               if(banques != null && !banques.isEmpty() && !nomEntiteMajFirst.equals("Patient")){
                  sb.append(" and e.banque in (:list)");
               }
            }
         }
         /* On exécute la requête. */
         log.info("findObjetByCritereManager : Exécution de la requête : \n" + sb.toString() + " avec les paramètres " + codes
            + ", " + libelles + ", " + value + " et " + isMorpho);
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         if(sb.toString().contains(":codes")){
            query.setParameter("codes", codes);
         }
         if(sb.toString().contains("libelles")){
            query.setParameter("libelles", libelles);
         }
         query.setParameter("code", value);
         query.setParameter("libelle", value);
         // query.setParameter("morpho", isMorpho);
         // si la liste n'est pas vide et que l'entité
         // recherchée n'est pas un patient
         if(sb.toString().contains("(:list)")){
            query.setParameter("list", banques);
         }
         if(sb.toString().contains("(:listEchans)")){
            query.setParameter("listEchans", banques);
         }

         objets = query.getResultList();
      }
      return objets;
   }

   @Override
   public List<Integer> findPrelevementsByNbEchantillonsWithBanquesManager(final String operateur, final Integer nb,
      final List<Banque> banques){

      if(operateur != null && !operateur.equals("") && nb != null && nb >= 0 && banques != null && banques.size() > 0){
         final Long nbCasted = new Long(nb);

         // construction de la requête
         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT distinct(p.prelevementId) " + "FROM Prelevement p ");
         sb.append("WHERE (select count(e) From Echantillon e ");
         sb.append("WHERE e.prelevement=p ");
         sb.append("and e.quantite > 0) ");
         sb.append(operateur);
         sb.append(" :nb");
         sb.append(" and p.banque in (:list)");

         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class).setParameter("nb", nbCasted);
         query.setParameter("list", banques);

         return query.getResultList();

      }
      return new ArrayList<>();

   }

   @Override
   public List<Integer> findPrelevementsByMedecinsManager(final Collaborateur collab, final List<Banque> banques){

      if(collab != null && banques != null && banques.size() > 0){

         // construction de la requête
         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT distinct(p.prelevementId) " + "FROM Prelevement p ");
         sb.append("LEFT JOIN p.maladie.collaborateurs o ");
         sb.append("LEFT JOIN p.maladie.patient.patientMedecins t ");
         sb.append("WHERE (t.pk.collaborateur = :c " + "OR o = :c ) ");
         sb.append("AND p.banque in (:list)");

         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class).setParameter("c", collab);
         query.setParameter("list", banques);

         return query.getResultList();

      }
      return new ArrayList<>();

   }

   @Override
   public List<Integer> findPrelevementsByAgePatientWithBanquesManager(final String operateur, final Integer age,
      final List<Banque> banques, final String dbms){

      if(operateur != null && !operateur.equals("") && age != null && age >= 0 && banques != null && banques.size() > 0
         && dbms != null){

         // banque Ids
         final List<Integer> banqueIds = new ArrayList<>();
         for(final Banque b : banques){
            banqueIds.add(b.getBanqueId());
         }

         String addDateInf = " DATE_ADD(a.date_naissance, INTERVAL " + age.toString() + " YEAR) ";
         String addDateSup = " DATE_ADD(a.date_naissance, INTERVAL " + new Integer(age + 1).toString() + " YEAR) ";
         if(dbms.equalsIgnoreCase("oracle")){
            addDateInf = " add_months(a.date_naissance, " + new Integer(age * 12).toString() + ") ";
            addDateSup = " add_months(a.date_naissance, " + new Integer((age + 1) * 12).toString() + ") ";
         }

         // Double dateInf = Double.valueOf(age * 365);
         // Integer dateSup = (age + 1) * 365;

         // requête calculant la diff entre les 2 dates
         // StringBuffer diffAge = new StringBuffer();
         // diffAge.append("datediff(p.datePrelevement - pat.dateNaissance)");

         // construction de la requête
         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT distinct(p.prelevement_id) " + "FROM PRELEVEMENT p, PATIENT a, MALADIE m ");
         sb.append("WHERE p.maladie_id = m.maladie_id ");
         sb.append("AND m.patient_id = a.patient_id ");
         sb.append("AND a.date_naissance is not null ");
         sb.append("AND p.date_prelevement is not null ");
         sb.append("AND (p.date_prelevement");
         if(operateur.equals("=")){
            sb.append(" > ");
            sb.append(addDateInf);
            sb.append(" AND p.date_prelevement < ");
            sb.append(addDateSup);
         }else if(operateur.equals("!=")){
            sb.append(" < ");
            sb.append(addDateInf);
            sb.append(" OR p.date_prelevement > ");
            sb.append(addDateSup);
         }else if(operateur.equals(">")){
            sb.append(operateur);
            sb.append(addDateSup);
         }else{
            sb.append(operateur);
            sb.append(addDateInf);
         }
         sb.append(")");
         sb.append(" and p.banque_id in (:list)");

         // if (operateur.equals("=")) {
         // sb.append(diffAge.toString());
         // sb.append(" >= :dateInf");
         // sb.append(" AND ");
         // sb.append(diffAge.toString());
         // sb.append(" <= :dateSup");
         // } else if (operateur.equals("<")) {
         // sb.append(diffAge.toString());
         // sb.append(" < :dateInf");
         // } else if (operateur.equals(">")) {
         // sb.append(diffAge.toString());
         // sb.append(" >= :dateSup");
         // }		

         final EntityManager em = entityManagerFactory.createEntityManager();

         final Query query = em.createNativeQuery(sb.toString());

         //			if (sb.toString().contains("dateInf")) {
         //				query.setParameter("dateInf", dateInf);
         //			}
         //			if (sb.toString().contains("dateSup")) {
         //				query.setParameter("dateSup", dateSup);
         //			}
         query.setParameter("list", banqueIds);

         final List<Integer> ids = new ArrayList<>();

         for(final Object num : query.getResultList()){
            if(num instanceof Integer){
               ids.add(Integer.class.cast(num));
            }else{
               ids.add(Integer.valueOf(Number.class.cast(num).intValue()));
            }
         }

         return ids;

      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findObjetByCritereOnCodesWithBanquesDerivesVersionManager(final List<Banque> banques,
      final List<String> codes, final List<String> libelles, final String value, final boolean isMorpho,
      final Entite echanEntite){
      List<Integer> objets = new ArrayList<>();
      final StringBuffer sb = new StringBuffer();
      // création de la requête
      sb.append("SELECT DISTINCT prod.prodDeriveId " + "From ProdDerive prod," + " Echantillon as e ");
      sb.append("JOIN e.codesAssignes as p1 ");

      sb.append("WHERE prod.transformation.objetId = e.echantillonId");
      sb.append(" AND prod.transformation.entite " + "= :entite ");

      sb.append("AND (");
      if(codes != null && codes.size() > 0){
         sb.append("p1.code in (:codes) OR ");
      }
      if(libelles != null && libelles.size() > 0){
         sb.append("p1.libelle in (:libelles) OR ");
      }
      sb.append("p1.code like :code OR ");
      sb.append("p1.libelle like :libelle) AND ");
      // sb.append("p1.isMorpho = :morpho ");
      if(isMorpho){
         sb.append("p1.isMorpho = 1 ");
      }else{
         sb.append("p1.isOrgane = 1 ");
      }

      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      if(banques != null && !banques.isEmpty()){
         sb.append(" and e.banque in (:list)");
      }
      /* On exécute la requête. */
      log.info("findObjetByCritereManager : Exécution de la requête : \n" + sb.toString() + " avec les paramètres " + codes + ", "
         + libelles + ", " + value + " et " + isMorpho);
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
      if(sb.toString().contains(":codes")){
         query.setParameter("codes", codes);
      }
      if(sb.toString().contains("libelles")){
         query.setParameter("libelles", libelles);
      }
      query.setParameter("code", value);
      query.setParameter("libelle", value);
      // query.setParameter("morpho", isMorpho);
      query.setParameter("entite", echanEntite);
      // si la liste n'est pas vide et que l'entité
      // recherchée n'est pas un patient
      if(sb.toString().contains("(:list)")){
         query.setParameter("list", banques);
      }

      objets = query.getResultList();

      return objets;
   }

   @Override
   public List<Integer> findEchantillonsByRequeteBiocapManager(final String dbms, final List<Banque> banques,
      final List<Service> services, final Calendar dateInf, final Calendar dateSup, final Integer age, final ObjetStatut statut){
      if(age != null && age >= 0 && banques != null && banques.size() > 0 && services != null && dateInf != null
         && dateSup != null){
         final Double dateAgeSup = age + .0;
         // requête calculant la diff entre les 2 dates
         final StringBuffer diffAge = new StringBuffer();
         if(dbms.contentEquals(ConfigManager.DB_MYSQL)){
            diffAge.append("datediff(p.datePrelevement,pat.dateNaissance)/365.25");
         }else{
            diffAge.append("trunc(p.datePrelevement - pat.dateNaissance)/365.25");
         }
         // construction de la requête
         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT distinct(e.echantillonId) " + "FROM Echantillon e ");
         sb.append("join e.prelevement as p ");
         sb.append("join p.maladie as m ");
         sb.append("join m.patient as pat ");
         sb.append("WHERE ");
         sb.append("p.datePrelevement >= :dateInf ");
         sb.append("AND p.datePrelevement <= :dateSup ");
         sb.append("AND ");
         sb.append(diffAge.toString());
         sb.append(" < :dateSupAge ");
         sb.append("AND p.banque in (:listBanques)");
         // since 2.0.13
         // si aucun services, alors recherche dans tous
         if(!services.isEmpty()){
            final Service empty = new Service();
            if(services.contains(empty)){
               services.remove(empty);
               if(!services.isEmpty()){
                  sb.append(" AND (p.servicePreleveur is null OR p.servicePreleveur in (:listServices))");
               }else{
                  sb.append(" AND p.servicePreleveur is null");
               }
            }else{
               sb.append(" AND p.servicePreleveur in (:listServices)");
            }
         }
         if(statut != null){
            sb.append(" AND e.objetStatut = :statut");
         }

         /* On exécute la requête. */
         log.info("findEchantillonsByRequeteBiocap " + ": Exécution de la requête : \n" + sb.toString() + " avec les paramètres "
            + banques + ", " + services + ", " + dateInf.getTime() + ", " + dateSup.getTime() + "et " + age);
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         query.setParameter("dateInf", dateInf);
         query.setParameter("dateSup", dateSup);
         query.setParameter("dateSupAge", dateAgeSup);
         query.setParameter("listBanques", banques);
         if(!services.isEmpty()){
            query.setParameter("listServices", services);
         }
         if(statut != null){
            query.setParameter("statut", statut);
         }

         return query.getResultList();

      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findObjetIdsFromNonConformiteNomManager(final String nom, final ConformiteType cType, final Plateforme pf,
      final List<Banque> banks){

      List<Integer> ids = new ArrayList<>();

      if(cType != null){

         // récupération des non conformites
         List<NonConformite> ncfs = null;
         if(nom != null && !nom.equals("")){
            ncfs = nonConformiteDao.findByTypePfAndNom(cType, pf, "%" + nom + "%");
         }

         // récupération des ids
         ids = objetNonConformeManager.findObjetIdsByNonConformitesManager(ncfs);

         // filtre des ids en fonction de la banque
         ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(cType.getEntite(), ids, banks);
      }
      return ids;
   }

   @Override
   public List<Integer> findFileUploadedManager(final Champ fileChp, final Entite targetE, final List<Banque> banques,
      final boolean empty){
      final List<Integer> ids = new ArrayList<>();

      if(fileChp != null && targetE != null && banques != null && !banques.isEmpty()){

         Entite fromE = null;
         final StringBuffer sb = new StringBuffer();

         // cr anapath
         if(fileChp.getChampEntite() != null){
            if(fileChp.getChampEntite().getNom().equals("CrAnapath")){
               fromE = entiteManager.findByIdManager(3);
               sb.append("SELECT distinct(e.echantillonId) " + "FROM Echantillon e ");
               if(!empty){
                  sb.append("WHERE e.crAnapath is not null ");
               }else{
                  sb.append("WHERE e.crAnapath is null ");
               }
               sb.append("AND e.banque in (:listBanques)");
            }
         }else if(fileChp.getChampAnnotation() != null){
            fromE = fileChp.getChampAnnotation().getTableAnnotation().getEntite();
            if(!empty){
               sb.append("SELECT distinct(a.objetId) " + "FROM AnnotationValeur a ");
               sb.append("WHERE a.fichier is not null ");
               sb.append("AND a.champAnnotation = :chpAnno ");
               sb.append("AND a.banque in (:listBanques)");
            }else{
               final String jpaKey = fromE.getNom().toLowerCase() + "Id";
               sb.append("SELECT distinct(e." + jpaKey + ") FROM " + fromE.getNom() + " e ");
               if(fromE.getNom().equals("Patient")){
                  sb.append("JOIN e.maladies m ");
                  sb.append("JOIN m.prelevements p ");
               }
               sb.append("WHERE e." + jpaKey + " not in (");
               sb.append("SELECT distinct(a.objetId) " + "FROM AnnotationValeur a ");
               sb.append("WHERE a.fichier is not null ");
               sb.append("AND a.champAnnotation = :chpAnno ");
               sb.append("AND a.banque in (:listBanques)");
               sb.append(") ");
               if(fromE.getNom().equals("Patient")){
                  sb.append("AND p.banque in (:listBanques)");
               }else{
                  sb.append("AND e.banque in (:listBanques)");
               }
            }
         }

         // execution de la requete
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         query.setParameter("listBanques", banques);
         if(fileChp.getChampAnnotation() != null){
            query.setParameter("chpAnno", fileChp.getChampAnnotation());
         }
         ids.addAll(query.getResultList());

         // correspondance si Entite target <> from
         // si target = ProdDerive, rech descendante car obligatoirement 
         // from > ProdDerive
         if(!targetE.equals(fromE)){
            return correspondanceIdManager.findTargetIdsFromIdsManager(ids, fromE, targetE, banques, true);
         }
      }

      return ids;
   }

   @Override
   public List<? extends Object> findTKStockableObjectsByTempStockWithBanquesManager(final Entite ent, final Object temp,
      final String op, final List<Banque> banques, final boolean fetchIds){

      if(ent != null && op != null && !op.equals("") && banques != null && banques.size() > 0){

         // banque Ids
         final List<Integer> banqueIds = new ArrayList<>();
         for(final Banque b : banques){
            banqueIds.add(b.getBanqueId());
         }

         // construction de la requête
         final StringBuffer sb = new StringBuffer();

         final String queryCol =
            !fetchIds ? "e" : ent.getNom().replaceFirst(".", (ent.getNom().charAt(0) + "").toLowerCase()) + "Id";

         if(!op.equals("is null")){
            sb.append("SELECT DISTINCT " + queryCol + " From " + ent.getNom() + " as e "
               + "WHERE get_conteneur(e.emplacement.emplacementId) " + "in (select c.conteneurId from Conteneur c "
               + "where c.temp " + op);

            sb.append(" :valeur)");
         }else{
            sb.append("SELECT DISTINCT " + queryCol + " From " + ent.getNom() + " as e " + "WHERE (e.emplacement is null OR "
               + "get_conteneur(e.emplacement.emplacementId) in " + "(select c.conteneurId from Conteneur c "
               + "where c.temp is null))");
         }
         sb.append(" AND ");
         sb.append("e.banque.banqueId in (:list)");

         final EntityManager em = entityManagerFactory.createEntityManager();

         final TypedQuery<Object> query = em.createQuery(sb.toString(), Object.class);
         if(sb.toString().contains(":valeur")){
            query.setParameter("valeur", temp);
         }
         query.setParameter("list", banqueIds);

         final List<Object> objs = query.getResultList();

         if(fetchIds){
            final List<Integer> ids = new ArrayList<>();

            for(final Object num : objs){
               if(num instanceof Integer){
                  ids.add((Integer) num);
               }else{
                  ids.add(Integer.valueOf(((Number) num).intValue()));
               }
            }
            return ids;
         }

         return objs;

      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findPrelevementIdsViaLaboInterManager(final Object obj, final List<Banque> banques){

      if(obj != null && banques != null && banques.size() > 0){

         // construction de la requête
         final StringBuffer sb = new StringBuffer();
         sb.append("SELECT DISTINCT e.prelevementId FROM Prelevement e JOIN e.laboInters l where l.");

         if(obj instanceof Etablissement){
            sb.append("service.etablissement");
         }else if(obj instanceof Service){
            sb.append("service");
         }else if(obj instanceof Collaborateur){
            sb.append("collaborateur");
         }else{ // break
            throw new IllegalArgumentException();
         }
         sb.append(" = :obj AND e.banque in (:list)");

         final EntityManager em = entityManagerFactory.createEntityManager();

         final TypedQuery<Integer> query = em.createQuery(sb.toString(), Integer.class);
         query.setParameter("obj", obj);
         query.setParameter("list", banques);

         final List<Integer> ids = query.getResultList();

         return ids;

      }
      return new ArrayList<>();
   }

}