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
package fr.aphp.tumorotek.manager.interfacage;

import java.util.List;

import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Interface pour le manager qui va injecter les données issues d'un
 * dossier externe dans des objets TK.
 * Interface créée le 11/10/2011.
 * version 2.1: DossierExterne et blocExterne peuvent avoir ids = null si
 * obtenus depuis une requete sur une vue
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface InjectionManager
{

   /**
    * Extrait l'objet d'un thésaurus qui correspond à la valeur
    * passée en paramètre.
    * @param champEntite Champ thésaurus.
    * @return Objet thésaurus.
    */
   Object extractValueForOneThesaurus(ChampEntite champEntite, Banque banque, Object valeur);

   /**
    * Extrait l'item d'un champ annotation correspondant à la
    * valeur passée en paramètre.
    * @param champAnnotation Champ thésaurus.
    * @return Un Item.
    */
   Object extractValueForOneAnnotationThesaurus(ChampAnnotation champAnnotation, Banque banque, String valeur);

   /**
    * Set la valeur passée en paramètre à l'objet.
    * @param value Valeur à fixer.
    * @param attibut Attribut à remplir.
    * @param obj Objet.
    */
   void setPropertyValueForObject(Object value, ChampEntite attribut, Object obj);

   /**
    * Set la valeur passée en paramètre à l'AnnotationValeur.
    * @param value Valeur à fixer.
    * @param annotation ChampAnnotation à remplir.
    */
   AnnotationValeur setPropertyValueForAnnotationValeur(Object value, ChampAnnotation annotation, Banque banque);

   /**
    * Injecte le contenu d'une ValeurExterne dans l'objet.
    * @param obj Objet à remplir.
    * @param banque Banque de l'objet.
    * @param valeurExterne Valeur à injecter.
    */
   void injectValeurExterneInObject(Object obj, Banque banque, ValeurExterne valeurExterne, List<AnnotationValeur> annoValeurs);

   /**
    * Injecte le contenu d'une ValeurExterne dans l'objet.
    * @param obj Objet à remplir.
    * @param banque Banque de l'objet.
    * @param blocExterne Bloc à injecter.
    */
   void injectBlocExterneInObject(Object obj, Banque banque, BlocExterne blocExterne, List<AnnotationValeur> annoValeurs);

   /**
    * Injecte le contenu d'un dossier.
    * @param dossier Dossier à injecter.
    * @param banque Banque.
    */
   ResultatInjection injectDossierManager(DossierExterne dossier, Banque banque);
}
