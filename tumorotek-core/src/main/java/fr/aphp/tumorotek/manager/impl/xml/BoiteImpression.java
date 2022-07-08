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

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * Objet représentant une boite à imprimer.
 * Objet créé le 08/09/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class BoiteImpression
{

   private Terminale boite;

   private String titreIntermediaire;

   private String titreModelisation;

   private String titreInstructions;

   private String nom;

   private List<String> instructions;

   private List<String> elements = new ArrayList<>();

   private String titreListe;

   private List<Integer> positions = new ArrayList<>();

   private String legendeVide;

   private String legendePris;

   private String legendeSelectionne;

   private Boolean separateur = false;

   public BoiteImpression(){
      separateur = false;
   }

   public BoiteImpression(final Terminale b, final List<String> e, final List<String> i, final String legendeP,
      final String legendeS, final String legendeV, final String n, final List<Integer> p, final String titreIs,
      final String titreL, final String titreM){
      super();
      this.boite = b;
      this.elements = e;
      this.instructions = i;
      this.legendePris = legendeP;
      this.legendeSelectionne = legendeS;
      this.legendeVide = legendeV;
      this.nom = n;
      this.positions = p;
      this.titreInstructions = titreIs;
      this.titreListe = titreL;
      this.titreModelisation = titreM;
      this.separateur = false;
   }

   public Terminale getBoite(){
      return boite;
   }

   public void setBoite(final Terminale b){
      this.boite = b;
   }

   public String getTitreModelisation(){
      return titreModelisation;
   }

   public void setTitreModelisation(final String titre){
      this.titreModelisation = titre;
   }

   public String getTitreInstructions(){
      return titreInstructions;
   }

   public void setTitreInstructions(final String titre){
      this.titreInstructions = titre;
   }

   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   public List<String> getInstructions(){
      return instructions;
   }

   public void setInstructions(final List<String> i){
      this.instructions = i;
   }

   public List<String> getElements(){
      return elements;
   }

   public void setElements(final List<String> e){
      this.elements = e;
   }

   public String getTitreListe(){
      return titreListe;
   }

   public void setTitreListe(final String titre){
      this.titreListe = titre;
   }

   public List<Integer> getPositions(){
      return positions;
   }

   public void setPositions(final List<Integer> p){
      this.positions = p;
   }

   public String getLegendeVide(){
      return legendeVide;
   }

   public void setLegendeVide(final String legende){
      this.legendeVide = legende;
   }

   public String getLegendePris(){
      return legendePris;
   }

   public void setLegendePris(final String legende){
      this.legendePris = legende;
   }

   public String getLegendeSelectionne(){
      return legendeSelectionne;
   }

   public void setLegendeSelectionne(final String legende){
      this.legendeSelectionne = legende;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final BoiteImpression test = (BoiteImpression) obj;
      return ((this.boite == test.getBoite() || (this.boite != null && this.boite.equals(test.getBoite()))));
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashBoite = 0;

      if(this.boite != null){
         hashBoite = this.boite.hashCode();
      }

      hash = 7 * hash + hashBoite;

      return hash;
   }

   public String getTitreIntermediaire(){
      return titreIntermediaire;
   }

   public void setTitreIntermediaire(final String titre){
      this.titreIntermediaire = titre;
   }

   public Boolean getSeparateur(){
      return separateur;
   }

   public void setSeparateur(final Boolean sep){
      this.separateur = sep;
   }
}
