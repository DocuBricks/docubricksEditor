package docubricks.gui;

import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Function;
import docubricks.data.FunctionImplementation;
import docubricks.data.FunctionImplementationPhysical;
import docubricks.data.FunctionImplementationBrick;
import docubricks.data.PhysicalPart;
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
		lay.addWidget(bDelete);
		setLayout(lay);
		lay.setMargin(0);
		
		updateListOfEntries(imp);	
		
		thecombo.currentIndexChanged.connect(this,"actionChanged()");
		bDelete.clicked.connect(this,"actionDelete()");
		
		setFocusPolicy(FocusPolicy.StrongFocus);
		}
	
	


	
	public FunctionImplementation getCurrentData()
		{
		return (FunctionImplementation)thecombo.itemData(thecombo.currentIndex());
		}
	
	
	
	public void updateListOfEntries(FunctionImplementation curdata)
		{
		thecombo.clear();
		
		//Empty selection
		thecombo.addItem("",null);
		
		//All units
		for(Brick u:project.bricks)
			{
			FunctionImplementationBrick ref=new FunctionImplementationBrick(u.id);
			thecombo.addItem(ref.getRepresentativeName(project), ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}
		
		//All physical parts
		for(PhysicalPart p:project.physicalParts)
			{
			FunctionImplementationPhysical ref=new FunctionImplementationPhysical(p);
			thecombo.addItem(ref.getRepresentativeName(project), ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}		
		}
	
	public void updateListOfEntries()
		{
		FunctionImplementation curdata=(FunctionImplementation)thecombo.itemData(thecombo.currentIndex());
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
	}
