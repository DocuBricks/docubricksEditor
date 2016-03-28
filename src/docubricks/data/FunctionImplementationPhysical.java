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
public class FunctionImplementationPhysical implements FunctionImplementation
	{
	PhysicalPart part;
	
	public FunctionImplementationPhysical(PhysicalPart p)
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
		if(obj instanceof FunctionImplementationPhysical)
			{
			FunctionImplementationPhysical o=(FunctionImplementationPhysical)obj;
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