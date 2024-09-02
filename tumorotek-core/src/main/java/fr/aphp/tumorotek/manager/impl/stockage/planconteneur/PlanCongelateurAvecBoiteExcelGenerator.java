package fr.aphp.tumorotek.manager.impl.stockage.planconteneur;


import fr.aphp.tumorotek.manager.impl.io.production.DocumentWithDataAsArrayExcelProducer;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;

public class PlanCongelateurAvecBoiteExcelGenerator extends AbstractPlanCongelateurSansBoiteGenerator {

    private DocumentWithDataAsArrayExcelProducer documentWithDataAsArrayExcelProducer;


    @Override
    protected String buildNomPlan() {
        return "";
    }

    @Override
    protected DocumentProducer getDocumentProducer() {
        return null;
    }


}