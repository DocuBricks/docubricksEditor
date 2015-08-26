package docubricks.gui;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QDesktopServices;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImageReader;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import docubricks.data.MediaFile;

/**
 * 
 * 
 * 
 * @author Johan Henriksson
 *
 */
public class PaneMediaFile extends QWidget
	{
	public MediaFile mf;
	private PaneMediaSet set;
	
	public class ResizableImagePane extends QWidget
		{
		QImage image;
		
		public ResizableImagePane()
			{
			QImageReader imreader=new QImageReader(mf.f.getAbsolutePath());
			if(imreader.canRead())
				{
				QSize siz=imreader.size();
				double ratio=siz.width()/(double)siz.height();
				if(siz.width()>2000)
					imreader.setScaledSize(new QSize((int)(600*ratio), 600));
				}

			
			image = imreader.read();//new QPixmap(mf.f.getAbsolutePath());
			if(image.isNull())
				{
				image=null;
				System.out.println("image is null: "+mf.f);
				}
			/*
			if(image!=null && image.width()>2000)
				{
				double ratio=image.width()/(double)image.height();
				image=image.scaled((int)(600*ratio), 600, AspectRatioMode.KeepAspectRatio, TransformationMode.FastTransformation);
				}
		*/
			
			//If another type of data, e.g. movie, STL etc, try to render it
			
			
			
			}
		
		@Override
		protected void paintEvent(QPaintEvent e)
			{
			super.paintEvent(e);
			QPainter p=new QPainter(this);
			
			if(image!=null)
				{
				double ratio=height()/(double)image.height();
				p.scale(ratio, ratio);
				p.drawImage(0, 0, image);
				}
			p.end();
			}
		
		@Override
		public QSize minimumSizeHint()
			{
			int h=200;
			int w;
			if(image!=null)
				w=image.width()*h/image.height();
			else
				w=50;
			return new QSize(w,h);
			}

		
		
		@Override
		public QSize sizeHint()
			{
			return minimumSizeHint();
			/*
			int h=200;
			int w=image.width()*h/image.height();
			return new QSize(w,h);
			*/
			}
		
		}
	
	public PaneMediaFile(PaneMediaSet set, MediaFile mf)
		{
		this.set=set;
		this.mf=mf;
		
		ResizableImagePane p=new ResizableImagePane();
		
		QVBoxLayout lay=new QVBoxLayout();
		lay.addWidget(p);
		setLayout(lay);
		
		//TODO: set keep ratio?
		}
	
	protected void mouseReleaseEvent(QMouseEvent event)
    {
    super.mousePressEvent(event);
    if(event.button()==MouseButton.LeftButton)
      {
      QMenu menu=new QMenu();
      menu.addAction(tr("File: ")+mf.f.getPath(), this, "actionSystemOpen()");
      menu.addAction(tr("Size: ")+mf.f.length());
      menu.addSeparator();
      //TODO: crop/resize/change format - if file is a raster image
      menu.addAction(tr("Unlink file"), this, "actionUnlink()");
      menu.exec(event.globalPos());
      }
    }

	public void actionSystemOpen()
		{
		QDesktopServices.openUrl(new QUrl(mf.f.getAbsolutePath()));
		}
	
	public void actionUnlink()
		{
		set.unlink(this);
		}
	
	}
