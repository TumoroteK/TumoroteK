package fr.aphp.tumorotek.action.listmodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.AbstractListModel;

import fr.aphp.tumorotek.decorator.factory.TKDecoratorFactory;
import fr.aphp.tumorotek.model.io.export.Affichage;

/**
 * ListModel qui permette de charger à la demande une liste de TK Objects.
 * @see http://books.zkoss.org/wiki/Small_Talks/2009/July/Handling_huge_data_using_ZK
 * Date: 15/08/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 */
public class ObjectPagingModel extends AbstractListModel<Object> {

		private static final long serialVersionUID = 6613208067174831719L;
		
		private List<? extends Object> objets;
		
		private List<? extends Object> _items;
		
		private TKDecoratorFactory decoFactory;
		
		protected int _startPageNumber;
		protected int _pageSize;
		protected int _itemStartNumber;
		
		private Affichage affichage;
		
		public Affichage getAffichage() {
			return affichage;
		}

		public void setAffichage(Affichage a) {
			this.affichage = a;
		}
				
		public TKDecoratorFactory getDecoFactory() {
			return decoFactory;
		}

		public void setDecoFactory(TKDecoratorFactory d) {
			this.decoFactory = d;
		}

		public List<? extends Object> getObjets() {
			return objets;
		}

		public void setObjets(List<? extends Object> o) {
			this.objets = o;
		}

		public ObjectPagingModel(int startPageNumber, int pageSize, 
									List<? extends Object> objs, 
									TKDecoratorFactory deco, Affichage aff) {
			
			super();
			
			setObjets(objs);
			setAffichage(aff);
			setDecoFactory(deco);
		
			this._startPageNumber = startPageNumber;
			this._pageSize = pageSize;
			this._itemStartNumber = startPageNumber * pageSize;
		
			firstPage();
		}
		
		protected void firstPage() {
			_items = getPageData(_itemStartNumber, _pageSize);			
		}

		public int getTotalSize() {
			return getObjets().size();
		}


		public Object getElementAt(int index) {
			return _items.get(index);
		}
		
		public int getSize() {
			return _items.size();
		}
		
		public int getStartPageNumber() {
			return this._startPageNumber;
		}
		
		public int getPageSize() {
			return this._pageSize;
		}
		
		public int getItemStartNumber() {
			return _itemStartNumber;
		}

		protected List<? extends Object> getPageData(int itemStartNumber, 
										int pageSize) {
			List<Object> pageObjs = 
									new ArrayList<Object>();
			
			int end = itemStartNumber + pageSize;
			if (end >= getObjets().size()) {
				end = getObjets().size();
			}
			
			for (int i = itemStartNumber; i < end; i ++) {
				pageObjs.add(getObjets().get(i));
			}
			
			if (getDecoFactory() != null) { 
				return getDecoFactory().decorateListe(pageObjs);
			}
			return pageObjs;
		}
		

}