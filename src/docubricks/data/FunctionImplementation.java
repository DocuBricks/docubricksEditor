package docubricks.data;

import org.jdom2.Element;

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
	}