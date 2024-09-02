package fr.aphp.tumorotek.manager.io.production;


import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsArray;

import java.util.List;

public abstract class DocumentProducer {

    public abstract OutputStreamData produce(List<DocumentWithDataAsArray> list);
}
