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
package fr.aphp.tumorotek.action.etiquettes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;

/**
 * Modifiée par Mathieu BARTHELEMY pour ajouter méthode print générique.
 * Date: 07/04/2013
 *
 * @since 2.0.10
 */
public class MBioBarcodePrinter
{

   private final Log log = LogFactory.getLog(MBioBarcodePrinter.class);

   private final int labelFormat;
   private final int printerId;
   private MBioFileProperties mbioFile;
   private final String confDir;

   /** 
    * @param _banqueDb
    */
   public MBioBarcodePrinter(final MBioFileProperties mbio, final String url){
      mbioFile = mbio;

      confDir = "" + url;
      final String labelFormatStr = mbioFile.getLabelFormatId();

      labelFormat = Integer.parseInt(labelFormatStr);
      printerId = mbioFile.getPrinterId();
   }

   public MBioBarcodePrinter(){

      //confDir = "../../conf/Catalina/localhost/mbio";
      confDir = ManagerLocator.getResourceBundleMbio().getMbioConfDirectory();

      labelFormat = 1;
      printerId = 1;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes codes
    * a barres d'une liste de patients.
    */
   public int printPatient(final List<Patient> patients, final int nb){
      return 0;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste de prelevements.
    */
   public int printPrelevement(final List<Prelevement> prelevements, final int nb){
      return 0;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste d'echantillons.
    */
   public int printEchantillon(final List<Echantillon> echantillons, final int nb){
      int codeRetour = 0;

      int i = 0;
      for(i = 0; i < echantillons.size(); i++){

         final Vector<String> data = ManagerLocator.getTumoPrinterUtilsManager().extractStaticDataForMBio(echantillons.get(i));

         final int ok = printData(data, nb);

         if(ok == 0){
            break;
         }
      }

      if(i == echantillons.size()){
         codeRetour = 1;
      }else{
         codeRetour = 0;
      }

      return codeRetour;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste de derives.
    */
   public int printDerive(final List<ProdDerive> derives, final int nb){
      int codeRetour = 0;

      int i = 0;
      for(i = 0; i < derives.size(); i++){

         final Vector<String> data = ManagerLocator.getTumoPrinterUtilsManager().extractStaticDataForMBio(derives.get(i));

         final int ok = printData(data, nb);

         if(ok == 0){
            break;
         }
      }

      if(i == derives.size()){
         codeRetour = 1;
      }else{
         codeRetour = 0;
      }

      return codeRetour;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste de cessions. 
    */
   public int printCession(final List<Cession> cessions, final int nb){
      return 0;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste de données.
    */
   public int printListData(final List<Vector<String>> datas){
      int codeRetour = 0;

      int i = 0;
      for(i = 0; i < datas.size(); i++){

         final int ok = printData(datas.get(i), 1);

         if(ok == 0){
            break;
         }
      }

      if(i == datas.size()){
         codeRetour = 1;
      }else{
         codeRetour = 0;
      }

      return codeRetour;
   }

   /**
    * Fonction qui effectue l'impression des etiquettes 
    * codes a barres d'une liste de (6) parametres.
    */
   public int printData(final Vector<String> data, final int nb){
      int codeRetour = 0;

      // Se referer au fichier de configuration mbioApi.xml 
      // pour definir le labelFormat et le printerId
      // Et aussi pour definir la correspondance entre les 
      // parametres et leur place sur l'etiquette.
      final int quantity = nb;

      try{
         /** instancie et appelle le MBioPrinter par reflection
          * pour ne pas inclure .jar com.modulbio dans le 
          * classpath.
          */
         final Class<?> mbio = Class.forName("com.modulbio.printer.MBioPrint");
         final Constructor<?> con = mbio.getConstructor(String.class);
         final Object printer = con.newInstance(confDir);
         final Class<?>[] partypes = new Class<?>[4];
         partypes[0] = Vector.class;
         partypes[1] = int.class;
         partypes[2] = int.class;
         partypes[3] = int.class;

         final Method printMeth = mbio.getDeclaredMethod("print", partypes);
         printMeth.invoke(printer, new Object[] {data, quantity, labelFormat, printerId});
         codeRetour = 1;
         // MBioPrint printer = new MBioPrint(confDir);
         // printer.print(data, quantity, labelFormat, printerId);

      }catch(final ClassNotFoundException e){
         log.error("mbio package not found");
         codeRetour = 0;
         log.error(e);
      }catch(final SecurityException e){
         codeRetour = 0;
         log.error(e);
         e.printStackTrace();
      }catch(final NoSuchMethodException e){
         codeRetour = 0;
         log.error(e);
         e.printStackTrace();
      }catch(final IllegalArgumentException e){
         codeRetour = 0;
         log.error(e);
         e.printStackTrace();
      }catch(final InstantiationException e){
         codeRetour = 0;
         log.error(e);
         e.printStackTrace();
      }catch(final IllegalAccessException e){
         codeRetour = 0;
         log.error(e);
         e.printStackTrace();
      }catch(final InvocationTargetException e){
         codeRetour = 0;
         e.printStackTrace();
         log.error(e);
         e.printStackTrace();
      }catch(final Exception e){
         codeRetour = 0;
         log.error("printData:Exception," + e);
         e.printStackTrace();
      }

      return codeRetour;
   }

   /**
    * Méthode d'entrée unique pour imprimer un TKStockableObject.
    * @since 2.0.10
    * @param objects
    * @param nb à imprimer
    * @return 0 si success
    */
   public int printStockableObject(final List<? extends TKStockableObject> objects, final int nb){
      int codeRetour = 0;

      int i = 0;
      for(i = 0; i < objects.size(); i++){

         final Vector<String> data = ManagerLocator.getTumoPrinterUtilsManager().extractStaticDataForMBio(objects.get(i));

         final int ok = printData(data, nb);

         if(ok == 0){
            break;
         }
      }

      if(i == objects.size()){
         codeRetour = 1;
      }else{
         codeRetour = 0;
      }

      return codeRetour;
   }

}
