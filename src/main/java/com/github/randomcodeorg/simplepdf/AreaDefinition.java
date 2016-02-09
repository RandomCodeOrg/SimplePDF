package com.github.randomcodeorg.simplepdf;

import org.w3c.dom.Node;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;

/**
 * Stellt eine zentrale Definition über einen Bereich innerhalb eines Dokumentes dar.  
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class AreaDefinition implements XmlSerializable {
	
	private String id;
	private AreaAvailability availability = AreaAvailability.GLOBAL;
	private Position position;
	private Size size;
	
	
	/**
	 * Legt eine neue Bereichs-Definition an.
	 * @param id Gibt die ID dieser Definition an.
	 * @param position Gibt die Position (in mm) dieses Bereiches auf der jeweiligen Seite des Dokumentes an.
	 * @param size Gibt die Größe (in mm) dieses Bereiches an.
	 * @throws NullPointerException Tritt auf, wenn id, psoition oder size den Wert {@code null} haben.
	 * @throws IllegalArgumentException Tritt auf, wenn die id ein leere String ist.
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
	 * Gibt die ID dieser Bereichs-Definition zurück.
	 * @return Die ID dieser Bereichs-Definition.
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * Gibt die Verfügbarkeit dieser Definition innerhalb des Dokumentes zurück.
	 * @return Die Verfügbarkeit dieser Definition.
	 */ 
	public AreaAvailability getAvailability(){
		return availability;
	}
	
	/**
	 * Gibt die Position dieses Bereichs auf einer Seite zurück.
	 * @return Die Position dieses Bereichs in mm.
	 */
	public Position getPosition(){
		return position;
	}
	
	/**
	 * Gibt die Größe dieses Bereichs zurück.
	 * @return Die Größe dieses Bereis in mm.
	 */
	public Size getSize(){
		return size;
	}
	
	/**
	 * Setzt die ID dieser Bereichs-Definition.
	 * @param id Die zu setzende ID.
	 * @throws NullPointerException Tritt auf, wenn die zu setzende ID den Wert {@code null} hat.
	 * @throws IllegalArgumentException Tritt auf, wenn die zu setzende ID ein leerer String ist.
	 */
	public void setID(String id){
		if(id == null) throw new NullPointerException("The id may not be null.");
		if(id.isEmpty()) throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}
	
	/**
	 * Setzt die Verfügbarkeit dieser Bereiches innerhalb eine Dokumentes.
	 * @param availability Die zu setzende Verfügbarkeit.
	 */
	public void setAvailability(AreaAvailability availability){
		this.availability = availability;
	}
	
	/**
	 * Setzt die Position dieses Bereichs auf einer Seite.
	 * @param position Die zu setzende Position in mm.
	 * @throws NullPointerException Tritt auf, wenn die zu setzende Position den Wert {@code null} hat.
	 */
	public void setPosition(Position position){
		if(position == null) throw new NullPointerException("The position may not be null.");
		this.position = position;
	}
	
	/**
	 * Setzt die Größe dieses Bereichs.
	 * @param size Die zu setzende Größe in mm.
	 * @throws NullPointerException Tritt auf, wenn die zu setzende Größe den Wert {@code null} hat.
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
