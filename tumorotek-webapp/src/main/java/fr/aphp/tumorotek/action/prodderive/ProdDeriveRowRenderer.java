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
package fr.aphp.tumorotek.action.prodderive;

import java.util.Calendar;

import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.action.utils.TKStockableObjectUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;

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
public class ProdDeriveRowRenderer extends TKSelectObjectRenderer
{

   private boolean accessible = true;
   private boolean accessPrelevement = true;
   private boolean accessEchantillon = true;
   private boolean isEmbedded = false;

   private boolean accessStockage = true;

   public void setAccessStockage(final boolean ac){
      this.accessStockage = ac;
   }

   public ProdDeriveRowRenderer(final boolean select, final boolean cols){
      setSelectionMode(select);
      setTtesCollections(cols);
   }

   @Override
   public void render(final Row row, final Object data, final int index){
      // dessine le checkbox
      super.render(row, data, index);

      final ProdDerive derive = (ProdDerive) data;

      final Hlayout icones = TKStockableObjectUtils.drawListIcones(derive, null, null);
      icones.setParent(row);

      // code
      final Label codeLabel = new Label(derive.getCode());
      if(accessible){
         codeLabel.addForward(null, codeLabel.getParent(), "onClickObject", derive);
         codeLabel.setClass("formLink");
      }
      codeLabel.setParent(row);

      if(isTtesCollections()){
         new Label(derive.getBanque().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // patient
      if(!isEmbedded){
         if(!anonyme){
            new Label(getPatient(derive)).setParent(row);
         }else{
            createAnonymeBlock().setParent(row);
         }
      }

      // code labo
      new Label(derive.getCodeLabo()).setParent(row);

      // type parent
      new Label(getTypeParent(derive)).setParent(row);

      // code parent
      (getCodeParent(derive)).setParent(row);

      // date de stockage
      new Label(ObjectTypesFormatters.dateRenderer2(derive.getDateStock())).setParent(row);

      // type
      if(derive.getProdType() != null){
         new Label(derive.getProdType().getType()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // volume
      new Label(getVolume(derive)).setParent(row);

      // concentration
      new Label(getConcentration(derive)).setParent(row);

      // quantite
      new Label(getQuantite(derive)).setParent(row);

      // statut
      if(derive.getObjetStatut() != null){
         final Label statut = new Label(ObjectTypesFormatters.ILNObjectStatut(derive.getObjetStatut()));
         if(derive.getObjetStatut().getStatut().equals("ENCOURS")){
            statut.setStyle("color: red");
         }
         statut.setParent(row);
      }else{
         new Label().setParent(row);
      }

      // emplacement
      if(!anonyme && accessStockage){
         final Label emplLabel = new Label(getEmplacementAdrl(derive));
         if(accessStockage){
            emplLabel.setSclass("formLink");
            emplLabel.addForward(null, emplLabel.getParent(), "onClickObjectEmplacement",
               ManagerLocator.getProdDeriveManager().getEmplacementManager(derive));
         }
         emplLabel.setParent(row);
      }else{
         createAnonymeBlock().setParent(row);
      }

      // nb derives
      new Label(String.valueOf(getNbDerives(derive))).setParent(row);

      // nb cessions
      new Label(String.valueOf(getNbCessions(derive))).setParent(row);

   }

   /**
    * Récupère le type du parent (Prelevement, Echantillon ou ProdDerive).
    */
   public String getTypeParent(final ProdDerive prodDerive){
      final Transformation transformation = prodDerive.getTransformation();
      if(transformation != null){
         // on récupère le type du parent (prlvt, echan, prodderive)
         return transformation.getEntite().getNom();
      }else{
         return null;
      }
   }

   /**
    * Récupère le code du parent (Prelevement, Echantillon ou ProdDerive).
    */
   public Label getCodeParent(final ProdDerive prodDerive){
      final Label codeLabel = new Label();
      final Transformation transformation = prodDerive.getTransformation();
      String code = "";
      if(transformation != null){
         final String typeParent = transformation.getEntite().getNom();

         // en fonction du type, on récupère l'objet
         if(typeParent.equals("Prelevement")){
            final Prelevement prel = ((Prelevement) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
            code = prel.getCode();
            if(accessPrelevement){
               codeLabel.addForward(null, codeLabel.getParent(), "onClickParent", prel);
               codeLabel.setClass("formLink");
            }
         }else if(typeParent.equals("Echantillon")){
            final Echantillon echan = ((Echantillon) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
            code = echan.getCode();
            if(accessEchantillon){
               codeLabel.addForward(null, codeLabel.getParent(), "onClickParent", echan);
               codeLabel.setClass("formLink");
            }
         }else if(typeParent.equals("ProdDerive")){
            final ProdDerive pd = ((ProdDerive) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
            code = pd.getCode();
            if(accessible){
               codeLabel.addForward(null, codeLabel.getParent(), "onClickParent", pd);
               codeLabel.setClass("formLink");
            }
         }
      }
      codeLabel.setValue(code);
      return codeLabel;
   }

   /**
    * Concatène la quantité et son unité.
    * @return String.
    */
   public String getQuantite(final ProdDerive prodDerive){
      final StringBuffer sb = new StringBuffer();
      if(prodDerive.getQuantite() != null){
         sb.append(floor(prodDerive.getQuantite(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(prodDerive.getQuantiteInit() != null){
         sb.append(floor(prodDerive.getQuantiteInit(), 4));
      }else{
         sb.append("-");
      }

      if(prodDerive.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(prodDerive.getQuantiteUnite().getUnite());
      }

      return sb.toString();
   }

   /**
    * Concatène le volume et son unité.
    * @return String.
    */
   public String getVolume(final ProdDerive prodDerive){
      final StringBuffer sb = new StringBuffer();
      if(prodDerive.getVolume() != null){
         sb.append(floor(prodDerive.getVolume(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(prodDerive.getVolumeInit() != null){
         sb.append(floor(prodDerive.getVolumeInit(), 4));
      }else{
         sb.append("-");
      }

      if(prodDerive.getVolumeUnite() != null){
         sb.append(" ");
         sb.append(prodDerive.getVolumeUnite().getUnite());
      }

      return sb.toString();
   }

   /**
    * Concatène la concentration et son unité.
    * @return String.
    */
   public String getConcentration(final ProdDerive prodDerive){
      final StringBuffer sb = new StringBuffer();
      if(prodDerive.getConc() != null){
         sb.append(floor(prodDerive.getConc(), 4));
      }else{
         sb.append("-");
      }

      if(prodDerive.getConcUnite() != null){
         sb.append(" ");
         sb.append(prodDerive.getConcUnite().getUnite());
      }

      return sb.toString();
   }

   public String getEmplacementAdrl(final ProdDerive prodDerive){
      if(prodDerive != null && prodDerive.getProdDeriveId() != null){
         return ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(prodDerive);
      }else{
         return "";
      }
   }

   public static int getNbDerives(final ProdDerive prodDerive){
      return ManagerLocator.getProdDeriveManager().getProdDerivesManager(prodDerive).size();
   }

   public Integer getNbCessions(final ProdDerive prodDerive){
      return ManagerLocator.getCederObjetManager().findByObjetManager(prodDerive).size();
   }

   /**
    * Récupère la date de création système du produit dérivé.
    * @return Date de création.
    */
   public String getDateCreation(final ProdDerive prodDerive){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(prodDerive);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }else{
         return null;
      }
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

   public String getPatient(final ProdDerive derive){
      final Prelevement prlvt = ManagerLocator.getProdDeriveManager().getPrelevementParent(derive);

      if(prlvt != null){
         return PrelevementUtils.getPatientNomAndPrenom(prlvt);
      }else{
         return "";
      }
   }

   public boolean isAccessible(){
      return accessible;
   }

   public void setAccessible(final boolean acc){
      this.accessible = acc;
   }

   public void setAccessEchantillon(final boolean ae){
      this.accessEchantillon = ae;
   }

   public void setAccessPrelevement(final boolean ap){
      this.accessPrelevement = ap;
   }

   public boolean isEmbedded(){
      return isEmbedded;
   }

   public void setEmbedded(final boolean isE){
      this.isEmbedded = isE;
   }

}
