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
package fr.aphp.tumorotek.manager.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Helper class permettant la préparation des Objets qui serviront
 * à la création des annotations référencant un fichier enregistré
 * sur le disque, pour un batch de TKAnnotableObjects
 *  Date: 15/05/2015
 *
 * @since 2.0.12
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 */
public class FileBatch
{

   private List<TKAnnotableObject> objs = new ArrayList<>();
   private InputStream stream;
   private Fichier file;
   private ChampAnnotation champ;
   private Boolean completed = false;

   public List<TKAnnotableObject> getObjs(){
      return objs;
   }

   public void setObjs(final List<TKAnnotableObject> objs){
      this.objs = objs;
   }

   public InputStream getStream(){
      return stream;
   }

   public void setStream(final InputStream stream){
      this.stream = stream;
   }

   public Fichier getFile(){
      return file;
   }

   public void setFile(final Fichier file){
      this.file = file;
   }

   public ChampAnnotation getChamp(){
      return champ;
   }

   public void setChamp(final ChampAnnotation champ){
      this.champ = champ;
   }

   public Boolean isCompleted(){
      return completed;
   }

   public void setCompleted(final Boolean completed){
      this.completed = completed;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final FileBatch test = (FileBatch) obj;

      return ((this.champ == test.champ || (this.champ != null && this.champ.equals(test.champ)))
         && (this.stream == test.stream || (this.stream != null && this.stream.equals(test.stream))));
   }

   @Override
   public int hashCode(){
      return super.hashCode();
   }
}
