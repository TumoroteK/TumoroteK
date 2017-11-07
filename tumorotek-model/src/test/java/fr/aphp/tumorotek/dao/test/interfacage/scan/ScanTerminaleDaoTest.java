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
package fr.aphp.tumorotek.dao.test.interfacage.scan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.interfacage.scan.ScanDeviceDao;
import fr.aphp.tumorotek.dao.interfacage.scan.ScanTerminaleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.interfacage.scan.ScanDevice;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;

/**
 * 
 * Classe de test pour le DAO ScanTerminaleDao et le bean du domaine ScanTerminale.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.1
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ScanTerminaleDaoTest extends AbstractDaoTest {
	
	private ScanTerminaleDao scanTerminaleDao;
	private ScanDeviceDao scanDeviceDao;
	
	public ScanTerminaleDaoTest() {	
	}
	
	@Override
	protected String[] getConfigLocations()	{
		return new String[]{ "applicationContextDao-interfacages.xml" };
	}
	
	public void setScanTerminaleDao(ScanTerminaleDao _s) {
		this.scanTerminaleDao = _s;
	}
	
	public synchronized void setScanDeviceDao(ScanDeviceDao _s) {
		this.scanDeviceDao = _s;
	}

	@Rollback(false)
	public void testCrudScanTerminale() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		ScanTerminale st1 = new ScanTerminale();
		ScanDevice visionMate = scanDeviceDao.findById(1);
		st1.setName("BT1");
		st1.setScanDevice(visionMate);
		st1.setWidth(10);
		st1.setHeight(12);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse("20/04/2016 12:12:12"));
		st1.setDateScan(c);
		ScanTube t1 = new ScanTube();
		t1.setCode("PTRA.1.1");
		t1.setCell("A01");
		t1.setRow("A");
		t1.setCol("01");
		st1.addTube(t1);
		ScanTube t2 = new ScanTube();
		t2.setCode("PTRA.1.2");
		t2.setCell("A02");
		t2.setRow("C");
		t2.setCol("02");
		st1.addTube(t2);
		ScanTube t3 = new ScanTube();
		t3.setCode("PTRA.2");
		t3.setCell("A03");
		t3.setRow("F");
		t3.setCol("08");
		st1.addTube(t3);
		
		int totSize = scanTerminaleDao.findAll().size();
		
		// test findByScanDevice
		assertTrue(scanTerminaleDao.findByScanDevice(null).isEmpty());
		assertTrue(scanTerminaleDao.findByScanDevice(visionMate).isEmpty());
		
		// Test de l'insertion
		scanTerminaleDao.createObject(st1);
		assertTrue(scanTerminaleDao.findAll().size() == totSize + 1);
		assertTrue(st1.getScanTerminaleId() != null);
		assertTrue(scanTerminaleDao.findByScanDevice(visionMate).size() == 1);
		// test findTkCodes
		assertTrue(scanTerminaleDao.findTKObjectCodes(null).isEmpty());
		
		List<String> codes = scanTerminaleDao.findTKObjectCodes(st1);
		assertTrue(codes.size() == 3);
		assertTrue(codes.get(0).equals("PTRA.1.1"));
		assertTrue(codes.get(1).equals("PTRA.1.2"));
		assertTrue(codes.get(2).equals("PTRA.2"));

		ScanTerminale st2 = scanTerminaleDao.findById(st1.getScanTerminaleId());
		// Vérification des données entrées dans la base
		assertNotNull(st2);
		assertTrue(st2.getName().equals("BT1"));
		assertTrue(st2.getDateScan().equals(c));
		assertTrue(st2.getWidth() == 10);
		assertTrue(st2.getHeight() == 12);
		assertTrue(st2.getScanDevice().equals(visionMate));
		assertTrue(st2.getScanTubes().size() == 3);
		assertTrue(st2.getScanTubes().get(0).equals(t1));
		assertTrue(st2.getScanTubes().get(0).getPosition() == 1);
		assertTrue(st2.getScanTubes().get(1).equals(t2));
		assertTrue(st2.getScanTubes().get(1).getPosition() == 22);
		assertTrue(st2.getScanTubes().get(2).equals(t3));
		assertTrue(st2.getScanTubes().get(2).getPosition() == 58);
		
		// Test de la délétion
		scanTerminaleDao.removeObject(st2.getScanTerminaleId());
		assertNull(scanTerminaleDao.findById(st2.getScanTerminaleId()));
		
		assertTrue(scanTerminaleDao.findAll().size() == totSize);
	}
	
//	/**
//	 * Test de la méthode surchargée "equals".
//	 * @throws ParseException 
//	 */
//	public void testEquals() throws ParseException {
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//
//		ConsultationIntf ci1 = new ConsultationIntf();
//		ConsultationIntf ci2 = new ConsultationIntf();
//		
//		String id1 = "id1";
//		String id2 = "id2";
//		Utilisateur u1 = utilisateurDao.findById(1);
//		Utilisateur u2 = utilisateurDao.findById(2);
//		String emetId1 = "id1";
//		String emetId2 = "id2";
//		Calendar c1 = Calendar.getInstance();
//		c1.setTime(sdf.parse("12/12/2015 12:12:00"));
//		Calendar c2 = Calendar.getInstance();
//		c2.setTime(sdf.parse("13/12/2015 12:12:00"));
//		
//		assertTrue(ci1.equals(ci1));
//		
//		ci1.setIdentification(id1);
//		ci1.setUtilisateur(u1);
//		ci1.setDate(c1);
//		ci1.setEmetteurIdent(emetId1);
//		ci2.setIdentification(id1);
//		ci2.setUtilisateur(u1);
//		ci2.setDate(c1);
//		ci2.setEmetteurIdent(emetId1);
//		
//		// L'objet 1 n'est pas égal à null
//		assertFalse(ci1.equals(null));
//		// L'objet 1 est égale à lui même
//		assertTrue(ci1.equals(ci1));
//		// 2 objets sont égaux entre eux
//		assertTrue(ci1.equals(ci2));
//		assertTrue(ci2.equals(ci1));
//
//		ci2.setIdentification(id2);
//		assertFalse(ci1.equals(ci2));
//		ci1.setIdentification(id2);
//		assertTrue(ci1.equals(ci2));
//		ci2.setIdentification(null);
//		assertFalse(ci1.equals(ci2));
//		ci2.setIdentification(id2);
//		assertTrue(ci1.equals(ci2));
//		
//		ci2.setUtilisateur(u2);
//		assertFalse(ci1.equals(ci2));
//		ci1.setUtilisateur(u2);
//		assertTrue(ci1.equals(ci2));
//		ci2.setUtilisateur(null);
//		assertFalse(ci1.equals(ci2));
//		ci2.setUtilisateur(u2);
//		assertTrue(ci1.equals(ci2));
//		
//		ci2.setDate(c2);
//		assertFalse(ci1.equals(ci2));
//		ci1.setDate(c2);
//		assertTrue(ci1.equals(ci2));
//		ci2.setDate(null);
//		assertFalse(ci1.equals(ci2));
//		ci2.setDate(c2);
//		assertTrue(ci1.equals(ci2));
//	
//		ci2.setEmetteurIdent(emetId2);
//		assertFalse(ci1.equals(ci2));
//		ci1.setEmetteurIdent(emetId2);
//		assertTrue(ci1.equals(ci2));
//		ci2.setEmetteurIdent(null);
//		assertFalse(ci1.equals(ci2));
//		ci2.setEmetteurIdent(emetId2);
//		assertTrue(ci1.equals(ci2));
//
//		Categorie c3 = new Categorie();
//		assertFalse(ci1.equals(c3));
//	}
//	
//	public void testHashCode() throws ParseException {
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//		
//		ConsultationIntf ci1 = new ConsultationIntf();
//		ConsultationIntf ci2 = new ConsultationIntf();
//		
//		String id1 = "id1";
//		String id2 = "id2";
//		Utilisateur u1 = utilisateurDao.findById(1);
//		Utilisateur u2 = utilisateurDao.findById(2);
//		String emetId1 = "id1";
//		String emetId2 = "id2";
//		Calendar c1 = Calendar.getInstance();
//		c1.setTime(sdf.parse("12/12/2015 12:12:00"));
//		Calendar c2 = Calendar.getInstance();
//		c2.setTime(sdf.parse("13/12/2015 12:12:00"));
//		
//		//null
//		assertTrue(ci1.hashCode() == ci2.hashCode());
//		
//		//Identification
//		ci2.setIdentification(id1);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setIdentification(id2);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setIdentification(id1);
//		assertTrue(ci1.hashCode() == ci2.hashCode());
//		
//		//Utilisateur
//		ci2.setUtilisateur(u1);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setUtilisateur(u2);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setUtilisateur(u1);
//		assertTrue(ci1.hashCode() == ci2.hashCode());
//		
//		//EmetId
//		ci2.setEmetteurIdent(emetId1);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setEmetteurIdent(emetId2);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setEmetteurIdent(emetId1);
//		assertTrue(ci1.hashCode() == ci2.hashCode());
//		
//		//date
//		ci2.setDate(c1);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setDate(c2);
//		assertFalse(ci1.hashCode() == ci2.hashCode());
//		ci1.setDate(c1);
//		assertTrue(ci1.hashCode() == ci2.hashCode());
//		
//		// un même objet garde le même hashcode dans le temps
//		int hash = ci1.hashCode();
//		assertTrue(hash == ci1.hashCode());
//		assertTrue(hash == ci1.hashCode());
//		assertTrue(hash == ci1.hashCode());
//		assertTrue(hash == ci1.hashCode());
//		
//	}

}
