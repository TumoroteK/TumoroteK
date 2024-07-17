package fr.aphp.tumorotek.decorator;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Constraint;

import fr.aphp.tumorotek.action.administration.ConstParametreValeur;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;

/**
 * classe Decorator permettant de gérer les actions pouvant être faites sur chaque élément
 * de la liste des paramètres dépendant de la plateforme, dans l'écran Administation / Paramètres
 * @author chuet
 *
 */
public class ParametreDecorator
{

   private ParametreDTO parametre;

   /** permet de savoir les boutons à afficher : 
    - editMode = false => Modifier et Réinitialiser (sous condition)
    - editMode = true => Enregister et Annuler
   */
   private boolean editMode;

   /**
    * contiendra la nouvelle valeur renseignée par l'utilsateur si celle-ci a été modifiée. Vaut null sinon 
    */
   private String newValeur;
   
   /**
    * indique si la valeur de l'attribut parametre correspond à la valeur par défaut.
    */
   private boolean defaultValeur;
   
   private ConstParametreValeur constParametreValeur;
   
   public ParametreDecorator(final ParametreDTO parametre){
      if(parametre == null) {
         this.parametre = new ParametreDTO();//permet de gérer plus facilement les getters appelant les getters de Parametre
      }
      else {
         this.parametre = parametre;
         this.defaultValeur=EParametreValeurParDefaut.isDefaultValue(parametre.getCode(), parametre.getValeur());
         this.constParametreValeur = new ConstParametreValeur(parametre.getType());
      }
   }
   
   public boolean displayModifierButton() {
      return !editMode;
   }
   
   public boolean displayEnregistrerButton() {
      return editMode;
   }
   
   public boolean displayAnnulerButton() {
      return editMode;
   }

   public boolean displayReinitButton() {
      return !editMode && !defaultValeur;
   }
   
   /**
    * Cette méthode retourne le libellé internationalisé du paramètre : 
    * Si le code est nul, elle retourne "".
    * Si le code n'est pas nul, la clé i18n est construite avec le préfixe "params." ajouté au code.
    * Si aucune valeur n'est définie pour cette clé, la clé est renvoyée
    *
    * @return la libellé internationalisé correspondant au code du paramètre
    */
   public String getLibelleI18n(){
      if(parametre.getCode() == null) {
         return "";
      }
      String keyI18n = new StringBuilder("params.").append(parametre.getCode()).toString();
      String libelleI18n = Labels.getLabel(keyI18n);
      if(libelleI18n == null) {
         libelleI18n = keyI18n;
      }
      return libelleI18n;
   }
   
   public void populateParametre() {
      getParametre().setValeur(newValeur);
   }
   
   public boolean isEditMode(){
      return editMode;
   }

   public void setEditMode(boolean editMode){
      this.editMode = editMode;
   }

   public String getNewValeur(){
      return newValeur;
   }

   public void setNewValeur(String valeur){
      this.newValeur = valeur;
   }

   public boolean isDefaultValeur(){
      return defaultValeur;
   }

   public void setDefaultValeur(boolean defaultValeur){
      this.defaultValeur = defaultValeur;
   }

   public ParametreDTO getParametre(){
      return parametre;
   }

   public void setParametre(ParametreDTO parametre){
      this.parametre = parametre;
   }
   
   public ConstParametreValeur getConstParametreValeur(){
      return constParametreValeur;
   }
   
   // définition des getters pour les champs de parametre qui ne peuvent pas être modifiés
   public String getCode() {
      return parametre.getCode();
   }
   public String getGroupe() {
      return parametre.getGroupe();
   }
   public String getType() {
      return parametre.getType();
   }
   //

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ParametreDecorator deco = (ParametreDecorator) obj;
      return this.parametre.equals(deco.getParametre());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEntite = 0;

      if(this.parametre != null){
         hashEntite = this.parametre.hashCode();
      }

      hash = 7 * hash + hashEntite;

      return hash;
   }
}
