package docubricks.gui;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Author;
import docubricks.data.DocubricksProject;
import docubricks.gui.resource.ImgResource;


/**
 * Widget to select one author
 * 
 * @author Johan Henriksson
 *
 */
public class ComboAuthorRef extends QWidget //later on it will be a more complex type. including delete!
	{
	public DocubricksProject project;
	public Author author;
	//LogicalPart logpart;
	public Signal1<ComboAuthorRef> sigUpdated=new Signal1<ComboAuthorRef>();
	public Signal1<ComboAuthorRef> sigDeleted=new Signal1<ComboAuthorRef>();


	QComboBox thecombo=new QComboBox();
	QPushButton bDelete=new QPushButton(new QIcon(ImgResource.delete),"");  //Could also be a special entry in list. but non-standard
	
	public ComboAuthorRef(DocubricksProject project, Author author)
		{
		this.project=project;
		this.author=author;
		thecombo.setSizePolicy(Policy.Expanding, Policy.Expanding);
		bDelete.setSizePolicy(Policy.Minimum, Policy.Expanding);
		QHBoxLayout lay=new QHBoxLayout();
		lay.addWidget(thecombo);
		lay.addWidget(bDelete);
		setLayout(lay);
		lay.setMargin(0);
		
		updateListOfEntries(author);	
		
		thecombo.currentIndexChanged.connect(this,"actionChanged()");
		bDelete.clicked.connect(this,"actionDelete()");
		
		thecombo.setFocusPolicy(FocusPolicy.NoFocus);
		//thecombo.installEventFilter(this);
		}
	
	


	
	public Author getCurrentAuthor()
		{
		return (Author)thecombo.itemData(thecombo.currentIndex());
		}
	
	
	
	public void updateListOfEntries(Author curdata)
		{
		thecombo.clear();
		
		//Empty selection
		thecombo.addItem("",null);
		
		//All units
		System.out.println("whoo "+project.authors);
		for(Author u:project.authors)
			{
//			LogicalPartImplementationUnit ref=new LogicalPartImplementationUnit(u.id);
			thecombo.addItem("Author: "+u.name+" <"+u.email+">", u);
			if(curdata!=null && curdata==u)
				thecombo.setCurrentIndex(thecombo.count()-1);
			}
		
		}
	
	public void updateListOfEntries()
		{
		Author curdata=(Author)thecombo.itemData(thecombo.currentIndex());
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
	  if(e.type() == QEvent.Type.Wheel)
	  	{
	  	e.ignore();
      return true;
	  	}
	  else
	  	return super.eventFilter( o, e );
		}
	}
