package fr.aphp.tumorotek.manager.impl.stats;

import java.util.List;

import fr.aphp.tumorotek.dao.stats.SubdivisionDao;
import fr.aphp.tumorotek.manager.stats.SubdivisionManager;
import fr.aphp.tumorotek.model.stats.Subdivision;

public class SubdivisionManagerImpl implements SubdivisionManager{

	private SubdivisionDao subdivisionDao;
	
	public void setSubdivisionDao(SubdivisionDao sDao) {
		this.subdivisionDao = sDao;
	}
	
	@Override
	public List<Subdivision> findAllObjectsManager() {
		return subdivisionDao.findAll();
	}

}
