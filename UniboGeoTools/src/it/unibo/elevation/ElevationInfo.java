package it.unibo.elevation;

import it.unibo.entity.GeoPoint;
import it.unibo.util.GeoUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Collect some elevation information about a given set of points
 * 
 * @author mone
 * 
 */
public class ElevationInfo {

	public static final double DEFAULT_THREASHOLD = 2;

	private double downhill;
	private double uphill;
	private double flat;

	private double totalDownhill;
	private double totalUphill;
	private double totalFlat;
	private double totalDrop;

	private double averageDownhillSlope;
	private double averageUphillSlope;
	private double averageFlatMarginalSlope;

	private double averageSlope;

	private double maxDownhillSlope;
	private double maxUphillSlope;

	private double threshold;

	private double totalDistance;

	private int precision;

	public ElevationInfo(List<GeoPoint> points) {
		this(points, DEFAULT_THREASHOLD);
	}

	public ElevationInfo(List<GeoPoint> points, double threshold) {
		this.threshold = threshold;
		totalDistance = 0;
		precision = 0;
		downhill = 0;
		uphill = 0;
		flat = 0;

		averageDownhillSlope = 0;
		averageUphillSlope = 0;
		averageFlatMarginalSlope = 0;

		averageSlope = 0;

		maxDownhillSlope = 0;
		maxUphillSlope = 0;

		GeoPoint prev = null;

		for (GeoPoint point : points) {
			if (prev != null) {
				double slope = GeoUtil.getSlopePercentage(prev, point);
				double distance = GeoUtil.getEquirectangularApproximationDistance(prev, point);

				averageSlope += slope * distance;
				totalDistance += distance;

				if (slope < -threshold) {
					downhill += distance;
					averageDownhillSlope += slope * distance;

					if (slope < maxDownhillSlope) {
						maxDownhillSlope = slope;
					}
				} else if (slope > threshold) {
					uphill += distance;
					averageUphillSlope += slope * distance;

					if (slope > maxUphillSlope) {
						maxUphillSlope = slope;
					}
				} else {
					flat += distance;
					averageFlatMarginalSlope += slope * distance;
				}

			}
			prev = point;
		}

		averageDownhillSlope /= downhill;
		averageUphillSlope /= uphill;
		averageFlatMarginalSlope /= flat;
		averageSlope /= totalDistance;

		totalDownhill =  downhill * averageDownhillSlope /100;
		totalUphill =  uphill * averageUphillSlope /100;
		totalFlat =  flat * averageFlatMarginalSlope /100;
		totalDrop = totalDownhill + totalUphill + totalFlat;
	}

	/**
	 * Distance traveled downhill in m
	 */
	public double getDownhill() {
		return round(downhill, precision);
	}

	/**
	 * Distance traveled uphill in m
	 */
	public double getUphill() {
		return round(uphill, precision);
	}

	/**
	 * Distance traveled flat in m
	 */
	public double getFlat() {
		return round(flat, precision);
	}

	/**
	 * Maximum downhill slope encountered in percentage
	 */
	public double getMaxDownhillSlope() {
		return round(maxDownhillSlope, precision);
	}

	/**
	 * Maximum Uphill slope encountered in percentage
	 */
	public double getMaxUphillSlope() {
		return round(maxUphillSlope, precision);
	}

	/**
	 * Average downhill slope in percentage
	 */
	public double getAverageDownhillSlope() {
		return round(averageDownhillSlope, precision);
	}

	/**
	 * Average uphill slope in percentage
	 */
	public double getAverageUphillSlope() {
		return round(averageUphillSlope, precision);
	}

	/**
	 * Average flat marginal slope in percentage
	 */
	public double getAverageFlatMarginalSlope() {
		return round(averageFlatMarginalSlope, precision);
	}

	/**
	 * Average total average slope in percentage
	 */
	public double getAverageSlope() {
		return round(averageSlope, precision);
	}

	/**
	 * Is the threshold within which a slope is considered flat in percentage.
	 * <p>
	 * <b>downhill < -treshold <= flat <= treshold < uphill
	 */
	public double getThreshold() {
		return round(threshold, precision);
	}

	/**
	 * Total distance from strat point to end point in m
	 */
	public double getTotalDistance() {
		return round(totalDistance, precision);
	}

	
	public double getTotalDownhill() {
		return round(totalDownhill, precision);
	}

	public double getTotalFlat() {
		return round(totalFlat, precision);
	}

	public double getTotalUphill() {
		return round(totalUphill, precision);
	}
	
	public double getTotalDrop() {
		return totalDrop;
	}

	public int getPrecision() {
		return precision;
	}

	/**
	 * The number of decimal places after the decimal point
	 * 
	 * @param precision: a number o decimal places or 0 for not round
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public static double round(double num, int digit) {
		if (digit == 0) {
			return num;
		}
		return new BigDecimal(num).setScale(digit, RoundingMode.HALF_EVEN).doubleValue();
	}
}
