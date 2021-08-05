/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.test.contexte.gatsbi;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.context.gatsbi.EtudeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;

/**
 *
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.3.0-gatsbi
 *
 */
public class EtudeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EtudeManager etudeManager;
   
   @Autowired
   private PlateformeDao plateformeDao;


   public EtudeManagerTest(){}


   @Test
   public void testFindByPfOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<Etude> list = etudeManager.findByPfOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getAcronyme().equals("AEP"));
      assertTrue(list.get(1).getAcronyme().equals("BEP"));
      // assertTrue(list.get(2).getAcronyme().equals("CEP")); archived
      pf = plateformeDao.findById(2);
      list = etudeManager.findByPfOrderManager(pf);
      assertTrue(list.isEmpty());
      list = etudeManager.findByPfOrderManager(null);
      assertTrue(list.isEmpty());
   }
}
