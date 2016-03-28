package docubricks.gui;

import java.io.File;

import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QFileDialog.FileMode;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.MediaFile;
import docubricks.data.MediaSet;

/**
 * 
 * Pane with a set of media files
 * 
 * @author Johan Henriksson
 *
 */
public class PaneMediaSet extends QWidget
	{
	private QHBoxLayout laymedia=new QHBoxLayout();
	private QScrollArea scroll=new QScrollArea();

	
	QPushButton bAdd=new QPushButton("Add media");
	MediaSet mediaset;
	
	public PaneMediaSet(MediaSet mediaset)
		{
		this.mediaset=mediaset;
		
		laymedia.setMargin(0);
		QWidget w=new QWidget();
		w.setLayout(laymedia);
		scroll.setMinimumHeight(30);          
		scroll.setWidgetResizable(true);
		scroll.setWidget(w);
		
		bAdd.setSizePolicy(Policy.Minimum, Policy.Minimum); //really maximum!
		bAdd.setMinimumWidth(110);
		
		QMenu menu=new QMenu();
		bAdd.setMenu(menu);
		menu.addAction(tr("From file"), this, "actionAddMedia()");
		menu.addAction(tr("From clipboard (todo)"));
		
		
		QVBoxLayout lay=new QVBoxLayout();
		lay.addWidget(scroll);
		laymedia.addWidget(bAdd, 1, AlignmentFlag.AlignRight);
		lay.setMargin(0);
		setLayout(lay);
		
		setAcceptDrops(true);
		
		loadvalues(mediaset);
		}

	
	public void actionAddMedia()
		{
		QFileDialog dia=new QFileDialog();
		dia.setFileMode(FileMode.ExistingFile);
		dia.setDirectory(MainWindow.lastDirectory.getAbsolutePath());
		if(dia.exec()!=0)
			{
			File f=new File(dia.selectedFiles().get(0));
			MainWindow.lastDirectory=f.getParentFile();
			addMedia(f);
			}		
		}


	private void addMedia(File f)
		{
		MediaFile mf=new MediaFile();
		mediaset.files.add(mf);
		mf.f=f;
		addMediaWidget(mf);
		}

	private void addMediaWidget(MediaFile mf)
		{
		PaneMediaFile mp=new PaneMediaFile(this, mf);
		laymedia.removeWidget(bAdd);
		laymedia.addWidget(mp);
		laymedia.addWidget(bAdd, 1, AlignmentFlag.AlignRight);
		scroll.setMinimumHeight(260);          //better way?
		}
	
  /**
   * Event: User drags something onto widget
   */
  protected void dragEnterEvent(com.trolltech.qt.gui.QDragEnterEvent event) 
  	{
  	if(event.mimeData().hasFormat("text/uri-list"))
  		event.acceptProposedAction();
  	}


  /**
   * Event: User drops MIME onto widget
   */
  protected void dropEvent(QDropEvent event)
  	{
  	for(QUrl url:event.mimeData().urls())
  		{
  		File f=new File(url.path());
  		MainWindow.lastDirectory=f.getParentFile();
  		addMedia(f);
  		}
  	}


	private void loadvalues(MediaSet media)
		{
		for(MediaFile mf:media.files)
			addMediaWidget(mf); //TODO questionable!
		}


	public void unlink(PaneMediaFile pane)
		{
		laymedia.removeWidget(pane);
		mediaset.files.remove(pane.mf);
		pane.setVisible(false);
		}
  
	}
