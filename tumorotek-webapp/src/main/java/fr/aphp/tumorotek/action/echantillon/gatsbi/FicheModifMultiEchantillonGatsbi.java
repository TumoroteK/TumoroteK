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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Constraint;

import fr.aphp.tumorotek.action.echantillon.FicheModifMultiEchantillon;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 *
 * Controller gérant la fiche de modification multiple des échantillons sous le
 * gestionnaire GATSBI.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FicheModifMultiEchantillonGatsbi extends FicheModifMultiEchantillon
{

   private static final long serialVersionUID = 1L;

   private Contexte c;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      c = GatsbiController.initWireAndDisplay(this, 3, false, null, null, null);
   }

   @Override
   protected List<Object> applyAnyThesaurusRestriction(final List<Object> thObjs, final Integer chpId) throws TKException{
      return GatsbiController.filterExistingListModel(c, thObjs, chpId);
   }

   @Override
   protected Constraint muteAnyRequiredConstraint(final Constraint cstr, final Integer chpId){
      return GatsbiController.muteConstraintFromContexte(cstr, c.isChampIdRequired(chpId));
   }

   @Override
   protected boolean switchAnyRequiredFlag(final Boolean flag, final Integer chpId){
      return c.isChampIdRequired(chpId);
   }
}