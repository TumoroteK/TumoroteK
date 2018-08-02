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
package fr.aphp.tumorotek.action.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * Classe 'Decorateur' héritant de SmallObjDecorator permettant d'utilise
 * les objets CodeAssigne dans une SmallobjsEditableGrid..
 * date: 12/07/2010
 *
 * @version 2.0
 * @author mathieu BARTHELEMY
 *
 */
public class CodeAssigneDecorator extends SmallObjDecorator
{

   public CodeAssigneDecorator(final CodeAssigne c){
      setObj(c);
      setOrdreInit(c.getOrdre());
      setOrdre(c.getOrdre());
   }

   public void setCode(final String c){
      ((CodeAssigne) getObj()).setCode(c);
   }

   public String getCode(){
      return ((CodeAssigne) getObj()).getCode();
   }

   public void setLibelle(final String l){
      if(!l.equals("")){
         ((CodeAssigne) getObj()).setLibelle(l);
      }else{
         ((CodeAssigne) getObj()).setLibelle(null);
      }
   }

   public String getLibelle(){
      return ((CodeAssigne) getObj()).getLibelle();
   }

   public void setEchantillon(final Echantillon e){
      ((CodeAssigne) getObj()).setEchantillon(e);
   }

   public boolean getExport(){
      return ((CodeAssigne) getObj()).getExport();
   }

   public void setExport(final boolean e){
      ((CodeAssigne) getObj()).setExport(e);
      //		if (export) {
      //			if (((CodeAssigne) getObj()).getIsOrgane()) {
      //				((CodeAssigne) getObj())
      //					.setEchanExpOrg(((CodeAssigne) getObj()).getEchantillon());
      //			} else {
      //				((CodeAssigne) getObj())
      //					.setEchanExpLes(((CodeAssigne) getObj()).getEchantillon());
      //			}
      //		} else {
      //			((CodeAssigne) getObj()).setEchanExpOrg(null);
      //			((CodeAssigne) getObj()).setEchanExpLes(null);
      //		}
   }

   @Override
   public Object getObjClone(){
      return ((CodeAssigne) getObj()).clone();
   }

   @Override
   public Integer getObjDbId(){
      return ((CodeAssigne) getObj()).getCodeAssigneId();
   }

   /**
    * Decore la liste de codes assigne d'un echantillon.
    * Set le flag export au decorator referencant le code assigne
    * export specifie par l'echantillon.
    * @param liste de codes à décorer
    * @return codes décorés.
    */
   public static List<SmallObjDecorator> decorateListe(final List<CodeAssigne> codes){
      final List<SmallObjDecorator> liste = new ArrayList<>();
      final Iterator<CodeAssigne> it = codes.iterator();
      CodeAssigne next;
      CodeAssigneDecorator deco;
      while(it.hasNext()){
         next = it.next();
         deco = new CodeAssigneDecorator(next);
         // set flag export au bon decorateur
         //			if (next.getEchantillon() != null) {
         //				if ((next.equals(next.getEchantillon().getCodeOrganeExport()))
         //					|| (next.equals(next.getEchantillon()
         //													.getCodeLesExport()))) {
         //					deco.setExport(true);
         //				}
         //			}
         deco.setOrdreInit(next.getOrdre());
         deco.setOrdre(next.getOrdre());

         liste.add(deco);
      }
      return liste;
   }

   /**
    * Decore la liste de codes assigne d'un echantillon après avoir 
    * supprimé toute référence vers cet échantillon ainsi que tout ID 
    * en base de données. Cette méthode est utilisée lors de modifications 
    * multiples pour créer une nouvelle liste avec d'échantillons à partir 
    * d'une liste assignée à un des échantillons
    * @param liste de codes à décorer
    * @return codes décorés.
    */
   public static List<SmallObjDecorator> cleanDecorateListe(final List<CodeAssigne> codes){
      final List<SmallObjDecorator> res = decorateListe(codes);
      setCodeAssigneDecosAsNew(res);
      return res;

   }

   /**
    * Transforme une liste de codes assignes en une liste identique,
    * clonée, mais 
    * dépourvue pour chaque code de référence vers l'échantillon et d'ID 
    * en base de données.
    * Utilisée pour les modifications multiples
    * @param codes
    * @return liste codes sans références ni ID
    */
   private static void setCodeAssigneDecosAsNew(final List<SmallObjDecorator> codes){
      CodeAssigne c;
      CodeAssigneDecorator deco;
      if(codes != null){
         final Iterator<SmallObjDecorator> it = codes.iterator();
         while(it.hasNext()){
            deco = (CodeAssigneDecorator) it.next();
            c = (CodeAssigne) deco.getObjClone();
            c.setCodeAssigneId(null);
            c.setEchantillon(null);
            //c.setEchanExpOrg(null);
            //c.setEchanExpLes(null);
            deco.setObj(c);
            deco.setValidated(true);
         }
      }
   }

   @Override
   public void syncOrdre(){
      ((CodeAssigne) getObj()).setOrdre(getOrdre());
   }

   @Override
   public boolean ordreChanged(){
      return super.ordreChanged() || getOrdre() != ((CodeAssigne) getObj()).getOrdre();
   }

   public static List<CodeAssigne> undecorateListe(final List<SmallObjDecorator> decos){
      final List<CodeAssigne> res = new ArrayList<>();
      if(decos != null){
         for(int i = 0; i < decos.size(); i++){
            decos.get(i).syncOrdre();
            //decos.get(i).setOrdre(i + 1);
            res.add((CodeAssigne) decos.get(i).getObj());
         }
      }
      return res;
   }

   @Override
   public CodeAssigneDecorator clone(){
      final CodeAssigneDecorator clone = new CodeAssigneDecorator((CodeAssigne) getObjClone());
      clone.setExport(getExport());
      clone.setOrdre(getOrdre());
      clone.setOrdreInit(getOrdreInit());
      clone.setValidated(isValidated());
      return clone;
   }

   //	@Override
   //	public boolean equals(Object deco2) {
   //		
   //		CodeAssigne c1 = (CodeAssigne) getObj();
   //		CodeAssigne c2 = (CodeAssigne) ((CodeAssigneDecorator) deco2).getObj();
   //		
   //		return ( (c1.getCode() == c2.getCode() 
   //					|| (c1.getCode() != null 
   //							&& c1.getCode().equals(c2.getCode()))) 
   //			&& (c1.getLibelle() == c2.getLibelle() 
   //					|| (c1.getLibelle() != null 
   //					&& c1.getLibelle().equals(c2.getLibelle()))) 
   //			&& (c1.getIsOrgane() == c2.getIsOrgane() 
   //					|| (c1.getIsOrgane() != null 
   //					&& c1.getIsOrgane().equals(c2.getIsOrgane()))) 
   //			&& (c1.getIsMorpho() == c2.getIsMorpho() 
   //					|| (c1.getIsMorpho() != null 
   //					&& c1.getIsMorpho().equals(c2.getIsMorpho()))) 
   //		);
   //		
   //	}
}
