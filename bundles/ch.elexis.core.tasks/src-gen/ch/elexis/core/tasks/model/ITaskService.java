/**
 * Copyright (c) 2019 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 */
package ch.elexis.core.tasks.model;

import ch.elexis.core.model.tasks.IIdentifiedRunnable;
import ch.elexis.core.model.tasks.TaskException;

import java.util.List;
import java.util.Map;

import java.util.Optional;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ITask Service</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see ch.elexis.core.tasks.model.ModelPackage#getITaskService()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ITaskService {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException" runnableDataType="ch.elexis.core.tasks.model.IIdentifiedRunnable"
	 * @generated
	 */
	ITaskDescriptor createTaskDescriptor(IIdentifiedRunnable runnable) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * Removing a task descriptor also removes all ITask entries that reference to it.
	 * <!-- end-user-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException"
	 * @generated
	 */
	boolean removeTaskDescriptor(ITaskDescriptor taskDescriptor) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * @param progressMonitor The progress monitor to report to. If null the task will initialise its own progress monitor.
	 * @param runContext key value pairs to add to the task run context already populated out of ITaskDescriptor#runContext (e.g. the file system trigger adds the file triggering the event)
	 * <!-- end-model-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException" taskDescriptorRequired="true" progressMonitorDataType="ch.elexis.core.tasks.model.IProgressMonitor"
	 * @generated
	 */
	ITask trigger(ITaskDescriptor taskDescriptor, IProgressMonitor progressMonitor, TaskTriggerType trigger, Map<String, String> runContext) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="ch.elexis.core.tasks.model.IIdentifiedRunnable" exceptions="ch.elexis.core.tasks.model.TaskException" runnableIdRequired="true"
	 * @generated
	 */
	IIdentifiedRunnable instantiateRunnableById(String runnableId) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException" taskDescriptorRequired="true"
	 * @generated
	 */
	void saveTaskDescriptor(ITaskDescriptor taskDescriptor) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * activates / deactivates triggering the provided ITaskDescriptor
	 * <!-- end-model-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException"
	 * @generated
	 */
	void setActive(ITaskDescriptor taskDescriptor, boolean active) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Trigger a task by its task descriptor reference id
	 * @param progressMonitor The progress monitor to report to. If null the task will initialise its own progress monitor.
	 * @param runContext key value pairs to add to the task run context already populated out of ITaskDescriptor#runContext (e.g. the file system trigger adds the file triggering the event)
	 * <!-- end-model-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException" progressMonitorDataType="ch.elexis.core.tasks.model.IProgressMonitor"
	 * @generated
	 */
	ITask trigger(String taskDescriptorReferenceId, IProgressMonitor progressMonitor, TaskTriggerType trigger, Map<String, String> runContext) throws TaskException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Get all available runnables. Do NOT use these for actual execution, but instead use #instantiateRunnableById to fetch a new one
	 * <!-- end-model-doc -->
	 * @model kind="operation" type="ch.elexis.core.types.List&lt;ch.elexis.core.tasks.model.IIdentifiedRunnable&gt;" required="true" many="false"
	 * @generated
	 */
	List<IIdentifiedRunnable> getIdentifiedRunnables();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="ch.elexis.core.types.Optional&lt;ch.elexis.core.tasks.model.ITaskDescriptor&gt;"
	 * @generated
	 */
	Optional<ITaskDescriptor> findTaskDescriptorByIdOrReferenceId(String idOrReferenceId);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Find the latest execution of the provided task descriptor, ordered by execution time, newest first. Includes currently running tasks.
	 * <!-- end-model-doc -->
	 * @model dataType="ch.elexis.core.types.Optional&lt;ch.elexis.core.tasks.model.ITask&gt;"
	 * @generated
	 */
	Optional<ITask> findLatestExecution(ITaskDescriptor taskDescriptor);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Get the currently running tasks
	 * <!-- end-model-doc -->
	 * @model kind="operation" type="ch.elexis.core.types.List&lt;ch.elexis.core.tasks.model.ITask&gt;" required="true" many="false"
	 * @generated
	 */
	List<ITask> getRunningTasks();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Get the incurred tasks, that is, all task descriptors that are active (will be run on the given trigger) for this runner (i.e. station)
	 * <!-- end-model-doc -->
	 * @model kind="operation" type="ch.elexis.core.types.List&lt;ch.elexis.core.tasks.model.ITaskDescriptor&gt;" required="true" many="false"
	 * @generated
	 */
	List<ITaskDescriptor> getIncurredTasks();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * @param progressMonitor The progress monitor to report to. If null the task will initialise its own progress monitor.
	 * @param runContext key value pairs to add to the task run context already populated out of ITaskDescriptor#runContext (e.g. the file system trigger adds the file triggering the event)
	 * <!-- end-model-doc -->
	 * @model exceptions="ch.elexis.core.tasks.model.TaskException" taskDescriptorRequired="true" progressMonitorDataType="ch.elexis.core.tasks.model.IProgressMonitor"
	 * @generated
	 */
	ITask triggerSync(ITaskDescriptor taskDescriptor, IProgressMonitor progressMonitor, TaskTriggerType trigger, Map<String, String> runContext) throws TaskException;

} // ITaskService
