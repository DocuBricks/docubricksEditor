package docubricks.data;

import org.jdom2.Element;

/**
 * 
 * Implementation of a logical part
 * 
 * @author Johan Henriksson
 *
 */
public interface LogicalPartImplementation
	{
	/**
	 * Serialize to XML
	 */
	public Element toXML();
	}