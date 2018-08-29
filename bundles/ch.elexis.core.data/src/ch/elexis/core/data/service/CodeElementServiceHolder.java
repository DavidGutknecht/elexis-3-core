package ch.elexis.core.data.service;

import java.util.HashMap;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ch.elexis.core.services.ICodeElementService;
import ch.elexis.core.services.ICodeElementService.ContextKeys;
import ch.elexis.data.Fall;
import ch.elexis.data.Konsultation;

@Component(service = {})
public class CodeElementServiceHolder {
	
	private static HashMap<Object, Object> emptyMap = new HashMap<>();
	
	private static ICodeElementService elementService;
	
	@Reference(unbind = "-")
	public void setCodeElementService(ICodeElementService elementService){
		CodeElementServiceHolder.elementService = elementService;
	}
	
	public static ICodeElementService get(){
		return elementService;
	}
	
	public static HashMap<Object, Object> createContext(){
		HashMap<Object, Object> ret = new HashMap<>();
		Optional<Konsultation> consultation =
			ContextServiceHolder.get().getRootContext().getTyped(Konsultation.class);
		if (consultation.isPresent()) {
			ret.put(ContextKeys.CONSULTATION, consultation.get());
		}
		Optional<Fall> coverage = ContextServiceHolder.get().getRootContext().getTyped(Fall.class);
		if (coverage.isPresent()) {
			ret.put(ContextKeys.COVERAGE, coverage.get());
		}
		return ret;
	}
	
	public static HashMap<Object, Object> emtpyContext(){
		return emptyMap;
	}
}
