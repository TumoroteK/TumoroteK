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
 * Classe utilitaire pour la gestion des non-conformités.
 * <p>
 * Cette classe a été créée pour répondre au besoin de vérifier si un champ d'entité
 * correspond à une raison de non-conformité, en prenant en compte deux cas distincts :
 * <ul>
 *     <li>Les noms de champ respectant le format <code>Conforme(.*)\.Raison</code>, où un point est explicitement attendu avant "Raison".</li>
 *     <li>Les noms de champ respectant le format <code>Conforme.*Raison</code>, sans contrainte spécifique sur le point.</li>
 * </ul>
 * Unification sous une seule expression <code>Conforme.*Raison</code> aurait pu entraîner des bugs silencieux,
 * en acceptant par erreur des valeurs non conformes ou en rejetant des valeurs valides.
 * C'est pourquoi la classe distingue ces deux cas en proposant des méthodes spécifiques
 * pour chaque situation.
 * </p>
 */
public final class NonConformiteUtils {
   private static final String PREFIX = "Conforme";
   private static final String SUFFIX_AVEC_POINT = ".Raison";

   private static final String SUFFIX_SANS_POINT = "Raison";

   // TODO: Il y a deux regex qui sont utilisées dans la verification de NonConformite:
   //  1. sans point "Conforme.*Raison"
   //  2. avec point "Conforme(.*)\\.Raison",
   // mais les méthodes proposées ne prennent en charge qu’un seul de ces cas (".Raison").
   // Étant donné que je ne peux pas modifier l’intention du code et utiliser une seule méthode pour les deux cas
   // (qui vérifierait soit le premier, soit le second), et qu’il n’y a pas d’intérêt à alourdir la méthode avec un booléen
   // (pour distinguer "avec point" ou "sans point"), j’ai préféré créer deux méthodes distinctes pour chaque cas.



   /**
    * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
    */
   private NonConformiteUtils() {
      throw new UnsupportedOperationException("Impossible d'instancier une classe utilitaire");
   }

   /**
    * Vérifie si le nom du champ d'entité correspond à une raison de non-conformité avec un point avant "Raison".
    *
    * @param champEntiteNom le nom du champ d'entité
    * @return vrai si le nom commence par "Conforme" et se termine par ".Raison"
    * @throws NullPointerException si champEntiteNom est null
    */
   public static boolean isUneRaisonDeNonConformiteAvecPoint (String champEntiteNom) {
      return champEntiteNom.startsWith(PREFIX) && champEntiteNom.endsWith(SUFFIX_AVEC_POINT);
   }

   /**
    * Extrait le nom de la non-conformité à partir du champ d'entité si celui-ci suit le format avec point.
    *
    * @param champEntiteNom le nom du champ d'entité
    * @return le nom de la non-conformité, ou null si ce n'est pas un champ valide
    * @throws NullPointerException si champEntiteNom est null
    */
   public static String retrieveNomDeLaNonConformiteAvecPointOrNull(String champEntiteNom) {
      if (isUneRaisonDeNonConformiteAvecPoint(champEntiteNom)) {
         return champEntiteNom.substring(
            PREFIX.length(),
            champEntiteNom.length() - SUFFIX_AVEC_POINT.length()
         );
      }
      return null;
   }
   /**
    * Vérifie si le nom du champ d'entité correspond à une raison de non-conformité sans point avant "Raison".
    *
    * @param champEntiteNom le nom du champ d'entité
    * @return vrai si le nom commence par "Conforme" et se termine par "Raison"
    * @throws NullPointerException si champEntiteNom est null
    */
   public static boolean isUneRaisonDeNonConformiteSansPoint  (String champEntiteNom) {
      return champEntiteNom.startsWith(PREFIX) && champEntiteNom.endsWith(SUFFIX_SANS_POINT);
   }

   /**
    * Extrait le nom de la non-conformité à partir du champ d'entité si celui-ci suit le format sans point.
    *
    * @param champEntiteNom le nom du champ d'entité
    * @return le nom de la non-conformité, ou null si ce n'est pas un champ valide
    * @throws NullPointerException si champEntiteNom est null
    */
   public static String retrieveNomDeLaNonConformiteSansPointOrNull(String champEntiteNom) {
      if (isUneRaisonDeNonConformiteSansPoint(champEntiteNom)) {
         return champEntiteNom.substring(
            PREFIX.length(),
            champEntiteNom.length() - SUFFIX_SANS_POINT.length()
         );
      }
      return null;
   }
}
