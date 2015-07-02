package docubricks.gui.qt;

import java.util.LinkedList;
import java.util.TreeSet;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QTableWidgetItem;

/**
 * 
 * 
 * Much of code inspired from stack overflow
 * 
 * @author Johan Henriksson
 *
 */
public class QTableWidgetWithCSVcopy extends QTableWidget
	{
	protected void keyPressEvent(com.trolltech.qt.gui.QKeyEvent event) 
		{
		if(event.matches(StandardKey.Copy) )
			copy();
		else
			super.keyPressEvent(event);
		}

	public String allToCSV()
		{
		TreeSet<Integer> whichcol=new TreeSet<Integer>();
		TreeSet<Integer> whichrow=new TreeSet<Integer>();
		for(int i=0;i<columnCount();i++)
			whichcol.add(i);
		for(int i=0;i<rowCount();i++)
			whichrow.add(i);
		return toCSV(whichcol, whichrow);
		}
	
	public void copy(TreeSet<Integer> whichcol, TreeSet<Integer> whichrow)
		{
		String s=toCSV(whichcol, whichrow);
		QApplication.clipboard().setText(s);
		}

	
	/**
	 * Export selection to CSV
	 */
	public String toCSV(TreeSet<Integer> whichcol, TreeSet<Integer> whichrow)
		{
		StringBuilder sb=new StringBuilder();
	
		boolean fst=true;
		for(int i:whichcol)
			{
			if(!fst)
				sb.append("\t");
			fst=false;
			sb.append(horizontalHeaderItem(i).text());
			}
		sb.append("\n");

		for(int currow:whichrow)
			{
			fst=true;
			for(int curcol:whichcol)
				{
				if(!fst)
					sb.append("\t");
				fst=false;
				QTableWidgetItem item=item(currow,curcol);
				if(item.data(Qt.ItemDataRole.UserRole)!=null)
					sb.append(item.data(Qt.ItemDataRole.UserRole).toString());
				else
					sb.append(item.text());
				}
			sb.append("\n");
			}
		sb.append("\n");
		return sb.toString();
		}

	
	/**
	 * Export everything to CSV. Can return null if no
	 */
	public String selectionToCSV()
		{
		LinkedList<QModelIndex> indexes=new LinkedList<QModelIndex>(selectionModel().selectedIndexes());
		if(indexes.size()>0)
			{
			TreeSet<Integer> whichcol=new TreeSet<Integer>();
			TreeSet<Integer> whichrow=new TreeSet<Integer>();
			for(QModelIndex in:indexes)
				{
				whichcol.add(in.column());
				whichrow.add(in.row());
				}
			return toCSV(whichcol, whichrow);
			}
		else
			return null;
		}
	
	public void copyAll()
		{
		String s=allToCSV();
		if(s!=null)
			QApplication.clipboard().setText(s);
		}
	
	public void copy()
		{
		String s=selectionToCSV();
		if(s!=null)
			QApplication.clipboard().setText(s);
		}


	}
