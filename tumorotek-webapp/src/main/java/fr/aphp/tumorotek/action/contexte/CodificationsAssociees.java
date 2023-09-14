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
import java.util.stream.Collectors;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;

public class CodificationsAssociees extends OneToManyComponent<BanqueTableCodage>
{

   private static final long serialVersionUID = 1L;

   private List<BanqueTableCodage> objects = new ArrayList<>();

   private final CodificationsRowRenderer codificationsRenderer = new CodificationsRowRenderer();

   private Listbox codeOrLibelleBox;

   public CodificationsRowRenderer getCodificationsRenderer(){
      return codificationsRenderer;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);
   }

   @Override
   public List<BanqueTableCodage> getObjects(){
      return this.objects;
   }

   @Override
   public void setObjects(final List<BanqueTableCodage> objs){
      this.objects = objs;
      updateComponent();
   }

   @Override
   public void addToListObjects(final BanqueTableCodage obj){

      Boolean value = false;
      if(codeOrLibelleBox.getSelectedItem() != null){
         value = codeOrLibelleBox.getSelectedItem().getValue().equals("libelle");
      }
      obj.setLibelleExport(value);

      getObjects().add(obj);
   }

   //	public void addToListObjects(final TableCodage obj){
   //		final BanqueTableCodage btc = new BanqueTableCodage();
   //		btc.setTableCodage(obj);
   //		Boolean value = false;
   //		if(codeOrLibelleBox.getSelectedItem() != null){
   //			value = codeOrLibelleBox.getSelectedItem().getValue().equals("libelle");
   //		}
   //		btc.setLibelleExport(value);
   //
   //		addToListObjects(btc);
   //	}

   @Override
   public void removeFromListObjects(final Object obj){
      getObjects().remove(obj);
   }

   @Override
   public String getGroupHeaderValue(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("Champ.Banque.Codifications"));
      sb.append(" (");
      sb.append(getObjects().size());
      sb.append(")");
      return sb.toString();
   }

   @Override
   public List<BanqueTableCodage> findObjectsAddable(){
      // TableCodages ajoutables
      final List<TableCodage> tabs = ManagerLocator.getTableCodageManager().findAllObjectsManager();
      tabs.remove(ManagerLocator.getTableCodageManager().findByNomManager("FAVORIS").get(0));

      // retire les TableCodages deja assignés
      for(int i = 0; i < getObjects().size(); i++){
         tabs.remove(getObjects().get(i).getTableCodage());
      }

      List<BanqueTableCodage> btc = new ArrayList<>();
      btc = tabs.stream().map(t -> new BanqueTableCodage(null, t)).collect(Collectors.toList());

      return btc;
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}
}
