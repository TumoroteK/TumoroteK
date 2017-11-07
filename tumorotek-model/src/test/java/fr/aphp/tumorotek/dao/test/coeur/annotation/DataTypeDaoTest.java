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
package fr.aphp.tumorotek.dao.test.coeur.annotation;

import java.util.List;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 * 
 * Classe de test pour le DAO DataTypeDao et le 
 * bean du domaine DataType.
 * Classe de test créée le 26/11/09.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class DataTypeDaoTest extends AbstractDaoTest {
	
	/** Beans Dao. */
	private DataTypeDao dataTypeDao;
	
	/**
	 * Constructeur.
	 */
	public DataTypeDaoTest() {
	}
	
	/**
	 * Setter du bean Dao.
	 * @param dtDao est le bean Dao.
	 */
	public void setDataTypeDao(DataTypeDao dtDao) {
		this.dataTypeDao = dtDao;
	}

	/**
	 * Test l'appel de la méthode toString().
	 */
	public void testToString() {
		DataType dt1 = dataTypeDao.findById(1);
		assertTrue(dt1.toString()
				.equals("{" + dt1.getType() + "}"));
		
		dt1 = new DataType();
		assertTrue(dt1.toString().equals("{Empty DataType}"));
	}	
	
	public void testFindByType() {
		List<DataType> dataTypes = dataTypeDao.findByType("alphanum");
		assertTrue(dataTypes.size() == 1);
		dataTypes = dataTypeDao.findByType("toto");
		assertTrue(dataTypes.size() == 0);
		dataTypes = dataTypeDao.findByType("date");
		assertTrue(dataTypes.size() == 1);
		dataTypes = dataTypeDao.findByType("boolean");
		assertTrue(dataTypes.size() == 1);
		dataTypes = dataTypeDao.findByType(null);
		assertTrue(dataTypes.size() == 0);
	}
	
	/**
	 * Test des méthodes surchargées "equals" et hashcode.
	 */
	public void testEqualsAndHashCode() {
		DataType dt1 = new DataType();
		DataType dt2 = new DataType();
		assertFalse(dt1.equals(null));
		assertNotNull(dt2);
		assertTrue(dt1.equals(dt1));
		assertTrue(dt1.equals(dt2));
		assertTrue(dt1.hashCode() == dt2.hashCode());
		
		String s1 = "type1";
		String s2 = "type2";
		String s3 = new String("type2");
		
		dt1.setType(s1);
		assertFalse(dt1.equals(dt2));
		assertFalse(dt2.equals(dt1));
		assertTrue(dt1.hashCode() != dt2.hashCode());
		dt2.setType(s2);
		assertFalse(dt1.equals(dt2));
		assertFalse(dt2.equals(dt1));
		assertTrue(dt1.hashCode() != dt2.hashCode());
		dt1.setType(s2);
		assertTrue(dt1.equals(dt2));
		assertTrue(dt2.equals(dt1));
		assertTrue(dt1.hashCode() == dt2.hashCode());
		dt1.setType(s3);
		assertTrue(dt1.equals(dt2));
		assertTrue(dt2.equals(dt1));
		assertTrue(dt1.hashCode() == dt2.hashCode());
		
		// dummy
		Categorie c = new Categorie();
		assertFalse(dt1.equals(c));
	}
	
//	/**
//	 * Test de la méthode surchargée "hashcode".
//	 * @throws ParseException 
//	 */
//	public void testHashCode() throws ParseException {
//
//		DataType dt1 = new DataType();
//		dt1.setDataTypeId(1);
//		DataType dt2 = new DataType();
//		dt2.setDataTypeId(2);
//		DataType dt3 = new DataType();
//		dt3.setDataTypeId(3);
//		
//		assertTrue(dt1.hashCode() == dt2.hashCode());
//		assertTrue(dt2.hashCode() == dt3.hashCode());
//		assertTrue(dt3.hashCode() > 0);
//		
//		int hash = dt1.hashCode();
//		// 2 objets égaux ont le même hashcode
//		assertTrue(dt1.hashCode() == dt2.hashCode());
//		// un même objet garde le même hashcode dans le temps
//		assertTrue(hash == dt1.hashCode());
//		assertTrue(hash == dt1.hashCode());
//		assertTrue(hash == dt1.hashCode());
//		assertTrue(hash == dt1.hashCode());
//	}
	

}
