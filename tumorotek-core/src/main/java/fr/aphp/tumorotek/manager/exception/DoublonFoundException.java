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
package fr.aphp.tumorotek.manager.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.aphp.tumorotek.manager.impl.coeur.patient.PatientDoublonFound;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * Classe gérant les exceptions lancées lors de la découverte de doublons.
 * Classe créée le 23/09/09.
 *
 * Depuis v2.1: gestion doublons niveau PF, ajout code + PF permettant de
 * détailler dans quelle collection se trouvent les doublons.
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 *
 */
public class DoublonFoundException extends TKException
{

   private static final long serialVersionUID = 1L;

   private String entite;

   private String operation;

   // @since 2.1
   //TK-426 : champ initialement de type String transformé en List pour gérer le cas des mises 
   //à jour simultanées de plusieurs codes. 
   //Exemple : modification en cascade des enfants et petits enfants d'un élément dont le code a été mis à jour. 
   private List<String> codes;
   
   private PatientDoublonFound patientDoublonFound; 

   private Plateforme plateforme;

   public DoublonFoundException(){
      super();
   }

   public DoublonFoundException(final String en, final String op){
      super();
      this.entite = en;
      this.operation = op;
   }

   /*
    * @since 2.1
    */
   public DoublonFoundException(final String _e, final String _o, final String _c, final Plateforme _p){
      this(_e, _o, new ArrayList<String>(Arrays.asList(_c)),_p);
   }
   
   public DoublonFoundException(final String _e, final String _o, final List<String> codes, final Plateforme _p){
      super();
      this.entite = _e;
      this.operation = _o;
      this.codes = new ArrayList<String>();
      this.codes.addAll(codes);
      this.plateforme = _p;
   }
   
   /**
    * @since 2.3.0-gatsbi
    */
   public DoublonFoundException(final String en, final String op, final PatientDoublonFound dbf){
      super();
      this.entite = en;
      this.operation = op;
      this.patientDoublonFound = dbf;
   }

   public String getEntite(){
      return entite;
   }

   public void setEntite(final String en){
      this.entite = en;
   }

   public String getOperation(){
      return operation;
   }

   public void setOperation(final String op){
      this.operation = op;
   }

   @Override
   public String getMessage(){
      return this.entite + ": doublon detecte lors de la " + this.operation + "";
   }

   public String getNaturalMessage(){
      return super.getMessage();
   }

   public List<String> getCodes(){
      if(codes == null) {
         return new ArrayList<String>();
      }
      return codes;
   }

   public void setCodes(final List<String> codes){
      this.codes = codes;
   }
   
   //ramène la 1ere valeur :
   public String getCode(){
      if(getCodes() != null && !getCodes().isEmpty()) {
         return getCodes().get(0);
      }
      return null;
   }

   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme _p){
      this.plateforme = _p;
   }

   public PatientDoublonFound getPatientDoublonFound(){
      return patientDoublonFound;
   }

   public void setPatientDoublonFound(PatientDoublonFound _dbf){
      this.patientDoublonFound = _dbf;
   }
}