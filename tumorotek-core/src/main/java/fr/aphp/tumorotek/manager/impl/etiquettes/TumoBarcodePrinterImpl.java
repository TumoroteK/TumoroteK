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
package fr.aphp.tumorotek.manager.impl.etiquettes;

import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.manager.etiquettes.TumoBarcodePrinter;
import fr.aphp.tumorotek.manager.etiquettes.TumoPrinterUtilsManager;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

public class TumoBarcodePrinterImpl implements TumoBarcodePrinter
{

   private TumoPrinterUtilsManager tumoPrinterUtilsManager;
   // private LigneEtiquetteManager ligneEtiquetteManager;
   private final ComponentPrinter componentPrinter = new ComponentPrinter();

   public TumoBarcodePrinterImpl(){

   }

   public void setTumoPrinterUtilsManager(final TumoPrinterUtilsManager tManager){
      this.tumoPrinterUtilsManager = tManager;
   }

   //	public void setLigneEtiquetteManager(LigneEtiquetteManager lManager) {
   //		this.ligneEtiquetteManager = lManager;
   //	}

   /**
    * Fonction qui effectue l'impression des etiquettes codes a barres d'une
    * liste de patients.
    */
   public int printPatient(final List<Patient> patients, final int nb){
      return 0;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes codes a barres d'une
    * liste de prelevements.
    */
   public int printPrelevement(final List<Prelevement> prelevements, final int nb){
      return 0;
   }

   //	/**
   //	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une
   //	 * liste d'echantillons.
   //	 * 
   //	 * @param List
   //	 *            la liste des echantillons a imprimer
   //	 * @param int nb le nombre d impressions a effectuer par echantillon
   //	 * @return int le code retour pour savoir si l impression s est correctement
   //	 *         deroulee si codeRetour = 0, l impression s est mal passee si
   //	 *         codeRetour = 1, l impression s est bien passee
   //	 * @throws PrinterException 
   //	 */
   //	public int printEchantillon(List<Echantillon> echantillons, int nb,
   //			Imprimante imprimante, Modele modele, String rawLang) throws PrinterException {
   //		boolean isDefault = true;
   //		List<LigneEtiquette> lignes = null;
   //		List<ComponentBarcodeLabel> components = new ArrayList<ComponentBarcodeLabel>();
   //		if (modele != null && modele.getIsDefault() != null) {
   //			isDefault = modele.getIsDefault();
   //		}
   //		int codeRetour = 0;
   //		int i = 0;
   //		for (i = 0; i < echantillons.size(); i++) {
   //			Echantillon echan = echantillons.get(i);
   //			if (isDefault) {
   //				lignes = tumoPrinterUtilsManager.createListEtiquette(echan,
   //						modele);
   //				// tumoPrinterUtilsManager.extractStaticDataForEchantillon(echan);
   //			} else {
   //				// lignes = ligneEtiquetteManager.findByModeleManager(modele);
   //				lignes = tumoPrinterUtilsManager.extractDynData(echan, modele);
   //			}
   //			try {
   //				components.add(ComponentPrinter.getComponent(lignes, imprimante,
   //														modele, echan.getCode()));
   //			} catch (Exception e) {
   //				e.printStackTrace();
   //			}
   //		}
   //		codeRetour = componentPrinter.printComponents(components, nb,
   //				imprimante, rawLang);
   //		return codeRetour;
   //	}
   //
   //	/**
   //	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une
   //	 * liste de derives.
   //	 * @throws PrinterException 
   //	 */
   //	public int printDerive(List<ProdDerive> derives, int nb,
   //			Imprimante imprimante, Modele modele, String rawLang) 
   //					throws PrinterException {
   //		boolean isDefault = true;
   //		List<LigneEtiquette> lignes = null;
   //		List<ComponentBarcodeLabel> components = new ArrayList<ComponentBarcodeLabel>();
   //
   //		if (modele != null && modele.getIsDefault() != null) {
   //			isDefault = modele.getIsDefault();
   //		}
   //
   //		int codeRetour = 0;
   //		int i = 0;
   //		for (i = 0; i < derives.size(); i++) {
   //			ProdDerive derive = derives.get(i);
   //			if (isDefault) {
   //				lignes = tumoPrinterUtilsManager.createListEtiquette(derive,
   //						modele);
   //			} else {
   //				lignes = tumoPrinterUtilsManager.extractDynData(derive, modele);
   //			}
   //			components.add(ComponentPrinter.getComponent(lignes, imprimante,
   //					modele, derive.getCode()));
   //		}
   //		codeRetour = componentPrinter.printComponents(components, nb,
   //				imprimante, rawLang);
   //		return codeRetour;
   //	}
   //
   //	/**
   //	 * Fonction qui effectue l'impression des etiquettes codes a barres d'une
   //	 * liste de cessions.
   //	 */
   //	public int printCession(List<Cession> cessions, int nb) {
   //		return 0;
   //	}

   //	@Override
   //	public int printData(List<LigneEtiquette> l, int nb, Imprimante imprimante,
   //			Modele modele, String rawLang) throws PrinterException {
   //		int codeRetour = 0;
   //		List<LigneEtiquette> lignes = new ArrayList<LigneEtiquette>();
   //		if (l != null) {
   //			lignes = l;
   //		} else if (modele != null && modele.getModeleId() != null) {
   //			lignes = ligneEtiquetteManager.findByModeleManager(modele);
   //		}
   //
   //		codeRetour = componentPrinter.print(lignes, nb, imprimante, modele, rawLang);
   //
   //		return codeRetour;
   //	}
   //	
   //
   //	@Override
   //	public int printListData(List<List<LigneEtiquette>> l,
   //			Imprimante imprimante, Modele modele, String rawLang) 
   //					throws PrinterException {
   //		int codeRetour = 0;
   //
   //		codeRetour = componentPrinter.printList(l, 1, imprimante, modele, rawLang);
   //
   //		return codeRetour;
   //	}

   @Override
   public int printListCopiesData(final List<List<LigneEtiquette>> liste, final int nb, final Imprimante imprimante,
      final Modele modele, final String rawLang, final BarcodeFieldDefault by) throws PrinterException{
      int codeRetour = 0;

      final List<ComponentBarcodeLabel> components = new ArrayList<>();
      for(int i = 0; i < liste.size(); i++){
         int j = 1;
         while(j <= nb){
            components.add(ComponentPrinter.getComponent(liste.get(i), imprimante, modele, null));
            j++;
         }
      }
      codeRetour = componentPrinter.printComponents(components, imprimante, rawLang, by);
      return codeRetour;

      //		if (l != null) {
      //			for (List<LigneEtiquette> listeValeurs : l) {
      //				codeRetour = printData(listeValeurs, nb, imprimante, modele, rawLang);
      //			}
      //		}

      //		return codeRetour;
   }

   @Override
   public int printStockableObjects(final List<? extends TKStockableObject> objects, final int nb, final Imprimante imp,
      final Modele mod, final String rawLang, final BarcodeFieldDefault by) throws PrinterException{
      boolean isDefault = true;
      List<LigneEtiquette> lignes = null;
      final List<ComponentBarcodeLabel> components = new ArrayList<>();
      if(mod != null && mod.getIsDefault() != null){
         isDefault = mod.getIsDefault();
      }
      int codeRetour = 0;
      int i = 0;
      for(i = 0; i < objects.size(); i++){
         final TKStockableObject obj = objects.get(i);
         if(isDefault){
            lignes = tumoPrinterUtilsManager.createListEtiquette(obj, mod);
         }else{
            lignes = tumoPrinterUtilsManager.extractDynData(obj, mod);
         }
         for(int j = 1; j <= nb; j++){
            components.add(ComponentPrinter.getComponent(lignes, imp, mod, obj.getCode()));
         }
      }
      codeRetour = componentPrinter.printComponents(components, //nb, 
         imp, rawLang, by);
      return codeRetour;
   }
}
