package docubricks.gui;

import java.util.Arrays;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import docubricks.data.Unit;
import docubricks.data.UsefulSourceProject;

/**
 * 
 * Tree view of the project
 * 
 * @author Johan Henriksson
 *
 */
public class PaneTree extends QTreeWidget
	{
	public UsefulSourceProject project=new UsefulSourceProject();
	public Signal1<Unit> sigSel=new Signal1<Unit>();
	
	public PaneTree()
		{
		header().setVisible(false);
		/**
		 * 
		 * TODO: need a root-item. or? can possibly just detect it!
		 * 
		 * can make a best-effort at building a tree. if listing sub-items which are not proper, just don't recurse further into them!
		 * 
		 * find roots.
		 * find all leaves. 
		 * mark everything else as a root
		 * 
		 * recurse and add unless previously added (circular!)
		 */
		
		selectionModel().selectionChanged.connect(this,"actionSelected()");
		}

	
	
	public void setProject(UsefulSourceProject project)
		{
		this.project=project;
		clear();
		
		new QTreeWidgetItem(this, Arrays.asList(new String[]{"Physical parts"}));

		for(Unit u:project.units)
			{
			QTreeWidgetItem item=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Unit: "+u.getName()}));
			item.setData(0, Qt.ItemDataRole.UserRole, u);
			}
		}
	
	
	public void actionSelected()
		{
		for(QTreeWidgetItem item:selectedItems())
			{
			Unit u=(Unit)item.data(0, Qt.ItemDataRole.UserRole);
			//System.out.println("sel: "+u);
			sigSel.emit(u);
			}
		}
	
	}
