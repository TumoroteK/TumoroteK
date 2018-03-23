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
package fr.aphp.tumorotek.manager.impl.stockage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementImportConteneurException;
import fr.aphp.tumorotek.manager.exception.EntiteObjectIdNotExistException;
import fr.aphp.tumorotek.manager.exception.InvalidPositionException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stockage.EmplacementValidator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Emplacement.
 * Interface créée le 02/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.1.1
 *
 */
public class EmplacementManagerImpl implements EmplacementManager
{

   private final Log log = LogFactory.getLog(EmplacementManager.class);

   /** Bean Dao EmplacementDao. */
   private EmplacementDao emplacementDao;
   /** Bean Dao EmplacementDao. */
   private TerminaleDao terminaleDao;
   /** Bean Dao EmplacementDao. */
   private EntiteDao entiteDao;
   /** Bean Manager. */
   private EntiteManager entiteManager;
   /** Bean validator. */
   private EmplacementValidator emplacementValidator;
   private JpaTransactionManager txManager;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private ConteneurManager conteneurManager;
   private EnceinteManager enceinteManager;
   private TerminaleManager terminaleManager;
   private EchantillonDao echantillonDao;
   private ProdDeriveDao prodDeriveDao;

   public void setEmplacementDao(final EmplacementDao eDao){
      this.emplacementDao = eDao;
   }

   public void setTerminaleDao(final TerminaleDao tDao){
      this.terminaleDao = tDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setEmplacementValidator(final EmplacementValidator eValidator){
      this.emplacementValidator = eValidator;
   }

   public void setTxManager(final JpaTransactionManager tManager){
      this.txManager = tManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setConteneurManager(final ConteneurManager cManager){
      this.conteneurManager = cManager;
   }

   public void setEnceinteManager(final EnceinteManager eManager){
      this.enceinteManager = eManager;
   }

   public void setTerminaleManager(final TerminaleManager tManager){
      this.terminaleManager = tManager;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   /**
    * Recherche un Emplacement dont l'identifiant est passé en paramètre.
    * @param emplacementId Identifiant de l'Emplacement que l'on recherche.
    * @return Un Emplacement.
    */
   @Override
   public Emplacement findByIdManager(final Integer emplacementId){
      return emplacementDao.findById(emplacementId);
   }

   /**
    * Recherche tous les Emplacements présents dans la base.
    * @return Liste d'Emplacements.
    */
   @Override
   public List<Emplacement> findAllObjectsManager(){
      return emplacementDao.findAll();
   }

   @Override
   public List<Emplacement> findByTerminaleAndVideManager(final Terminale terminale, final boolean vide){
      log.debug("Recherche de tous les emplacements vides " + "ou non d'une terminale");
      if(terminale != null){
         return emplacementDao.findByTerminaleAndVide(terminale, vide);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Emplacement> findByTerminaleAndPosition(final Terminale terminale, final Integer position){
      if(terminale != null && position != null){
         return emplacementDao.findByTerminaleAndPosition(terminale, position);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Emplacement> findByTerminaleWithOrder(final Terminale terminale){
      log.debug("Recherche de tous les emplacements d'une terminale");
      if(terminale != null){
         return emplacementDao.findByTerminaleWithOrder(terminale);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean checkEmplacementInTerminale(final Emplacement emplacement){
      Boolean valide = true;

      // on vérifie que l'emplacement n'est pas vide
      if(emplacement != null){
         final Terminale term = emplacement.getTerminale();

         if(term != null && emplacement.getPosition() != null){
            // sa position doit être contenue dans les limites de
            // l'enceinte terminale
            final Integer nbPlaces = term.getTerminaleType().getNbPlaces();
            if(emplacement.getPosition() > nbPlaces){
               valide = false;
            }
         }else{
            valide = false;
         }

      }else{
         valide = false;
      }

      return valide;
   }

   @Override
   public Emplacement findByEmplacementAdrlManager(final String adrl, final Banque banque){
      Emplacement empl = null;
      if(adrl != null && !adrl.equals("")){
         final String[] elts = adrl.split("\\.");
         if(elts.length >= 3){
            final String position = elts[elts.length - 1];
            final String terminale = elts[elts.length - 2];
            final String conteneur = elts[0];
            final List<String> enceintes = new ArrayList<>();
            if(elts.length > 3){
               for(int i = 1; i < elts.length - 2; i++){
                  enceintes.add(elts[i]);
               }
            }

            final List<Conteneur> conteneurs = conteneurManager.findByBanqueAndCodeManager(banque, conteneur);
            if(conteneurs.size() == 1){
               List<Enceinte> enceintesTmp = new ArrayList<>();
               for(int i = 0; i < enceintes.size(); i++){
                  if(i == 0){
                     enceintesTmp = enceinteManager.findByConteneurAndNomManager(conteneurs.get(0), enceintes.get(i));
                  }else if(enceintesTmp.size() == 1){
                     enceintesTmp = enceinteManager.findByEnceintePereAndNomManager(enceintesTmp.get(0), enceintes.get(i));
                  }
               }

               List<Terminale> terminales = new ArrayList<>();
               if(enceintesTmp.size() == 1){
                  terminales = terminaleManager.findByEnceinteAndNomManager(enceintesTmp.get(0), terminale);
               }

               Integer pos = -1;

               if(terminales.size() == 1){
                  try{
                     pos = getPositionByAdrl(terminales.get(0), position);
                  }catch(final Exception e){
                     log.error(e);
                  }
                  //						try {
                  //							// pos = Integer.parseInt(position);
                  //						} catch (NumberFormatException n) {
                  //							log.error(n);
                  //						}
                  if(pos > 0){
                     final List<Emplacement> empls = findByTerminaleAndPosition(terminales.get(0), pos);

                     if(empls.size() == 1){
                        empl = empls.get(0);
                     }else if(empls.size() == 0){
                        empl = new Emplacement();
                        empl.setTerminale(terminales.get(0));
                        empl.setPosition(pos);
                        //empl.setVide(false);
                     }
                  }
               }
            }else if(conteneurs.size() > 1){
               throw new EmplacementImportConteneurException();
            }
         }
      }

      return empl;
   }

   @Override
   public Emplacement findDoublonManager(final Emplacement emplacement){
      if(emplacement != null){
         final Terminale term = emplacement.getTerminale();
         if(emplacement.getEmplacementId() == null){
            final List<Emplacement> empls = emplacementDao.findByTerminaleAndPosition(term, emplacement.getPosition());
            // boolean doublon = false;
            //				for (int i = 0; i < empls.size(); i++) {
            //					if (empls.get(i).equals(emplacement)) {
            //						return empls.get(i);
            //					}
            //				}
            if(!empls.isEmpty()){
               return empls.get(0);
            }
         }else{
            final List<Emplacement> empls = emplacementDao.findByExcludedIdTerminale(emplacement.getEmplacementId(), term);
            if(empls.contains(emplacement)){
               return empls.get(empls.indexOf(emplacement));
            }
         }
      }
      return null;
   }

   @Override
   public Boolean isUsedObjectManager(Emplacement emplacement){

      if(emplacement != null){
         emplacement = emplacementDao.mergeObject(emplacement);

         return !(emplacement.getEchantillons().size() == 0 && emplacement.getProdDerives().size() == 0);

      }else{
         return false;
      }
   }

   @Override
   public Conteneur getConteneurManager(final Emplacement emplacement){
      Conteneur cont = null;

      if(emplacement != null && emplacement.getTerminale() != null){
         Enceinte enc = emplacement.getTerminale().getEnceinte();

         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
         }

         if(enc.getConteneur() != null){
            cont = enc.getConteneur();
         }
      }

      return cont;
   }

   @Override
   public String getTerminaleAdrlManager(final Terminale terminale){
      final StringBuffer adrl = new StringBuffer();

      if(terminale != null){
         adrl.insert(0, terminale.getNom());
         adrl.insert(0, ".");
         Conteneur cont = null;
         Enceinte enc = terminale.getEnceinte();
         adrl.insert(0, enc.getNom());
         adrl.insert(0, ".");
         while(enc.getEnceintePere() != null){
            enc = enc.getEnceintePere();
            adrl.insert(0, enc.getNom());
            adrl.insert(0, ".");
         }

         if(enc.getConteneur() != null){
            cont = enc.getConteneur();
            adrl.insert(0, cont.getCode());
         }
      }
      return adrl.toString();

   }

   @Override
   public String getAdrlManager(final Emplacement emplacement, final boolean positions){
      final StringBuffer adrl = new StringBuffer();

      if(emplacement != null){
         // existing emplacement
         if(emplacement.getEmplacementId() != null){

            java.sql.Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet res = null;
            try{
               con = txManager.getDataSource().getConnection();
               con.setAutoCommit(false);

               final String func = !positions ? "get_adrl" : "get_adrl_positions";

               final String sql = "select " + func + "(?) from dual";
               pstmt = con.prepareStatement(sql);
               pstmt.setInt(1, emplacement.getEmplacementId());
               res = pstmt.executeQuery();
               while(res.next()){
                  adrl.append(res.getString(1));
                  break;
               }
            }catch(final SQLException e){
               log.error(e);
            }finally{
               if(con != null){
                  try{
                     con.close();
                  }catch(final Exception e){
                     con = null;
                  }
               }
               if(res != null){
                  try{
                     res.close();
                  }catch(final Exception e){
                     res = null;
                  }
               }
               if(pstmt != null){
                  try{
                     pstmt.close();
                  }catch(final Exception e){
                     pstmt = null;
                  }
               }
            }
            // newly created emplacement
         }else if(emplacement.getTerminale() != null && emplacement.getPosition() != null){

            final Terminale terminale = emplacement.getTerminale();
            adrl.append(getTerminaleAdrlManager(terminale));

            adrl.append(".");
            adrl.append(getNumerotationByPositionAndTerminaleManager(emplacement.getPosition(), emplacement.getTerminale()));
         }
      }
      return adrl.toString();
   }

   @Override
   public List<String> getNomsForEmplacementsManager(final List<Emplacement> emplacements){
      final List<String> results = new ArrayList<>();

      // on vérifie qu'il y a des emplacements
      if(emplacements != null){
         java.sql.Connection con = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmt2 = null;
         ResultSet res = null;
         try{
            // connection à la base
            con = txManager.getDataSource().getConnection();
            con.setAutoCommit(false);

            // requete qui récupère le code d'un échantillon
            String sql = "select CODE FROM ECHANTILLON " + "WHERE ECHANTILLON_ID = ?";
            pstmt = con.prepareStatement(sql);

            // requete qui récupère le code d'un dérivé
            sql = "select CODE FROM PROD_DERIVE " + "WHERE PROD_DERIVE_ID = ?";
            pstmt2 = con.prepareStatement(sql);

            // pour chaque emplacement
            for(int i = 0; i < emplacements.size(); i++){
               final Emplacement emp = emplacements.get(i);

               // s'il n'est pas vide et qu'on connait son contenu
               if(emp.getVide() != null && !emp.getVide() && emp.getEntite() != null && emp.getObjetId() != null){
                  // s'il contient un échantillon
                  if(emp.getEntite().getNom().equals("Echantillon")){

                     pstmt.setInt(1, emp.getObjetId());
                     res = pstmt.executeQuery();
                     while(res.next()){
                        results.add(res.getString(1));
                     }

                  }else if(emp.getEntite().getNom().equals("ProdDerive")){
                     // s'il contient un dérivé
                     pstmt2.setInt(1, emp.getObjetId());
                     res = pstmt2.executeQuery();
                     while(res.next()){
                        results.add(res.getString(1));
                     }

                  }
               }else{
                  results.add("");
               }
            }
         }catch(final SQLException e){
            log.error(e);
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(res != null){
               try{
                  res.close();
               }catch(final Exception e){
                  res = null;
               }
            }
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmt2 != null){
               try{
                  pstmt2.close();
               }catch(final Exception e){
                  pstmt2 = null;
               }
            }
         }
      }

      return results;
   }

   @Override
   public List<String> getTypesForEmplacementsManager(final List<Emplacement> emplacements){
      final List<String> results = new ArrayList<>();

      // on vérifie qu'il y a des emplacements
      if(emplacements != null){
         java.sql.Connection con = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmt2 = null;
         ResultSet res = null;
         try{
            // connection à la base
            con = txManager.getDataSource().getConnection();
            con.setAutoCommit(false);

            // requete qui récupère le type d'un échantillon
            String sql = "SELECT et.TYPE FROM ECHANTILLON_TYPE et " + "JOIN ECHANTILLON e ON e.ECHANTILLON_TYPE_ID "
               + "= et.ECHANTILLON_TYPE_ID " + "WHERE e.ECHANTILLON_ID = ?";
            pstmt = con.prepareStatement(sql);

            // requete qui récupère le type d'un dérivé
            sql = "SELECT pt.TYPE FROM PROD_TYPE pt " + "JOIN PROD_DERIVE p ON p.PROD_TYPE_ID " + "= pt.PROD_TYPE_ID "
               + "WHERE p.PROD_DERIVE_ID = ?";
            pstmt2 = con.prepareStatement(sql);

            // pour chaque emplacement
            for(int i = 0; i < emplacements.size(); i++){
               final Emplacement emp = emplacements.get(i);

               // s'il n'est pas vide et qu'on connait son contenu
               if(emp.getVide() != null && !emp.getVide() && emp.getEntite() != null && emp.getObjetId() != null){
                  // s'il contient un échantillon
                  if(emp.getEntite().getNom().equals("Echantillon")){

                     pstmt.setInt(1, emp.getObjetId());
                     res = pstmt.executeQuery();
                     boolean added = false;
                     while(res.next()){
                        added = true;
                        results.add(res.getString(1));
                     }

                     if(!added){
                        results.add("");
                     }

                  }else if(emp.getEntite().getNom().equals("ProdDerive")){
                     // s'il contient un dérivé
                     pstmt2.setInt(1, emp.getObjetId());
                     res = pstmt2.executeQuery();
                     boolean added = false;
                     while(res.next()){
                        added = true;
                        results.add(res.getString(1));
                     }
                     if(!added){
                        results.add("");
                     }

                  }
               }else{
                  results.add("");
               }
            }
         }catch(final SQLException e){
            log.error(e);
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(res != null){
               try{
                  res.close();
               }catch(final Exception e){
                  res = null;
               }
            }
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmt2 != null){
               try{
                  pstmt2.close();
               }catch(final Exception e){
                  pstmt2 = null;
               }
            }
         }
      }

      return results;
   }

   @Override
   public String getNumerotationByPositionAndTerminaleManager(final Integer position, final Terminale terminale){
      final StringBuffer sb = new StringBuffer();
      Integer numColonne = 0;
      Integer numLigne = 0;

      // on vérifie que les paramètres sont valides
      if(position != null && position > 0 && terminale != null && terminale.getTerminaleId() != null
         && position <= terminale.getTerminaleType().getNbPlaces()){

         // si la terminale est définie par un scheme
         if(terminale.getTerminaleType().getScheme() != null){

            Integer nbEmpOld = 0;
            Integer nbEmpAct = 0;
            Integer cptLigne = 1;
            // on récupère le nb d'emplacements par ligne
            final String[] values = terminale.getTerminaleType().getScheme().split(";");
            int i = 0;
            // on cherche le numéro de ligne et de colonne de
            // la position
            while(nbEmpAct < position && i < values.length){
               final int nbPlaces = Integer.parseInt(values[i]);
               nbEmpOld = nbEmpAct;
               nbEmpAct = nbEmpAct + nbPlaces;

               if(nbEmpAct >= position){
                  numLigne = cptLigne;
                  numColonne = position - nbEmpOld;
               }
               ++cptLigne;
               ++i;
            }
            // si la terminale est définie par une hauteur et une
            // longueur
         }else if(terminale.getTerminaleType().getHauteur() != null && terminale.getTerminaleType().getLongueur() != null){

            // si la position < longueur : la position est sur la 1ère
            // ligne
            if(position <= terminale.getTerminaleType().getLongueur()){
               numColonne = position;
               numLigne = 1;
            }else if(position < terminale.getTerminaleType().getNbPlaces()){
               // sinon on cherche le numéro de ligne et de colonne de
               // la position
               if(position % terminale.getTerminaleType().getLongueur() != 0){
                  numLigne = (position / terminale.getTerminaleType().getLongueur()) + 1;
                  numColonne = (position % terminale.getTerminaleType().getLongueur());
               }else{
                  numLigne = position / terminale.getTerminaleType().getLongueur();
                  numColonne = terminale.getTerminaleType().getLongueur();
               }
            }else if(position.equals(terminale.getTerminaleType().getNbPlaces())){
               // sinon on cherche le numéro de ligne et de colonne de
               // la position
               numLigne = terminale.getTerminaleType().getHauteur();
               numColonne = terminale.getTerminaleType().getLongueur();
            }

         }else{
            sb.append(position);
         }
      }

      // si on a trouvé un num de ligne et de colonne
      if(numLigne != 0 && numColonne != 0){
         if(terminale.getTerminaleNumerotation().getLigne().equals("POS")){
            sb.append(position);
         }else{
            // si la numérotation est numérique, on met le numéro
            if(terminale.getTerminaleNumerotation().getLigne().equals("NUM")){
               sb.append(numLigne);
            }else{
               // sinon on crée un code en fct du numéro
               sb.append(Utils.createListChars(numLigne, null, new ArrayList<String>()).get(numLigne - 1));
            }
            sb.append("-");
            // si la numérotation est numérique, on met le numéro
            if(terminale.getTerminaleNumerotation().getColonne().equals("NUM")){
               sb.append(numColonne);
            }else{
               // sinon on crée un code en fct du numéro
               sb.append(Utils.createListChars(numColonne, null, new ArrayList<String>()).get(numColonne - 1));
            }
         }
      }

      return sb.toString();
   }

   @Override
   public Integer getPositionByAdrl(final Terminale terminale, final String adrlPos){

      if(terminale != null && adrlPos != null){
         // pos like
         if(adrlPos.matches("\\d+") || terminale.getTerminaleNumerotation().getLigne().equals("POS")){
            if(Integer.valueOf(adrlPos) <= terminale.getTerminaleType().getNbPlaces()){
               return Integer.valueOf(adrlPos);
            }else{
               return 0;
            }
         }else{
            Integer numLigne;
            Integer numColonne;
            final String[] adrlPosLigneCol = adrlPos.split("-");

            // numLigne
            if(terminale.getTerminaleNumerotation().getLigne().equals("NUM")){
               numLigne = Integer.valueOf(adrlPosLigneCol[0]);
            }else{
               if(terminale.getTerminaleType().getScheme() == null){
                  numLigne = Utils.createListChars(terminale.getTerminaleType().getHauteur(), null, new ArrayList<String>())
                     .indexOf(adrlPosLigneCol[0]) + 1;
               }else{
                  numLigne = Utils
                     .createListChars(terminale.getTerminaleType().getScheme().split(";").length, null, new ArrayList<String>())
                     .indexOf(adrlPosLigneCol[0]) + 1;
               }
            }
            // numCol
            if(terminale.getTerminaleNumerotation().getColonne().equals("NUM")){
               numColonne = Integer.valueOf(adrlPosLigneCol[1]);
            }else{
               if(terminale.getTerminaleType().getScheme() == null){
                  numColonne = Utils.createListChars(terminale.getTerminaleType().getLongueur(), null, new ArrayList<String>())
                     .indexOf(adrlPosLigneCol[1]) + 1;
               }else{
                  numColonne =
                     Utils.createListChars(Integer.parseInt(terminale.getTerminaleType().getScheme().split(";")[numLigne]), null,
                        new ArrayList<String>()).indexOf(adrlPosLigneCol[1]) + 1;
               }
            }

            return getPositionByCoordonnees(terminale, numLigne, numColonne);
         }
      }
      return null;
   }

   @Override
   public Integer getPositionByCoordonnees(final Terminale terminale, final Integer numLigne, final Integer numColonne){
      Integer position = 0;

      if(terminale != null && terminale.getTerminaleId() != null && numLigne != null && numLigne > 0 && numColonne != null
         && numColonne > 0){

         if(terminale.getTerminaleType().getScheme() != null){
            final String[] lignes = terminale.getTerminaleType().getScheme().split(";");

            if(numLigne <= lignes.length){
               if(numColonne <= Integer.parseInt(lignes[numLigne - 1])){
                  for(int i = 0; i < numLigne - 1; i++){
                     position = position + Integer.parseInt(lignes[i]);
                  }
                  position = position + numColonne;
               }
            }
         }else{
            if(numLigne <= terminale.getTerminaleType().getHauteur() && numColonne <= terminale.getTerminaleType().getLongueur()){
               position = (numLigne - 1) * terminale.getTerminaleType().getHauteur();
               position = position + numColonne;
            }
         }
      }

      return position;
   }

   @Override
   public void createObjectManager(final Emplacement emplacement, final Terminale terminale, final Entite entite){

      //Terminale required
      if(terminale != null){
         emplacement.setTerminale(terminaleDao.mergeObject(terminale));
      }else{
         log.warn("Objet obligatoire Terminale manquant" + " lors de la création d'un Emplacement");
         throw new RequiredObjectIsNullException("Emplacement", "creation", "Terminale");
      }

      emplacement.setEntite(entiteDao.mergeObject(entite));

      // Test de la position
      if(!checkEmplacementInTerminale(emplacement)){
         log.warn("La position n'est pas dans la limite des places de " + "la terminale");
         throw new InvalidPositionException("Emplacement", "creation", emplacement.getPosition());
      }

      if(emplacement.getEntite() != null && emplacement.getObjetId() != null){
         // on vérifie que le couple Entité/ObjectId référence 
         // un objet existant
         if(entiteManager.findObjectByEntiteAndIdManager(emplacement.getEntite(), emplacement.getObjetId()) == null){
            log.warn("Couple Entite : " + emplacement.getEntite().toString() + " - ObjetId :" + emplacement.getObjetId()
               + " inexistant lors de la " + "création d'un objet Emplacement");
            throw new EntiteObjectIdNotExistException("Emplacement", entite.getNom(), emplacement.getObjetId());
         }
      }

      // Test s"il y a des doublons
      if(findDoublonManager(emplacement) != null){
         log.warn("Doublon lors de la creation de l'objet Emplacement : " + emplacement.toString());
         final EmplacementDoublonFoundException dbe = new EmplacementDoublonFoundException("Emplacement", "creation");
         dbe.setMessage("error.emplacement.doublon");
         dbe.setTerminale(emplacement.getTerminale());
         dbe.setPosition(emplacement.getPosition());
         throw dbe;
      }else{

         // validation du Contrat
         BeanValidator.validateObject(emplacement, new Validator[] {emplacementValidator});

         emplacement.setVide(emplacement.getObjetId() == null);

         emplacementDao.createObject(emplacement);

         log.info("Enregistrement de l'objet Emplacement : " + emplacement.toString());
      }

   }

   @Override
   public void updateObjectManager(final Emplacement emplacement, final Terminale terminale, final Entite entite){

      //Terminale required
      if(terminale != null){
         emplacement.setTerminale(terminaleDao.mergeObject(terminale));
      }else{
         log.warn("Objet obligatoire Terminale manquant" + " lors de la modification d'un Emplacement");
         throw new RequiredObjectIsNullException("Emplacement", "modification", "Terminale");
      }

      emplacement.setEntite(entiteDao.mergeObject(entite));

      // Test de la position
      if(!checkEmplacementInTerminale(emplacement)){
         log.warn("La position n'est pas dans la limite des places de " + "la terminale");
         throw new InvalidPositionException("Emplacement", "modification", emplacement.getPosition());
      }

      if(emplacement.getEntite() != null && emplacement.getObjetId() != null){
         // on vérifie que le couple Entité/ObjectId référence 
         // un objet existant
         if(entiteManager.findObjectByEntiteAndIdManager(emplacement.getEntite(), emplacement.getObjetId()) == null){
            log.warn("Couple Entite : " + emplacement.getEntite().toString() + " - ObjetId :" + emplacement.getObjetId()
               + " inexistant lors de la " + "modification d'un objet Emplacement");
            throw new EntiteObjectIdNotExistException("Emplacement", entite.getNom(), emplacement.getObjetId());
         }
      }

      // Test s"il y a des doublons
      if(findDoublonManager(emplacement) != null){
         log.warn("Doublon lors de la modification de l'objet " + "Emplacement : " + emplacement.toString());
         final EmplacementDoublonFoundException dbe = new EmplacementDoublonFoundException("Emplacement", "modification");
         dbe.setMessage("error.emplacement.doublon");
         dbe.setTerminale(emplacement.getTerminale());
         dbe.setPosition(emplacement.getPosition());
         throw dbe;
      }else{

         // validation du Contrat
         BeanValidator.validateObject(emplacement, new Validator[] {emplacementValidator});

         emplacement.setVide(emplacement.getObjetId() == null);

         emplacementDao.updateObject(emplacement);

         log.info("Modification de l'objet Emplacement : " + emplacement.toString());
      }

   }

   @Override
   public void removeObjectManager(final Emplacement emplacement){
      if(emplacement != null){
         if(isUsedObjectManager(emplacement)){
            log.warn("Objet utilisé lors de la suppression de l'objet " + "Emplacement : " + emplacement.toString());
            throw new ObjectUsedException("Emplacement", "suppression");
         }else{

            emplacementDao.removeObject(emplacement.getEmplacementId());
            log.info("Suppression de l'objet Emplacement : " + emplacement.toString());
         }
      }else{
         log.warn("Suppression d'un Emplacement null");
      }
   }

   @Override
   public List<Emplacement> createMultiObjetcsManager(final Terminale terminale, final Integer number){

      final List<Emplacement> emplacements = new ArrayList<>();

      //Terminale required
      if(terminale != null){
         if(number != null){
            if(number > terminale.getTerminaleType().getNbPlaces()){
               log.warn("La position n'est pas dans la limite " + "des places de " + "la terminale");
               throw new InvalidPositionException("Emplacement", "creation", number);
            }

            final String adrl = getTerminaleAdrlManager(terminale);

            for(int i = 0; i < number; i++){
               final Emplacement empl = new Emplacement();
               empl.setTerminale(terminale);
               empl.setPosition(i + 1);
               empl.setObjetId(null);
               empl.setEntite(null);
               empl.setVide(true);
               empl.setAdrp(null);

               final StringBuffer sb = new StringBuffer(adrl);
               sb.append(".");
               sb.append(empl.getPosition());
               empl.setAdrl(sb.toString());

               createObjectManager(empl, terminale, null);

               emplacements.add(empl);
            }
         }

      }else{
         log.warn("Objet obligatoire Terminale manquant" + " lors de la creation d'un Emplacement");
         throw new RequiredObjectIsNullException("Emplacement", "creation", "Terminale");
      }

      return emplacements;
   }

   @Override
   public void deplacerMultiEmplacementsManager(final List<Emplacement> emplacements, final Utilisateur utilisateur){
      if(emplacements != null){
         validateMultiEmplacementsManager(emplacements);

         final OperationType opType = operationTypeDao.findByNom("Deplacement").get(0);
         for(int i = 0; i < emplacements.size(); i++){
            final Emplacement emplacement = emplacements.get(i);

            if(emplacement != null && emplacement.getEmplacementId() != null){
               //					// Test de la position
               //					if (!checkEmplacementInTerminale(emplacement)) {
               //						log.warn("La position n'est pas dans la limite " 
               //								+ "des places de " 
               //								+ "la terminale");
               //						throw new InvalidPositionException(
               //								"Emplacement", "modification", 
               //								emplacement.getPosition());
               //					}
               //					
               //					// validation du Contrat
               //					BeanValidator.validateObject(
               //							emplacement, new Validator[]{emplacementValidator});

               emplacementDao.updateObject(emplacement);

               log.info("Modification de l'objet Emplacement : " + emplacement.toString());

               //Enregistrement de l'operation associee
               if(!emplacement.getVide() && emplacement.getObjetId() != null && emplacement.getEntite() != null){
                  final Object obj =
                     entiteManager.findObjectByEntiteAndIdManager(emplacement.getEntite(), emplacement.getObjetId());

                  if(obj != null){
                     final Operation op = new Operation();
                     op.setDate(Utils.getCurrentSystemCalendar());
                     operationManager.createObjectManager(op, utilisateur, opType, obj);
                  }
               }
            }
         }
      }
   }

   @Override
   public void validateMultiEmplacementsManager(final List<Emplacement> emplacements){
      if(emplacements != null){
         for(int i = 0; i < emplacements.size(); i++){
            final Emplacement empl = emplacements.get(i);

            if(empl != null){

               //Terminale required
               if(empl.getTerminale() == null){
                  log.warn("Objet obligatoire Terminale manquant" + " lors de la création d'un Emplacement");
                  throw new RequiredObjectIsNullException("Emplacement", "creation", "Terminale");
               }

               // Test de la position
               if(!checkEmplacementInTerminale(empl)){
                  log.warn("La position n'est pas dans la " + "limite des places de " + "la terminale");
                  throw new InvalidPositionException("Emplacement", "creation", empl.getPosition());
               }

               if(empl.getEntite() != null && empl.getObjetId() != null){
                  // on vérifie que le couple Entité/ObjectId référence 
                  // un objet existant
                  if(entiteManager.findObjectByEntiteAndIdManager(empl.getEntite(), empl.getObjetId()) == null){
                     log.warn("Couple Entite : " + empl.getEntite().toString() + " - ObjetId :" + empl.getObjetId()
                        + " inexistant lors de la " + "création d'un objet Emplacement");
                     throw new EntiteObjectIdNotExistException("Emplacement", empl.getEntite().getNom(), empl.getObjetId());
                  }
               }

               // Test s"il y a des doublons
               if(findDoublonManager(empl) != null){
                  final Emplacement emp = findDoublonManager(empl);
                  boolean skipErr = false;
                  if(empl.getEmplacementId() != null){
                     // verifie si la liste contient le doublon
                     // en cours de modification
                     for(final Emplacement empModif : emplacements){
                        if(emp.getEmplacementId().equals(empModif.getEmplacementId())
                           && (emp.getPosition() != empModif.getPosition())
                           || !emp.getTerminale().equals(empModif.getTerminale())){
                           skipErr = true;
                           break;
                        }
                     }
                  }
                  if(!skipErr){
                     log.warn("Doublon lors de la creation de " + "l'objet Emplacement : " + empl.toString());
                     final EmplacementDoublonFoundException dbe = new EmplacementDoublonFoundException("Emplacement", "creation");
                     dbe.setMessage("error.emplacement.doublon");
                     dbe.setTerminale(empl.getTerminale());
                     dbe.setPosition(empl.getPosition());
                     throw dbe;
                  }
               }else{
                  // validation du Contrat
                  BeanValidator.validateObject(empl, new Validator[] {emplacementValidator});
               }
            }
         }
      }
   }

   @Override
   public void saveMultiEmplacementsManager(final List<Emplacement> emplacements){
      if(emplacements != null){
         validateMultiEmplacementsManager(emplacements);

         for(int i = 0; i < emplacements.size(); i++){
            final Emplacement empl = emplacements.get(i);

            if(empl != null){
               final Terminale term = empl.getTerminale();
               empl.setTerminale(terminaleDao.mergeObject(term));

               final Entite ent = entiteDao.mergeObject(empl.getEntite());
               empl.setEntite(ent);

               empl.setVide(empl.getObjetId() == null);

               if(empl.getEmplacementId() != null){
                  emplacementDao.updateObject(empl);
               }else{
                  emplacementDao.createObject(empl);
               }
            }
         }
      }
   }

   @Override
   public List<TKStockableObject> findObjByEmplacementManager(final Emplacement empl, String entiteNom){
      final List<TKStockableObject> objs = new ArrayList<>();

      // @since v2.1 to handle null entiteNom
      if(entiteNom == null){
         if(empl != null && empl.getEntite() != null){
            entiteNom = empl.getEntite().getNom();
         }
      }

      if(entiteNom != null){
         if(entiteNom.equals("Echantillon")){
            objs.addAll(echantillonDao.findByEmplacement(empl.getTerminale(), empl.getPosition()));
         }else if(entiteNom.equals("ProdDerive")){
            objs.addAll(prodDeriveDao.findByEmplacement(empl.getTerminale(), empl.getPosition()));
         }
      }

      return objs;
   }

}
