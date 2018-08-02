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
package fr.aphp.tumorotek.manager.interfacage.scan;

import java.util.HashMap;
import java.util.Map;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Objet de transfert contenant l'association entre le scan,
 * la terminale correspondante dans le système de stockage,
 * et l'association entre les emplacements et les tubes d'intérêts
 * pour le contexte du scan (emplacements à occuper ou à libérer).
 *
 * Interface créée le 06/06/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class TKScanTerminaleDTO
{

   private ScanTerminale scanTerminale;
   private Terminale terminale;
   private Map<ScanTube, Emplacement> emplacementsToFill = new HashMap<>();
   private Map<ScanTube, TKStockableObject> emplacementsToFree = new HashMap<>();
   private Map<ScanTube, TKStockableObject> emplacementsMismatch = new HashMap<>();

   public TKScanTerminaleDTO(){}

   public ScanTerminale getScanTerminale(){
      return scanTerminale;
   }

   public void setScanTerminale(final ScanTerminale _s){
      this.scanTerminale = _s;
   }

   public Terminale getTerminale(){
      return terminale;
   }

   public void setTerminale(final Terminale _t){
      this.terminale = _t;
   }

   public Map<ScanTube, Emplacement> getEmplacementsToFill(){
      return emplacementsToFill;
   }

   public void setEmplacementsToFill(final Map<ScanTube, Emplacement> _e){
      this.emplacementsToFill = _e;
   }

   public Map<ScanTube, TKStockableObject> getEmplacementsToFree(){
      return emplacementsToFree;
   }

   public void setEmplacementsToFree(final Map<ScanTube, TKStockableObject> _e){
      this.emplacementsToFree = _e;
   }

   public Map<ScanTube, TKStockableObject> getEmplacementsMismatch(){
      return emplacementsMismatch;
   }

   public void setEmplacementsMismatch(final Map<ScanTube, TKStockableObject> _e){
      this.emplacementsMismatch = _e;
   }
}
