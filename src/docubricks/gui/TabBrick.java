package docubricks.gui;


import java.util.HashMap;

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QMessageBox.StandardButtons;
import com.trolltech.qt.gui.QMessageBox.StandardButton;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.data.StepByStepInstruction;
import docubricks.gui.resource.ImgResource;


/**
 * 
 * Tab: one logical unit
 * 
 * @author Johan Henriksson
 *
 */
public class TabBrick extends QWidget
	{
	private QLineEdit tfName=new QLineEdit();
	private QLineEdit tfAbstract=new QLineEdit();

	private QTextEditResize tfLongDesc=new QTextEditResize();
	private QTextEditResize tfNotes=new QTextEditResize();
	
	private QScrollArea scroll=new QScrollArea();
	private WidgetInstruction wInstruction;
	private PaneFunctions parts;
	private PaneCopyright copyright;
	private PaneMediaSet mediapane;
	private QPushButton bRemove=new QPushButton(new QIcon(ImgResource.delete),"");

	private QPushButton bAddInstruction=new QPushButton("Add instruction set");

	public Brick unit;
	public DocubricksProject project;

	private HashMap<StepByStepInstruction, WidgetInstruction> mapInstWid=new HashMap<StepByStepInstruction, WidgetInstruction>();

	public Signal1<TabBrick> sigNameChanged=new Signal1<TabBrick>();
	public Signal1<TabBrick> sigRemove=new Signal1<TabBrick>();
	
	QVBoxLayout layinstructions=new QVBoxLayout();
	

	
	public TabBrick(DocubricksProject project, Brick unit)
		{
		this.project=project;
		this.unit=unit;	

		mediapane=new PaneMediaSet(unit.media);
		
		QVBoxLayout laytot=new QVBoxLayout();
		laytot.addWidget(scroll);
		scroll.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
		scroll.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
		scroll.setWidgetResizable(true);
		laytot.setMargin(0);
		setLayout(laytot);

		
		//Having a widget in the scrollpane, instead of a layout directly, is key to get it to work
		QGridLayout layGrid=new QGridLayout();
		QWidget scrollwid=new QWidget();
		scrollwid.setObjectName("form");
		scrollwid.setLayout(layGrid);
		scroll.setWidget(scrollwid);

		QHBoxLayout layMaterial=new QHBoxLayout();
		layMaterial.addWidget(tfName);
		layMaterial.addWidget(bRemove);

		
		int row=0;
		layGrid.addWidget(new HeaderLabel(tr("Brick overview")),row,0,1,2);
		row++;
		layGrid.addWidget(new QLabel(tr("Name:")),row,0);
		layGrid.addLayout(layMaterial,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Abstract:")),row,0);
		layGrid.addWidget(tfAbstract,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Description:")),row,0);
		layGrid.addWidget(tfLongDesc,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Notes:")),row,0);
		layGrid.addWidget(tfNotes,row,1);
		row++;
		layGrid.addWidget(mediapane,row,0,1,2);
		row++;

		//List all copyrights
		copyright=new PaneCopyright(project, unit);
		layGrid.addLayout(copyright,row,0,1,2);
		row++;
		

		//List all subunits here
		parts=new PaneFunctions(project, unit);
		layGrid.addLayout(parts,row,0,1,2);
		row++;
		

		
		wInstruction=new WidgetInstruction(project, unit, unit.asmInstruction, tr("Assembly instructions"), false);
		layGrid.addWidget(wInstruction,row,0,1,2);
		row++;

		layGrid.addLayout(layinstructions, row, 0, 1, 2);
		row++;
		layGrid.addWidget(bAddInstruction, row, 0, 1, 2, AlignmentFlag.AlignRight);
		row++;

	
		loadvalues();

		
		tfName.textChanged.connect(this,"actionNameChanged()");
		parts.sigChanged.connect(this,"actionPartsChanged()");
		bRemove.clicked.connect(this,"actionRemove()");
		bAddInstruction.clicked.connect(this,"actionAddInstructions()");
		}
	


	public void actionRemove()
		{
		StandardButton btn=QMessageBox.question(this, QtProgramInfo.programName, tr("Are you sure you want to delete this brick?"), 
				new StandardButtons(StandardButton.Ok, StandardButton.Cancel));
		if(btn.equals(StandardButton.Ok))
			sigRemove.emit(this);
		}
	public void actionNameChanged()
		{
		unit.setName(tfName.text());
		sigNameChanged.emit(this);
		}
	public void actionPartsChanged()
		{
		sigNameChanged.emit(this);
		updateAllCombos();
		}
	
	
	public void loadvalues()
		{
		//mediapane.loadvalues(unit.media);

		tfName.setText(unit.getName());
		tfAbstract.setText(unit.getAbstract());
		tfLongDesc.setText(unit.getLongDescription());
		tfNotes.setText(unit.getNotes());

		for(StepByStepInstruction inst:unit.instructions)
			addInstructionWidget(inst);
		}



	public void storevalues()
		{
		unit.setName(tfName.text());
		unit.setAbstract(tfAbstract.text());
		unit.setLongDescription(tfLongDesc.toPlainText());
		unit.setNotes(tfNotes.toPlainText());
		copyright.storevalues();
		
		wInstruction.storevalues();
		for(WidgetInstruction i:mapInstWid.values())
			i.storevalues();
		}
	

	public void updateAllCombos()
		{
		parts.updateAllCombos();
		copyright.updateAllCombos();
		wInstruction.updateAllCombos();
		}
	
	
	public void actionAddInstructions()
		{
		StepByStepInstruction inst=new StepByStepInstruction();
		unit.instructions.add(inst);
		addInstructionWidget(inst);
		}
	
	
	
	
	private void addInstructionWidget(StepByStepInstruction inst)
		{
		WidgetInstruction i=new WidgetInstruction(project, unit, inst, "Custom instructions: ", true);
		i.sigDeleted.connect(this,"deleteInstruction(StepByStepInstruction)");
		layinstructions.addWidget(i);
		mapInstWid.put(inst, i);
		}
	
	public void deleteInstruction(StepByStepInstruction inst)
		{
		mapInstWid.get(inst).hide();
		layinstructions.removeWidget(mapInstWid.get(inst));
		mapInstWid.remove(inst);
		}
	
	}
