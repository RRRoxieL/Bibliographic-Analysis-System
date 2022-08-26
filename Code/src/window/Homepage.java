package window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

public class Homepage extends JPanel {
	
	private JPanel contentPane;
	private JTextArea txtrn;
	/**
	 * Create the panel.
	 */
	public Homepage() {
				
		setForeground(UIManager.getColor("CheckBox.highlight"));
		setToolTipText("");
		//setBackground(new Color(248, 248, 255));
		setBorder(null);
		setBounds(0,0,1500,1000);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{80, 1500, 80, 0, 0};
		gbl_contentPane.rowHeights = new int[]{900, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gbl_contentPane);
		
		PicPanel pic=new PicPanel();
		GridBagConstraints gbc_pic = new GridBagConstraints();
		gbc_pic.insets = new Insets(0, 0, 0, 5);
		gbc_pic.fill = GridBagConstraints.BOTH;
		gbc_pic.gridx = 1;
		gbc_pic.gridy = 0;
		add(pic, gbc_pic);
	}
	
}
