package docubricks.gui;

import java.util.Arrays;
import java.util.HashSet;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import docubricks.data.Function;
import docubricks.data.FunctionImplementation;
import docubricks.data.FunctionImplementationBrick;
import docubricks.data.Brick;
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
	public Signal2<TreeSelection,Brick> sigSel=new Signal2<TreeSelection,Brick>();

	private QTreeWidgetItem itemPhy;
	private QTreeWidgetItem itemAuthors;

	public PaneProjectTree()
		{
		header().setVisible(false);
		setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAsNeeded);
		selectionModel().selectionChanged.connect(this,"actionSelected()");
		setMinimumWidth(350);
		}
	
	
	
	public void setProject(DocubricksProject project)
		{
		this.project=project;
		clear();
		
		itemPhy=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Physical parts"}));
		itemAuthors=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Authors"}));

		//Place units as a tree
		HashSet<Brick> placedUnitsTotal=new HashSet<Brick>();
		HashSet<Brick> placedUnitsDepth=new HashSet<Brick>();
		for(Brick u:project.getRootUnits())
			setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, u, null);
		//Place remaining units (circular!) arbitrarily
		for(Brick u:project.units)
			if(!placedUnitsTotal.contains(u))
				setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, u, null);
		expandAll();
		resizeColumnToContents(0);
		//setMinimumWidth(header().width());
		}
	
	
	/**
	 * Build tree recursively
	 */
	private void setProjectRecursive(DocubricksProject project, HashSet<Brick> placedUnitsTotal, HashSet<Brick> placedUnitsDepth, Brick toplace, QTreeWidgetItem itemParent)
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
			for(Function lp:toplace.logicalParts)
				for(FunctionImplementation imp:lp.implementingPart)
					if(imp instanceof FunctionImplementationBrick)
						setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, ((FunctionImplementationBrick)imp).get(project), itemThis);
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
				Brick u=(Brick)item.data(0, Qt.ItemDataRole.UserRole);
				sigSel.emit(TreeSelection.BRICK, u);
				}

			}
		}
	
	}
