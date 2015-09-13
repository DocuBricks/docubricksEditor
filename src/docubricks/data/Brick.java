package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;

/**
 * 
 * One useful source unit
 * 
 * @author Johan Henriksson
 *
 */
public class Brick
	{
	public String id;
	private String name="";
	private String vabstract="";
	private String longdesc="";
	private String notes="";
	private String license="";
	
	public ArrayList<Author> authors=new ArrayList<Author>();
	public StepByStepInstruction asmInstruction=new StepByStepInstruction();
	public ArrayList<Function> functions=new ArrayList<Function>();
	
	public MediaSet media=new MediaSet();
	
	
	public void setName(String s)
		{
		name=s;
		}
	public String getName()
		{
		return name;
		}
	
	
	public void setAbstract(String s)
		{
		vabstract=s;
		}
	public String getAbstract()
		{
		return vabstract;
		}
	
	public void setLongDescription(String s)
		{
		longdesc=s;
		}
	public String getLongDescription()
		{
		return longdesc;
		}
	
	
	
	
	public String getNotes()
		{
		return notes;
		}
	public void setWhy(String s)
		{
		notes=s;
		}
	
	
	
	public String getLicense()
		{
		return license;
		}
	public void setLicense(String text)
		{
		license=text;
		}
	
	
	public Function createLogicalPart()
		{
		Function p=new Function();
		p.id=findFreeLogicalPartID();
		functions.add(p);
		return p;
		}
	
	

	public String findFreeLogicalPartID()
		{
		String id;
		for(;;)
			{
			id=""+(int)(Math.random()*Integer.MAX_VALUE);
			for(Function p:functions)
				if(p.id.equals(id))
					continue;
			break;
			}
		return id;
		}
	
	
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("brick");
		eroot.setAttribute("id",""+id);
		
		//kicking out <description> - no need for one more section
		
		eroot.addContent(elWithContent("name", name));
		eroot.addContent(elWithContent("abstract", vabstract));
		eroot.addContent(elWithContent("long_description", longdesc));
		eroot.addContent(elWithContent("why", notes));
		eroot.addContent(elWithContent("license", license)); //Note: done very differently in spec!

		eroot.addContent(media.toXML(basepath));
		
		eroot.addContent(asmInstruction.toXML(basepath));
		for(Author a:authors)
			if(a!=null)
				{
				Element e=new Element("author");
				e.setAttribute("id",a.id);
				eroot.addContent(e);
				}
		for(Function p:functions)
			if(p!=null)
				eroot.addContent(p.toXML(basepath));
		
		return eroot;
		}
	
	
	
	private static Element elWithContent(String el, String content)
		{
		Element e=new Element(el);
		e.addContent(content);
		return e;
		}
	
	
	public static Brick fromXML(File basepath, DocubricksProject proj, Element root)
		{
		Brick u=new Brick();
		u.id=root.getAttributeValue("id");

		u.name=root.getChildText("name");
		u.vabstract=root.getChildText("abstract");
		u.longdesc=root.getChildText("long_description");
		u.notes=root.getChildText("why");
		u.license=root.getChildText("license");


		for(Element child:root.getChildren())
			if(child.getName().equals("author"))
				{
				String id=child.getAttributeValue("id");
				u.authors.add(proj.getAuthor(id));
				}
		for(Element child:root.getChildren())
			if(child.getName().equals("logical_part") || child.getName().equals("function"))
				u.functions.add(Function.fromXML(basepath, proj, child));

		u.asmInstruction=StepByStepInstruction.fromXML(u, basepath, root.getChild("assembly_instruction"));

		u.media=MediaSet.fromXML(basepath, root.getChild("media"));

		return u;
		}
	
	
	public Function getFunction(String id)
		{
		for(Function f:functions)
			{
			System.out.println("--- "+f.id);
			if(f.id.equals(id))
				return f;
			}
		throw new RuntimeException("Cannot find function "+id);
		}

	}
