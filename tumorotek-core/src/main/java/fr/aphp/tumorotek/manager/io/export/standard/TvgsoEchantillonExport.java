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
package fr.aphp.tumorotek.manager.io.export.standard;

import java.sql.Connection;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * Classe regroupant les methodes récupérant les items
 * du bloc INFORMATIONS ECHANTILLON spécifié par l'export TVGSO.
 * Date: 01/08/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface TvgsoEchantillonExport extends IncaEchantillonExport
{

   /**
    * Type Echantillon TUMORAL ou non. OBLIGATOIRE.
    * Obtenu directement dans le champ Tumoral ou 
    * à partir du type echantillon selon specs tvgso 
    * (T ou N dans type echantillon)
    * @param echantillon
    * @param tvgso
    * @return O ou N
    */
   @Override
   String getIsTumoral(Echantillon echantillon);

   /**
    * Mode de conservation. Encoder selon specifications.
    * @param echantillon
    * @return 1, 2, 3, 4 ou ""
    */
   @Override
   String getModeConservation(Echantillon echantillon);

   /**
    * Mode de preparation. Annotation si tvgso ou MODE_PREPA sinon.
    * @param con
    * @param echantillon
    * @return un chiffre 1, 2, 9 ou ""
    */
   String getModePreparation(Connection con, Echantillon echantillon);

   /**
    * Contrôles sur tissus. ECHAN_QUALITE ou annotation.
    * Non obligatoire.
    * @param con
    * @param echantillon
    * @return 1, 2, 3, 4, 5 , 9 ou ""
    */
   @Override
   String getControles(Connection con, Echantillon echantillon);

   /**
    * Pourcentage de cellules tumorales. 
    * @param con
    * @param echantillon
    * @return 0-100 ou "".
    */
   @Override
   String getPourcentageCellulesTumorales(Connection con, Echantillon echantillon);

   /**
    * Ressources biologiques associées à l'échantillon.
    * @see EchantillonManager.itemINCa50to53manager
    * @param echantillon
    * @param resType
    * @return 1 ou N
    */
   @Override
   String getRessourceBiolAssociee(Echantillon echantillon, String resType);

   /**
    * ADN constitutionnel. 
    * N'Applique pas le Regexp sur le consentement associé au prélèvement.
    * @param con
    * @param echantillon
    * @return O, N ou ""
    */
   String getADNconstitutionnel(Connection con, Echantillon echantillon);

}
