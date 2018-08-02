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
package fr.aphp.tumorotek.manager.impl.io.imports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * Classe utilitaire permettant la création de lots de dérivés à partir de la lecture
 * d'un fichier d'import. Le lot est charactérisé par le parent d'origine de la transformation,
 * la quantité utilisée et la date qualifiant l'évènement de stockage (quand le parent est un
 * TKStockableObjet echantillon/dérivé)
 *
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 * @since 2.0.12
 *
 */
public class DerivesImportBatches
{

   private TKAnnotableObject parent;

   private Float transfoQte;
   private Unite transfoQteUnite;
   private String observations;
   private List<ProdDerive> derives = new ArrayList<>();
   private Calendar dateSortie;
   private Hashtable<ProdDerive, Row> importRow = new Hashtable<>();
   private Hashtable<ProdDerive, List<AnnotationValeur>> annoVals = new Hashtable<>();
   private Hashtable<ProdDerive, Emplacement> emplacements = new Hashtable<>();
   private Hashtable<ProdDerive, List<NonConformite>> ncfsTrait = new Hashtable<>();
   private Hashtable<ProdDerive, List<NonConformite>> ncfsCess = new Hashtable<>();

   public DerivesImportBatches(final TKAnnotableObject parent, final Float transfoQte, final String observations,
      final Calendar dateSortie){
      super();
      this.parent = parent;
      this.transfoQte = transfoQte;

      // recupération automatique de la bonne unite
      if(parent != null){
         if(parent instanceof TKStockableObject){
            transfoQteUnite = ((TKStockableObject) parent).getQuantiteUnite();
         }else{
            transfoQteUnite = ((Prelevement) parent).getQuantiteUnite();
         }
      }

      this.observations = observations;
      this.dateSortie = dateSortie;
   }

   public TKAnnotableObject getParent(){
      return parent;
   }

   public void setParent(final TKAnnotableObject parent){
      this.parent = parent;
   }

   public Float getTransfoQte(){
      return transfoQte;
   }

   public void setTransfoQte(final Float transfoQte){
      this.transfoQte = transfoQte;
   }

   public Unite getTransfoQteUnite(){
      return transfoQteUnite;
   }

   public void setTransfoQteUnite(final Unite transfoQteUnite){
      this.transfoQteUnite = transfoQteUnite;
   }

   public String getObservations(){
      return observations;
   }

   public void setObservations(final String observations){
      this.observations = observations;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }

   public void setDerives(final List<ProdDerive> derives){
      this.derives = derives;
   }

   public Calendar getDateSortie(){
      return dateSortie;
   }

   public void setDateSortie(final Calendar dateSortie){
      this.dateSortie = dateSortie;
   }

   public Hashtable<ProdDerive, List<AnnotationValeur>> getAnnoVals(){
      return annoVals;
   }

   public void setAnnoVals(final Hashtable<ProdDerive, List<AnnotationValeur>> annoVals){
      this.annoVals = annoVals;
   }

   public Hashtable<ProdDerive, Emplacement> getEmplacements(){
      return emplacements;
   }

   public void setEmplacements(final Hashtable<ProdDerive, Emplacement> emplacements){
      this.emplacements = emplacements;
   }

   public Hashtable<ProdDerive, List<NonConformite>> getNcfsTrait(){
      return ncfsTrait;
   }

   public void setNcfsTrait(final Hashtable<ProdDerive, List<NonConformite>> ncfsTrait){
      this.ncfsTrait = ncfsTrait;
   }

   public Hashtable<ProdDerive, List<NonConformite>> getNcfsCess(){
      return ncfsCess;
   }

   public void setNcfsCess(final Hashtable<ProdDerive, List<NonConformite>> ncfsCess){
      this.ncfsCess = ncfsCess;
   }

   public Hashtable<ProdDerive, Row> getImportRow(){
      return importRow;
   }

   public void setImportRow(final Hashtable<ProdDerive, Row> importRow){
      this.importRow = importRow;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final DerivesImportBatches test = (DerivesImportBatches) obj;
      return ((this.parent == test.parent || (this.parent != null && this.parent.equals(test.parent)))
         && (this.transfoQte == test.transfoQte || (this.transfoQte != null && this.transfoQte.equals(test.transfoQte)))
         // && (this.transfoQteUnite == test.transfoQteUnite 
         //	|| (this.transfoQteUnite != null 
         //	&& this.transfoQteUnite.equals(test.transfoQteUnite)))
         && (this.dateSortie == test.dateSortie || (this.dateSortie != null && this.dateSortie.equals(test.dateSortie))));
   }

   /**
    * Le hashcode est calculé sur les attributs code et banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashParent = 0;
      int hashTfQuantite = 0;
      // int hashTfQuantiteUnite = 0;
      int hashDateSortie = 0;

      if(this.parent != null){
         hashParent = this.parent.hashCode();
      }
      if(this.transfoQte != null){
         hashTfQuantite = this.transfoQte.hashCode();
      }
      //		if (this.transfoQteUnite != null) {
      //			hashTfQuantiteUnite = this.transfoQteUnite.hashCode();
      //		}
      if(this.dateSortie != null){
         hashDateSortie = this.dateSortie.hashCode();
      }

      hash = 31 * hash + hashParent;
      hash = 31 * hash + hashTfQuantite;
      //	hash = 31 * hash + hashTfQuantiteUnite;
      hash = 31 * hash + hashDateSortie;

      return hash;

   }

}
