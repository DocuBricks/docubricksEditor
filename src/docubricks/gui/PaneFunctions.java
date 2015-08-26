package docubricks.gui;

import java.util.HashMap;
import java.util.LinkedList;

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;

import docubricks.data.Function;
import docubricks.data.FunctionImplementation;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * Pane for one logical part
 * 
 * @author Johan Henriksson
 *
 */
public class PaneFunctions extends QVBoxLayout
	{
	private DocubricksProject proj;
	private Brick unit;
	
	private HeaderLabel header=new HeaderLabel(tr("Functions"));
	private QPushButton bAdd=new QPushButton(tr("Add function"));  //or use header? no. inconsistent
	private HashMap<Function, PaneOneLogicalPart> mapLogPanes=new HashMap<Function, PaneFunctions.PaneOneLogicalPart>();
	
	private QVBoxLayout laylistLogicalPart=new QVBoxLayout();
	public Signal0 sigChanged=new Signal0();


	
	/**
	 * Constructor for list of logical parts
	 */
	public PaneFunctions(DocubricksProject proj, Brick unit)
		{
		this.proj=proj;
		this.unit=unit;
//		this.part=part;
		
		addWidget(header);
		addLayout(laylistLogicalPart);
		addWidget(bAdd);
		
		laylistLogicalPart.setMargin(0);
		setMargin(0);
		
		loadvalues();
		
		bAdd.clicked.connect(this,"actionAddPart()");
		}
	
	
	/**
	 * Add new logical part
	 */
	public void actionAddPart()
		{
		addPartWidget(unit.createLogicalPart());
		}
	
	/**
	 * Add a logical part
	 */
	public void addPartWidget(Function part)
		{
		PaneOneLogicalPart onepane=new PaneOneLogicalPart(part);
		mapLogPanes.put(part, onepane);
		laylistLogicalPart.addWidget(onepane);
		onepane.loadvalues();
		sigChanged.emit();
		}
	

	/**
	 * Update the available entries in all combos
	 */
	public void updateAllCombos()
		{
		for(PaneOneLogicalPart p:mapLogPanes.values())
			p.updateAllCombos();
		}

	
	
	
	/**
	 * Load values from object
	 */
	public void loadvalues()
		{
		for(Function p:unit.logicalParts)
			addPartWidget(p);
		}
	
	
	
	
	
	
	
	
	
	/**
	 * Constructor for one logical part
	 */
	public class PaneOneLogicalPart extends QGroupBox
		{
		private Function part;
		
		private QLineEdit tfDescription=new QLineEdit();
		private QLineEdit tfDesignator=new QLineEdit();
		private QLineEdit tfQuantity=new QLineEdit();
		private QPushButton bRemoveLogPart=new QPushButton(new QIcon(ImgResource.delete),"");
		private QPushButton bAddImplementation=new QPushButton(tr("Add implementation"));
		private QVBoxLayout lay=new QVBoxLayout();
		private QHBoxLayout laybuttons=new QHBoxLayout();
		private QVBoxLayout laylistImp=new QVBoxLayout();
		private PaneMediaSet mediapane;

		private LinkedList<ComboImplementingPart> mapImplementationPanes=new LinkedList<ComboImplementingPart>();

		
		/**
		 * Constructor for one logical part pane
		 */
		public PaneOneLogicalPart(Function part)
			{
			this.part=part;
			
			setLayout(lay);
			
			QHBoxLayout layName=new QHBoxLayout();
			layName.addWidget(tfDescription);
			layName.addWidget(bRemoveLogPart);

			mediapane=new PaneMediaSet(part.media);
			
			QGridLayout layGrid=new QGridLayout();
			
			int row=0;
			layGrid.addWidget(new QLabel(tr("Name:")),row,0);
			layGrid.addLayout(layName,row,1);
			row++;
			layGrid.addWidget(new QLabel(tr("Designator:")),row,0);
			layGrid.addWidget(tfDesignator,row,1);
			row++;
			layGrid.addWidget(new QLabel(tr("Quantity:")),row,0);
			layGrid.addWidget(tfQuantity,row,1);
			row++;
			layGrid.addWidget(mediapane,row,0,1,2);
			row++;
			layGrid.addWidget(new QLabel(tr("Implementations:")),row,0);
			row++;
			
			
			laybuttons.addWidget(bAddImplementation);
			
			lay.addLayout(layGrid);
			lay.addLayout(laylistImp);
			lay.addLayout(laybuttons);

			bRemoveLogPart.clicked.connect(this,"actionRemoveLogPart()");
			bAddImplementation.clicked.connect(this,"actionAddImplementation()");
			
			tfDescription.textChanged.connect(this,"storeValues()");
			tfDesignator.textChanged.connect(this,"storeValues()");
			tfQuantity.textChanged.connect(this,"storeValues()");
			}

		/**
		 * Load values from object
		 */
		public void loadvalues()
			{
			tfDescription.setText(part.getDescription());
			tfDesignator.setText(part.designator);
			tfQuantity.setText(part.getQuantity());
			
			for(FunctionImplementation imp:part.implementingPart)
				addImplementationWidget(imp);
			}
		
		public void storeValues()
			{
			part.setDescription(tfDescription.text());
			part.designator=tfDesignator.text();
			part.setQuantity(tfQuantity.text());
			}
		
		
		/**
		 * Action: Remove this logical part
		 */
		public void actionRemoveLogPart()
			{
			mapLogPanes.remove(part);
			setVisible(false);
			unit.logicalParts.remove(part);
			}

		/**
		 * Action: Add a new implementation
		 */
		public void actionAddImplementation()
			{
			addImplementation(null);
			}
		
		private void addImplementation(FunctionImplementation imp)
			{
			part.implementingPart.add(imp);
			addImplementationWidget(imp);
			}
		private void addImplementationWidget(FunctionImplementation imp)
			{
			ComboImplementingPart combo=new ComboImplementingPart(proj,part,imp);
			mapImplementationPanes.add(combo);
			laylistImp.addWidget(combo);
			combo.updateListOfEntries();
			combo.sigUpdated.connect(this,"actionChangePart(ComboImplementingPart)");
			combo.sigDeleted.connect(this,"actionDeletePart(ComboImplementingPart)");			
			sigChanged.emit();
			}
		
		/**
		 * Action: A part has changed
		 */
		public void actionChangePart(ComboImplementingPart p)
			{
			int index=mapImplementationPanes.indexOf(p);
			part.implementingPart.set(index, p.getCurrentData());
			sigChanged.emit();
			}

		/**
		 * Action: Delete implementation
		 */
		public void actionDeletePart(ComboImplementingPart p)
			{
			int index=mapImplementationPanes.indexOf(p);
			mapImplementationPanes.remove(p);
			part.implementingPart.remove(index);
			p.setVisible(false);
			laylistImp.removeWidget(p);
			sigChanged.emit();
			}

		/**
		 * Update entries in all the combos
		 */
		public void updateAllCombos()
			{
			for(ComboImplementingPart p:mapImplementationPanes)
				p.updateListOfEntries();
			}
		}

	}
