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
package fr.aphp.tumorotek.action.thesaurus;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;

import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Diagnostic;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

public class ListeThesaurus extends AbstractListeController2
{

   private static final long serialVersionUID = -3344253941641747494L;

   private List<Thesaurus<?>> listObjects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 145;
      listPanel.setHeight(height + "px");
   }

   @Override
   public List<Thesaurus<?>> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects.clear();
      this.listObjects = (List<Thesaurus<?>>) objs;
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){}

   @Override
   public void addToSelectedObjects(final TKdataObject obj){}

   @Override
   public List<Integer> doFindObjects(){
      return null;
   }

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return null;
   }

   @Override
   public List<? extends Object> getSelectedObjects(){
      return null;
   }

   @Override
   public void initObjectsBox(){
      initListes();
      setCurrentRow(null);
      setCurrentObject(null);

      if(getBinder() != null){
         getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
      }
   }

   /**
    * éthode initialisant la liste des thésaurus modifiables.
    */
   public void initListes(){
      listObjects.clear();
      // Catégorie
      listObjects.add(new Thesaurus<>(Categorie.class, Labels.getLabel("thesaurus.liste.categorie")));
      // examen
      listObjects.add(new Thesaurus<>(CessionExamen.class, Labels.getLabel("thesaurus.liste.examen.cession")));
      // Milieu de conditionnement
      listObjects.add(new Thesaurus<>(ConditMilieu.class, Labels.getLabel("thesaurus.liste.milieu.conditionnement")));
      // Mode de préparation
      listObjects.add(new Thesaurus<>(ModePrepa.class, Labels.getLabel("thesaurus.liste.mode.preparation")));
      // Mode de préparation dérivés
      listObjects.add(new Thesaurus<>(ModePrepaDerive.class, Labels.getLabel("thesaurus.liste.mode.preparation.derive")));
      // Motif de destruction
      listObjects.add(new Thesaurus<>(DestructionMotif.class, Labels.getLabel("thesaurus.liste.motif.destruction")));
      // nature des prlvts
      listObjects.add(new Thesaurus<>(Nature.class, Labels.getLabel("thesaurus.liste.nature.prelevement")));
      // Non conformite a l'arrivee
      listObjects.add(new Thesaurus<>(NonConformite.class, "NonConformiteArrivee", Labels.getLabel("thesaurus.liste.nonConformite.arrivee")));
      // Non conformite apres traitement Echantillon
      listObjects
         .add(new Thesaurus<>(NonConformite.class, "NonConformiteTraitementEchan", Labels.getLabel("thesaurus.liste.nonConformite.traitement.echan")));
      // Non conformite a la cession Echantillon
      listObjects.add(new Thesaurus<>(NonConformite.class, "NonConformiteCessionEchan", Labels.getLabel("thesaurus.liste.nonConformite.cession.echan")));
      // Non conformite apres traitement Derive
      listObjects
         .add(new Thesaurus<>(NonConformite.class, "NonConformiteTraitementDerive", Labels.getLabel("thesaurus.liste.nonConformite.traitement.derive")));
      // Non conformite a la cession Derive
      listObjects
         .add(new Thesaurus<>(NonConformite.class, "NonConformiteCessionDerive", Labels.getLabel("thesaurus.liste.nonConformite.cession.derive")));
      // Protocole SerotK
      listObjects.add(new Thesaurus<>(Protocole.class, Labels.getLabel("thesaurus.liste.serotk.protocole")));
   // Diagnostic SerotK
      listObjects.add(new Thesaurus<>(Diagnostic.class, Labels.getLabel("thesaurus.liste.serotk.diagnostic")));
      // Qualité de l'échantillon
      listObjects.add(new Thesaurus<>(EchanQualite.class, Labels.getLabel("thesaurus.liste.qualite.echantillon")));
      // Qualité du dérivé
      listObjects.add(new Thesaurus<>(ProdQualite.class, Labels.getLabel("thesaurus.liste.qualite.prodDerive")));
      // Risque
      listObjects.add(new Thesaurus<>(Risque.class, Labels.getLabel("thesaurus.liste.risque")));
      // Spécialité
      listObjects.add(new Thesaurus<>(Specialite.class, Labels.getLabel("thesaurus.liste.specialite")));
      // Statut juridique
      listObjects.add(new Thesaurus<>(ConsentType.class, Labels.getLabel("thesaurus.liste.type.consentement")));
      // Type d'échantillon
      listObjects.add(new Thesaurus<>(EchantillonType.class, Labels.getLabel("thesaurus.liste.type.echantillon")));
      // Type d'enceinte
      listObjects.add(new Thesaurus<>(EnceinteType.class, Labels.getLabel("thesaurus.liste.type.enceinte")));
      // Type de conditionnement
      listObjects.add(new Thesaurus<>(ConditType.class, Labels.getLabel("thesaurus.liste.type.conditionnement")));
      // Type de conteneur
      listObjects.add(new Thesaurus<>(ConteneurType.class, Labels.getLabel("thesaurus.liste.type.conteneur")));
      // Type de prélèvement
      listObjects.add(new Thesaurus<>(PrelevementType.class, Labels.getLabel("thesaurus.liste.type.prelevement")));
      // Type de dérivé
      listObjects.add(new Thesaurus<>(ProdType.class, Labels.getLabel("thesaurus.liste.type.prodDerive")));
      // Type de protocole
      listObjects.add(new Thesaurus<>(ProtocoleType.class, Labels.getLabel("thesaurus.liste.type.protocole")));
   }

   public void onClick$viewObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event),
         (TKdataObject) AbstractListeController2.getBindingData((ForwardEvent) event, false));

      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();

      // on envoie le thésaurus à la fiche
      // Thesaurus edit = nomsObjectsTable.get(getCurrentObject());
      getFiche().setObject(getCurrentObject());
      getFiche().switchToStaticMode();
   }

   /**
    * Recupere le controller du composant representant la fiche associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche FicheProfil
    */
   public AbstractFicheCombineController getFiche(){
      return getObjectTabController().getFicheCombine();
   }

   @Override
   public void passListToSelected(){}

   @Override
   public void passSelectedToList(){}

   @Override
   public void removeObjectFromList(final TKdataObject obj){}

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){}

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){}

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }

}
