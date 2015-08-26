package docubricks.data;

import org.jdom2.Element;

/**
 * 
 * Implementation for a logical part: unit
 * 
 * @author Johan Henriksson
 *
 */
public class FunctionImplementationBrick implements FunctionImplementation
	{
	/*
	Unit unit;
	
	public LogicalPartImplementationUnit(Unit u)
		{
		this.unit=u;
		}*/
	
	String id;
	public FunctionImplementationBrick(String id)
		{
		this.id=id;
		}
	
	/**
	 * Serialize to XML
	 */
	public Element toXML()
		{
		Element e=new Element("implementation");
		e.setAttribute("type", "unit");
		e.setAttribute("id", id);
		return e;
		}
	
	@Override
	public boolean equals(Object obj)
		{
		if(obj instanceof FunctionImplementationBrick)
			{
			FunctionImplementationBrick o=(FunctionImplementationBrick)obj;
			return o.id.equals(id);//==unit;
			}
		else
			return false;
		}

	public Brick get(DocubricksProject project)
		{
		return project.getUnit(id);
		}
	}