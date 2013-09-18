package it.unibo.elevation;

import static org.junit.Assert.*;
import it.unibo.direction.DirectionAPI;
import it.unibo.direction.google.GoogleDirectionApi;
import it.unibo.elevation.ElevationAPI;
import it.unibo.elevation.ElevationInfo;
import it.unibo.elevation.google.GoogleElevationAPI;
import it.unibo.entity.GeoPoint;
import it.unibo.entity.Route;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class ElevtionInfoTest {

	private static List<GeoPoint> pointsToMontesole;
	private static List<GeoPoint> pointsToMarzabotto;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DirectionAPI directionAPI = new GoogleDirectionApi();
		ElevationAPI elevationAPI = new GoogleElevationAPI();
		List<Route> routesToMontesole = directionAPI.getDirectionsBetween("Pian di Venola, Marzabotto, BO", "Montesole, Marzabotto, BO");
		List<Route> routesToMarzabotto = directionAPI.getDirectionsBetween("Via Porrettana, Casagrande BO", "Strada Statale 64 Marzabotto BO");
		pointsToMontesole = elevationAPI.getElevations(routesToMontesole.get(0).getOverviewPolyline());
		pointsToMarzabotto = elevationAPI.getElevations(routesToMarzabotto.get(0).getOverviewPolyline());
	}

	@Test
	public void testGetElevationInfo() {
		ElevationInfo montesoleInfo = new ElevationInfo(pointsToMontesole);
		ElevationInfo marzabottoInfo = new ElevationInfo(pointsToMarzabotto);

		assertTrue(montesoleInfo.getUphill() > montesoleInfo.getDownhill());
		assertTrue(montesoleInfo.getUphill() > montesoleInfo.getFlat());
		assertTrue(montesoleInfo.getAverageUphillSlope() > montesoleInfo.getAverageDownhillSlope());
		assertTrue(montesoleInfo.getAverageUphillSlope() > montesoleInfo.getAverageFlatMarginalSlope());
		assertTrue(montesoleInfo.getAverageSlope() > 0);

		GeoPoint startMontesole = pointsToMontesole.get(0);
		GeoPoint endMontesole = pointsToMontesole.get(pointsToMontesole.size() - 1);
		double totalMontesoleDrop = endMontesole.getElevation() - startMontesole.getElevation();
		assertTrue(montesoleInfo.getTotalUphill() > montesoleInfo.getTotalDownhill());
		assertEquals(totalMontesoleDrop, montesoleInfo.getTotalDrop(), 0.000001);

		assertTrue(marzabottoInfo.getFlat() > marzabottoInfo.getUphill());
		assertTrue(marzabottoInfo.getFlat() > marzabottoInfo.getDownhill());
		assertTrue(marzabottoInfo.getAverageFlatMarginalSlope() > marzabottoInfo.getAverageDownhillSlope());
		assertTrue(marzabottoInfo.getAverageFlatMarginalSlope() < marzabottoInfo.getAverageUphillSlope());
		assertTrue(marzabottoInfo.getAverageSlope() < marzabottoInfo.getThreshold());
		assertTrue(marzabottoInfo.getAverageSlope() > -marzabottoInfo.getThreshold());

		assertTrue(marzabottoInfo.getTotalDistance() < montesoleInfo.getTotalDistance());
		
		GeoPoint startMarzabotto = pointsToMarzabotto.get(0);
		GeoPoint endMarzabotto = pointsToMarzabotto.get(pointsToMarzabotto.size() - 1);
		double totalMarzabottoDrop = endMarzabotto.getElevation() - startMarzabotto.getElevation();
		assertTrue(marzabottoInfo.getTotalFlat() > marzabottoInfo.getTotalDownhill());
		assertEquals(totalMarzabottoDrop, marzabottoInfo.getTotalDrop(), 0.000001);

		montesoleInfo.setPrecision(2);

		printEevationInfo("Monte Sole", montesoleInfo);
		System.out.println("********************************");
		printEevationInfo("Marzabotto", marzabottoInfo);

	}

	private static void printEevationInfo(String label, ElevationInfo info) {
		System.out.println(label + ":");
		System.out.println("total distance: " + info.getTotalDistance());
		System.out.println("downhill: " + info.getDownhill());
		System.out.println("uphill: " + info.getUphill());
		System.out.println("flat: " + info.getFlat());
		System.out.println("-------------------");
		System.out.println("average downhill: " + info.getAverageDownhillSlope());
		System.out.println("average uphill: " + info.getAverageUphillSlope());
		System.out.println("average flat: " + info.getAverageFlatMarginalSlope());
		System.out.println("average slope: " + info.getAverageSlope());
		System.out.println("-------------------");
		System.out.println("total downhill: " + info.getTotalDownhill());
		System.out.println("total uphill: " + info.getTotalUphill());
		System.out.println("total flat: " + info.getTotalFlat());
		System.out.println("total drop: " + info.getTotalDrop());
		System.out.println("-------------------");
		System.out.println("maxDownhillSlope: " + info.getMaxDownhillSlope());
		System.out.println("maxUphillSlope: " + info.getMaxUphillSlope());
	}

}
