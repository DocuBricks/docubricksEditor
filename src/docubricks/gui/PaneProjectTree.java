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
import docubricks.data.PhysicalPart;

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
	public Signal2<TreeSelection,Object> sigSel=new Signal2<TreeSelection,Object>();

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
		
		itemAuthors=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Authors"}));

		//Place physical parts
		QTreeWidgetItem itemPhy=new QTreeWidgetItem(this, Arrays.asList(new String[]{"Bill of materials"}));
		for(PhysicalPart p:project.physicalParts)
			{
			QTreeWidgetItem item=new QTreeWidgetItem(itemPhy, Arrays.asList(new String[]{p.name}));
			//itemThis=new QTreeWidgetItem(this, Arrays.asList(new String[]{nodeName}));
			item.setData(0, Qt.ItemDataRole.UserRole, p);
				
			}
		
		//Place units as a tree
		HashSet<Brick> placedUnitsTotal=new HashSet<Brick>();
		HashSet<Brick> placedUnitsDepth=new HashSet<Brick>();
		for(Brick u:project.getRootUnits())
			setProjectRecursive(project, placedUnitsTotal, placedUnitsDepth, u, null);
		//Place remaining units (circular!) arbitrarily
		for(Brick u:project.bricks)
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
		String nodeName=tr("Brick: ")+toplace.getName();
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
			for(Function lp:toplace.functions)
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
			else
				{
				Object o=item.data(0, Qt.ItemDataRole.UserRole);
				if(o instanceof PhysicalPart)
					{
					PhysicalPart u=(PhysicalPart)o;
					sigSel.emit(TreeSelection.PHYS,u);
					}
				else if(o instanceof Brick)
					{
					Brick u=(Brick)o;
					sigSel.emit(TreeSelection.BRICK, u);
					}
				}

			}
		}
	
	}
