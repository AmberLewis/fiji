package fiji.plugin;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.gui.MultiLineLabel;
import ij.plugin.BrowserLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIHelper
{
	final public static String myURL = "http://www.preibisch.net/";
	final public static String paperURL = "http://www.nature.com/nmeth/journal/v7/n6/full/nmeth0610-418.html";
	final public static String messagePaper = "Please note that the SPIM Registration is based on a publication.\n" +
											  "If you use it successfully for your research please be so kind to cite our work:\n" +
											  "Preibisch et al., Nature Methods (2010), 7(6):418-419\n";

	final public static String messageWebsite = "This plugin is written and maintained by Stephan Preibisch (click for webpage)\n";

	public static void addNatMethBeadsPaper( final GenericDialog gd ) { addNatMethBeadsPaper( gd, messagePaper ); }
	public static void addNatMethBeadsPaper( final GenericDialog gd, final String msg )  { addHyperLink( gd, msg, paperURL ); }

	public static void addWebsite( final GenericDialog gd ) { addWebsite( gd, messageWebsite ); }
	public static void addWebsite( final GenericDialog gd, final String msg ) { addHyperLink( gd, msg, myURL ); }
	
	public static final void addHyperLink( final GenericDialog gd, final String msg, final String url )
	{
		gd.addMessage( msg );
		MultiLineLabel text =  (MultiLineLabel) gd.getMessage();
		GUIHelper.addHyperLinkListener( text, url );		
	}
	
	public static final void addHyperLinkListener( final MultiLineLabel text, final String myURL )
	{
		if ( text != null && myURL != null )
		{
			text.addMouseListener( new MouseAdapter()
			{
				@Override
				public void mouseClicked( final MouseEvent e )
				{
					try
					{
						BrowserLauncher.openURL( myURL );
					}
					catch ( Exception ex )
					{
						IJ.log( "" + ex);
					}
				}
	
				@Override
				public void mouseEntered( final MouseEvent e )
				{
					text.setForeground( Color.BLUE );
					text.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
				}
	
				@Override
				public void mouseExited( final MouseEvent e )
				{
					text.setForeground( Color.BLACK );
					text.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
				}
			});
		}
	}

	/**
	 * A copy of Curtis's method
	 * 
	 * https://github.com/openmicroscopy/bioformats/blob/v4.4.8/components/loci-plugins/src/loci/plugins/util/WindowTools.java#L72
	 * 
	 * <dependency>
     * <groupId>${bio-formats.groupId}</groupId>
     * <artifactId>loci_plugins</artifactId>
     * <version>${bio-formats.version}</version>
     * </dependency>
	 * 
	 * @param pane
	 */
	public static void addScrollBars(Container pane) {
		GridBagLayout layout = (GridBagLayout) pane.getLayout();

		// extract components
		int count = pane.getComponentCount();
		Component[] c = new Component[count];
		GridBagConstraints[] gbc = new GridBagConstraints[count];
		for (int i = 0; i < count; i++) {
			c[i] = pane.getComponent(i);
			gbc[i] = layout.getConstraints(c[i]);
		}

		// clear components
		pane.removeAll();
		layout.invalidateLayout(pane);

		// create new container panel
		Panel newPane = new Panel();
		GridBagLayout newLayout = new GridBagLayout();
		newPane.setLayout(newLayout);
		for (int i = 0; i < count; i++) {
			newLayout.setConstraints(c[i], gbc[i]);
			newPane.add(c[i]);
		}

		// HACK - get preferred size for container panel
		// NB: don't know a better way:
		// - newPane.getPreferredSize() doesn't work
		// - newLayout.preferredLayoutSize(newPane) doesn't work
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		f.add(newPane, BorderLayout.CENTER);
		f.pack();
		final Dimension size = newPane.getSize();
		f.remove(newPane);
		f.dispose();

		// compute best size for scrollable viewport
		size.width += 25;
		size.height += 15;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int maxWidth = 7 * screen.width / 8;
		int maxHeight = 3 * screen.height / 4;
		if (size.width > maxWidth)
			size.width = maxWidth;
		if (size.height > maxHeight)
			size.height = maxHeight;

		// create scroll pane
		ScrollPane scroll = new ScrollPane() {
			private static final long serialVersionUID = 1L;

			public Dimension getPreferredSize() {
				return size;
			}
		};
		scroll.add(newPane);

		// add scroll pane to original container
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		layout.setConstraints(scroll, constraints);
		pane.add(scroll);
	}	

	/**
	 * Removes any of those characters from a String: (, ), [, ], {, }, <, >
	 * 
	 * @param entry input (with brackets)
	 * @return input, but without any brackets
	 */
	public static String removeBrackets( String entry )
	{
		return removeSequences( entry, new String[] { "(", ")", "{", "}", "[", "]", "<", ">" } );
	}
	
	public static String removeSequences( String entry, final String[] sequences )
	{
		while ( contains( entry, sequences ) )
		{
			for ( final String s : sequences )
			{
				final int index = entry.indexOf( s );

				if ( index == 0 )
					entry = entry.substring( s.length(), entry.length() );
				else if ( index == entry.length() - s.length() )
					entry = entry.substring( 0, entry.length() - s.length() );
				else if ( index > 0 )
					entry = entry.substring( 0, index ) + entry.substring( index + s.length(), entry.length() );
			}
		}

		return entry;		
	}
	
	public static boolean contains( final String entry, final String[] sequences )
	{
		for ( final String seq : sequences )
			if ( entry.contains( seq ) )
				return true;
		
		return false;
	}	
}
