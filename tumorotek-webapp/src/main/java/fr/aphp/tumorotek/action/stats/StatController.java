package fr.aphp.tumorotek.action.stats;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.East;
import org.zkoss.zul.Include;

public class StatController {

	@Wire("#east")
	East east;
	@Wire("#inner")
	Include inner;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void onLoadPage(@BindingParam("target") String path) {
		inner.setSrc(path);
		// east.setOpen(true);
		// east.setCollapsible(true);
	}

}
