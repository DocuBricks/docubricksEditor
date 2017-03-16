package docubricks.gui;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QAbstractSpinBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Function;
import docubricks.data.FunctionImplementation;
import docubricks.data.FunctionImplementationPart;
import docubricks.data.FunctionImplementationBrick;
import docubricks.data.Part;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;


/**
 * Widget to select one implementation of a logical unit
 * 
 * @author Johan Henriksson
 *
 */
public class ComboImplementingPart extends QWidget //later on it will be a more complex type. including delete!
	{
	public DocubricksProject project;
	public Function logpart;
	public Signal1<ComboImplementingPart> sigUpdated=new Signal1<ComboImplementingPart>();
	public Signal1<ComboImplementingPart> sigDeleted=new Signal1<ComboImplementingPart>();

	QSpinBox spQuantity=new QSpinBox();

	QComboBox thecombo=new QComboBox();
	QPushButton bDelete=new QPushButton(new QIcon(ImgResource.delete),"");  //Could also be a special entry in list. but non-standard
	
	public ComboImplementingPart(DocubricksProject project, Function logpart, FunctionImplementation imp)
		{
		this.project=project;
		this.logpart=logpart;
		thecombo.setSizePolicy(Policy.Expanding, Policy.Expanding);
		bDelete.setSizePolicy(Policy.Minimum, Policy.Expanding);
		QHBoxLayout lay=new QHBoxLayout();
		lay.addWidget(thecombo);
		lay.addWidget(spQuantity);
		lay.addWidget(bDelete);
		setLayout(lay);
		lay.setMargin(0);
		
		spQuantity.setRange(0, 1000);
		spQuantity.setSingleStep(1);
		spQuantity.setValue(imp.quantity);
		
		updateListOfEntries(imp);	

		spQuantity.valueChanged.connect(this,"actionChanged()");
		thecombo.currentIndexChanged.connect(this,"actionChanged()");
		bDelete.clicked.connect(this,"actionDelete()");
		
		thecombo.setFocusPolicy(FocusPolicy.NoFocus);
//		thecombo.installEventFilter(this);
		}
	
	


	
	public FunctionImplementation getCurrentData()
		{
		FunctionImplementation i=(FunctionImplementation)thecombo.itemData(thecombo.currentIndex());
		i.quantity=spQuantity.value(); //this is such a hack
		return i;
		}
	
	
	
	boolean ischanging=false;
	public void updateListOfEntries(FunctionImplementation curdata)
		{
		ischanging=true;
		thecombo.clear();
		
		//Empty selection
		thecombo.addItem("",null);
		
		//All units
		for(Brick u:project.bricks)
			{
			FunctionImplementationBrick ref=new FunctionImplementationBrick(u.id,1);
			thecombo.addItem(ref.getRepresentativeName(project), ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}
		
		//All parts
		for(Part p:project.parts)
			{
			FunctionImplementationPart ref=new FunctionImplementationPart(p,1);
			thecombo.addItem(ref.getRepresentativeName(project), ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}		
		ischanging=false;
		}
	
	public void updateListOfEntries()
		{
		FunctionImplementation curdata=(FunctionImplementation)thecombo.itemData(thecombo.currentIndex());
		updateListOfEntries(curdata);
		}
	
	
	public void actionDelete()
		{
		if(!ischanging)
			sigDeleted.emit(this);
		}

	public void actionChanged()
		{
		if(!ischanging)
			sigUpdated.emit(this);
		}	
	
	
	
	////////// For ignoring 
	public void focusInEvent(QFocusEvent ev)
		{
		setFocusPolicy(Qt.FocusPolicy.WheelFocus);
		}
	public void focusOutEvent(QFocusEvent ev)
		{
		setFocusPolicy(Qt.FocusPolicy.StrongFocus);
		}
	public boolean eventFilter(QObject o, QEvent e )
		{
	  if(e.type() == QEvent.Type.Wheel && o instanceof QAbstractSpinBox)
	  	{
	  	e.ignore();
      return true;
	  	}
	  else
	  	return super.eventFilter( o, e );
		}

	}
