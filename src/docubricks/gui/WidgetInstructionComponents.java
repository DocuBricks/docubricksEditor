package docubricks.gui;

import java.util.LinkedList;

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;

import docubricks.data.AssemblyStep;
import docubricks.data.AssemblyStepComponent;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;

/**
 * 
 * Pane for list of step components
 * 
 * @author Johan Henriksson
 *
 */
public class WidgetInstructionComponents extends QGridLayout
	{
	private final DocubricksProject proj;
	private final Brick unit;
	private final AssemblyStep asmstep;
	
//	private LinkedList<AssemblyStepComponent> mapImplementationPanes=new LinkedList<AssemblyStepComponent>();
	private LinkedList<OneComponentRef> listWidget=new LinkedList<WidgetInstructionComponents.OneComponentRef>();
	
	
	
	
	private void dolayout()
		{
		int row=0;
		addWidget(new QLabel(tr("Quantity:")),row,0);
		addWidget(new QLabel(tr("Function:")),row,1);
		for(int i=0;i<listWidget.size();i++)
			{
			OneComponentRef r=listWidget.get(i);
			row=i+1;
			addWidget(r.tfQuantity,row,0);
			addWidget(r.comboFunction,row,1);
			}
		}
	
	
	public class OneComponentRef
		{
		private ComboFunction comboFunction;
		private QLineEdit tfQuantity=new QLineEdit("1");
		private AssemblyStepComponent comp;

		public OneComponentRef(AssemblyStepComponent comp)
			{
			this.comp=comp;
			comboFunction=new ComboFunction(proj, unit, comp.function);
			
			updateListOfEntries();
			comboFunction.sigUpdated.connect(this,"actionChangePart(ComboFunction)");
			comboFunction.sigDeleted.connect(this,"actionDeletePart(ComboFunction)");			

			tfQuantity.textChanged.connect(this,"storeValues()");
			}
		
		/**
		 * Action: A part has changed
		 */
		public void actionChangePart(ComboFunction f)
			{
			int index=listWidget.indexOf(this);
			asmstep.components.get(index).function=comboFunction.getCurrentData();
			}
		
		
		
		/**
		 * Action: Delete implementation
		 */
		public void actionDeletePart(ComboFunction f)
			{
			int index=listWidget.indexOf(this);
			listWidget.remove(index);
			asmstep.components.remove(index);
			
			comboFunction.setVisible(false);
			tfQuantity.setVisible(false);
			removeWidget(comboFunction);
			removeWidget(tfQuantity);
			dolayout();
			}

		public void updateListOfEntries()
			{
			comboFunction.updateListOfEntries();
			}
		
		public void storeValues()
			{
			comp.quantity=Integer.parseInt(tfQuantity.text());
			comp.function=comboFunction.getCurrentData();
			}
		}
	
	
	/**
	 * Constructor for one logical part pane
	 */
	public WidgetInstructionComponents(DocubricksProject proj, Brick part, AssemblyStep asmstep)
		{
		this.proj=proj;
		this.unit=part;
		this.asmstep=asmstep;
		loadvalues();
		}

	/**
	 * Load values from object
	 */
	public void loadvalues()
		{
		for(AssemblyStepComponent imp:asmstep.components)
			addComponent(imp);
		}
	
	public void storeValues()
		{
		for(OneComponentRef c:listWidget)
			c.storeValues();
		}
	

	/**
	 * Action: Add a new implementation
	 */
	public void actionAddComponent()
		{
		AssemblyStepComponent c=new AssemblyStepComponent();
		asmstep.components.add(c);
		addComponent(c);
		}
	
	
	private void addComponent(AssemblyStepComponent imp)
		{
		addComponentWidget(imp);
		}
	private void addComponentWidget(AssemblyStepComponent imp)
		{
		OneComponentRef r=new OneComponentRef(imp);
		listWidget.add(r);
		//sigChanged.emit();
		dolayout();
		}
	

	/**
	 * Update entries in all the combos
	 */
	public void updateAllCombos()
		{		
		for(OneComponentRef p:listWidget)
			p.updateListOfEntries();
		}

	}
