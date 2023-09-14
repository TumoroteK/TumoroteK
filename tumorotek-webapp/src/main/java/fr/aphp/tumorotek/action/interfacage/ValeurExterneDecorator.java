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
package fr.aphp.tumorotek.action.interfacage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

public class ValeurExterneDecorator
{

   /** Objets principaux. **/
   private ValeurExterne valeurExterne;

   private Object oldObject;

   private List<Echantillon> echantillons = new ArrayList<>();

   private String oldValue;

   private String newValue;

   private boolean importer = true;

   // since 2.0.13
   private CodeCommon code;

   public ValeurExterneDecorator(final ValeurExterne val, final Object obj, final boolean imp, final List<Echantillon> echans,
      final CodeCommon _c){
      this.valeurExterne = val;
      this.oldObject = obj;
      this.importer = imp;
      this.echantillons = echans;
      this.code = _c;

      initNewValue();
      initOldValue();
      initImporter();
   }

   public ValeurExterne getValeurExterne(){
      return valeurExterne;
   }

   public void setValeurExterne(final ValeurExterne v){
      this.valeurExterne = v;
   }

   public Object getOldObject(){
      return oldObject;
   }

   public void setOldObject(final Object o){
      this.oldObject = o;
   }

   public boolean isImporter(){
      return importer;
   }

   public void setImporter(final boolean i){
      this.importer = i;
   }

   public String getNomChamp(){
      String nom = "";
      if(valeurExterne.getChampEntiteId() != null){
         final ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(valeurExterne.getChampEntiteId());
         if(ce != null){
            nom = ObjectTypesFormatters.getLabelForChampEntite(ce);
         }
      }else if(valeurExterne.getChampAnnotationId() != null){
         final ChampAnnotation ca =
            ManagerLocator.getChampAnnotationManager().findByIdManager(valeurExterne.getChampAnnotationId());
         if(ca != null){
            nom = ca.getNom();
         }
      }
      return nom;
   }

   public void initOldValue(){
      if(oldObject != null){
         setOldValue(getValueForObject(oldObject));
      }else if(echantillons != null && echantillons.size() > 0){
         final StringBuffer sb = new StringBuffer();
         final List<String> tmps = new ArrayList<>();
         for(int i = 0; i < echantillons.size(); i++){
            final String tmp = getValueForObject(echantillons.get(i));
            if(tmp != null && !tmp.equals("") && !tmps.contains(tmp)){
               tmps.add(tmp);
            }
         }
         for(int i = 0; i < tmps.size(); i++){
            if(i > 0){
               sb.append(", ");
            }
            sb.append(tmps.get(i));
         }
         setOldValue(sb.toString());
      }else{
         setOldValue(null);
      }
   }

   public String getValueForObject(final Object obj){
      String value = null;
      if(valeurExterne.getChampEntiteId() != null){
         final ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(valeurExterne.getChampEntiteId());
         // dans le cas où on souhaiterait afficher les codes
         // lésionnels ou organes d'un échantillon
         if(ce.getNom().equals("CodeOrganes")){
            // si ce sont les codes organes à extraire
            final List<String> codes = ManagerLocator.getCodeAssigneManager().formatCodesAsStringsManager(
               ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager((Echantillon) obj));
            final StringBuffer sb = new StringBuffer();
            for(int k = 0; k < codes.size(); k++){
               sb.append(codes.get(k));
               if(k + 1 < codes.size()){
                  sb.append(", ");
               }
            }
            value = sb.toString();
         }else if(ce.getNom().equals("CodeMorphos")){
            // si ce sont les codes morphos à extraire
            final List<String> codes = ManagerLocator.getCodeAssigneManager().formatCodesAsStringsManager(
               ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager((Echantillon) obj));
            final StringBuffer sb = new StringBuffer();
            for(int k = 0; k < codes.size(); k++){
               sb.append(codes.get(k));
               if(k + 1 < codes.size()){
                  sb.append(", ");
               }
            }
            value = sb.toString();
         }else{
            value = (String) ManagerLocator.getChampManager().getValueForObjectManager(new Champ(ce), obj, true);
         }
      }else if(valeurExterne.getChampAnnotationId() != null){
         final ChampAnnotation ca =
            ManagerLocator.getChampAnnotationManager().findByIdManager(valeurExterne.getChampAnnotationId());

         // on récupère toutes les valeurs
         final List<AnnotationValeur> valeurs =
            ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(ca, (TKAnnotableObject) obj);
         final StringBuffer sb = new StringBuffer();
         for(int j = 0; j < valeurs.size(); j++){
            sb.append(ObjectTypesFormatters.annotationValeurRenderer(valeurs.get(j)));

            if(j < valeurs.size() - 1){
               sb.append(", ");
            }
         }
         value = sb.toString();
      }
      return value;
   }

   public void initNewValue(){
      String value = valeurExterne.getValeur();

      if(valeurExterne.getChampEntiteId() != null){
         final ChampEntite ce = ManagerLocator.getChampEntiteManager().findByIdManager(valeurExterne.getChampEntiteId());
         // on formate la nouvelle valeur si c'est une date
         if(ce.getDataType().getType().matches("date.*")){
            // si l'attibut est un calendar, on caste
            // la valeur issue du fichier
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try{
               date = sdf.parse(value);
            }catch(final ParseException e){
               sdf = new SimpleDateFormat("yyyyMMddHHmm");
               try{
                  date = sdf.parse(value);
               }catch(final ParseException e1){
                  sdf = new SimpleDateFormat("yyyyMMdd");
                  try{
                     date = sdf.parse(value);
                  }catch(final ParseException e2){}
               }
            }
            Calendar cal = Calendar.getInstance();
            if(date != null){
               cal.setTime(date);
            }else{
               cal = null;
            }
            value = ObjectTypesFormatters.dateRenderer2(cal);
         }else if(ce.getDataType().getType().equals("boolean")){
            // on formate la nouvelle valeur si c'est un boolean
            if("0".equals(value)){
               value = Labels.getLabel("general.checkbox.false");
            }else if("1".equals(value)){
               value = Labels.getLabel("general.checkbox.true");
            }
         }else{
            if(ce.getQueryChamp() != null){
               // on formate la nouvelle valeur si c'est un collaborateur
               if(ce.getQueryChamp().getEntite().getNom().equals("Collaborateur")){
                  final Collaborateur coll =
                     ManagerLocator.getCollaborateurManager().findByIdManager(Integer.parseInt(valeurExterne.getValeur()));
                  if(coll != null){
                     value = coll.getNom();
                  }
               }else if(ce.getQueryChamp().getEntite().getNom().equals("Service")){
                  // on formate la nouvelle valeur si c'est un service
                  final Service serv =
                     ManagerLocator.getServiceManager().findByIdManager(Integer.parseInt(valeurExterne.getValeur()));
                  if(serv != null){
                     value = serv.getNom();
                  }
               }else if(ce.getQueryChamp().getEntite().getNom().equals("Etablissement")){
                  // on formate la nouvelle valeur si c'est
                  // un etablissemet
                  final Etablissement etab =
                     ManagerLocator.getEtablissementManager().findByIdManager(Integer.parseInt(valeurExterne.getValeur()));
                  if(etab != null){
                     value = etab.getNom();
                  }
               }
            }
            // formatage des codes
            // if (ce.getNom().equals("CodeOrganes")
            //		|| ce.getNom().equals("CodeMorphos")) {
            //	value = value.replace(";", ", ");
            // }
            if(code != null){
               value = code.getCode() + (code.getLibelle() != null ? " - " + code.getLibelle() : "");
            }
         }
      }else if(valeurExterne.getChampAnnotationId() != null){
         final ChampAnnotation ca =
            ManagerLocator.getChampAnnotationManager().findByIdManager(valeurExterne.getChampAnnotationId());

         // on formate la nouvelle valeur si c'est une date
         if(ca.getDataType().getType().equals("date")){
            // si l'attibut est un calendar, on caste
            // la valeur issue du fichier
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try{
               date = sdf.parse(value);
            }catch(final ParseException e){
               sdf = new SimpleDateFormat("yyyyMMddHHmm");
               try{
                  date = sdf.parse(value);
               }catch(final ParseException e1){
                  sdf = new SimpleDateFormat("yyyyMMdd");
                  try{
                     date = sdf.parse(value);
                  }catch(final ParseException e2){}
               }
            }
            Calendar cal = Calendar.getInstance();
            if(date != null){
               cal.setTime(date);
            }else{
               cal = null;
            }
            value = ObjectTypesFormatters.dateRenderer2(cal);
         }else if(ca.getDataType().getType().equals("boolean")){
            // on formate la nouvelle valeur si c'est un boolean
            if("0".equals(value)){
               value = Labels.getLabel("general.checkbox.false");
            }else if("1".equals(value)){
               value = Labels.getLabel("general.checkbox.true");
            }
         }
      }

      setNewValue(value);
   }

   /**
    * Initialise la variable importer : si la nouvelle valeur et
    * l'ancienne sont égales, importer sera à faux.
    */
   public void initImporter(){
      if(oldValue != null){
         setImporter(!oldValue.equals(newValue));
      }else{
         setImporter(newValue != null);
      }
   }

   /**
    * Decore une liste de ValeurExterne
    */
   public static List<ValeurExterneDecorator> decorateListe(final List<ValeurExterne> valeurs, final Object obj,
      final boolean imp){
      final List<ValeurExterneDecorator> liste = new ArrayList<>();
      final Iterator<ValeurExterne> it = valeurs.iterator();
      while(it.hasNext()){
         liste.add(new ValeurExterneDecorator(it.next(), obj, imp, null, null));
      }
      return liste;
   }

   /**
    * Decore la liste pour les champ d'informations propres aux
    * échantillons.
    * @since 2.0.13
    */
   public static List<ValeurExterneDecorator> decorateListeEchantillons(final List<ValeurExterne> valeurs, final Object obj,
      final boolean imp, final List<Echantillon> echans, final List<Banque> banques, final List<TableCodage> tables){

      final List<ValeurExterneDecorator> liste = new ArrayList<>();
      for(final ValeurExterne val : valeurs){
         if(val.getChampEntiteId() != null && (val.getChampEntiteId() == 229 || val.getChampEntiteId() == 230)){

            String[] codesVal = null;
            // DIAMIC Hack
            if(val.getValeur().contains("~")){
               codesVal = val.getValeur().split("~");
            }else{
               codesVal = val.getValeur().split(";");
            }

            final Set<CodeCommon> allCodes = new HashSet<>();
            final Set<CodeCommon> codes = new HashSet<>();

            for(final String cVal : codesVal){
               codes.clear();
               CodeUtils.findCodesInAllTables(cVal, val.getChampEntiteId() == 229, val.getChampEntiteId() == 230, codes, true,
                  banques);
               if(!codes.isEmpty()){
                  allCodes.addAll(codes);
               }else{
                  final CodeAssigne codeAs = new CodeAssigne();
                  codeAs.setCode(cVal);
                  allCodes.add(codeAs);
               }
            }
            ValeurExterneDecorator deco;
            final List<CodeCommon> transcodes = new ArrayList<>();
            for(final CodeCommon code : allCodes){
               deco = new ValeurExterneDecorator(val, obj, imp, echans, code);
               liste.add(deco);
               transcodes.clear();
               transcodes.addAll(ManagerLocator.getTableCodageManager().transcodeManager(code, tables, banques));
               for(final CodeCommon transcode : transcodes){
                  deco = new ValeurExterneDecorator(val, obj, imp, echans, transcode);
                  liste.add(deco);
               }
            }

         }else{
            liste.add(new ValeurExterneDecorator(val, obj, imp, echans, null));
         }
      }
      return liste;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ValeurExterneDecorator deco = (ValeurExterneDecorator) obj;
      return this.getValeurExterne().equals(deco.getValeurExterne());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashValeur = 0;

      if(this.getValeurExterne() != null){
         hashValeur = this.getValeurExterne().hashCode();
      }

      hash = 7 * hash + hashValeur;

      return hash;
   }

   public String getOldValue(){
      return oldValue;
   }

   public void setOldValue(final String o){
      this.oldValue = o;
   }

   public String getNewValue(){
      return newValue;
   }

   public void setNewValue(final String n){
      this.newValue = n;
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public CodeCommon getCode(){
      return code;
   }

   public void setCode(final CodeCommon code){
      this.code = code;
   }
}
