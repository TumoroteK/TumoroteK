/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.contexte.gatsbi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Visite implements Serializable
{

   private static final long serialVersionUID = 1L;
   
   private String nom;
   private Integer ordre;
   private Integer intervalleDepuisInitiale;
   private IntervalleType intervalleType;
   private List<VisitePrelevement> visitePrelevements = new ArrayList<VisitePrelevement>();
   
   public Visite(String nom, 
         Integer ordre, 
         Integer intervalleDepuisInitiale, 
         IntervalleType intervalleType,
         List<VisitePrelevement> visitePrelevements){
      super();
      this.nom = nom;
      this.ordre = ordre;
      this.intervalleDepuisInitiale = intervalleDepuisInitiale;
      this.intervalleType = intervalleType;
      if (visitePrelevements != null) {
         this.visitePrelevements.addAll(visitePrelevements);
      }
   }

   public String getNom(){
      return nom;
   }

   public void setNom(String nom){
      this.nom = nom;
   }

   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(Integer ordre){
      this.ordre = ordre;
   }

   public Integer getIntervalleDepuisInitiale(){
      return intervalleDepuisInitiale;
   }

   public void setIntervalleDepuisInitiale(Integer intervalleDepuisInitiale){
      this.intervalleDepuisInitiale = intervalleDepuisInitiale;
   }

   public IntervalleType getIntervalleType(){
      return intervalleType;
   }

   public void setIntervalleType(IntervalleType _i){
      this.intervalleType = _i;
   }

   public List<VisitePrelevement> getVisitePrelevements() {
      return visitePrelevements;
   }

   public void setVisitePrelevements(List<VisitePrelevement> _r) {
      this.visitePrelevements = _r;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Visite vis = (Visite) obj;

        return Objects.equals(nom, vis.getNom());
   }
   
   @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((nom == null) ? 0 : nom.hashCode());
      return result;
   }
   
   public String writePrelSchema() {
      StringBuilder bld = new StringBuilder();
      Iterator<VisitePrelevement> vpIt = visitePrelevements.iterator();
      VisitePrelevement vp;
      while (vpIt.hasNext()){
         vp = vpIt.next();
         bld.append(vp.getParametrage().getNom());
         bld.append("[");
         bld.append(vp.getNbPrelevementMin() != null ? vp.getNbPrelevementMin().toString() : "0");
         bld.append(",");
         bld.append(vp.getNbPrelevementMax() != null 
            && vp.getNbPrelevementMax() > 0 ? vp.getNbPrelevementMax().toString() : "~");
         bld.append("]");
         if (vpIt.hasNext()) {
            bld.append(", ");
         }
      }
      return bld.toString();
   }
}