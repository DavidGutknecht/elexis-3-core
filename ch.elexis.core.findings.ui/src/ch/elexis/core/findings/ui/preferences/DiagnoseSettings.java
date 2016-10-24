package ch.elexis.core.findings.ui.preferences;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.findings.ICondition;
import ch.elexis.core.findings.ICondition.ConditionCategory;
import ch.elexis.core.findings.ICondition.ConditionStatus;
import ch.elexis.core.findings.IFinding;
import ch.elexis.core.findings.IFindingsService;
import ch.elexis.core.findings.ui.services.FindingsServiceComponent;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Patient;
import ch.elexis.data.Query;

public class DiagnoseSettings extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	
	private BooleanFieldEditor diagStructFieldEditor;
	
	@Override
	public void init(IWorkbench workbench){
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.globalCfg));
	}
	
	@Override
	protected void createFieldEditors(){
		diagStructFieldEditor =
			new BooleanFieldEditor(SettingsConstants.DIAGNOSE_SETTINGS_USE_STRUCTURED,
				"Diagnosen strukturiert anzeigen (global)", getFieldEditorParent());
		addField(diagStructFieldEditor);
	}
	
	private Logger getLogger(){
		return LoggerFactory.getLogger(DiagnoseSettings.class);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event){
		super.propertyChange(event);
		if (event != null && event.getSource() == diagStructFieldEditor) {
			if (event.getNewValue().equals(Boolean.TRUE)) {
				if (MessageDialog.openConfirm(getShell(), "Strukturierte Diagnosen",
					"Bisher erfasste Text Diagnosen werden automatisch in strukturierte umgewandelt.\n"
						+ "Wollen Sie wirklich von nun an strukturierte Diagnosen verwenden?")) {
					ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(getShell());
					try {
						progressDialog.run(true, true, new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException{
								Query<Patient> query = new Query<>(Patient.class);
								List<Patient> patients = query.execute();
								monitor.beginTask("Strukturierte Diagnosen erzeugen.",
									patients.size());
								IFindingsService findingsService =
									FindingsServiceComponent.getService();
								for (Patient patient : patients) {
									String diagnosen = patient.getDiagnosen();
									List<IFinding> existing =
										getExistingDiagnoses(patient.getId(), findingsService);
									// only create if there is a diagnosis and no structured diagnosis already there
									if (diagnosen != null && !diagnosen.isEmpty()
										&& existing.isEmpty()) {
										ICondition condition =
											findingsService.getFindingsFactory().createCondition();
										condition.setPatientId(patient.getId());
										condition.setCategory(ConditionCategory.DIAGNOSIS);
										condition.setStatus(ConditionStatus.ACTIVE);
										condition.setText(diagnosen);
										findingsService.saveFinding(condition);
									}
									monitor.worked(1);
									if (monitor.isCanceled()) {
										break;
									}
								}
								monitor.done();
							}
							
							private List<IFinding> getExistingDiagnoses(String patientId,
								IFindingsService findingsService){
								return findingsService
									.getPatientsFindings(patientId, ICondition.class).stream()
									.filter(condition -> ((ICondition) condition)
										.getCategory() == ConditionCategory.DIAGNOSIS)
									.collect(Collectors.toList());
							};
						});
					} catch (InvocationTargetException | InterruptedException e) {
						MessageDialog.openError(getShell(), "Diagnosen konvertieren",
							"Fehler beim erzeugen der strukturierten Diagnosen.");
						getLogger().error("Error creating structured diagnosis", e);
					}
				} else {
					getPreferenceStore()
						.setValue(SettingsConstants.DIAGNOSE_SETTINGS_USE_STRUCTURED, false);
					// refresh later, on immediate refresh wasSelected of FieldEditor gets overwritten
					getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run(){
							diagStructFieldEditor.load();
						}
					});
				}
			} else {
				if (MessageDialog.openConfirm(getShell(), "Strukturierte Diagnosen",
					"Bisher erfasste Strukturierte Diagnosen werden nicht in Text umgewandelt.\n"
						+ "Wollen Sie wirklich von nun an Text Diagnosen verwenden?")) {
					// nothig to do here
				} else {
					getPreferenceStore()
						.setValue(SettingsConstants.DIAGNOSE_SETTINGS_USE_STRUCTURED, true);
					// refresh later, on immediate refresh wasSelected of FieldEditor gets overwritten
					getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run(){
							diagStructFieldEditor.load();
						}
					});
				}
			}
		}
	}
}
