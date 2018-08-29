package ch.elexis.core.ui.eigenartikel;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ch.elexis.core.common.ElexisEventTopics;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.service.ContextServiceHolder;
import ch.elexis.core.data.service.CoreModelServiceHolder;
import ch.elexis.core.eigenartikel.EigenartikelUtil;
import ch.elexis.core.model.ITypedArticle;
import ch.elexis.core.model.eigenartikel.EigenartikelTyp;
import ch.elexis.core.types.ArticleTyp;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.locks.IUnlockable;
import ch.elexis.core.ui.util.SWTHelper;

public class EigenartikelProductComposite extends Composite implements IUnlockable {
	
	private WritableValue<ITypedArticle> productEigenartikel =
		new WritableValue<>(null, ITypedArticle.class);
	
	private Text txtProductName;
	private Text txtAtcCode;
	private ComboViewer comboViewerProductType;
	private Label lblAtcCode;
	private Button btnAddDrugPackage;
	private ScrolledComposite scrolledComposite;
	private Composite compositeArticleItems;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EigenartikelProductComposite(Composite parent, int style){
		super(parent, SWT.BORDER_SOLID);
		setLayout(new GridLayout(2, false));
		
		Label lblProductName = new Label(this, SWT.NONE);
		lblProductName.setAlignment(SWT.RIGHT);
		lblProductName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProductName.setText(Messages.EigenartikelDisplay_productName);
		
		txtProductName = new Text(this, SWT.BORDER);
		txtProductName.setTextLimit(127);
		txtProductName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProductType = new Label(this, SWT.NONE);
		lblProductType.setAlignment(SWT.RIGHT);
		lblProductType.setText(Messages.EigenartikelComposite_lblProductType_text);
		
		comboViewerProductType = new ComboViewer(this, SWT.NONE);
		Combo comboProductType = comboViewerProductType.getCombo();
		
		GridData gd_comboProductType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboProductType.widthHint = 150;
		comboProductType.setLayoutData(gd_comboProductType);
		comboViewerProductType.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerProductType.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				return ((EigenartikelTyp) element).getLocaleText();
			}
		});
		comboViewerProductType.setInput(EigenartikelTyp.values());
		
		lblAtcCode = new Label(this, SWT.NONE);
		lblAtcCode.setAlignment(SWT.RIGHT);
		lblAtcCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAtcCode.setText(Messages.EigenartikelDisplay_atcCode);
		
		Composite compAtcCode = new Composite(this, SWT.NONE);
		GridLayout gl_compAtcCode = new GridLayout(2, false);
		gl_compAtcCode.marginWidth = 0;
		gl_compAtcCode.marginHeight = 0;
		compAtcCode.setLayout(gl_compAtcCode);
		compAtcCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		txtAtcCode = new Text(compAtcCode, SWT.BORDER);
		GridData gd_txtAtcCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtAtcCode.widthHint = 80;
		txtAtcCode.setLayoutData(gd_txtAtcCode);
		txtAtcCode.setTextLimit(8);
		new Label(compAtcCode, SWT.NONE);
		
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		btnAddDrugPackage = new Button(this, SWT.FLAT);
		btnAddDrugPackage.setText(Messages.EigenartikelComposite_newArticle_text);
		btnAddDrugPackage.setImage(Images.IMG_NEW.getImage());
		btnAddDrugPackage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		scrolledComposite = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		scrolledComposite.setLayoutData(SWTHelper.getFillGridData(2, true, 1, true));
		
		compositeArticleItems = new Composite(scrolledComposite, SWT.BORDER_DASH);
		
		GridLayout gl_compositeArticleItems = new GridLayout(1, false);
		gl_compositeArticleItems.marginWidth = 0;
		gl_compositeArticleItems.marginHeight = 0;
		compositeArticleItems.setLayout(gl_compositeArticleItems);
		compositeArticleItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		scrolledComposite.setContent(compositeArticleItems);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		comboProductType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				EigenartikelUtil.copyProductAttributesToArticleSetAsChild(getProductArtikel(),
					null);
				ContextServiceHolder.get().postEvent(ElexisEventTopics.EVENT_UPDATE,
					getProductArtikel());
			}
		});
		
		btnAddDrugPackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				ITypedArticle product = getProductArtikel();
				if (product != null) {
					ITypedArticle articleNew =
						CoreModelServiceHolder.get().create(ITypedArticle.class);
					articleNew.setTyp(ArticleTyp.EIGENARTIKEL);
					articleNew.setName(product.getName());
					EigenartikelUtil.copyProductAttributesToArticleSetAsChild(product, articleNew);
					createEigenartikelComposite(articleNew);
					scrolledComposite.setVisible(true);
					scrolledComposite
						.setMinSize(compositeArticleItems.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					scrolledComposite.layout(true, true);
					ContextServiceHolder.get().postEvent(ElexisEventTopics.EVENT_RELOAD,
						ITypedArticle.class);
				}
			}
		});
		
		setUnlocked(false);
		
		initDataBindings();
	}
	
	@Override
	protected void checkSubclass(){
		// Disable the check that prevents subclassing of SWT components
		
	}
	
	@Override
	public void setUnlocked(boolean unlocked){
		txtProductName.setEditable(unlocked);
		txtAtcCode.setEditable(unlocked);
		comboViewerProductType.getCombo().setEnabled(unlocked);
		btnAddDrugPackage.setEnabled(unlocked);
		for (Control c : compositeArticleItems.getChildren()) {
			IUnlockable ul = (IUnlockable) c;
			ul.setUnlocked(unlocked);
		}
	}
	
	public ITypedArticle getProductArtikel(){
		return (ITypedArticle) productEigenartikel.getValue();
	}
	
	public void setProductEigenartikel(ITypedArticle productEigenartikel){
		this.productEigenartikel.setValue(productEigenartikel);
		
		for (Control c : compositeArticleItems.getChildren()) {
			c.dispose();
		}
		if (productEigenartikel != null && productEigenartikel.isProduct()) {
			btnAddDrugPackage.setVisible(true);
			scrolledComposite.setVisible(true);
			@SuppressWarnings("unchecked")
			List<ITypedArticle> packages =
				(List<ITypedArticle>) (List<?>) productEigenartikel.getPackages();
			for (ITypedArticle eigenartikel : packages) {
				createEigenartikelComposite(eigenartikel);
			}
		} else {
			scrolledComposite.setVisible(false);
			btnAddDrugPackage.setVisible(false);
		}
		scrolledComposite.setMinSize(compositeArticleItems.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.layout(true, true);
	}
	
	@SuppressWarnings("unchecked")
	public void initDataBindings(){
		DataBindingContext bindingContext = new DataBindingContext();
		
		UpdateValueStrategy strategyUpdateProductChilds = new UpdateValueStrategy() {
			@Override
			protected IStatus doSet(IObservableValue observableValue, Object value){
				IStatus status = super.doSet(observableValue, value);
				EigenartikelUtil.copyProductAttributesToArticleSetAsChild(getProductArtikel(),
					null);
				ContextServiceHolder.get().postEvent(ElexisEventTopics.EVENT_UPDATE,
					getProductArtikel());
				return status;
			}
		};
		
		//
		ISWTObservableValue observeTextTxtProductNameObserveWidget =
			WidgetProperties.text(SWT.Modify).observeDelayed(300, txtProductName);
		IObservableValue<String> productEigenartikelNameObserveDetailValue = PojoProperties
			.value(ITypedArticle.class, "name", String.class).observeDetail(productEigenartikel);
		bindingContext.bindValue(observeTextTxtProductNameObserveWidget,
			productEigenartikelNameObserveDetailValue, strategyUpdateProductChilds, null);
		observeTextTxtProductNameObserveWidget.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event){
				if (productEigenartikel.getValue() != null) {
					if (event.diff.getOldValue() != null
						&& !event.diff.getOldValue().toString().isEmpty()) {
						ContextServiceHolder.get().postEvent(ElexisEventTopics.EVENT_UPDATE,
							productEigenartikel.getValue());
					}
				}
			}
		});
		
		//
		IViewerObservableValue observeSingleSelectionComboViewerProductType =
			ViewerProperties.singleSelection().observe(comboViewerProductType);
		IObservableValue<ITypedArticle> productEigenartikelTypObserveDetailValue =
			PojoProperties.value(ITypedArticle.class, "subTyp", String.class)
				.observeDetail(productEigenartikel);
		bindingContext.bindValue(observeSingleSelectionComboViewerProductType,
			productEigenartikelTypObserveDetailValue, new UpdateValueStrategy() {
				// target to model	
				@Override
				public Object convert(Object value){
					return Character.toString(((EigenartikelTyp) value).getTypeChar());
				}
			}, new UpdateValueStrategy() {
				// model to target
				@Override
				public Object convert(Object value){
					return EigenartikelTyp.byCharSafe((String) value);
				}
			});
		
		//
		ISWTObservableValue observeTextTxtAtcCodeObserveWidget =
			WidgetProperties.text(SWT.Modify).observeDelayed(300, txtAtcCode);
		IObservableValue<String> productEigenartikelATC_codeObserveDetailValue = PojoProperties
			.value(ITypedArticle.class, "atcCode", String.class)
			.observeDetail(productEigenartikel);
		bindingContext.bindValue(observeTextTxtAtcCodeObserveWidget,
			productEigenartikelATC_codeObserveDetailValue, strategyUpdateProductChilds, null);
	}
	
	private void createEigenartikelComposite(ITypedArticle articleNew){
		EigenartikelComposite ec =
			new EigenartikelComposite(compositeArticleItems, SWT.NONE, articleNew);
		ec.setUnlocked(CoreHub.getLocalLockService().isLocked(productEigenartikel.getValue()));
	}
}
