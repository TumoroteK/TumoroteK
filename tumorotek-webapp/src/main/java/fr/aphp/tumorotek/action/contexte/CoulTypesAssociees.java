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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelSet;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.manager.systeme.CouleurManager;
import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;
import fr.aphp.tumorotek.model.AbstractThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class CoulTypesAssociees extends OneToManyComponent<CouleurEntiteType>
{

   private static final long serialVersionUID = 1L;

   private Listbox couleurBox;

   private Listbox typeBox;

   private List<CouleurEntiteType> objects = new ArrayList<>();

   private final CoulTypesRowRenderer coulTypesRenderer = new CoulTypesRowRenderer();

   public CoulTypesRowRenderer getCoulTypesRenderer(){
      return coulTypesRenderer;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);

      final List<Couleur> listCouleurs = ManagerLocator.getManager(CouleurManager.class).findAllObjectsManager().stream()
         .sorted(Comparator.comparing(Couleur::getCouleur)).collect(Collectors.toList());

      final ListModel<Couleur> couleurs = new ListModelSet<>(listCouleurs);

      final ListitemRenderer<AbstractThesaurusObject> typesRenderer = (li, item, idx) -> {
         li.setLabel(item.getNom());
         li.setValue(item);
      };

      final ListitemRenderer<Couleur> couleursRenderer = (li, couleur, idx) -> {
         li.setLabel(couleur.getCouleur());
         li.setStyle(couleur.getHexaCssStyle());
         li.setValue(couleur);
      };

      typeBox.setItemRenderer(typesRenderer);

      couleurBox.setItemRenderer(couleursRenderer);
      couleurBox.setModel(couleurs);

   }

   @Override
   public List<CouleurEntiteType> getObjects(){
      return this.objects;
   }

   @Override
   public void setObjects(final List<CouleurEntiteType> objs){
      this.objects = objs;
      updateComponent();
   }

   @Override
   public void addToListObjects(final CouleurEntiteType obj){}

   @Override
   public void onClick$addObj(){

      final List<AbstractThesaurusObject> listTypes = findObjectsAddable();
      final ListModelSet<AbstractThesaurusObject> types = new ListModelSet<>(listTypes);

      typeBox.setModel(types);

      addObjBox.setVisible(true);
      addObj.setVisible(false);
      deleteHeader.setVisible(false);
   }

   @Override
   public void onClick$addSelObj(){

      final CouleurEntiteType cet = new CouleurEntiteType();

      final AbstractPfDependantThesaurusObject selectedType = typeBox.getSelectedItem().getValue();
      if(selectedType instanceof ProdType){
         cet.setProdType((ProdType) selectedType);
      }else{
         cet.setEchantillonType((EchantillonType) selectedType);
      }

      ((ListModelSet<Object>) typeBox.getListModel()).remove(selectedType);

      cet.setCouleur((Couleur) couleurBox.getSelectedItem().getValue());

      objects.add(cet);

   }

   @Override
   public void removeFromListObjects(final Object obj){
      getObjects().remove(obj);
   }

   @Override
   public String getGroupHeaderValue(){
      return null;
   }

   @Override
   public List<AbstractThesaurusObject> findObjectsAddable(){

      final boolean typeEchantillon = (Boolean) arg.get("isEchantillonTyped");

      final List<AbstractThesaurusObject> listTypes;
      if(typeEchantillon){
         final List<EchantillonType> listTypesAssignes = getObjects().stream().map(CouleurEntiteType::getEchantillonType)
            .filter(Objects::nonNull).collect(Collectors.toList());
         listTypes = ManagerLocator.getManager(EchantillonTypeManager.class)
            .findByOrderManager(SessionUtils.getCurrentPlateforme()).stream().filter(et -> !listTypesAssignes.contains(et))
            .sorted(Comparator.comparing(EchantillonType::getNom)).collect(Collectors.toList());
      }else{
         final List<ProdType> listTypesAssignes =
            getObjects().stream().map(CouleurEntiteType::getProdType).filter(Objects::nonNull).collect(Collectors.toList());
         listTypes = ManagerLocator.getManager(ProdTypeManager.class).findByOrderManager(SessionUtils.getCurrentPlateforme())
            .stream().filter(pt -> !listTypesAssignes.contains(pt)).sorted(Comparator.comparing(ProdType::getNom))
            .collect(Collectors.toList());

      }

      return listTypes;
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}
}
