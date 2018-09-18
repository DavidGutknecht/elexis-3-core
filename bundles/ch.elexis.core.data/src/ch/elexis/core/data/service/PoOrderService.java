package ch.elexis.core.data.service;

import java.util.List;

import ch.elexis.core.data.interfaces.IOrder;
import ch.elexis.core.data.interfaces.IOrderEntry;
import ch.elexis.core.data.interfaces.IStockEntry;
import ch.elexis.core.data.services.IOrderService;
import ch.elexis.data.Artikel;
import ch.elexis.data.BestellungEntry;
import ch.elexis.data.Query;
import ch.elexis.data.StockEntry;

/**
 * 
 * @deprecated use OSGi component implementation of {@link ch.elexis.core.services.IOrderService}
 *             instead.
 *
 */
public class PoOrderService implements IOrderService {
	
	public IOrderEntry findOpenOrderEntryForStockEntry(IStockEntry ise){
		StockEntry se = (StockEntry) ise;
		Artikel article = se.getArticle();
		Query<BestellungEntry> qre = new Query<BestellungEntry>(BestellungEntry.class);
		qre.add(BestellungEntry.FLD_STOCK, Query.EQUALS, se.getStock().getId());
		qre.add(BestellungEntry.FLD_ARTICLE_TYPE, Query.EQUALS, article.getClass().getName());
		qre.add(BestellungEntry.FLD_ARTICLE_ID, Query.EQUALS, article.getId());
		qre.add(BestellungEntry.FLD_STATE, Query.NOT_EQUAL,
			Integer.toString(BestellungEntry.STATE_DONE));
		List<BestellungEntry> execute = qre.execute();
		if (!execute.isEmpty()) {
			return execute.get(0);
		}
		return null;
	}

	@Override
	public IOrderEntry addRefillForStockEntryToOrder(IStockEntry ise, IOrder order){
		int current = ise.getCurrentStock();
		int max = ise.getMaximumStock();
		if (max == 0) {
			max = ise.getMinimumStock();
		}
		int toOrder = max - current;
		if (toOrder > 0) {
			return order.addEntry(ise.getArticle(), ise.getStock(), ise.getProvider(), toOrder);
		}
		return null;
	}
	
}