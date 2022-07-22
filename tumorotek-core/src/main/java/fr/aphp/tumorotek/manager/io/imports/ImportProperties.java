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
package fr.aphp.tumorotek.manager.io.imports;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.ss.usermodel.FormulaEvaluator;

import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ImportProperties
{

   /**
    * Template de l'import.
    */
   private ImportTemplate importTemplate;

   /**
    * Hashtable contenant l'indice des colonnes en fct de leur nom.
    */
   private Hashtable<String, Integer> colonnesHeaders = new Hashtable<>();

   /**
    * Hashtable contenant la liste des colonnes en fct de leur entité.
    */
   private Hashtable<Entite, List<ImportColonne>> colonnesForEntites = new Hashtable<>();

   /**
    * Hashtable contenant les valeurs des thésaurus en fct de leur
    * ebtité.
    */
   private Hashtable<Object, Hashtable<String, Object>> thesaurusValues = new Hashtable<>();

   /**
    * hashtable contenant les valeurs des annotations thésaurus en fct
    * de leur ChampAnnotation.
    */
   private Hashtable<ChampAnnotation, Hashtable<String, Object>> annotationThesaurusValues = new Hashtable<>();

   /**
    * Hashtable contenant les AnnotationsValeurs à enregistrer en fct
    * de l'entité.
    */
   private Hashtable<Entite, List<AnnotationValeur>> annotationsEntite = new Hashtable<>();

   private Banque banque;

   private FormulaEvaluator evaluator;

   private final List<ImportColonne> subDerivesCols = new ArrayList<>();

   public ImportProperties(){}

   public ImportTemplate getImportTemplate(){
      return importTemplate;
   }

   public void setImportTemplate(final ImportTemplate i){
      this.importTemplate = i;
   }

   public Hashtable<String, Integer> getColonnesHeaders(){
      return colonnesHeaders;
   }

   public void setColonnesHeaders(final Hashtable<String, Integer> c){
      this.colonnesHeaders = c;
   }

   public Hashtable<Entite, List<ImportColonne>> getColonnesForEntites(){
      return colonnesForEntites;
   }

   public void setColonnesForEntites(final Hashtable<Entite, List<ImportColonne>> c){
      this.colonnesForEntites = c;
   }

   public Hashtable<Object, Hashtable<String, Object>> getThesaurusValues(){
      return thesaurusValues;
   }

   public void setThesaurusValues(final Hashtable<Object, Hashtable<String, Object>> t){
      this.thesaurusValues = t;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   public Hashtable<ChampAnnotation, Hashtable<String, Object>> getAnnotationThesaurusValues(){
      return annotationThesaurusValues;
   }

   public void setAnnotationThesaurusValues(final Hashtable<ChampAnnotation, Hashtable<String, Object>> aValues){
      this.annotationThesaurusValues = aValues;
   }

   public Hashtable<Entite, List<AnnotationValeur>> getAnnotationsEntite(){
      return annotationsEntite;
   }

   public void setAnnotationsEntite(final Hashtable<Entite, List<AnnotationValeur>> aEntite){
      this.annotationsEntite = aEntite;
   }

   public FormulaEvaluator getEvaluator(){
      return evaluator;
   }

   public void setEvaluator(final FormulaEvaluator ev){
      this.evaluator = ev;
   }

   public List<ImportColonne> getSubDerivesCols(){
      return subDerivesCols;
   }
}
