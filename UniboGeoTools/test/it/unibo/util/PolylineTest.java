package it.unibo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.unibo.entity.GeoPoint;
import it.unibo.util.Polylines;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class PolylineTest {

	private String polyline;
	private List<GeoPoint> points;

	@Before
	public void init() throws ParserConfigurationException, SAXException {

		polyline = "gfo}EtohhUxD@bAxJmGF";

		GeoPoint p1Res = new GeoPoint(36.4555600, -116.8666700);
		GeoPoint p2Res = new GeoPoint(36.4546300, -116.8666800);
		GeoPoint p3Res = new GeoPoint(36.4542900, -116.8685700);
		GeoPoint p4Res = new GeoPoint(36.4556400, -116.8686100);
		points = new ArrayList<GeoPoint>(5);
		points.add(p1Res);
		points.add(p2Res);
		points.add(p3Res);
		points.add(p4Res);
	}

	@Test
	public void testDecode() {
		System.out.println("\ntestDecode ---------");
		List<GeoPoint> resPoints = Polylines.decode(polyline);

		if (resPoints.size() != points.size()) {
			fail("The size of decoded point list is wrong! ");
		}

		for (int i = 0; i < points.size(); i++) {
			System.out.println("decoded lat: " + points.get(i).getLatitude() + " " + resPoints.get(i).getLatitude());
			System.out.println("decoded lon: " + points.get(i).getLongitude() + " " + resPoints.get(i).getLongitude());
			assertEquals(points.get(i).getLatitude(), resPoints.get(i).getLatitude(), 0);
			assertEquals(points.get(i).getLongitude(), resPoints.get(i).getLongitude(), 0);
		}
	}

	@Test
	public void testEncode() {
		System.out.println("\ntestEncode ---------");
		String result = Polylines.encode(points);
		System.out.println(polyline);
		System.out.println(result);
		assertEquals(polyline, result);
	}

}
