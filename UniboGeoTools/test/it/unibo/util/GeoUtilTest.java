package it.unibo.util;

import static org.junit.Assert.assertTrue;
import it.unibo.direction.DirectionAPI;
import it.unibo.direction.google.GoogleDirectionApi;
import it.unibo.elevation.ElevationAPI;
import it.unibo.elevation.google.GoogleElevationAPI;
import it.unibo.entity.GeoPoint;
import it.unibo.entity.Route;
import it.unibo.util.GeoUtil;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class GeoUtilTest {

	private static DirectionAPI directionAPI;
	private static ElevationAPI elevationAPI;
	private static List<GeoPoint> points;

	@BeforeClass
	public static void beforeClass() throws Exception {
		elevationAPI = new GoogleElevationAPI();
		directionAPI = new GoogleDirectionApi();

		List<Route> routes = directionAPI.getDirectionsBetween("Lama di Reno, Marzabotto, BO", "Monte Sole, Marzabotto, BO");
		points = elevationAPI.getElevations(routes.get(0).getOverviewPolyline());

		// ensure that there is at least one equal elevation
		GeoPoint equalP1 = new GeoPoint(44, 11, 110);
		GeoPoint equalP2 = new GeoPoint(44, 11, 110);
		points.add(0, equalP1);
		points.add(1, equalP2);
	}

	@Test
	public void testGetInclinationDegree() throws Exception {
		GeoPoint prev = null;

		for (GeoPoint point : points) {
			if (prev != null) {
				double inclination = GeoUtil.getInclinationDegree(prev, point);
				System.out.println("prev:" + prev.getElevation() + " point:" + point.getElevation() + " inc:" + inclination);

				if (prev.getElevation() < point.getElevation()) {
					assertTrue(inclination > 0);
				} else if (prev.getElevation() == point.getElevation()) {
					assertTrue(inclination == 0);
				} else {
					assertTrue(inclination < 0);
				}
			}
			prev = point;
		}
	}

	@Test
	public void testGetSlopePercentage() throws Exception {
		GeoPoint prev = null;

		for (GeoPoint point : points) {
			if (prev != null) {
				double angle = Math.abs(GeoUtil.getInclinationDegree(prev, point));
				double slope = Math.abs(GeoUtil.getSlopePercentage(prev, point));
				System.out.println("prev:" + prev + " point:" + point);
				System.out.println("angle:" + angle + " slope:" + slope);

				if (angle <= 5.71) {
					assertTrue(slope <= 10);
				} else if (angle <= 11.31) {
					assertTrue(slope <= 20 && slope > 10);
				} else if (angle <= 16.70) {
					assertTrue(slope <= 30 && slope > 20);
				} else if (angle <= 21.80) {
					assertTrue(slope <= 40 && slope > 30);
				} else if (angle <= 26.57) {
					assertTrue(slope <= 50 && slope > 40);
				} else if (angle <= 36.87) {
					assertTrue(slope <= 75 && slope > 50);
				} else if (angle <= 45) {
					assertTrue(slope <= 100 && slope > 75);
				} else if (angle <= 56.31) {
					assertTrue(slope <= 150 && slope > 100);
				} else if (angle <= 63.43) {
					assertTrue(slope <= 200 && slope > 150);
				} else if (angle <= 71.57) {
					assertTrue(slope <= 300 && slope > 200);
				} else if (angle <= 78.69) {
					assertTrue(slope <= 500 && slope > 300);
				} else if (angle <= 84.29) {
					assertTrue(slope <= 1000 && slope > 500);
				} else if (angle > 84.29) {
					assertTrue(slope > 1000);
				}
			}
			prev = point;
		}
	}

}
