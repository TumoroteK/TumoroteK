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
package fr.aphp.tumorotek.webapp.tree.export;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;

/**
 * Classe abstraite représentant un noeud d'un arbre RequeteModel.
 * Classe créée le 09/02/10.
 *
 * @author GOUSSEAU Maxime.
 * @version 2.0.
 *
 */
public class GroupementNode extends ExportNode
{

   /**
    * Liste des enfants du noeud.
    */
   ExportNode node1 = null;

   ExportNode node2 = null;

   Integer operateur = null;

   Groupement groupement = null;

   public GroupementNode(){
      super();
   }

   public ExportNode getNode1(){
      return node1;
   }

   public void setNode1(final ExportNode n1){
      this.node1 = n1;
   }

   public ExportNode getNode2(){
      return node2;
   }

   public void setNode2(final ExportNode n2){
      this.node2 = n2;
   }

   public Integer getOperateur(){
      return operateur;
   }

   public void setOperateur(final Integer op){
      this.operateur = op;
   }

   public Groupement getGroupement(){
      return groupement;
   }

   public void setGroupement(final Groupement g){
      this.groupement = g;
   }

   /**
    * Renvoie l'enfant selon sa position
    * @param arg1 1: Noeud 1, 2: Opérateur, 3: Noeud 2
    * @return l'objet correspondant à la position
    */
   public ExportNode getChild(final int arg1){
      ExportNode retour = null;
      if(arg1 == 1){
         retour = node1;
      }else if(arg1 == 2){
         retour = node2;
      }
      return retour;
   }

   /**
    * @return Nombre d'enfant du noeud.
    */
   public int getChildCount(){
      int cpt = 0;
      if(node1 != null){
         cpt++;
      }
      if(node2 != null){
         cpt++;
      }
      return cpt;
   }

   /**
    * Méthode abstraite pour savoir si le noeud est une feuille ou non.
    * @return True si le noeud est une feuille.
    */
   @Override
   public boolean isLeaf(){
      return false;
   }

   /**
    * Méthode qui ajoute un Critere dans un arbre.
    * @param critere Critère à ajouter.
    * @param frere CritereNode qui sera le frère du Critère.
    * @param operateur Opérateur du GroupementNode créé.
    * @return true si le Critere a été correctement ajouté, false sinon.
    */
   public boolean addCritere(final Critere critere, final CritereNode frere, final Integer operateur){
      boolean retour = false;
      if(frere == null){
         insertToNode1(new CritereNode(critere, this));
         retour = true;
      }else{
         final GroupementNode nodeRef = getGroupementNode(frere);
         if(nodeRef.getNode1().equals(frere)){
            if(nodeRef.getNode2() == null){
               nodeRef.insertToNode2(new CritereNode(critere, this));
               nodeRef.setOperateur(operateur);
            }else{
               /** On crée un nouveau GroupementNode. */
               final GroupementNode newNode = new GroupementNode();
               /** On y ajoute le frère et la condition. */
               newNode.insertToNode1(frere);
               newNode.setOperateur(operateur);
               /** On y ajoute le Critère. */
               newNode.insertToNode2(new CritereNode(critere, newNode));
               /**
                * On ajoute le nouveau Groupement à la place du frère dans
                * le parent.
                */
               nodeRef.insertToNode1(newNode);
            }
            retour = true;
         }else if(nodeRef.getNode2().equals(frere)){
            /** On crée un nouveau GroupementNode. */
            final GroupementNode newNode = new GroupementNode();
            /** On y ajoute le frère et la condition. */
            frere.setParent(newNode);
            newNode.insertToNode1(frere);
            newNode.setOperateur(operateur);
            /** On y ajoute le Critère. */
            newNode.insertToNode2(new CritereNode(critere, newNode));
            /** On ajoute le nouveau Groupement à la place du frère
             *  dans le parent. */
            nodeRef.insertToNode2(newNode);
            retour = true;
         }
      }

      return retour;
   }

   public void insertToNode1(final ExportNode e){
      setNode1(e);
      e.setParent(this);
   }

   public void insertToNode2(final ExportNode e){
      setNode2(e);
      e.setParent(this);
   }

   /**
    * Méthode qui retourne le GroupementNode qui contient le ExportNode
    * @param eNode Noeud dont on cherche le GroupementNode parent
    * @return GroupementNode qui contient l'ExportNode, null si aucun.
    */
   public GroupementNode getGroupementNode(final ExportNode eNode){
      GroupementNode gNode = null;
      if(getChild(1) != null && getChild(1).equals(eNode)){
         gNode = this;
      }else if(getChild(2) != null && getChild(2).equals(eNode)){
         gNode = this;
      }else{
         if(this.getChild(1) instanceof GroupementNode){
            gNode = ((GroupementNode) this.getChild(1)).getGroupementNode(eNode);
         }
         if(gNode == null && this.getChild(2) instanceof GroupementNode){
            gNode = ((GroupementNode) this.getChild(2)).getGroupementNode(eNode);
         }
      }
      return gNode;
   }

   public List<Critere> removeNode(final ExportNode node){
      final List<Critere> crits = new ArrayList<>();
      if(getNode1() != null && getNode1().equals(node)){
         /** Si on supprime le noeud 1 */
         if(getNode1() instanceof GroupementNode){
            crits.addAll(((GroupementNode) getNode1()).removeNode(((GroupementNode) getNode1()).getNode1()));
            crits.addAll(((GroupementNode) getNode1()).removeNode(((GroupementNode) getNode1()).getNode2()));
            ((GroupementNode) getNode1()).setOperateur(null);
         }else if(getNode1() instanceof CritereNode){
            getNode1().setParent(null);
            setNode1(null);
            crits.add(((CritereNode) node).getCritere());
         }
      }else if(getNode2() != null && getNode2().equals(node)){
         /** Si on supprime le noeud 2 */
         if(getNode2() instanceof GroupementNode){
            crits.addAll(((GroupementNode) getNode2()).removeNode(((GroupementNode) getNode2()).getNode1()));
            crits.addAll(((GroupementNode) getNode2()).removeNode(((GroupementNode) getNode2()).getNode2()));
            ((GroupementNode) getNode2()).setOperateur(null);
         }else if(getNode2() instanceof CritereNode){
            getNode2().setParent(null);
            setNode2(null);
            crits.add(((CritereNode) node).getCritere());
         }
      }
      return crits;
   }

   /**
    * Méthode qui retourne tous les CritèreNodes orphelins du GroupementNode.
    * @return Liste des CritèreNodes orphelins
    */
   public List<CritereNode> getLeaves(){
      final List<CritereNode> crNodes = new ArrayList<>();
      if(getNode1() != null){
         if(getNode1().isLeaf()){
            crNodes.add((CritereNode) getNode1());
         }else{
            crNodes.addAll(((GroupementNode) getNode1()).getLeaves());
         }
      }
      if(getNode2() != null){
         if(getNode2().isLeaf()){
            crNodes.add((CritereNode) getNode2());
         }else{
            crNodes.addAll(((GroupementNode) getNode2()).getLeaves());
         }
      }
      return crNodes;
   }

   @Override
   public boolean equals(final Object obj){
      boolean retour = false;
      if(obj instanceof GroupementNode){
         if(this.getNode1() != null){
            if(this.getNode1().equals(((GroupementNode) obj).getNode1())){
               if(this.getOperateur() != null){
                  if(this.getOperateur().equals(((GroupementNode) obj).getOperateur())){
                     if(this.getNode2() != null){
                        if(this.getNode2().equals(((GroupementNode) obj).getNode2())){
                           retour = true;
                        }
                     }else{
                        retour = ((GroupementNode) obj).getNode2() == null;
                     }
                  }
               }else{
                  retour = ((GroupementNode) obj).getOperateur() == null;
               }
            }
         }else{
            retour = ((GroupementNode) obj).getNode1() == null;
         }
      }
      return retour;
   }

   @Override
   public String toString(){
      String retour = null;
      if(getOperateur() != null){
         if(getOperateur() == 1){
            retour = Labels.getLabel("general.selectlist.and");
         }else if(getOperateur() == 2){
            retour = Labels.getLabel("general.selectlist.or");
         }
      }
      return retour;
   }

   @Override
   public String getWidth(){
      return 30 * getParentCount() + "px";
   }

   /**
    * Action qui crée un arbre de groupement en base de données depuis un
    * GroupementNode.
    * @return
    */
   public Groupement convertToGroupement(){
      groupement = new Groupement();
      if(getOperateur() != null){
         if(getOperateur() == 1){
            groupement.setOperateur("and");
         }else if(getOperateur() == 2){
            groupement.setOperateur("or");
         }
      }
      if(getNode1() != null){
         if(getNode1() instanceof CritereNode){
            if(((CritereNode) getNode1()).getCritere().getCritereId() != null){
               groupement
                  .setCritere1(ManagerLocator.getCritereManager().copyCritereManager(((CritereNode) getNode1()).getCritere()));
            }else{
               groupement.setCritere1(((CritereNode) getNode1()).getCritere());
            }
         }
      }
      if(getNode2() != null){
         if(getNode2() instanceof CritereNode){
            if(((CritereNode) getNode2()).getCritere().getCritereId() != null){
               groupement
                  .setCritere2(ManagerLocator.getCritereManager().copyCritereManager(((CritereNode) getNode2()).getCritere()));
            }else{
               groupement.setCritere2(((CritereNode) getNode2()).getCritere());
            }
         }
      }
      ManagerLocator.getGroupementManager().createObjectManager(groupement, groupement.getCritere1(), groupement.getCritere2(),
         groupement.getOperateur(), groupement.getParent());
      if(getNode1() != null){
         if(getNode1() instanceof GroupementNode){
            final Groupement enfant = ((GroupementNode) getNode1()).convertToGroupement();
            enfant.setParent(groupement);
            ManagerLocator.getGroupementManager().updateObjectManager(enfant, enfant.getCritere1(), enfant.getCritere2(),
               enfant.getOperateur(), enfant.getParent());
         }
      }
      if(getNode2() != null){
         if(getNode2() instanceof GroupementNode){
            final Groupement enfant = ((GroupementNode) getNode2()).convertToGroupement();
            enfant.setParent(groupement);
            ManagerLocator.getGroupementManager().updateObjectManager(enfant, enfant.getCritere1(), enfant.getCritere2(),
               enfant.getOperateur(), enfant.getParent());
         }
      }
      return groupement;
   }

   public static GroupementNode convertFromGroupement(final Groupement racine, final GroupementNode parent){
      final GroupementNode n = new GroupementNode();
      final Groupement groupement = ManagerLocator.getGroupementManager().findByIdManager(racine.getGroupementId());
      n.setGroupement(groupement);
      n.setParent(parent);
      //On récupère sa liste des enfants
      final List<Groupement> enfants = ManagerLocator.getGroupementManager().findEnfantsManager(groupement);
      if(groupement.getCritere1() != null){
         n.setNode1(new CritereNode(groupement.getCritere1(), n));
      }else{
         if(enfants.size() >= 1){
            n.setNode1(convertFromGroupement(enfants.get(0), n));
         }
      }
      if(groupement.getOperateur() != null){
         if(groupement.getOperateur().equals("and")){
            n.setOperateur(1);
         }else if(groupement.getOperateur().equals("or")){
            n.setOperateur(2);
         }

         if(groupement.getCritere2() != null){
            n.setNode2(new CritereNode(groupement.getCritere2(), n));
         }else{
            if(enfants.size() >= 2){
               n.setNode2(convertFromGroupement(enfants.get(1), n));
            }else if(enfants.size() >= 1){
               n.setNode2(convertFromGroupement(enfants.get(0), n));
            }
         }
      }

      return n;
   }

   @Override
   public String getSclass(){
      if(getOperateur() != null){
         return "formValueItalics";
      }else{
         return null;
      }
   }
}
