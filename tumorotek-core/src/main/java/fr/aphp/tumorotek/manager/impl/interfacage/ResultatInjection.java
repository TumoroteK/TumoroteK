package fr.aphp.tumorotek.manager.impl.interfacage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Classe contenant les objets créés par l'injection d'un dossier
 * externe dans TK.
 *
 * @author Pierre Ventadour
 * Créée le 12/10/2011.
 *
 * @version 2.0.13
 *
 */
public class ResultatInjection
{

   private Prelevement prelevement;
   private List<AnnotationValeur> annosPrelevement = new ArrayList<>();
   private List<AnnotationValeur> annosPatient = new ArrayList<>();
   private Echantillon echantillon;
   private List<AnnotationValeur> annosEchantillon = new ArrayList<>();
   private DossierExterne dossierExterne;
   private List<CodeAssigne> codesOrgane = new ArrayList<>();
   private List<CodeAssigne> codesMorpho = new ArrayList<>();

   private Fichier crAnapath;
   private InputStream stream;

   public ResultatInjection(){
      super();
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

   public List<AnnotationValeur> getAnnosPrelevement(){
      return annosPrelevement;
   }

   public void setAnnosPrelevement(final List<AnnotationValeur> annos){
      this.annosPrelevement = annos;
   }

   public List<AnnotationValeur> getAnnosPatient(){
      return annosPatient;
   }

   public void setAnnosPatient(final List<AnnotationValeur> annos){
      this.annosPatient = annos;
   }

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon e){
      this.echantillon = e;
   }

   public List<AnnotationValeur> getAnnosEchantillon(){
      return annosEchantillon;
   }

   public void setAnnosEchantillon(final List<AnnotationValeur> annos){
      this.annosEchantillon = annos;
   }

   public List<CodeAssigne> getCodesOrgane(){
      return codesOrgane;
   }

   public void setCodesOrgane(final List<CodeAssigne> codesOrgane){
      this.codesOrgane = codesOrgane;
   }

   public List<CodeAssigne> getCodesMorpho(){
      return codesMorpho;
   }

   public void setCodesMorpho(final List<CodeAssigne> codesMorpho){
      this.codesMorpho = codesMorpho;
   }

   public Fichier getCrAnapath(){
      return crAnapath;
   }

   public void setCrAnapath(final Fichier crAnapath){
      this.crAnapath = crAnapath;
   }

   public DossierExterne getDossierExterne(){
      return dossierExterne;
   }

   public void setDossierExterne(final DossierExterne dExterne){
      this.dossierExterne = dExterne;
   }

   public InputStream getStream(){
      return stream;
   }

   public void setStream(final InputStream i){
      this.stream = i;
   }

}
