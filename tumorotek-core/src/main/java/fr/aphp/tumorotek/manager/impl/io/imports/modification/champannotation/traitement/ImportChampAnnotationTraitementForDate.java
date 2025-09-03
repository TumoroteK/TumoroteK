/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.traitement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.EAnnotationValeurInfoColonne;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.reader.ImportChampAnnotationReaderFactory;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 * traitement d'import qui correspond à un champ d'annotation dont la valeur est stockée dans le champ ANNO_DATE de la table ANNOTATION_VALEUR
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportChampAnnotationTraitementForDate extends AbstractImportChampAnnotationTraitement
{

   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationTraitementForDate.class);
   
   private static final EAnnotationValeurInfoColonne ANNOTATION_VALEUR__INFO_COLONNE = EAnnotationValeurInfoColonne.INFO_ANNO_DATE;

   public ImportChampAnnotationTraitementForDate(Integer champAnnotationId, int indexColonne, ImportColonne importColonne) {
      setChampAnnotationId(champAnnotationId);
      setIndexColonneInFile(indexColonne);
      setImportColonne(importColonne);
      setImportChampAnnotationReader(ImportChampAnnotationReaderFactory.getDateReader());
   }
   
//   public ImportChampAnnotationTraitementForDate(Integer champAnnotationId, int indexColonne, String nomColonne) {
//      setChampAnnotationId(champAnnotationId);
//      setIndexColonneInFile(indexColonne);
//      setNomColonneInFile(nomColonne);
//      setImportChampAnnotationReader(ImportChampAnnotationReaderFactory.getDateReader());
//   }
   
   @Override
   public EAnnotationValeurInfoColonne getAnnotationValeurInfoColonne() {
      return ANNOTATION_VALEUR__INFO_COLONNE;
   }

   @Override
   public String convertValeurAsString(Object valeur) {
      Date valeurAsDate = (Date)valeur;
      return new SimpleDateFormat(defineDateFormat(), Locale.FRANCE).format(valeurAsDate);//TODO idéalement à internationaliser mais la locale n'est pas passé au back
   }
   
   public String defineDateFormat( ) {
      return "dd/MM/yyyy HH:mm";
   }
}
