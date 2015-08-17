package xml;

import java.util.ArrayList;
import java.util.List;

import geometry.PointXY;
import geometry.PolygonShape;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A JAXB adaptor used to convert a collection of points defined in an XML file
 * into a PolygonShape object.  The collection of points are defined in the 
 * XmlPoints class, which the XML parser is able to understand.
 * 
 * This is currently only intended to be used for unmarshalling.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class PolygonShapeAdapter extends XmlAdapter<XmlPoints, PolygonShape> {

	@Override
	public XmlPoints marshal(PolygonShape polygonShape) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public PolygonShape unmarshal(XmlPoints xmlPoints) throws Exception {
		
		List<PointXY> xyPoints = convertXmlPoints(xmlPoints.getPoints());
		return new PolygonShape(xyPoints);
		
	}
	
	/**
	 * Convert the provided list of XmlPoint into a list of PointXY.
	 * 
	 * @param xmlPoints - the list of points to convert.
	 * @return the list of provided points in PointXY format.
	 */
	private List<PointXY> convertXmlPoints(List<XmlPoint> xmlPoints) {
		
		List<PointXY> xyPoints = new ArrayList<PointXY>();
		
		for (XmlPoint xmlPoint : xmlPoints) {
			PointXY xyPoint = new PointXY(xmlPoint.getX(), xmlPoint.getY());
			xyPoints.add(xyPoint);
		}
		
		return xyPoints;
	}

}
