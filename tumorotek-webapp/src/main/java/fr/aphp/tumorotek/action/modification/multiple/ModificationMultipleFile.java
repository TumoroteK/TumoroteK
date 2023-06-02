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
package fr.aphp.tumorotek.action.modification.multiple;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import fr.aphp.tumorotek.manager.code.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.*;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.constraints.ConstFilename;
import fr.aphp.tumorotek.action.echantillon.EchantillonConstraints;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'un
 * fichier. Repose sur la fonctionalité de partage des compte-rendus
 * anapath entre différents échantillons.
 * Classe créée le 25/03/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ModificationMultipleFile extends AbstractModificationMultipleComponent
{
   private static final Logger log = LoggerFactory.getLogger(ModificationMultipleFile.class);

   private static final long serialVersionUID = 3551763682958457361L;

   private Textbox fileUniqueBox;

   private Textbox fileEraseBox;

   private Image addEraseFile;

   private Fichier file = null;

   private InputStream fileStream = null;

   @Override
   public Object extractValueFromEraserBox(){
      if(file != null && fileEraseBox.getValue() != null && !fileEraseBox.getValue().equals("")){
         file.setNom(fileEraseBox.getValue());
      }else{
         file = null;
         fileStream = null;
      }
      return file;
   }

   @Override
   public Object extractValueFromMultiBox(){
      if(file != null && fileUniqueBox.getValue() != null && !fileUniqueBox.getValue().equals("")){
         file.setNom(fileUniqueBox.getValue());
      }else{
         file = null;
         fileStream = null;
      }
      return file;
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      // fileEraseBox.setVisible(visible);
      addEraseFile.setVisible(visible);
   }

   @Override
   public void passValueToEraserBox(){
      if(getSelectedValue() != null){
         fileEraseBox.setValue(((Fichier) getSelectedValue()).getNom());
         file = (Fichier) getSelectedValue();
      }else{
         fileEraseBox.setValue(null);
         fileStream = null;
         file = null;
      }
      showDeleteAndFileEraseBox(getSelectedValue() != null);
   }

   @Override
   public void passNullToEraserBox(){}

   @Override
   public void setUniqueValueToMultiBox(){
      setNewValue(((Fichier) getOldUniqueValue()).clone());
      file = (Fichier) getNewValue();
      fileUniqueBox.setVisible(true);
      fileUniqueBox.setValue(((Fichier) getNewValue()).getNom());
   }

   @Override
   public void onClick$lock(){
      super.onClick$lock();
      // si re-lock met le stream a null
      if(multiListBox.isDisabled()){
         fileStream = null;
      }
   }

   @Override
   public void setOldUniqueValueAsNewObject(){
      setOldUniqueValue(new Fichier());
   }

   @Override
   public void extractValuesFromObjects(){

      // pour chaque objet à modifier
      // on extrait la valeur actuelle du champ à modifier
      // toutes ces valeurs sont placées dans la liste values
      setHasNulls(false);
      final Set<String> paths = new HashSet<>();
      for(final Object object : getListObjets()){

         try{

            boolean isDelegateProperty = false;
            TKDelegateObject<?> delegate = null;

            if(object instanceof TKDelegetableObject){
               delegate = ((TKDelegetableObject<?>) object).getDelegate();
               isDelegateProperty = delegate != null && PropertyUtils.describe(delegate).keySet().contains(getChamp());
            }

            Fichier fichier = null;
            if(isDelegateProperty){
               fichier = (Fichier) PropertyUtils.getSimpleProperty(delegate, getChamp());
            }else{
               fichier = (Fichier) PropertyUtils.getSimpleProperty(object, getChamp());
            }

            if(fichier != null){
               if(!paths.contains(fichier.getPath()) || !getStringValues().contains(fichier.getNom())){
                  paths.add(fichier.getPath());
                  getValues().add(fichier);
                  getStringValues().add(formatLocalObject(fichier));
               }
            }else{
               setHasNulls(true);
            }

         }catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            log.error(e.getMessage(), e);
         }

      }
   }

   /**
    * Formatte l'affichage d'un Fichier en utilisant son nom.
    */
   @Override
   public String formatLocalObject(final Object obj){
      if(obj != null){
         return ((Fichier) obj).getNom();
      }
      return null;
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationFile", true));
   }

   public void onClick$addUniqueFile(){
      Media[] medias;
      medias = Fileupload.get(
         ObjectTypesFormatters.getLabel("general.upload.limit",
            new String[] {String.valueOf(EchantillonConstraints.getSizeLimit())}),
         Labels.getLabel("ficheEchantillon" + ".crAnapath.upload.title"), 1, EchantillonConstraints.getSizeLimit(), true);
      if(medias != null && medias.length > 0){
         file = new Fichier();
         fileStream = new ByteArrayInputStream(medias[0].getByteData());
         fileUniqueBox.setValue(medias[0].getName());
         showDeleteAndFileUniqueBox(true);
         getCrAnapathConstraint().validate(fileUniqueBox, medias[0].getName());
      }
   }

   /**
    * Affiche ou non les composants delete/textbox pour un fichier
    * en mode edit.
    * @param boolean visible
    */
   public void showDeleteAndFileUniqueBox(final boolean visible){
      fileUniqueBox.setVisible(visible);
   }

   public void onBlur$fileUniqueBox(){
      getCrAnapathConstraint().validate(fileUniqueBox, fileUniqueBox.getValue());
   }

   public void onClick$addEraseFile(){
      Media[] medias;
      medias = Fileupload.get(
         ObjectTypesFormatters.getLabel("general.upload.limit",
            new String[] {String.valueOf(EchantillonConstraints.getSizeLimit())}),
         Labels.getLabel("ficheEchantillon" + ".crAnapath.upload.title"), 1, EchantillonConstraints.getSizeLimit(), true);
      if(medias != null && medias.length > 0){
         file = new Fichier();
         fileStream = new ByteArrayInputStream(medias[0].getByteData());
         fileEraseBox.setValue(medias[0].getName());
         showDeleteAndFileEraseBox(true);
         getCrAnapathConstraint().validate(fileEraseBox, medias[0].getName());
      }
   }

   /**
    * Affiche ou non les composants delete/textbox pour un fichier
    * en mode edit.
    * @param boolean visible
    */
   public void showDeleteAndFileEraseBox(final boolean visible){
      fileEraseBox.setVisible(visible);
   }

   public void onBlur$fileEraseBox(){
      getCrAnapathConstraint().validate(fileEraseBox, fileUniqueBox.getValue());
   }

   public ConstFilename getCrAnapathConstraint(){
      return EchantillonConstraints.getCrAnapathConstraint();
   }

   @Override
   public void onClick$validate(){
      Object finalValue = null;
      setChangedValue(false);
      // si on était en mode liste
      if(getValues().size() > 1){
         if(!multiListBox.isDisabled()){
            // si une valeur a été saisie dans le nouveau champ,
            // c'est la nouvelle valeur
            // sinon
            extractValueFromEraserBox();
            setNewValue(file);
         }
      }else{ // mode unique
         extractValueFromMultiBox();
         setNewValue(file);

      }

      // verifie le changement
      if((getNewValue() != null
         && ((getOldUniqueValue() != null && !((Fichier) getNewValue()).getNom().equals(((Fichier) getOldUniqueValue()).getNom()))
            || getHasNulls()))
         || (getNewValue() == null && getOldUniqueValue() != null) || fileStream != null){
         finalValue = getNewValue();
         setChangedValue(true);
      }

      final SimpleChampValue scv = new SimpleChampValue();
      scv.setChamp(getChamp());
      scv.setValue(finalValue);

      scv.setPrintValue(getPrintFinalValue(finalValue));

      // insert le stream dans le postback
      if(finalValue != null){
         final FilePack pck = new FilePack();
         pck.setFile((Fichier) finalValue);
         pck.setStream(fileStream);
         scv.setValue(pck);
      }

      postBack(scv);

      Events.postEvent(new Event("onClose", self.getRoot()));
   }
}
