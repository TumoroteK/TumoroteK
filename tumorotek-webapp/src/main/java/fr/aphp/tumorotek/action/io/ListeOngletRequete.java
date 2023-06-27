package fr.aphp.tumorotek.action.io;

import fr.aphp.tumorotek.action.controller.*;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

public abstract class ListeOngletRequete extends AbstractListeController2
{
   private Column collection;


   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      listPanel.setHeight(getMainWindow().getListPanelHeight() + 35 + "px");
      initColumns();
   }

   protected void initColumns() {
      collection.setVisible(sessionScope.containsKey("ToutesCollections"));
   }

   protected void reloadComponent(){
      getBinder().loadComponent(self);
      initColumns();
   }

   public Column getCollection(){
      return collection;
   }

   public void setCollection(Column collection){
      this.collection = collection;
   }


}
