package fr.aphp.tumorotek.model.validation;

public enum OperateursComparaison
{

   SUPERIEUR(">"),
   SUPERIEUR_EGAL(">="),
   EGAL("="),
   DIFFERENT("!="),
   INFERIEUR_EGAL("<="),
   INFERIEUR("<");

   private String label;
   
   /**
    * Constructeur
    * @param label
    */
   private OperateursComparaison(String label){
      this.label = label;
   }

   /**
    * @return the label
    */
   public String getLabel(){
      return label;
   }
   
}
