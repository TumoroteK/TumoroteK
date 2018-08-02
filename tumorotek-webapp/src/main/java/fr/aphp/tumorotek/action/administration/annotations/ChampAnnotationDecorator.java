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
package fr.aphp.tumorotek.action.administration.annotations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utils.Duree;

/**
 * Classe 'Decorateur' qui decore Champannotation pour
 * lui ajouter un membre booleen edition.
 * Utilisé pour modifier le mode d' affichage de la ligne dans le module
 * d'administration de création/modification de tables.
 * date: 23/03/2010
 *
 * @version 2.0
 * @author mathieu BARTHELEMY
 *
 */
public class ChampAnnotationDecorator extends SmallObjDecorator
{

   private ChampAnnotation champ;
   private final LinkedHashSet<AnnotationDefaut> defauts = new LinkedHashSet<>();
   private final LinkedHashSet<AnnotationDefaut> defautsCopy = new LinkedHashSet<>();

   private List<Item> items = new ArrayList<>();
   private AnnotationDefaut defaut;
   // flag qui specifie si l'objet a ete valide (ou en cours de creation)
   private boolean validated = false;

   private boolean edition = false;
   // flag qui permet de ne pas d'afficher typeBox en modification
   private boolean creation = false;

   private boolean catalogueChp = false;

   private Banque currentBanque = null;

   private ChampCalcule champCalcule;

   @Override
   public ChampAnnotationDecorator clone(){
      final ChampAnnotationDecorator clone = new ChampAnnotationDecorator(getChamp(), currentBanque, getCatalogueChp());
      return clone;
   }

   /**
    * Constructeur.
    * @param chp
    */
   public ChampAnnotationDecorator(final ChampAnnotation chp, final Banque bank, final boolean isCatalogueChp){
      this.currentBanque = bank;
      setChampAnnotation(chp);
      setCatalogueChp(isCatalogueChp);
   }

   public void setChampAnnotation(final ChampAnnotation chp){
      this.champ = chp;
      if(isThesaurus()){
         defautsContainsEmptyFirst(ManagerLocator.getChampAnnotationManager().getAnnotationDefautsManager(chp), this.defauts);

         this.items = new ArrayList<>(ManagerLocator.getChampAnnotationManager().getItemsManager(chp, currentBanque));

      }
      if(isChampCalcule()){
         defautsContainsEmptyFirst(ManagerLocator.getChampAnnotationManager().getAnnotationDefautsManager(chp), this.defauts);
         this.champCalcule = ManagerLocator.getChampAnnotationManager().getChampCalculeManager(chp);
      }else{
         //type alphanum par defaut
         this.defauts.addAll(ManagerLocator.getChampAnnotationManager().getAnnotationDefautsManager(chp));
         //this.champ.setAnnotationDefauts(this.defauts);
         // ajoute une valeur vide au besoin
         if(this.defauts.size() == 0){
            reset();
         }else{
            this.defaut = this.defauts.iterator().next();
         }

      }
   }

   /**
    * Nettoie les valeurs d'annotations par defaut et popule la liste
    * avec une AnnotationDefaut vide.
    */
   public void reset(){

      this.defaut = new AnnotationDefaut();
      this.defaut.setChampAnnotation(champ);
      this.defaut.setBanque(currentBanque);
      this.defaut.setObligatoire(false);
      this.defauts.clear();
      this.defauts.add(defaut);
      if(isThesaurus()){
         this.items.clear();
      }
   }

   public ChampAnnotation getChamp(){
      return this.champ;
   }

   public ChampCalcule getChampCalcule(){
      return champCalcule;
   }

   public void setChampCalcule(ChampCalcule champCalcule){
      this.champCalcule = champCalcule;
   }

   @Override
   public boolean isValidated(){
      return validated;
   }

   @Override
   public void setValidated(final boolean v){
      this.validated = v;
   }

   @Override
   public boolean getEdition(){
      return edition;
   }

   @Override
   public boolean getStatique(){
      return !edition;
   }

   @Override
   public void setEdition(final boolean b){
      this.edition = b;
   }

   public boolean getCreation(){
      return creation;
   }

   public boolean getNoCreation(){
      return !creation;
   }

   public void setCreation(final boolean c){
      this.creation = c;
   }

   public boolean getCatalogueChp(){
      return catalogueChp;
   }

   public void setCatalogueChp(final boolean c){
      this.catalogueChp = c;
   }

   public void setNom(final String s){
      this.champ.setNom(s);
   }

   public String getNom(){
      return this.champ.getNom();
   }

   public String getDataType(){
      if(this.champ.getDataType() != null){
         return Labels.getLabel("DataType." + this.champ.getDataType().getType());
      }
      return null;
   }

   public void setDataType(final DataType type){
      this.champ.setDataType(type);
   }

   /****** Obligatoire. *****/

   public boolean getObligatoire(){
      return this.defaut.getObligatoire();
   }

   public void setObligatoire(final boolean obl){
      this.defaut.setObligatoire(obl);
   }

   public boolean getIsObligeable(){
      if(this.champ != null && this.champ.getDataType() != null){
         return (!"fichier".equals(this.champ.getDataType().getType()) && !"calcule".equals(this.champ.getDataType().getType()));
      }
      return !catalogueChp;
   }

   public boolean getIsNotObligeable(){
      return !getIsObligeable();
   }

   public boolean getIsObligeableEdition(){
      return getIsObligeable() && edition;
   }

   public boolean getIsObligeableStatique(){
      return getIsObligeable() && !edition;
   }

   public String getObligatoireFormatted(){
      if(this.champ != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(getObligatoire());
      }
      return null;
   }

   /****** Combine. ******/

   public Boolean getCombine(){
      return this.champ.getCombine();
   }

   public String getCombineFormatted(){
      if(this.champ != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.champ.getCombine());
      }
      return null;
   }

   public void setCombine(final Boolean b){
      this.champ.setCombine(b);
   }

   public boolean getIsCombinable(){
      if(this.champ != null && this.champ.getDataType() != null){
         return (!"fichier".equals(this.champ.getDataType().getType()) && !"boolean".equals(this.champ.getDataType().getType()) && !"calcule".equals(this.champ.getDataType().getType()));
      }
      return !catalogueChp;
   }

   public boolean getIsNotCombinable(){
      return !getIsCombinable();
   }

   public boolean getIsCombinableEdition(){
      return getIsCombinable() && edition;
   }

   public boolean getIsCombinableStatique(){
      return getIsCombinable() && !edition;
   }

   /****** Flags Catalogue. ******/
   public boolean getEditionNoCatalogue(){
      return getEdition() && !catalogueChp;
   }

   public boolean getStatiqueNoCatalogue(){
      return getStatique() && !catalogueChp;
   }

   public boolean getStatiqueCatalogue(){
      return getStatique() || catalogueChp;
   }

   /**
    * Affiche en mode statique la gomme permettant de supprimer les valeurs
    * par défaut si defaut n'est pas empty et/ou les items si thesaurus.
    * @return true si visible false sinon
    */
   public Boolean getDefautEraserDisplay(){
      return ((!isThesaurus() && !isThesaurusM() && !this.defaut.isEmpty() || !this.items.isEmpty())
         || (("calcule".equals(this.champ.getDataType().getType()) && this.champCalcule != null))) && !getStatique();
   }

   /**
    * Rend le sTring "details" cliquable pour afficher le details des 
    * items dans la modale.
    * @return String style classe cliquable ou vide.
    */
   public String getDetailsDisplay(){
      if(!this.items.isEmpty() && getStatique() && this.items.size() > 5){
         return "formLink";
      }
      return "";
   }

   /**
    * Affiche en mode edition le lien permettant d'ajouter les valeurs
    * par défaut si defaut n'est pas empty et les items si thesaurus.
    * @return true si visible false sinon
    */
   public Boolean getAddDefautDisplay(){
      return ((this.defaut.isEmpty() && this.items.isEmpty() && null == this.champCalcule)) && !getStatique();
   }

   /**
    * Formatte la (ou les) valeur(s) par defauts.
    * @return string formaté du Set AnnotationDefaut
    **/
   public String getDefautsFormatted(){
      if(isAlphanum() || isHyperlien()){
         return this.defaut.getAlphanum();
      }else if(isBoolean()){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.defaut.getBool());
      }else if(isDate() || isDatetime()){
         return ObjectTypesFormatters.dateRenderer2(this.defaut.getDate());
      }else if(isNum() && this.defaut.getAlphanum() != null){
         return ObjectTypesFormatters.doubleLitteralFormatter(Double.parseDouble(this.defaut.getAlphanum()));
      }else if(isTexte()){
         return this.defaut.getTexte();
      }else if(isThesaurus()){
         if(items.size() <= 5){
            return ObjectTypesFormatters.renderItems(this.items, this.defauts);
         }
         return Labels.getLabel("general.details");
      }else if(isChampCalcule()){
         return ObjectTypesFormatters.renderChampCalcule(this.champCalcule);
      }else if(isDuree() && null != this.defaut.getAlphanum()){
         return ObjectTypesFormatters.formatDuree(new Duree(new Long(this.defaut.getAlphanum()), Duree.SECONDE));
      }
      return null;
   }

   public Set<AnnotationDefaut> getDefauts(){
      return this.defauts;
   }

   public void setDefauts(final Set<AnnotationDefaut> defs){

      // ajoute la premiere valeur par defaut
      if(!isThesaurus()){
         this.defauts.clear();
         this.defauts.addAll(defs);
         this.defaut = defauts.iterator().next();
      }else{
         defautsContainsEmptyFirst(defs, this.defauts);
      }
   }

   public List<Item> getItems(){
      return items;
   }

   public void setItems(final List<Item> its){
      this.items = its;
   }

   /***** flags instruisant le type du champ et le mode des composants. ***/

   public boolean isAlphanum(){
      return ("alphanum".equals(this.champ.getDataType().getType()));
   }

   public boolean isBoolean(){
      return ("boolean".equals(this.champ.getDataType().getType()));
   }

   public boolean isDate(){
      return ("date".equals(this.champ.getDataType().getType()));
   }

   public boolean isDatetime(){
      return ("datetime".equals(this.champ.getDataType().getType()));
   }

   public boolean isNum(){
      return ("num".equals(this.champ.getDataType().getType()));
   }

   public boolean isDuree(){
      return ("duree".equals(this.champ.getDataType().getType()));
   }

   public boolean isTexte(){
      return ("texte".equals(this.champ.getDataType().getType()));
   }

   public boolean isThesaurus(){
      return (this.champ.getDataType().getType().matches("thesaurus.?"));
   }

   public boolean isThesaurusM(){
      return (this.champ.getDataType().getType().matches("thesaurusM"));
   }

   public boolean isHyperlien(){
      return (this.champ.getDataType().getType().matches("hyperlien"));
   }

   public boolean isChampCalcule(){
      return this.champ.getDataType().getType().matches("calcule");
   }

   public Banque getCurrentBanque(){
      return currentBanque;
   }

   /**
    * Decore une liste de champs.
    * @param champs
    * @param Banque courante
    * @return champs décorés.
    */
   public static List<ChampAnnotationDecorator> decorateListe(final List<ChampAnnotation> chps, final Banque bank,
      final boolean isCatalogueChp){
      final List<ChampAnnotationDecorator> liste = new ArrayList<>();
      final Iterator<ChampAnnotation> it = chps.iterator();
      while(it.hasNext()){
         liste.add(new ChampAnnotationDecorator(it.next(), bank, isCatalogueChp));
      }
      return liste;
   }

   /**
    * Retrouve la liste de champs a partir de la liste de decorateurs en 
    * conservant l'ordre.
    * @param decorateurs
    * @return champs ChampAnnotation dans le même ordre
    */
   public static List<ChampAnnotation> unDecore(final List<ChampAnnotationDecorator> decos){
      final List<ChampAnnotation> liste = new ArrayList<>();
      final Iterator<ChampAnnotationDecorator> it = decos.iterator();
      while(it.hasNext()){
         liste.add(it.next().getChamp());
      }
      return liste;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ChampAnnotationDecorator deco = (ChampAnnotationDecorator) obj;
      return this.getChamp().equals(deco.getChamp());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashPrlvt = 0;

      if(this.champ != null){
         hashPrlvt = this.champ.hashCode();
      }

      hash = 7 * hash + hashPrlvt;

      return hash;
   }

   /**
    * Comparator permettant le sort dans la liste en fonction de l'ordre
    * specifié pour le champ.
    * @return int négatif si ordre1 < ordre2, 0 si ordre1 = ordre2, 
    * positif si ordre1 > ordre 2.
    */
   public static class ChampAnnotationDecoratorComparator implements Comparator<Object>
   {

      @Override
      public int compare(final Object arg0, final Object arg1){
         final ChampAnnotationDecorator chp1 = (ChampAnnotationDecorator) arg0;
         final ChampAnnotationDecorator chp2 = (ChampAnnotationDecorator) arg1;
         return (chp1.getChamp().getOrdre() - chp2.getChamp().getOrdre());
      }

   }

   /**
    * Verifie au moment de la validation du champ que ce dernier
    * a au moins une valeur défaut non vide, sinon aucune.
    */
   public void checkEmptyDefaut(){
      if(!this.champ.getAnnotationDefauts().isEmpty() && this.champ.getAnnotationDefauts().iterator().next().isEmpty()){
         this.champ.getAnnotationDefauts().clear();
      }
   }

   /**
    * Verifie que dans la liste de defauts qui doit etre assignee à l'autre,
    * la valeur empty existe et se trouve en premier place. Cette valeur empty
    * est utilisee pour les thesaurus pour renseigner le caractere obligatoire
    * (qui peut etre specifie bien qu'au item ne soit choisit par défaut).
    * @param defs à assigner
    */
   private void defautsContainsEmptyFirst(final Set<AnnotationDefaut> defs, final LinkedHashSet<AnnotationDefaut> newSet){
      final Iterator<AnnotationDefaut> itor = defs.iterator();
      AnnotationDefaut def = null;
      boolean hasEmpty = false;
      int i = 0;
      while(itor.hasNext()){
         def = itor.next();
         if(def.isEmpty()){
            hasEmpty = true;
            break;
         }
         i++;
      }
      newSet.clear();
      // cree l'annotation par defaut empty
      if(!hasEmpty){
         final AnnotationDefaut empty = new AnnotationDefaut();
         empty.setChampAnnotation(champ);
         empty.setBanque(currentBanque);
         empty.setObligatoire(false);
         newSet.add(empty);
         this.defaut = empty;
      }else if(i != 0){ // ajoute l'empty en premiere position
         newSet.add(def);
         defs.remove(def);
         this.defaut = def;
      }else{
         this.defaut = defs.iterator().next();
      }
      newSet.addAll(defs);

   }

   @Override
   public Object getObjClone(){
      return ((ChampAnnotation) getObj()).clone();
   }

   @Override
   public Integer getObjDbId(){
      return ((ChampAnnotation) getObj()).getId();
   }

   @Override
   public void syncOrdre(){
      ((ChampAnnotation) getObj()).setOrdre(getOrdre());
   }

   /**
    * Copy la liste de defauts avant son edition.
    */
   public void cloneDefauts(){
      defautsCopy.clear();
      final Iterator<AnnotationDefaut> defautsIt = defauts.iterator();
      while(defautsIt.hasNext()){
         defautsCopy.add(defautsIt.next().clone());
      }
   }

   /**
    * Replace la liste de defauts initiales dans le cas d'un revert.
    */
   public void revertDefauts(){
      defauts.clear();
      defauts.addAll(defautsCopy);
      if(!defauts.isEmpty()){
         defaut = defauts.iterator().next();
      }else{
         reset();
      }
   }
}
