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
package fr.aphp.tumorotek.dao.test.io.export;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.io.export.ChampDelegueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.AbstractInMemoryTests;

/**
 * @author Gille Chapelot
 *
 */
@Transactional
public class ChampDelegueDaoTest extends AbstractInMemoryTests
{

   @Autowired
   private ChampDelegueDao champDelegueDao;

   @Autowired
   private EntiteDao entiteDao;

   @Test
   public void testFindByEntiteAndContexte(){

      final Entite entite = entiteDao.findByNom("Prelevement").get(0);

      final Predicate<ChampDelegue> predicate =
         cd -> entite.equals(cd.getEntite()) && EContexte.SEROLOGIE.equals(cd.getContexte());

      final List<ChampDelegue> champsDelegues = champDelegueDao.findByEntiteAndContexte(entite, EContexte.SEROLOGIE);

      assertTrue(champsDelegues.stream().allMatch(predicate));

   }

   @Test
   public void testFindByNomAndEntiteAndContexte(){

      final String nomChamp = "Diagnostic";
      final Entite entite = entiteDao.findByNom("Maladie").get(0);

      final Predicate<ChampDelegue> predicate =
         cd -> nomChamp.equals(cd.getNom()) && entite.equals(cd.getEntite()) && EContexte.SEROLOGIE.equals(cd.getContexte());

      final List<ChampDelegue> champsDelegues =
         champDelegueDao.findByNomAndEntiteAndContexte(nomChamp, entite, EContexte.SEROLOGIE);

      assertTrue(champsDelegues.stream().allMatch(predicate));

   }

}
