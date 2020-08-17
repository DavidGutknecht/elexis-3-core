package ch.elexis.core.services;

import java.util.List;

import ch.elexis.core.text.ITextPlaceholderResolver;

/**
 * Perform replacement of text templates according to
 * <a href="https://medelexis.ch/wp-content/uploads/Tabelle_Platzhalter.pdf">Elexis Platzhalter</a>.
 * Cannot use IDataAccess as this is available in ch.elexis.core.data only.
 */
public interface ITextReplacementService {
	
	/**
	 * Get all currently available {@link ITextPlaceholderResolver} implemenations.
	 * 
	 * @return
	 */
	public List<ITextPlaceholderResolver> getResolvers();
	
	/**
	 * @param context
	 *            to apply during replacement
	 * @param template
	 *            the source text
	 * @return replaced result
	 */
	public String performReplacement(IContext context, String template);
	
}
