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
package fr.aphp.tumorotek.decorator;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * Classe permettant de décorer un Emplacement.
 * @author Pierre Ventadour.
 *
 */
public class EmplacementDecorator
{

   private Emplacement emplacement;
   private Boolean vide;
   //	private Echantillon echantillon;
   //	private ProdDerive prodDerive;
   private TKStockableObject tkStockObj;
   private Integer position;
   private Terminale terminale;
   private String libelle;
   private String code = "";
   private String adrl = "";
   private String adrlDestination;
   private EmplacementDecorator emplDestination;
   private Boolean emplacementDepart = false;
   private String type = "";

   public EmplacementDecorator(final Emplacement emp){
      if(emp.equals(new Emplacement())){
         emplacement = new Emplacement();
         vide = true;
         libelle = "";
         adrl = "";
      }else{
         emplacement = emp;
         vide = emplacement.getVide();
         position = emplacement.getPosition();
         adrl = emplacement.getAdrl();
      }
   }

   /**
    * Méthode générant le libellé à afficher sur un emplacement.
    */
   public void generateLibelle(final String adrlTerminale, final Terminale t){
      final StringBuffer sb = new StringBuffer();
      sb.append(adrlTerminale);
      sb.append(".");
      sb.append(ManagerLocator.getEmplacementManager().getNumerotationByPositionAndTerminaleManager(position, t));
      adrl = sb.toString();
      if(!vide){
         sb.append(" : ");
         sb.append(code);
      }

      this.libelle = sb.toString();

   }

   public Emplacement getEmplacement(){
      return emplacement;
   }

   public void setEmplacement(final Emplacement emp){
      this.emplacement = emp;
   }

   public Boolean getVide(){
      return vide;
   }

   public void setVide(final Boolean v){
      this.vide = v;
   }

   //	public Echantillon getEchantillon() {
   //		return echantillon;
   //	}
   //	public void setEchantillon(Echantillon e) {
   //		this.echantillon = e;
   //	}
   //	public ProdDerive getProdDerive() {
   //		return prodDerive;
   //	}
   //	public void setProdDerive(ProdDerive p) {
   //		this.prodDerive = p;
   //	}
   public Integer getPosition(){
      return position;
   }

   public void setPosition(final Integer p){
      this.position = p;
   }

   public String getLibelle(){
      return libelle;
   }

   public void setLibelle(final String l){
      this.libelle = l;
   }

   public String getCode(){
      return code;
   }

   public void setCode(final String c){
      this.code = c;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final EmplacementDecorator deco = (EmplacementDecorator) obj;
      return this.getEmplacement().equals(deco.getEmplacement());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEmplacement = 0;

      if(this.emplacement != null){
         hashEmplacement = this.emplacement.hashCode();
      }

      hash = 7 * hash + hashEmplacement;

      return hash;
   }

   @Override
   public String toString(){
      if(this.emplacement != null){
         return this.emplacement.toString();
      }
      return "{Empty Emplacement}";
   }

   public String getAdrl(){
      return adrl;
   }

   public void setAdrl(final String a){
      this.adrl = a;
   }

   public String getAdrlDestination(){
      return adrlDestination;
   }

   public void setAdrlDestination(final String adrlDest){
      this.adrlDestination = adrlDest;
   }

   public EmplacementDecorator getEmplDestination(){
      return emplDestination;
   }

   public void setEmplDestination(final EmplacementDecorator emplDest){
      this.emplDestination = emplDest;
   }

   public Boolean getEmplacementDepart(){
      return emplacementDepart;
   }

   public void setEmplacementDepart(final Boolean emplacementDep){
      this.emplacementDepart = emplacementDep;
   }

   public Terminale getTerminale(){
      return terminale;
   }

   public void setTerminale(final Terminale t){
      this.terminale = t;
   }

   public String getType(){
      return type;
   }

   public void setType(final String t){
      this.type = t;
   }

   public ObjetStatut getObjectStatut(){
      if(tkStockObj != null){
         return tkStockObj.getObjetStatut();
      }
      return null;
   }

   public TKStockableObject getTkStockObj(){
      return tkStockObj;
   }

   public void setTkStockObj(final TKStockableObject tkStockObj){
      this.tkStockObj = tkStockObj;
   }

}
