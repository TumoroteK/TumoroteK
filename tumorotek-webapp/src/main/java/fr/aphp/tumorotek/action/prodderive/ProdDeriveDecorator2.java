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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 * Classe 'Decorateur' qui reprend les attributs de ProdDerive.
 * pour les afficher dans la liste associées :
 *  - Code
 *  - Code Labo
 *  - Type du parent
 *  - Code du parent
 *  - Date de stockage
 *  - Type
 *  - Quantité
 *  - Volume
 *  - Concentration
 *  - Statut
 *  - Date de création système
 * date: 09/12/09
 *
 * @version 2.0.12
 * @author Pierre Ventadour
 *
 */
public class ProdDeriveDecorator2
{

   private final boolean isNew;
   private ProdDerive prodDerive;
   private Boolean checkedProdDerive;
   private String adrlTmp;

   private List<NonConformite> nonConformiteTraitements = new ArrayList<>();
   private List<NonConformite> nonConformiteCessions = new ArrayList<>();

   public ProdDeriveDecorator2(final ProdDerive derive){
      this.prodDerive = derive;
      checkedProdDerive = false;
      isNew = (this.prodDerive.getProdDeriveId() == null);
   }

   public ProdDerive getProdDerive(){
      return prodDerive;
   }

   public void setProdDerive(final ProdDerive derive){
      this.prodDerive = derive;
   }

   public String getCode(){
      if(this.prodDerive.getCode() != null){
         return this.prodDerive.getCode();
      }
      return null;
   }

   public String getBanque(){
      if(this.prodDerive.getBanque() != null){
         return this.prodDerive.getBanque().getNom();
      }
      return null;
   }

   public String getCodeLabo(){
      if(this.prodDerive.getCodeLabo() != null){
         return this.prodDerive.getCodeLabo();
      }
      return null;
   }

   /**
    * Récupère le type du parent (Prelevement, Echantillon ou ProdDerive).
    */
   public String getTypeParent(){
      final Transformation transformation = this.prodDerive.getTransformation();
      if(transformation != null){
         // on récupère le type du parent (prlvt, echan, prodderive)
         return transformation.getEntite().getNom();
      }
      return null;
   }

   /**
    * Récupère le code du parent (Prelevement, Echantillon ou ProdDerive).
    */
   public String getCodeParent(){
      final Transformation transformation = this.prodDerive.getTransformation();
      if(transformation != null){
         final String typeParent = transformation.getEntite().getNom();

         // en fonction du type, on récupère l'objet
         if(typeParent.equals("Prelevement")){
            return ((Prelevement) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(transformation.getEntite(),
               transformation.getObjetId())).getCode();
         }else if(typeParent.equals("Echantillon")){
            return ((Echantillon) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(transformation.getEntite(),
               transformation.getObjetId())).getCode();
         }else if(typeParent.equals("ProdDerive")){
            return ((ProdDerive) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(transformation.getEntite(),
               transformation.getObjetId())).getCode();
         }else{
            return null;
         }
      }
      return null;
   }

   public String getDateStock(){
      if(this.prodDerive.getDateStock() != null){
         return ObjectTypesFormatters.dateRenderer2(this.prodDerive.getDateStock());
      }
      return null;
   }

   public String getType(){
      if(this.prodDerive.getProdType() != null){
         return this.prodDerive.getProdType().getNom();
      }
      return null;
   }

   /**
    * Concatène la quantité et son unité.
    * @return String.
    */
   public String getQuantite(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getQuantite() != null){
         sb.append(floor(this.prodDerive.getQuantite(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(this.prodDerive.getQuantiteInit() != null){
         sb.append(floor(this.prodDerive.getQuantiteInit(), 4));
      }else{
         sb.append("-");
      }

      if(this.prodDerive.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   /**
    * Concatène la quantité initiale et son unité.
    * @return String.
    */
   public String getQuantiteInitiale(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getQuantiteInit() != null){
         sb.append(this.prodDerive.getQuantiteInit());
      }else{
         sb.append("-");
      }

      if(this.prodDerive.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getQuantiteUnite().getNom());
      }

      return sb.toString();
   }

   /**
    * Concatène le volume et son unité.
    * @return String.
    */
   public String getVolume(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getVolume() != null){
         sb.append(floor(this.prodDerive.getVolume(), 4));
      }else{
         sb.append("-");
      }

      sb.append(" / ");

      if(this.prodDerive.getVolumeInit() != null){
         sb.append(floor(this.prodDerive.getVolumeInit(), 4));
      }else{
         sb.append("-");
      }

      if(this.prodDerive.getVolumeUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getVolumeUnite().getNom());
      }

      return sb.toString();
   }

   /**
    * Concatène le volume initial et son unité.
    * @return String.
    */
   public String getVolumeInitial(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getVolumeInit() != null){
         sb.append(this.prodDerive.getVolumeInit());
      }else{
         sb.append("-");
      }

      if(this.prodDerive.getVolumeUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getVolumeUnite().getNom());
      }

      return sb.toString();
   }

   /**
    * Concatène la concentration et son unité.
    * @return String.
    */
   public String getConcentration(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getConc() != null){
         sb.append(floor(this.prodDerive.getConc(), 4));
      }else{
         sb.append("-");
      }

      if(this.prodDerive.getConcUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getConcUnite().getNom());
      }

      return sb.toString();
   }

   public String getStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(prodDerive.getObjetStatut());
   }

   public Integer getNbCessions(){

      return ManagerLocator.getCederObjetManager().findByObjetManager(prodDerive).size();

   }

   /**
    * Récupère la date de création système du produit dérivé.
    * @return Date de création.
    */
   public String getDateCreation(){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(this.prodDerive);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }
      return null;
   }

   public String getCollaborateurNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      final Collaborateur collaborateur = this.prodDerive.getCollaborateur();

      if(collaborateur.getNom() != null && collaborateur.getPrenom() != null){
         sb.append(collaborateur.getNom());
         sb.append(" ");
         sb.append(collaborateur.getPrenom());
      }else if(collaborateur.getNom() != null){
         sb.append(collaborateur.getNom());
      }else if(collaborateur.getPrenom() != null){
         sb.append(collaborateur.getPrenom());
      }
      return sb.toString();
   }

   public String getEmplacementAdrl(){
      if(this.prodDerive != null && this.prodDerive.getProdDeriveId() != null){
         return ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(prodDerive);
      }
      return "";
   }

   public String getEmplacementAdrlinMulti(){
      if(adrlTmp != null){
         return adrlTmp;
      }else if(this.prodDerive != null && this.prodDerive.getProdDeriveId() != null){
         return ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(prodDerive);
      }else{
         return null;
      }
   }

   public String getQualite(){
      if(this.prodDerive.getProdQualite() != null){
         return this.prodDerive.getProdQualite().getNom();
      }
      return null;
   }

   public String getTransformation(){
      if(this.prodDerive.getTransformation() != null){
         final StringBuffer sb = new StringBuffer();

         // Quantite
         if(this.prodDerive.getTransformation().getQuantite() != null){
            sb.append(this.prodDerive.getTransformation().getQuantite());
         }else{
            sb.append("-");
         }

         if(this.prodDerive.getTransformation().getQuantiteUnite() != null){
            sb.append(" ");
            sb.append(this.prodDerive.getTransformation().getQuantiteUnite());
         }
         return sb.toString();
      }
      return null;
   }

   /**
    * Utilise le format de date AbstractFicheController.
    * @return date transformation formattée pour affichage zk
    */
   public String getFormattedDateTransformation(){
      return ObjectTypesFormatters.dateRenderer2(this.prodDerive.getDateTransformation());
   }

   public String getEtatIncomplet(){
      if(this.prodDerive.getEtatIncomplet() != null){
         if(this.prodDerive.getEtatIncomplet().booleanValue() == true){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   public String getArchive(){
      if(this.prodDerive.getArchive() != null){
         if(this.prodDerive.getArchive().booleanValue()){
            return "oui";
         }
         return "non";
      }
      return null;
   }

   /**
    * Decore une liste d'échantillons.
    * @param echans
    * @return Echantillons décorés.
    * Decore une liste de dérivés.
    * @param derives
    * @return Derives décorés.
    */
   public static List<ProdDeriveDecorator2> decorateListe(final List<ProdDerive> derives){
      final List<ProdDeriveDecorator2> liste = new ArrayList<>();
      final Iterator<ProdDerive> it = derives.iterator();
      while(it.hasNext()){
         liste.add(new ProdDeriveDecorator2(it.next()));
      }
      return liste;
   }

   /**
    * Extrait les dérivés d'une liste de Decorator.
    * @param CederObjets
    * @return CederObjets décorés.
    */
   public static List<ProdDerive> extractListe(final List<ProdDeriveDecorator2> derives){
      final List<ProdDerive> liste = new ArrayList<>();
      final Iterator<ProdDeriveDecorator2> it = derives.iterator();

      while(it.hasNext()){
         liste.add(it.next().getProdDerive());
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

      final ProdDeriveDecorator2 deco = (ProdDeriveDecorator2) obj;
      return this.getProdDerive().equals(deco.getProdDerive());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashDerive = 0;

      if(this.prodDerive != null){
         hashDerive = this.prodDerive.hashCode();
      }

      hash = 7 * hash + hashDerive;

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

   public Boolean getCheckedProdDerive(){
      return checkedProdDerive;
   }

   public void setCheckedProdDerive(final Boolean checked){
      this.checkedProdDerive = checked;
   }

   public String getAdrlTmp(){
      return adrlTmp;
   }

   public void setAdrlTmp(final String adrlT){
      this.adrlTmp = adrlT;
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
      getProdDerive().setCode(value);
   }

   public boolean isNew(){
      return isNew;
   }

   /**
    * @since 2.0.13
    * @return température de stockage
    */
   public String getTempStock(){
      if(prodDerive != null){
         final Float temp = ManagerLocator.getConteneurManager().findTempForEmplacementManager(prodDerive.getEmplacement());
         if(temp != null){
            return temp.toString();
         }
      }
      return null;
   }
}
