package docubricks.gui;

import java.util.Arrays;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import docubricks.data.Unit;
import docubricks.data.DocubricksProject;

/**
 * 
 * Tree view of the project
 * 
 * @author Johan Henriksson
 *
 */
public class PaneProjectTree extends QTreeWidget
	{
	public DocubricksProject project=new DocubricksProject();
	public Signal2<TreeSelection,Unit> sigSel=new Signal2<TreeSelection,Unit>();
	
	public PaneProjectTree()
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

	QTreeWidgetItem itemPhy;
	QTreeWidgetItem itemAuthors;
	
	public void setProject(DocubricksProject project)
		{
		this.project=project;
		clear();
		
		itemPhy=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Physical parts"}));
		itemAuthors=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Authors"}));

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
			if(item==itemAuthors)
				{
				sigSel.emit(TreeSelection.AUTHORS,null);
				}
			else if(item==itemPhy)
				{
				sigSel.emit(TreeSelection.PHYS,null);
				}
			else
				{
				Unit u=(Unit)item.data(0, Qt.ItemDataRole.UserRole);
				sigSel.emit(TreeSelection.UNIT, u);
				}

			}
		}
	
	}
