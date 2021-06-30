/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import org.codehaus.jackson.annotate.JsonCreator;

public enum ContexteType {
	
	PATIENT(1, Values.PATIENT), 
	PRELEVEMENT(2, Values.PRELEVEMENT),
	ECHANTILLON(3, Values.ECHANTILLON);
    
	private Integer entiteId;
	private String type;

	ContexteType(Integer _i, String _t) {
        this.entiteId = _i;
        this.type = _t;
    }

	private ContexteType (String val) {
	     if (!this.type.equals(val))
	        throw new IllegalArgumentException();
	  }

    public static class Values {
        public static final String PATIENT = "Patient";
        public static final String PRELEVEMENT = "Prelevement";
        public static final String ECHANTILLON = "Echantillon";
    }
    
    public static ContexteType getById(Integer _i) {
        for(ContexteType ref : values()) {
            if(ref.entiteId.equals(_i)) return ref;
        }
        return null;
    }
    
    public static ContexteType getByType(String _s) {
        for(ContexteType ref : values()) {
            if(ref.type.equals(_s)) return ref;
        }
        return null;
    }
    
    @JsonCreator // This is the factory method and must be static
    public static ContexteType fromString(String _s) {
        return getByType(_s);
    }
    
    public String getType() {
        return type;
    }

    public Integer getEntiteId() {
        return entiteId;
    }
}