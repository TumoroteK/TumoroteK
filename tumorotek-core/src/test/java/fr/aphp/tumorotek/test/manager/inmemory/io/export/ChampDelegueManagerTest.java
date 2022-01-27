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
package fr.aphp.tumorotek.test.manager.inmemory.io.export;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 * @author Gille Chapelot
 *
 */
public class ChampDelegueManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ChampDelegueManager champDelegueManager;

   @Autowired
   private EntiteManager entiteManager;

   @Autowired
   private PrelevementManager prelevementManager;

   /**
    * Test method for {@link fr.aphp.tumorotek.manager.impl.io.ChampDelegueManagerImpl#findByEntiteAndContexte(fr.aphp.tumorotek.model.systeme.Entite, fr.aphp.tumorotek.model.contexte.EContexte)}.
    */
   @Test
   public void testFindByEntiteAndContexte(){

      final Entite entite = entiteManager.findByNomManager("Prelevement").get(0);

      final Predicate<ChampDelegue> isValide =
         cd -> entite.equals(cd.getEntite()) && EContexte.SEROLOGIE.equals(cd.getContexte());

      final List<ChampDelegue> res = champDelegueManager.findByEntiteAndContexte(entite, EContexte.SEROLOGIE);

      assertTrue(res.stream().allMatch(isValide));

   }

   /**
    * Test method for {@link fr.aphp.tumorotek.manager.impl.io.ChampDelegueManagerImpl#getValueForEntite(fr.aphp.tumorotek.model.io.export.ChampDelegue, fr.aphp.tumorotek.model.TKDelegetableObject)}.
    */
   @Test
   public void testGetValueForEntite(){

      final Entite entite = entiteManager.findByNomManager("Prelevement").get(0);
      final Prelevement prelevement = prelevementManager.findByIdManager(2);

      final ChampDelegue champDelegue =
         champDelegueManager.findByNomAndEntiteAndContexte("Protocoles", entite, EContexte.SEROLOGIE).get(0);

      final Set<Protocole> valRef = ((PrelevementSero) prelevement.getDelegate()).getProtocoles();

      final Object valReelle = champDelegueManager.getValueForEntite(champDelegue, prelevement);

      assertThat(valReelle, is(valRef));

   }

   @Test
   public void testGetValueForEntiteAttributInexistant(){

      final Entite entite = entiteManager.findByNomManager("Prelevement").get(0);
      final Prelevement prelevement = prelevementManager.findByIdManager(2);

      ChampDelegue champDelegueInexistant = new ChampDelegue();
      champDelegueInexistant.setNom("ChampInconnu");
      champDelegueInexistant.setContexte(EContexte.SEROLOGIE);
      champDelegueInexistant.setEntite(entite);
      
      Object valeur = champDelegueManager.getValueForEntite(champDelegueInexistant, prelevement);

      assertNull(valeur);
      
   }
   
   /**
    * Test method for {@link fr.aphp.tumorotek.manager.impl.io.ChampDelegueManagerImpl#findByNomAndEntiteAndContexte(java.lang.String, fr.aphp.tumorotek.model.systeme.Entite, fr.aphp.tumorotek.model.contexte.EContexte)}.
    */
   @Test
   public void testFindByNomAndEntiteAndContexte(){

      final Entite entite = entiteManager.findByNomManager("Maladie").get(0);
      final String nomChamp = "Diagnostic";

      final Predicate<ChampDelegue> isValide =
         cd -> nomChamp.equals(cd.getNom()) && entite.equals(cd.getEntite()) && EContexte.SEROLOGIE.equals(cd.getContexte());

      final List<ChampDelegue> res = champDelegueManager.findByNomAndEntiteAndContexte(nomChamp, entite, EContexte.SEROLOGIE);

      assertTrue(res.stream().allMatch(isValide));

   }

}
