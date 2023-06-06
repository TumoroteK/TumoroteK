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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Contexte implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer contexteId;

   private String nom;

   private ContexteType contexteType;

   private Boolean archive;

   protected List<Parametrage> parametrages = new ArrayList<>();

   protected List<ChampEntite> champEntites = new ArrayList<>();
   
   // gatsbi clefs naturelles pour chaque onglet
   private Map<ContexteType, List<Integer>> nkIds = Stream.of(
         new AbstractMap.SimpleEntry<>(ContexteType.PATIENT, Arrays.asList(272)), // identifiant patient  
         new AbstractMap.SimpleEntry<>(ContexteType.MALADIE, Arrays.asList(17, 20)), // libelle, date visite
         new AbstractMap.SimpleEntry<>(ContexteType.PRELEVEMENT, Arrays.asList(23)), // code prelevement
         new AbstractMap.SimpleEntry<>(ContexteType.ECHANTILLON, Arrays.asList(54)), // code échantillon
         new AbstractMap.SimpleEntry<>(ContexteType.PROD_DERIVE, Arrays.asList(79))) // code dérivé
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
   
   public Contexte(){}

   public Contexte(final Integer contexteId, final String nom, final ContexteType contexteType, final Boolean archive,
      final List<Parametrage> parametrages, final List<ChampEntite> champEntites){
      super();
      this.contexteId = contexteId;
      this.nom = nom;
      this.contexteType = contexteType;
      this.archive = archive;
      this.parametrages = parametrages;
      this.champEntites = champEntites;
   }

   public Integer getContexteId(){
      return contexteId;
   }

   public void setContexteId(final Integer contexteId){
      this.contexteId = contexteId;
   }

   public String getNom(){
      return nom;
   }

   public void setNom(final String _l){
      this.nom = _l;
   }

   public ContexteType getContexteType(){
      return contexteType;
   }

   public void setContexteType(final ContexteType _t){
      this.contexteType = _t;
   }

   public Boolean getArchive(){
      return archive;
   }

   public void setArchive(final Boolean _a){
      this.archive = _a;
   }

   public Boolean getSiteIntermediaire(){
      return isChampIdVisible(273);
   }

   public List<Parametrage> getParametrages(){
      return parametrages;
   }

   public void setParametrages(final List<Parametrage> parametrages){
      this.parametrages = parametrages;
   }

   public List<ChampEntite> getChampEntites(){
      return champEntites;
   }

   public void setChampEntites(final List<ChampEntite> champEntites){
      this.champEntites = champEntites;
   }

   @Override
   public boolean equals(final Object obj){
      if(obj == this){
         return true;
      }
      if(obj == null || obj.getClass() != this.getClass()){
         return false;
      }

      final Contexte contexte = (Contexte) obj;

      return Objects.equals(nom, contexte.getNom()) && Objects.equals(contexteType, contexte.getContexteType());
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((nom == null) ? 0 : nom.hashCode());
      result = prime * result + ((contexteType == null) ? 0 : contexteType.hashCode());
      return result;
   }

   public List<Integer> getHiddenChampEntiteIds(){
      final List<Integer> ids = new ArrayList<>();
      for(final ChampEntite c : champEntites){
         if(!c.getVisible()){
            ids.add(c.getChampEntiteId());
         }
      }
      
      // clef naturelles sont retirés 
      // par securite
      removeNaturalKeys(ids);

      // dependances entite specifiques
      addPrelevementComplementaryVisibleIds(ids);
      addEchantillonComplementaryVisibleChpIds(ids);

      return ids;
   }
   
   /**
    * Supprime par sécurité des champs invisible les 
    * clefs naturelles imposées par Gatsbi:
    *  - patient identifiant
    *  - maladie/visite libelle + date debut
    *  - prelevement code
    *  - echantillon code 
    *  - derive code
    * @param ids
    */
   private void removeNaturalKeys(List<Integer> ids) {
      ids.removeAll(nkIds.get(contexteType));
   }

   /**
    * Dependances visibilité entre champs prélèvement
    * @param ids
    */
   private void addPrelevementComplementaryVisibleIds(final List<Integer> ids){

      if(ids.contains(40)){
         ids.add(41); // unite adds unite id
      }
      if(ids.contains(256)){
         ids.add(257); // non conformite adds raisons no conf
      }
   }

   /**
    * Dependances visibilité entre champs echantillons 
    * @param ids
    */
   private void addEchantillonComplementaryVisibleChpIds(final List<Integer> ids){

      if(ids.contains(229)){
         ids.add(59); // adicap organe id
      }
      if(ids.contains(230)){
         ids.add(216); // code assigne id
      }
      if(ids.contains(61)){ // quantite
         ids.add(62); // quantité init
         ids.add(63); // unite id
      }
      if(ids.contains(243)){ // non conformite traitement
         ids.add(261); // raisons no conf traitement
      }
      if(ids.contains(244)){ // non conformite cession
         ids.add(262); // raisons no conf cession
      }
   }

   public List<Integer> getRequiredChampEntiteIds(){
      final List<Integer> ids = new ArrayList<>();
      for(final ChampEntite c : champEntites){
         if(c.getVisible() && c.getObligatoire()){
            ids.add(c.getChampEntiteId());
         }
      }
      
      // ajouter par sécurité 
      // les clefs naturelles gatsbi
      addNaturalKeys(ids);

      addEchantillonComplementaryRequiredIds(ids);

      return ids;
   }
   
   /**
    * Ajoute par sécurité des champs obligatoires les 
    * clefs naturelles imposées par Gatsbi:
    *  - patient identifiant
    *  - maladie/visite libelle + date debut
    *  - prelevement code
    *  - echantillon code 
    *  - derive code
    * @param ids
    */
   private void addNaturalKeys(List<Integer> ids) {
      for(Integer nk : nkIds.get(contexteType)){
         if (!ids.contains(nk)){
            ids.add(nk);
         }
      }
   }

   /**
    * Dependances obligatoire entre champs echantillons 
    * @param ids
    */
   private void addEchantillonComplementaryRequiredIds(final List<Integer> ids){
      // echantillon
      if(ids.contains(61)){ // quantite
         ids.add(63); // quantité unite
      }
   }

   /**
    * Collecte et renvoie les champs de contexte considérés comme un thésaurus dont les 
    * valeurs sont à filtrer. Contient les champs:
    *  - les thesaurus (IsChampReferToThesaurus = true)
    *  - les champs présentant des valeurs de thésaurus (ex: quantite, thésaurus des unités à filtrer)
    * @return liste champs entite ids
    */
   public List<Integer> getThesaurusChampEntiteIds(){
      final List<Integer> ids = new ArrayList<>();
      for(final ChampEntite c : champEntites){
         //bug TG-163 :
         //Si l'utilisateur n'a pas sélectionné de valeur pour un thesaurus dans Gatsbi, Gatsbi renvoie toutes les valeurs
         //présentes dans TK donc c.getThesaurusValues().isEmpty() ne peut pas être empty dans le cas d'un thesaurus
         //on garde toutefois la sécurité implémentée dans TK : si aucune valeur transmise pour un champ associé à un thesaurus
         //affichage de toutes les valeurs dans TK
         if(c.getVisible() 
               && ( (c.getThesaurusTableNom() != null && !c.getThesaurusTableNom().isEmpty()) 
                     || !c.getThesaurusValues().isEmpty())){
            ids.add(c.getChampEntiteId());
         }
      }
      return ids;
   }

   public List<ThesaurusValue> getThesaurusValuesForChampEntiteId(final Integer id){

      final List<ThesaurusValue> tValues = new ArrayList<>();
      for(final ChampEntite c : champEntites){
         if(c.getChampEntiteId().equals(id)){
            tValues.addAll(c.getThesaurusValues());
         }
      }
      return tValues;
   }

   public boolean isChampIdRequired(final Integer id){
      
      // clefs naturelles toujours obligatoires
      if (nkIds.get(contexteType).contains(id)) {
         return true;
      }
      
      for(final ChampEntite c : champEntites){
         if(c.getChampEntiteId().equals(id)){
            return c.getVisible() && c.getObligatoire();
         }
      }
      return false;
   }

   public boolean isChampIdVisible(final Integer id){
      
      // clefs naturelles toujours visibles
      if (nkIds.get(contexteType).contains(id)) {
         return true;
      }
      
      for(final ChampEntite c : champEntites){
         if(c.getChampEntiteId().equals(id)){
            return c.getVisible();
         }
      }
      return true;
   }
   
   public boolean isChampInTableau(final Integer id){
      
      // clefs naturelles toujours dans le tableau
      if (nkIds.get(contexteType).contains(id)) {
         return true;
      }
      
      for(final ChampEntite c : champEntites){
         if(c.getChampEntiteId().equals(id)){
            return c.getVisible() && c.getInTableau();
         }
      }
      return true;
   }

   public List<Integer> getChampEntiteInTableauOrdered(){
      final List<Integer> ids = new ArrayList<>();

      Collections.sort(champEntites);

      for(final ChampEntite c : champEntites){
         if(c.getVisible() && c.getInTableau()){
            ids.add(c.getChampEntiteId());
         }
      }
      return ids;
   }

}
