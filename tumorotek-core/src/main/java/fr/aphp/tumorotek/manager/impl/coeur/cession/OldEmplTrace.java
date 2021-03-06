package fr.aphp.tumorotek.manager.impl.coeur.cession;

import java.util.Objects;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;

/**
 * POJO associant un objet stockable à une ancienne adresse 
 * et un conteneur d'origine.
 * 
 * Fix: https://tumorotek.myjetbrains.com/youtrack/issue/TK-291
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 */
public class OldEmplTrace {

	private TKStockableObject tkObj;
	private String oldAdrl;
	private Conteneur conteneur;
	private Emplacement current;

	public OldEmplTrace(TKStockableObject _o, String _a, Conteneur _c, Emplacement _e) {
		this.tkObj = _o;
		this.oldAdrl = _a;
		this.conteneur = _c;
		this.current = _e;
	}

	public TKStockableObject getTkObj() {
		return tkObj;
	}

	public void setTkObj(TKStockableObject _t) {
		this.tkObj = _t;
	}

	public String getOldAdrl() {
		return oldAdrl;
	}

	public void setOldAdrl(String _o) {
		this.oldAdrl = _o;
	}

	public Conteneur getConteneur() {
		return conteneur;
	}

	public void setConteneur(Conteneur _c) {
		this.conteneur = _c;
	}

	public Emplacement getCurrent() {
		return current;
	}

	public void setCurrent(Emplacement current) {
		this.current = current;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		OldEmplTrace trace = (OldEmplTrace) obj;

		return (Objects.equals(getTkObj(), trace.getTkObj()));
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getTkObj() == null) ? 0 : getTkObj().hashCode());
		return result;
	}

}