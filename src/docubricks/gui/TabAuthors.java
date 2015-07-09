package docubricks.gui;

import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.DocubricksProject;

/**
 * 
 * Tab: For the project
 * 
 * @author Johan Henriksson
 *
 */
public class TabAuthors extends QWidget
	{
	public Signal0 signalUpdated=new Signal0();
	private QScrollArea scroll=new QScrollArea();
	private PaneAuthorData parts;
	

	public TabAuthors(DocubricksProject proj)
		{
		QVBoxLayout laytot=new QVBoxLayout();
		laytot.addWidget(scroll);
		scroll.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
		scroll.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
		scroll.setWidgetResizable(true);
		laytot.setMargin(0);
		setLayout(laytot);
		
		//Having a widget in the scrollpane, instead of a layout directly, is key to get it to work
		QWidget scrollwid=new QWidget();
		scrollwid.setObjectName("form");
		QVBoxLayout lay=new QVBoxLayout();
		scrollwid.setLayout(lay);
		scroll.setWidget(scrollwid);
		
		parts=new PaneAuthorData(proj);
		lay.addLayout(parts);
		lay.addStretch();
		
		parts.signalUpdated.connect(signalUpdated);
		}

	
	public void storevalues()
		{
		parts.storevalues();
		}


	}
