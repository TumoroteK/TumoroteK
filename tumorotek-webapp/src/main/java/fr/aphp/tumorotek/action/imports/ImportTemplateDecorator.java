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
package fr.aphp.tumorotek.action.imports;

import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateStatutPartage;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * since TK-537 : partage de modèle d'import
 * cette classe permet de définir sur un ImportTemplate des attributs spécifiques à l'affichage
 * @author chuet
 *
 */
public class ImportTemplateDecorator implements TKdataObject
{
   private ImportTemplate importTemplate;
   
   //L'icône utilisé pour indiquer le partage est différent selon le fait que le modèle est partagé par la collectioon courante ou non
   private final IconDisplayElement FLAG_PARTAGE_PAR_BANQUE_COURANTE = 
      new IconDisplayElement("fa fa-share-alt-square fa-lg tumo-fa-orange", "importTemplate.tooltip.partageParBanqueCourante");
   private final IconDisplayElement FLAG_PARTAGE_PAR_AUTRE_BANQUE = 
      new IconDisplayElement("fa fa-lock fa-lg tumo-fa-orange", "importTemplate.tooltip.partageParAutreBanque");

   /**
    * boolean qui permet d'indiquer que le template n'appartient pas à la collection courante. Intrinsèquement, il a la valeur partage "ENCOURS"
    * mais un template peut être partagé et appartenir à la collection courante d'où ce champ supplémentaire
    */
   private boolean notInCurrentCollection;
   
   /**
    * boolean qui permet de savoir si le template associé est utilisé pour la 1ere fois (dans le cas d'un template partagé)
    * valorisé à true si le ImportTemplateDecorator appartient à la liste des modèles partagés non utilisés
    */
   private boolean fromNotUsedTemplates;
   
   //class utilisée dans la liste des modèles pour gérer un affichage différent en fonction du statut archivé ou non
   private String sclassForList;


   public ImportTemplateDecorator() {}
   
   public ImportTemplateDecorator(ImportTemplate importTemplate) {
      this.importTemplate = importTemplate;
      this.sclassForList = defineSclassForList();
   }
   
   public ImportTemplateDecorator(ImportTemplate importTemplate, boolean notInCurrentCollection, boolean fromNotUsedTemplates) {
      this.importTemplate = importTemplate;
      this.notInCurrentCollection = notInCurrentCollection;
      this.fromNotUsedTemplates = fromNotUsedTemplates;
      this.sclassForList = defineSclassForList();
   }
   
      @Override
   public Integer listableObjectId(){
         return getImportTemplate().listableObjectId();
   }

   @Override
   public ImportTemplateDecorator clone(){
      ImportTemplateDecorator result = new ImportTemplateDecorator(getImportTemplate().clone());
      result.setFromNotUsedTemplates(this.isFromNotUsedTemplates());
      result.setNotInCurrentCollection(this.notInCurrentCollection);
      
      return result;
   }

   public ImportTemplate getImportTemplate(){
      if(importTemplate == null) {
         return new ImportTemplate();
      }
      return importTemplate;
   }

   public boolean isFromNotUsedTemplates(){
      return fromNotUsedTemplates;
   }

   public void setFromNotUsedTemplates(boolean fromNotUsedTemplates){
      this.fromNotUsedTemplates = fromNotUsedTemplates;
   }
   
   public boolean isNotInCurrentCollection() {
      return notInCurrentCollection;
   }
   public void setNotInCurrentCollection(boolean value) {
      this.notInCurrentCollection = value;
   }

   public String getSclassForList(){
      return sclassForList;
   }
   
   //si le template est partagé
   //l'icône est différent si le partage est fait par la collection courante ou par une autre
   private IconDisplayElement getInfoForIconePartage() {
      if(isPartage()) {
         if(notInCurrentCollection) {
            return FLAG_PARTAGE_PAR_AUTRE_BANQUE;
         }
         else {
            return FLAG_PARTAGE_PAR_BANQUE_COURANTE;
         }
      }
      return null;
   }
   

   public String getClassForIconePartage () {
      IconDisplayElement infoForIconePartage = getInfoForIconePartage();
      if(infoForIconePartage != null) {
         return infoForIconePartage.getCss();
      }
      
      return null;
   }
   
   public String getTooltiptextForIconePartage () {
      IconDisplayElement infoForIconePartage = getInfoForIconePartage();
      if(infoForIconePartage != null) {
         return Labels.getLabel(infoForIconePartage.getTooltiptext());
      }
      
      return "";
   }

   public boolean isPartage() {
      return getStatutPartage() == EImportTemplateStatutPartage.PARTAGE_ENCOURS;
   }
  
   private String defineSclassForList() {
      String sclassForList = "formLink";
      if(isArchive()) {
         sclassForList = "formLinkArchive";
      }
      return sclassForList;
   }
   
   
   //-------- getters et setters provenant du template. Facilite les appels dans le zul
   public Integer getImportTemplateId(){
      return getImportTemplate().getImportTemplateId();
   }
   public void setImportTemplateId(final Integer id){
      getImportTemplate().setImportTemplateId(id);
   }

   public Banque getBanque(){
      return getImportTemplate().getBanque();
   }
   public void setBanque(final Banque b){
      getImportTemplate().setBanque(b);
   }
   
   public String getNom(){
      return getImportTemplate().getNom();
   }
   public void setNom(final String n){
      getImportTemplate().setNom(n);
   }
   
   public String getDescription(){
      return getImportTemplate().getDescription();
   }
   public void setDescription(final String d){
      getImportTemplate().setDescription(d);
   }
   
   public Entite getDeriveParentEntite(){
      return getImportTemplate().getDeriveParentEntite();
   }

   public void setDeriveParentEntite(final Entite e){
      getImportTemplate().setDeriveParentEntite(e);
   }

   public Set<Entite> getEntites(){
      return getImportTemplate().getEntites();
   }

   public void setEntites(final Set<Entite> e){
      getImportTemplate().setEntites(e);
   }

   public Boolean isArchive(){
      return getImportTemplate().isArchive();
   }

   public void setArchive(Boolean archive){
      getImportTemplate().setArchive(archive);
      this.sclassForList = defineSclassForList();
   }

   public Integer getStatutPartageCode(){
      return getImportTemplate().getStatutPartageCode();
   }

   public void setStatutPartageCode(Integer statutPartageCode){
      getImportTemplate().setStatutPartageCode(statutPartageCode);
   }
 //------- FIN  getters et setters provenant du template. 
   
   public EImportTemplateStatutPartage getStatutPartage() {
      return EImportTemplateStatutPartage.findByCode(getStatutPartageCode());
   }   

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ImportTemplateDecorator deco = (ImportTemplateDecorator) obj;
      return this.getImportTemplate().equals(deco.getImportTemplate());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashColonne = 0;

      if(this.getImportTemplate() != null){
         hashColonne = this.getImportTemplate().hashCode();
      }

      hash = 7 * hash + hashColonne;

      return hash;
   }   
   
   
   
   
   //classe qui permet de regrouper les éléments d'affichage d'un icône
   private class IconDisplayElement {
      private String css;
      private String tooltiptext;
      
      public IconDisplayElement(String css, String tooltiptext) {
         this.css = css;
         this.tooltiptext = tooltiptext;
      }
      
      public String getCss(){
         return css;
      }
      public String getTooltiptext(){
         return tooltiptext;
      }
   }
   

   
}
