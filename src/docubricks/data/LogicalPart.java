package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jdom2.Element;


/**
 * 
 * One logical part
 * 
 * @author Johan Henriksson
 *
 */
public class LogicalPart
	{
	private String description="";
	public String designator="";
	private String quantity="";
	public String id;

	public LinkedList<LogicalPartImplementation> implementingPart=new LinkedList<LogicalPartImplementation>();
	public HashMap<String, String> mapParam=new HashMap<String, String>();
	//param, is this a unit or phys part thing? or both?
	public MediaSet media=new MediaSet();

	
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
		Element eroot=new Element("logical_part");
		eroot.setAttribute("id", id);

		eroot.addContent(elWithContent("description", description));
		
		eroot.addContent(elWithContent("designator", designator));
		eroot.addContent(elWithContent("quantity", ""+quantity));

		for(LogicalPartImplementation imp:implementingPart)
			if(imp!=null)
				eroot.addContent(imp.toXML());
			else
				System.out.println("null implementation");

		eroot.addContent(media.toXML(basepath));

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
	public static LogicalPart fromXML(File basepath, UsefulSourceProject proj, Element root)
		{
		LogicalPart part=new LogicalPart();
		part.id=root.getAttributeValue("id");
		part.description=root.getChildText("description");
		part.designator=root.getChildText("designator");
		part.quantity=root.getChildText("quantity");

		part.media=MediaSet.fromXML(basepath, root.getChild("media"));

		for(Element e:root.getChildren())
			{
			if(e.getName().equals("implementation"))
				{
				String t=e.getAttributeValue("type");
				String id=e.getAttributeValue("id");
				if(t.equals("unit"))
					part.implementingPart.add(new LogicalPartImplementationUnit(id));//proj.getUnit(id)));
				else if(t.equals("physical_part"))
					part.implementingPart.add(new LogicalPartImplementationPhysical(proj.getPhysicalPart(id)));
				else
					throw new RuntimeException();
				}
			}

		return part;
		}

	
	
	/*
	public void setDesignator(String s);  //assumes there is a single blueprint, or that they all agree
	public void addDigitalPart(File f);

	public void setParameter(String var, String val);
	
	public void addPhysicalPart(PhysicalPart p);
	*/
	
	
	
	
	}
