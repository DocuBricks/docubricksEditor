package docubricks.gui;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import org.apache.commons.io.FileUtils;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFileDialog.AcceptMode;
import com.trolltech.qt.gui.QFileDialog.FileMode;
import com.trolltech.qt.gui.QDesktopServices;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.data.Part;
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
	public DocubricksProject project=new DocubricksProject();

	public static File lastDirectory=new File(".");

	private PaneProjectTree tree;
	private ListBrickOrder listOrder=new ListBrickOrder();
	private QMenuBar menubar=new QMenuBar();
	//private TabProject tabProject;
	private TabAuthors tabAuthors;
	private QPushButton bAddUnit=new QPushButton(tr("New brick"));	
	private QPushButton bAddPart=new QPushButton(tr("New part"));	
	private QHBoxLayout laytab=new QHBoxLayout();
	
	private LinkedList<QWidget> listTab=new LinkedList<QWidget>();
	private HashMap<Brick, QWidget> mapUnitTab=new HashMap<Brick, QWidget>();
	private HashMap<Part, QWidget> mapPartTab=new HashMap<Part, QWidget>();
	
	private File currentProjectFile=null;
	

	/**
	 * Constructor
	 */
	public MainWindow()
		{
		try
			{
			InputStream styleStream=MainWindow.class.getResourceAsStream("style.css");
			String stylesheet=IoUtil.readStreamToString(styleStream);
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
		//mFile.addAction(tr("Export ZIP for upload"), this, "actionZIP()");
		mFile.addSeparator();
		mFile.addAction(tr("Exit"), this, "close()");
		menubar.addSeparator();
		QMenu mHelp=menubar.addMenu(tr("Help"));
		mHelp.addAction(tr("About"), this, "actionAbout()");
		mHelp.addAction(tr("Website"), this, "actionWebsite()");

		tree=new PaneProjectTree();
		tree.sigSel.connect(this,"actionSelTab(TreeSelection,Object)");
		tree.setSizePolicy(Policy.Fixed, Policy.Expanding);
		
		laytab.setMargin(0);
		QVBoxLayout layleft=new QVBoxLayout();
		layleft.addWidget(bAddUnit);
		layleft.addWidget(bAddPart);
		layleft.addWidget(tree);
		layleft.addWidget(new QLabel(tr("Display order:")));
		layleft.addLayout(listOrder);
		layleft.setMargin(0);
		
		QHBoxLayout laytot=new QHBoxLayout();
		laytot.addLayout(layleft);
		laytot.addLayout(laytab);
		
		bAddUnit.clicked.connect(this,"actionNewUnit()");
		bAddPart.clicked.connect(this,"actionNewPart()");
		
		QWidget cent=new QWidget();
		cent.setLayout(laytot);
		setCentralWidget(cent);

		setProject(new DocubricksProject());
		actionNewUnit();
		
		setMinimumSize(1200, 480);
		show();	
		
		openMinimal();
		}
	
	private void openMinimal()
		{
		try
			{
			InputStream is=MainWindow.class.getResourceAsStream("minimal.docubricks.xml");
			setProject(DocubricksProject.loadXML(is, new File(".")));
			currentProjectFile=null;
			is.close();
			}
		catch (IOException e)
			{
			QTutil.showNotice(this, e.getMessage());
			e.printStackTrace();
			}
		}		
	

	
	/**
	 * Action: one tab was selected
	 */
	public void actionSelTab(TreeSelection sel, Object u)
		{
		//hide all tabs
		for(QWidget t:listTab)
			t.setVisible(false);
		
		if(sel==TreeSelection.AUTHORS)
			tabAuthors.setVisible(true);
		else if(u instanceof Brick)
			mapUnitTab.get(u).setVisible(true);
		if(sel==TreeSelection.PHYS)
			mapPartTab.get(u).setVisible(true);
		}
	

	/**
	 * Action: Make a new unit
	 */
	public void actionNewUnit()
		{
		Brick nu=project.createUnit();
		nu.setName("Unnamed");
		addUnitTab(nu);
		actionSelTab(TreeSelection.BRICK, nu);
		listOrder.updateContent();
		}

	public void actionNewPart()
		{
		Part nu=project.createPart();
		nu.name="Unnamed";
		addPartTab(nu);
		actionSelTab(TreeSelection.PHYS, nu);
		listOrder.updateContent();
		}

	
	/**
	 * Add a tab for given unit
	 */
	private void addUnitTab(final Brick nu)
		{
		TabBrick tabUnit=new TabBrick(project, nu);
		tabUnit.sigNameChanged.connect(this,"cbNameChanged(TabBrick)");
		tabUnit.sigRemove.connect(this,"cbRemoved(TabBrick)");
		laytab.addWidget(tabUnit);
		tabUnit.setVisible(false);
		mapUnitTab.put(nu, tabUnit);
		listTab.add(tabUnit);
		tabUnit.actionNameChanged();
		}


	/**
	 * Add a tab for given part
	 */
	private void addPartTab(final Part nu)
		{
		TabPart tabPart=new TabPart(project, nu);
		tabPart.sigNameChanged.connect(this,"cbNameChanged(TabPart)");
		tabPart.sigRemove.connect(this,"cbRemoved(TabPart)");
		laytab.addWidget(tabPart);
		tabPart.setVisible(false);
		mapPartTab.put(nu, tabPart);
		listTab.add(tabPart);
		tabPart.editvalues();
		}

	/**
	 * Callback: name of a unit changed
	 */
	public void cbNameChanged(TabBrick u)
		{
		tree.setProject(project);
		listOrder.updateContent();
		}
	public void cbNameChanged(TabPart u)
		{
		tree.setProject(project);
		listOrder.updateContent();
		}

	/**
	 * Callback: unit was removed
	 */
	public void cbRemoved(TabBrick nu)
		{
		nu.setVisible(false);
		//tabProject.setVisible(true);
		mapUnitTab.remove(nu.unit);
		listTab.remove(nu);
		project.removeBrick(nu.unit);
		tree.setProject(project);
		}
	public void cbRemoved(TabPart nu)
		{
		nu.setVisible(false);
		//tabProject.setVisible(true);
		mapPartTab.remove(nu.part);
		listTab.remove(nu);
		project.parts.remove(nu.part);
		tree.setProject(project);
		}

	
	/**
	 * TODO not tested
	 */
	private TabBrick getUnitTab(Brick u)  
		{
		return (TabBrick)mapUnitTab.get(u);
		}
	
	private TabPart getPartTab(Part u)  
		{
		return (TabPart)mapPartTab.get(u);
		}
	
	
	
	
	/**
	 * Action: New project
	 */
	public void actionNewProject()
		{
		setProject(new DocubricksProject());
		}
	
	
	public String fileExtension="docubricks.xml";
	
	/**
	 * Open a project
	 */
	public void actionOpenProject()
		{
		QFileDialog dia=new QFileDialog();
		dia.setFileMode(FileMode.ExistingFile);
		dia.setDirectory(lastDirectory.getAbsolutePath());
		dia.setNameFilter(tr("Project files")+" (*."+fileExtension+")");
		if(dia.exec()!=0)
			{
			File f=new File(dia.selectedFiles().get(0));
			lastDirectory=f.getParentFile();
			
			try
				{
				setProject(DocubricksProject.loadXML(f));
				System.out.println(project.toJSON(f.getParentFile()));
				currentProjectFile=f;
				}
			catch (IOException e)
				{
				QTutil.showNotice(this, e.getMessage());
				e.printStackTrace();
				}
			}		
		}
	
	
	private void setProject(DocubricksProject proj)
		{
		this.project=proj;

		//Remove all tabs
		for(QWidget t:listTab)
			{
			t.setVisible(false);
			laytab.removeWidget(t);
			}
		mapUnitTab.clear();
		listTab.clear();
		
		//Update project tab
		tabAuthors=new TabAuthors(proj);
		
		mapUnitTab.put(null, tabAuthors);
		listTab.add(tabAuthors);
		laytab.addWidget(tabAuthors);
		tabAuthors.setVisible(false);
		
		//tabProject.setVisible(false);
		//tabProject.signalUpdated.connect(this,"updatedvalues()");
		tabAuthors.signalUpdated.connect(this,"updatedvalues()");
		tree.setProject(project);
		listOrder.setProject(project);
		
		//Add all part tabs
		for(Part p:project.parts)
			addPartTab(p);
		//Add all new unit tabs
		for(Brick u:project.bricks)
			addUnitTab(u);
		}
	
	
	
	public void updatedvalues()
		{
		for(QWidget t:mapUnitTab.values())
			if(t instanceof TabBrick)
				((TabBrick)t).updateAllCombos();
		tree.setProject(project);
		listOrder.setProject(project);
		}
	
	
	
	/**
	 * Action: Save project
	 */
	public void actionSaveProject()
		{
		
		if(ImageCompressor.needCompression(project))
			{
			System.out.println("Compressing");
			ImageCompressor.compress(project);
			}
		
		
		
		if(currentProjectFile!=null)
			{
			//Serialize everything
			/*
			System.out.println("aaaa");
			tabProject.storevalues();*/
			tabAuthors.storevalues();
			for(Part u:project.parts)
				{
				TabPart tu=getPartTab(u);
				tu.storevalues();
				}
			for(Brick u:project.bricks)
				{
				TabBrick tu=getUnitTab(u);
				tu.storevalues();
				}
			
			//Write to disk
			try
				{
				project.storeXML(currentProjectFile);
				
				actionZIP();
				}
			catch (IOException e)
				{
				QTutil.showNotice(this, e.getMessage());
				e.printStackTrace();
				}
			}
		else
			actionSaveProjectAs();
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
		dia.setDefaultSuffix(fileExtension);
		dia.setNameFilter(tr("Project files")+" (*."+fileExtension+")");
		if(dia.exec()!=0)
			{
			File f=new File(dia.selectedFiles().get(0));
			lastDirectory=f.getParentFile();
			currentProjectFile=f;
			actionSaveProject();
			}
		}

	
	/**
	 * Store as a zip file
	 */
	public void actionZIP() throws IOException
		{
		File tempzip=new File(currentProjectFile.getParentFile(),currentProjectFile.getName()+".zip");
    
		
		ArrayList<File> refFiles=new ArrayList<File>(project.getReferencedFiles());
		refFiles.add(currentProjectFile);
		
    //Output file 
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempzip));
  	//zipDirectoryHelper(dir.getRoot(), dir.getRoot(), zos);

    File rootDirectory=currentProjectFile.getParentFile();
    for(File file:refFiles)
    	{
			FileInputStream fi = new FileInputStream(file);

			//creating structure and avoiding duplicate file names
			String name = file.getAbsolutePath().replace(rootDirectory.getAbsolutePath(), "");
			System.out.println(name);
			if(name.startsWith("/"))
				name=name.substring(1);
			while(name.startsWith("./"))
				name=name.substring(2);
			
			zos.putNextEntry(new ZipEntry(name));
			int count;
			BufferedInputStream origin = new BufferedInputStream(fi,2048);
			byte[] data = new byte[2048];
			while ((count = origin.read(data, 0 , 2048)) != -1)
				zos.write(data, 0, count);
			origin.close();
      zos.closeEntry();
    	}
    
    
    //TODO what about the XSLT? put it in too?
    zos.close();
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
		QDesktopServices.openUrl(new QUrl("http://www.docubricks.org"));  
		}
	}
