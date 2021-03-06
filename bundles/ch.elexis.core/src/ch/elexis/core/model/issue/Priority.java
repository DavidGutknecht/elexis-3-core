package ch.elexis.core.model.issue;

import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import ch.elexis.core.interfaces.ILocalizedEnum;
import ch.elexis.core.interfaces.INumericEnum;

public enum Priority implements INumericEnum, ILocalizedEnum {
	LOW(0), MEDIUM(1), HIGH(2);

	private final int numeric;

	private Priority(int numeric) {
		this.numeric = numeric;
	}

	public int numericValue() {
		return numeric;
	}

	@Override
	public String getLocaleText() {
		try {
			return ResourceBundle.getBundle(ch.elexis.core.l10n.Messages.BUNDLE_NAME)
					.getString(Priority.class.getSimpleName() + "_" + this.name());
		} catch (Exception e) {
			return this.name();
		}
	}

	public static Priority byNumericSafe(String priority) {
		if (StringUtils.isNotBlank(priority)) {
			try {
				int numeric = Integer.parseInt(priority);
				for (Priority prio : Priority.values()) {
					if (prio.numericValue() == numeric) {
						return prio;
					}
				}
			} catch (NumberFormatException e) {
				// ignore and return default
			}
		}
		return Priority.MEDIUM;
	}
}
