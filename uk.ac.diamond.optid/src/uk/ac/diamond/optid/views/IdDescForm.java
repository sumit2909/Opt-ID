package uk.ac.diamond.optid.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class IdDescForm extends ViewPart {
	
	static final String ID = "uk.ac.diamond.optid.idDescForm";
	
	/* Combo items */
	private static final String[] ID_PARAM_TYPE_LIST = new String[] {"PPM AntiSymmetric", "APPLE Symmetric"};
	private static final String[] INPUT_METHOD_LIST = new String[] {"Create new file", "Load from disk"};
	
	/* Components */
	private ScrolledComposite scrolledComp;
	private Composite mainComposite;
	private Combo cboInputMethod;
	private Combo cboType;

	@Override
	public void createPartControl(Composite parent) {
		// Display vertical scroll bar if contents do not fit
		scrolledComp = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		scrolledComp.setExpandHorizontal(true);
		scrolledComp.setExpandVertical(true);
		
		// Top-level composite
		mainComposite = new Composite(scrolledComp, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setupInputMethod(mainComposite);
		addHorizontalSeparator(mainComposite);
		setupNewFileForm(mainComposite);
		setupLoadFile(mainComposite);

		scrolledComp.setContent(mainComposite);
		// Set width at which vertical scroll bar will be used
		scrolledComp.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * Setup components to select input method
	 * @param parent
	 */
	private void setupInputMethod(Composite parent) {
		// Container with grid layout 2 components wide
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		// Remove margins
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		comp.setLayout(gridLayout);
	    comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
	    // Components: Label & Combo
		(new Label(comp, SWT.NONE)).setText("Input method");
		cboInputMethod = new Combo(comp, SWT.READ_ONLY);
		cboInputMethod.setItems(INPUT_METHOD_LIST);
		
		// Make combo stretch to fill width of view
		cboInputMethod.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	/**
	 * Setup components for creating new JSON file
	 * @param parent
	 */
	private void setupNewFileForm(Composite parent) {
		// Container of input groups that will be hidden/showed
		final Composite comp = new Composite(parent, SWT.NONE);		
		GridLayout gridLayout = new GridLayout(1, false);
		// Remove margins
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 15;
		comp.setLayout(gridLayout);
	    final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
	    comp.setLayoutData(gridData);
	    
		// Create groups for each category of inputs
		setupIdParams(comp);
		setupMagnetDims(comp);
		setupCalParams(comp);
		setupAppleSymOnlyParams(comp);
		
		// Initially hidden
		gridData.exclude = true;
		comp.setVisible(false);
		
		// If "Create new file" selected then show corresponding components
		cboInputMethod.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {			
				if (cboInputMethod.getText().equals("Create new file")) {
					gridData.exclude = false;
					comp.setVisible(true);
				} else {
					gridData.exclude = true;
					comp.setVisible(false);
				}
				
				// Resizes parent and adjusts scroll bar to adapt to new size
				mainComposite.pack();
				scrolledComp.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}
	
	/**
	 * Setup components for loading existing file
	 * @param parent
	 */
	private void setupLoadFile(Composite parent) {
		// Container of input groups that will be hidden/showed
		final Composite comp = new Composite(parent, SWT.NONE);		
		GridLayout gridLayout = new GridLayout(2, false);
		// Remove margins
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		comp.setLayout(gridLayout);
	    final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
	    comp.setLayoutData(gridData);
	    
	    // Label
		(new Label(comp, SWT.NONE)).setText("ID Description JSON File");
		new Label(comp, SWT.NONE); // Dummy label to fill 2nd cell
		
		// Text box
		Text txtFilePath = new Text(comp, SWT.SINGLE | SWT.BORDER);
		txtFilePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Button - File path dialog
		Button btnDialog = new Button(comp, SWT.PUSH);
		btnDialog.setText("Open");
		
		// Initially hidden
		gridData.exclude = true;
		comp.setVisible(false);
		
		// If "Load from disk" selected then show corresponding components
		cboInputMethod.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {			
				if (cboInputMethod.getText().equals("Load from disk")) {
					gridData.exclude = false;
					comp.setVisible(true);
				} else {
					gridData.exclude = true;
					comp.setVisible(false);
				}
				
				// Resizes parent and adjusts scroll bar to adapt to new size
				mainComposite.pack();
				scrolledComp.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}

	/**
	 * Setup group of text fields to input general ID parameters
	 * @param parent
	 */
	private void setupIdParams(final Composite parent) {
		// Group - ID Parameters
		Group grpIdParams = new Group(parent, SWT.NONE);
		grpIdParams.setText("ID Parameters");
		grpIdParams.setLayout(new GridLayout(2, false));
		grpIdParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Text (String) Field - Name
		(new Label(grpIdParams, SWT.NONE)).setText("Name");
		Text txtName = new Text(grpIdParams, SWT.SINGLE | SWT.BORDER);
		
		// Combo (String) Field - Type
		(new Label(grpIdParams, SWT.NONE)).setText("Type");
		cboType = new Combo(grpIdParams, SWT.READ_ONLY);
		cboType.setItems(ID_PARAM_TYPE_LIST);
				
		// Text (int) Field - Periods
		(new Label(grpIdParams, SWT.NONE)).setText("Periods");
		Text txtPeriods = new Text(grpIdParams, SWT.SINGLE | SWT.BORDER);
		txtPeriods.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Text (float) Field - Gap
		(new Label(grpIdParams, SWT.NONE)).setText("Gap");
		Text txtGap = new Text(grpIdParams, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - Interstice
		(new Label(grpIdParams, SWT.NONE)).setText("Interstice");
		Text txtInterstice = new Text(grpIdParams, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cboType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtGap.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtInterstice.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
	}
	
	/**
	 * Setup group to input magnet dimensions
	 * @param parent
	 */
	private void setupMagnetDims(Composite parent) {
		// Group - Magnet Dimensions
		Group grpMagDims = new Group(parent, SWT.NONE);
		grpMagDims.setText("Magnet Dimensions");
		// Each row has 4 cells: label followed by 3 text boxes
		grpMagDims.setLayout(new GridLayout(4, false));
		grpMagDims.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// X, Z, S labels
		new Label(grpMagDims, SWT.NONE); // Dummy label to skip 1st cell
		Label lblColTitle;
		lblColTitle = new Label(grpMagDims, SWT.NONE);
		lblColTitle.setText("X");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		
		lblColTitle = new Label(grpMagDims, SWT.NONE);
		lblColTitle.setText("Z");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));

		lblColTitle = new Label(grpMagDims, SWT.NONE);
		lblColTitle.setText("S");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		
		/* Full magnet block */
		(new Label(grpMagDims, SWT.NONE)).setText("Full");
		// Text (float) Field - Full X
		Text txtFullX = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - Full Z
		Text txtFullZ = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - Full S
		Text txtFullS = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtFullX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtFullZ.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtFullS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* VE magnet block */
		(new Label(grpMagDims, SWT.NONE)).setText("VE");
		// Text (float) Field - VE X
		Text txtVeX = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - VE Z
		Text txtVeZ = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - VE S
		Text txtVeS = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtVeX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtVeZ.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtVeS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* HE magnet block */
		(new Label(grpMagDims, SWT.NONE)).setText("HE");
		// Text (float) Field - HE X
		Text txtHeX = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - HE Z
		Text txtHeZ = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - HE S
		Text txtHeS = new Text(grpMagDims, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtHeX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtHeZ.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtHeS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/**
	 * Setup group to input calculation parameters
	 * @param parent
	 */
	private void setupCalParams(Composite parent) {
		// Group - Calculation Parameters
		Group grpCalParams = new Group(parent, SWT.NONE);
		grpCalParams.setText("Calculation Parameters");
		grpCalParams.setLayout(new GridLayout(1, false));
		grpCalParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Composite - X & Z Container
		Composite comp1 = new Composite(grpCalParams, SWT.NONE);
		// Each row has 4 cells: label followed by 3 text boxes
		comp1.setLayout(new GridLayout(4, false));
		comp1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Start, Stop, Step labels
		new Label(comp1, SWT.NONE); // Dummy label to skip 1st cell
		Label lblColTitle;
		lblColTitle = new Label(comp1, SWT.NONE);
		lblColTitle.setText("Start");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		
		lblColTitle = new Label(comp1, SWT.NONE);
		lblColTitle.setText("Stop");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));

		lblColTitle = new Label(comp1, SWT.NONE);
		lblColTitle.setText("Step");
		lblColTitle.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		
		/* X Start-Stop-Step */
		(new Label(comp1, SWT.NONE)).setText("X");
		// Text (float) Field - X Start
		Text txtXStart = new Text(comp1, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - X Stop
		Text txtXStop = new Text(comp1, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - X Step
		Text txtXStep = new Text(comp1, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtXStart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtXStop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtXStep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* Z Start-Stop-Step */
		(new Label(comp1, SWT.NONE)).setText("Z");
		// Text (float) Field - Z Start
		Text txtZStart = new Text(comp1, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - Z Stop
		Text txtZStop = new Text(comp1, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - Z Step
		Text txtZStep = new Text(comp1, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtZStart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtZStop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtZStep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Composite - 'Steps in S' Container
		Composite comp2 = new Composite(grpCalParams, SWT.NONE);
		// Each row has 2 cells: label followed by a text box
		comp2.setLayout(new GridLayout(2, false));
		comp2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Text (float) Field - Steps in S
		(new Label(comp2, SWT.NONE)).setText("Steps in S");
		Text txtStepsS = new Text(comp2, SWT.SINGLE | SWT.BORDER);
		txtStepsS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	/**
	 * Setup group to input Apple Symmetric only parameters
	 * <p>
	 * Visibility depends on value of Type in ID Parameters
	 * @param parent
	 */
	private void setupAppleSymOnlyParams(final Composite parent) {
		// A composite is required between parent and grpAppleSymOnly
		// since we need to show/hide contents depending on value of
		// Type in ID Parameters
		final Composite compAppleSymParams = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		// Remove margins so that group has same positioning as groups above it
		// Composite is no longer visible on the UI
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		compAppleSymParams.setLayout(gridLayout);
		// Used in cboType selection listener
	    final GridData gridDataAppleSymParams = new GridData(SWT.FILL, SWT.FILL, true, false);
	    compAppleSymParams.setLayoutData(gridDataAppleSymParams);
		
		// Group - Apple Symmetric only parameters
		Group grpAppleSymOnly = new Group(compAppleSymParams, SWT.NONE);
		grpAppleSymOnly.setText("APPLE Symmetric only");
		// Each row has 2 cells: label followed by a text box
		grpAppleSymOnly.setLayout(new GridLayout(2, false));
		grpAppleSymOnly.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Text (float) Field - End gap
		(new Label(grpAppleSymOnly, SWT.NONE)).setText("End gap");
		Text txtEndGap = new Text(grpAppleSymOnly, SWT.SINGLE | SWT.BORDER);
		
		// Text (float) Field - Phasing gap
		(new Label(grpAppleSymOnly, SWT.NONE)).setText("Phasing gap");
		Text txtPhasingGap = new Text(grpAppleSymOnly, SWT.SINGLE | SWT.BORDER);

		// Text (float) Field - Clamp cut
		(new Label(grpAppleSymOnly, SWT.NONE)).setText("Clamp cut");
		Text txtClampCut = new Text(grpAppleSymOnly, SWT.SINGLE | SWT.BORDER);
		
		// Make text box stretch to fill width of view
		txtEndGap.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtPhasingGap.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtClampCut.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Initially no value is selected in cboType so parameters are hidden
		gridDataAppleSymParams.exclude = true;
		compAppleSymParams.setVisible(false);
		
		// If "APPLE Symmetric" selected then show optional parameters
		// otherwise hide them
		cboType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {			
				if (cboType.getText().equals("APPLE Symmetric")) {
					gridDataAppleSymParams.exclude = false;
					compAppleSymParams.setVisible(true);
				} else {
					gridDataAppleSymParams.exclude = true;
					compAppleSymParams.setVisible(false);
				}
				
				// Resizes parent and adjusts scroll bar to adapt to new size
				mainComposite.pack();
				scrolledComp.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}
	
	/**
	 * Adds horizontal line separator to composite
	 * @param parent
	 */
	private void addHorizontalSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
	    separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	@Override
	public void setFocus() {
	}

}
