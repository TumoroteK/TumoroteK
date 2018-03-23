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
package fr.aphp.tumorotek.interfacage.storageRobot;

public class StorageEmplacement implements Comparable<StorageEmplacement>
{

   private String barcode;
   private String conteneurCode;
   private String rack;
   private String boite;
   private String position;

   public StorageEmplacement(final String barcode, final String conteneurCode, final String rack, final String boite,
      final String position){
      super();
      this.barcode = barcode;
      this.conteneurCode = conteneurCode;
      this.rack = rack;
      this.boite = boite;
      this.position = position;
   }

   public String getBarcode(){
      return barcode;
   }

   public void setBarcode(final String _b){
      this.barcode = _b;
   }

   public String getConteneurCode(){
      return conteneurCode;
   }

   public void setConteneurCode(final String _c){
      this.conteneurCode = _c;
   }

   public String getRack(){
      return rack;
   }

   public void setRack(final String _r){
      this.rack = _r;
   }

   public String getBoite(){
      return boite;
   }

   public void setBoite(final String _b){
      this.boite = _b;
   }

   public String getPosition(){
      return position;
   }

   public void setPosition(final String _p){
      this.position = _p;
   }

   @Override
   public int compareTo(final StorageEmplacement sT){
      if(sT != null && isComplete()){
         final int contCompare = conteneurCode.compareTo(sT.getConteneurCode());
         final int rackCompare = rack.compareTo(sT.getRack());
         final int boiteCompare = boite.compareTo(sT.getBoite());
         final int posCompare = position.compareTo(sT.getPosition());
         final int barcodeCompare = barcode.compareTo(sT.getBarcode());
         return contCompare == 0 ? (rackCompare == 0
            ? (boiteCompare == 0 ? (posCompare == 0 ? barcodeCompare : posCompare) : boiteCompare) : rackCompare) : contCompare;
      }
      return 0;
   }

   public boolean isComplete(){
      return getBarcode() != null && getConteneurCode() != null && getRack() != null && getBoite() != null
         && getPosition() != null;
   }
}
