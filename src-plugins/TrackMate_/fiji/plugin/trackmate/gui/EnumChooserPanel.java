package fiji.plugin.trackmate.gui;

import static fiji.plugin.trackmate.gui.TrackMateFrame.FONT;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import fiji.plugin.trackmate.InfoTextable;
import fiji.plugin.trackmate.segmentation.SegmenterType;

/**
 * A panel to let the user choose what displayer he wants to use.
 */
public class EnumChooserPanel <K extends InfoTextable> extends ActionListenablePanel {
	
	private static final long serialVersionUID = -2349025481368788479L;
	protected JLabel jLabelHeader;
	protected JComboBox jComboBoxChoice;
	protected K[] types;
	protected JLabel jLabelHelpText;
	protected String typeName;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * CONSTRUCTOR
	 */
	public EnumChooserPanel(K[] choices, K defaultChoice, String typeName) {
		super();
		this.typeName = typeName;
		this.types = choices;
		initGUI(defaultChoice);
	}

	public<L extends Enum<L> & InfoTextable> EnumChooserPanel(L defaultChoice, String typeName) {
		this((K[])defaultChoice.getDeclaringClass().getEnumConstants(), (K)defaultChoice, typeName);
	}

	/*
	 * PUBLIC METHODS
	 */
	
	public K getChoice() {
		return types[jComboBoxChoice.getSelectedIndex()];
	}
	

	/*
	 * PRIVATE METHODS
	 */

	private void initGUI(K defaultChoice) {
		try {
			this.setPreferredSize(new java.awt.Dimension(300, 470));
			this.setLayout(null);
			{
				jLabelHeader = new JLabel();
				this.add(jLabelHeader);
				jLabelHeader.setFont(FONT.deriveFont(Font.BOLD));
				jLabelHeader.setText("Select a "+typeName);
				jLabelHeader.setBounds(6, 20, 270, 16);
				jLabelHeader.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				String[] names = new String[types.length];
				int selected = 0;
				for (int i = 0; i < types.length; i++) {
					names[i] = types[i].toString();
					if (names[i].equals(types[i]))
						selected = i;
				}
				ComboBoxModel jComboBoxDisplayerChoiceModel = new DefaultComboBoxModel(names);
				jComboBoxChoice = new JComboBox();
				jComboBoxChoice.setModel(jComboBoxDisplayerChoiceModel);
				jComboBoxChoice.setSelectedIndex(selected);
				this.add(jComboBoxChoice);
				jComboBoxChoice.setFont(FONT);
				jComboBoxChoice.setBounds(12, 48, 270, 27);
				jComboBoxChoice.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						echo(types[jComboBoxChoice.getSelectedIndex()]);
					}
				});
			}
			{
				jLabelHelpText = new JLabel();
				jLabelHelpText.setFont(FONT.deriveFont(Font.ITALIC));
				jLabelHelpText.setBounds(12, 80, 270, 150);
				echo(defaultChoice);
				this.add(jLabelHelpText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void echo(K choice) {
		jLabelHelpText.setText(choice.getInfoText().replace("<br>", "").replace("<html>", "<html><p align=\"justify\">"));
	}
	

	/*
	 * MAIN METHOD
	 */
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		{
			EnumChooserPanel<SegmenterType> instance = new EnumChooserPanel<SegmenterType>(SegmenterType.LOG_SEGMENTER, "segmenter");
			frame.getContentPane().add(instance);
			instance.setPreferredSize(new java.awt.Dimension(300, 469));
		}
	}
	
	
	
}