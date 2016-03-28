package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jdom2.Element;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


/**
 * 
 * One logical part
 * 
 * @author Johan Henriksson
 *
 */
public class Function
	{
	private String description="";
	public String designator="";
	private String quantity="";
	public String id;

	public LinkedList<FunctionImplementation> implementingPart=new LinkedList<FunctionImplementation>();
	public HashMap<String, String> mapParam=new HashMap<String, String>();
	//param, is this a unit or phys part thing? or both?

	
	public void setDescription(String s)
		{
		description=s;
		}
	public String getDescription()
		{
		return description;
		}

	
	
	public void setQuantity(String num)
		{
		quantity=num;
		}
	public String getQuantity()
		{
		return quantity;
		}
	

	/**
	 * Serialize to XML
	 */
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("function");
		eroot.setAttribute("id", id);

		eroot.addContent(elWithContent("description", description));
		
		eroot.addContent(elWithContent("designator", designator));
		eroot.addContent(elWithContent("quantity", ""+quantity));

		for(FunctionImplementation imp:implementingPart)
			if(imp!=null)
				eroot.addContent(imp.toXML());
			else
				System.out.println("null implementation");

		return eroot;
		}
	

	/**
	 * Wrap element text
	 */
	private static Element elWithContent(String el, String content)
		{
		Element e=new Element(el);
		e.addContent(content);
		return e;
		}
	
	
	/**
	 * Deserialize from XML
	 */
	public static Function fromXML(File basepath, DocubricksProject proj, Element root)
		{
		Function part=new Function();
		part.id=root.getAttributeValue("id");
		part.description=root.getChildText("description");
		part.designator=root.getChildText("designator");
		part.quantity=root.getChildText("quantity");

		for(Element e:root.getChildren())
			{
			if(e.getName().equals("implementation"))
				{
				String t=e.getAttributeValue("type");
				String id=e.getAttributeValue("id");
				if(t.equals("unit") || t.equals("brick"))
					part.implementingPart.add(new FunctionImplementationBrick(id));//proj.getUnit(id)));
				else if(t.equals("physical_part"))
					part.implementingPart.add(new FunctionImplementationPhysical(proj.getPhysicalPart(id)));
				else
					throw new RuntimeException();
				}
			}

		return part;
		}
	
	
	public String getRepresentativeName(DocubricksProject project)
		{
		if(description.equals(""))
			{
			/*
			for(FunctionImplementation p:implementingPart)
				System.out.println(p);
				System.out.println("===");
				*/
			for(FunctionImplementation p:implementingPart)
				if(p!=null)
					return p.getRepresentativeName(project);
			return "<unnamed function>";
			}
		else
			{
			return description;
			}
		}
	
	
	public JSONObject toJSON(File basepath)
		{
		JSONObject eroot=new JSONObject();
		eroot.put("id", id);

		eroot.put("description", description);
		
		eroot.put("designator", designator);
		eroot.put("quantity", ""+quantity);

		JSONArray arrimp=new JSONArray();
		eroot.put("implementations",arrimp);
				
		for(FunctionImplementation imp:implementingPart)
			if(imp!=null)
				arrimp.add(imp.toJSON());
			else
				System.out.println("null implementation");

		return eroot;
		}

	
	
	
	}
