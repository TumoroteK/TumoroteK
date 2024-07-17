package fr.aphp.tumorotek.param;

/**
 * ces valeurs correspondent au type attendu pour la valeur saisie par l'utilisateur.
 * la généricité du mécanisme fait que la donnée sera toujours stockée en base de données sous forme 
 * d'une chaîne de caractères. Mais connaître le type attendu permet de faire des contrôles lors de la saisie
 * NB : lors de la saisie, pour le type Boolean, des radio-boutons (Oui / Non) seront proposés mais pour les autres 
 * ce sera un textbox
 * 
 * @author chuet
 *
 */
public enum EParametreType
{
   //BOOLEAN sera associé à un radio button dans la page des paramètres
   BOOLEAN ("Boolean"), 
   //les autres types sont associés à un textbox dans la page des paramètres
   STRING("String"),
   INTEGER("Integer"),
   FLOAT("Float");
   
   private String type;
   
   EParametreType(String type) {
      this.type = type;
   }
   
   public String getType(){
      return type;
   }
   
}
