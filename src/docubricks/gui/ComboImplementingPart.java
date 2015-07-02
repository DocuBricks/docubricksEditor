package docubricks.gui;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.LogicalPart;
import docubricks.data.LogicalPartImplementation;
import docubricks.data.LogicalPartImplementationPhysical;
import docubricks.data.LogicalPartImplementationUnit;
import docubricks.data.PhysicalPart;
import docubricks.data.Unit;
import docubricks.data.UsefulSourceProject;
import docubricks.gui.resource.ImgResource;


/**
 * Widget to select one implementation of a logical unit
 * 
 * @author Johan Henriksson
 *
 */
public class ComboImplementingPart extends QWidget //later on it will be a more complex type. including delete!
	{
	public UsefulSourceProject project;
	public LogicalPart logpart;
	public Signal1<ComboImplementingPart> sigUpdated=new Signal1<ComboImplementingPart>();
	public Signal1<ComboImplementingPart> sigDeleted=new Signal1<ComboImplementingPart>();


	QComboBox thecombo=new QComboBox();
	QPushButton bDelete=new QPushButton(new QIcon(ImgResource.delete),"");  //Could also be a special entry in list. but non-standard
	
	public ComboImplementingPart(UsefulSourceProject project, LogicalPart logpart, LogicalPartImplementation imp)
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
		}
	
	


	
	public LogicalPartImplementation getCurrentData()
		{
		return (LogicalPartImplementation)thecombo.itemData(thecombo.currentIndex());
		}
	
	
	
	public void updateListOfEntries(LogicalPartImplementation curdata)
		{
		thecombo.clear();
		
		//Empty selection
		thecombo.addItem("",null);
		
		//All units
		for(Unit u:project.units)
			{
			LogicalPartImplementationUnit ref=new LogicalPartImplementationUnit(u.id);
			thecombo.addItem("Unit: "+u.getName(), ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}
		
		//All physical parts
		for(PhysicalPart p:project.physicalParts)
			{
			LogicalPartImplementationPhysical ref=new LogicalPartImplementationPhysical(p);
			thecombo.addItem("Part: "+p.description, ref);
			if(curdata!=null && curdata.equals(ref))
				thecombo.setCurrentIndex(thecombo.count()-1);
			}		
		}
	
	public void updateListOfEntries()
		{
		LogicalPartImplementation curdata=(LogicalPartImplementation)thecombo.itemData(thecombo.currentIndex());
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
