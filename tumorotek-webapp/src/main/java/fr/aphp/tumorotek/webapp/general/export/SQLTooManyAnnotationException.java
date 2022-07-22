package fr.aphp.tumorotek.webapp.general.export;

import java.sql.SQLException;

import org.zkoss.util.resource.Labels;

//TK-320
/**
 * Permet de gérer un message d'erreur fonctionnel spécifique quand 
 * lors des exports il y a trop d'annotations ce qui fait trop de colonnes 
 * ajoutées dans une table temporaire (procédure fill_tmp_table_annotation)
 *
 * @author chuet
 */
public class SQLTooManyAnnotationException extends RuntimeException
{

    private static final long serialVersionUID = 1L;
    
    public SQLTooManyAnnotationException(SQLException sqlException){
       super(sqlException);
    }

    public String getMessage() {
       return Labels.getLabel("export.annotations.oversize.error");
    }
}
