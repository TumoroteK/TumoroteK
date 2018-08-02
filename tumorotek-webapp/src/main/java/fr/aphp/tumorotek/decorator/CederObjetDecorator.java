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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.ECederObjetStatut;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * Classe 'Decorateur' qui reprend les attributs de CederObjet.
 * pour les afficher dans la liste associée.

 * date: 04/01/10.
 *
 * @version 2.1
 * @author Pierre Ventadour
 *
 */
public class CederObjetDecorator
{

   private CederObjet cederObjet;
   private Echantillon echantillon;
   private ProdDerive prodDerive;
   private Float quantiteMax = (float) 0.0;
   private Float quantiteRestante = null;
   private boolean isAnonyme = false;
   private ObjetStatut newStatut;
   private Emplacement oldEmplacement;
   private Retour retour;
   private List<Unite> unites = new ArrayList<>();

   // @since 2.1
   // color ligne vert si checked=true
   private boolean checked = false;

   private Prelevement parent;

   public CederObjetDecorator(){}

   public CederObjetDecorator(final CederObjet objet, final boolean newCeder, final boolean _a, final boolean _ck){
      this.cederObjet = objet;
      isAnonyme = _a;
      checked = _ck;

      if(this.cederObjet.getEntite() != null){
         if(this.cederObjet.getEntite().getNom().equals("Echantillon")){
            echantillon = (Echantillon) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(cederObjet.getEntite(),
               cederObjet.getObjetId());

            if(this.cederObjet.getQuantite() != null){
               quantiteMax = quantiteMax + this.cederObjet.getQuantite();
            }
            if(this.echantillon.getQuantite() != null){
               quantiteMax = quantiteMax + this.echantillon.getQuantite();
               quantiteRestante = this.echantillon.getQuantite();
            }

            parent = ManagerLocator.getEchantillonManager().getPrelevementManager(getEchantillon());

         }else if(this.cederObjet.getEntite().getNom().equals("ProdDerive")){
            prodDerive = (ProdDerive) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(cederObjet.getEntite(),
               cederObjet.getObjetId());

            if(this.cederObjet.getQuantite() != null){
               quantiteMax = quantiteMax + this.cederObjet.getQuantite();
            }

            if(this.cederObjet.getQuantiteUnite() != null){
               if(this.cederObjet.getQuantiteUnite().getType().equals("volume")){
                  if(this.prodDerive.getVolume() != null){
                     quantiteMax = quantiteMax + this.prodDerive.getVolume();
                     quantiteRestante = this.prodDerive.getVolume();
                  }
               }else{
                  if(this.prodDerive.getQuantite() != null){
                     quantiteMax = quantiteMax + this.prodDerive.getQuantite();
                     quantiteRestante = this.prodDerive.getQuantite();
                  }
               }
            }else if(this.prodDerive.getQuantite() != null){ // qte par defaut si pas unite
               quantiteMax = quantiteMax + this.prodDerive.getQuantite();
               quantiteRestante = this.prodDerive.getQuantite();
            }else if(this.prodDerive.getVolume() != null){ // @since 2.1.1 vol par defaut si pas unite
               quantiteMax = quantiteMax + this.prodDerive.getVolume();
               quantiteRestante = this.prodDerive.getVolume();
            }

            // inits unites
            if(prodDerive.getQuantite() != null && prodDerive.getQuantiteUnite() != null){
               unites.add(prodDerive.getQuantiteUnite());
            }
            if(prodDerive.getVolume() != null && prodDerive.getVolumeUnite() != null){
               unites.add(prodDerive.getVolumeUnite());
            }

            parent = ManagerLocator.getProdDeriveManager().getPrelevementParent(getProdDerive());
         }

         if(newCeder){
            this.cederObjet.setQuantite(quantiteRestante);
            quantiteRestante = (float) 0.0;
            if(!unites.isEmpty()){
               setSelectedUnite(unites.get(0));
            }
         }
      }
   }

   public CederObjet getCederObjet(){
      return cederObjet;
   }

   public void setCederObjet(final CederObjet ceder){
      this.cederObjet = ceder;
   }

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon echan){
      this.echantillon = echan;
   }

   public ProdDerive getProdDerive(){
      return prodDerive;
   }

   public void setProdDerive(final ProdDerive derive){
      this.prodDerive = derive;
   }

   public TKStockableObject getTKobj(){
      if(getEchantillon() != null){
         return getEchantillon();
      }
      return getProdDerive();

   }

   public Float getQuantiteMax(){
      return quantiteMax;
   }

   public void setQuantiteMax(final Float quantite){
      this.quantiteMax = quantite;
   }

   public Float getQuantiteRestante(){
      return quantiteRestante;
   }

   public String getQuantiteRestanteWithUnite(){
      if(this.cederObjet != null){
         final StringBuffer sb = new StringBuffer();
         if(quantiteRestante != null){
            sb.append(quantiteRestante);
         }
         if(this.cederObjet.getQuantiteUnite() != null){
            sb.append(" ");
            sb.append(this.cederObjet.getQuantiteUnite().getUnite());
         }
         return sb.toString();
      }
      return "-";
   }

   public void setQuantiteRestante(final Float quantite){
      this.quantiteRestante = quantite;
   }

   public String getNumeroCession(){
      if(this.cederObjet != null){
         return this.cederObjet.getCession().getNumero();
      }
      return null;
   }

   public String getDateDemandeCessionFormatted(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null){
         return ObjectTypesFormatters.dateRenderer2(this.cederObjet.getCession().getDemandeDate());
      }
      return "-";
   }

   public String getDateValidationCessionFormatted(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null){
         String value = "-";
         if(this.cederObjet.getCession().getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
            value = ObjectTypesFormatters.dateRenderer2(this.cederObjet.getCession().getDestructionDate());
         }else{
            value = ObjectTypesFormatters.dateRenderer2(this.cederObjet.getCession().getValidationDate());
         }
         return value;
      }
      return "-";
   }

   public String getCessionDemandeur(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null && this.cederObjet.getCession().getDemandeur() != null){
         return this.cederObjet.getCession().getDemandeur().getNom();
      }
      return "-";
   }

   public String getCessionDestinataire(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null){
         String value = "-";
         if(this.cederObjet.getCession().getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
            if(this.cederObjet.getCession().getDemandeur() != null){
               value = this.cederObjet.getCession().getDemandeur().getNom();
            }
         }else{
            if(this.cederObjet.getCession().getDestinataire() != null){
               value = this.cederObjet.getCession().getDestinataire().getNom();
            }
         }
         return value;
      }
      return "-";
   }

   public String getCessionType(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null
         && this.cederObjet.getCession().getCessionType() != null){
         return Labels.getLabel("cession.type." + this.cederObjet.getCession().getCessionType().getType().toLowerCase());
      }
      return "-";
   }

   public String getCessionEtude(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null){
         String value = "-";
         if(this.cederObjet.getCession().getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
            if(this.cederObjet.getCession().getDestructionMotif() != null){
               value = this.cederObjet.getCession().getDestructionMotif().getMotif();
            }
         }else if(this.cederObjet.getCession().getCessionType().getType().toUpperCase().equals("SANITAIRE")){
            if(this.cederObjet.getCession().getCessionExamen() != null){
               value = this.cederObjet.getCession().getCessionExamen().getExamen();
            }
         }else{
            if(this.cederObjet.getCession().getEtudeTitre() != null){
               value = this.cederObjet.getCession().getEtudeTitre();
            }
         }
         return value;
      }
      return "-";
   }

   public String getCessionStatut(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null
         && this.cederObjet.getCession().getCessionStatut() != null){
         return ObjectTypesFormatters.ILNObjectStatut(this.cederObjet.getCession().getCessionStatut());
      }
      return "-";
   }

   public String getQuantiteDemandeeFormatted(){
      if(this.cederObjet != null){
         final StringBuffer sb = new StringBuffer();
         if(this.cederObjet.getQuantite() != null){
            sb.append(this.cederObjet.getQuantite());
         }else{
            sb.append("0.0");
         }

         if(this.cederObjet.getQuantiteUnite() != null){
            sb.append(" ");
            sb.append(this.cederObjet.getQuantiteUnite().getUnite());
         }

         return sb.toString();
      }
      return null;
   }

   public String getQuantiteCedeeFormatted(){
      if(this.cederObjet != null && this.cederObjet.getCession() != null){

         if(this.cederObjet.getCession().getCessionStatut() != null){

            if(this.cederObjet.getCession().getCessionStatut().getStatut().equals("VALIDEE")){
               return getQuantiteDemandeeFormatted();
            }
            if(this.cederObjet.getQuantiteUnite() != null){
               return "0.0 " + this.cederObjet.getQuantiteUnite().getUnite();
            }
            return "0.0";
         }
         return null;
      }
      return null;
   }

   public String getEchantillonCode(){
      if(this.echantillon != null){
         return this.echantillon.getCode();
      }
      return null;
   }

   public String getProdDeriveCode(){
      if(this.prodDerive != null){
         return this.prodDerive.getCode();
      }
      return null;
   }

   public String getEchantillonType(){
      if(this.echantillon != null && this.echantillon.getEchantillonType() != null){
         return this.echantillon.getEchantillonType().getType();
      }
      return null;
   }

   public String getProdDeriveType(){
      if(this.prodDerive != null && this.prodDerive.getProdType() != null){
         return this.prodDerive.getProdType().getType();
      }
      return null;
   }

   public Float getEchantillonQuantite(){
      if(this.echantillon != null){
         return this.echantillon.getQuantite();
      }
      return null;
   }

   public Float getProdDeriveQuantite(){
      if(this.prodDerive != null){
         return this.prodDerive.getQuantite();
      }
      return null;
   }

   public Float getProdDeriveVolume(){
      if(this.prodDerive != null){
         return this.prodDerive.getVolume();
      }
      return null;
   }

   public Float getCederQuantite(){
      if(this.cederObjet != null){
         return this.cederObjet.getQuantite();
      }
      return null;
   }

   public String getCederQuantiteWithUnite(){
      if(this.cederObjet != null){
         final StringBuffer sb = new StringBuffer();
         if(this.cederObjet.getQuantite() != null){
            sb.append(this.cederObjet.getQuantite());
         }
         if(this.cederObjet.getQuantiteUnite() != null){
            sb.append(" ");
            sb.append(this.cederObjet.getQuantiteUnite().getUnite());
         }
         return sb.toString();
      }
      return "-";
   }

   /**
    * Retourne true si la quantité est éditable : non nulle.
    */
   public boolean getQuantiteEdition(){
      return (this.quantiteRestante != null);
   }

   /**
    * Retourne true si la quantité est nulle : visualisation.
    */
   public boolean getQuantiteVisualisation(){
      return (this.quantiteRestante == null);
   }

   public boolean getQuantiteVisualisationUnite(){
      return (this.unites.size() > 1);
   }

   public boolean getQuantiteVisualisationUniteInv(){
      return (this.unites.size() <= 1);
   }

   public String getCederQuantiteUnite(){
      if(this.cederObjet != null && this.cederObjet.getQuantiteUnite() != null){
         return this.cederObjet.getQuantiteUnite().getUnite();
      }
      return null;
   }

   /**
    * Extrait les CederObjets d'une liste de Decorator.
    * @param CederObjets
    * @return CederObjets décorés.
    */
   public static List<CederObjet> extractListe(final List<CederObjetDecorator> cedes){
      final List<CederObjet> liste = new ArrayList<>();
      final Iterator<CederObjetDecorator> it = cedes.iterator();

      while(it.hasNext()){
         liste.add(it.next().getCederObjet());
      }
      return liste;
   }

   public String getPrelevementCode(){
      return null;
   }

   public String getPatient(){
      return null;
   }

   public String getStatutJuridique(){
      Prelevement prlvt = null;
      if(echantillon != null){
         prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
      }else if(prodDerive != null){
         prlvt = ManagerLocator.getProdDeriveManager().getPrelevementParent(prodDerive);
      }

      if(prlvt != null){
         return prlvt.getConsentType().getType();
      }
      return "";
   }

   public String getNomPatient(){
      if(!isAnonyme){
         Prelevement prlvt = null;
         if(echantillon != null){
            prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
         }else if(prodDerive != null){
            prlvt = ManagerLocator.getProdDeriveManager().getPrelevementParent(prodDerive);
         }

         if(prlvt != null){
            return PrelevementUtils.getPatientNomAndPrenom(prlvt);
         }
         return null;
      }
      return "-";
   }

   public String getEmplacementAdrl(){
      String adrl = "";
      List<Retour> rets;
      final List<Integer> id = new ArrayList<>();
      if(!isAnonyme){
         if(echantillon != null){
            if(!echantillon.getObjetStatut().getStatut().equals("EPUISE")){
               adrl = ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(echantillon);
            }
         }else if(prodDerive != null){
            if(!prodDerive.getObjetStatut().getStatut().equals("EPUISE")){
               adrl = ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(prodDerive);
            }
         }

         if(adrl.equals("")){
            id.add(cederObjet.getObjetId());
            rets = ManagerLocator.getRetourManager().findByObjectDateRetourEmptyManager(id, cederObjet.getEntite());
            if(!rets.isEmpty()){
               adrl = rets.get(0).getOldEmplacementAdrl();
            }
         }
      }

      return adrl;
   }

   public Integer getNbSorties(){
      if(echantillon != null){
         return new Integer(ManagerLocator.getRetourManager().getRetoursForObjectManager(echantillon).size());
      }else if(prodDerive != null){
         return new Integer(ManagerLocator.getRetourManager().getRetoursForObjectManager(prodDerive).size());
      }
      return null;
   }

   public ObjetStatut getNewStatut(){
      return newStatut;
   }

   public void setNewStatut(final ObjetStatut n){
      this.newStatut = n;
   }

   public Emplacement getOldEmplacement(){
      return oldEmplacement;
   }

   public void setOldEmplacement(final Emplacement o){
      this.oldEmplacement = o;
   }

   public Retour getRetour(){
      return retour;
   }

   public void setRetour(final Retour e){
      this.retour = e;
   }

   public List<Unite> getUnites(){
      return unites;
   }

   public void setUnites(final List<Unite> unites){
      this.unites = unites;
   }

   public Unite getSelectedUnite(){
      return this.cederObjet.getQuantiteUnite();
   }

   public void setSelectedUnite(final Unite u){
      this.cederObjet.setQuantiteUnite(u);
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final CederObjetDecorator deco = (CederObjetDecorator) obj;
      return this.getCederObjet().equals(deco.getCederObjet());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashCederObjet = 0;

      if(this.getCederObjet() != null){
         hashCederObjet = this.getCederObjet().hashCode();
      }

      hash = 7 * hash + hashCederObjet;

      return hash;
   }

   public void switchQuantiteAndVolume(final Unite unite){

      if(unite.getType().equals("volume")){
         // restaure volume
         final Float vol = AbstractController.calculerVolumeRestant(prodDerive, getQuantiteMax());
         quantiteMax = vol;
         this.cederObjet.setQuantite(vol);
      }else{
         final Float qte = AbstractController.calculerQuantiteRestante(prodDerive, getQuantiteMax());
         quantiteMax = qte;
         this.cederObjet.setQuantite(qte);
      }

      quantiteRestante = (float) 0.0;

   }

   public Boolean getImpactVisible(){
      return !ManagerLocator.getRetourManager().findByObjectAndImpactManager(getTKobj(), true).isEmpty();
   }

   public Boolean getPrelC(){
      if(parent != null){
         return parent.getConformeArrivee();
      }
      return false;
   }

   public Boolean getPrelNC(){
      if(parent != null){
         return parent.getConformeArrivee() != null && !parent.getConformeArrivee();
      }
      return false;
   }

   public String getPrelNcfs(){
      String ncfs = null;
      if(getPrelNC()){

         String noconfs = ": ";
         final Iterator<NonConformite> ncIt = ManagerLocator.getNonConformiteManager()
            .getFromObjetNonConformes(ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(parent, "Arrivee"))
            .iterator();
         while(ncIt.hasNext()){
            noconfs = noconfs + ncIt.next().getNom();
            if(ncIt.hasNext()){
               noconfs = noconfs + ", ";
            }
         }
         ncfs = ObjectTypesFormatters.getLabel("tooltip.nonconforme.arrivee", new String[] {noconfs});
      }
      return ncfs;
   }

   public Boolean getTKObjTraitementC(){
      if(getTKobj() != null){
         return getTKobj().getConformeTraitement();
      }
      return false;
   }

   public Boolean getTKObjTraitementNC(){
      if(getTKobj() != null){
         return getTKobj().getConformeTraitement() != null && !getTKobj().getConformeTraitement();
      }
      return false;
   }

   public String getTKObjTraitementNcfs(){
      String ncfs = null;
      if(getTKObjTraitementNC()){
         String noconfs = ": ";
         final Iterator<NonConformite> ncIt = ManagerLocator.getNonConformiteManager().getFromObjetNonConformes(
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(getTKobj(), "Traitement")).iterator();
         while(ncIt.hasNext()){
            noconfs = noconfs + ncIt.next().getNom();
            if(ncIt.hasNext()){
               noconfs = noconfs + ", ";
            }
         }
         ncfs = ObjectTypesFormatters.getLabel("tooltip.nonconforme.traitement", new String[] {noconfs});
      }
      return ncfs;
   }

   public Boolean getTKObjCessionC(){
      if(getTKobj() != null){
         return getTKobj().getConformeCession();
      }
      return false;
   }

   public Boolean getTKObjCessionNC(){
      if(getTKobj() != null){
         return getTKobj().getConformeCession() != null && !getTKobj().getConformeCession();
      }
      return false;
   }

   public String getTKObjCessionNcfs(){
      String ncfs = null;
      if(getTKObjCessionNC()){
         String noconfs = ": ";
         final Iterator<NonConformite> ncIt = ManagerLocator.getNonConformiteManager().getFromObjetNonConformes(
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(getTKobj(), "Cession")).iterator();
         while(ncIt.hasNext()){
            noconfs = noconfs + ncIt.next().getNom();
            if(ncIt.hasNext()){
               noconfs = noconfs + ", ";
            }
         }
         ncfs = ObjectTypesFormatters.getLabel("tooltip.nonconforme.cession", new String[] {noconfs});
      }
      return ncfs;
   }

   public boolean isChecked(){
      return checked;
   }

   public void setChecked(final boolean _c){
      this.checked = _c;
   }

   public String getRowCheckedStyleClass(){
      if(checked){
         return "greenrow";
      }
      return null;
   }

   public String getStatut(){
      if(null != this.cederObjet.getStatut()){
         return Labels.getLabel("cederObjet.statut." + this.cederObjet.getStatut().getStatut());
      }
      return null;
   }

   public String getProduitRetourList(){
      String res = null;
      if(null != this.cederObjet && null != this.cederObjet.getProduitRetourList()
         && !this.cederObjet.getProduitRetourList().isEmpty()){
         Integer nbProdRetour = this.cederObjet.getProduitRetourList().size();
         res = nbProdRetour + " ...";
      }

      return res;
   }

   public Boolean getIsEnTraitement(){
      Boolean res = false;
      if(null != this.cederObjet){
         res = ECederObjetStatut.TRAITEMENT == this.cederObjet.getStatut();
      }
      return res;
   }

   public Boolean getHasProduitRetour(){
      Boolean res = false;
      if(null != this.cederObjet && null != this.cederObjet.getProduitRetourList()){
         res = !this.cederObjet.getProduitRetourList().isEmpty();
      }
      return res;
   }
}
