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
package fr.aphp.tumorotek.model.qualite;

public enum EOperationTypeId
{
   CONSULTATION(1),
   EXPORT(2),
   CREATION(3),
   IMPORT(4),
   MODIFICATION(5),
   MODIF_MULTIPLE(6),
   ARCHIVAGE(7),
   RESTAURATION(8),
   VALIDATION(9),
   ANNOTATION(10),
   EXPORT_ANONYME(11),
   STOCKAGE(12),
   DESTOCKAGE(13),
   DEPLACEMENT(14),
   SUPPRESSION(15),
   LOGIN(16),
   LOGOUT(17),
   CHANGE_COLLECTION(18),
   SYNCHRONISATION(19),
   FUSION(20),
   EXPORT_TVGSO(21),
   EXPORT_INCA(22),
   EXPORT_BIOCAP(23),
   PARTAGE(24),
   SUPPRESSION_PARTAGE(25);
   
   private EOperationTypeId(Integer id) {
      this.id = id;
   }
   
   private Integer id;
   
   public Integer getId(){
      return id;
   }
}
