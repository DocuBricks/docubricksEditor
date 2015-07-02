package docubricks.gui;

import java.util.HashMap;
import java.util.TreeMap;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;

import docubricks.data.MaterialUnit;
import docubricks.data.PhysicalPart;
import docubricks.data.UsefulSourceProject;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * Pane for one logical part
 * 
 * @author Johan Henriksson
 *
 */
public class PanePhysicalParts extends QVBoxLayout
	{
	private UsefulSourceProject proj;
	
	public Signal0 signalUpdated=new Signal0();
	
	private HeaderLabel header=new HeaderLabel(tr("Physical parts"));
	private QPushButton bAdd=new QPushButton(tr("Add part"));  //or use header? no. inconsistent
	private HashMap<PhysicalPart, PaneOnePhysicalPart> mapPanes=new HashMap<PhysicalPart, PanePhysicalParts.PaneOnePhysicalPart>();
	
	private QVBoxLayout laylist=new QVBoxLayout();

	/**
	 * Constructor for one logical part
	 *
	 */
	public class PaneOnePhysicalPart extends QGroupBox
		{
		private QLineEdit tfDescription=new QLineEdit();
		private QLineEdit tfSupplier=new QLineEdit();
		private QLineEdit tfSupplierPartNum=new QLineEdit();
		private QLineEdit tfManufacturerPartNum=new QLineEdit();
		private QLineEdit tfURL=new QLineEdit();

		private QLineEdit tfMaterialAmount=new QLineEdit();
		private QComboBox comboQuantityUnit=new QComboBox();
		
		private PhysicalPart part;
		private QPushButton bRemovePart=new QPushButton(new QIcon(ImgResource.delete),"");
		private QVBoxLayout lay=new QVBoxLayout();
		private QHBoxLayout laybuttons=new QHBoxLayout();

		private MediaSetPane mediapane;
		
		private TreeMap<String, MaterialUnit> mapMaterialUnitFWD=new TreeMap<String, MaterialUnit>();
		
		/**
		 * Constructor for one logical part pane
		 */
		public PaneOnePhysicalPart(PhysicalPart part)
			{
			this.part=part;
			
			mapMaterialUnitFWD.put(tr("<none>"), MaterialUnit.NONE);
			mapMaterialUnitFWD.put(tr("kg"), MaterialUnit.KG);
			mapMaterialUnitFWD.put(tr("litres"), MaterialUnit.LITRES);
			for(String n:mapMaterialUnitFWD.keySet())
				{
				comboQuantityUnit.addItem(n);
//				if(mapMaterialUnit.get(n).equals(part.materialUnit))
//					comboQuantityUnit.setCurrentIndex(comboQuantityUnit.count()-1);
				}
			
			
			
			setLayout(lay);
			
			mediapane=new MediaSetPane(part.media);
		
			
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
			
			
			lay.addLayout(layGrid);
			lay.addLayout(laybuttons);
			
			loadvalues();

			tfDescription.textEdited.connect(this,"storevalues()");
			bRemovePart.clicked.connect(this,"actionRemovePhysPart()");
			}
		
		/**
		 * Remove this physical part
		 */
		public void actionRemovePhysPart()
			{
			mapPanes.remove(part);
			setVisible(false);
			proj.physicalParts.remove(part);
			signalUpdated.emit();
			}
		
		public void loadvalues()
			{
			tfDescription.setText(part.description);
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
			//mediapane.loadvalues(part.media);
			}
		
		
		public void storevalues()
			{
			part.description=tfDescription.text();
			part.supplier=tfSupplier.text();
			part.supplierPartNum=tfSupplierPartNum.text();
			part.manufacturerPartNum=tfManufacturerPartNum.text();
			part.url=tfURL.text();
			
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
			signalUpdated.emit();
			}
		
		}

	
	/**
	 * Constructor for list of logical parts
	 */
	public PanePhysicalParts(UsefulSourceProject proj)
		{
		this.proj=proj;
//		this.part=part;
		
		addWidget(header);
		addLayout(laylist);
		addWidget(bAdd);
		
		loadvalues();
		
		bAdd.clicked.connect(this,"actionAddPart()");
		}
	
	
	public void loadvalues()
		{
		for(PhysicalPart p:proj.physicalParts)
			addPart(p);
		}
	
	public void storevalues()
		{
		for(PaneOnePhysicalPart p:mapPanes.values())
			p.storevalues();
		}

	
	/**
	 * Add new logical part
	 */
	public void actionAddPart()
		{
		PhysicalPart p=proj.createPhysicalPart();
		addPart(p);
		}
	
	public void addPart(PhysicalPart part)
		{
		PaneOnePhysicalPart onepane=new PaneOnePhysicalPart(part);
		mapPanes.put(part, onepane);
		laylist.addWidget(onepane);
		signalUpdated.emit();
		}


	
	}
