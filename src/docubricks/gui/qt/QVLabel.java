package docubricks.gui.qt;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QWidget;

/**
 * 
 * Vertical label
 * 
 * @author Johan Henriksson
 *
 */
public class QVLabel extends QWidget
	{
	private String text="abc";

	public QVLabel(QWidget parent)
		{
		super(parent);
		setMinimumWidth(15);
		}
	
	public void setText(String text)
		{
		this.text=text;
		repaint();
		}
	
	
	@Override
	protected void paintEvent(QPaintEvent e)
		{
		super.paintEvent(e);
		
		QPainter painter=new QPainter(this);
		QFontMetrics fm=new QFontMetrics(painter.font());
		QFont font=painter.font();
		font.setBold(true);
		painter.setFont(font);
    painter.setPen(QColor.black);
    painter.rotate(-90);
    painter.drawText(new QPoint(-(height()-fm.width(text))/2,11), text);
		}

	
	}
