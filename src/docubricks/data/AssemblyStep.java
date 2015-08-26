package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;

/**
 * 
 * 
 * @author Johan Henriksson
 *
 */
public class AssemblyStep
	{
	public MediaSet media=new MediaSet();
	public ArrayList<Function> parts=new ArrayList<Function>();
	private String desc="";
	
	
	/*
	public void addMedia(MediaFile f)
		{
		media.add(f);
		}*/
	
	public void setDescription(String s)
		{
		desc=s;
		}
	public String getDescription()
		{
		return desc;
		}

	
	
	
	public void addPart(Function part)
		{
		parts.add(part);
		}

	
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("step");

//		eroot.addContent(elWithContent("id", ""+id));
		eroot.addContent(elWithContent("description", desc));
		eroot.addContent(media.toXML(basepath));
		
		return eroot;
		}
	
	
	private static Element elWithContent(String el, String content)
		{
		Element e=new Element(el);
		e.addContent(content);
		return e;
		}

	
	public static AssemblyStep fromXML(File basepath, Element root)
		{
		AssemblyStep step=new AssemblyStep();
		step.desc=root.getChildText("description");
		step.media=MediaSet.fromXML(basepath, root.getChild("media"));
		return step;
		}
	
	
	
	//rebuild: needs explaining
	
	}
