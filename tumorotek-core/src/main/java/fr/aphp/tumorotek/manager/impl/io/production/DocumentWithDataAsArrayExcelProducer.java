package fr.aphp.tumorotek.manager.impl.io.production;

import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.manager.io.production.OutputStreamData;
import production.document.DocumentWithDataAsArray;

import java.util.List;

public class DocumentWithDataAsArrayExcelProducer implements DocumentProducer {
    @Override
    public OutputStreamData produce(List<DocumentWithDataAsArray> list) {
        return null;
    }
}
