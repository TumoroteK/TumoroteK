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

/**
 * Objet représentant un paragraphe du document.
 * Objet créé le 09/07/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class Paragraphe
{

   private String titre;

   private Object[] lignes;

   private SousParagraphe[] sousParagraphes;

   private String inconnu;

   private ListeElement liste;

   public Paragraphe(){

   }

   public Paragraphe(final String t, final Object[] lig, final SousParagraphe[] sousP, final String inc, final ListeElement l){
      super();
      this.lignes = lig;
      this.titre = t;
      this.sousParagraphes = sousP;
      this.inconnu = inc;
      this.liste = l;
   }

   public String getTitre(){
      return titre;
   }

   public void setTitre(final String t){
      this.titre = t;
   }

   public Object[] getLignes(){
      return lignes;
   }

   public void setLignes(final Object[] lig){
      this.lignes = lig;
   }

   public SousParagraphe[] getSousParagraphes(){
      return sousParagraphes;
   }

   public void setSousParagraphes(final SousParagraphe[] sousP){
      this.sousParagraphes = sousP;
   }

   public String getInconnu(){
      return inconnu;
   }

   public void setInconnu(final String inc){
      this.inconnu = inc;
   }

   public ListeElement getListe(){
      return liste;
   }

   public void setListe(final ListeElement l){
      this.liste = l;
   }

}
