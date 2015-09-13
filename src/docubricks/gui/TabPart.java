package docubricks.gui;

import java.util.LinkedList;
import java.util.TreeMap;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QMessageBox.StandardButton;
import com.trolltech.qt.gui.QMessageBox.StandardButtons;

import docubricks.data.MaterialUnit;
import docubricks.data.PhysicalPart;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * Pane for one logical part
 * 
 * @author Johan Henriksson
 *
 */
public class TabPart extends QWidget
	{
	public static LinkedList<String> manufacturingMethods=new LinkedList<String>();
	
	public static void addLicense(String s)
		{
		manufacturingMethods.add(s);
		}
	
	static
		{
		manufacturingMethods.add("3D printing");
		manufacturingMethods.add("Laser cutting");
		manufacturingMethods.add("PCB/electronics");
		manufacturingMethods.add("CNC milling");
		manufacturingMethods.add("Other");
		}
		
	
	
	private DocubricksProject proj;
	public PhysicalPart part;
	
	public Signal0 signalUpdated=new Signal0();
	public Signal1<TabPart> sigNameChanged=new Signal1<TabPart>();
	public Signal1<TabPart> sigRemove=new Signal1<TabPart>();
	
	private QLineEdit tfDescription=new QLineEdit();
	private QLineEdit tfSupplier=new QLineEdit();
	private QLineEdit tfSupplierPartNum=new QLineEdit();
	private QLineEdit tfManufacturerPartNum=new QLineEdit();
	private QLineEdit tfURL=new QLineEdit();

	private QLineEdit tfMaterialAmount=new QLineEdit();
	private QComboBox comboQuantityUnit=new QComboBox();
	
	private WidgetInstruction wInstruction;

	private QPushButton bRemovePart=new QPushButton(new QIcon(ImgResource.delete),"");
	private QHBoxLayout laybuttons=new QHBoxLayout();

	private PaneMediaSet mediapane;
	
	private TreeMap<String, MaterialUnit> mapMaterialUnitFWD=new TreeMap<String, MaterialUnit>();

	private QComboBox comboManufacturingMethod=new QComboBox();

	
	/**
	 * Constructor for one logical part pane
	 */
	public TabPart(DocubricksProject proj, PhysicalPart part)
		{
		this.part=part;
		this.proj=proj;
		
		QVBoxLayout lay=new QVBoxLayout();
		setLayout(lay);

		//Set list of licenses
		comboManufacturingMethod.addItem("");
		for(String s:manufacturingMethods)
			comboManufacturingMethod.addItem(s);
		comboManufacturingMethod.setEditable(true);
		
		mapMaterialUnitFWD.put(tr("<none>"), MaterialUnit.NONE);
		mapMaterialUnitFWD.put(tr("kg"), MaterialUnit.KG);
		mapMaterialUnitFWD.put(tr("litres"), MaterialUnit.LITRES);
		for(String n:mapMaterialUnitFWD.keySet())
			comboQuantityUnit.addItem(n);
		
		
		mediapane=new PaneMediaSet(part.media);
		wInstruction=new WidgetInstruction(proj, null, part.instructions, tr("Manufacturing instructions"));
		
		QHBoxLayout layMaterial=new QHBoxLayout();
		layMaterial.addWidget(tfMaterialAmount);
		layMaterial.addWidget(comboQuantityUnit);

		QHBoxLayout layName=new QHBoxLayout();
		layName.addWidget(tfDescription);
		layName.addWidget(bRemovePart);

		QGridLayout layGrid=new QGridLayout();
		
		int row=0;
		layGrid.addWidget(new QLabel(tr("Name:")),row,0);
		layGrid.addLayout(layName,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Manufacturing method:")),row,0);
		layGrid.addWidget(comboManufacturingMethod,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Supplier:")),row,0);
		layGrid.addWidget(tfSupplier,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Supplier part#:")),row,0);
		layGrid.addWidget(tfSupplierPartNum,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Manufacturer part#:")),row,0);
		layGrid.addWidget(tfManufacturerPartNum,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("URL:")),row,0);
		layGrid.addWidget(tfURL,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Material usage:")),row,0);
		layGrid.addLayout(layMaterial,row,1);
		row++;
		layGrid.addWidget(new QLabel(tr("Media:")),row,0);
		row++;
		layGrid.addWidget(mediapane,row,0,1,2);
		row++;
		layGrid.addWidget(wInstruction,row,0,1,2);
		row++;
		
		
		lay.addLayout(layGrid);
		lay.addLayout(laybuttons);
		
		loadvalues();

		tfDescription.textEdited.connect(this,"editvalues()");
		bRemovePart.clicked.connect(this,"actionRemovePhysPart()");
		}
	
	/**
	 * Remove this physical part
	 */
	public void actionRemovePhysPart()
		{
		StandardButton btn=QMessageBox.question(this, QtProgramInfo.programName, tr("Are you sure you want to delete this part?"), 
				new StandardButtons(StandardButton.Ok, StandardButton.Cancel));
		if(btn.equals(StandardButton.Ok))
			{
			proj.physicalParts.remove(part);
			sigRemove.emit(this);
			}

		
		}
	
	public void loadvalues()
		{
		tfDescription.setText(part.description);
		comboManufacturingMethod.setEditText(part.manufacturingMethod);
		tfSupplier.setText(part.supplier);
		tfSupplierPartNum.setText(part.supplierPartNum);
		tfManufacturerPartNum.setText(part.manufacturerPartNum);
		tfURL.setText(part.url);
		if(part.materialAmount!=null)
			tfMaterialAmount.setText(""+part.materialAmount);
		int i=0;
		for(String n:mapMaterialUnitFWD.keySet())
			{
			if(mapMaterialUnitFWD.get(n).equals(part.materialUnit))
				comboQuantityUnit.setCurrentIndex(i);
			i++;
			}
		}
	
	
	public void editvalues()
		{
		storevalues();
		sigNameChanged.emit(this);
		}
	
	public void storevalues()
		{
		part.description=tfDescription.text();
		part.supplier=tfSupplier.text();
		part.supplierPartNum=tfSupplierPartNum.text();
		part.manufacturerPartNum=tfManufacturerPartNum.text();
		part.url=tfURL.text();
		part.manufacturingMethod=comboManufacturingMethod.currentText();
		
		String ma=tfMaterialAmount.text();
		part.materialAmount=null;
		try
			{
			part.materialAmount=Double.parseDouble(ma);
			}
		catch (NumberFormatException e)
			{
			}
		part.materialUnit=mapMaterialUnitFWD.get(comboQuantityUnit.currentText());
		wInstruction.storevalues();
		}
		


	
	}
