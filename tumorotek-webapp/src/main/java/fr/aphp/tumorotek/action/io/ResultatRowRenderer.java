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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ResultatRowRenderer implements RowRenderer<ResultatRow>
{

   private List<Resultat> resultats = new ArrayList<>();

   @Override
   public void render(final Row row, final ResultatRow data, final int index) throws Exception{

      final ResultatRow resultat = data;

      for(int i = 0; i < resultat.getValuesToShow().size(); i++){
         Label label = null;
         if(resultat.getValuesToShow().get(i) != null){
            if(!(resultat.getValuesToShow().get(i) instanceof Date) && !(resultat.getValuesToShow().get(i) instanceof Calendar)){
               label = new Label(resultat.getValuesToShow().get(i).toString());
               decorateLabel(label, resultats.get(i), resultat.getObjects().get(i));
            }else{
               label = new Label(ObjectTypesFormatters.dateRenderer2(resultat.getValuesToShow().get(i)));
            }
         }else{
            label = new Label("-");
         }
         label.setParent(row);
      }
   }

   public void decorateLabel(final Label label, final Resultat res, final Object obj){
      // On regarde dans le champ du resultat si c'est un entiteId
      if(res != null && res.getChamp() != null && res.getChamp().getChampEntite() != null){
         final Entite entite = res.getChamp().getChampEntite().getEntite();
         final String nomChampEntite = res.getChamp().getChampEntite().getNom();
         if(entite.getNom().equals("Patient") 
               && (nomChampEntite.equals("Nip") || nomChampEntite.equals("Identifiant")) ) {
            Patient recup = null;
            if(obj instanceof Patient){
               recup = (Patient) obj;
               label.addForward(null, label.getParent(), "onClickPatientNipOrIdentifiant", recup);
               label.setClass("formLink");
            }
         }else if(entite.getNom().equals("Prelevement") && nomChampEntite.equals("Code")){
            Prelevement recup = null;
            if(obj instanceof Prelevement){
               recup = (Prelevement) obj;
               label.addForward(null, label.getParent(), "onClickPrelevementCode", recup);
               label.setClass("formLink");
            }
         }else if(entite.getNom().equals("Echantillon") && nomChampEntite.equals("Code")){
            Echantillon recup = null;
            if(obj instanceof Echantillon){
               recup = (Echantillon) obj;
               label.addForward(null, label.getParent(), "onClickEchantillonCode", recup);
               label.setClass("formLink");
            }
         }else if(entite.getNom().equals("ProdDerive") && nomChampEntite.equals("Code")){
            ProdDerive recup = null;
            if(obj instanceof ProdDerive){
               recup = (ProdDerive) obj;
               label.addForward(null, label.getParent(), "onClickProdDeriveCode", recup);
               label.setClass("formLink");
            }
         }else if(entite.getNom().equals("Cession") && nomChampEntite.equals("Numero")){
            Cession recup = null;
            if(obj instanceof Cession){
               recup = (Cession) obj;
               label.addForward(null, label.getParent(), "onClickCessionNumero", recup);
               label.setClass("formLink");
            }
         }
      }
   }

   public List<Resultat> getResultats(){
      return resultats;
   }

   public void setResultats(final List<Resultat> r){
      this.resultats = r;
   }

}
