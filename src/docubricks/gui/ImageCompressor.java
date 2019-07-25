package docubricks.gui;

import java.io.File;
import java.io.IOException;

import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImageReader;
import com.trolltech.qt.gui.QImageWriter;

import docubricks.data.AssemblyStep;
import docubricks.data.Brick;
import docubricks.data.DocubricksProject;
import docubricks.data.MediaFile;
import docubricks.data.MediaSet;
import docubricks.data.Part;


/**
 * 
 * Compress the data, in case uncompressed files were added. Possible offer zipping files as well
 * 
 * @author Johan Henriksson
 *
 */
public class ImageCompressor
	{

	public static boolean needCompression(DocubricksProject project)
		{
		boolean need=false;
		for(Brick b:project.bricks)
			{
			for(AssemblyStep si:b.asmInstruction.steps)
				need |= needCompression(si.media);
			need |= needCompression(b.media);
			}
		for(Part p:project.parts)
			{
			for(AssemblyStep si:p.instructions.steps)
				need |= needCompression(si.media);
			need |= needCompression(p.media);
			}
		System.out.println("need to compress: "+need);
		return need;
		}



	private static boolean needCompression(MediaSet media)
		{
		for(MediaFile f:media.files)
			if(needCompression(f.f))
				return true;
		return false;
		}
	

	private static boolean needCompression(File f)
		{
		String n=f.getName().toLowerCase();
		System.out.println(n);
		if(n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg"))
			{
			//Nothing to do
			}
		else 
			{
			QImageReader imreader=new QImageReader(f.getAbsolutePath());
			if(imreader.canRead())
				return true;
			}
		return false;
		}


	public static void compress(DocubricksProject project)
		{
		for(Brick b:project.bricks)
			{
			for(AssemblyStep si:b.asmInstruction.steps)
				compress(si.media);
			compress(b.media);
			}
		for(Part p:project.parts)
			{
			for(AssemblyStep si:p.instructions.steps)
				compress(si.media);
			compress(p.media);
			}
		}



	private static void compress(MediaSet media)
		{
		for(MediaFile f:media.files)
			if(needCompression(f.f))
				{
				String n=f.f.getName();
				
				try
					{
					File tempPNG=File.createTempFile("foo", ".png");
					File tempJPG=File.createTempFile("foo", ".jpg");
					
					QImageReader imreader=new QImageReader(f.f.getAbsolutePath());
					QImage im=imreader.read();
					QImageWriter imwriterPNG=new QImageWriter(tempPNG.getAbsolutePath());
					QImageWriter imwriterJPG=new QImageWriter(tempJPG.getAbsolutePath());
					imwriterPNG.write(im);
					imwriterJPG.write(im);

					long sizePNG=tempPNG.length();
					long sizeJPG=tempJPG.length();
					
					//File useFile;
					String ext;
					if(sizeJPG*1.5 < sizePNG)
						{
						//Use JPG
						//useFile=tempJPG;
						ext=".jpg";
						}
					else
						{
						//Use PNG
						//useFile=tempPNG;
						ext=".png";
						}
					
					String newn = n.substring(0, n.lastIndexOf('.')) + ext;
					File newf=new File(f.f.getParentFile(), newn);
					QImageWriter imwriterN=new QImageWriter(newf.getAbsolutePath());
					imwriterN.write(im);
					f.f=newf;
					}
				catch (IOException e)
					{
					e.printStackTrace();
					}
				}
		}

	}
