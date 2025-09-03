package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.traitement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.reader.ImportChampAnnotationReaderFactory;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 * traitement d'import qui correspond à un champ d'annotation dont la valeur est stockée dans le champ ALPHANUM de la table ANNOTATION_VALEUR
 * mais au format d'un lien hypertexte
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportChampAnnotationTraitementForLienHypertexteAsAlphanum extends ImportChampAnnotationTraitementForAlphanum
{
   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationTraitementForLienHypertexteAsAlphanum.class);
   
   public ImportChampAnnotationTraitementForLienHypertexteAsAlphanum(Integer champAnnotationId, int indexColonne, ImportColonne importColonne) {
      super(champAnnotationId, indexColonne, importColonne);
      //surcharge du reader du parent : 
      setImportChampAnnotationReader(ImportChampAnnotationReaderFactory.getLienHypertexteReader());
   }
}
