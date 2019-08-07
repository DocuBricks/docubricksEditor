package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Element;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

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
	public ArrayList<StepByStepInstruction> instructions=new ArrayList<StepByStepInstruction>();
	
	
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
	public void setNotes(String s)
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
		eroot.addContent(elWithContent("notes", notes));
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
	
		for(StepByStepInstruction instr:instructions)
			{
			Element e=instr.toXML(basepath);
			e.setName("instruction");
			//EEEEW!
			e.setAttribute("name", instr.name);
			eroot.addContent(e);
			}
			
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
		u.notes=root.getChildText("notes");
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
		for(Element child:root.getChildren())
			if(child.getName().equals("instruction"))
				{
				StepByStepInstruction i=StepByStepInstruction.fromXML(u, basepath, child);
				i.name=child.getAttributeValue("name");
				u.instructions.add(i);
				}

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
	
	
	public void removeBrickRef(Brick b)
		{
		for(Function lp:functions)
			for(FunctionImplementation imp:new LinkedList<FunctionImplementation>(lp.implementingPart))
				if(imp instanceof FunctionImplementationBrick)
					{
					FunctionImplementationBrick fi=(FunctionImplementationBrick)imp;
					if(fi.id.equals(b.id))
						lp.implementingPart.remove(imp);
					}
		}
	
	
	public JSONObject toJSON(File basepath) throws IOException
		{
		JSONObject eroot=new JSONObject();
		//eroot.put("id",""+id);
		
		//kicking out <description> - no need for one more section
		
		eroot.put("id", id);
		eroot.put("name", name);
		eroot.put("abstract", vabstract);
		eroot.put("long_description", longdesc);
		eroot.put("notes", notes);
		eroot.put("license", license);
		
		eroot.put("files",media.toJSON(basepath));
		
		JSONArray arrAuthors=new JSONArray();
		eroot.put("authors",arrAuthors);
		for(Author a:authors)
			if(a!=null)
				arrAuthors.add(a.id);
		
		JSONArray arrFunctions=new JSONArray();
		eroot.put("functions", arrFunctions);
		for(Function p:functions)
			if(p!=null)
				arrFunctions.add(p.toJSON(basepath));
	
		JSONArray arrInst=new JSONArray();
		eroot.put("instructions", arrInst);
		for(StepByStepInstruction instr:instructions)
			{
			JSONObject e=instr.toJSON(basepath);
//			e.setName("instruction");
			//EEEEW!
			e.put("name", instr.name);
			arrInst.add(e);
			}

		JSONObject obasm=asmInstruction.toJSON(basepath);
		obasm.put("name", "assembly");
		arrInst.add(obasm);
		

		return eroot;	
		}
	
	public void getReferencedFiles(Collection<File> files)
		{
		for(Author a:authors)
			a.getReferencedFiles(files);
		asmInstruction.getReferencedFiles(files);
		media.getReferencedFiles(files);
		for(StepByStepInstruction inst:instructions)
			inst.getReferencedFiles(files);
		}

	}
