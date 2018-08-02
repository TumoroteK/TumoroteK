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
package fr.aphp.tumorotek.manager.test.io.export.standard;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ExportTVGSOManagerImpl;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class TVGSOProcedureTest extends AbstractManagerTest
{

   /** Bean Manager. */
   private ExportTVGSOManagerImpl exportTVGSOManager;
   private EchantillonDao echantillonDao;
   private UtilisateurDao utilisateurDao;
   private OperationTypeDao operationTypeDao;
   private CatalogueDao catalogueDao;

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setExportTVGSOManager(final ExportTVGSOManagerImpl etm){
      this.exportTVGSOManager = etm;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setCatalogueDao(final CatalogueDao cDao){
      this.catalogueDao = cDao;
   }

   public void testTvgsoExport() throws SQLException{
      final Echantillon e = echantillonDao.findById(2);
      final List<Echantillon> echans = new ArrayList<>();
      echans.add(e);
      final Utilisateur u = utilisateurDao.findById(1);
      final Catalogue tvgso = catalogueDao.findById(3);
      exportTVGSOManager.exportEchansCatalogueManager(echans, u, tvgso);

      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(e, operationTypeDao.findByNom("Export TVGSO").get(0))
         .size() == 1);

      getOperationManager().removeObjectManager(
         getOperationManager().findByObjetIdEntiteAndOpeTypeManager(e, operationTypeDao.findByNom("%TVGSO").get(0)).get(0));

      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);

   }

}
