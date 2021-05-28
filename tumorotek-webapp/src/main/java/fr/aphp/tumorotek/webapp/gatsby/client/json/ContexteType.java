/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsby.client.json;

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
	     if (!this.name().equals(val))
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
    
    public String getType() {
        return type;
    }

    public Integer getEntiteId() {
        return entiteId;
    }
}