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
package fr.aphp.tumorotek.action.utils;

import java.util.Iterator;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 * Utility class fournissant les methodes récupérant et formattant les valeurs
 * de Prelevement pour un affichage particulier dans l'interface.
 * Date: 26/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.9
 */
public final class PrelevementUtils
{

   private PrelevementUtils(){}

   /**
    * Affiche le Nom et Prenom du patient concaténé.
    * @param prelevement
    * @return String
    */
   public static String getPatientNomAndPrenom(final Prelevement prel){
      final StringBuffer sb = new StringBuffer();

      final Maladie maladie = prel.getMaladie();

      if(maladie != null && maladie.getPatient() != null){
         final Patient pat = maladie.getPatient();

         if(pat.getNom() != null && pat.getPrenom() != null){
            sb.append(pat.getNom());
            sb.append(" ");
            sb.append(pat.getPrenom());
         }else if(pat.getNom() != null){
            sb.append(pat.getNom());
         }else if(pat.getPrenom() != null){
            sb.append(pat.getPrenom());
         }
      }
      return sb.toString();
   }

   /**
    * Recupere le nombre d'echantillons restants.
    * @param Prelevement
    * @return x restants
    */
   public static int getNbEchanRestants(final Prelevement prel){
      return ManagerLocator.getEchantillonManager().findCountRestantsByPrelevementManager(prel).intValue();
   }

   /**
    * Recupere les echantillons restants et totals pour concaténer
    * sous la forme total (x restants).
    * @param Prelevement
    * @return y (x restants)
    */
   public static String getNbEchanRestantsSurTotalEtStockes(final Prelevement prel){
      final StringBuffer sb = new StringBuffer();
      sb.append(getNbEchanRestants(prel));
      sb.append(" / ");
      sb.append(ManagerLocator.getEchantillonManager().findCountByPrelevementManager(prel).intValue());

      sb.append(" (");
      sb.append(getNbEchanStockesEtReserves(prel));
      sb.append(")");

      return sb.toString();
   }

   /**
    * Recupere le nombre d'echantillons stockés et réservés.
    * @param Prelevement
    * @return x stockés
    */
   public static int getNbEchanStockesEtReserves(final Prelevement prel){
      return ManagerLocator.getEchantillonManager().findCountByPrelevementAndStockeReserveManager(prel).intValue();
   }

   /**
    * Recupere les echantillons stockés et totaux pour concaténer
    * sous la forme total (x stockes).
    * @param Prelevement
    * @return y (x stockes)
    */
   public static String getNbEchanStockesSurTotal(final Prelevement prel){
      final StringBuffer sb = new StringBuffer();
      sb.append(getNbEchanStockesEtReserves(prel));
      sb.append(" / ");
      sb.append(ManagerLocator.getPrelevementManager().getEchantillonsManager(prel).size());

      return sb.toString();
   }

   /**
    * Dessine la liste d'icône en tête d'une liste de prélèvements 
    * (à l'exception de l'icone concernant l'interfacage)
    * @param prel
    * @return Hlayout contenant les icônes
    * @since 2.0.9
    */
   public static Hlayout drawListIcones(final Prelevement prel){
      final Hlayout icones = new Hlayout();
      //infectieux
      final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(prel).iterator();
      final Div bioHzd = new Div();
      boolean risky = false;
      String risks = ": ";
      Risque risque;
      while(risksIt.hasNext()){
         risque = risksIt.next();
         if(risque.getInfectieux()){
            risky = true;
            risks = risks + " " + risque.getNom();
         }
      }
      if(risky){
         bioHzd.setWidth("18px");
         bioHzd.setHeight("18px");
         bioHzd.setSclass("biohazard");
         bioHzd.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.risque", new String[] {risks}));
      }
      bioHzd.setParent(icones);
      // non conformité

      if(prel != null && prel.getConformeArrivee() != null){
         final Div nonConf = new Div();
         nonConf.setWidth("18px");
         nonConf.setHeight("18px");
         if(prel.getConformeArrivee()){
            nonConf.setSclass("conformeArrivee");
            nonConf.setTooltiptext(Labels.getLabel("tooltip.conforme.arrivee"));
         }else{
            String noconfs = ": ";
            final Iterator<NonConformite> ncIt = ManagerLocator.getNonConformiteManager()
               .getFromObjetNonConformes(ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prel, "Arrivee"))
               .iterator();
            while(ncIt.hasNext()){
               noconfs = noconfs + ncIt.next().getNom();
               if(ncIt.hasNext()){
                  noconfs = noconfs + ", ";
               }
            }
            nonConf.setSclass("nonConformeArrivee");
            nonConf.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.nonconforme.arrivee", new String[] {noconfs}));
         }
         nonConf.setParent(icones);
      }
      return icones;
   }
}
