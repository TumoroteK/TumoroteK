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
package fr.aphp.tumorotek.manager.impl.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.XSLTransformException;
import org.jdom.transform.XSLTransformer;
import org.springframework.core.io.Resource;

import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.xml.XmlUtils;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.utils.Utils;

public class XmlUtilsImpl implements XmlUtils {
	
	private Log log = LogFactory.getLog(XmlUtils.class);
	
	private Resource xslPdfMainResource;
	private Resource xslHtmlMainResource;
	private Resource xslHtmlBoiteResource;
	private Resource xslHtmlContenuResource;
	private Resource xslPdfAccordTransfert;
	private EmplacementManager emplacementManager;

	public void setXslPdfMainResource(Resource xResource) {
		this.xslPdfMainResource = xResource;
	}

	public void setXslHtmlMainResource(Resource xResource) {
		this.xslHtmlMainResource = xResource;
	}

	public void setXslHtmlBoiteResource(Resource xResource) {
		this.xslHtmlBoiteResource = xResource;
	}

	public void setXslHtmlContenuResource(Resource xslResource) {
		this.xslHtmlContenuResource = xslResource;
	}

	public void setXslPdfAccordTransfert(Resource xslResource) {
		this.xslPdfAccordTransfert = xslResource;
	}

	public void setEmplacementManager(EmplacementManager eManager) {
		this.emplacementManager = eManager;
	}

	@Override
	public Document createJDomDocument() {
		Document document = null;
		// Racine du document
		Element racine = new Element("Impression");
		// DTD
   		//DocType dtd = new DocType(
   				//"Impression", "src/main/resources/impression.dtd");
   		// Nouveau document JDOM
   		document = new Document(racine);
   		return document;
	}

	@Override
	public void addCoupleValeur(Element parent, CoupleValeur couple) {
		if (parent != null && couple != null) {
			// creation du couplevaleurs
			Element coupleValeurs = new Element("CoupleValeurs");
			
			// ajout dunom de la valeur
			if (!couple.getObligatoire()) {
				Element nom = new Element("NomValeur");
				nom.setText(couple.getNomValeur());
				coupleValeurs.addContent(nom);
			} else {
				Element nom = new Element("NomValeurObligatoire");
				nom.setText(couple.getNomValeur());
				coupleValeurs.addContent(nom);
			}
			
			// ajout de la valeur
			if (!couple.getAnonyme()) {
				Element valeur = new Element("Valeur");
				valeur.setText(couple.getValeur());
				coupleValeurs.addContent(valeur);
			} else {
				Element valeur = new Element("ValeurAnonyme");
				coupleValeurs.addContent(valeur);
			}
			
			// ajout au parent
			parent.addContent(coupleValeurs);
		}
	}
	
	@Override
	public void addCoupleSimpleValeur(
			Element parent, CoupleSimpleValeur couple) {
		if (parent != null && couple != null) {
			// creation du couplevaleurs
			Element coupleValeurs = new Element("CoupleSimpleValeurs");
			
			// ajout dunom de la valeur
			if (!couple.getObligatoire()) {
				Element nom = new Element("NomValeur");
				nom.setText(couple.getNomValeur());
				coupleValeurs.addContent(nom);
			} else {
				Element nom = new Element("NomValeurObligatoire");
				nom.setText(couple.getNomValeur());
				coupleValeurs.addContent(nom);
			}
			
			// ajout de la valeur
			if (!couple.getAnonyme()) {
				Element valeur = new Element("Valeur");
				valeur.setText(couple.getValeur());
				coupleValeurs.addContent(valeur);
			} else {
				Element valeur = new Element("ValeurAnonyme");
				coupleValeurs.addContent(valeur);
			}
			
			// ajout au parent
			parent.addContent(coupleValeurs);
		}
	}

	@Override
	public void addLigne(Element parent, LigneParagraphe ligne) {
		if (parent != null && ligne != null) {
			Element ligneFirst = new Element("Ligne");
			parent.addContent(ligneFirst);
			Element lignePara = new Element("LigneParagraphe");
			lignePara.setAttribute("id", ligne.getId());
			for (int i = 0; i < ligne.getValeurs().length; i++) {
				addCoupleValeur(lignePara, ligne.getValeurs()[i]);
			}
			ligneFirst.addContent(lignePara);
		}
	}
	
	@Override
	public void addLigneSimple(Element parent, LigneSimpleParagraphe ligne) {
		if (parent != null && ligne != null) {
			Element ligneFirst = new Element("Ligne");
			parent.addContent(ligneFirst);
			Element lignePara = new Element("LigneSimpleParagraphe");
			addCoupleSimpleValeur(lignePara, ligne.getValeur());
			ligneFirst.addContent(lignePara);
		}
	}
	
	@Override
	public void addLigneDeuxColonnesParagraphe(Element parent,
			LigneDeuxColonnesParagraphe ligne) {
		if (parent != null && ligne != null) {
			Element ligneFirst = new Element("Ligne");
			parent.addContent(ligneFirst);
			Element lignePara = new Element("LigneDeuxColonnesParagraphe");
			addCoupleValeur(lignePara, ligne.getValeurs());
			ligneFirst.addContent(lignePara);
		}
	}
	
	@Override
	public void addLigneListe(Element parent, LigneListe ligne) {
		if (parent != null && ligne != null) {
			Element ligneListe = new Element("LigneListe");
			for (int i = 0; i < ligne.getCellules().length; i++) {
				Element cellule = new Element("Cellule");
				cellule.setText(ligne.getCellules()[i]);
				ligneListe.addContent(cellule);
			}
			parent.addContent(ligneListe);
		}
	}
	
	@Override
	public void addColonnesListe(Element parent, int nb) {
		if (parent != null) {
			Element colonnes = new Element("Colonnes");
			for (int i = 0; i < nb; i++) {
				Element colonne = new Element("Colonne");
				colonnes.addContent(colonne);
			}
			parent.addContent(colonnes);
		}
	}
	
	@Override
	public void addEnteteListe(Element parent, EnteteListe entete) {
		if (parent != null && entete != null) {
			Element enteteListe = new Element("EnteteListe");
			for (int i = 0; i < entete.getColonnes().length; i++) {
				Element colonne = new Element("NomColonne");
				colonne.setText(entete.getColonnes()[i]);
				enteteListe.addContent(colonne);
			}
			parent.addContent(enteteListe);
		}
	}

	@Override
	public void addSousParagraphe(Element parent, SousParagraphe sous) {
		if (parent != null && sous != null) {
			Element sousPara = new Element("SousParagraphe");
			if (sous.getTitre() != null) {
				Element titre = new Element("TitreSousParagraphe");
				titre.setText(sous.getTitre());
				sousPara.addContent(titre);
			}
			
			if (sous.getInconnu() != null) {
				Element inconnu = new Element("Inconnu");
				inconnu.setText(sous.getInconnu());
				sousPara.addContent(inconnu);
			}
			
			if (sous.getLignes() != null) {
				for (int i = 0; i < sous.getLignes().length; i++) {
					if (sous.getLignes()[i].getClass().getSimpleName()
							.equals("LigneParagraphe")) {
						addLigne(sousPara, (LigneParagraphe) 
								sous.getLignes()[i]);
					} else if (sous.getLignes()[i].getClass().getSimpleName()
							.equals("LigneSimpleParagraphe")) {
						addLigneSimple(sousPara, (LigneSimpleParagraphe) 
								sous.getLignes()[i]);
					} else if (sous.getLignes()[i].getClass().getSimpleName()
							.equals("LigneDeuxColonnesParagraphe")) {
						addLigneDeuxColonnesParagraphe(sousPara, 
								(LigneDeuxColonnesParagraphe) 
								sous.getLignes()[i]);
					}
				}
			}
			
			if (sous.getListe() != null) {
				addListe(sousPara, sous.getListe());
			}
			parent.addContent(sousPara);
		}
	}

	@Override
	public void addParagraphe(Element parent, Paragraphe para) {
		if (parent != null && para != null) {
			Element eltPara = new Element("Paragraphe");
			if (para.getTitre() != null) {
				Element titre = new Element("TitreParagraphe");
				titre.setText(para.getTitre());
				eltPara.addContent(titre);
			}
			
			if (para.getInconnu() != null) {
				Element inconnu = new Element("Inconnu");
				inconnu.setText(para.getInconnu());
				eltPara.addContent(inconnu);
			}
			
			if (para.getLignes() != null) {
				for (int i = 0; i < para.getLignes().length; i++) {
					if (para.getLignes()[i].getClass().getSimpleName()
							.equals("LigneParagraphe")) {
						addLigne(eltPara, (LigneParagraphe) 
								para.getLignes()[i]);
					} else if (para.getLignes()[i].getClass().getSimpleName()
								.equals("LigneSimpleParagraphe")) {
							addLigneSimple(eltPara, (LigneSimpleParagraphe) 
									para.getLignes()[i]);
					} else if (para.getLignes()[i].getClass().getSimpleName()
							.equals("LigneDeuxColonnesParagraphe")) {
						addLigneDeuxColonnesParagraphe(eltPara, 
								(LigneDeuxColonnesParagraphe) 
								para.getLignes()[i]);
					}
				}
			}
			
			if (para.getSousParagraphes() != null) {
				for (int i = 0; i < para.getSousParagraphes().length; i++) {
					addSousParagraphe(
							eltPara, para.getSousParagraphes()[i]);
				}
			}
			
			if (para.getListe() != null) {
				addListe(eltPara, para.getListe());
			}
			parent.addContent(eltPara);
		}
	}
	
	@Override
	public void addListe(Element parent, ListeElement liste) {
		if (parent != null && liste != null) {
			Element eltListe = new Element("Liste");
			if (liste.getTitre() != null) {
				Element titre = new Element("TitreParagraphe");
				titre.setText(liste.getTitre());
				eltListe.addContent(titre);
			}
			
			
			if (liste.getEntete() != null) {
				int nb = liste.getEntete().getColonnes().length;
				addColonnesListe(eltListe, nb);
				addEnteteListe(eltListe, liste.getEntete());
			}
			
			if (liste.getLignes() != null) {
				for (int i = 0; i < liste.getLignes().length; i++) {
					addLigneListe(eltListe, liste.getLignes()[i]);
				}
			}
			parent.addContent(eltListe);
		}
	}

	@Override
	public Element addPage(Element root, String titre) {
		if (root != null) {
			Element page = new Element("Page");
			
			if (titre != null) {
				Element eltTitre = new Element("Titre");
				eltTitre.setText(titre);
				page.addContent(eltTitre);
			}
			
			root.addContent(page);
			return page;
		} else {
			return null;
		}
	}

	@Override
	public void addTitreForDocument(Element root, String titre) {
		if (root != null) {
			Element eltTitre = new Element("Titre");
			eltTitre.setText(titre);
			root.addContent(eltTitre);
		}
	}
	
	@Override
	public void addHautDePage(Element parent, String legende,
			boolean addImage, String adr) {
		if (parent != null) {
			Element hautDePage = new Element("HautDePage");
			hautDePage.setText(legende);
			parent.addContent(hautDePage);
			
			if (addImage && adr != null) {
				Element image = new Element("ImageHautDePage");
				image.setText(adr);
				hautDePage.addContent(image);
			}
		}
	}
	
	@Override
	public void addBasDePage(Element parent, String legende) {
		if (parent != null) {
			Element basDePage = new Element("BasDePage");
			basDePage.setText(legende);
			parent.addContent(basDePage);
		}
	}

	@Override
	public void createXMLFile(Document doc, String folder, String file) {
		try {
	      	File fileFolder = new File(folder);
	      	try {
		      	if (!fileFolder.exists()) {
		      		fileFolder.mkdir();
		      	}
	      	} catch (Exception e) {
	      		log.error("Erreur dans la creation du fichier XML " 
	      				+ e.getMessage());
	      	}
	      	
	      	String fileTitle = folder + file;
	      	File xml = new File(fileTitle);
	      	
	         //On utilise ici un affichage classique avec getPrettyFormat()
	         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	         sortie.output(doc, new FileOutputStream(xml));
	      } catch (java.io.IOException e) {
	    	  log.error("Erreur dans l'enregistrement du fichier " 
	    			  + e.getMessage());
	      }
	}

	@Override
	public org.w3c.dom.Document transformAsPdf(Document doc) {
		org.w3c.dom.Document transformed = null;		
   		
   		XSLTransformer transformer;
		try {
			InputStream inputXsl = xslPdfMainResource.getInputStream();
			//transformer = new XSLTransformer(xslPath);
			transformer = new XSLTransformer(inputXsl);
			Document docFo = transformer.transform(doc);
	   		//export le document JDOM dans un format utilisable par FOP
	   		DOMOutputter export = new DOMOutputter();
	   		transformed = export.output(docFo);
		} catch (XSLTransformException e) {
			log.error("Erreur dans la transformation du fichier " 
	    			  + e.getMessage());
			log.error(e);
		} catch (JDOMException e) {
			log.error("Erreur dans la création du fichier JDOM" 
	    			  + e.getMessage());
		} catch (IOException e) {
			log.error(e);
		}
   		
		return transformed;
	}

	@Override
	public byte[] creerHtml(Document doc) throws Exception {
		// Trandformation du Document JDom en document org.w3c.dom.Document
		DOMOutputter outputter = new DOMOutputter();
		org.w3c.dom.Document docOut = null;
		try {
			docOut = outputter.output(doc);
		} catch (JDOMException e1) {
			log.error(e1);
		}
        Source source = new DOMSource(docOut);
		
		// Création du fichier de sortie
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] results = null;
		Result resultat = new StreamResult(out);
		
		// Configuration du transformer
		TransformerFactory fabriqueT = TransformerFactory.newInstance();
		
		try {
			// on récupère la feuille xsl et on la transforme en
			// InputStream
			InputStream inputXsl = xslHtmlMainResource.getInputStream();
			StreamSource stylesource = new StreamSource(inputXsl);
			Transformer transformer;
			transformer = fabriqueT.newTransformer(stylesource);
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			// Transformation
			transformer.transform(source, resultat);
			
			results = out.toByteArray();
		} catch (TransformerConfigurationException e) {
			log.error(e);
		} catch (TransformerException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			out.close();
		}
		
		return results;
	}

	@Override
	public byte[] creerPdf(Document document) throws Exception {
		// transformation du document
		org.w3c.dom.Document transformed = transformAsPdf(document);
		
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent(); 

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] results;

		try {
			// Construct fop with desired output format and output stream
			Fop fop = fopFactory
			.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			// Setup Identity Transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			// identity transformer
			Transformer transformer = 
				factory.newTransformer();

			// Setup input for XSLT transformation
			Source src = new DOMSource(transformed);

			// Resulting SAX events (the generated FO) must 
			// be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
			
			results = out.toByteArray();
		} finally {
			out.close();
		} 

		return results;
	}
	
	@Override
	public Document createJDomDocumentBoites() {
		Document document = null;
		// Racine du document
		Element racine = new Element("ImpressionBoite");
   		document = new Document(racine);
   		return document;
	}
	
	@Override
	public Element addPageBoite(Element root, String titre) {
		if (root != null) {
			Element page = new Element("PageBoite");
			
			if (titre != null) {
				Element eltTitre = new Element("Titre");
				eltTitre.setText(titre);
				page.addContent(eltTitre);
			}
			
			root.addContent(page);
			return page;
		} else {
			return null;
		}
	}

	@Override
	public void addTitreIntermediaire(Element elt, String titre) {
		if (elt != null && titre != null) {
			Element eltTitre = new Element("TitreIntermediaire");
			eltTitre.setText(titre);
			elt.addContent(eltTitre);
		}
	}
	
	@Override
	public void addSeparateur(Element elt) {
		if (elt != null) {
			Element eltTitre = new Element("Separateur");
			elt.addContent(eltTitre);
		}
	}

	@Override
	public void addBoite(Element elt, 
			BoiteImpression boite,
			String adrImages) {
		
		if (elt != null && boite != null) {
			Element boiteElt = new Element("Boite");
			
			if (boite.getSeparateur()) {
				addSeparateur(boiteElt);
			}
			addTitreIntermediaire(boiteElt, boite.getTitreIntermediaire());
			
			Element titreModele = new Element("TitreModelisation");
			titreModele.setText(boite.getTitreModelisation());
			boiteElt.addContent(titreModele);
			
			Element titreInst = new Element("TitreInstruction");
			titreInst.setText(boite.getTitreInstructions());
			boiteElt.addContent(titreInst);
			
			createModelisation(
					boiteElt, 
					boite.getBoite(), 
					boite.getNom(), 
					boite.getPositions(),
					boite.getLegendeVide(), 
					boite.getLegendePris(), 
					boite.getLegendeSelectionne(),
					adrImages);
			addInstructions(boiteElt, boite.getInstructions());
			addListeElements(boiteElt, boite.getElements()
					, boite.getTitreListe());
			
			elt.addContent(boiteElt);
		}
	}
	
	@Override
	public void createModelisation(Element elt, Terminale boite, 
			String nom, List<Integer> positions,
			String legendeVide,
			String legendePris,
			String legendeSelectionne,
			String adrImages) {
		Element modelisationElt = new Element("Modelisation");
		Element nomElt = new Element("TitreBoite");
		nomElt.setText(nom);
		modelisationElt.addContent(nomElt);
		
		List<String> emplacements = initEmplacements(boite);
		
		if (boite.getTerminaleType().getScheme() == null) {
			
			if (boite.getTerminaleType().getHauteur() != null
					&& boite.getTerminaleType().getLongueur() != null) {
				
				// affichage des abscisses
				Element coordonnees = new Element("LigneCoordonnees");
				for (int i = 1; i <= boite.getTerminaleType()
					.getLongueur(); i++) {
					Element coord = new Element("Coordonnee");
					coord.setText(getValueAbscisse(boite, i));
					coordonnees.addContent(coord);
				}
				modelisationElt.addContent(coordonnees);
				
				int cpt = 0;
				int cptPosition = 0;
				int nbSelectionne = 0;
				List<Element> lignes = new ArrayList<Element>();
				for (int i = 0; i < boite.getTerminaleType()
					.getHauteur(); i++) {
					Element ligne = new Element("LigneBoite");
					// affichage des ordonnées
					Element coord = new Element("Coordonnee");
					coord.setText(getValueOrdonnee(boite, i + 1));
					ligne.addContent(coord);
					for (int j = 0; j < boite.getTerminaleType()
						.getLongueur(); j++) {
						String value = emplacements.get(cpt);
						Element emp = new Element("Emplacement");
						
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ "empImpressionVIDE.gif");
							vide.addContent(adresse);
							emp.addContent(vide);
						} else {
							if (positions.contains(cpt + 1)) {
								Element selectionne = 
									new Element("EmpSelectionne");
								++cptPosition;
								++nbSelectionne;
								selectionne.setText(
									String.valueOf(positions
										.indexOf(cpt + 1) + 1));
								emp.addContent(selectionne);
							} else {
								Element occ = new Element("EmpOccupe");
								occ.setText(emplacements.get(cpt));
								Element adresse = new Element("AdrImage");
								adresse.setText(adrImages 
										+ "empImpressionUSE.gif");
								occ.addContent(adresse);
								emp.addContent(occ);
							}
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					lignes.add(ligne);
				}
				// si la numérotation commence sur la 1ere ligne
				if (boite.getTerminaleType().getDepartNumHaut()) {
					for (int k = 0; k < lignes.size(); k++) {
						modelisationElt.addContent(lignes.get(k));
					}
				} else {
					// sinon on inverse l'affichage des lignes
					for (int k = lignes.size() - 1; k >= 0; k--) {
						modelisationElt.addContent(lignes.get(k));
					}
				}
			}
			
		} else {
			
			String[] values = boite.getTerminaleType()
				.getScheme()
				.split(";");
			int cpt = 0;
			int cptPosition = 0;
			int nbSelectionne = 0;
			List<Element> lignes = new ArrayList<Element>();
			if (boite.getTerminaleType().getDepartNumHaut()) {
				for (int i = 0; i < values.length; i++) {
					int nbPlaces = Integer.parseInt(values[i]);
					Element ligne = new Element("LigneBoite");
					
					for (int j = 0; j < nbPlaces; j++) {
						String value = emplacements.get(cpt);
						
						Element emp = new Element("Emplacement");
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ "empImpressionVIDE.gif");
							vide.addContent(adresse);
							emp.addContent(vide);
						} else {
							if (positions.contains(cpt + 1)) {
								Element selectionne = 
									new Element("EmpSelectionne");
								++cptPosition;
								++nbSelectionne;
								selectionne.setText(
										String.valueOf(positions
											.indexOf(cpt + 1) + 1));
								emp.addContent(selectionne);
							} else {
								Element occ = new Element("EmpOccupe");
								occ.setText(emplacements.get(cpt));
								Element adresse = new Element("AdrImage");
								adresse.setText(adrImages 
										+ "empImpressionUSE.gif");
								occ.addContent(adresse);
								emp.addContent(occ);
							}
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					lignes.add(ligne);
				}
			} else {
				for (int i = values.length - 1; i >= 0; i--) {
					int nbPlaces = Integer.parseInt(values[i]);
					Element ligne = new Element("LigneBoite");
					
					for (int j = 0; j < nbPlaces; j++) {
						String value = emplacements.get(cpt);
						
						Element emp = new Element("Emplacement");
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ "empImpressionVIDE.gif");
							vide.addContent(adresse);
							emp.addContent(vide);
						} else {
							if (positions.contains(cpt + 1)) {
								Element selectionne = 
									new Element("EmpSelectionne");
								++cptPosition;
								++nbSelectionne;
								selectionne.setText(
										String.valueOf(positions
											.indexOf(cpt + 1) + 1));
								emp.addContent(selectionne);
							} else {
								Element occ = new Element("EmpOccupe");
								occ.setText(emplacements.get(cpt));
								Element adresse = new Element("AdrImage");
								adresse.setText(adrImages 
										+ "empImpressionUSE.gif");
								occ.addContent(adresse);
								emp.addContent(occ);
							}
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					lignes.add(0, ligne);
				}
			}
			for (int i = 0; i < lignes.size(); i++) {
				modelisationElt.addContent(lignes.get(i));
			}
		}
		
		Element imageLegende1 = new Element("ImageLegendeVide");
		imageLegende1.setText(adrImages 
				+ "empImpressionVIDE.gif");
		modelisationElt.addContent(imageLegende1);
		
		Element legende1 = new Element("LegendeVide");
		legende1.setText(legendeVide);
		modelisationElt.addContent(legende1);
		
		Element imageLegende2 = new Element("ImageLegendePris");
		imageLegende2.setText(adrImages 
				+ "empImpressionUSE.gif");
		modelisationElt.addContent(imageLegende2);
		
		Element legende2 = new Element("LegendePris");
		legende2.setText(legendePris);
		modelisationElt.addContent(legende2);
		
		Element legende3 = new Element("LegendeSelectionne");
		legende3.setText(legendeSelectionne);
		modelisationElt.addContent(legende3);
		
		elt.addContent(modelisationElt);
	}

	@Override
	public void addInstructions(Element elt, List<String> instructions) {
		if (elt != null && instructions != null) {
			Element eltTitre = new Element("Instructions");
			elt.addContent(eltTitre);
			
			for (int i = 0; i < instructions.size(); i++) {
				Element instruction = new Element("Instruction");
				instruction.setText(instructions.get(i));
				eltTitre.addContent(instruction);
			}
		}
	}

	@Override
	public void addListeElements(Element elt, List<String> elements,
			String titreListe) {
		if (elt != null && elements != null) {
			Element liste = new Element("ListeElements");
			elt.addContent(liste);
			
			if (titreListe != null) {
				Element titre = new Element("TitreListe");
				titre.setText(titreListe);
				liste.addContent(titre);
			}
			
			for (int i = 0; i < elements.size(); i++) {
				Element value = new Element("Element");
				value.setText(elements.get(i));
				liste.addContent(value);
			}
		}
	}
	
	/**
	 * Initialise les listes d'emplacements.
	 */
	public List<String> initEmplacements(Terminale terminale) {
		List<String> results = new ArrayList<String>();
		
		// on récupère les emplacements de la boite
		List<Emplacement> emplacements = emplacementManager
			.findByTerminaleWithOrder(terminale);
		// on récupère le code de l'objet se trouvant sur chaque
		// emplacement
		List<String> codes = emplacementManager
			.getNomsForEmplacementsManager(emplacements);
		
		int cpt = 1;
		// on parcourt la liste d'emplacements
		for (int i = 0; i < emplacements.size(); i++) {
			Emplacement emp = emplacements.get(i);
			
			// si aucun emplacement n'est défini à la position "cpt"
			// on crée un decorator vide
			while (emp.getPosition() > cpt) {
				results.add("*--*");
				++cpt;
			}
			// on remplit un decorator avec l'emplacement
			if (!emp.getVide()) {
				results.add(codes.get(i));
			} else {
				results.add("*--*");
			}
			++cpt;
		}
		
		// si aucun emplacement n'est défini à la position "cpt"
		// on crée un decorator vide
		while (terminale.getTerminaleType().getNbPlaces() 
				> results.size()) {
			results.add("*--*");
			++cpt;
		}
		
		emplacements = null;
		return results;
	}
	
	/**
	 * Initialise les listes d'emplacements.
	 */
	public List<String> initEntites(Terminale terminale) {
		List<String> results = new ArrayList<String>();
		
		// on récupère les emplacements de la boite
		List<Emplacement> emplacements = emplacementManager
			.findByTerminaleWithOrder(terminale);
		
		int cpt = 1;
		// on parcourt la liste d'emplacements
		for (int i = 0; i < emplacements.size(); i++) {
			Emplacement emp = emplacements.get(i);
			
			// si aucun emplacement n'est défini à la position "cpt"
			// on crée un decorator vide
			while (emp.getPosition() > cpt) {
				results.add("*--*");
				++cpt;
			}
			// on remplit un decorator avec l'emplacement
			if (!emp.getVide()) {
				results.add(emp.getEntite().getNom());
			} else {
				results.add("*--*");
			}
			++cpt;
		}
		
		// si aucun emplacement n'est défini à la position "cpt"
		// on crée un decorator vide
		while (terminale.getTerminaleType().getNbPlaces() 
				> results.size()) {
			results.add("*--*");
			++cpt;
		}
		
		emplacements = null;
		return results;
	}
	
	/**
	 * Initialise les listes d'emplacements.
	 */
	public List<String> initTypes(Terminale terminale) {
		List<String> results = new ArrayList<String>();
		
		// on récupère les emplacements de la boite
		List<Emplacement> emplacements = emplacementManager
			.findByTerminaleWithOrder(terminale);
		// on récupère le type de l'objet se trouvant sur chaque
		// emplacement
		List<String> typesObjs = emplacementManager
			.getTypesForEmplacementsManager(emplacements);
		
		int cpt = 1;
		// on parcourt la liste d'emplacements
		for (int i = 0; i < emplacements.size(); i++) {
			Emplacement emp = emplacements.get(i);
			
			// si aucun emplacement n'est défini à la position "cpt"
			// on crée un decorator vide
			while (emp.getPosition() > cpt) {
				results.add("*--*");
				++cpt;
			}
			// on remplit un decorator avec l'emplacement
			if (!emp.getVide()) {
				results.add(typesObjs.get(i));
			} else {
				results.add("*--*");
			}
			++cpt;
		}
		
		// si aucun emplacement n'est défini à la position "cpt"
		// on crée un decorator vide
		while (terminale.getTerminaleType().getNbPlaces() 
				> results.size()) {
			results.add("*--*");
			++cpt;
		}
		
		emplacements = null;
		return results;
	}
	
	@Override
	public byte[] creerBoiteHtml(Document doc) throws Exception {
		// Trandformation du Document JDom en document org.w3c.dom.Document
		DOMOutputter outputter = new DOMOutputter();
		org.w3c.dom.Document docOut = null;
		try {
			docOut = outputter.output(doc);
		} catch (JDOMException e1) {
			log.error(e1);
		}
        Source source = new DOMSource(docOut);
		
		// Création du fichier de sortie
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] results = null;
		Result resultat = new StreamResult(out);
		
		// Configuration du transformer
		TransformerFactory fabriqueT = TransformerFactory.newInstance();
		
		try {
			// on récupère la feuille xsl et on la transforme en
			// InputStream
			InputStream inputXsl = xslHtmlBoiteResource.getInputStream();
			StreamSource stylesource = new StreamSource(inputXsl);
			Transformer transformer;
			transformer = fabriqueT.newTransformer(stylesource);
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			// Transformation
			transformer.transform(source, resultat);
			
			results = out.toByteArray();
		} catch (TransformerConfigurationException e) {
			log.error(e);
		} catch (TransformerException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			out.close();
		}
		
		return results;
	}
	
	@Override
	public Document createJDomDocumentContenuBoite() {
		Document document = null;
		// Racine du document
		Element racine = new Element("ImpressionContenu");
   		document = new Document(racine);
   		return document;
	}
	
	@Override
	public Element addPageContenuBoite(Element root, String titre) {
		if (root != null) {
			Element page = new Element("PageContenuBoite");
			
			if (titre != null) {
				Element eltTitre = new Element("Titre");
				eltTitre.setText(titre);
				page.addContent(eltTitre);
			}
			
			root.addContent(page);
			return page;
		} else {
			return null;
		}
	}
	
	@Override
	public void addListeParents(Element elt, List<String> listeParents) {
		if (elt != null && listeParents != null) {
			Element eltParent = new Element("ListeParents");
			elt.addContent(eltParent);
			
			for (int i = 0; i < listeParents.size(); i++) {
				Element parent = new Element("Parent");
				parent.setText(listeParents.get(i));
				eltParent.addContent(parent);
			}
		}
	}

	@Override
	public void addListeNombres(Element elt, List<String> listeNombres) {
		if (elt != null && listeNombres != null) {
			Element eltParent = new Element("ListeNombres");
			elt.addContent(eltParent);
			
			for (int i = 0; i < listeNombres.size(); i++) {
				Element nb = new Element("Nombre");
				nb.setText(listeNombres.get(i));
				eltParent.addContent(nb);
			}
		}
	}

	@Override
	public void createContenu(Element elt, Terminale boite, 
			String valeurVide, String adrImages,
			Hashtable<String, String> echantillonTypesCouleurs,
			String echantillonCouleur,
			Hashtable<String, String> prodDeriveTypesCouleurs,
			String prodDeriveCouleur) {
		Element modelisationElt = new Element("Modelisation");
		
		List<String> emplacements = initEmplacements(boite);
		List<String> types = initTypes(boite);
		List<String> entites = initEntites(boite);
		
		if (boite.getTerminaleType().getScheme() == null) {
			
			if (boite.getTerminaleType().getHauteur() != null
					&& boite.getTerminaleType().getLongueur() != null) {
				
				// affichage des abscisses
				Element coordonnees = new Element("LigneCoordonnees");
				for (int i = 1; i <= boite.getTerminaleType()
					.getLongueur(); i++) {
					Element coord = new Element("Coordonnee");
					coord.setText(getValueAbscisse(boite, i));
					coordonnees.addContent(coord);
				}
				modelisationElt.addContent(coordonnees);
				
				int cpt = 0;
				List<Element> lignes = new ArrayList<Element>();
				for (int i = 0; i < boite.getTerminaleType()
					.getHauteur(); i++) {
					Element ligne = new Element("LigneBoite");
					// affichage des ordonnées
					Element coord = new Element("Coordonnee");
					coord.setText(getValueOrdonnee(boite, i + 1));
					ligne.addContent(coord);
					for (int j = 0; j < boite.getTerminaleType()
						.getLongueur(); j++) {
						String value = emplacements.get(cpt);
						Element emp = new Element("Emplacement");
						Element position = new Element("Position");
						position.setText(String.valueOf(cpt + 1));
						Element nom = new Element("NomEmplacement");
						Element type = new Element("TypeEmplacement");
						
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							nom.setText(valeurVide);
							vide.addContent(position);
							vide.addContent(nom);
							vide.addContent(type);
							emp.addContent(vide);
						} else {
							Element occ = new Element("EmpOccupe");
							nom.setText(emplacements.get(cpt));
							type.setText(types.get(cpt));
							occ.addContent(position);
							occ.addContent(nom);
							occ.addContent(type);
							emp.addContent(occ);
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ getImageSrc(types.get(cpt), 
									entites.get(cpt), 
									echantillonTypesCouleurs, 
									echantillonCouleur, 
									prodDeriveTypesCouleurs, 
									prodDeriveCouleur));
							occ.addContent(adresse);
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					//modelisationElt.addContent(ligne);
					lignes.add(ligne);
				}
				// si la numérotation commence sur la 1ere ligne
				if (boite.getTerminaleType().getDepartNumHaut()) {
					for (int k = 0; k < lignes.size(); k++) {
						modelisationElt.addContent(lignes.get(k));
					}
				} else {
					// sinon on inverse l'affichage des lignes
					for (int k = lignes.size() - 1; k >= 0; k--) {
						modelisationElt.addContent(lignes.get(k));
					}
				}
			}
			
		} else {
			
			String[] values = boite.getTerminaleType()
				.getScheme()
				.split(";");
			int cpt = 0;
			List<Element> lignes = new ArrayList<Element>();
			if (boite.getTerminaleType().getDepartNumHaut()) {
				for (int i = 0; i < values.length; i++) {
					int nbPlaces = Integer.parseInt(values[i]);
					Element ligne = new Element("LigneBoite");
					
					for (int j = 0; j < nbPlaces; j++) {
						String value = emplacements.get(cpt);
						
						Element emp = new Element("Emplacement");
						Element position = new Element("Position");
						position.setText(String.valueOf(cpt + 1));
						Element nom = new Element("NomEmplacement");
						Element type = new Element("TypeEmplacement");
						
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							nom.setText(valeurVide);
							vide.addContent(position);
							vide.addContent(nom);
							vide.addContent(type);
							emp.addContent(vide);
						} else {
							Element occ = new Element("EmpOccupe");
							nom.setText(emplacements.get(cpt));
							type.setText(types.get(cpt));
							occ.addContent(position);
							occ.addContent(nom);
							occ.addContent(type);
							emp.addContent(occ);
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ getImageSrc(types.get(cpt), 
									entites.get(cpt), 
									echantillonTypesCouleurs, 
									echantillonCouleur, 
									prodDeriveTypesCouleurs, 
									prodDeriveCouleur));
							occ.addContent(adresse);
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					//modelisationElt.addContent(ligne);
					lignes.add(ligne);
				}
			} else {
				for (int i = values.length - 1; i >= 0; i--) {
					int nbPlaces = Integer.parseInt(values[i]);
					Element ligne = new Element("LigneBoite");
					
					for (int j = 0; j < nbPlaces; j++) {
						String value = emplacements.get(cpt);
						
						Element emp = new Element("Emplacement");
						Element position = new Element("Position");
						position.setText(String.valueOf(cpt + 1));
						Element nom = new Element("NomEmplacement");
						
						if (value.equals("*--*")) {
							Element vide = new Element("EmpLibre");
							nom.setText(valeurVide);
							vide.addContent(position);
							vide.addContent(nom);
							emp.addContent(vide);
						} else {
							Element occ = new Element("EmpOccupe");
							nom.setText(emplacements.get(cpt));
							occ.addContent(position);
							occ.addContent(nom);
							emp.addContent(occ);
							Element adresse = new Element("AdrImage");
							adresse.setText(adrImages 
									+ getImageSrc(types.get(cpt), 
									entites.get(cpt), 
									echantillonTypesCouleurs, 
									echantillonCouleur, 
									prodDeriveTypesCouleurs, 
									prodDeriveCouleur));
							occ.addContent(adresse);
						}
						ligne.addContent(emp);
						
						++cpt;
					}
					//modelisationElt.addContent(ligne);
					lignes.add(0, ligne);
				}
			}
			for (int i = 0; i < lignes.size(); i++) {
				modelisationElt.addContent(lignes.get(i));
			}
		}
		
		elt.addContent(modelisationElt);
	}
	
	public String getImageSrc(String type, String entite,
			Hashtable<String, String> echantillonTypesCouleurs,
			String echantillonCouleur,
			Hashtable<String, String> prodDeriveTypesCouleurs,
			String prodDeriveCouleur) {
		StringBuffer sb = new StringBuffer();
		
		// si l'emplacement contient un échantillon
		if (entite.equals("Echantillon")) {
			
			if (echantillonTypesCouleurs.containsKey(type)) {
				sb.append(echantillonTypesCouleurs.get(type));
			} else if (echantillonCouleur != null) {
				sb.append(echantillonCouleur);
			} else {
				sb.append("VERT");
			}
			
		} else if (entite.equals("ProdDerive")) {
			// si l'emplacement contient un dérivé
			if (prodDeriveTypesCouleurs.containsKey(type)) {
				sb.append(prodDeriveTypesCouleurs.get(type));
			} else if (prodDeriveCouleur != null) {
				sb.append(prodDeriveCouleur);
			} else {
				sb.append("VERT");
			}
		}
		
		sb.append(".png");
		return sb.toString();
	}
	
	/**
	 * Retourne la valeur à afficher pour une abscisse.
	 */
	public String getValueAbscisse(Terminale terminale, 
			Integer num) {
		if (terminale.getTerminaleNumerotation()
				.getColonne().equals("CAR")) {
			return Utils.createListChars(num, null, 
					new ArrayList<String>()).get(num - 1);
		} else {
			return String.valueOf(num);
		}
	}
	
	/**
	 * Retourne la valeur à afficher pour une ordonnée.
	 */
	public String getValueOrdonnee(Terminale terminale,
			Integer num) {
		if (terminale.getTerminaleNumerotation()
				.getLigne().equals("CAR")) {
			return Utils.createListChars(num, null, 
					new ArrayList<String>()).get(num - 1);
		} else {
			return String.valueOf(num);
		}
	}

	@Override
	public void addBoiteContenu(Element elt, BoiteContenu boite,
			String adrImages,
			Hashtable<String, String> echantillonTypesCouleurs,
			String echantillonCouleur,
			Hashtable<String, String> prodDeriveTypesCouleurs,
			String prodDeriveCouleur) {
		if (elt != null && boite != null) {
			Element boiteElt = new Element("Contenu");
			
			addListeParents(boiteElt, boite.getListeParents());
			
			createContenu(boiteElt, boite.getBoite(), 
					boite.getValeurLibre(), adrImages,
					echantillonTypesCouleurs,
					echantillonCouleur,
					prodDeriveTypesCouleurs,
					prodDeriveCouleur);
			
			addListeNombres(boiteElt, boite.getListeNombres());
			
			elt.addContent(boiteElt);
		}
	}

	@Override
	public byte[] creerContenuHtml(Document doc) throws Exception {
		// Trandformation du Document JDom en document org.w3c.dom.Document
		DOMOutputter outputter = new DOMOutputter();
		org.w3c.dom.Document docOut = null;
		try {
			docOut = outputter.output(doc);
		} catch (JDOMException e1) {
			log.error(e1);
		}
        Source source = new DOMSource(docOut);
		
		// Création du fichier de sortie
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] results = null;
		Result resultat = new StreamResult(out);
		
		// Configuration du transformer
		TransformerFactory fabriqueT = TransformerFactory.newInstance();
		
		try {
			// on récupère la feuille xsl et on la transforme en
			// InputStream
			InputStream inputXsl = xslHtmlContenuResource.getInputStream();
			StreamSource stylesource = new StreamSource(inputXsl);
			Transformer transformer;
			transformer = fabriqueT.newTransformer(stylesource);
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			// Transformation
			transformer.transform(source, resultat);
			
			results = out.toByteArray();
		} catch (TransformerConfigurationException e) {
			log.error(e);
		} catch (TransformerException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			out.close();
		}
		
		return results;
	}

	@Override
	public void addBlocPrincipal(Element parent, BlocPrincipal bloc) {
		if (parent != null && bloc != null) {
			Element eltBloc = new Element("BlocPrincipal");
			if (bloc.getNomBloc() != null) {
				Element nom = new Element("NomBloc");
				nom.setText(bloc.getNomBloc());
				eltBloc.addContent(nom);
			}
			
			if (bloc.getLignes() != null) {
				for (int i = 0; i < bloc.getLignes().length; i++) {
					addLigneAccord(eltBloc, bloc.getLignes()[i]);
				}
			}
			parent.addContent(eltBloc);
		}
	}
	
	@Override
	public void addCoupleAccordValeur(Element parent, 
			CoupleAccordValeur couple) {
		if (parent != null && couple != null) {
			// creation du couplevaleurs
			Element coupleValeurs = new Element("CoupleAccordValeur");
			
			// ajout dunom de la valeur
			Element nom = new Element("NomValeur");
			nom.setText(couple.getNomValeur());
			coupleValeurs.addContent(nom);
			
			Element valeurs = new Element("Valeurs");
			if (couple.getValeurs() != null) {
				for (int i = 0; i < couple.getValeurs().length; i++) {
					Element valeur = new Element("Valeur");
					valeur.setText(couple.getValeurs()[i]);
					valeurs.addContent(valeur);
				}
			}
			coupleValeurs.addContent(valeurs);	
			// ajout au parent
			parent.addContent(coupleValeurs);
		}
	}

	@Override
	public void addLigneAccord(Element parent, LigneAccord ligne) {
		if (parent != null && ligne != null) {
			Element lignePara = new Element("LigneAccord");
			for (int i = 0; i < ligne.getValeurs().length; i++) {
				addCoupleAccordValeur(lignePara, ligne.getValeurs()[i]);
			}
			parent.addContent(lignePara);
		}
	}

	@Override
	public void addTableau(Element parent, BlocPrincipal[] blocs) {
		if (parent != null && blocs != null) {
			Element eltTableau = new Element("Tableau");

			for (int i = 0; i < blocs.length; i++) {
				addBlocPrincipal(eltTableau, blocs[i]);
			}
			parent.addContent(eltTableau);
		}
	}

	@Override
	public Document createJDomAccordTranfert() {
		Document document = null;
		// Racine du document
		Element racine = new Element("AccordTransfert");
   		document = new Document(racine);
   		return document;
	}
	
	@Override
	public void addValeursSignatures(Element parent, 
			ValeursSignatures valeurs) {
		if (parent != null && valeurs != null) {
			// creation du couplevaleurs
			Element coupleValeurs = new Element("ValeursSignatures");
			
			// ajout de la valeur1
			Element valeur1 = new Element("Valeur1");
			valeur1.setText(valeurs.getValeur1());
			coupleValeurs.addContent(valeur1);
			
			// ajout de la valeur2
			Element valeur2 = new Element("Valeur2");
			valeur2.setText(valeurs.getValeur2());
			coupleValeurs.addContent(valeur2);
			
			// ajout au parent
			parent.addContent(coupleValeurs);
		}
	}
	
	@Override
	public void addListeSignature(Element parent, ListeSignature liste) {
		if (parent != null && liste != null) {
			Element listeElt = new Element("ListeSignature");
			for (int i = 0; i < liste.getValeurs().length; i++) {
				addValeursSignatures(listeElt, liste.getValeurs()[i]);
			}
			parent.addContent(listeElt);
		}
	}
	
	@Override
	public void addSignatures(Element parent, Signatures signatures) {
		if (parent != null && signatures != null) {
			Element eltSignatures = new Element("Signatures");
			if (signatures.getTitre1() != null
					&& signatures.getTitre2() != null) {
				Element entete = new Element("Entete");
				
				Element titre1 = new Element("Titre1");
				titre1.setText(signatures.getTitre1());
				entete.addContent(titre1);
				
				Element titre2 = new Element("Titre2");
				titre2.setText(signatures.getTitre2());
				entete.addContent(titre2);
				
				eltSignatures.addContent(entete);
			}
			
			if (signatures.getListe() != null) {
				for (int i = 0; i < signatures.getListe().length; i++) {
					addListeSignature(eltSignatures, 
							signatures.getListe()[i]);
				}
			}
			
			parent.addContent(eltSignatures);
		}
	}

	@Override
	public byte[] creerAccordTransfertPdf(Document document) throws Exception {
		// transformation du document
		org.w3c.dom.Document transformed = transformAsAccordPdf(document);
		
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent(); 

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] results;

		try {
			// Construct fop with desired output format and output stream
			Fop fop = fopFactory
			.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			// Setup Identity Transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			// identity transformer
			Transformer transformer = 
				factory.newTransformer();

			// Setup input for XSLT transformation
			Source src = new DOMSource(transformed);

			// Resulting SAX events (the generated FO) must 
			// be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
			
			results = out.toByteArray();
		} finally {
			out.close();
		} 

		return results;
	}

	@Override
	public org.w3c.dom.Document transformAsAccordPdf(Document doc) {
		org.w3c.dom.Document transformed = null;		
   		
   		XSLTransformer transformer;
		try {
			InputStream inputXsl = xslPdfAccordTransfert.getInputStream();
			//transformer = new XSLTransformer(xslPath);
			transformer = new XSLTransformer(inputXsl);
			Document docFo = transformer.transform(doc);
	   		//export le document JDOM dans un format utilisable par FOP
	   		DOMOutputter export = new DOMOutputter();
	   		transformed = export.output(docFo);
		} catch (XSLTransformException e) {
			log.error("Erreur dans la transformation du fichier " 
	    			  + e.getMessage());
			log.error(e);
		} catch (JDOMException e) {
			log.error("Erreur dans la création du fichier JDOM" 
	    			  + e.getMessage());
		} catch (IOException e) {
			log.error(e);
		}
   		
		return transformed;
	}
	
}
