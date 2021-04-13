package fr.aphp.tumorotek.model.contexte;

/**
 * Created by JDI on 27/11/2017.
 */
public enum EContexte
{
   DEFAUT("DEFAUT", "Anatomopathologie"), SEROLOGIE("SEROLOGIE", "Sérologie"), HEMATOLOGIE("HEMATOLOGIE", "Hématologie");

   private String nom;
   private String libelle;

   EContexte(final String nom, final String libelle){
      this.nom = nom;
      this.libelle = libelle;
   }

   public String getNom(){
      return nom;
   }


   public String getLibelle(){
      return libelle;
   }

   @Override
   public String toString(){
      return nom;
   }
}
