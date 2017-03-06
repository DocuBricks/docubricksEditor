package docubricks.gui;

import java.io.File;
import java.util.ArrayList;

import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFileDialog.FileMode;

import docubricks.data.StepByStepInstruction;
import docubricks.data.AssemblyStep;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.data.MediaFile;
import docubricks.gui.qt.QLineEditTODO;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * List of instructions
 * 
 * @author Johan Henriksson
 *
 */
public class WidgetInstruction extends QWidget
	{
	public Signal1<StepByStepInstruction> sigDeleted=new Signal1<StepByStepInstruction>();
	
	private QPushButton bAddStep=new QPushButton(tr("Add step"));
	private QPushButton bAddManySteps=new QPushButton(tr("Add steps from files"));
	private QPushButton bRemove=new QPushButton(new QIcon(ImgResource.delete),"");
	private QLineEditTODO tfName=new QLineEditTODO();
	
	private QVBoxLayout layv=new QVBoxLayout();
	private QVBoxLayout laysteps=new QVBoxLayout();
	private ArrayList<WidgetStep> stepWidgets=new ArrayList<WidgetInstruction.WidgetStep>();
	
	private HeaderLabel labInstruction;
	
	private final DocubricksProject proj;
	private final Brick brick;
	private final StepByStepInstruction instructions;
			
	/**
	 * One instruction step
	 * @author Johan Henriksson
	 */
	public class WidgetStep extends QWidget
		{
		private QTextEditResize tfText=new QTextEditResize();
		private QPushButton bMenu=new QPushButton(tr("Options"));

		PaneMediaSet mediapane;
		
		private QMenu mOptions=new QMenu();
		
		WidgetInstructionComponents components;
		
		AssemblyStep step;
		public WidgetStep(AssemblyStep step)
			{
			this.step=step;
			mediapane=new PaneMediaSet(step.media);
			if(brick!=null)
				components=new WidgetInstructionComponents(proj, brick, step);
			
			QVBoxLayout layr=new QVBoxLayout();
			layr.addWidget(tfText);
			if(components!=null)
				layr.addLayout(components);
			layr.addWidget(bMenu);
			layr.setMargin(0);
			
			QHBoxLayout lay1=new QHBoxLayout();
			lay1.addWidget(mediapane);
			lay1.addLayout(layr);
			lay1.setMargin(0);
			setLayout(lay1);
			
			
			mOptions.addAction(tr("Move step up"), this, "actionMoveUp()");
			mOptions.addAction(tr("Move step down"), this, "actionMoveDown()");
			mOptions.addSeparator();
			mOptions.addAction(tr("Insert step before"), this, "actionInsertStep()");
			if(components!=null)
				{
				mOptions.addSeparator();
				mOptions.addAction(tr("Add component reference"), this, "actionAddComponent()");
				}
			mOptions.addSeparator();
			mOptions.addAction(tr("Remove step"), this, "actionRemoveStep()");
			bMenu.setMenu(mOptions);
	    
//			setMinimumHeight(200);
//	    updateGeometry();  //not enough!
			loadvalues();
			}
		
		
		public void actionInsertStep()
			{
			addStep(stepWidgets.indexOf(this), new AssemblyStep());
			}
		
		public void actionRemoveStep()
			{
			int ind=stepWidgets.indexOf(this);
			stepWidgets.remove(ind);
			instructions.steps.remove(ind);
			setVisible(false);
			laysteps.removeWidget(this);
			}


		public void actionMoveUp()
			{
			int ind=stepWidgets.indexOf(this);
			if(ind>0)
				{
				ind--;
				actionRemoveStep();
				inswidget(ind);
				}
			}

		public void actionMoveDown()
			{
			int ind=stepWidgets.indexOf(this);
			if(ind<stepWidgets.size()-1)
				{
				ind++;
				actionRemoveStep();
				inswidget(ind);
				}
			}

		
		public void actionAddComponent()
			{
			components.actionAddComponent();
			}
		
		private void inswidget(int ind)
			{
			instructions.steps.add(ind, step);
			stepWidgets.add(ind, this);
			setVisible(true);
			laysteps.insertWidget(ind, this);
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


		public void updateAllCombos()
			{
			components.updateAllCombos();
			}
		}

	
	public WidgetInstruction(DocubricksProject proj, Brick brick, StepByStepInstruction instructions, String header, boolean isExtraInstruction)
		{
		this.proj=proj;
		this.brick=brick;
		this.instructions=instructions;

		labInstruction=new HeaderLabel(header);

		QHBoxLayout layh=new QHBoxLayout();
		layh.setMargin(0);
		layh.addWidget(labInstruction);
		if(isExtraInstruction)
			{
			layh.addWidget(tfName);
			layh.addWidget(bRemove);
			}
		
		QHBoxLayout layButton=new QHBoxLayout();
		layButton.setMargin(0);
		layButton.addWidget(bAddStep);
		layButton.addWidget(bAddManySteps);

		layv.addLayout(layh);
		layv.addLayout(laysteps);
		layv.addLayout(layButton);
		layv.setMargin(0);
		setLayout(layv);

		loadvalues();
		
		bAddStep.clicked.connect(this,"actionAddStep()");
		bAddManySteps.clicked.connect(this,"actionAddSteps()");
		bRemove.clicked.connect(this,"actionDeleted()");
		}
	
	
	public void actionDeleted()
		{
		brick.instructions.remove(instructions);
		sigDeleted.emit(instructions);
		}
	
	
	public void loadvalues()
		{
		tfName.setText(instructions.name);
		for(AssemblyStep step:instructions.steps)
			addStepWidget(stepWidgets.size(), step);
		}
	
	public void storevalues()
		{
		instructions.name=tfName.text();
		for(WidgetStep ws:stepWidgets)
			ws.storevalues();
		}
	
	public void actionAddStep()
		{
		addStep(instructions.steps.size(), new AssemblyStep());
		}
	
	public void addStep(int index, AssemblyStep step)
		{
		instructions.steps.add(index,step);
		addStepWidget(index, step);
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
				addStep(stepWidgets.size(), step);
				}
			}		
		}

	public void addStepWidget(int index, AssemblyStep step)
		{
		WidgetStep ws=new WidgetStep(step);
		stepWidgets.add(index, ws);
		laysteps.insertWidget(index, ws);
		}


	public void updateAllCombos()
		{
		for(WidgetStep ws:stepWidgets)
			ws.updateAllCombos();
		}
	
	}
