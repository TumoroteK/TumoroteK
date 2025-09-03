package fr.aphp.tumorotek.action.historique;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.qualite.EOperationTypeId;
import fr.aphp.tumorotek.model.qualite.Fantome;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * classe utilitaire pour l'affichage d'un historique
 * @author chuet
 *
 */
public class HistoriqueUtils
{
   /**
    * construit le libellé de l'action effectuée correspondant à l'opération à afficher dans l'historique
    * le libellé est internationalisé
    * @param op
    * @return le libellé à afficher
    */
   public static String buildOperationToDisplay(final Operation operation){
      final StringBuffer type = new StringBuffer();
      
      final String operationNom = operation.getOperationType().getNom();
      final Integer operationTypeId = operation.getOperationType().getOperationTypeId();
      final String labelOperationType = Labels.getLabel("OperationType." + operationNom);

      if(labelOperationType != null){
         type.append(labelOperationType);
      }else{
         type.append(operationNom);
      }
      //Dans le cas d'une création, on complète l'information avec (Import) si la création a eu lieu suite à un import.
      //NB1 : la création par import passe par exactement le même code que la création manuelle, c'est pour cela que c'est le même type d'opération qui est utilisé dans les 2 cas.
      //pour faire la distinction, on regarde si un enregistrement existe dans la table IMPORTATION pour le type "NEW".
      //NB2 : pour le cas des imports en modification des annotations, le traitement est différent de celui de la modification manuelle donc 2 types d'opération différents sont utilisés. A noter que la règle de gestion 
      //utilisée pour la création n'aurait pas pu être appliquée pour la modification car l'utilisateur peut faire plusieurs modifications dans le temps sur un même objet soit manuellement soit par import
      //donc il aurait été difficile de rattacher les enregistrements dans importations à la bonne modification
      if(EOperationTypeId.CREATION.getId().equals(operationTypeId)){
         if(ManagerLocator.getImportHistoriqueManager()
            .findImportationsForCreationByEntiteIdAndObjectIdManager(operation.getEntite().getEntiteId(), operation.getObjetId()).size() > 0){
            final String labelSousTypeImport = Labels.getLabel("OperationSousType.Import");
            //on est sûr que la clé est définie car elle n'est pas dynamique => pas besoin de gérer le cas labelSousTypeImport null :
            type.append(" (").append(labelSousTypeImport).append(")");
         }
      }
      
      return type.toString();
   }
   
   /**
    * construit le nom de la collection correspondant à l'opération à afficher dans l'historique
    * @param operation
    * @return le nom à afficher
    */
   public static String buildBanqueToDisplay(final Operation operation){
      Object obj = null;
      String value = "-";
      if(!operation.getEntite().getNom().contains("Code")){
         obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(operation.getEntite(), operation.getObjetId());
      }

      if(obj != null){
         if(operation.getEntite().getNom().equals("Prelevement")){
            value = ((Prelevement) obj).getBanque().getNom();
         }else if(operation.getEntite().getNom().equals("Echantillon")){
            value = ((Echantillon) obj).getBanque().getNom();
         }else if(operation.getEntite().getNom().equals("ProdDerive")){
            value = ((ProdDerive) obj).getBanque().getNom();
         }else if(operation.getEntite().getNom().equals("Cession")){
            value = ((Cession) obj).getBanque().getNom();
         }else if(operation.getEntite().getNom().equals("Banque")){
            value = ((Banque) obj).getNom();
         }
      }

      return value;
   }
   
   /**
    * construit le nom de l'entité correspondant à l'opération à afficher dans l'historique
    * @param operation
    * @return le nom à afficher
    */   
   public static String buildEntiteToDisplay(final Operation operation){
      String nom = "-";

      if(operation.getEntite() != null){
         if(operation.getEntite().getNom().equals("Fantome")){
            final Object obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(
               operation.getEntite(), operation.getObjetId());
            nom = ((Fantome) obj).getEntite().getNom();
         }else{
            nom = operation.getEntite().getNom();
         }
      }

      return nom;
   }
   
   /**
    * construit le nom de l'objet correspondant à l'opération à afficher dans l'historique (colonne identifiant)
    * @param operation
    * @return le nom à afficher
    */  
   public static String buildIdentifiantToDisplay(final Operation operation){
      Object obj = null;
      String value = "-";
      if(!operation.getEntite().getNom().contains("Code")){
         obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(operation.getEntite(), operation.getObjetId());
      }

      if(obj != null){
         if(operation.getEntite().getNom().equals("Fantome")){
            value = ((Fantome) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Patient")){
            value = ((Patient) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Maladie")){
            value = ((Maladie) obj).getLibelle();
         }else if(operation.getEntite().getNom().equals("Prelevement")){
            value = ((Prelevement) obj).getCode();
         }else if(operation.getEntite().getNom().equals("Echantillon")){
            value = ((Echantillon) obj).getCode();
         }else if(operation.getEntite().getNom().equals("ProdDerive")){
            value = ((ProdDerive) obj).getCode();
         }else if(operation.getEntite().getNom().equals("Cession")){
            value = ((Cession) obj).getNumero();
         }else if(operation.getEntite().getNom().equals("Conteneur")){
            value = ((Conteneur) obj).getCode();
         }else if(operation.getEntite().getNom().equals("Enceinte")){
            value = ((Enceinte) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Terminale")){
            value = ((Terminale) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Collaborateur")){
            value = ((Collaborateur) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Etablissement")){
            value = ((Etablissement) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Service")){
            value = ((Service) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Transporteur")){
            value = ((Transporteur) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Contrat")){
            value = ((Contrat) obj).getNumero();
         }else if(operation.getEntite().getNom().equals("Profil")){
            value = ((Profil) obj).getNom();
         }else if(operation.getEntite().getNom().equals("Utilisateur")){
            value = ((Utilisateur) obj).getLogin();
         }else if(operation.getEntite().getNom().equals("Banque")){
            value = ((Banque) obj).getNom();
         }else if(operation.getEntite().getNom().equals("TableAnnotation")){
            value = ((TableAnnotation) obj).getNom();
         }
      }

      return value;
   }
}
