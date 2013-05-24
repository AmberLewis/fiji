package fiji.plugin.trackmate.features.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fiji.plugin.trackmate.Dimension;
import fiji.plugin.trackmate.FeatureModel;
import fiji.plugin.trackmate.TrackMateModel;

public class TrackIndexAnalyzer implements TrackAnalyzer {

	/** The key for this analyzer. */
	public static final String 		KEY = "Track index";
	/** The key for the feature TRACK_INDEX */
	public static final String TRACK_INDEX = "TRACK_INDEX";
	public static final String TRACK_ID = "TRACK_ID";

	private static final List<String> FEATURES = new ArrayList<String>(1);
	private static final Map<String, String> FEATURE_NAMES = new HashMap<String, String>(1);
	private static final Map<String, String> FEATURE_SHORT_NAMES = new HashMap<String, String>(1);
	private static final Map<String, Dimension> FEATURE_DIMENSIONS = new HashMap<String, Dimension>(1);
	
	static {
		FEATURES.add(TRACK_INDEX);
		FEATURES.add(TRACK_ID);

		FEATURE_NAMES.put(TRACK_INDEX, "Track index");
		FEATURE_NAMES.put(TRACK_ID, "Track ID");

		FEATURE_SHORT_NAMES.put(TRACK_INDEX, "Index");
		FEATURE_SHORT_NAMES.put(TRACK_ID, "ID");

		FEATURE_DIMENSIONS.put(TRACK_INDEX, Dimension.NONE);
		FEATURE_DIMENSIONS.put(TRACK_ID, Dimension.NONE);
	}
	
	private final TrackMateModel model;
	private long processingTime;
	
	public TrackIndexAnalyzer(final TrackMateModel model) {
		this.model = model;
	}
	
	/**
	 * {@link TrackIndexAnalyzer} is not local, since the indices are re-arranged according to names.
	 */
	@Override
	public boolean isLocal() {
		return false;
	}
	
	@Override
	public void process(Collection<Integer> trackIDs) {
		long start = System.currentTimeMillis();
		FeatureModel fm = model.getFeatureModel();
		int index = 0;
		for (Integer trackID : trackIDs) {
			fm.putTrackFeature(trackID, TRACK_INDEX, Double.valueOf(index++));
			fm.putTrackFeature(trackID, TRACK_ID, Double.valueOf(trackID));
		}
		long end = System.currentTimeMillis();
		processingTime = end - start;
	}

	@Override
	public long getProcessingTime() {
		return processingTime;
	}
	
	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public List<String> getFeatures() {
		return FEATURES;
	}

	@Override
	public Map<String, String> getFeatureShortNames() {
		return FEATURE_SHORT_NAMES;
	}

	@Override
	public Map<String, String> getFeatureNames() {
		return FEATURE_NAMES;
	}

	@Override
	public Map<String, Dimension> getFeatureDimensions() {
		return FEATURE_DIMENSIONS;
	}

	/**
	 * Ignored. This analyzer is single-threaded.
	 */
	@Override
	public void setNumThreads() {
	}

	/**
	 * Ignored. This analyzer is single-threaded.
	 */
	@Override
	public void setNumThreads(int numThreads) {}

	/**
	 * Ignore. This analyzer is single-threaded.
	 */
	@Override
	public int getNumThreads() {
		return 1;
	}
}
