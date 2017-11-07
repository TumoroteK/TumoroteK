package fr.aphp.tumorotek.interfacage.jaxb.inclusion;


import org.apache.log4j.Logger;

import org.springframework.ws.client.core.WebServiceTemplate;


public class InclusionServiceClientImpl implements InclusionServiceClient {

    private Logger log = Logger
    					.getLogger(InclusionServiceClient.class);
    private static final ObjectFactory 
    					WS_CLIENT_FACTORY = new ObjectFactory();
           
    private  WebServiceTemplate webServiceTemplate;
           
    public void setWebServiceTemplate(WebServiceTemplate wT) {
		this.webServiceTemplate = wT;
	}
    
    private  WebServiceTemplate webServiceTemplateTK;
    
    public void setWebServiceTemplateTK(WebServiceTemplate wT) {
		this.webServiceTemplateTK = wT;
	}

	@Override
    public boolean addSubject(StudySubjectType subject) {
    	log.debug("Preparing CreateRequest.....");
        
        CreateRequest request =   WS_CLIENT_FACTORY.createCreateRequest();
        request.getStudySubject().add(subject);

        log.debug("Invoking Web service Operation[CancelOrder]....");
        CreateResponse response = (CreateResponse) webServiceTemplate
        								.marshalSendAndReceive(request);
           
        log.debug("Has the subject added " + response.getLabel());
        log.debug("Has the subject added " + response.getResult());
           
        return response.getError().isEmpty();
    }
	
	@Override
    public boolean addSubjectTK(StudySubjectType subject) {
    	log.debug("Preparing CreateRequest.....");
        
        CreateRequest request =   WS_CLIENT_FACTORY.createCreateRequest();
        request.getStudySubject().add(subject);

        log.debug("Invoking Web service Operation[CancelOrder]....");
        CreateResponse response = (CreateResponse) webServiceTemplateTK
        								.marshalSendAndReceive(request);
           
        log.debug("Has the subject added " + response.getLabel());
        log.debug("Has the subject added " + response.getResult());
           
        return response.getError().isEmpty();
    }
}    
