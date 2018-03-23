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
package fr.aphp.tumorotek.action.imports;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ImportHistoriqueRowRenderer implements RowRenderer<ImportHistorique>
{

   @Override
   public void render(final Row row, final ImportHistorique data, final int index) throws Exception{
      final ImportHistorique historique = data;

      // date
      final Label dateLabel = new Label(ObjectTypesFormatters.dateRenderer2(historique.getDate()));
      dateLabel.setParent(row);

      // utilisateur
      final Label utilisateurLabel = new Label(historique.getUtilisateur().getLogin());
      utilisateurLabel.setParent(row);

      // Nb patients
      final Entite ePatient = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      Integer nb =
         ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(historique, ePatient).size();
      final Label nbPatientsLabel = new Label(String.valueOf(nb));
      if(nb > 0){
         nbPatientsLabel.setSclass("formLink");
         nbPatientsLabel.addForward(null, nbPatientsLabel.getParent(), "onClickNbPatients", historique);
      }
      nbPatientsLabel.setParent(row);

      // Nb Prélèvements
      final Entite ePrlvt = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      nb = ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(historique, ePrlvt).size();
      final Label nbPrlvtLabel = new Label(String.valueOf(nb));
      if(nb > 0){
         nbPrlvtLabel.setSclass("formLink");
         nbPrlvtLabel.addForward(null, nbPrlvtLabel.getParent(), "onClickNbPrelevements", historique);
      }
      nbPrlvtLabel.setParent(row);

      // Nb echans
      final Entite eEchan = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      nb = ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(historique, eEchan).size();
      final Label nbEchansLabel = new Label(String.valueOf(nb));
      if(nb > 0){
         nbEchansLabel.setSclass("formLink");
         nbEchansLabel.addForward(null, nbEchansLabel.getParent(), "onClickNbEchantillons", historique);
      }
      nbEchansLabel.setParent(row);

      // Nb dérivés
      final Entite eDerive = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      nb = ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(historique, eDerive).size();
      final Label nbDerivesLabel = new Label(String.valueOf(nb));
      if(nb > 0){
         nbDerivesLabel.setSclass("formLink");
         nbDerivesLabel.addForward(null, nbDerivesLabel.getParent(), "onClickNbDerives", historique);
      }
      nbDerivesLabel.setParent(row);
   }

}
