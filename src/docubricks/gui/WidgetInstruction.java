package docubricks.gui;

import java.io.File;
import java.util.ArrayList;

import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFileDialog.FileMode;

import docubricks.data.AssemblyInstruction;
import docubricks.data.AssemblyStep;
import docubricks.data.MediaFile;

/**
 * 
 * List of instructions
 * 
 * @author Johan Henriksson
 *
 */
public class WidgetInstruction extends QWidget
	{
	private QPushButton bAddStep=new QPushButton(tr("Add step"));
	private QPushButton bAddManySteps=new QPushButton(tr("Add steps from files"));
	private QVBoxLayout layv=new QVBoxLayout();
	private QVBoxLayout laysteps=new QVBoxLayout();
	private ArrayList<WidgetStep> steps=new ArrayList<WidgetInstruction.WidgetStep>();
	
	private HeaderLabel labInstruction=new HeaderLabel(tr("Assembly instructions"));
	private AssemblyInstruction instructions;
			
	/**
	 * One instruction step
	 * @author Johan Henriksson
	 */
	public class WidgetStep extends QWidget
		{
		private QTextEdit tfText=new QTextEdit();
		private QPushButton bMenu=new QPushButton(tr("Options"));

		MediaSetPane mediapane;
		
		private QMenu mOptions=new QMenu();
		
		AssemblyStep step;
		public WidgetStep(AssemblyStep step)
			{
			this.step=step;
			mediapane=new MediaSetPane(step.media);
			
			QVBoxLayout layr=new QVBoxLayout();
			layr.addWidget(tfText);
			layr.addWidget(bMenu);
			layr.setMargin(0);
			
			QHBoxLayout lay1=new QHBoxLayout();
			lay1.addWidget(mediapane);
			lay1.addLayout(layr);
			lay1.setMargin(0);
			setLayout(lay1);
			
			
			mOptions.addAction(tr("Remove step"), this, "actionRemoveStep()");
			bMenu.setMenu(mOptions);
	    
//			setMinimumHeight(200);
//	    updateGeometry();  //not enough!
			loadvalues();
			}
		
		
		
		public void actionRemoveStep()
			{
			int ind=steps.indexOf(this);
			steps.remove(ind);
			instructions.steps.remove(ind);
			setVisible(false);
			laysteps.removeWidget(this);
			}


		public void loadvalues()
			{
			tfText.setText(step.getDescription());
			//mediapane.loadvalues(step.media);
			}

		public void storevalues()
			{
			step.setDescription(tfText.toPlainText());
			}
		}

	
	public WidgetInstruction(AssemblyInstruction instructions)
		{
		this.instructions=instructions;

		QHBoxLayout layh=new QHBoxLayout();
		layh.setMargin(0);
		layh.addWidget(bAddStep);
		layh.addWidget(bAddManySteps);

		layv.addWidget(labInstruction);
		layv.addLayout(laysteps);
		layv.addLayout(layh);
		layv.setMargin(0);
		setLayout(layv);

		loadvalues();
		
		bAddStep.clicked.connect(this,"actionAddStep()");
		bAddManySteps.clicked.connect(this,"actionAddSteps()");
		}
	
	
	public void loadvalues()
		{
		for(AssemblyStep step:instructions.steps)
			addStepWidget(step);
		}
	
	public void storevalues()
		{
		for(WidgetStep ws:steps)
			ws.storevalues();
		}
	
	public void actionAddStep()
		{
		AssemblyStep step=new AssemblyStep();
		addStep(step);
		}
	
	public void addStep(AssemblyStep step)
		{
		instructions.steps.add(step);
		addStepWidget(step);
		}

	public void actionAddSteps()
		{
		QFileDialog dia=new QFileDialog();
		dia.setFileMode(FileMode.ExistingFiles);
		dia.setDirectory(MainWindow.lastDirectory.getAbsolutePath());
		if(dia.exec()!=0)
			{
			for(int i=0;i<dia.selectedFiles().size();i++)
				{
				File f=new File(dia.selectedFiles().get(i));
				MainWindow.lastDirectory=f.getParentFile();
				
				MediaFile mf=new MediaFile();
				mf.f=f;
				
				AssemblyStep step=new AssemblyStep();
				step.media.files.add(mf);
				addStep(step);
				}
			}		
		}

	public void addStepWidget(AssemblyStep step)
		{
		WidgetStep ws=new WidgetStep(step);
		steps.add(ws);
		laysteps.addWidget(ws);
		}
	
	}
