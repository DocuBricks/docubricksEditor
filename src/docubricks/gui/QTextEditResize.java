package docubricks.gui;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;

public class QTextEditResize extends QTextEdit
	{
	
	public QTextEditResize()
		{
		textChanged.connect(this,"textEditChanged()");
		cursorPositionChanged.connect(this,"textEditChanged()");
		}
	
	@Override
	public QSize minimumSizeHint()
		{
		QTextDocument doc=document();
		QSize s=doc.size().toSize();
		QRect fr=frameRect();
		QRect cr=contentsRect();
		
		return new QSize(
				super.minimumSizeHint().width(), 
				Math.max(30, 10 + s.height() + (fr.height() - cr.height() )));
		}


	
	public void textEditChanged()
		{
		//TODO check if the size changed to avoid calls??
		updateGeometry();
		
		// Make sure the cursor is visible
		QRect cursor=cursorRect();
		QPoint pos = pos();
		QWidget pw = parentWidget();
		QScrollArea area;
		while (pw!=null)
			{
			if (pw.parentWidget()!=null)
				{
				QWidget area1 = pw.parentWidget().parentWidget();
				if (area1 instanceof QScrollArea)
					{
					area = (QScrollArea) area1;
					QPoint p2 = pos.add(cursor.center());
					area.ensureVisible(p2.x(), p2.y(), 
							10+cursor.width(),
							2*cursor.height());
					break;
					}
				}
			pos = pw.mapToParent(pos);
			pw = pw.parentWidget();
			}

		if(document().toPlainText().toLowerCase().contains("todo"))
			setStyleSheet("background-color:yellow;");
		else
			setStyleSheet("");

		}

	}
