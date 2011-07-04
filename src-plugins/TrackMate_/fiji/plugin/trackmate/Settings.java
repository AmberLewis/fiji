package fiji.plugin.trackmate;

import fiji.plugin.trackmate.segmentation.DogSegmenter;
import fiji.plugin.trackmate.segmentation.LogSegmenter;
import fiji.plugin.trackmate.segmentation.LogSegmenterSettings;
import fiji.plugin.trackmate.segmentation.PeakPickerSegmenter;
import fiji.plugin.trackmate.segmentation.SegmenterSettings;
import fiji.plugin.trackmate.segmentation.SegmenterType;
import fiji.plugin.trackmate.segmentation.SpotSegmenter;
import fiji.plugin.trackmate.tracking.FastLAPTracker;
import fiji.plugin.trackmate.tracking.LAPTracker;
import fiji.plugin.trackmate.tracking.SpotTracker;
import fiji.plugin.trackmate.tracking.TrackerSettings;
import fiji.plugin.trackmate.tracking.TrackerType;
import ij.ImagePlus;
import mpicbg.imglib.type.numeric.RealType;

/**
 * This class is used to store user settings for the {@link TrackMate_} plugin.
 * It is simply made of public fields 
 */
public class Settings {
	
	public static final Settings DEFAULT = new Settings();
			
	/** The ImagePlus to operate on. */
	public ImagePlus imp;
	// Crop cube
	public int tstart;
	public int tend;
	public int xstart;
	public int xend;
	public int ystart;
	public int yend;
	public int zstart;
	public int zend;
	// Image info
	public float dt 	= 1;
	public float dx 	= 1;
	public float dy 	= 1;
	public float dz 	= 1;
	public int width;
	public int height;
	public int nslices;
	public int nframes;
	public String imageFolder 		= "";
	public String imageFileName 	= "";
	public String timeUnits 		= "frames";
	public String spaceUnits 		= "pixels";
	
	public SegmenterType segmenterType = SegmenterType.PEAKPICKER_SEGMENTER;
	public TrackerType trackerType = TrackerType.LAP_TRACKER;
	
	public SegmenterSettings segmenterSettings = null;
	public TrackerSettings trackerSettings = null;
	
	/*
	 * METHODS
	 */
	
	/**
	 * Return a new {@link SpotSegmenter} as selected in this settings object.
	 */
	public <T extends RealType<T>> SpotSegmenter<T> getSpotSegmenter() {
		switch(segmenterType) {
		case LOG_SEGMENTER:
			return new LogSegmenter<T>((LogSegmenterSettings) segmenterSettings);
		case PEAKPICKER_SEGMENTER:
			return new PeakPickerSegmenter<T>(segmenterSettings);
		case DOG_SEGMENTER:
			return new DogSegmenter<T>(segmenterSettings);
		}
		return null;
	}
	
	/**
	 * Return a new {@link SpotTracker} as selected in this settings object, initialized for the given model.
	 */
	public SpotTracker getSpotTracker(TrackMateModel model) {
		return trackerType.getSpotTracker(model);
	}
	
	/**
	 * A utility method that returns a new float array with the 3 elements building the spatial calibration
	 * (pixel size).
	 */
	public float[] getCalibration() {
		return new float[] {dx, dy, dz};
	}
	
	
	@Override
	public String toString() {
		String str = ""; 
		if (null == imp) {
			str = "Image with:\n";
		} else {
			str = "For image: "+imp.getShortTitle()+'\n';			
		}
		str += String.format("  X = %4d - %4d, dx = %g %s\n", xstart, xend, dx, spaceUnits);
		str += String.format("  Y = %4d - %4d, dy = %g %s\n", ystart, yend, dy, spaceUnits);
		str += String.format("  Z = %4d - %4d, dz = %g %s\n", zstart, zend, dz, spaceUnits);
		str += String.format("  T = %4d - %4d, dt = %g %s\n", tstart, tend, dt, timeUnits);
		return str;
	}

}