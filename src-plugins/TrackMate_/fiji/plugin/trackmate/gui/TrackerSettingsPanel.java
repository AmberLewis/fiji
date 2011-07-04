package fiji.plugin.trackmate.gui;

import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.tracking.SpotTracker;
import fiji.plugin.trackmate.tracking.TrackerSettings;
import fiji.plugin.trackmate.tracking.TrackerType;

/**
 * Mother class for tracker settings panel. Also offer a factory method to instantiate 
 * the correct panel pointed by a tracker type.
 * @author Jean-Yves Tinevez <jeanyves.tinevez@gmail.com> Jan 12, 2011
 *
 */
public abstract class TrackerSettingsPanel extends ActionListenablePanel {

	private static final long serialVersionUID = 6489221290360334663L;
	
	/** 
	 * Return a {@link TrackerSettingsPanel} that is able to configure the {@link SpotTracker}
	 * selected in the settings object.
	 */
	public static TrackerSettingsPanel createPanel(Settings settings) {
		TrackerType trackerType = settings.trackerType;
		TrackerSettings trackerSettings = settings.trackerSettings;
		if (null == trackerSettings) {
			trackerSettings = trackerType.createSettings();
			trackerSettings.spaceUnits = settings.spaceUnits;
			trackerSettings.timeUnits = settings.timeUnits;
		}
		return trackerType.createPanel(trackerSettings);
	}
	

	/**
	 * Update the {@link TrackerSettings} object given at the creation of this panel with the
	 * settings entered by the user on this panel. 
	 */
	public abstract TrackerSettings getSettings();
	
}
