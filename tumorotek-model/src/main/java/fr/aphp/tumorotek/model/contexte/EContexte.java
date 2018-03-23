package fr.aphp.tumorotek.model.contexte;

/**
 * Created by JDI on 27/11/2017.
 */
public enum EContexte
{
   DEFAUT("DEFAUT", "Anatomopathologie"), SEROLOGIE("SEROLOGIE", "Sérologie"), BTO("BTO", "Banque de tissus osseux");

   private String nom;
   private String libelle;

   EContexte(final String nom, final String libelle){
      this.nom = nom;
      this.libelle = libelle;
   }

   @Override
   public String toString(){
      return nom;
   }
}
