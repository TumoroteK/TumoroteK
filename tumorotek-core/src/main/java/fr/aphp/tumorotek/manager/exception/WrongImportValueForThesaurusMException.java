package fr.aphp.tumorotek.manager.exception;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 * exception lancée quand la valeur renseignée pour un champ de type thesaurusM contient au moins une valeur
 * ne correspondant pas aux valeurs définies pour ce thesaurus
 * A noter que les valeurs sont acceptées quelque soit la casse et qu'elles sont séparées par un point-virgule.
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class WrongImportValueForThesaurusMException extends WrongImportValueForThesaurusException
{

   private static final long serialVersionUID = 1L;
   
   private List<String> listValeurNonAutorisee;
   
   public WrongImportValueForThesaurusMException(final ImportColonne col, final List<String> listValeurNonAutorisee, final Set<String> listValeurAutorisee){
      super(col, listValeurAutorisee);
      this.listValeurNonAutorisee = listValeurNonAutorisee;
   }
   
   @Override
   protected String getI18nKey() {
      return "validation.wrong.import.thesaurusM";
   }
   
   @Override
   public String getMessage(){
      return getColonne().getNom() + ": Erreur de validation, valeurs de thésaurus inconnues.";
   }
   
   @Override
   public UIMessage buildUIMessage(){
      return buildUIMessage(new String[] {   getColonne().getNom(), 
                                              listValeurNonAutorisee.stream().collect(Collectors.joining(", ")),
                                              getListValeurAutorisee().stream().collect(Collectors.joining(", "))});
   }

}
