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
package fr.aphp.tumorotek.manager.xml;

import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import fr.aphp.tumorotek.manager.impl.xml.BlocPrincipal;
import fr.aphp.tumorotek.manager.impl.xml.BoiteContenu;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.impl.xml.CoupleAccordValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleSimpleValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneAccord;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneSimpleParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.ListeSignature;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.manager.impl.xml.Signatures;
import fr.aphp.tumorotek.manager.impl.xml.SousParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ValeursSignatures;
import fr.aphp.tumorotek.model.stockage.Terminale;

public interface XmlUtils
{

   /**
    * Crée un document JDOM avec sa racine et la DTD.
    * @return Document JDOM.
    */
   Document createJDomDocument();

   /**
    * Crée un document JDOM avec sa racine et la DTD.
    * @return Document JDOM.
    */
   Document createJDomAccordTranfert();

   /**
    * Crée un document JDOM avec sa racine et la DTD.
    * @return Document JDOM.
    */
   Document createJDomDocumentBoites();

   /**
    * Crée un document JDOM avec sa racine et la DTD.
    * @return Document JDOM.
    */
   Document createJDomDocumentContenuBoite();

   /**
    * Ajoute un titre au document.
    * @param root RootElement du Document.
    * @param titre Titre du document.
    */
   void addTitreForDocument(Element root, String titre);

   /**
    * Ajoute une page au document.
    * @param root RootElement du Document.
    * @param titre Titre du document.
    */
   Element addPage(Element root, String titre);

   /**
    * Ajoute un nouveau couple de valeurs à un élément du
    * document JDOM.
    * @param parent Parent auquel on va ajouter le couple de
    * valeurs.
    * @param couple Couple de valeurs à ajouter.
    */
   void addCoupleValeur(Element parent, CoupleValeur couple);

   /**
    * Ajoute un nouveau CoupleSimpleValeur à un élément du
    * document JDOM.
    * @param parent Parent auquel on va ajouter le couple de
    * valeurs.
    * @param couple CoupleSimpleValeur de valeurs à ajouter.
    */
   void addCoupleSimpleValeur(Element parent, CoupleSimpleValeur couple);

   /**
    * Ajoute une nouvelle ligne à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param ligne Ligne à ajouter.
    */
   void addLigne(Element parent, LigneParagraphe ligne);

   /**
    * Ajoute une nouvelle ligne à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param ligne Ligne à ajouter.
    */
   void addLigneSimple(Element parent, LigneSimpleParagraphe ligne);

   /**
    * Ajoute une nouvelle ligne à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param ligne Ligne à ajouter.
    */
   void addLigneDeuxColonnesParagraphe(Element parent, LigneDeuxColonnesParagraphe ligne);

   /**
    * Ajoute une nouvelle ligne à une liste du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param ligne Ligne à ajouter.
    */
   void addLigneListe(Element parent, LigneListe ligne);

   /**
    * Ajoute une nouvelle entete à une liste du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param entete Entete à ajouter.
    */
   void addEnteteListe(Element parent, EnteteListe entete);

   /**
    * Ajoute nb éléments colonnes pour connaitre le nb de celles-ci
    * dans la liste.
    * @param parent
    * @param nb
    */
   void addColonnesListe(Element parent, int nb);

   /**
    * Ajoute un nouveau sous-paragraphe à un élément du 
    * document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param sous SousParagraphe à ajouter.
    */
   void addSousParagraphe(Element parent, SousParagraphe sous);

   /**
    * Ajoute un nouveau paragraphe à un élément du 
    * document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param para Paragraphe à ajouter.
    */
   void addParagraphe(Element parent, Paragraphe para);

   /**
    * Ajoute une nouvelle liste à un élément du 
    * document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param liste Liste à ajouter.
    */
   void addListe(Element parent, ListeElement liste);

   /**
    * Ajoute un haut de page au document JDOM.
    * @param parent Parent auquel on va ajouter la légende.
    * @param legende Haut de page.
    */
   void addHautDePage(Element parent, String legende, boolean addImage, String adr);

   /**
    * Ajoute un bas de page au document JDOM.
    * @param parent Parent auquel on va ajouter la légende.
    * @param legende Bas de page.
    */
   void addBasDePage(Element parent, String legende);

   /**
    * Crée le fichier XML associé au Document JDOM.
    * @param doc Document JDOM.
    * @param folder Folder du fichier XML.
    * @param file Fichier xml.
    */
   void createXMLFile(Document doc, String folder, String file);

   /**
    * Transforme le document JDOM en pdf.
    * @param doc Document pdf.
    */
   org.w3c.dom.Document transformAsPdf(Document doc);

   /**
    * Crée le fichier PDF et renvoie un OutputStream le contenant.
    * @param doc Document DOM.
    * @return OutputStream contenant le fichier PDF.
    * @throws Exception
    */
   byte[] creerPdf(Document document) throws Exception;

   /**
    * Crée un fichier HTML qui va contenir la fiche à imprimer.
    * @param doc. 
    */
   byte[] creerHtml(Document doc) throws Exception;

   /**
    * Ajoute une pageboite au document.
    * @param root RootElement du Document.
    * @param titre Titre du document.
    */
   Element addPageBoite(Element root, String titre);

   /**
    * Ajoute un titre intermédiaire à la pageboite.
    * @param root Element auquel ajouter un titre intermediaire.
    * @param titre Titre intermediaire.
    */
   void addTitreIntermediaire(Element elt, String titre);

   /**
    * Ajoute un séparateur à la pageboite.
    * @param root Element auquel ajouter un titre intermediaire.
    */
   void addSeparateur(Element elt);

   /**
    * Ajoute la modelisation d'une boite au document.
    * @param elt Element parent de la boite dans le document.
    * @param boite Terminale a modeliser.
    * @param instructions Instructions a afficher pour trouver la
    * boite.
    * @param titreListe Titre de la liste d'éléments.
    * @param elements Elements a prendre dans la boite.
    * @param positions Positions des éléments a traiter.
    */
   void addBoite(Element elt, BoiteImpression boite, String adrlImages);

   /**
    * Modélise la boite.
    * @param elt Element parent.
    * @param boite Terminale.
    * @param nom Nom de la boite.
    */
   void createModelisation(Element elt, Terminale boite, String nom, List<Integer> positions, String legendeVide,
      String legendePris, String legendeSelectionne, String adrlImages);

   /**
    * Ajoute les instructions pour trouver une boite.
    * @param elt Element parent des instructions.
    * @param instructions Instructions a afficher pour trouver la
    * boite.
    */
   void addInstructions(Element elt, List<String> instructions);

   /**
    * Ajoute la liste des elements a traiter.
    * @param elt Element parent de la liste.
    * @param titreListe Titre de la liste d'éléments.
    * @param elements Elements a prendre dans la boite.
    */
   void addListeElements(Element elt, List<String> elements, String titreListe);

   /**
    * Crée un fichier HTML qui va contenir la fiche à imprimer.
    * @param doc. 
    */
   byte[] creerBoiteHtml(Document doc) throws Exception;

   /**
    * Ajoute une PageContenuBoite au document.
    * @param root RootElement du Document.
    * @param titre Titre de la page.
    */
   Element addPageContenuBoite(Element root, String titre);

   /**
    * Ajoute les Parents de la boite.
    * @param elt Element parent de la liste.
    * @param listeParents Liste de Parents a afficher pour trouver la
    * boite.
    */
   void addListeParents(Element elt, List<String> listeParents);

   /**
    * Ajoute les Liste de Nombres stats de la boite.
    * @param elt Element parent des instructions.
    * @param listeNombres Liste de Nombres stats de la boite.
    */
   void addListeNombres(Element elt, List<String> listeNombres);

   /**
    * Modélise le contenu de la boite.
    * @param elt Element parent.
    * @param boite Terminale.
    * @param valeurVide Valeur d'un emplacement vide de la boite.
    */
   void createContenu(Element elt, Terminale boite, String valeurVide, String adrImages,
      Hashtable<String, String> echantillonTypesCouleurs, String echantillonCouleur,
      Hashtable<String, String> prodDeriveTypesCouleurs, String prodDeriveCouleur);

   /**
    * Ajoute la modelisation d'une boite au document.
    * @param elt Element parent de la boite dans le document.
    * @param boite Terminale a modeliser.
    */
   void addBoiteContenu(Element elt, BoiteContenu boite, String adrImages, Hashtable<String, String> echantillonTypesCouleurs,
      String echantillonCouleur, Hashtable<String, String> prodDeriveTypesCouleurs, String prodDeriveCouleur);

   /**
    * Crée un fichier HTML qui va contenir la fiche à imprimer.
    * @param doc. 
    */
   byte[] creerContenuHtml(Document doc) throws Exception;

   /**
    * Ajoute un nouveau couple de valeurs à un élément du
    * document JDOM.
    * @param parent Parent auquel on va ajouter le couple de
    * valeurs.
    * @param couple Couple de valeurs à ajouter.
    */
   void addCoupleAccordValeur(Element parent, CoupleAccordValeur couple);

   /**
    * Ajoute une nouvelle ligne à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param ligne Ligne à ajouter.
    */
   void addLigneAccord(Element parent, LigneAccord ligne);

   /**
    * Ajoute un BlocPrincipal à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param bloc BlocPrincipal à ajouter.
    */
   void addBlocPrincipal(Element parent, BlocPrincipal bloc);

   /**
    * Ajoute un Tableau à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param blocs BlocPrincipaux formants le tableau.
    */
   void addTableau(Element parent, BlocPrincipal[] blocs);

   /**
    * Ajoute un nouveau ValeursSignatures à un élément du
    * document JDOM.
    * @param parent Parent auquel on va ajouter le couple de
    * valeurs.
    * @param valeurs ValeursSignatures à ajouter.
    */
   void addValeursSignatures(Element parent, ValeursSignatures valeurs);

   /**
    * Ajoute une nouvelle ListeSignature à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param liste ListeSignature à ajouter.
    */
   void addListeSignature(Element parent, ListeSignature liste);

   /**
    * Ajoute de nouvelles Signatures à un élément du document JDOM.
    * @param parent Parent auquel on va ajouter la ligne.
    * @param signatures Signatures à ajouter.
    */
   void addSignatures(Element parent, Signatures signatures);

   /**
    * Transforme le document JDOM en pdf.
    * @param doc Document pdf.
    */
   org.w3c.dom.Document transformAsAccordPdf(Document doc);

   /**
    * Crée le fichier PDF et renvoie un OutputStream le contenant.
    * @param doc Document DOM.
    * @return OutputStream contenant le fichier PDF.
    * @throws Exception
    */
   byte[] creerAccordTransfertPdf(Document document) throws Exception;

}
