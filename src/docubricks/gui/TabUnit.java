package docubricks.gui;

import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Unit;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;


/**
 * 
 * Tab: one logical unit
 * 
 * @author Johan Henriksson
 *
 */
public class TabUnit extends QWidget
	{
	private QLineEdit tfName=new QLineEdit();
	private QLineEdit tfAbstract=new QLineEdit();

	private QTextEdit tfLongDesc=new QTextEdit();
	private QTextEdit tfWhy=new QTextEdit();
	private QTextEdit tfHow=new QTextEdit();
	private QTextEdit tfWhat=new QTextEdit();
	//private QTextEdit tfAuthor=new QTextEdit();
	
	
	private QScrollArea scroll=new QScrollArea();
	private WidgetInstruction wins;
	private PaneLogicalParts parts;
	PaneCopyright copyright;
	private PaneMediaSet mediapane;
	private QPushButton bRemove=new QPushButton(new QIcon(ImgResource.delete),"");

	public Unit unit;
	public DocubricksProject project;

	public Signal1<TabUnit> sigNameChanged=new Signal1<TabUnit>();
	public Signal1<TabUnit> sigRemove=new Signal1<TabUnit>();
	

	

	
	public TabUnit(DocubricksProject project, Unit unit)
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
		layGrid.addWidget(new HeaderLabel(tr("General unit information")),row,0,1,2);
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
		layGrid.addWidget(new QLabel(tr("What:")),row,0);
		layGrid.addWidget(tfWhat,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Why:")),row,0);
		layGrid.addWidget(tfWhy,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("How:")),row,0);
		layGrid.addWidget(tfHow,row,1);
		row++;
		//layGrid.addWidget(new QLabel(tr("Media:")),row,0);
		//row++;
		layGrid.addWidget(mediapane,row,0,1,2);
		row++;

		//List all copyrights
		copyright=new PaneCopyright(project, unit);
		layGrid.addLayout(copyright,row,0,1,2);
		row++;
		

		//List all subunits here
		parts=new PaneLogicalParts(project, unit);
		layGrid.addLayout(parts,row,0,1,2);
		row++;
		
		wins=new WidgetInstruction(unit.asmInstruction);
		layGrid.addWidget(wins,row,0,1,2);
		row++;

	
		loadvalues();

		
		tfName.textChanged.connect(this,"actionNameChanged()");
		bRemove.clicked.connect(this,"actionRemove()");
		}
	


	public void actionRemove()
		{
		sigRemove.emit(this);
		}
	public void actionNameChanged()
		{
		unit.setName(tfName.text());
		sigNameChanged.emit(this);
		}
	
	
	public void loadvalues()
		{
		//mediapane.loadvalues(unit.media);

		tfName.setText(unit.getName());
		tfAbstract.setText(unit.getAbstract());
		tfLongDesc.setText(unit.getLongDescription());
		tfWhy.setText(unit.getWhy());
		tfHow.setText(unit.getHow());
		tfWhat.setText(unit.getWhat());
//		tfAuthor.setText(unit.authors);

		
		}



	public void storevalues()
		{
		unit.setName(tfName.text());
		unit.setAbstract(tfAbstract.text());
		unit.setLongDescription(tfLongDesc.toPlainText());
		unit.setWhy(tfWhy.toPlainText());
		unit.setHow(tfHow.toPlainText());
		unit.setWhat(tfWhat.toPlainText());
//		unit.authors=tfAuthor.toPlainText();
		wins.storevalues();
		copyright.storevalues();
		}
	

	public void updateAllCombos()
		{
		parts.updateAllCombos();
		copyright.updateAllCombos();
		}
	
	
	}
