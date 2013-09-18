package it.unibo.direction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.unibo.direction.DirectionUtil;
import it.unibo.direction.google.GoogleDirectionApi;
import it.unibo.entity.GeoPoint;
import it.unibo.entity.Leg;
import it.unibo.entity.Route;
import it.unibo.entity.Step;
import it.unibo.util.Network;
import it.unibo.util.Polylines;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GoogleDirectionAPITest {

	private GoogleDirectionApi api;
	private String fromLamaDiReno;
	private String toBologna;
	private String calderino;
	private String giardiniMargherita;
	private List<String> waypoints;
	private List<GeoPoint> waypointsPoint;
	private GeoPoint fromLamaDiRenoPoint;
	private GeoPoint toBolognaPoint;
	GeoPoint calderinoPoint;
	GeoPoint giardiniMargheritaPoint;

	public GoogleDirectionAPITest() throws Exception {
		api = new GoogleDirectionApi();
		fromLamaDiReno = "Lama di Reno, Marzabotto, BO";
		toBologna = "Bologna, BO";
		fromLamaDiRenoPoint = new GeoPoint(44.36446, 11.21480);
		toBolognaPoint = new GeoPoint(44.49494, 11.34261);

		waypoints = new ArrayList<String>(2);
		calderino = "Calderino, BO";
		giardiniMargherita = "Giardini Margherita, Bologna, BO";
		waypoints.add(calderino);
		waypoints.add(giardiniMargherita);

		waypointsPoint = new ArrayList<GeoPoint>(2);
		calderinoPoint = new GeoPoint(44.45198, 11.19173);
		giardiniMargheritaPoint = new GeoPoint(44.48287, 11.35490);
		waypointsPoint.add(calderinoPoint);
		waypointsPoint.add(giardiniMargheritaPoint);

	}

	@Test
	public void testGetDirectionsBetweenGeoPointGeoPoint() throws Exception {
		List<Route> routes = api.getDirectionsBetween(fromLamaDiRenoPoint, toBolognaPoint);

		int legDuration = 0, legDistance = 0, stepDuration = 0, stepDistance = 0;

		for (Route r : routes) {
			for (Leg l : r.getLegs()) {
				legDuration += l.getDuration();
				legDistance += l.getDistance();
				for (Step s : l.getSteps()) {
					stepDuration += s.getDuration();
					stepDistance += s.getDistance();
				}
			}
			assertEquals(legDuration, stepDuration, 0);
			assertEquals(legDistance, stepDistance, 0);
			legDuration = legDistance = stepDuration = stepDistance = 0;
		}

	}

	@Test
	public void testGetDirectionsBetweenGeoPointGeoPointListOfGeoPoint() throws Exception {
		System.out.println(api.buildUrl(fromLamaDiRenoPoint, toBolognaPoint, waypointsPoint));
		System.out.println(Network.getRawData(api.buildUrl(fromLamaDiRenoPoint, toBolognaPoint, waypointsPoint)));
		List<Route> routes = api.getDirectionsBetween(fromLamaDiRenoPoint, toBolognaPoint, waypointsPoint);

		int legDuration = 0, legDistance = 0, stepDuration = 0, stepDistance = 0;

		for (Route r : routes) {
			for (Leg l : r.getLegs()) {
				assertNotNull(l.getEndAddress());
				assertNotNull(l.getStartAddress());
				legDuration += l.getDuration();
				legDistance += l.getDistance();
				for (Step s : l.getSteps()) {
					stepDuration += s.getDuration();
					stepDistance += s.getDistance();
				}
			}
			assertEquals(legDuration, stepDuration, 0);
			assertEquals(legDistance, stepDistance, 0);
			legDuration = legDistance = stepDuration = stepDistance = 0;
		}
	}

	@Test
	public void testGetDirectionsBetweenStringString() throws Exception {
		List<Route> routes = api.getDirectionsBetween(fromLamaDiReno, toBologna);

		int legDuration = 0, legDistance = 0, stepDuration = 0, stepDistance = 0;

		for (Route r : routes) {
			for (Leg l : r.getLegs()) {
				legDuration += l.getDuration();
				legDistance += l.getDistance();
				for (Step s : l.getSteps()) {
					stepDuration += s.getDuration();
					stepDistance += s.getDistance();
				}
			}
			assertEquals(legDuration, stepDuration, 0);
			assertEquals(legDistance, stepDistance, 0);
			legDuration = legDistance = stepDuration = stepDistance = 0;
		}

	}

	@Test
	public void testGetDirectionsBetweenStringStringListOfString() throws Exception {
		List<Route> routes = api.getDirectionsBetween(fromLamaDiReno, toBologna, waypoints);

		int legDuration = 0, legDistance = 0, stepDuration = 0, stepDistance = 0;

		for (Route r : routes) {
			for (Leg l : r.getLegs()) {
				assertNotNull(l.getEndAddress());
				assertNotNull(l.getStartAddress());
				legDuration += l.getDuration();
				legDistance += l.getDistance();
				for (Step s : l.getSteps()) {
					stepDuration += s.getDuration();
					stepDistance += s.getDistance();
				}
			}
			assertEquals(legDuration, stepDuration, 0);
			assertEquals(legDistance, stepDistance, 0);
			legDuration = legDistance = stepDuration = stepDistance = 0;
		}
	}
	
	@Test
	public void testNumberOfPoint() throws Exception {
		List<Route> routes = api.getDirectionsBetween(fromLamaDiReno, toBologna);

		for (Route route : routes) {
			List<GeoPoint> summaryPoints = Polylines.decode(route.getOverviewPolyline());
			List<GeoPoint> points = DirectionUtil.getAllRoutePoints(route);
			assertEquals(summaryPoints.size(), points.size());

			for (int i = 0; i < summaryPoints.size(); i++) {
				assertEquals(summaryPoints.get(i), points.get(i));
			}
		}

	}

}
