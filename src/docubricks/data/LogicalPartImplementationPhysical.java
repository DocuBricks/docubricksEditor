package docubricks.data;

import org.jdom2.Element;

/**
 * 
 * Implementation for a logical part: physical part
 * 
 * @author Johan Henriksson
 *
 */
public class LogicalPartImplementationPhysical implements LogicalPartImplementation
	{
	PhysicalPart part;
	
	public LogicalPartImplementationPhysical(PhysicalPart p)
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
	
	@Override
	public boolean equals(Object obj)
		{
		if(obj instanceof LogicalPartImplementationPhysical)
			{
			LogicalPartImplementationPhysical o=(LogicalPartImplementationPhysical)obj;
			return o.part==part;
			}
		else
			return false;
		}
	}