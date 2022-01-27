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
package fr.aphp.tumorotek.manager.impl.coeur;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.TKAnnotableObjectManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.ObjetStatutValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;

/**
 *
 * Implémentation manager générique TKAnnotableObject.
 * Classe créée le 05/02/19.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3
 *
 */
public class TKAnnotableObjectManagerImpl implements TKAnnotableObjectManager
{

   private final Log log = LogFactory.getLog(TKAnnotableObjectManager.class);

   @Autowired
   private PatientDao patientDao;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private ProdDeriveDao prodDeriveDao;
   @Autowired
   private CessionDao cessionDao;
   
   @Override
   public TKAnnotableObject findByIdManager(TKAnnotableObject tkObj) {
	   if (tkObj != null && tkObj.entiteNom() != null) {
		   if (tkObj.entiteNom().equals("Patient")) {
			   return patientDao.findById(tkObj.listableObjectId());
		   } else if (tkObj.entiteNom().equals("Prelevement")) {
			   return prelevementDao.findById(tkObj.listableObjectId());
		   } else if (tkObj.entiteNom().equals("Echantillon")) {
			   return echantillonDao.findById(tkObj.listableObjectId());
		   } else if (tkObj.entiteNom().equals("ProdDerive")) {
			   return prodDeriveDao.findById(tkObj.listableObjectId());
		   } else if (tkObj.entiteNom().equals("Cession")) {
			   return cessionDao.findById(tkObj.listableObjectId());
		   } else {
			   return tkObj;
		   }
	   }
	   return null;
   }

}
