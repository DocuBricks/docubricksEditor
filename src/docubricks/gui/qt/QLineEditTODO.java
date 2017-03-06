package docubricks.gui.qt;

import com.trolltech.qt.gui.QLineEdit;

/**
 * 
 * 
 * Much of code inspired from stack overflow
 * 
 * @author Johan Henriksson
 *
 */
public class QLineEditTODO extends QLineEdit
	{
	public QLineEditTODO()
		{
		textChanged.connect(this,"updateStyle()");
		}

	public QLineEditTODO(String string)
		{
		this();
		setText(string);
		}

	public void updateStyle()
		{
		if(text().toLowerCase().contains("todo"))
			setStyleSheet("background:yellow;");
		else
			setStyleSheet("");
		}

	}
