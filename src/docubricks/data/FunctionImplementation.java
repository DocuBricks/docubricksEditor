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
public interface FunctionImplementation
	{
	public String getRepresentativeName(DocubricksProject project);

	/**
	 * Serialize to XML
	 */
	public Element toXML();

	public JSONObject toJSON();
	}