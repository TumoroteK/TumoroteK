/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.utils;

/**
 * Classe utilitaire pour la gestion des non-conformités dans le projet.
 * Cette classe fournit des méthodes statiques pour vérifier et récupérer des informations
 * sur les raisons de non-conformité à partir des noms de champs d'entités.
 */
public final class NonConformiteUtils {
   private static final String CONFORME = "Conforme";
   private static final String POINT_RAISON = ".Raison";


   /**
    * Vérifie si le nom du champ d'entité correspond à une raison de non-conformité.
    * Un champ est considéré comme une raison de non-conformité s'il commence par "Conforme"
    * et se termine par ".Raison".
    *
    * @param champEntiteNom le nom du champ d'entité à vérifier
    * @return true si le champ est une raison de non-conformité, false sinon
    * @throws NullPointerException si champEntiteNom est null
    */
   public static boolean isUneRaisonDeNonConformite(String champEntiteNom) {
      return champEntiteNom.startsWith(CONFORME) && champEntiteNom.endsWith(POINT_RAISON);
   }

   /**
    * Récupère le nom de la non-conformité si le champ d'entité est une raison de non-conformité.
    * Si ce n'est pas le cas, renvoie null.
    *
    * @param champEntiteNom le nom du champ d'entité à vérifier
    * @return le nom de la non-conformité ou null si ce n'est pas une raison de non-conformité
    * @throws NullPointerException si champEntiteNom est null
    */
   public static String retrieveNomDeLaNonConformiteOrNull(String champEntiteNom) {
      if (isUneRaisonDeNonConformite(champEntiteNom)) {
         return champEntiteNom.substring(CONFORME.length(), champEntiteNom.length() - POINT_RAISON.length());
      }
      return null;
   }
}
