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
package fr.aphp.tumorotek.dao.test;

import fr.aphp.tumorotek.model.utils.Utils;

public class UtilsTest extends AbstractDaoTest
{

   public UtilsTest(){

   }

   public void testFloor(){
      assertTrue(Utils.floor((float) 15.45545, 2) == (float) 15.46);
      assertTrue(Utils.floor((float) 15.45645, 1) == (float) 15.5);
      assertTrue(Utils.floor((float) 15.45545, 0) == 15);
      assertTrue(Utils.floor((float) 15, 2) == 15);
      assertTrue(Utils.floor((float) 9.999, 2) == 10);
      assertNull(Utils.floor(null, 2));
   }

   public void testTruncateIfNecessary_shouldNotTruncateBecauseShorter() {
      final String VALEUR_A_TRAITER = "Test";
      final int TAILLE_MAX = 10;
      final String VALEUR_ATTENDUE = VALEUR_A_TRAITER;
      assertTrue(Utils.truncateIfNecessary(VALEUR_A_TRAITER, TAILLE_MAX).equals(VALEUR_ATTENDUE));
   }
   
   public void testTruncateIfNecessary_shouldNotTruncateBecauseLimit() {
      final String VALEUR_A_TRAITER = "TestLimiteMax";
      final int TAILLE_MAX = 13;
      final String VALEUR_ATTENDUE = VALEUR_A_TRAITER;
      assertTrue(Utils.truncateIfNecessary(VALEUR_A_TRAITER, TAILLE_MAX).equals(VALEUR_ATTENDUE));
   }
   
   public void testTruncateIfNecessary_shouldTruncate() {
      final String VALEUR_A_TRAITER = "TestTropLong";
      final int TAILLE_MAX = 10;
      final String VALEUR_ATTENDUE = "TestTropLo";
      assertTrue(Utils.truncateIfNecessary(VALEUR_A_TRAITER, TAILLE_MAX).equals(VALEUR_ATTENDUE));
   }
   

}
