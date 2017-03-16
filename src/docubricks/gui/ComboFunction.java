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
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Function;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;


/**
 * Widget to select one function
 * 
 * @author Johan Henriksson
 *
 */
public class ComboFunction extends QWidget //later on it will be a more complex type. including delete!
	{
	public final DocubricksProject project;
	public final Function logpart;
	public final Signal1<ComboFunction> sigUpdated=new Signal1<ComboFunction>();
	public final Signal1<ComboFunction> sigDeleted=new Signal1<ComboFunction>();


	private final QComboBox thecombo=new QComboBox(){
		public void wheelEvent(QWheelEvent event)
			{
//		  if (!hasFocus())
		      event.ignore();
//		  else
//		      super.wheelEvent(event);
		  
			}	
	};
	private final QPushButton bDelete=new QPushButton(new QIcon(ImgResource.delete),"");  //Could also be a special entry in list. but non-standard
	private final Brick brick;
	
	public ComboFunction(DocubricksProject project, Brick brick, Function currentFunction)
		{
		this.project=project;
		this.brick=brick;
		this.logpart=currentFunction;
		thecombo.setSizePolicy(Policy.Expanding, Policy.Expanding);
		bDelete.setSizePolicy(Policy.Minimum, Policy.Expanding);
		QHBoxLayout lay=new QHBoxLayout();
		lay.addWidget(thecombo);
		lay.addWidget(bDelete);
		setLayout(lay);
		lay.setMargin(0);
		
		updateListOfEntries(currentFunction);	
		
		thecombo.currentIndexChanged.connect(this,"actionChanged()");
		bDelete.clicked.connect(this,"actionDelete()");
		
		thecombo.setFocusPolicy(FocusPolicy.NoFocus);
	//	thecombo.installEventFilter(this);
		}
	
	


	
	public Function getCurrentData()
		{
		return (Function)thecombo.itemData(thecombo.currentIndex());
		}
	
	
	
	public void updateListOfEntries(Function curdata)
		{
		thecombo.clear();
		
		//Empty selection
		thecombo.addItem("",null);
		
		//All functions
		for(Function f:brick.functions)
			{
			thecombo.addItem(""+f.getRepresentativeName(project), f);
			if(curdata!=null && curdata==f)
				thecombo.setCurrentIndex(thecombo.count()-1);
			}
		}
	
	public void updateListOfEntries()
		{
		Function curdata=(Function)thecombo.itemData(thecombo.currentIndex());
		updateListOfEntries(curdata);
		}
	
	
	public void actionDelete()
		{
		sigDeleted.emit(this);
		}

	public void actionChanged()
		{
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
