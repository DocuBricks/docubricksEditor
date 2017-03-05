package docubricks.data;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * Implementation for a logical part: physical part
 * 
 * @author Johan Henriksson
 *
 */
public class FunctionImplementationPart implements FunctionImplementation
	{
	Part part;
	
	public FunctionImplementationPart(Part p)
		{
		part=p;
		}
	
	/**
	 * Serialize to XML
	 */
	public Element toXML()
		{
		Element e=new Element("implementation");
		e.setAttribute("type", "physical_part");
		e.setAttribute("id", part.id);
		return e;
		}
	
	public JSONObject toJSON()
		{
		JSONObject e=new JSONObject();
		e.put("type", "brick");
		e.put("id", part.id);
		return e;
		}

	
	@Override
	public boolean equals(Object obj)
		{
		if(obj instanceof FunctionImplementationPart)
			{
			FunctionImplementationPart o=(FunctionImplementationPart)obj;
			return o.part==part;
			}
		else
			return false;
		}

	public String getRepresentativeName(DocubricksProject project)
		{
		return "Physical part: "+part.name;
		}
	}