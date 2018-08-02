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
package fr.aphp.tumorotek.manager.impl.io.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectPropertyDuo;
import fr.aphp.tumorotek.manager.io.utils.TKAnnotableDuoManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.utils.Utils;

public class TKAnnotableDuoManagerImpl implements TKAnnotableDuoManager
{

   private PrelevementManager prelevementManager;

   public void setPrelevementManager(final PrelevementManager pM){
      this.prelevementManager = pM;
   }

   @Override
   public List<TKAnnotableObjectPropertyDuo> compareObjectsDuoManager(final TKAnnotableObjectDuo duo,
      final List<ChampEntite> chpEntites, final List<ChampAnnotation> chpAnnos){

      final List<TKAnnotableObjectPropertyDuo> propCompList = new ArrayList<>();

      if(duo != null && duo.getFirstObj() != null && duo.getSecondObj() != null){
         TKAnnotableObjectPropertyDuo propDuo;
         try{
            // champs TK
            for(final ChampEntite chpE : chpEntites){

               propDuo = new TKAnnotableObjectPropertyDuo();

               populateTKPropDuo(chpE, duo, propDuo);

               // ssi le propDuo a été remplit...
               if(propDuo.getChampEntite() != null){
                  propCompList.add(propDuo);
               }
            }

            // annotations
            for(final ChampAnnotation chpA : chpAnnos){
               propDuo = new TKAnnotableObjectPropertyDuo();

               populateAnnoPropDuo(chpA, duo, propDuo);

               // ssi le propDuo a été remplit...
               if(propDuo.getChampAnnotation() != null){
                  propCompList.add(propDuo);
               }
            }
         }catch(final Exception e){
            throw new RuntimeException(e);
         }

      }
      return propCompList;
   }

   /**
    * Récupère les valeurs pour les champs TK pour chacun des deux objets 
    * afin de peupler un duo de valeurs si celle-ci sont divergentes.
    * Implémente les exceptions à la récupération générique par PropertyUtils à partir de champEntite (ex:
    * non conformites, codes assignes etc...)
    * @param nomChamp property
    * @param object duo
    * @param propDuo property values duo
    * @throws NoSuchMethodException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    */
   
   private void populateTKPropDuo(final ChampEntite chpE, final TKAnnotableObjectDuo duo,
      final TKAnnotableObjectPropertyDuo propDuo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

      Object firstValue;
      Object secondValue;

      // Exception champEntite Prelevement
      if(duo.getEntite().getNom().equals("Prelevement") && chpE.getNom().equals("Risques")){
         firstValue = ((Prelevement) duo.getFirstObj()).getRisques();
         secondValue = prelevementManager.getRisquesManager((Prelevement) duo.getSecondObj());

         if(!((Set<Risque>) firstValue).containsAll((Set<Risque>) secondValue)
            || !((Set<Risque>) secondValue).containsAll((Set<Risque>) firstValue)){
            propDuo.setFirstValue(firstValue);
            propDuo.setSecondValue(secondValue);
            propDuo.setChampEntite(chpE);
         }
      }else if(duo.getEntite().getNom().equals("Prelevement") && chpE.getNom().equals("ConformeArrivee.Raison")){
         firstValue = duo.getFirstNoConfs();
         secondValue = duo.getSecondNoConfs();

         if(!((List<NonConformite>) firstValue).containsAll((List<NonConformite>) secondValue)
            || !((List<NonConformite>) secondValue).containsAll((List<NonConformite>) firstValue)){
            propDuo.setFirstValue(firstValue);
            propDuo.setSecondValue(secondValue);
            propDuo.setChampEntite(chpE);
         }
      }else if(duo.getEntite().getNom().equals("Echantillon") && chpE.getNom().equals("ConformeTraitement.Raison")){
         firstValue = duo.getFirstNoConfs();
         secondValue = duo.getSecondNoConfs();

         if(!((List<NonConformite>) firstValue).containsAll((List<NonConformite>) secondValue)
            || !((List<NonConformite>) secondValue).containsAll((List<NonConformite>) firstValue)){
            propDuo.setFirstValue(firstValue);
            propDuo.setSecondValue(secondValue);
            propDuo.setChampEntite(chpE);
         }
      }else if(duo.getEntite().getNom().equals("Echantillon") && chpE.getNom().equals("ConformeCession.Raison")){
         //			firstValue = duo.getFirstNoConfs();
         //			secondValue = duo.getSecondNoConfs();
         //
         //			if (!((List<NonConformite>) firstValue).containsAll((List<NonConformite>) secondValue)
         //					|| !((List<NonConformite>) secondValue).containsAll((List<NonConformite>) firstValue)) {
         //				propDuo.setFirstValue(firstValue);
         //				propDuo.setSecondValue(secondValue);
         //				propDuo.setChampEntite(chpE);
         //			}
      }else{
         String nomChamp = chpE.getNom().replaceFirst(".", (chpE.getNom().charAt(0) + "").toLowerCase());
         if(nomChamp.endsWith("Id")){
            nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
         }
         if(PropertyUtils.isReadable(duo.getFirstObj(), nomChamp) && PropertyUtils.isReadable(duo.getSecondObj(), nomChamp)){
            firstValue = PropertyUtils.getProperty(duo.getFirstObj(), nomChamp);
            secondValue = PropertyUtils.getProperty(duo.getSecondObj(), nomChamp);

            if((firstValue != null && !firstValue.equals(secondValue) || (firstValue == null && secondValue != null))){
               propDuo.setFirstValue(firstValue);
               propDuo.setSecondValue(secondValue);
               propDuo.setChampEntite(chpE);
            }
         }
      }
   }

   /**
    * Récupère les valeurs pour les champs d'annotations
    * pour chacun des deux objets afin de peupler un duo de valeurs si celle-ci sont 
    * divergentes
    * @param nomChamp property
    * @param object duo
    * @param propDuo property values duo
    * @throws NoSuchMethodException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    */
   private void populateAnnoPropDuo(final ChampAnnotation chpA, final TKAnnotableObjectDuo duo,
      final TKAnnotableObjectPropertyDuo propDuo){

      final List<AnnotationValeur> firstAnnoValue = duo.getFirstAnnoValsMap().get(chpA);
      final List<AnnotationValeur> secondAnnoValue = duo.getSecondAnnoValsMap().get(chpA);

      boolean isDiff = false;
      // 1 seul valeur
      if(!chpA.getDataType().getType().equals("thesaurusM")){
         if(!firstAnnoValue.isEmpty()){
            if(!secondAnnoValue.isEmpty()){ // second valeur existe 
               if(!firstAnnoValue.get(0).getValeur().equals(secondAnnoValue.get(0).getValeur())){
                  isDiff = true;
               }
            }else{ // deuxieme valeur absente
               isDiff = true;
            }
         }else if(!secondAnnoValue.isEmpty()){ // first valeur absente ms seconde existe
            isDiff = true;
         }
      }else{ // thesaurus multiple
         if(!firstAnnoValue.isEmpty()){
            for(final AnnotationValeur faV : firstAnnoValue){
               isDiff = true;
               for(final AnnotationValeur saV : secondAnnoValue){ // elle est dans l'autre liste
                  if(faV.getValeur().equals(saV.getValeur())){
                     isDiff = false;
                     break;
                  }
               }
            }
            if(!isDiff){ // teste l'égalité dans l'autre sens
               for(final AnnotationValeur saV : secondAnnoValue){
                  isDiff = true;
                  for(final AnnotationValeur faV : firstAnnoValue){ // elle est dans l'autre liste
                     if(saV.getValeur().equals(faV.getValeur())){
                        isDiff = false;
                        break;
                     }
                  }
               }
            }
         }else if(!secondAnnoValue.isEmpty()){ // first valeur absente ms seconde existe
            isDiff = true;
         }
      }

      if(isDiff){
         propDuo.setFirstValue(firstAnnoValue);
         propDuo.setSecondValue(secondAnnoValue);
         propDuo.setChampAnnotation(chpA);
      }
   }

   @Override
   public Boolean mergeDuoObjectsManager(final TKAnnotableObjectDuo duo, final List<TKAnnotableObjectPropertyDuo> propDuos){

      Boolean merged = false;
      try{
         if(propDuos != null && duo != null){
            // actualise les deux listes d'annotations valeurs
            // à partir des propDuos
            duo.getFirstAnnoVals().clear();
            duo.getSecondAnnoVals().clear();
            for(final TKAnnotableObjectPropertyDuo pDuo : propDuos){
               // si au moins une des méthodes merge(Anno)Values renvoie true
               // alors merged = true
               boolean duoMerged = false;
               if(pDuo.getChampEntite() != null){
                  duoMerged = mergeValues(duo.getSecondObj(), pDuo);
               }else if(pDuo.getChampAnnotation() != null){
                  duoMerged = mergeAnnoValues(duo, pDuo);
               }
               merged = duoMerged || merged;
            }
            return merged;
         }
      }catch(final Exception e){
         throw new RuntimeException(e);
      }
      return merged;
   }

   /**
    * Applique la valeur du firstObj nouvellement composé à l'objet existant,
    * Implémente les exceptions à l'assignation générique par PropertyUtils à partir de champEntite (ex:
    * non conformites, codes assignes etc...).
    * @param secondObj TKAnnotableObjet existant
    * @param propertyDuo contenant la valeur et le champ assigné auxquel l'assigner
    * @return true si la modification a été appliquée (elle peut ne pas avoir été appliquée sir 
    * une valeur nulle est attribuée à un champ obligatoire)
    * @throws NoSuchMethodException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    */
   private boolean mergeValues(final TKAnnotableObject secondObj, final TKAnnotableObjectPropertyDuo propDuo)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{

      boolean merged = false;

      // Exception champEntite Prelevement
      if(secondObj instanceof Prelevement && propDuo.getChampEntite().getNom().equals("Risques")){

      }else if(secondObj instanceof Prelevement && propDuo.getChampEntite().getNom().equals("ConformeArrivee.Raison")){

      }else if(secondObj instanceof Echantillon && propDuo.getChampEntite().getNom().equals("ConformeTraitement.Raison")){

      }else if(secondObj instanceof Echantillon && propDuo.getChampEntite().getNom().equals("ConformeCession.Raison")){

      }else{
         final String nomChamp = Utils.getReadablePropertyFromChampEntite(propDuo.getChampEntite());

         // gestion des champs obligatoires
         if(propDuo.getFirstValue() != null || propDuo.getChampEntite().isNullable()){
            PropertyUtils.setSimpleProperty(secondObj, nomChamp, propDuo.getFirstValue());
            merged = true;
         }
      }

      return merged;

   }

   /**
    * Le TKAnnotableObjectPropertyDuo passé en paramètre contient les valeurs d'annotation 
    * pour le champ d'annotation qui devront être supprimées pour le secondObj, et créées pour 
    * le firstObj.
    * Cette méthode met donc à jour les deux Maps d'annotation valeurs qui seront prises en 
    * compte dans la methode d'enregistrement des modifications (saveObjectsRowManager).
    * @param duo d'objets TKAnnotable
    * @param pDuo de liste de valeurs d'annotation TKAnnotableObjectPropertyDuo
    * @return true 
    */
   
   private boolean mergeAnnoValues(final TKAnnotableObjectDuo duo, final TKAnnotableObjectPropertyDuo pDuo){

      duo.getFirstAnnoValsMap().put(pDuo.getChampAnnotation(), ((List<AnnotationValeur>) pDuo.getFirstValue()));
      duo.getSecondAnnoValsMap().put(pDuo.getChampAnnotation(), ((List<AnnotationValeur>) pDuo.getSecondValue()));

      return true;
   }
}
