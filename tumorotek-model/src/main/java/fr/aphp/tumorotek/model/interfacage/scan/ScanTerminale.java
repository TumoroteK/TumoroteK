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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table SCAN_DEVICE.
 * Classe créée le 24/04/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "SCAN_TERMINALE")
//@NamedQueries(value = {
//   @NamedQuery(name = "ScanTerminale.findByScanDevice",
//      query = "SELECT s FROM ScanTerminale s WHERE s.scanDevice = ?1 " + "order by s.dateScan DESC"),
//   @NamedQuery(name = "ScanTerminale.findTKObjectCodes",
//      query = "SELECT t.code FROM ScanTube t WHERE t.scanTerminale = ?1 " + "and t.code is not null order by t.code")
//   //	@NamedQuery(name = "ScanTerminale.findTKObjectEmplacements", 
//   //		query = "SELECT e FROM Emplacement e WHERE t.scanTerminale = ? "
//   //		+ "and t.code is not null order by t.code"),
//})
public class ScanTerminale implements java.io.Serializable
{

   private static final long serialVersionUID = 1035198779653396102L;

   private Integer scanTerminaleId;
   private ScanDevice scanDevice;
   private String name;
   private Calendar dateScan;

   private Integer width;
   private Integer height;

   private List<ScanTube> scanTubes = new ArrayList<>();

   public ScanTerminale(){
      super();
   }

   @Id
   @Column(name = "SCAN_TERMINALE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getScanTerminaleId(){
      return scanTerminaleId;
   }

   public void setScanTerminaleId(final Integer _i){
      this.scanTerminaleId = _i;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "SCAN_DEVICE_ID", nullable = false)
   public ScanDevice getScanDevice(){
      return scanDevice;
   }

   public void setScanDevice(final ScanDevice _s){
      this.scanDevice = _s;
   }

   @Column(name = "NAME", nullable = false)
   public String getName(){
      return name;
   }

   public void setName(final String _n){
      this.name = _n;
   }

   @Column(name = "DATE_SCAN", nullable = true)
   public Calendar getDateScan(){
      return dateScan;
   }

   public void setDateScan(final Calendar _c){
      this.dateScan = _c;
   }

   @OneToMany(mappedBy = "scanTerminale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @OrderBy("cell")
   public List<ScanTube> getScanTubes(){
      return scanTubes;
   }

   public void setScanTubes(final List<ScanTube> _s){
      this.scanTubes = _s;
   }

   @Column(name = "WIDTH", nullable = false)
   public Integer getWidth(){
      return width;
   }

   public void setWidth(final Integer width){
      this.width = width;
   }

   @Column(name = "HEIGHT", nullable = false)
   public Integer getHeight(){
      return height;
   }

   public void setHeight(final Integer height){
      this.height = height;
   }

   public void addTube(final ScanTube _s){
      if(!getScanTubes().contains(_s)){
         getScanTubes().add(_s);
         _s.setScanTerminale(this);
      }
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ScanTerminale test = (ScanTerminale) obj;
      return ((this.name == test.name || (this.name != null && this.name.equals(test.name)))
         && (this.scanDevice == test.scanDevice || (this.scanDevice != null && this.scanDevice.equals(test.scanDevice)))
         && (this.dateScan == test.dateScan || (this.dateScan != null && this.dateScan.equals(test.dateScan))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashName = 0;
      int hashDevice = 0;
      int hashDateScan = 0;

      if(this.name != null){
         hashName = this.name.hashCode();
      }
      if(this.scanDevice != null){
         hashDevice = this.scanDevice.hashCode();
      }
      if(this.dateScan != null){
         hashDateScan = this.dateScan.hashCode();
      }

      hash = 7 * hash + hashName;
      hash = 7 * hash + hashDevice;
      hash = 7 * hash + hashDateScan;

      return hash;
   }

   @Transient
   public int getNbTubesStored(){
      int tot = 0;
      for(final ScanTube scanTube : scanTubes){
         if(!scanTube.isEmpty()){
            tot++;
         }
      }
      return tot;
   }

}
