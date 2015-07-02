package docubricks.gui;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFileDialog.AcceptMode;
import com.trolltech.qt.gui.QFileDialog.FileMode;
import com.trolltech.qt.gui.QDesktopServices;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Unit;
import docubricks.data.UsefulSourceProject;
import docubricks.gui.qt.QTutil;
import docubricks.gui.resource.ImgResource;

/**
 * 
 * The main window
 * 
 * @author Johan Henriksson
 *
 */
public class MainWindow extends QMainWindow
	{
	public UsefulSourceProject project=new UsefulSourceProject();

	public static File lastDirectory=new File(".");

	private PaneTree tree;
	private QMenuBar menubar=new QMenuBar();
	private TabProject tabProject;
	private QPushButton bAddUnit=new QPushButton(tr("New unit"));	
	private QHBoxLayout laytab=new QHBoxLayout();
	
	private HashMap<Unit, QWidget> mapUnitTab=new HashMap<Unit, QWidget>();
	
	private File currentProjectFile=null;
	

	/**
	 * Constructor
	 */
	public MainWindow()
		{
		try
			{
			InputStream styleStream=MainWindow.class.getResourceAsStream("style.css");
			String stylesheet=LabnoteUtil.readStreamToString(styleStream);
			styleStream.close();
			setStyleSheet(stylesheet);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}

		
		setMenuBar(menubar);

		ImgResource.setWindowIcon(this);
		
		
		QMenu mFile=menubar.addMenu(tr("File"));
		mFile.addAction(tr("New project"), this, "actionNewProject()");
		mFile.addAction(tr("Open project"), this, "actionOpenProject()");
		mFile.addAction(tr("Save project"), this, "actionSaveProject()");
		mFile.addAction(tr("Save project as"), this, "actionSaveProjectAs()");
		mFile.addSeparator();
		mFile.addAction(tr("Exit"), this, "close()");
		menubar.addSeparator();
		QMenu mHelp=menubar.addMenu(tr("Help"));
		mHelp.addAction(tr("About"), this, "actionAbout()");
		mHelp.addAction(tr("Website"), this, "actionWebsite()");

		tree=new PaneTree();
		tree.sigSel.connect(this,"actionSelTab(Unit)");
		tree.setSizePolicy(Policy.Fixed, Policy.Expanding);
		
		laytab.setMargin(0);
		QVBoxLayout layleft=new QVBoxLayout();
		layleft.addWidget(tree);
		layleft.addWidget(bAddUnit);
		layleft.setMargin(0);
		
		QHBoxLayout laytot=new QHBoxLayout();
		laytot.addLayout(layleft);
		laytot.addLayout(laytab);
		
		bAddUnit.clicked.connect(this,"actionNewUnit()");
		
		QWidget cent=new QWidget();
		cent.setLayout(laytot);
		setCentralWidget(cent);

		setProject(new UsefulSourceProject());
		actionNewUnit();
		
		setMinimumSize(800, 480);
		show();	
		}
	
	/**
	 * Action: one tab was selected
	 */
	public void actionSelTab(Unit u)
		{
		//hide all tabs
		for(QWidget t:mapUnitTab.values())
			t.setVisible(false);
		
		if(u==null)
			tabProject.setVisible(true);
		else
			mapUnitTab.get(u).setVisible(true);
		}
	

	/**
	 * Action: Make a new unit
	 */
	public void actionNewUnit()
		{
		Unit nu=project.createUnit();
		nu.setName("Unnamed");
		addUnitTab(nu);
		actionSelTab(nu);
		}
	
	
	/**
	 * Add a tab for given unit
	 */
	private void addUnitTab(final Unit nu)
		{
		TabUnit tabUnit=new TabUnit(project, nu);
		tabUnit.sigNameChanged.connect(this,"cbNameChanged(TabUnit)");
		tabUnit.sigRemove.connect(this,"cbRemoved(TabUnit)");
		laytab.addWidget(tabUnit);
		tabUnit.setVisible(false);
		mapUnitTab.put(nu, tabUnit);
		tabUnit.actionNameChanged();
		}


	/**
	 * Callback: name of a unit changed
	 */
	public void cbNameChanged(TabUnit u)
		{
		tree.setProject(project);
		}

	/**
	 * Callback: unit was removed
	 */
	public void cbRemoved(TabUnit nu)
		{
		nu.setVisible(false);
		tabProject.setVisible(true);
		mapUnitTab.remove(nu.unit);
		project.units.remove(nu.unit);
		tree.setProject(project);
		}

	
	/**
	 * TODO not tested
	 */
	private TabUnit getUnitTab(Unit u)  
		{
		return (TabUnit)mapUnitTab.get(u);
		}
	
	
	
	
	
	/**
	 * Action: New project
	 */
	public void actionNewProject()
		{
		setProject(new UsefulSourceProject());
		}
	
	
	/**
	 * Open a project
	 */
	public void actionOpenProject()
		{
		QFileDialog dia=new QFileDialog();
		dia.setFileMode(FileMode.ExistingFile);
		dia.setDirectory(lastDirectory.getAbsolutePath());
		dia.setNameFilter(tr("Project files")+" (*.docubricks.xml)");
		if(dia.exec()!=0)
			{
			File f=new File(dia.selectedFiles().get(0));
			lastDirectory=f.getParentFile();
			
			try
				{
				setProject(UsefulSourceProject.loadXML(f));
				currentProjectFile=f;
				}
			catch (IOException e)
				{
				QTutil.showNotice(this, e.getMessage());
				e.printStackTrace();
				}
			}		
		}
	
	
	private void setProject(UsefulSourceProject proj)
		{
		this.project=proj;

		//Remove all tabs
		for(QWidget t:mapUnitTab.values())
			{
			t.setVisible(false);
			laytab.removeWidget(t);
			}
		mapUnitTab.clear();

		//Update project tab
		tabProject=new TabProject(project);
		//tabwidget.addTab(tabProject, tr("Project"));
		mapUnitTab.put(null, tabProject);
		laytab.addWidget(tabProject);
		//tabProject.setVisible(false);
		tabProject.signalUpdated.connect(this,"updatedvalues()");
		tree.setProject(project);
		
		//Add all new unit tabs
		for(Unit u:project.units)
			addUnitTab(u);
		}
	
	
	
	public void updatedvalues()
		{
		for(QWidget t:mapUnitTab.values())
			if(t instanceof TabUnit)
				((TabUnit)t).updateAllCombos();
		tree.setProject(project);
		}
	
	
	
	/**
	 * Action: Save project
	 */
	public void actionSaveProject()
		{
		if(currentProjectFile==null)
			actionSaveProjectAs();
		if(currentProjectFile!=null)
			{
			//Serialize everything
			tabProject.storevalues();
			for(Unit u:project.units)
				{
				TabUnit tu=getUnitTab(u);
				tu.storevalues();
				}
			
			//Write to disk
			try
				{
				project.storeXML(currentProjectFile);
				}
			catch (IOException e)
				{
				QTutil.showNotice(this, e.getMessage());
				e.printStackTrace();
				}
			}
		}

	
	/**
	 * Action: Save as... file
	 */
	public void actionSaveProjectAs()
		{
		QFileDialog dia=new QFileDialog();
		dia.setFileMode(FileMode.AnyFile);
		dia.setAcceptMode(AcceptMode.AcceptSave);
		dia.setDirectory(lastDirectory.getAbsolutePath());
		dia.setDefaultSuffix("docubricks.xml");
		dia.setNameFilter(tr("Project files")+" (*.docubricks.xml)");
		if(dia.exec()!=0)
			{
			File f=new File(dia.selectedFiles().get(0));
			lastDirectory=f.getParentFile();
			currentProjectFile=f;
			actionSaveProject();
			}
		}

	
		
	

	
	/**
	 * Entry point
	 */
	public static void main(String[] args)
		{
		QApplication.initialize(QtProgramInfo.programName, args);
		QCoreApplication.setApplicationName(QtProgramInfo.programName);
		new MainWindow();
		QTutil.execStaticQApplication();		
		}


	
	/**
	 * Show About-information
	 */
	public void actionAbout()
		{
		new DialogAbout().exec();
		}
	
	/**
	 * Open up website
	 */
	public void actionWebsite()
		{
		QDesktopServices.openUrl(new QUrl("http://www.facsanadu.org"));
		}
	}
