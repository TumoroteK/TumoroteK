package fr.aphp.tumorotek.action.contexte;

import java.text.DecimalFormat;

public class DuoEntites
{

   private int idEntiteA;
   private int idEntiteB;
   private String nomEntiteA;
   private String nomEntiteB;
   private String taux;
   private String infoEntiteA;
   private String infoEntiteB;

   //pour arrondir le taux
   static DecimalFormat df = new DecimalFormat();

   //constructeur pour etablissement
   public DuoEntites(final int idA, final int idB, final String nomA, final String nomB, final double t){

      df.setMaximumFractionDigits(2);
      df.setMinimumFractionDigits(2);
      df.setDecimalSeparatorAlwaysShown(true);

      idEntiteA = idA;
      idEntiteB = idB;

      if(nomA != null){
         nomEntiteA = nomA;
      }else{
         nomEntiteA = "";
      }

      if(nomB != null){
         nomEntiteB = nomB;
      }else{
         nomEntiteB = "";
      }

      taux = df.format(t);

      infoEntiteA = (nomEntiteA.concat(" id : ")).concat(Integer.toString(idEntiteA));
      infoEntiteB = (nomEntiteB.concat(" id : ")).concat(Integer.toString(idEntiteB));

   }

   //constructeur pour Collaborateurs et Services
   public DuoEntites(final int idA, final int idB, final String nomA, final String nomB, final String nomEtaA,
      final String nomEtaB, final double t){

      df.setMaximumFractionDigits(2);
      df.setMinimumFractionDigits(2);
      df.setDecimalSeparatorAlwaysShown(true);

      idEntiteA = idA;
      idEntiteB = idB;
      if(nomA != null){
         nomEntiteA = nomA;
      }else{
         nomEntiteA = "";
      }

      if(nomB != null){
         nomEntiteB = nomB;
      }else{
         nomEntiteB = "";
      }

      taux = df.format(t);

      infoEntiteA = ((nomEntiteA.concat(" id : ")).concat(Integer.toString(idEntiteA)));
      if(nomEtaA != null){
         infoEntiteA = infoEntiteA + ", etablissement : " + nomEtaA;
      }
      infoEntiteB = ((nomEntiteB.concat(" id : ")).concat(Integer.toString(idEntiteB)));
      if(nomEtaB != null){
         infoEntiteB = infoEntiteB + ", etablissement : " + nomEtaB;
      }

   }

   public int getIdEntiteA(){
      return idEntiteA;
   }

   public int getIdEntiteB(){
      return idEntiteB;
   }

   public String getTaux(){
      return taux;
   }

   public void setIdEntiteA(final int idEntiteA){
      this.idEntiteA = idEntiteA;
   }

   public void setIdEntiteB(final int idEntiteB){
      this.idEntiteB = idEntiteB;
   }

   public void setTaux(final String taux){
      this.taux = taux;
   }

   public String getNomEntiteA(){
      return nomEntiteB;
   }

   public void setNomEntiteA(final String nomEntiteB){
      this.nomEntiteB = nomEntiteB;
   }

   public String getNomEntiteB(){
      return nomEntiteB;
   }

   public void setNomEntiteB(final String nomEntiteB){
      this.nomEntiteB = nomEntiteB;
   }

   public String getInfoEntiteA(){
      return infoEntiteA;
   }

   public String getInfoEntiteB(){
      return infoEntiteB;
   }

   public void setInfoEntiteA(final String infoEntiteA){
      this.infoEntiteA = infoEntiteA;
   }

   public void setInfoEntiteB(final String infoEntiteB){
      this.infoEntiteB = infoEntiteB;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final DuoEntites test = (DuoEntites) obj;
      return (this.idEntiteA == test.idEntiteA && this.idEntiteB == test.idEntiteB)
         || (this.idEntiteA == test.idEntiteB && this.idEntiteB == test.idEntiteA);
   }

   @Override
   public int hashCode(){
      return super.hashCode();
   }

}
