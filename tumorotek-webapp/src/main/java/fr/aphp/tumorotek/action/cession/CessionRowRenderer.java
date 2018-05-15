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
package fr.aphp.tumorotek.action.cession;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.cession.Cession;

/**
 * ProdDeriveRenderer affiche dans le Row
 * les membres de ProdDerive sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/04/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class CessionRowRenderer extends TKSelectObjectRenderer
{

   private boolean accessible = true;

   public CessionRowRenderer(final boolean select, final boolean cols){
      setSelectionMode(select);
      setTtesCollections(cols);
   }

   @Override
   public void render(final Row row, final Object data, final int index){

      // dessine le checkbox
      super.render(row, data, index);

      final Cession cession = (Cession) data;

      // @since 2.1
      // barcodes validated cession
      final Div scannedDiv = new Div();
      scannedDiv.setHeight("18px");
      scannedDiv.setWidth("18px");
      if(cession.getLastScanCheckDate() != null){
         scannedDiv.setStyle("color: #5CB85C");
         scannedDiv.setSclass("fa fa-barcode fa-2x");
         scannedDiv.setTooltiptext(ObjectTypesFormatters.dateRenderer2(cession.getLastScanCheckDate()));
      }
      scannedDiv.setParent(row);

      // numéro
      final Label numeroLabel = new Label(String.valueOf(cession.getNumero()));
      if(accessible){
         numeroLabel.addForward(null, numeroLabel.getParent(), "onClickObject", cession);
         numeroLabel.setClass("formLink");
      }
      numeroLabel.setParent(row);

      // type cession
      new Label(Labels.getLabel("cession.type." + cession.getCessionType().getType().toLowerCase())).setParent(row);

      // banque
      // if (isTtesCollections()) {
      new Label(cession.getBanque().getNom()).setParent(row);
      // } else {
      //	new Label().setParent(row);
      //}

      // nb echantillons
      drawNbEchantillons(cession, row);

      // nb derives
      drawNbProdDerives(cession, row);

      // date de demande
      new Label(ObjectTypesFormatters.dateRenderer2(cession.getDemandeDate())).setParent(row);

      // date de cession
      String dateCession = "-";
      if(cession.getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
         dateCession = ObjectTypesFormatters.dateRenderer2(cession.getDestructionDate());
      }else{
         dateCession = ObjectTypesFormatters.dateRenderer2(cession.getDepartDate());
      }
      new Label(dateCession).setParent(row);

      // demandeur
      if(cession.getDemandeur() != null){
         new Label(cession.getDemandeur().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // Etude
      new Label(getEtude(cession)).setParent(row);

      // cession statut
      if(cession.getCessionStatut() != null){
         new Label(ObjectTypesFormatters.ILNObjectStatut(cession.getCessionStatut())).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // état
      new Label(getEtat(cession)).setParent(row);

      // contrat
      if(cession.getContrat() != null){
         new Label(cession.getContrat().getNumero()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // exécutant
      if(cession.getExecutant() != null){
         new Label(cession.getExecutant().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   /**
    * Récupère la date de création système de la cession.
    * @return Date de création.
    */
   public String getDateCreation(final Cession cession){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(cession);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }
      return null;
   }

   public void drawNbEchantillons(final Cession cession, final Row row){

      final Long echansCount = ManagerLocator.getCederObjetManager().findObjectsCessedCountManager(cession,
         ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));

      if(echansCount > 0){
         final Label label1 = new Label(String.valueOf(echansCount));
         // dessine le label avec un lien vers popup 
         final Hbox labelAndLinkBox = new Hbox();
         labelAndLinkBox.setSpacing("5px");
         labelAndLinkBox.appendChild(label1);
         if(echansCount < 50){
            final List<String> codes = ManagerLocator.getCederObjetManager().findCodesByCessionEntiteManager(cession,
               ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup popUp = new Popup();
            popUp.setParent(row.getParent().getParent().getParent());
            final Iterator<String> it = codes.iterator();
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               libelleStaticLabel = new Label(it.next());
               libelleStaticLabel.setSclass("formValue");
               popupVbox.appendChild(libelleStaticLabel);
            }
            popUp.appendChild(popupVbox);
            moreLabel.setTooltip(popUp);
            labelAndLinkBox.appendChild(moreLabel);
         }
         labelAndLinkBox.setParent(row);
      }else{
         new Label("0").setParent(row);
      }
   }

   public void drawNbProdDerives(final Cession cession, final Row row){

      final Long prodsCount = ManagerLocator.getCederObjetManager().findObjectsCessedCountManager(cession,
         ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));

      if(prodsCount > 0){
         final Label label1 = new Label(String.valueOf(prodsCount));
         // dessine le label avec un lien vers popup 
         final Hbox labelAndLinkBox = new Hbox();
         labelAndLinkBox.setSpacing("5px");
         labelAndLinkBox.appendChild(label1);
         if(prodsCount < 50){
            final List<String> codes = ManagerLocator.getCederObjetManager().findCodesByCessionEntiteManager(cession,
               ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup popUp = new Popup();
            popUp.setParent(row.getParent().getParent().getParent());
            final Iterator<String> it = codes.iterator();
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               libelleStaticLabel = new Label(it.next());
               libelleStaticLabel.setSclass("formValue");
               popupVbox.appendChild(libelleStaticLabel);
            }
            popUp.appendChild(popupVbox);
            moreLabel.setTooltip(popUp);
            labelAndLinkBox.appendChild(moreLabel);
         }
         labelAndLinkBox.setParent(row);
      }else{
         new Label("0").setParent(row);
      }
   }

   /**
    * Renvoie le nombre de dérivés dans la cession.
    */
   public static Integer getNbDerives(final Cession cession){

      return ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager(cession).size();

   }

   public String getEtat(final Cession cession){
      if(cession.getEtatIncomplet() != null){
         if(cession.getEtatIncomplet()){
            return Labels.getLabel("cession.statut.incomplete");
         }
         return Labels.getLabel("cession.statut.complete");
      }
      return null;
   }

   public String getEtude(final Cession cession){
      if(cession.getCessionType() != null){
         if(cession.getCessionType().getType().toUpperCase().equals("RECHERCHE")){
            return cession.getEtudeTitre();
         }else if(cession.getCessionType().getType().toUpperCase().equals("SANITAIRE")){
            if(cession.getCessionExamen() != null){
               return cession.getCessionExamen().getExamen();
            }
            return null;
         }else if(cession.getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
            if(cession.getDestructionMotif() != null){
               return cession.getDestructionMotif().getMotif();
            }
            return null;
         }
         return null;
      }
      return null;
   }

   public boolean isAccessible(){
      return accessible;
   }

   public void setAccessible(final boolean a){
      this.accessible = a;
   }
}
