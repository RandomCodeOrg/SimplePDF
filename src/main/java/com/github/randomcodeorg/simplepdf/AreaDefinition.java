package com.github.randomcodeorg.simplepdf;

import org.w3c.dom.Node;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;

/**
 * A central definition for an area within a document page.
 *
 */
public class AreaDefinition implements XmlSerializable {
	
	private String id;
	private AreaAvailability availability = AreaAvailability.GLOBAL;
	private Position position;
	private Size size;
	
	
	/**
	 * Creates a new area definition.
	 * @param id The identifier of the area to create.
	 * @param position The position of the area to create.
	 * @param size The size of the area to create.
	 */
	public AreaDefinition(String id, Position position, Size size){
		if(id == null) throw new NullPointerException("The id may not be null.");
		if(id.isEmpty()) throw new IllegalArgumentException("The id may not be empty.");
		if(position == null) throw new NullPointerException("The position may not be null.");
		if(size == null) throw new NullPointerException("The size may not be null.");
		this.id = id;
		this.position = position;
		this.size = size;
	}
	
	/**
	 * Returns the identifier of this area definition.
	 * @return The identifier of this area definition.
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * Returns the availability of the defined area.
	 * @return The availability of the defined area.
	 */
	public AreaAvailability getAvailability(){
		return availability;
	}
	
	/**
	 * Returns the position of the defined area.
	 * @return The position of the defined area.
	 */
	public Position getPosition(){
		return position;
	}
	
	/**
	 * Returns the size of the defined area.
	 * @return The size of the defined area.
	 */
	public Size getSize(){
		return size;
	}
	
	
	/**
	 * Sets the identifier of this area definition.
	 * @param id The identifier to set.
	 * @throws NullPointerException If the given identifier is {@code null}.
	 * @throws IllegalArgumentException If the given identifier is an empty string. 
	 */
	public void setID(String id){
		if(id == null) throw new NullPointerException("The id may not be null.");
		if(id.isEmpty()) throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}
	
	/**
	 * Sets the availability of the defined area.
	 * @param availability The availability to set.
	 */
	public void setAvailability(AreaAvailability availability){
		this.availability = availability;
	}
	
	/**
	 * Sets the position of the defined area.
	 * @param position The position to set.
	 */
	public void setPosition(Position position){
		if(position == null) throw new NullPointerException("The position may not be null.");
		this.position = position;
	}
	
	/**
	 * Sets the size of the defined area.
	 * @param size The size to set.
	 * @throws NullPointerException If the given size is <code>null</code>.
	 */
	public void setSize(Size size){
		if(size == null) throw new NullPointerException("The size may not be null.");
		this.size = size;
	}
	
	

	@Override
	public String toXML() {
		/*<AreaDefinition AreaAvailability="GLOBAL" ID="letter_head">
      <Position>
        <X>20</X>
        <Y>8.46</Y>
      </Position>
      <Size Width="105" Height="18.54" />
    </AreaDefinition>*/
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<AreaDefinition AreaAvailability=\"%s\" ID=\"%s\">\n\t", availability, id));
		sb.append(size.toXML());
		sb.append("\n");
		sb.append(FormattingTools.indentText(position.toXML()));
		sb.append("\n</AreaDefinition>");
		return sb.toString();
		
	}
	
	static AreaDefinition parse(Node n){
		AreaDefinition ad = new AreaDefinition(getAttribute(n, "ID", ""), Position.parse(getChild(n, "Position")), Size.parse(getChild(n, "Size")));
		String avail = getAttribute(n, "AreaAvailability", "GLOBAL");
		if(avail.equals("ONLY_FIRST_PAGE")) ad.setAvailability(AreaAvailability.ONLY_FIRST_PAGE);
		return ad;
	}

}
