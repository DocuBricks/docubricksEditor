package docubricks.data;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * Implementation of a logical part
 * 
 * @author Johan Henriksson
 *
 */
public abstract class FunctionImplementation
	{
	public abstract String getRepresentativeName(DocubricksProject project);

	public int quantity=1;
	
	/**
	 * Serialize to XML
	 */
	public abstract Element toXML();

	public abstract JSONObject toJSON();
	}