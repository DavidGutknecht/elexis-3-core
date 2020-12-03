package ch.elexis.core.tasks.internal.console;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.elexis.core.console.AbstractConsoleCommandProvider;
import ch.elexis.core.console.CmdAdvisor;
import ch.elexis.core.console.CmdParam;
import ch.elexis.core.console.ConsoleProgressMonitor;
import ch.elexis.core.model.tasks.IIdentifiedRunnable;
import ch.elexis.core.model.tasks.TaskException;
import ch.elexis.core.services.IContextService;
import ch.elexis.core.services.IModelService;
import ch.elexis.core.services.IVirtualFilesystemService;
import ch.elexis.core.services.IVirtualFilesystemService.IVirtualFilesystemHandle;
import ch.elexis.core.tasks.internal.service.TaskDescriptor;
import ch.elexis.core.tasks.model.ITask;
import ch.elexis.core.tasks.model.ITaskDescriptor;
import ch.elexis.core.tasks.model.ITaskService;
import ch.elexis.core.tasks.model.TaskTriggerType;

@Component(service = CommandProvider.class, immediate = true)
public class ConsoleCommandProvider extends AbstractConsoleCommandProvider {
	
	@Reference
	private ITaskService taskService;
	
	@Reference(target = "(" + IModelService.SERVICEMODELNAME + "=ch.elexis.core.tasks.model)")
	private IModelService taskModelService;
	
	@Reference
	private IContextService contextService;
	
	@Reference
	private IVirtualFilesystemService vfsService;
	
	@Activate
	public void activate(){
		register(this.getClass());
	}
	
	@CmdAdvisor(description = "task management")
	public void _task(CommandInterpreter ci){
		executeCommand("task", ci);
	}
	
	@CmdAdvisor(description = "list all persisted task descriptors")
	public void __task_descriptor_list(){
		List<ITaskDescriptor> taskDescriptors =
			taskModelService.getQuery(ITaskDescriptor.class, true, false).execute();
		taskDescriptors.forEach(e -> ci.println(e));
	}
	
	@CmdAdvisor(description = "list all currently active tasks (running and incurred)")
	public void __task_list(){
		List<ITask> runningTasks = taskService.getRunningTasks();
		// Trigger	ID	Descriptor Id/RefId		StartTime		Progress (%)
		ci.println("RUNNING");
		prflp("Trigger", 10);
		prflp("ID", 25);
		prflp("Descriptor Id/RefId", 25);
		prflp("StartTime", 30, true);
		
		runningTasks.stream().forEach(t -> {
			ITaskDescriptor td = t.getTaskDescriptor();
			prflp(td.getTriggerType().getName(), 10);
			prflp(t.getId(), 25);
			prflp(td.getReferenceId(), 25);
			prflp(t.getRunAt().toString(), 30, true);
		});
		
		ci.println("INCURRED");
		prflp("Trigger", 10);
		prflp("Id/RefId", 25);
		prflp("NextExecution", 30, true);
		
		//	Trigger  DescriptorId/RefId NextExecution
		List<ITaskDescriptor> incurredTasks = taskService.getIncurredTasks();
		incurredTasks.stream().forEach(td -> {
			prflp(td.getTriggerType().getName(), 10);
			prflp(td.getReferenceId(), 25);
			prflp((String) td.getTransientData().get("cron-next-exectime"), 30, true);
		});
	}
	
	@CmdAdvisor(description = "Gracefully cancel a running task")
	public void __task_cancel(
		@CmdParam(description = "task id OR task descriptor id or referenceId") String id){
		List<ITask> activeTasks = taskService.getRunningTasks();
		for (ITask task : activeTasks) {
			if (id.equals(task.getId()) || id.equals(task.getTaskDescriptor().getId())
				|| id.equalsIgnoreCase(task.getTaskDescriptor().getReferenceId())) {
				
				task.getProgressMonitor().setCanceled(true);
				ok("Sent setCanceled to Task " + task.getId());
				return;
			}
		}
		fail("No matching task for given id");
	}
	
	@CmdAdvisor(description = "create or modify a task descriptor from a json file")
	public void __task_descriptor_url(
		@CmdParam(description = "url referencing json file") String urlString)
		throws IOException, TaskException{
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		IVirtualFilesystemHandle of = vfsService.of(urlString);
		String content = new String(of.readAllBytes(), StandardCharsets.UTF_8);
		TaskDescriptor fromJson = gson.fromJson(content, TaskDescriptor.class);
		taskService.saveTaskDescriptor(fromJson);
	}
	
	@CmdAdvisor(description = "serialize a task descriptor to a json string")
	public void __task_descriptor_json(
		@CmdParam(description = "id or referenceId of the task descriptor") String idOrReferenceId){
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Optional<ITaskDescriptor> taskDescriptor =
			taskService.findTaskDescriptorByIdOrReferenceId(idOrReferenceId);
		if (taskDescriptor.isPresent()) {
			ITaskDescriptor iTaskDescriptor = taskDescriptor.get();
			String json = gson.toJson(iTaskDescriptor, TaskDescriptor.class);
			ci.println(json);
		} else {
			fail("object not found");
		}
	}
	
	@CmdAdvisor(description = "trigger the manual execution of a task")
	public void __task_descriptor_trigger(
		@CmdParam(description = "id or referenceId of the task descriptor") String idOrReferenceId)
		throws TaskException{
		Optional<ITaskDescriptor> taskDescriptor =
			taskService.findTaskDescriptorByIdOrReferenceId(idOrReferenceId);
		if (taskDescriptor.isPresent()) {
			String result = taskService.trigger(taskDescriptor.get(), new ConsoleProgressMonitor(),
				TaskTriggerType.MANUAL, null).toString();
			ok(result);
		} else {
			fail("Invalid or ambiguous id argument");
		}
	}
	
	@CmdAdvisor(description = "deactivate and remove a task descriptor")
	public void __task_descriptor_remove(
		@CmdParam(description = "id or referenceId of the task descriptor") String idOrReferenceId)
		throws TaskException{
		Optional<ITaskDescriptor> taskDescriptor =
			taskService.findTaskDescriptorByIdOrReferenceId(idOrReferenceId);
		if (taskDescriptor.isPresent()) {
			boolean result = taskService.removeTaskDescriptor(taskDescriptor.get());
			ok(result);
		} else {
			fail("Invalid or ambiguous id argument");
		}
	}
	
	@CmdAdvisor(description = "list all available identified runnables")
	public void __task_runnable_list(){
		List<IIdentifiedRunnable> availableRunnables = taskService.getIdentifiedRunnables();
		availableRunnables.stream().forEach(e -> ci.println("\t(" + e.getClass().getName() + ") "
			+ e.getId() + " - " + e.getLocalizedDescription()));
	}
	
	@CmdAdvisor(description = "de-/activate a task descriptor for execution")
	public String __task_descriptor_activate(List<String> arguments) throws TaskException{
		if (arguments.size() != 2) {
			return "usage: descriptorId (true | false)";
		}
		
		Optional<ITaskDescriptor> taskDescriptor =
			taskService.findTaskDescriptorByIdOrReferenceId(arguments.get(0));
		if (!taskDescriptor.isPresent()) {
			return "Invalid or ambiguous id argument";
		}
		Boolean active = Boolean.parseBoolean(arguments.get(1));
		
		taskService.setActive(taskDescriptor.get(), active);
		return ok();
	}
	
}
