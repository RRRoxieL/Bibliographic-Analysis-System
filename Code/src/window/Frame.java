package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
/*
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.painter.SpecularWaveGradientPainter;
import org.jvnet.substance.painter.text.*;
import org.jvnet.substance.painter.*;
import org.jvnet.substance.skin.*;
import org.jvnet.substance.theme.*;
*/

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.painter.SpecularWaveGradientPainter;
import org.jvnet.substance.skin.CremeCoffeeSkin;
import org.jvnet.substance.skin.SubstanceSaharaLookAndFeel;
import org.jvnet.substance.theme.SubstanceEbonyTheme;
import java.awt.GridBagLayout;



public class Frame extends JFrame{

	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	private JMenu menu2;
	private JMenuItem menuItem3;
	private JMenuItem menuItem4;
	private JMenuItem menuItem5;
	private JMenu menu3;
	private JMenuItem menuItem6;
	private JMenuItem menuItem7;
	private JMenuItem menuItem8;
	private JPanel contentPane;
	public static String filepath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		/*java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				 try {
					
			            UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
			            JFrame.setDefaultLookAndFeelDecorated(true);
			            JDialog.setDefaultLookAndFeelDecorated(true);	           
			            SubstanceLookAndFeel.setCurrentTheme(new SubstanceEbonyTheme());
			            SubstanceLookAndFeel.setSkin(new CremeCoffeeSkin());
			            SubstanceLookAndFeel.setCurrentGradientPainter(new SpecularWaveGradientPainter());
			            SubstanceLookAndFeel.setCurrentButtonShaper(new org.jvnet.substance.button.ClassicButtonShaper());
			          
			           
			            } 
				 catch (Exception e) {
					 System.err.println("Something went wrong!");
					 }
				 }
			});
		*/
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 10, 1500,1000);
		setTitle("\u6587\u732E\u9898\u5F55\u5206\u6790\u8F6F\u4EF6");
		setForeground(new Color(0, 0, 0));
		setBackground(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Homepage hp=new Homepage();
		GridBagLayout gridBagLayout = (GridBagLayout) hp.getLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		contentPane = hp;
		setContentPane(contentPane);
		
		//添加菜单栏
		menuBar = new JMenuBar();
		menuBar.setForeground(new Color(102, 51, 0));
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 50));
		setJMenuBar(menuBar);
		
		menu1 = new JMenu("\u6587\u4EF6");
		menu1.setBackground(UIManager.getColor("CheckBox.background"));
		menu1.setForeground(new Color(0, 0, 0));
		menu1.setHorizontalAlignment(SwingConstants.LEFT);
		menu1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 28));
		menuBar.add(menu1);
		
		//菜单：文件——导入文件，统计出关键词、机构、作者的频次和共现信息，写入数据库中以备查找
		menuItem1 = new JMenuItem("\u5BFC\u5165\u6587\u4EF6");
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {     
				JFileChooser chooser = new JFileChooser();               //设置选择器
				chooser.setMultiSelectionEnabled(true);                  //设为多选
				int returnVal = chooser.showOpenDialog(menuItem1);        //是否打开文件选择框
				if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
					filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
					KW kw=new KW();
					kw.setfilepath(filepath);
					kw.countfrequency();
					kw.countcooccurrence();
					AU au=new AU();
					au.setfilepath(filepath);
					au.countcooccurrence();
					au.countfrequency();
					IN ins=new IN();
					ins.setfilepath(filepath);
					ins.countfrequency();	
					ins.countcooccurrence();
					
					JOptionPane.showMessageDialog(null, "导入成功！", "", JOptionPane.INFORMATION_MESSAGE);
					}
				}
		});
		menuItem1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu1.add(menuItem1);
		
		//菜单：文件——返回首页
		menuItem2 = new JMenuItem("\u8FD4\u56DE\u9996\u9875");
		menuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Homepage hp=new Homepage();
				contentPane.removeAll();
				setContentPane(hp);
				revalidate();
			}
		});
		
		menuItem2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu1.add(menuItem2);
		
		menu2 = new JMenu("\u7EDF\u8BA1");
		menu2.setForeground(new Color(0, 0, 0));
		menu2.setBackground(UIManager.getColor("CheckBox.background"));
		menu2.setHorizontalAlignment(SwingConstants.LEFT);
		menu2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 28));
		menuBar.add(menu2);
		
		menuItem3 = new JMenuItem("\u5173\u952E\u8BCD\u7EDF\u8BA1");
		menuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				KeywordStatisticsPanel ksp=new KeywordStatisticsPanel();		
				contentPane.removeAll();
				ksp.setFilepath(filepath);
				setContentPane(ksp);
				revalidate();
			}
		});
		menuItem3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu2.add(menuItem3);
		
		menuItem4 = new JMenuItem("\u4F5C\u8005\u7EDF\u8BA1");
		menuItem4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AuthorStatisticsPanel asp=new AuthorStatisticsPanel();		
				contentPane.removeAll();
				asp.setFilepath(filepath);
				setContentPane(asp);
				revalidate();
			}
		});
		menuItem4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu2.add(menuItem4);
		
		menuItem5 = new JMenuItem("\u673A\u6784\u7EDF\u8BA1");
		menuItem5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InstitutionStatisticsPanel isp=new InstitutionStatisticsPanel();		
				contentPane.removeAll();
				isp.setFilepath(filepath);
				setContentPane(isp);
				revalidate();
			}
		});
		menuItem5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu2.add(menuItem5);
		
		menu3 = new JMenu("\u5206\u6790");
		menu3.setBackground(UIManager.getColor("CheckBox.background"));
		menu3.setForeground(new Color(0, 0, 0));
		menu3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 28));
		menuBar.add(menu3);
		
		menuItem6 = new JMenuItem("\u5173\u952E\u8BCD\u5206\u6790");
		menuItem6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeywordAnalysisPanel kap=new KeywordAnalysisPanel();
				kap.setFilepath(filepath);
				contentPane.removeAll();
				setContentPane(kap);
				revalidate();
				
			}
		});
		menuItem6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu3.add(menuItem6);
		
		menuItem7 = new JMenuItem("\u4F5C\u8005\u5206\u6790");
		menuItem7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AuthorAnalysisPanel aap=new AuthorAnalysisPanel();
				aap.setFilepath(filepath);
				contentPane.removeAll();
				setContentPane(aap);
				revalidate();
			}
		});
		menuItem7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu3.add(menuItem7);
		
		menuItem8 = new JMenuItem("\u673A\u6784\u5206\u6790");
		menuItem8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    InstitutionAnalysisPanel iap=new InstitutionAnalysisPanel();
				iap.setFilepath(filepath);
				contentPane.removeAll();
				setContentPane(iap);
				revalidate();
			}
		});
		menuItem8.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		menu3.add(menuItem8);
		
		
	}

}
