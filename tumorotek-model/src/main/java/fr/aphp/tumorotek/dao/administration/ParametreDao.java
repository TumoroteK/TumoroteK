package fr.aphp.tumorotek.dao.administration;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;

import java.util.List;

public interface ParametreDao extends GenericDaoJpa<ParametreValeurSpecifique, Integer>
{
   List<ParametreValeurSpecifique> findByPlateformeIdAndCode(Integer plateformeId, String code);
   List<ParametreValeurSpecifique> findAllByPlateformeId(Integer plateformeId);

}