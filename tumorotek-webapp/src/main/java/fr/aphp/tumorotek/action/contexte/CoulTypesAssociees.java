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
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.decorator.CouleurItemRenderer;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class CoulTypesAssociees extends OneToManyComponent
{

   private static final long serialVersionUID = 1L;

   private Listbox couleurBox;

   private boolean isEchantillonTyped = true;

   private List<CouleurEntiteType> objects = new ArrayList<>();
   private final List<Couleur> couleurs = new ArrayList<>();

   private final CoulTypesRowRenderer coulTypesRenderer = new CoulTypesRowRenderer();
   private final CouleurItemRenderer couleurRenderer = new CouleurItemRenderer();

   public CoulTypesRowRenderer getCoulTypesRenderer(){
      return coulTypesRenderer;
   }

   public CouleurItemRenderer getCouleurRenderer(){
      return couleurRenderer;
   }

   public List<Couleur> getCouleurs(){
      return couleurs;
   }

   public void setEchantillonTyped(final boolean is){
      this.isEchantillonTyped = is;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);

      couleurs.addAll(ManagerLocator.getCouleurManager().findAllObjectsManager());

      getBinder().loadAttribute(couleurBox, "model");
   }

   @Override
   public List<CouleurEntiteType> getObjects(){
      return this.objects;
   }

   
   @Override
   public void setObjects(final List<? extends Object> objs){
      this.objects = (List<CouleurEntiteType>) objs;
      updateComponent();
   }

   @Override
   public void addToListObjects(final Object obj){
      final CouleurEntiteType cet = new CouleurEntiteType();
      if(isEchantillonTyped){
         cet.setEchantillonType((EchantillonType) obj);
      }else{
         cet.setProdType((ProdType) obj);
      }
      Couleur selected = couleurs.get(0);
      if(couleurBox.getSelectedItem() != null){
         selected = (Couleur) couleurBox.getSelectedItem().getValue();
      }
      cet.setCouleur(selected);

      getObjects().add(cet);
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
   public List<? extends Object> findObjectsAddable(){
      if(isEchantillonTyped){
         final List<EchantillonType> echanTypes = (List<EchantillonType>) ManagerLocator.getEchantillonTypeManager()
            .findByOrderManager(SessionUtils.getPlateforme(sessionScope));
         // retire les types deja assignés
         for(int i = 0; i < getObjects().size(); i++){
            echanTypes.remove(getObjects().get(i).getEchantillonType());
         }
         return echanTypes;
      }else{
         final List<ProdType> prodTypes =
            (List<ProdType>) ManagerLocator.getProdTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
         // retire les types deja assignés
         for(int i = 0; i < getObjects().size(); i++){
            prodTypes.remove(getObjects().get(i).getProdType());
         }
         return prodTypes;
      }
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}
}
