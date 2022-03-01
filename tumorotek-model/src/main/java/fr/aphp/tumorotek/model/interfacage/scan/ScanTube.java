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
package fr.aphp.tumorotek.model.interfacage.scan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table SCAN_DEVICE.
 * Classe créée le 24/04/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
@Entity
@Table(name = "SCAN_TUBE")
public class ScanTube implements java.io.Serializable
{

   private static final long serialVersionUID = 1035198779653396102L;

   private Integer scanTubeId;
   private ScanTerminale scanTerminale;
   private String code;
   private String cell;
   private String row;
   private String col;

   public ScanTube(){
      super();
   }

   @Id
   @Column(name = "SCAN_TUBE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getScanTubeId(){
      return scanTubeId;
   }

   public void setScanTubeId(final Integer _i){
      this.scanTubeId = _i;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "SCAN_TERMINALE_ID", nullable = false)
   public ScanTerminale getScanTerminale(){
      return scanTerminale;
   }

   public void setScanTerminale(final ScanTerminale _s){
      this.scanTerminale = _s;
   }

   @Column(name = "CODE", nullable = true)
   public String getCode(){
      return code;
   }

   public void setCode(final String _c){
      this.code = _c;
   }

   @Column(name = "CELL", nullable = false)
   public String getCell(){
      return cell;
   }

   public void setCell(final String _c){
      this.cell = _c;
   }

   @Column(name = "ROW", nullable = false)
   public String getRow(){
      return row;
   }

   public void setRow(final String _r){
      this.row = _r;
   }

   @Column(name = "COL", nullable = false)
   public String getCol(){
      return col;
   }

   public void setCol(final String _c){
      this.col = _c;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ScanTube test = (ScanTube) obj;
      return ((this.scanTerminale == test.scanTerminale
         || (this.scanTerminale != null && this.scanTerminale.equals(test.scanTerminale)))
         && (this.cell == test.cell || (this.cell != null && this.cell.equals(test.cell))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashTerminale = 0;
      int hashCell = 0;

      if(this.scanTerminale != null){
         hashTerminale = this.scanTerminale.hashCode();
      }
      if(this.cell != null){
         hashCell = this.cell.hashCode();
      }

      hash = 7 * hash + hashTerminale;
      hash = 7 * hash + hashCell;

      return hash;
   }

   @Transient
   public int getPosition(){
      int pos = 0;
      if(row != null && col != null){
         pos = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(row)) * getScanTerminale().getWidth() + new Integer(col);
      }
      return pos;
   }

   @Transient
   public boolean isEmpty(){
      return code == null;
   }

}
