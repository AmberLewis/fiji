package fiji.plugin.trackmate.gui;

import java.awt.Component;

import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.TrackMateModel;
import fiji.plugin.trackmate.TrackMate_;
import fiji.plugin.trackmate.util.TMUtils;
import fiji.plugin.trackmate.visualization.hyperstack.HyperStackDisplayer;
import fiji.plugin.trackmate.visualization.hyperstack.SpotEditTool;

public class DetectorDescriptor implements WizardPanelDescriptor {
	
	public static final String DESCRIPTOR = "DetectionPanel";
	protected LogPanel logPanel;
	protected TrackMate_ plugin;
	protected TrackMateWizard wizard;
	protected Logger logger;
	protected Thread motherThread;

	@Override
	public void setWizard(TrackMateWizard wizard) { 
		this.wizard = wizard;
		this.logPanel = wizard.getLogPanel();
		this.logger = wizard.getLogger();
	}

	@Override
	public void setPlugin(TrackMate_ plugin) {
		this.plugin = plugin;
	}

	@Override
	public Component getComponent() {
		return logPanel;
	}

	@Override
	public String getDescriptorID() {
		return DESCRIPTOR;
	}
	
	@Override
	public String getComponentID() {
		return LogPanel.DESCRIPTOR;
	}

	@Override
	public String getNextDescriptorID() {
		return InitFilterDescriptor.DESCRIPTOR;
	}

	@Override
	public String getPreviousDescriptorID() {
		return DetectorConfigurationPanelDescriptor.DESCRIPTOR;
	}

	@Override
	public void aboutToDisplayPanel() {	}

	@Override
	public void displayingPanel() {
		wizard.setNextButtonEnabled(false);
		final Settings settings = plugin.getModel().getSettings();
		logger.log("Starting detection using "+settings.detectorFactory.toString()+"\n", Logger.BLUE_COLOR);
		logger.log("with settings:\n");
		logger.log(TMUtils.echoMap(settings.detectorSettings, 2));
		motherThread = new Thread("TrackMate detection mother thread") {
			public void run() {
				long start = System.currentTimeMillis();
				try {
					plugin.execDetection();
					plugin.computeSpotFeatures(true);
					TrackMateModel model = plugin.getModel();
					model.setFilteredSpots(model.getSpots(), false);
					HyperStackDisplayer displayer = new HyperStackDisplayer(model);
					displayer.render();
					displayer.refresh();
				} catch (Exception e) {
					logger.error("An error occured:\n"+e+'\n');
					e.printStackTrace(logger);
				} finally {
					wizard.setNextButtonEnabled(true);
					long end = System.currentTimeMillis();
					logger.log(String.format("Detection done in %.1f s.\n", (end-start)/1e3f), Logger.BLUE_COLOR);
				}
				motherThread = null;
			}
		};
		motherThread.start();
	}

	@Override
	public synchronized void aboutToHidePanel() {
		final Thread thread = motherThread;
		if (thread != null) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException exc) {
				// ignore
			}
		}
	}
}
