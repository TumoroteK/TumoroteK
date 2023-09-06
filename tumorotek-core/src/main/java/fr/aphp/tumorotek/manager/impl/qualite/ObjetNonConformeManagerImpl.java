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
package fr.aphp.tumorotek.manager.impl.qualite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ObjetNonConformeManagerImpl implements ObjetNonConformeManager
{

   private final Logger log = LoggerFactory.getLogger(ObjetNonConformeManager.class);

   private ObjetNonConformeDao objetNonConformeDao;

   private NonConformiteDao nonConformiteDao;

   private ConformiteTypeDao conformiteTypeDao;

   private EntiteDao entiteDao;

   public void setObjetNonConformeDao(final ObjetNonConformeDao oDao){
      this.objetNonConformeDao = oDao;
   }

   public void setNonConformiteDao(final NonConformiteDao nDao){
      this.nonConformiteDao = nDao;
   }

   public void setConformiteTypeDao(final ConformiteTypeDao cDao){
      this.conformiteTypeDao = cDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Override
   public ObjetNonConforme findByIdManager(final Integer objetNonConformeId){
      return objetNonConformeDao.findById(objetNonConformeId);
   }

   @Override
   public List<ObjetNonConforme> findAllObjectsManager(){
      log.debug("Recherche de tous les ObjetNonConformes");
      return objetNonConformeDao.findAll();
   }

   @Override
   public List<ObjetNonConforme> findByObjetManager(final Object obj){
      log.debug("Recherche de tous les ObjetNonConformes d'un  objet");
      if(obj != null){
         Entite entite = null;
         Integer id = null;
         if(obj instanceof Prelevement){
            entite = entiteDao.findByNom("Prelevement").get(0);
            id = ((Prelevement) obj).getPrelevementId();
         }else if(obj instanceof Echantillon){
            entite = entiteDao.findByNom("Echantillon").get(0);
            id = ((Echantillon) obj).getEchantillonId();
         }else if(obj instanceof ProdDerive){
            entite = entiteDao.findByNom("ProdDerive").get(0);
            id = ((ProdDerive) obj).getProdDeriveId();
         }
         return objetNonConformeDao.findByObjetAndEntite(id, entite);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<ObjetNonConforme> findByObjetAndTypeManager(final Object obj, final Object type){
      log.debug("Recherche de tous les ObjetNonConformes d'un  objet en fonction du type de conformite");
      if(obj != null && type != null){
         Entite entite = null;
         Integer id = null;
         if(obj instanceof Prelevement){
            entite = entiteDao.findByNom("Prelevement").get(0);
            id = ((Prelevement) obj).getPrelevementId();
         }else if(obj instanceof Echantillon){
            entite = entiteDao.findByNom("Echantillon").get(0);
            id = ((Echantillon) obj).getEchantillonId();
         }else if(obj instanceof ProdDerive){
            entite = entiteDao.findByNom("ProdDerive").get(0);
            id = ((ProdDerive) obj).getProdDeriveId();
         }

         ConformiteType ct = null;
         if(type instanceof String){
            final List<ConformiteType> list = conformiteTypeDao.findByEntiteAndType((String) type, entite);
            if(list.size() == 1){
               ct = list.get(0);
            }
         }else if(type instanceof ConformiteType){
            ct = (ConformiteType) type;
         }

         return objetNonConformeDao.findByObjetEntiteAndType(id, entite, ct);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public void createUpdateOrRemoveObjectManager(final Object obj, final NonConformite nonConformite, final String type){
      if(obj != null && type != null){
         // on va tester si une non conformité est déjà
         // définie pour cet objet
         // List<ObjetNonConforme> list = findByObjetAndTypeManager(obj,
         //		type);

         // si la nonConformite est null, on cherchera les obj
         // non conformes et on les supprimera
         if(nonConformite != null){
            // s'il n'y en avait pas : création
            // if (list.size() == 0) {
            final ObjetNonConforme newObj = new ObjetNonConforme();

            // on récup l'entité et l'id
            Entite entite = null;
            Integer id = null;
            if(obj instanceof Prelevement){
               entite = entiteDao.findByNom("Prelevement").get(0);
               id = ((Prelevement) obj).getPrelevementId();
            }else if(obj instanceof Echantillon){
               entite = entiteDao.findByNom("Echantillon").get(0);
               id = ((Echantillon) obj).getEchantillonId();
            }else if(obj instanceof ProdDerive){
               entite = entiteDao.findByNom("ProdDerive").get(0);
               id = ((ProdDerive) obj).getProdDeriveId();
            }

            // si entite null
            if(entite == null){
               throw new RequiredObjectIsNullException("ObjetNonConforme", "creation", "Entite");
            }else{
               newObj.setEntite(entiteDao.mergeObject(entite));
            }
            // si objetid null
            if(id == null){
               throw new RequiredObjectIsNullException("ObjetNonConforme", "creation", "ObjetId");
            }else{
               newObj.setObjetId(id);
            }

            // validation entite non conformite type
            if(!nonConformite.getConformiteType().getEntite().equals(entite)){
               throwConformiteTypeRuntimeException();
            }

            newObj.setNonConformite(nonConformiteDao.mergeObject(nonConformite));

            // création
            objetNonConformeDao.createObject(newObj);
            log.info("Enregistrement de l'objet ObjetNonConforme : {}",  newObj);
            //				}
            //				else {
            //					// sinon, on va mettre à jour l'objet non conforme
            //					// (si ça non conformité a changer
            //					ObjetNonConforme upObj = list.get(0);
            //
            //					// si la non conf a changée => update
            //					if (!nonConformite.equals(upObj.getNonConformite())) {
            //						upObj.setNonConformite(nonConformiteDao.mergeObject(
            //								nonConformite));
            //
            //						// update
            //						objetNonConformeDao.updateObject(upObj);
            //						log.info("Modification de l'objet ObjetNonConforme : "
            //								+ upObj.toString());
            //					}
            //				}
            //			} else {
            //				// suppression de tous les objs non conformes
            //				for (int i = 0; i < list.size(); i++) {
            //					removeObjectManager(list.get(i));
            //				}
         }
      }
   }

   @Override
   public void prepareListJDBCManager(final EchantillonJdbcSuite jdbcSuite, final TKAnnotableObject tkObj,
      final List<NonConformite> nonConformites) throws SQLException{

      if(tkObj != null && nonConformites != null){
         if(!nonConformites.isEmpty()){

            // Integer maxNcId;
            // Statement stmt = null;
            // PreparedStatement pstmtNc = null;
            // ResultSet rs2 = null;
            final Integer maxNcId = jdbcSuite.getMaxObjetNonConformeId();

            try{
               // stmt = DataSourceUtils.getConnection(dataSource)
               //		.createStatement();
               // rs2 = stmt.executeQuery("select max(objet_non_conforme_id)"
               //		+ " from OBJET_NON_CONFORME");
               // rs2.first();
               // maxNcId = rs2.getInt(1);

               //					String sql = "insert into OBJET_NON_CONFORME (OBJET_NON_CONFORME_ID, "
               //							+ "OBJET_ID, ENTITE_ID, NON_CONFORMITE_ID) "
               //							+ "values (?,?,?,?)";
               //
               //					pstmtNc = DataSourceUtils.getConnection(dataSource)
               //							.prepareStatement(sql);

               final Integer objId = tkObj.listableObjectId();
               // si objetid null
               if(objId == null){
                  throw new RequiredObjectIsNullException("ObjetNonConforme", "creation", "ObjetId");
               }

               Integer entiteId;
               // si entite null
               final Entite entite = entiteDao.findByNom(tkObj.entiteNom()).get(0);

               if(entite == null){
                  throw new RequiredObjectIsNullException("ObjetNonConforme", "creation", "Entite");
               }else{
                  entiteId = entite.getEntiteId();
               }

               // création des nouvelles non conformités
               // HashSet exclue doublons
               for(final NonConformite nc : new LinkedHashSet<>(nonConformites)){
                  // validation entite non conformite type
                  if(!nc.getConformiteType().getEntite().equals(entite)){
                     throwConformiteTypeRuntimeException();
                  }
                  jdbcSuite.incrementMaxObjetNonConformeId();
                  jdbcSuite.getPstmtNc().clearParameters();
                  jdbcSuite.getPstmtNc().setInt(1, jdbcSuite.getMaxObjetNonConformeId());
                  jdbcSuite.getPstmtNc().setInt(2, objId);
                  jdbcSuite.getPstmtNc().setInt(3, entiteId);
                  jdbcSuite.getPstmtNc().setInt(4, nc.getNonConformiteId());
                  jdbcSuite.getPstmtNc().addBatch();
               }
               // pstmtNc.executeBatch();
            }catch(final Exception e){
               // log.error(e.getMessage(), e);
               // rollback create operation
               jdbcSuite.setMaxObjetNonConformeId(maxNcId);
               throw e;
            }finally{
               //					if (stmt != null) {
               //						try { stmt.close();
               //						} catch (Exception e) { stmt = null; }
               //					}
               //					if (pstmtNc != null) {
               //						try { pstmtNc.close();
               //						} catch (Exception e) { pstmtNc = null; }
               //					}
               //					if (rs2 != null) {
               //						try { rs2.close();
               //						} catch (Exception e) { rs2 = null; }
               //					}
            }
         }
      }
   }

   private void throwConformiteTypeRuntimeException(){
      throw new RuntimeException("conformiteType.entite.illegal");
   }

   @Override
   public void createUpdateOrRemoveListObjectManager(final Object obj, final List<NonConformite> nonConformites,
      final String type){
      if(obj != null && type != null && nonConformites != null){
         // on va tester si des non conformités sont déjà
         // définies pour cet objet
         final List<ObjetNonConforme> list = findByObjetAndTypeManager(obj, type);

         // si la nonConformite est null, on cherchera les obj
         // non conformes et on les supprimera
         if(!nonConformites.isEmpty()){
            // clone sans doublons
            final Set<NonConformite> ncfs = new LinkedHashSet<>(nonConformites);
            // s'il y avait des non conformités, on cherche celles
            // à conserver, celles à supprimer
            if(list.size() > 0){
               // suppression
               for(int i = 0; i < list.size(); i++){
                  if(!ncfs.contains(list.get(i).getNonConformite())){
                     removeObjectManager(list.get(i));
                  }else{
                     ncfs.remove(list.get(i).getNonConformite());
                  }

               }
            }
            // création des nouvelles non conformités
            for(final NonConformite nc : ncfs){
               createUpdateOrRemoveObjectManager(obj, nc, type);
            }
         }else{
            // suppression de tous les objs non conformes
            for(int i = 0; i < list.size(); i++){
               removeObjectManager(list.get(i));
            }
         }
      }
   }

   @Override
   public void removeObjectManager(final ObjetNonConforme objetNonConforme){
      if(objetNonConforme != null){
         objetNonConformeDao.removeObject(objetNonConforme.getObjetNonConformeId());
         log.info("Suppression de l'objet ObjetNonConforme : {}",  objetNonConforme);
      }
   }

   @Override
   public List<Integer> findObjetIdsByNonConformitesManager(final List<NonConformite> nocfs){
      final List<Integer> ids = new ArrayList<>();

      if(nocfs != null && !nocfs.isEmpty()){
         ids.addAll(objetNonConformeDao.findObjetIdsByNonConformites(nocfs));
      }

      return ids;
   }
}
