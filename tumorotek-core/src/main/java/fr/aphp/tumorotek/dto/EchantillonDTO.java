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
package fr.aphp.tumorotek.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 * Classe 'Decorateur' qui reprend les attributs d'Echantillon.
 * pour les afficher dans la liste associées :
 *  - Code
 *  - Date de stockage
 *  - Organe
 *  - Code lésionnel
 *  - Type
 *  - Quantité
 *  - Volume
 *  - Statut
 *  - Nb de produits dérivés
 *  - Date de création système
 * date: 09/12/09
 * transformé depuis Decorator vers DTO le 06/02/2017
 *
 * @version 2.1.1
 * @author Mathieu BARTHELEMY
 *
 */
public class EchantillonDTO
{

   private final boolean isNew;
   private Echantillon echantillon;
   private Boolean checkedEchantillon;
   private String adrlTmp;

   private List<AnnotationValeur> valeursToCreateOrUpdate;
   private List<CodeAssigne> codesOrgsToCreateOrEdit = new ArrayList<>();
   private List<CodeAssigne> codesLesToCreateOrEdit = new ArrayList<>();
   private CodeAssigne codeOrganeToExport;
   private CodeAssigne codeLesToExport;
   private List<NonConformite> nonConformiteTraitements = new ArrayList<>();
   private List<NonConformite> nonConformiteCessions = new ArrayList<>();

   private Integer nbDerives = 0;
   private Integer nbCessions = 0;
   private Calendar dateCreation;
   private String emplacementAdrl;
   private Float tempStock;

   public EchantillonDTO(final Echantillon newEchan){
      this.echantillon = newEchan;
      isNew = (this.echantillon.getEchantillonId() == null);
      this.checkedEchantillon = false;

      //	if (this.echantillon.getEchantillonId() != null) {
      //		setCodesOrgsToCreateOrEdit(codeAssigneManager()
      //				.findCodesOrganeByEchantillonManager(this.echantillon));
      //		
      //		setCodesLesToCreateOrEdit(codeAssigneManager()
      //				.findCodesMorphoByEchantillonManager(this.echantillon));
      //	}
   }

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon echan){
      this.echantillon = echan;
   }

   public String getCode(){
      return this.echantillon.getCode();
   }

   public String getBanque(){
      if(this.echantillon.getBanque() != null){
         return this.echantillon.getBanque().getNom();
      }
      return null;
   }

   public String getPrelevement(){
      if(this.echantillon.getPrelevement() != null){
         return this.echantillon.getPrelevement().getCode();
      }
      return null;
   }

   public Calendar getDateStockage(){
      return this.echantillon.getDateStock();
   }

   public String getAdicapOrgane(){
      return null;
   }

   public String getType(){
      if(this.echantillon.getEchantillonType() != null){
         return this.echantillon.getEchantillonType().getNom();
      }
      return null;
   }

   /**
    * Concatène la quantité et son unité.
    * @return String.
    */
   public String getQuantite(){
      final StringBuffer sb = new StringBuffer();
      if(this.echantillon.getQuantite() != null){
         sb.append(floor(this.echantillon.getQuantite(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(this.echantillon.getQuantiteInit() != null){
         sb.append(floor(this.echantillon.getQuantiteInit(), 4));
      }else{
         sb.append("-");
      }

      if(this.echantillon.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.echantillon.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   /**
    * Concatène la quantité initiale et son unité.
    * @return String.
    */
   public String getQuantiteInitiale(){
      final StringBuffer sb = new StringBuffer();
      if(this.echantillon.getQuantiteInit() != null){
         sb.append(this.echantillon.getQuantiteInit());
      }else{
         sb.append("-");
      }

      if(this.echantillon.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.echantillon.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   public String getOnlyQuantiteInit(){

      final StringBuffer sb = new StringBuffer();
      if(this.echantillon.getQuantiteInit() != null){
         sb.append(this.echantillon.getQuantiteInit());
      }else{
         sb.append("-");
      }

      if(this.echantillon.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.echantillon.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   public ObjetStatut getStatut(){
      return this.echantillon.getObjetStatut();

   }

   public Integer getNbDerives(){
      return nbDerives;
   }

   public Integer getNbCessions(){
      return nbCessions;
   }

   public boolean isNew(){
      return isNew;
   }

   /**
    * Récupère la date de création système de l'échantillon.
    * @return Date de création.
    */
   public Calendar getDateCreation(){
      return dateCreation;
      // Calendar date = ManagerLocator.getOperationManager()
      // 	.findDateCreationManager(this.echantillon);
      // if (dateCreation != null) {
      //	return ObjectTypesFormatters.dateRenderer2(date);
      // } else {
      //	return null;
      // }
   }

   public String getCollaborateurNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      final Collaborateur collaborateur = this.echantillon.getCollaborateur();
      if(collaborateur != null){
         if(collaborateur.getNom() != null && collaborateur.getPrenom() != null){
            sb.append(collaborateur.getNom());
            sb.append(" ");
            sb.append(collaborateur.getPrenom());
         }else if(collaborateur.getNom() != null){
            sb.append(collaborateur.getNom());
         }else if(collaborateur.getPrenom() != null){
            sb.append(collaborateur.getPrenom());
         }
      }
      return sb.toString();
   }

   public String getEmplacementAdrl(){
      // if (echantillon != null && echantillon.getEchantillonId() != null) {
      //	return ManagerLocator.getEchantillonManager()
      //		.getEmplacementAdrlManager(echantillon);
      // } else {
      //	return "";
      // }
      return emplacementAdrl;
   }

   /**
    * @since 2.0.13
    * @version 2.1.1
    * @return température de stockage
    */
   public String getTempStock(){
      //	if (echantillon != null) {
      //		Float temp = ManagerLocator.getConteneurManager()
      //				.findTempForEmplacementManager(echantillon.getEmplacement());
      //		if (temp != null) {
      //			return temp.toString();
      //		}
      //	}
      //	return null;
      if(tempStock != null){
         return tempStock.toString();
      }
      return null;
   }

   public String getEmplacementAdrlinMulti(){
      if(adrlTmp != null){
         return adrlTmp;
      }else if(echantillon != null && echantillon.getEchantillonId() != null){
         //			return ManagerLocator.getEchantillonManager()
         //				.getEmplacementAdrlManager(echantillon);
         return emplacementAdrl;
      }else{
         return null;
      }
   }

   public String getLateralite(){
      //return this.echantillon.getLateralite();
      return null;
   }

   public String getDelaiDeCongelation(){
      if(this.echantillon.getDelaiCgl() != null){
         return this.echantillon.getDelaiCgl().toString();
      }
      return null;
   }

   public String getQualite(){
      if(this.echantillon.getEchanQualite() != null){
         return this.echantillon.getEchanQualite().getNom();
      }
      return null;
   }

   public String getTumoral(){
      if(this.echantillon.getTumoral() != null){
         if(this.echantillon.getTumoral().booleanValue()){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   public String getModeDePreparation(){
      if(this.echantillon.getModePrepa() != null){
         return this.echantillon.getModePrepa().getNom();
      }
      return null;
   }

   public String getSterile(){
      if(this.echantillon.getSterile() != null){
         if(this.echantillon.getSterile().booleanValue()){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   public String getEtatIncomplet(){
      if(this.echantillon.getEtatIncomplet() != null){
         if(this.echantillon.getEtatIncomplet().booleanValue()){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   public String getArchive(){
      if(this.echantillon.getArchive() != null){
         if(this.echantillon.getArchive().booleanValue()){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final EchantillonDTO deco = (EchantillonDTO) obj;
      return this.getEchantillon().equals(deco.getEchantillon());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEchantillon = 0;

      if(this.echantillon != null){
         hashEchantillon = this.echantillon.hashCode();
      }

      hash = 7 * hash + hashEchantillon;

      return hash;
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

   public Boolean getCheckedEchantillon(){
      return checkedEchantillon;
   }

   public void setCheckedEchantillon(final Boolean checked){
      this.checkedEchantillon = checked;
   }

   public String getAdrlTmp(){
      return adrlTmp;
   }

   public void setAdrlTmp(final String tmp){
      this.adrlTmp = tmp;
   }

   public List<AnnotationValeur> getValeursToCreateOrUpdate(){
      return valeursToCreateOrUpdate;
   }

   public void setValeursToCreateOrUpdate(final List<AnnotationValeur> vals){
      this.valeursToCreateOrUpdate = vals;
   }

   public List<CodeAssigne> getCodesOrgsToCreateOrEdit(){
      return codesOrgsToCreateOrEdit;
   }

   public void setCodesOrgsToCreateOrEdit(final List<CodeAssigne> cOS){
      this.codesOrgsToCreateOrEdit = cOS;
   }

   public List<CodeAssigne> getCodesLesToCreateOrEdit(){
      return codesLesToCreateOrEdit;
   }

   public void setCodesLesToCreateOrEdit(final List<CodeAssigne> cLS){
      this.codesLesToCreateOrEdit = cLS;
   }

   public CodeAssigne getCodeOrganeToExport(){
      return codeOrganeToExport;
   }

   public void setCodeOrganeToExport(final CodeAssigne coE){
      this.codeOrganeToExport = coE;
   }

   public CodeAssigne getCodeLesToExport(){
      return codeLesToExport;
   }

   public void setCodeLesToExport(final CodeAssigne coE){
      this.codeLesToExport = coE;
   }

   public List<NonConformite> getNonConformiteTraitements(){
      return nonConformiteTraitements;
   }

   public void setNonConformiteTraitements(final List<NonConformite> n){
      this.nonConformiteTraitements = n;
   }

   public List<NonConformite> getNonConformiteCessions(){
      return nonConformiteCessions;
   }

   public void setNonConformiteCessions(final List<NonConformite> n){
      this.nonConformiteCessions = n;
   }

   public void setCode(final String value){
      getEchantillon().setCode(value);
   }

   public void setNbDerives(final Integer _d){
      this.nbDerives = _d;
   }

   public void setNbCessions(final Integer _c){
      this.nbCessions = _c;
   }

   public void setDateCreation(final Calendar _d){
      this.dateCreation = _d;
   }

   public void setEmplacementAdrl(final String _e){
      this.emplacementAdrl = _e;
   }

   public void setTempStock(final Float _t){
      this.tempStock = _t;
   }
}
