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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.etiquettes.TumoPrinterUtilsManager;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.io.ExtractValueFromChampManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Gestion des données etiquettes dynamique et statique créé le 12/08/2012.
 *
 * @author Julien Husson
 * @version 2.0.8
 *
 */
public class TumoPrinterUtilsManagerImpl implements TumoPrinterUtilsManager
{

   private static final String DERIVE = "ProdDerive";

   private static final String ECHANTILLON = "Echantillon";

   private EchantillonManager echantillonManager;

   private ProdDeriveManager prodDeriveManager;

   private LigneEtiquetteManager ligneEtiquetteManager;

   private ChampLigneEtiquetteManager champLigneEtiquetteManager;

   private EntiteManager entiteManager;

   private ExtractValueFromChampManager extractValueFromChampManager;

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setLigneEtiquetteManager(final LigneEtiquetteManager lManager){
      this.ligneEtiquetteManager = lManager;
   }

   public void setChampLigneEtiquetteManager(final ChampLigneEtiquetteManager cManager){
      this.champLigneEtiquetteManager = cManager;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setExtractValueFromChampManager(final ExtractValueFromChampManager eManager){
      this.extractValueFromChampManager = eManager;
   }

   @Override
   public List<LigneEtiquette> extractDynData(final TKStockableObject obj, final Modele modele){
      List<LigneEtiquette> db_lignes = null;
      final List<LigneEtiquette> dyn_lignes = new ArrayList<>();
      LigneEtiquette ligne = null;
      if(obj != null && modele != null){
         db_lignes = ligneEtiquetteManager.findByModeleManager(modele);
         for(int i = 0; i < db_lignes.size(); i++){
            ligne = new LigneEtiquette();
            // on ajoute d'abord l'entete
            if(db_lignes.get(i).getEntete() != null){
               ligne.setEntete(db_lignes.get(i).getEntete());
            }else{
               ligne.setEntete("");
            }
            String sb = new String();
            // on ajoute le contenu stocké dans les champsEtiquette
            if(db_lignes.get(i).getContenu() == null){
               // sinon, on va rechercher les champs
               final List<ChampLigneEtiquette> champs = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(
                  db_lignes.get(i), entiteManager.findByNomManager(obj.getClass().getSimpleName()).get(0));

               final StringBuilder contenu = new StringBuilder();
               // on va parcourir les champs
               for(int j = 0; j < champs.size(); j++){
                  // extraction de la valeur pour le champ
                  if(obj.entiteNom().equals(DERIVE)){
                     sb = extractValueFromChampManager.extractValueForChampManager((ProdDerive) obj, champs.get(j).getChamp());
                  }else if((obj.entiteNom().equals(ECHANTILLON))){
                     sb = extractValueFromChampManager.extractValueForChampManager((Echantillon) obj, champs.get(j).getChamp());
                  }
                  // formatage de la valeur
                  if(contenu.length() > 0){
                     contenu.append(" ");
                  }
                  contenu.append(Utils.formateString(sb, champs.get(j).getExpReg()));
               }
               ligne.setContenu(contenu.length() > 0 ? contenu.toString() : null);
            }else{
               ligne.setContenu(db_lignes.get(i).getContenu());
            }
            if(ligne.getContenu() == null || ligne.getContenu().contains("null")){
               ligne.setContenu("");
            }
            // Barcode?
            ligne.setIsBarcode(db_lignes.get(i).getIsBarcode());
            // size font, style
            ligne.setSize(db_lignes.get(i).getSize());
            ligne.setStyle(db_lignes.get(i).getStyle());
            ligne.setFont(db_lignes.get(i).getFont());
            dyn_lignes.add(ligne);
         }
      }
      return dyn_lignes;
   }

   @Override
   public List<LigneEtiquette> createListEtiquette(final TKStockableObject obj, final Modele modele){
      final List<LigneEtiquette> ligneEtiquette = new ArrayList<>();

      String param1, param2, param3, param4, param5, param6 = "";
      LigneEtiquette l1, l2, l3, l4, l5, l6, l7, l8, l9;

      param1 = PrinterFormat.extraireCodePrelevement(obj.getCode());
      param2 = PrinterFormat.extraireNumeroTube(obj.getCode());
      param3 = obj.getType().getNom();

      l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu(param1);

      l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setEntete(ConfigManager.ENTETE_PRELEVEMENT);
      l2.setStyle(ConfigManager.BOLD_FONT_STYLE);
      l2.setSize(6);
      l2.setContenu(param1);

      l3 = new LigneEtiquette();
      l3.setIsBarcode(true);
      l3.setContenu(param2);

      l4 = new LigneEtiquette();
      l4.setIsBarcode(false);
      l4.setEntete(ConfigManager.ENTETE_TUBE);
      l4.setStyle(ConfigManager.BOLD_FONT_STYLE);
      l4.setSize(6);
      l4.setContenu(param2);

      l5 = new LigneEtiquette();
      l5.setIsBarcode(false);
      l5.setEntete(ConfigManager.ENTETE_TYPE);
      l5.setContenu(param3);

      String nom = "";
      if(obj.entiteNom().equals(DERIVE)){
         final Patient pat = prodDeriveManager.getPatientParentManager((ProdDerive) obj);
         if(pat != null){
            nom = pat.getNom();
         }
         param4 = PrinterFormat.formaterNom(nom);
      }else if(obj.entiteNom().equals(ECHANTILLON)){
         final Prelevement prlvt = echantillonManager.getPrelevementManager((Echantillon) obj);
         if(prlvt != null && prlvt.getMaladie() != null && prlvt.getMaladie().getPatient() != null){
            nom = prlvt.getMaladie().getPatient().getNom();
         }
      }
      param4 = PrinterFormat.formaterNom(nom);
      l6 = new LigneEtiquette();
      l6.setIsBarcode(false);
      l6.setEntete(ConfigManager.ENTETE_PATIENT);
      l6.setContenu(param4);

      String date = "";
      if(obj.getDateStock() != null){
         date = new SimpleDateFormat("dd/MM/yyyy").format(obj.getDateStock().getTime());
      }
      param5 = date;
      l7 = new LigneEtiquette();
      l7.setIsBarcode(false);
      l7.setEntete(ConfigManager.ENTETE_DATE_CONGELATION);
      l7.setContenu(param5);

      String qte = "";
      if(obj.getQuantite() != null && obj.getQuantiteUnite() != null){
         qte = String.valueOf(Math.round(obj.getQuantite() * 100.0) / 100.0) + " "
            + PrinterFormat.formaterUnite(obj.getQuantiteUnite().getUnite());

      }
      param6 = qte;
      l8 = new LigneEtiquette();
      l8.setIsBarcode(false);
      l8.setEntete(ConfigManager.ENTETE_QUANTITE);
      l8.setContenu(param6);

      ligneEtiquette.add(l1);
      ligneEtiquette.add(l2);
      ligneEtiquette.add(l3);
      ligneEtiquette.add(l4);
      ligneEtiquette.add(l5);
      ligneEtiquette.add(l6);
      ligneEtiquette.add(l7);
      ligneEtiquette.add(l8);

      // modele texte libre
      l9 = new LigneEtiquette();
      l9.setIsBarcode(false);
      l9.setEntete("");
      l9.setSize(5);
      if(modele != null && modele.getTexteLibre() != null){
         l9.setContenu(modele.getTexteLibre());
         ligneEtiquette.add(l9);
      }

      return ligneEtiquette;
   }

   @Override
   public Vector<String> extractStaticDataForMBio(final TKStockableObject obj){

      final Vector<String> data = new Vector<>();
      String param1 = "";
      String param2 = "";
      String param3 = "";
      String param4 = "";
      String param5 = "";
      String param6 = "";

      param1 = PrinterFormat.extraireCodePrelevement(obj.getCode());
      param2 = PrinterFormat.extraireNumeroTube(obj.getCode());
      param3 = obj.getType().getNom();

      String nom = "";
      if(obj.entiteNom().equals(DERIVE)){
         final Patient pat = prodDeriveManager.getPatientParentManager((ProdDerive) obj);
         if(pat != null){
            nom = pat.getNom();
         }
         param4 = PrinterFormat.formaterNom(nom);
      }else if(obj.entiteNom().equals(ECHANTILLON)){
         final Prelevement prlvt = echantillonManager.getPrelevementManager((Echantillon) obj);
         if(prlvt != null && prlvt.getMaladie() != null && prlvt.getMaladie().getPatient() != null){
            nom = prlvt.getMaladie().getPatient().getNom();
         }
      }
      param4 = PrinterFormat.formaterNom(nom);

      String date = "";
      if(obj.getDateStock() != null){
         date = new SimpleDateFormat("dd/MM/yyyy").format(obj.getDateStock().getTime());
      }
      param5 = date;
      String qte = "";
      if(obj.getQuantite() != null && obj.getQuantiteUnite() != null){
         qte = String.valueOf(Math.round(obj.getQuantite() * 100.0) / 100.0) + " "
            + PrinterFormat.formaterUnite(obj.getQuantiteUnite().getUnite());
      }
      param6 = qte;

      data.add(param1);
      data.add(param2);
      data.add(param3);
      data.add(param4);
      data.add(param5);
      data.add(param6);

      return data;
   }

}
