/**
 */
package ch.elexis.core.findings.templates.model.provider;

import ch.elexis.core.findings.templates.model.util.ModelAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelItemProviderAdapterFactory extends ModelAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
		supportedTypes.add(ITableItemLabelProvider.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.FindingsTemplates} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FindingsTemplatesItemProvider findingsTemplatesItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.FindingsTemplates}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFindingsTemplatesAdapter() {
		if (findingsTemplatesItemProvider == null) {
			findingsTemplatesItemProvider = new FindingsTemplatesItemProvider(this);
		}

		return findingsTemplatesItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.FindingsTemplate} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FindingsTemplateItemProvider findingsTemplateItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.FindingsTemplate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFindingsTemplateAdapter() {
		if (findingsTemplateItemProvider == null) {
			findingsTemplateItemProvider = new FindingsTemplateItemProvider(this);
		}

		return findingsTemplateItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.InputDataNumeric} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputDataNumericItemProvider inputDataNumericItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.InputDataNumeric}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInputDataNumericAdapter() {
		if (inputDataNumericItemProvider == null) {
			inputDataNumericItemProvider = new InputDataNumericItemProvider(this);
		}

		return inputDataNumericItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.InputDataText} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputDataTextItemProvider inputDataTextItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.InputDataText}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInputDataTextAdapter() {
		if (inputDataTextItemProvider == null) {
			inputDataTextItemProvider = new InputDataTextItemProvider(this);
		}

		return inputDataTextItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.InputDataGroup} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputDataGroupItemProvider inputDataGroupItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.InputDataGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInputDataGroupAdapter() {
		if (inputDataGroupItemProvider == null) {
			inputDataGroupItemProvider = new InputDataGroupItemProvider(this);
		}

		return inputDataGroupItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link ch.elexis.core.findings.templates.model.InputDataGroupComponent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputDataGroupComponentItemProvider inputDataGroupComponentItemProvider;

	/**
	 * This creates an adapter for a {@link ch.elexis.core.findings.templates.model.InputDataGroupComponent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInputDataGroupComponentAdapter() {
		if (inputDataGroupComponentItemProvider == null) {
			inputDataGroupComponentItemProvider = new InputDataGroupComponentItemProvider(this);
		}

		return inputDataGroupComponentItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (findingsTemplatesItemProvider != null) findingsTemplatesItemProvider.dispose();
		if (findingsTemplateItemProvider != null) findingsTemplateItemProvider.dispose();
		if (inputDataNumericItemProvider != null) inputDataNumericItemProvider.dispose();
		if (inputDataTextItemProvider != null) inputDataTextItemProvider.dispose();
		if (inputDataGroupItemProvider != null) inputDataGroupItemProvider.dispose();
		if (inputDataGroupComponentItemProvider != null) inputDataGroupComponentItemProvider.dispose();
	}

}
