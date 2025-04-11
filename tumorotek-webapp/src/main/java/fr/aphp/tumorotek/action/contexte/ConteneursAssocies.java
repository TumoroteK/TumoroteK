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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.exception.ForbiddenI18nException;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/*
 * /!\ dans le cadre du ticket TK-636, cette classe a été réécrite pour respecter le principe d'indépendance de ce composant qui ne doit pas 
 * porter de règle de gestion propre à ces cas d'appel (pour cela, utilisation d'une "strategy")
 * De même, les objets gérés par ce composant (objects) n'ont pas à être reportés comme attributs des objets appelants
 * Cela a eu pour conséquence de gérer la copy de ceux-ci dans cette classe et de définir une méthode revert.
 * Par conséquent cette classe est différente des autres classes XxxAssocies. 
 * Dans l'absolu, il faudrait les adapter avec la même logique mais c'est lourd et un peu risqué. Sera fait selon les besoins
 * 
 *
 */
public class ConteneursAssocies extends OneToManyComponent<ConteneurDecorator>
{

   private static final long serialVersionUID = 1L;

   private List<ConteneurDecorator> objects = new ArrayList<>();
   
   private List<ConteneurDecorator> copyObjects = new ArrayList<>();
   
   //TK-636 : certains traitements sont différents selon l'appelant (Fiche Banque ou Fiche Plateforme). 
   //On passe donc par le pattern Strategy : l'appelant instanciera la bonne stratégie
   private ConteneursAssociesStrategy conteneursAssociesStrategy;
   
   //plateforme dépendant de l'appelant. Elle est comparée à la plateforme des conteneurs
   //pour appliquer des règles de gestion particulières dans le cas de conteneur partagé
   private Plateforme plateforme;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);
   }
   
   public void initData(List<Conteneur> listConteneurDejaAssocie, Plateforme plateforme) {
      this.plateforme = plateforme;
      setObjects(decorateConteneurs(listConteneurDejaAssocie));
   }
   
   private List<ConteneurDecorator> decorateConteneurs(List<Conteneur> listConteneur) {
      List<ConteneurDecorator> result = new ArrayList<ConteneurDecorator>();
      if(listConteneur != null){
         for(final Conteneur conteneur : listConteneur){
            result.add(new ConteneurDecorator(conteneur, plateforme, 
                                    conteneursAssociesStrategy.returnIfConteneurIsSupprimable(plateforme, conteneur)));
         }
      }
      
      return result;
   }
   
   @Override
   public List<ConteneurDecorator> getObjects(){
      return this.objects;
   }

   @Override
   public void setObjects(final List<ConteneurDecorator> objs){
      this.objects = objs;
      copyObjects = objs;
      updateComponent();
   }
   
   public void revert() {
      setObjects(copyObjects);
   }
   
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme p){
      this.plateforme = p;
   }

   public void setConteneursAssociesStrategy(ConteneursAssociesStrategy conteneursAssociesStrategy){
      this.conteneursAssociesStrategy = conteneursAssociesStrategy;
   }
   
   @Override
   public void addToListObjects(final ConteneurDecorator obj){
      getObjects().add(obj);
   }

   @Override
   public void removeFromListObjects(final Object obj){
      getObjects().remove(obj);
   }

   /**
    * Lien vers la fiche detaillee de l'objet.
    */
   @Override
   public void onClick$objLinkLabel(final Event event){
      if(objLinkLabel.getSclass().equals("formLink")){
         // recupere le conteneur
         final Object cont = AbstractListeController2.getBindingData((ForwardEvent) event, false);

         // ouvre l'onglet conteneur;
         final StockageController controller = StockageController.backToMe(getMainWindow(), page);
         controller.switchToFicheConteneurMode(((ConteneurDecorator) cont).getConteneur());
      }
   }

   @Override
   public void switchToEditMode(final boolean b){
      super.switchToEditMode(SessionUtils.isAdminPF(sessionScope));
   }

   @Override
   public String getGroupHeaderValue(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("Champ.Banque.Conteneurs"));
      sb.append(" (");
      sb.append(getObjects().size());
      sb.append(")");
      return sb.toString();
   }
   
   @Override
   public List<ConteneurDecorator> findObjectsAddable(){
      // conteneurs ajoutables sous forme de conteneurDecorator
      List<ConteneurDecorator> listConteneurDecoratorAjoutable = new ArrayList<>();
      
      //Pour déterminer les conteneurs ajoutables, on va chercher tous les conteneurs "associables" et on retire les conteneurs déjà ajoutés. Cela permet de garder les 2 listes bien synchronisées
      List<Conteneur> listConteneur = new ArrayList<Conteneur>(conteneursAssociesStrategy.retrieveListAllConteneurAssociable(plateforme));

      //on retire les conteneurs déjà associés
      for(int i = 0; i < getObjects().size(); i++){
         listConteneur.remove(getObjects().get(i).getConteneur());
      }
      
      //transforme en decorator
      listConteneurDecoratorAjoutable.addAll(decorateConteneurs(listConteneur));

      return listConteneurDecoratorAjoutable;
   }
   
   
   @Override
   public void drawActionForComponent(){
      addObj.setDisabled(!SessionUtils.isAdminPF(sessionScope));

      // si pas le droit d'accès aux conteneurs, on cache le lien
      if(!getDroitsConsultation().get("Stockage")){
         objLinkLabel.setSclass("formValue");
      }else{
         objLinkLabel.setSclass("formLink");
      }
   }

   /**
    * cette méthode permet de renvoyer à l'appelant du composant les conteneurs pour l'association
    * @return
    */
   public List<Conteneur> retrieveListConteneurForAssociation() {
      return conteneursAssociesStrategy.retrieveListConteneurForAssociation(objects);
   }
   
   /**
    * Vérifie qu'aucun référencement sur ce conteneur, impliquant un
    * probable stockage de matériel, n'a été établi à partir de la
    * ConteneurPlateforme.
    **/
   @Override
   public void onClick$deleteImage(final Event event){

      final ConteneurDecorator cur = (ConteneurDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      try {
         conteneursAssociesStrategy.doBeforeDelete(cur);
         super.onClick$deleteImage(event);
      }
      catch (ForbiddenI18nException forbiddenI18nException) {
         Messagebox.show(Labels.getLabel(forbiddenI18nException.getKeyI18n()), Labels.getLabel("general.warning"),
            Messagebox.OK, Messagebox.ERROR);
      }

   }
}
