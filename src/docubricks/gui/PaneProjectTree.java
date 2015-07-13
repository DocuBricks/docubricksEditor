package docubricks.gui;

import java.util.Arrays;
import java.util.HashSet;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import docubricks.data.LogicalPart;
import docubricks.data.LogicalPartImplementation;
import docubricks.data.LogicalPartImplementationUnit;
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

	private QTreeWidgetItem itemPhy;
	private QTreeWidgetItem itemAuthors;

	public PaneProjectTree()
		{
		header().setVisible(false);
		selectionModel().selectionChanged.connect(this,"actionSelected()");
		}
	
	
	
	public void setProject(DocubricksProject project)
		{
		this.project=project;
		clear();
		
		itemPhy=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Physical parts"}));
		itemAuthors=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Authors"}));

		//Place units as a tree
		HashSet<Unit> placedUnitsTotal=new HashSet<Unit>();
		HashSet<Unit> placedUnitsDepth=new HashSet<Unit>();
		for(Unit u:project.getRootUnits())
			setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, u, null);
		//Place remaining units (circular!) arbitrarily
		for(Unit u:project.units)
			if(!placedUnitsTotal.contains(u))
				setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, u, null);
		expandAll();
		}
	
	
	/**
	 * Build tree recursively
	 */
	private void setProjectRecursive(DocubricksProject project, HashSet<Unit> placedUnitsTotal, HashSet<Unit> placedUnitsDepth, Unit toplace, QTreeWidgetItem itemParent)
		{
		String nodeName=tr("Unit: ")+toplace.getName();
		QTreeWidgetItem itemThis;
		if(itemParent==null)
			itemThis=new QTreeWidgetItem(this, Arrays.asList(new String[]{nodeName}));
		else
			itemThis=new QTreeWidgetItem(itemParent, Arrays.asList(new String[]{nodeName}));
		itemThis.setData(0, Qt.ItemDataRole.UserRole, toplace);
		
		//Add children
		if(!placedUnitsDepth.contains(toplace))
			{
			placedUnitsDepth.add(toplace);
			for(LogicalPart lp:toplace.logicalParts)
				for(LogicalPartImplementation imp:lp.implementingPart)
					if(imp instanceof LogicalPartImplementationUnit)
						setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, ((LogicalPartImplementationUnit)imp).get(project), itemThis);
			placedUnitsDepth.remove(toplace);
			}
		placedUnitsTotal.add(toplace);
		}
	
	
	/**
	 * Action: on selection changed
	 */
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
