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

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
 * EchantillonRenderer affiche dans le Row les membres d'Echantillon sous forme
 * de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples Date: 17/04/2010
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

      // dessine le checkbox
      super.render(row, data, index);

      renderObjets(row, data);
   }

   public void renderObjets(final Row row, final Object data){

      final Echantillon echan = (Echantillon) data;

      // @since gatsbi, icones peuvent ne jamais s'afficher
      // icones
      if(areIconesRendered()){
         final Hlayout icones = TKStockableObjectUtils.drawListIcones(echan, null, null);

         // infectieux
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
         icones.setParent(row);
      }

      // identifiant
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
      // }else if(!isEmbedded){
      } else {
         new Label().setParent(row);
      }

      // patient
      if(!isEmbedded){
         if(!anonyme){
            new Label(getPatient(echan)).setParent(row);
         }else{
            createAnonymeLabelIsClickable(true).setParent(row);
         }
      }

      // @since gatsbi
      try{
         renderEchantillon(row, echan);
      }catch(final Exception e){
         // une erreur inattendue levée dans la récupération
         // ou le rendu d'une propriété prel
         // va arrêter le rendu du reste du tableau
         throw new RuntimeException(e);
      }

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

   public String getPatient(final Echantillon echan){
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echan);

      if(prlvt != null){
         return PrelevementUtils.getPatientNomAndPrenomOrIdentifiantGatsbi(prlvt);
      }
      return "";
   }

   public boolean isEmbedded(){
      return isEmbedded;
   }

   public void setEmbedded(final boolean isE){
      this.isEmbedded = isE;
   }

   /**
    * Rendu des colonnes spécifiques échantillon, sera surchargé par Gatsbi.
    *
    * @param row
    * @param echan
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    */
   protected void renderEchantillon(final Row row, final Echantillon echan)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      renderDateProperty(row, echan, "dateStock");

      renderCodeAssignes(row, ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echan));

      renderCodeAssignes(row, ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echan));

      renderThesObjectProperty(row, echan, "echantillonType");

      renderQuantite(row, echan);

      renderObjetStatut(row, echan);

      renderEmplacement(row, echan, isAnonyme(), isAccessStockage());

      renderNbDerives(row, echan);

      renderNbCessions(row, echan);
   }

   // codes lésionnels : liste des codes exportés pour l'échantillon
   public static void renderCodeAssignes(final Row row, final List<CodeAssigne> codes){
      ObjectTypesFormatters.drawCodesExpLabel(codes, row, null, false);
   }

   /**
    * Arrondi d'un double avec n éléments après la virgule.
    *
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
   public static float floor(final float a, final int n){
      final double p = Math.pow(10.0, n);
      return (float) (Math.floor((a * p) + 0.5) / p);
   }

   /**
    * Concatène la quantité et son unité et écris dans la row
    *
    * @param row
    * @param echantillon Echantillon pour lequel on veut la quantité.
    * @return String.
    */
   public static void renderQuantite(final Row row, final Echantillon echantillon){
      new Label(formatQuantite(echantillon)).setParent(row);
   }

   public static String formatQuantite(final Echantillon echan){
      final StringBuffer sb = new StringBuffer();
      if(echan.getQuantite() != null){
         sb.append(floor(echan.getQuantite(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(echan.getQuantiteInit() != null){
         sb.append(floor(echan.getQuantiteInit(), 4));
      }else{
         sb.append("-");
      }

      if(echan.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(echan.getQuantiteUnite().getNom());
      }
      return sb.toString();
   }

   /**
    * Colorie le statut suivant la valeur
    *
    * @param row
    * @param echan
    */
   public static void renderObjetStatut(final Row row, final Echantillon echan){
      if(echan.getObjetStatut() != null){

         final Label statut = new Label(ObjectTypesFormatters.ILNObjectStatut(echan.getObjetStatut()));
         if(echan.getObjetStatut().getStatut().equals("ENCOURS")){
            statut.setStyle("color: red");
         }
         statut.setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   public static void renderEmplacement(final Row row, final Echantillon echan, final boolean anonyme,
      final boolean accessStockage){
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
         createAnonymeLabelIsClickable(false).setParent(row);
      }
   }

   public static String getEmplacementAdrl(final Echantillon echantillon){
      if(echantillon != null && echantillon.getEchantillonId() != null){
         return ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(echantillon);
      }
      return "-";
   }

   public static void renderNbDerives(final Row row, final Echantillon echan){
      new Label(String.valueOf(ManagerLocator.getEchantillonManager().getProdDerivesManager(echan).size())).setParent(row);
   }

   public static void renderNbCessions(final Row row, final Echantillon echan){
      new Label(String.valueOf(ManagerLocator.getCederObjetManager().findByObjetManager(echan).size())).setParent(row);
   }

   public static void renderDelaiCgl(final Row row, final Echantillon echan){
      if(echan.getDelaiCgl() != null){
         new Label(formatDelaiCgl(echan)).setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   public static String formatDelaiCgl(final Echantillon echantillon){
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

   /**
    * Récupère la date de création système de l'échantillon.
    *
    * @return Date de création.
    */
   public static String getDateCreation(final Echantillon echantillon){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(echantillon);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }
      return null;
   }

   public static String getCodeAssigneInString(final Echantillon echantillon, final boolean isOrg){
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

   public static void renderLateralite(final Row row, final Echantillon echan){
      if(echan.getLateralite() != null){
         new Label(Labels.getLabel("echantillon.lateralite.".concat(echan.getLateralite()))).setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   public static void renderCrAnapath(final Row row, final Echantillon echan, final boolean anonyme){
      if(echan.getCrAnapath() != null){
         final Label crAnapathLabel = new Label(echan.getCrAnapath().getNom());

         // clickable -> download
         if(!anonyme){
            crAnapathLabel.setClass("formLink");
            crAnapathLabel.addForward("onClick", row.getParent(), "onClickCrAnapathLabel", echan.getCrAnapath());
         }

         crAnapathLabel.setParent(row);
      }else{
         new Label().setParent(row);
      }
   }
}
