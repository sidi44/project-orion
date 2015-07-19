package xml;

import java.util.ArrayList;
import java.util.List;

import geometry.PointXY;
import geometry.PolygonShape;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
	
	private List<PointXY> convertXmlPoints(List<XmlPoint> xmlPoints) {
		
		List<PointXY> xyPoints = new ArrayList<PointXY>();
		
		for (XmlPoint xmlPoint : xmlPoints) {
			PointXY xyPoint = new PointXY(xmlPoint.getX(), xmlPoint.getY());
			xyPoints.add(xyPoint);
		}
		
		return xyPoints;
	}

}
