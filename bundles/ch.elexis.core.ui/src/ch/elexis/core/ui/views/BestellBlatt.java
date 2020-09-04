/*******************************************************************************
 * Copyright (c) 2006-2009, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.core.ui.views;

import static ch.elexis.core.ui.text.TextTemplateRequirement.TT_ORDER;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.util.LocalLock;
import ch.elexis.core.services.IConflictHandler;
import ch.elexis.core.services.ILocalDocumentService;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.services.LocalDocumentServiceHolder;
import ch.elexis.core.ui.text.ITextPlugin;
import ch.elexis.core.ui.text.ITextPlugin.ICallback;
import ch.elexis.core.ui.text.TextContainer;
import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.BestellungEntry;
import ch.elexis.data.Brief;
import ch.elexis.data.Kontakt;
import ch.rgw.tools.Money;
import ch.rgw.tools.StringTool;

public class BestellBlatt extends ViewPart implements ICallback {
	public final static String ID = "ch.elexis.BestellBlatt"; //$NON-NLS-1$
	TextContainer text;
	Brief actBest;
	private static final String ERRMSG_CAPTION = Messages.BestellBlatt_CouldNotCreateOrder; //$NON-NLS-1$
	private static final String ERRMSG_BODY = Messages.BestellBlatt_CouldNotCreateOrderBody; //$NON-NLS-1$
	
	@Override
	public void createPartControl(final Composite parent){
		setTitleImage(Images.IMG_PRINTER.getImage());
		text = new TextContainer(getViewSite());
		text.getPlugin().createContainer(parent, this);
	}
	
	public void createOrder(final Kontakt adressat, final List<BestellungEntry> bestellungEntries){
		String[][] tbl = new String[bestellungEntries.size() + 2][];
		int i = 1;
		Money sum = new Money();
		tbl[0] = new String[] {
			Messages.BestellBlatt_Number, Messages.BestellBlatt_Pharmacode,
			Messages.BestellBlatt_Name, Messages.BestellBlatt_UnitPrice,
			Messages.BestellBlatt_LinePrice
		};
		for (BestellungEntry be : bestellungEntries) {
			String[] row = new String[5];
			row[0] = Integer.toString(be.getCount());
			row[1] = be.getArticle().getPharmaCode();
			row[2] = be.getArticle().getName();
			row[3] = be.getArticle().getEKPreis().getAmountAsString();
			Money amount = be.getArticle().getEKPreis().multiply(be.getCount());
			row[4] = amount.getAmountAsString();
			sum.addMoney(amount);
			tbl[i++] = row;
		}
		tbl[i] = new String[] {
			Messages.BestellBlatt_Sum, StringTool.leer, StringTool.leer, StringTool.leer,
			sum.getAmountAsString()
				//$NON-NLS-1$
		};
		actBest = text.createFromTemplateName(null, TT_ORDER, Brief.BESTELLUNG, adressat, null);
		if (actBest == null) {
			SWTHelper.showError(ERRMSG_CAPTION, ERRMSG_BODY + "'" + TT_ORDER + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			this.getSite().getPage().hideView(this);
		} else {
			actBest.setPatient(CoreHub.actUser);
			text.getPlugin().insertTable("[" + TT_ORDER + "]", //$NON-NLS-1$ //$NON-NLS-2$
				ITextPlugin.FIRST_ROW_IS_HEADER | ITextPlugin.GRID_VISIBLE, tbl, null);
			if (text.getPlugin().isDirectOutput()) {
				text.getPlugin().print(null, null, true);
				getSite().getPage().hideView(this);
				return;
			}
			save();
			openLocalDocument(this, actBest);
		}
	}

	/**
	 * Open the {@link Brief} as local document. Changes to the local document are
	 * not saved.
	 * 
	 * @param view
	 * @param brief
	 */
	private void openLocalDocument(BestellBlatt view, Brief brief) {
		ILocalDocumentService service = LocalDocumentServiceHolder.getService().orElse(null);
		if (service != null) {
			Optional<File> file = service.add(brief, new IConflictHandler() {
				@Override
				public Result getResult() {
					return Result.OVERWRITE;
				}
			});
			if (file.isPresent()) {
				Program.launch(file.get().getAbsolutePath());
			} else {
				MessageDialog.openError(getSite().getShell(),
						ch.elexis.core.ui.commands.Messages.StartEditLocalDocumentHandler_errortitle,
						ch.elexis.core.ui.commands.Messages.StartEditLocalDocumentHandler_errormessage);
			}
			if (service.contains(brief)) {
				Optional<LocalLock> lock = LocalLock.getManagedLock(brief);
				lock.ifPresent(localDocumentLock -> localDocumentLock.unlock());

				service.remove(brief, false);
			}
			view.getSite().getPage().hideView(view);
		}
	}
	
	@Override
	public void setFocus(){
		// TODO Automatisch erstellter Methoden-Stub
		
	}
	
	public void save(){
		if (actBest != null) {
			actBest.save(text.getPlugin().storeToByteArray(), text.getPlugin().getMimeType());
		}
	}
	
	public boolean saveAs(){
		// TODO Automatisch erstellter Methoden-Stub
		return false;
	}
}
