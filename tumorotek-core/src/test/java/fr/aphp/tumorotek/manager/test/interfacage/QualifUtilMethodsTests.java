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
package fr.aphp.tumorotek.manager.test.interfacage;

import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * Package utilitaire de methodes permettant de tester le  
 * mapping sgl-tk.
 * Cette classe de test n'est donc pas intégré au jeu de test lancé
 * par Maven.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.13
 *
 */
public class QualifUtilMethodsTests extends AbstractManagerTest4 {
	
	@Autowired
	private InterfacageParsingUtils interfacageParsingUtils;
	@Autowired
	private EmetteurDao emetteurDao;
	@Autowired
	private DossierExterneManager dossierExterneManager;
	@Autowired
	private BlocExterneManager blocExterneManager;
	@Autowired
	private ValeurExterneManager valeurExterneManager;
	@Autowired
	private ChampEntiteDao champEntiteDao;
	
	/**
	 * teste diamic-mapping.xml + fichier .hl7
	 * @throws Exception
	 */
	@Test
	public void testParseInterfacageXmlFile2() throws Exception {
		Emetteur e1 = emetteurDao.findById(1);
		// changer ICI 
		String folder = "/home/mathieu/partageXP/diamic-qulif/";
		Integer nbD = dossierExterneManager.findAllObjectsManager().size();
		Integer nbB = blocExterneManager.findAllObjectsManager().size();
		Integer nbV = valeurExterneManager.findAllObjectsManager().size();
		
		InputStream input = null;
		try {
			// changer ICI
			//input = new FileInputStream(folder + "000000044_S1500030.hl7");
			////InputStream input = new FileInputStream(folder + "apix-message.hl7");
			//byte[] ba = new byte[input.available()];
			//input.read(ba);
			//String str = new String(ba);

			String str = lireDonnees("DIAMIC/000000044_S1500030.hl7");
			
			// changer ICI
			DossierExterne dossier = interfacageParsingUtils
				.parseInterfacageXmlFile(
					folder + "diamic-mapping2.xml", str, e1, false, 100);

			StringBuilder bld;
			for (BlocExterne bloc : blocExterneManager.findByDossierExterneManager(dossier)) {
				System.out.println("--");
				for (ValeurExterne val : 
							valeurExterneManager.findByBlocExterneManager(bloc)) {
					bld = new StringBuilder();
					bld.append(champEntiteDao.findById(val.getChampEntiteId()).getNom());
					bld.append(" : ");
					bld.append(val.getValeur());
					System.out.println(bld.toString());
				}
			}
				
			// suppression
			dossierExterneManager.removeObjectManager(dossier);
			assertTrue(dossierExterneManager.findAllObjectsManager().size() 
					== nbD);
			assertTrue(blocExterneManager.findAllObjectsManager().size() 
					== nbB);
			assertTrue(valeurExterneManager.findAllObjectsManager().size() 
					== nbV);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		} finally {
			if (input != null) {input.close();}
		}
	}

	public String lireDonnees(String fileName) throws Exception {
		/*return lireDonnees(fileName, this.getClass().getClassLoader());
		lireDonnees(fileName, "UTF-8", classLoader)*/
		URL fileUrl = this.getClass().getClassLoader().getResource("data/" + fileName);
		//return readFile(fileUrl, charEncoding);
		File file = new File(fileUrl.toURI());
		InputStream fileReader = new FileInputStream(file);
		return IOUtils.toString(fileReader, "UTF-8");
	}
}