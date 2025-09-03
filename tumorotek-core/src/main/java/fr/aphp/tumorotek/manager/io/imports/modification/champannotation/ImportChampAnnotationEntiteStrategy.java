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
package fr.aphp.tumorotek.manager.io.imports.modification.champannotation;

import java.util.List;

import fr.aphp.tumorotek.model.CodeIdPair;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * cette interface définit les méthodes implémentées dans les strategy, classe dédiées aux règles différentes selon les entités 
 * dans le cadre de l'import pour modifier des annotations. 
 * Ces spécificités concernent la création du modèle et les contrôles en amont du traitement de mise à jour des annotations, qui lui ne dépend pas de l'entité concerné
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public interface ImportChampAnnotationEntiteStrategy
{
   /**
    * méthode qui récupère le champ correspondant à la clé fonctionnelle de l'objet à modifier
    * @param entite
    * @return ChampEntite
    */
   ChampEntite retrieveChampForCleFonctionnelle(Entite entite);
   
   /**
    * retourne l'éventuel champ utilisé pour contrôler la valeur de la clé fonctionnelle. Utilisé à date uniquement pour le Patient
    * @param entite
    * @return
    */
   ChampEntite retrieveChampForContole(Entite entite);
   
   /**
    * va chercher en base de données les identifiants des objets à mettre à jour ainsi que la valeur de contrôle si nécessaire
    * @param listCodeForSelect : liste des codes des objets à modifier
    * @param banque : banque de rattachement des objets : permet de faire un contrôle sur le code et de ne pas autoriser une modification sur une collection n'appartenant pas à l'utilisateur
    * @param nomChampForControle : nom du champ de la donnée de contrôle à récupérer. Sera null dans la majorité des cas sauf pour Patient
    * @return une liste de CodeIdPair, DTO qui contient le code (donnée en entrée), l'id associé en base et la valeur du champ de contrôle 
    */
   List<CodeIdPair> findIdAndDataForControleByCodesAndBanque(List<String> listCodeForSelect, Banque banque, String nomChampForControle);
}
