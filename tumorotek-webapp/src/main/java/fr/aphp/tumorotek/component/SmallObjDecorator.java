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
package fr.aphp.tumorotek.component;

/**
 * Classe 'Decorateur' qui decore un objet pour le rendre utilisable
 * par SmallObjsEditableGrid..
 * date: 12/07/2010
 *
 * @version 2.0
 * @author mathieu BARTHELEMY
 *
 */
public abstract class SmallObjDecorator
{

   private Object obj;

   // flag qui specifie si l'objet a ete valide (ou en cours de creation)
   private boolean validated = false;
   private boolean edition = false;

   private Integer ordre;
   private Integer ordreInit;

   public SmallObjDecorator(){}

   public SmallObjDecorator(final Object object){
      setObj(object);
   }

   public void setObj(final Object o){
      this.obj = o;
   }

   public Object getObj(){
      return this.obj;
   }

   public boolean isValidated(){
      return validated;
   }

   public void setValidated(final boolean v){
      this.validated = v;
   }

   public boolean getEdition(){
      return edition;
   }

   public boolean getStatique(){
      return !edition;
   }

   public void setEdition(final boolean b){
      this.edition = b;
   }

   @Override
   public boolean equals(final Object deco2){

      if(this == deco2){
         return true;
      }

      if((deco2 == null) || deco2.getClass() != this.getClass()){
         return false;
      }

      final SmallObjDecorator deco = (SmallObjDecorator) deco2;
      return this.getObj().equals(deco.getObj()) && this.getEdition() == deco.getEdition();

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashObj = 0;
      final int hashEdition = new Boolean(edition).hashCode();

      if(this.obj != null){
         hashObj = this.obj.hashCode();
      }

      hash = 7 * hash + hashObj;
      hash = 7 * hash + hashEdition;

      return hash;
   }

   public abstract Integer getObjDbId();

   public abstract Object getObjClone();

   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   public Integer getOrdreInit(){
      return ordreInit;
   }

   public void setOrdreInit(final Integer oI){
      this.ordreInit = oI;
   }

   /**
    * Indique si l'ordre du decorator a change par rapport au code de l'objet 
    * sous jacent.
    * @return true si chgt
    */
   public boolean ordreChanged(){
      return getOrdreInit() != this.getOrdre();
   }

   /**
    * Passe l'ordre du decorateur à l'objet sous-jacent.
    * sous jacent.
    */
   public abstract void syncOrdre();
}
