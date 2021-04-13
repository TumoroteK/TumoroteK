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
package fr.aphp.tumorotek.action.echantillon;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.action.utils.TKStockableObjectUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;

/**
 * EchantillonRenderer affiche dans le Row
 * les membres d'Echantillon sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/04/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class EchantillonRowRenderer extends TKSelectObjectRenderer<Echantillon>
{

   private final Log log = LogFactory.getLog(EchantillonRowRenderer.class);

   private boolean accessible = true;
   private boolean isEmbedded = false;

   private boolean accessStockage = true;

   public void setAccessStockage(final boolean ac){
      this.accessStockage = ac;
   }

   public EchantillonRowRenderer(final boolean select, final boolean cols){
      setSelectionMode(select);
      setTtesCollections(cols);
   }

   @Override
   public void render(final Row row, final Echantillon data, final int index){

      log.debug("init render");

      // dessine le checkbox
      drawCheckbox(row, data, index);

      log.debug("super");

      final Echantillon echan = data;

      final Hlayout icones = TKStockableObjectUtils.drawListIcones(echan, null, null);

      //infectieux
      final Prelevement prel = ManagerLocator.getEchantillonManager().getPrelevementManager(echan);
      if(prel != null){
         final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(prel).iterator();
         final Div bioHzd = new Div();
         boolean risky = false;
         String risks = "";
         Risque risque;
         while(risksIt.hasNext()){
            risque = risksIt.next();
            if(risque.getInfectieux()){
               risky = true;
               risks = risks + " " + risque.getNom();
            }
         }
         if(risky){
            bioHzd.setWidth("18px");
            bioHzd.setHeight("18px");
            bioHzd.setSclass("biohazard");
            bioHzd.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.risque", new String[] {risks}));
         }

         icones.insertBefore(bioHzd, icones.getFirstChild());
      }
      log.debug("risque");

      icones.setParent(row);

      // code
      final Label codeLabel = new Label(echan.getCode());
      if(accessible){
         codeLabel.addForward(null, codeLabel.getParent(), "onClickObject", echan);
         codeLabel.setClass("formLink");
      }
      codeLabel.setParent(row);
      log.debug("code");

      if(isTtesCollections()){
         new Label(echan.getBanque().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // patient
      if(!isEmbedded){
         if(!anonyme){
            new Label(getPatient(echan)).setParent(row);
         }else{
            createAnonymeBlock().setParent(row);
         }
      }

      // date de stockage
      new Label(ObjectTypesFormatters.dateRenderer2(echan.getDateStock())).setParent(row);
      log.debug("date");

      // codes organes : liste des codes exportés pour échantillons
      ObjectTypesFormatters.drawCodesExpLabel(ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echan),
         row, null, false);
      log.debug("codes organes");

      // codes lésionnels : liste des codes exportés pour échantillons
      ObjectTypesFormatters.drawCodesExpLabel(ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echan),
         row, null, false);
      log.debug("codes lesionnels");

      // type
      if(echan.getEchantillonType() != null){
         new Label(echan.getEchantillonType().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }
      log.debug("type");

      // quantité
      new Label(getQuantite(echan)).setParent(row);
      log.debug("quantite");

      // objet statut
      if(echan.getObjetStatut() != null){
         final Label statut = new Label(ObjectTypesFormatters.ILNObjectStatut(echan.getObjetStatut()));
         if(echan.getObjetStatut().getStatut().equals("ENCOURS")){
            statut.setStyle("color: red");
         }
         statut.setParent(row);
      }else{
         new Label().setParent(row);
      }
      log.debug("objet statut");

      // emplacement
      if(!anonyme && accessStockage){
         final Label emplLabel = new Label(getEmplacementAdrl(echan));
         if(accessStockage){
            emplLabel.setSclass("formLink");
            emplLabel.addForward(null, emplLabel.getParent(), "onClickObjectEmplacement",
               ManagerLocator.getEchantillonManager().getEmplacementManager(echan));
         }
         emplLabel.setParent(row);
      }else{
         createAnonymeBlock().setParent(row);
      }

      // nb prodderives
      new Label(String.valueOf(getNbDerives(echan))).setParent(row);

      // nb cessions
      new Label(String.valueOf(getNbCessions(echan))).setParent(row);
      log.debug("cessions");
   }

   /**
    * Concatène la quantité et son unité.
    * @param echantillon Echantillon pour lequel on veut la quantité.
    * @return String.
    */
   public String getQuantite(final Echantillon echantillon){
      final StringBuffer sb = new StringBuffer();
      if(echantillon.getQuantite() != null){
         sb.append(floor(echantillon.getQuantite(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(echantillon.getQuantiteInit() != null){
         sb.append(floor(echantillon.getQuantiteInit(), 4));
      }else{
         sb.append("-");
      }

      if(echantillon.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(echantillon.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   public String getEmplacementAdrl(final Echantillon echantillon){
      if(echantillon != null && echantillon.getEchantillonId() != null){
         return ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(echantillon);
      }
      return "-";
   }

   public String getDelaiCgl(final Echantillon echantillon){
      final StringBuffer sb = new StringBuffer();
      String heureLabel = "";
      String minLabel = "";
      if(echantillon.getDelaiCgl() != null && echantillon.getDelaiCgl() > -1){
         final Float heure = echantillon.getDelaiCgl() / 60;
         Integer heureDelai = 0;
         Integer minDelai = 0;
         if(heure > 0){
            heureDelai = heure.intValue();
            minDelai = echantillon.getDelaiCgl().intValue() - (heureDelai * 60);
            heureLabel = heureDelai.toString();
            minLabel = minDelai.toString();
         }else{
            heureDelai = 0;
            minDelai = echantillon.getDelaiCgl().intValue();
            heureLabel = heureDelai.toString();
            minLabel = minDelai.toString();
         }

         sb.append(heureLabel);
         sb.append("h ");
         sb.append(minLabel);
         sb.append("min");
      }else{
         sb.append(Labels.getLabel("ficheEchantillon.delaiCgl.inconnu"));
      }

      return sb.toString();
   }

   public static int getNbDerives(final Echantillon echantillon){
      return ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon).size();
   }

   public static int getNbCessions(final Echantillon echantillon){
      return ManagerLocator.getCederObjetManager().findByObjetManager(echantillon).size();
   }

   /**
    * Récupère la date de création système de l'échantillon.
    * @return Date de création.
    */
   public String getDateCreation(final Echantillon echantillon){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(echantillon);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }
      return null;
   }

   /**
    * Arrondi d'un double avec n éléments après la virgule.
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
   public static float floor(final float a, final int n){
      final double p = Math.pow(10.0, n);
      return (float) (Math.floor((a * p) + 0.5) / p);
   }

   public boolean isAccessible(){
      return accessible;
   }

   public void setAccessible(final boolean a){
      this.accessible = a;
   }
   
   public boolean isAccessStockage(){
      return accessStockage;
   }

   public String getCodeAssigneInString(final Echantillon echantillon, final boolean isOrg){
      List<CodeAssigne> codes;
      if(isOrg){
         codes = ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echantillon);
      }else{
         codes = ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echantillon);
      }

      final StringBuffer sb = new StringBuffer();
      if(!codes.isEmpty()){
         for(int i = 0; i < codes.size(); i++){
            if(isOrg){
               sb.append(codes.get(0).getLibelle());
               sb.append(" [");
               sb.append(codes.get(0).getCode());
               sb.append("]");
            }else{
               sb.append(codes.get(0).getCode());
               sb.append(" [");
               sb.append(codes.get(0).getLibelle());
               sb.append("]");
            }

            if(i < codes.size() - 1){
               sb.append(", ");
            }
         }
      }else{
         sb.append("-");
      }

      return sb.toString();
   }

   public String getPatient(final Echantillon echan){
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echan);

      if(prlvt != null){
         return PrelevementUtils.getPatientNomAndPrenom(prlvt);
      }
      return "";
   }

   public boolean isEmbedded(){
      return isEmbedded;
   }

   public void setEmbedded(final boolean isE){
      this.isEmbedded = isE;
   }

}
