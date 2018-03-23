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
package fr.aphp.tumorotek.action.historique;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
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
import fr.aphp.tumorotek.model.qualite.Fantome;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * HistoriqueRenderer affiche dans le Row
 * les membres d'Operation sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 23/11/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class HistoriqueRenderer implements RowRenderer<Operation>
{

   private String identifiant;
   private String collection;

   @Override
   public void render(final Row row, final Operation op, final int index) throws Exception{

      //Operation op = (Operation) data;
      identifiant = "-";
      collection = "-";

      setIdentifiantAndBanque(op);

      // Utilisateur
      new Label(op.getUtilisateur().getLogin()).setParent(row);

      // Type d'opération
      final StringBuffer type = new StringBuffer();
      final String label = Labels.getLabel("OperationType." + op.getOperationType().getNom());
      if(label != null){
         type.append(label);
      }else{
         type.append(op.getOperationType().getNom());

         if(type.toString().equals("Creation")){
            if(ManagerLocator.getImportHistoriqueManager()
               .findImportationsByEntiteAndObjectIdManager(op.getEntite(), op.getObjetId()).size() > 0){
               type.append(" (Import)");
            }
         }
      }

      new Label(type.toString()).setParent(row);

      // Entité
      new Label(getEntite(op)).setParent(row);

      // Identifiant
      new Label(identifiant).setParent(row);

      // Collection
      new Label(collection).setParent(row);

      // date de stockage
      new Label(ObjectTypesFormatters.dateRenderer2(op.getDate())).setParent(row);

      // applique style si v1
      if(op.getV1()){
         final Label lab = new Label("v1");
         lab.setStyle("color : #ff0000");
         row.setStyle("font-style : italic");
         lab.setParent(row);
      }else{
         new Label().setParent(row);
      }

   }

   public static String getOperationType(final Operation op){
      final StringBuffer type = new StringBuffer();
      type.append(op.getOperationType().getNom());

      if(type.toString().equals("Creation")){
         if(ManagerLocator.getImportHistoriqueManager()
            .findImportationsByEntiteAndObjectIdManager(op.getEntite(), op.getObjetId()).size() > 0){
            type.append(" (Import)");
         }
      }

      return type.toString();
   }

   public static String getEntite(final Operation op){
      String nom = "-";

      if(op.getEntite() != null){
         if(op.getEntite().getNom().equals("Fantome")){
            final Object obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(op.getEntite(), op.getObjetId());
            nom = ((Fantome) obj).getEntite().getNom();
         }else{
            nom = op.getEntite().getNom();
         }
      }

      return nom;
   }

   public void setIdentifiantAndBanque(final Operation op){
      Object obj = null;
      if(op.getEntite() != null){
         if(!op.getEntite().getNom().contains("Code")){
            obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(op.getEntite(), op.getObjetId());
         }

         if(obj != null){
            if(op.getEntite().getNom().equals("Fantome")){
               identifiant = ((Fantome) obj).getNom();
            }else if(op.getEntite().getNom().equals("Patient")){
               identifiant = ((Patient) obj).getNom();
            }else if(op.getEntite().getNom().equals("Maladie")){
               identifiant = ((Maladie) obj).getLibelle();
            }else if(op.getEntite().getNom().equals("Prelevement")){
               identifiant = ((Prelevement) obj).getCode();
               collection = ((Prelevement) obj).getBanque().getNom();
            }else if(op.getEntite().getNom().equals("Echantillon")){
               identifiant = ((Echantillon) obj).getCode();
               collection = ((Echantillon) obj).getBanque().getNom();
            }else if(op.getEntite().getNom().equals("ProdDerive")){
               identifiant = ((ProdDerive) obj).getCode();
               collection = ((ProdDerive) obj).getBanque().getNom();
            }else if(op.getEntite().getNom().equals("Cession")){
               identifiant = ((Cession) obj).getNumero();
               collection = ((Cession) obj).getBanque().getNom();
            }else if(op.getEntite().getNom().equals("Conteneur")){
               identifiant = ((Conteneur) obj).getCode();
            }else if(op.getEntite().getNom().equals("Enceinte")){
               identifiant = ((Enceinte) obj).getNom();
            }else if(op.getEntite().getNom().equals("Terminale")){
               identifiant = ((Terminale) obj).getNom();
            }else if(op.getEntite().getNom().equals("Collaborateur")){
               identifiant = ((Collaborateur) obj).getNom();
            }else if(op.getEntite().getNom().equals("Etablissement")){
               identifiant = ((Etablissement) obj).getNom();
            }else if(op.getEntite().getNom().equals("Service")){
               identifiant = ((Service) obj).getNom();
            }else if(op.getEntite().getNom().equals("Transporteur")){
               identifiant = ((Transporteur) obj).getNom();
            }else if(op.getEntite().getNom().equals("Contrat")){
               identifiant = ((Contrat) obj).getNumero();
            }else if(op.getEntite().getNom().equals("Profil")){
               identifiant = ((Profil) obj).getNom();
            }else if(op.getEntite().getNom().equals("Utilisateur")){
               identifiant = ((Utilisateur) obj).getLogin();
            }else if(op.getEntite().getNom().equals("Banque")){
               identifiant = ((Banque) obj).getNom();
               collection = ((Banque) obj).getNom();
            }else if(op.getEntite().getNom().equals("TableAnnotation")){
               identifiant = ((TableAnnotation) obj).getNom();
            }
         }
      }else if(op.getIdentificationDossier() != null){
         identifiant = op.getIdentificationDossier();
      }
   }

   public static String getIdentifiantForOperation(final Operation op){
      Object obj = null;
      String value = "-";
      if(!op.getEntite().getNom().contains("Code")){
         obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(op.getEntite(), op.getObjetId());
      }

      if(obj != null){
         if(op.getEntite().getNom().equals("Fantome")){
            value = ((Fantome) obj).getNom();
         }else if(op.getEntite().getNom().equals("Patient")){
            value = ((Patient) obj).getNom();
         }else if(op.getEntite().getNom().equals("Maladie")){
            value = ((Maladie) obj).getLibelle();
         }else if(op.getEntite().getNom().equals("Prelevement")){
            value = ((Prelevement) obj).getCode();
         }else if(op.getEntite().getNom().equals("Echantillon")){
            value = ((Echantillon) obj).getCode();
         }else if(op.getEntite().getNom().equals("ProdDerive")){
            value = ((ProdDerive) obj).getCode();
         }else if(op.getEntite().getNom().equals("Cession")){
            value = ((Cession) obj).getNumero();
         }else if(op.getEntite().getNom().equals("Conteneur")){
            value = ((Conteneur) obj).getCode();
         }else if(op.getEntite().getNom().equals("Enceinte")){
            value = ((Enceinte) obj).getNom();
         }else if(op.getEntite().getNom().equals("Terminale")){
            value = ((Terminale) obj).getNom();
         }else if(op.getEntite().getNom().equals("Collaborateur")){
            value = ((Collaborateur) obj).getNom();
         }else if(op.getEntite().getNom().equals("Etablissement")){
            value = ((Etablissement) obj).getNom();
         }else if(op.getEntite().getNom().equals("Service")){
            value = ((Service) obj).getNom();
         }else if(op.getEntite().getNom().equals("Transporteur")){
            value = ((Transporteur) obj).getNom();
         }else if(op.getEntite().getNom().equals("Contrat")){
            value = ((Contrat) obj).getNumero();
         }else if(op.getEntite().getNom().equals("Profil")){
            value = ((Profil) obj).getNom();
         }else if(op.getEntite().getNom().equals("Utilisateur")){
            value = ((Utilisateur) obj).getLogin();
         }else if(op.getEntite().getNom().equals("Banque")){
            value = ((Banque) obj).getNom();
         }else if(op.getEntite().getNom().equals("TableAnnotation")){
            value = ((TableAnnotation) obj).getNom();
         }
      }

      return value;
   }

   public static String getBanqueForOperation(final Operation op){
      Object obj = null;
      String value = "-";
      if(!op.getEntite().getNom().contains("Code")){
         obj = ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(op.getEntite(), op.getObjetId());
      }

      if(obj != null){
         if(op.getEntite().getNom().equals("Prelevement")){
            value = ((Prelevement) obj).getBanque().getNom();
         }else if(op.getEntite().getNom().equals("Echantillon")){
            value = ((Echantillon) obj).getBanque().getNom();
         }else if(op.getEntite().getNom().equals("ProdDerive")){
            value = ((ProdDerive) obj).getBanque().getNom();
         }else if(op.getEntite().getNom().equals("Cession")){
            value = ((Cession) obj).getBanque().getNom();
         }else if(op.getEntite().getNom().equals("Banque")){
            value = ((Banque) obj).getNom();
         }
      }

      return value;
   }

   public String getIdentifiant(){
      return identifiant;
   }

   public void setIdentifiant(final String i){
      this.identifiant = i;
   }

   public String getCollection(){
      return collection;
   }

   public void setCollection(final String c){
      this.collection = c;
   }

}
