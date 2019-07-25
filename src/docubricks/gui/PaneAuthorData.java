package docubricks.gui;

import java.util.HashMap;

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;

import docubricks.data.Author;
import docubricks.data.DocubricksProject;
import docubricks.gui.qt.QLineEditTODO;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * Pane for one logical part
 * 
 * @author Johan Henriksson
 *
 */
public class PaneAuthorData extends QVBoxLayout
	{
	private DocubricksProject proj;
	
	public Signal0 signalUpdated=new Signal0();
	
	private HeaderLabel header=new HeaderLabel(tr("Authors"));
	private QPushButton bAdd=new QPushButton(tr("Add author"));  //or use header? no. inconsistent
	private HashMap<Author, PaneOneAuthor> mapPanes=new HashMap<Author, PaneAuthorData.PaneOneAuthor>();
	
	private QVBoxLayout laylist=new QVBoxLayout();

	/**
	 * Constructor for one logical part
	 *
	 */
	public class PaneOneAuthor extends QGroupBox
		{
		private QLineEditTODO tfName=new QLineEditTODO();
		private QLineEditTODO tfEmail=new QLineEditTODO();
		private QLineEditTODO tfAffiliation=new QLineEditTODO();
		private QLineEditTODO tfOrcid=new QLineEditTODO();

		
		private Author author;
		private QPushButton bRemovePart=new QPushButton(new QIcon(ImgResource.delete),"");
		private QVBoxLayout lay=new QVBoxLayout();
		private QHBoxLayout laybuttons=new QHBoxLayout();

		
		/**
		 * Constructor for one author pane
		 */
		public PaneOneAuthor(Author a)
			{
			this.author=a;
			
			
			setLayout(lay);
			
			QHBoxLayout layName=new QHBoxLayout();
			layName.addWidget(tfName);
			layName.addWidget(bRemovePart);

			QGridLayout layGrid=new QGridLayout();
			
			int row=0;
			layGrid.addWidget(new QLabel(tr("Name:")),row,0);
			layGrid.addLayout(layName,row,1);
			row++;
			layGrid.addWidget(new QLabel(tr("Email:")),row,0);
			layGrid.addWidget(tfEmail,row,1);
			row++;
			layGrid.addWidget(new QLabel(tr("Affiliation:")),row,0);
			layGrid.addWidget(tfAffiliation,row,1);
			row++;
			layGrid.addWidget(new QLabel(tr("ORCID:")),row,0);
			layGrid.addWidget(tfOrcid,row,1);
			row++;
			
			
			lay.addLayout(layGrid);
			lay.addLayout(laybuttons);
			
			loadvalues();

			tfName.textEdited.connect(this,"storevalues()");
			bRemovePart.clicked.connect(this,"actionRemoveAuthor()");
			}
		
		/**
		 * Remove this author
		 */
		public void actionRemoveAuthor()
			{
			mapPanes.remove(author);
			setVisible(false);
			proj.authors.remove(author);
			signalUpdated.emit();
			}
		
		public void loadvalues()
			{
			tfName.setText(author.name);
			tfEmail.setText(author.email);
			tfAffiliation.setText(author.affiliation);
			tfOrcid.setText(author.orcid);
			}
		
		
		public void storevalues()
			{
			author.name=tfName.text();
			author.email=tfEmail.text();
			author.affiliation=tfAffiliation.text();
			author.orcid=tfOrcid.text();
			signalUpdated.emit();
			}
		
		}

	
	/**
	 * Constructor for list of logical parts
	 */
	public PaneAuthorData(DocubricksProject proj)
		{
		this.proj=proj;
//		this.part=part;
		
		addWidget(header);
		addLayout(laylist);
		addWidget(bAdd, 1, AlignmentFlag.AlignRight);
		
		loadvalues();
		
		bAdd.clicked.connect(this,"actionAddAuthor()");
		}
	
	
	public void loadvalues()
		{
		for(Author p:proj.authors)
			addAuthor(p);
		}
	
	public void storevalues()
		{
		for(PaneOneAuthor p:mapPanes.values())
			p.storevalues();
		}

	
	/**
	 * Add new author
	 */
	public void actionAddAuthor()
		{
		Author p=proj.createAuthor();
		addAuthor(p);
		}
	
	public void addAuthor(Author part)
		{
		PaneOneAuthor onepane=new PaneOneAuthor(part);
		mapPanes.put(part, onepane);
		laylist.addWidget(onepane);
		signalUpdated.emit();
		}


	
	}
