package it.unibo.elevation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.unibo.elevation.mapquest.MapQuestElevationAPI;
import it.unibo.entity.GeoPoint;
import it.unibo.util.Network;
import it.unibo.util.Polylines;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MapQuestElevationAPITest {

	private MapQuestElevationAPI api;

	// Coordinates of Porta San Donato
	private double latitude = 44.49809709;
	private double longitude = 11.35488510;
	List<GeoPoint> bolognaPoints;
	List<GeoPoint> marzabottoVadoPoints;
	String marzabottoVadoPolyline;

	public MapQuestElevationAPITest() throws Exception {
		new File("/home/mone/Documenti/Università/Elevation/paparazzi/srtm");
		api = new MapQuestElevationAPI();
		bolognaPoints = new ArrayList<GeoPoint>();
		marzabottoVadoPoints = new ArrayList<GeoPoint>();

		double bolognaLatNord = 44.49932150;
		double bolognaLongSud = 11.32690430;

		double marzabottoLatNord = 44.34398494;
		double marzabottoLongSud = 11.20330811;

		// gives a line along Bologna
		GeoPoint p = null;
		for (int i = 0, j = 0; i < 50; i++, j++) {
			p = new GeoPoint(bolognaLatNord - (i * 0.0001), bolognaLongSud + (j * 0.0003));
			bolognaPoints.add(p);
		}

		for (int i = 0, j = 0; i < 50; i++, j++) {
			p = new GeoPoint(marzabottoLatNord - (i * 0.0002), marzabottoLongSud + (j * 0.0005));
			marzabottoVadoPoints.add(p);
		}

		marzabottoVadoPolyline = Polylines.encode(marzabottoVadoPoints);
		System.out.println(p);
	}

	@Test
	public void testGetElevationDoubleDouble() throws Exception {
		double elevation = api.getElevation(latitude, longitude);
		assertEquals("fails on:" + elevation, 55, elevation, 1.0);// > 54 && elevation < 56);
	}

	@Test
	public void testGetElevationGeoPoint() throws Exception {
		GeoPoint p = new GeoPoint(latitude, longitude);
		double elevation = api.getElevation(p);
		assertEquals("fails on:" + elevation, 55, elevation, 1.0);
	}

	@Test
	public void testSetElevation() throws Exception {
		GeoPoint p = new GeoPoint(latitude, longitude);
		api.setElevation(p);
		double elevation = p.getElevation();
		assertEquals("fails on:" + elevation, 55, elevation, 1.0);
	}

	@Test
	public void testGetElevationsListOfGeoPoint() throws Exception {
		String url = api.buildUrl(bolognaPoints);
		System.out.println(Network.getRawData(url));

		List<GeoPoint> newPoints = api.getElevations(bolognaPoints);
		for (GeoPoint p : newPoints) {
			double elevation = p.getElevation();
			assertTrue("fails on:" + elevation, (elevation > 54) && (elevation < 76));
		}
	}

	@Test
	public void testSetElevations() throws Exception {
		api.setElevations(bolognaPoints);
		for (GeoPoint p : bolognaPoints) {
			double elevation = p.getElevation();
			assertTrue("fails on:" + elevation, (elevation > 50) && (elevation < 76));
		}
	}

	@Test
	public void testGetElevationsString() throws Exception {
		List<GeoPoint> newPoints = api.getElevations(marzabottoVadoPolyline);
		double max = 0;

		for (GeoPoint p : newPoints) {
			double elevation = p.getElevation();
			assertTrue("fails on:" + elevation, (elevation > 120) && (elevation < 570));
			if (elevation > max) {
				max = elevation;
			}
		}
		assertTrue("fails on max:" + max, (max > 258) && (max < 260));
	}

}
