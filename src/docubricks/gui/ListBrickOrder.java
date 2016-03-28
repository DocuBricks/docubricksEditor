package docubricks.gui;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QVBoxLayout;

import docubricks.data.Brick;
import docubricks.data.DocubricksProject;

public class ListBrickOrder extends QVBoxLayout
	{
	private DocubricksProject proj=new DocubricksProject();
	private QListWidget wlist=new QListWidget();
	private QPushButton bUp=new QPushButton(tr("Up"));
	private QPushButton bDown=new QPushButton(tr("Down"));
	
	
	
	public ListBrickOrder()
		{
		QHBoxLayout lay=new QHBoxLayout();
		lay.addWidget(bDown);
		lay.addWidget(bUp);
		addWidget(wlist);
		addLayout(lay);
		wlist.setSizePolicy(Policy.Minimum, Policy.MinimumExpanding);
		bDown.setSizePolicy(Policy.Minimum, Policy.Minimum);
		bUp.setSizePolicy(Policy.Minimum, Policy.Minimum);
		
		//setSizePolicy(Policy.MinimumExpanding, Policy.MinimumExpanding);
		
		bDown.clicked.connect(this,"actionDown()");
		bUp.clicked.connect(this,"actionUp()");
		
		updateContent();
		}
	
	
	
	public void actionUp()
		{
		Brick b=getSelectedBrick();
		if(b!=null)
			{
			int ind=proj.bricks.indexOf(b);
			if(ind!=0)
				{
				proj.bricks.remove(ind);
				proj.bricks.add(ind-1, b);
				updateContent();  
				}
			}
		}
	public void actionDown()
		{
		Brick b=getSelectedBrick();
		if(b!=null)
			{
			int ind=proj.bricks.indexOf(b);
			if(ind!=proj.bricks.size()-1)
				{
				proj.bricks.remove(ind);
				proj.bricks.add(ind+1, b);
				updateContent();  
				}
			}
		}
	public Brick getSelectedBrick()
		{
		if(!wlist.selectedItems().isEmpty())
			return (Brick)wlist.selectedItems().get(0).data(Qt.ItemDataRole.UserRole);
		else
			return null;
		}
	
	
	
	public void updateContent()	
		{
		System.out.println("update content");
		Brick sel=getSelectedBrick();
		wlist.model().removeRows(0, wlist.model().rowCount());
		for(Brick brick:proj.bricks)
			{
			QListWidgetItem it=new QListWidgetItem(brick.getName());
			it.setData(Qt.ItemDataRole.UserRole, brick);
			wlist.addItem(it);
			if(brick==sel)
				it.setSelected(true);
			}
		}



	public void setProject(DocubricksProject project)
		{
		this.proj=project;
		updateContent();
		}
	
	}
