package window;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

import java.awt.CardLayout;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;

public class KeywordAnalysisPanel extends JPanel {
	
	public static String filepath="d:/temp/database1.mdb";
	private JTextField textField1;
	private JTable table;
	DefaultTableModel model;
	DefaultTableModel model1;
	DefaultTableModel model2;
	DefaultTableModel model3;
	private JTextField textField2;
	private JTable authortable;
	private JTable journaltable;
	private JTable institutiontable;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JScrollPane scrollpane;
	private JScrollPane scrollpane1;
	private JScrollPane scrollpane2;
	private JScrollPane scrollpane3;
	private JPanel testpanel;
	private ArrayList items;
	int exist;     //用于标识关键词是否存在
	public void setFilepath(String path) {
		filepath=path;
	}
	public KeywordAnalysisPanel() {
		 
		
		items=new ArrayList();
	    label1=new JLabel();
		label2=new JLabel();
		label3=new JLabel();
		setLayout(new GridLayout(0, 1, 0, 0));
		setBounds(0,0,1500,1000);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 26));
		tabbedPane.setPreferredSize(new Dimension(200,200));
		tabbedPane.setBackground(SystemColor.inactiveCaption);
		add(tabbedPane);
		
		/*
		 * 第一个tab
		 */
		JPanel copanel = new JPanel();
		copanel.setBackground(SystemColor.menu);
		copanel.setBorder(null);
		tabbedPane.addTab("\u5173\u952E\u8BCD\u5171\u73B0\u9891\u6B21\uFF1ATop N", null, copanel, null);
		GridBagLayout gbl_copanel = new GridBagLayout();
		gbl_copanel.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_copanel.rowHeights = new int[]{40, 40, 30, 30, 40, 600, 25, 0, 0};
		gbl_copanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_copanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		copanel.setLayout(gbl_copanel);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 2;
		gbc_separator.gridy = 0;
		copanel.add(separator, gbc_separator);
		
		JLabel lblTopN = new JLabel("\u8BF7\u5728\u8F93\u5165\u6846\u8F93\u5165\u6570\u5B57\uFF0C\u67E5\u8BE2 Top N \u7684\u5173\u952E\u8BCD\u5171\u73B0\u4FE1\u606F");
		lblTopN.setForeground(new Color(0, 0, 0));
		lblTopN.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		lblTopN.setBackground(new Color(0, 0, 0));
		GridBagConstraints gbc_lblTopN = new GridBagConstraints();
		gbc_lblTopN.gridwidth = 5;
		gbc_lblTopN.insets = new Insets(0, 0, 5, 5);
		gbc_lblTopN.gridx = 1;
		gbc_lblTopN.gridy = 1;
		copanel.add(lblTopN, gbc_lblTopN);
		
		//输入框
		textField1 = new JTextField();
		textField1.setFont(new Font("黑体", Font.PLAIN, 22));
		textField1.setHorizontalAlignment(SwingConstants.LEFT);
		textField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					String input=textField1.getText();
					int n=Integer.parseInt(input);
					keyword_co(n);
					scrollpane.setVisible(true);
					scrollpane.setBounds(83,227,1306,595);
					
				}
			}
		});
		GridBagConstraints gbc_textField1 = new GridBagConstraints();
		gbc_textField1.insets = new Insets(0, 0, 5, 5);
		gbc_textField1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField1.gridx = 2;
		gbc_textField1.gridy = 3;
		copanel.add(textField1, gbc_textField1);
		textField1.setColumns(10);
		
		//查询按钮
		JButton button1 = new JButton("\u67E5\u8BE2");
		button1.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String input=textField1.getText();
				int n=Integer.parseInt(input);
				keyword_co(n);
				scrollpane.setVisible(true);
				scrollpane.setBounds(83,227,1306,595);
				
			}
		});
		
		GridBagConstraints gbc_button1 = new GridBagConstraints();
		gbc_button1.anchor = GridBagConstraints.EAST;
		gbc_button1.insets = new Insets(0, 0, 5, 5);
		gbc_button1.gridx = 4;
		gbc_button1.gridy = 3;
		copanel.add(button1, gbc_button1);
		
		model=new DefaultTableModel(new Object[][] {},new String[] {"排名", "关键词", "关键词", "共现频次"});
		GridBagConstraints gbc_table_1 = new GridBagConstraints();
		gbc_table_1.insets = new Insets(0, 0, 5, 5);
		gbc_table_1.fill = GridBagConstraints.BOTH;
		gbc_table_1.gridx = 3;
		gbc_table_1.gridy = 5;
		
		//存放表格的滚动框
		scrollpane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		scrollpane.setViewportBorder(null);
		scrollpane.setBorder(null);
		scrollpane.setVisible(false);
		copanel.add(scrollpane, gbc_scrollPane);
		
		//表格
		table =new JTable();
		table.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setBorder(null);
		table.setRowHeight(35);
		table.setModel(model);
		table.setBackground(SystemColor.inactiveCaptionBorder);
		table.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();  
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);
		scrollpane.setViewportView(table);
		
		
		/*
		 * 第二个tab
		 */
		
		exist=0;
		JPanel repanel = new JPanel();
		repanel.setBackground(SystemColor.menu);
		repanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
		});
		tabbedPane.addTab("\u5173\u952E\u8BCD\u5BF9\u5E94\u5173\u7CFB", null, repanel, null);
		GridBagLayout gbl_repanel = new GridBagLayout();
		gbl_repanel.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_repanel.rowHeights = new int[]{30, 40, 30, 30, 30, 600, 30, 40, 0, 0};
		gbl_repanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_repanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		repanel.setLayout(gbl_repanel);
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u5728\u6587\u672C\u6846\u4E2D\u8F93\u5165\u60A8\u60F3\u8981\u67E5\u770B\u7684\u5173\u952E\u8BCD");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		repanel.add(lblNewLabel, gbc_lblNewLabel);
		
		//文本框
		textField2 = new JTextField();
		textField2.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		textField2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					textField2.requestFocusInWindow();
					String input=textField2.getText();
					model1.setRowCount(0);
					model2.setRowCount(0);
					model3.setRowCount(0);
					keyword_author(input);	
					keyword_journal(input);
					keyword_institution(input);
					if(exist!=0) {
				    label1.setText("关键词\""+input+"\"对应的前十名的作者及频次如下：");
					label2.setText("关键词\""+input+"\"对应的前十名的期刊及频次如下：");
					label3.setText("关键词\""+input+"\"对应的前十名的机构及频次如下：");
					label1.setFont(new Font("微软雅黑", Font.PLAIN, 24));
					label2.setFont(new Font("微软雅黑", Font.PLAIN, 24));
					label3.setFont(new Font("微软雅黑", Font.PLAIN, 24));
					scrollpane1.setVisible(true);
					scrollpane2.setVisible(true);
					scrollpane3.setVisible(true);
					}
					exist=0;
				}
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 2;
		gbc_separator_2.gridy = 2;
		repanel.add(separator_2, gbc_separator_2);
		GridBagConstraints gbc_textField2 = new GridBagConstraints();
		gbc_textField2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField2.insets = new Insets(0, 0, 5, 5);
		gbc_textField2.gridx = 2;
		gbc_textField2.gridy = 3;
		repanel.add(textField2, gbc_textField2);
		
		AutoCompleteComponet acc1=new AutoCompleteComponet();
		items=acc1.draw("keyword", "keywordfrequency",filepath);
		acc1.setupAutoComplete(textField2, items);	
		textField2.setColumns(10);
		
		//确认按钮
		JButton button2 = new JButton("\u786E\u5B9A");
		button2.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		button2.addMouseListener(new MouseAdapter() {			

			@Override
			public void mouseClicked(MouseEvent e) {
				String input=textField2.getText();
				model1.setRowCount(0);
				model2.setRowCount(0);
				model3.setRowCount(0);
				keyword_author(input);	
				keyword_journal(input);
				keyword_institution(input);
				if(exist!=0) {
				    label1.setText("关键词\""+input+"\"对应的前十名的作者及频次如下：");
					label2.setText("关键词\""+input+"\"对应的前十名的期刊及频次如下：");
					label3.setText("关键词\""+input+"\"对应的前十名的机构及频次如下：");
					label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
					label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
					label3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
					scrollpane1.setVisible(true);
					scrollpane2.setVisible(true);
					scrollpane3.setVisible(true);
					}
					exist=0;
			}
		});
		GridBagConstraints gbc_button2 = new GridBagConstraints();
		gbc_button2.anchor = GridBagConstraints.EAST;
		gbc_button2.insets = new Insets(0, 0, 5, 5);
		gbc_button2.gridx = 4;
		gbc_button2.gridy = 3;
		repanel.add(button2, gbc_button2);
		
		
		//卡片区域
		JPanel cardpanel = new JPanel();
		CardLayout cardLayout = new CardLayout(); 
		GridBagConstraints gbc_cardpanel = new GridBagConstraints();
		gbc_cardpanel.gridwidth = 5;
		gbc_cardpanel.insets = new Insets(0, 0, 5, 5);
		gbc_cardpanel.fill = GridBagConstraints.BOTH;
		gbc_cardpanel.gridx = 1;
		gbc_cardpanel.gridy = 5;
		repanel.add(cardpanel, gbc_cardpanel);
		cardpanel.setLayout(cardLayout);
		
		
		String s=textField2.getText();
		//作者卡片
		JPanel panel1 = new JPanel();
		cardpanel.add("author",panel1);
		
		GridBagLayout gbl_panel1 = new GridBagLayout();
		gbl_panel1.columnWidths = new int[]{0, 0};
		gbl_panel1.rowHeights = new int[]{0, 0, 0};
		gbl_panel1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel1.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel1.setLayout(gbl_panel1);
		
		
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.insets = new Insets(0, 0, 5, 0);
		gbc_label1.gridx = 0;
		gbc_label1.gridy = 0;
		panel1.add(label1, gbc_label1);
		
		scrollpane1 = new JScrollPane();
		GridBagConstraints gbc_scrollpane1 = new GridBagConstraints();
		gbc_scrollpane1.fill = GridBagConstraints.BOTH;
		gbc_scrollpane1.gridx = 0;
		gbc_scrollpane1.gridy = 1;
		panel1.add(scrollpane1, gbc_scrollpane1);
		
		authortable = new JTable();
		authortable.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		authortable.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		r.setHorizontalAlignment(JLabel.CENTER);   
		authortable.setDefaultRenderer(Object.class, r);	
		authortable.setRowHeight(40);
		model1 = new DefaultTableModel(new Object[][] {},new String[] {"排名", "作者","频次"});
		authortable.setModel(model1);
		scrollpane1.setViewportView(authortable);
		scrollpane1.setVisible(false);
		
		
		//期刊卡片
		
		JPanel panel2 = new JPanel();
		cardpanel.add("journal",panel2);
		
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{0, 0};
		gbl_panel2.rowHeights = new int[]{0, 0, 0};
		gbl_panel2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.insets = new Insets(0, 0, 5, 0);
		gbc_label2.gridx = 0;
		gbc_label2.gridy = 0;
		panel2.add(label2, gbc_label2);
		
		scrollpane2 = new JScrollPane();
		GridBagConstraints gbc_scrollpane2 = new GridBagConstraints();
		gbc_scrollpane2.fill = GridBagConstraints.BOTH;
		gbc_scrollpane2.gridx = 0;
		gbc_scrollpane2.gridy = 1;
		panel2.add(scrollpane2, gbc_scrollpane2);
		
		journaltable = new JTable();
		journaltable.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		journaltable.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		journaltable.setRowHeight(40);
		r.setHorizontalAlignment(JLabel.CENTER);   
		journaltable.setDefaultRenderer(Object.class, r);	
		model2 = new DefaultTableModel(new Object[][] {},new String[] {"排名", "期刊","频次"});
		journaltable.setModel(model2);
		scrollpane2.setViewportView(journaltable);
		scrollpane2.setVisible(false);
		
		//机构卡片
		JPanel panel3 = new JPanel();
		cardpanel.add("institution",panel3);
		
		GridBagLayout gbl_panel3 = new GridBagLayout();
		gbl_panel3.columnWidths = new int[]{0, 0};
		gbl_panel3.rowHeights = new int[]{0, 0, 0};
		gbl_panel3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel3.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel3.setLayout(gbl_panel3);
		
		GridBagConstraints gbc_label3 = new GridBagConstraints();
		gbc_label3.insets = new Insets(0, 0, 5, 0);
		gbc_label3.gridx = 0;
		gbc_label3.gridy = 0;
		panel3.add(label3, gbc_label3);
		
		scrollpane3 = new JScrollPane();
		GridBagConstraints gbc_scrollpane3 = new GridBagConstraints();
		gbc_scrollpane3.fill = GridBagConstraints.BOTH;
		gbc_scrollpane3.gridx = 0;
		gbc_scrollpane3.gridy = 1;
		panel3.add(scrollpane3, gbc_scrollpane3);
		
		institutiontable = new JTable();
		institutiontable.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		institutiontable.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		institutiontable.setRowHeight(40);
		r.setHorizontalAlignment(JLabel.CENTER);   
		institutiontable.setDefaultRenderer(Object.class, r);	
		model3 = new DefaultTableModel(new Object[][] {},new String[] {"排名", "机构","频次"});
		institutiontable.setModel(model3);
		scrollpane3.setViewportView(institutiontable);
		scrollpane3.setVisible(false);
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 2;
		gbc_separator_1.gridy = 6;
		repanel.add(separator_1, gbc_separator_1);
		
		//卡片控制区：存放三个控制按钮
		JPanel controlpanel = new JPanel();
		GridBagConstraints gbc_controlpanel = new GridBagConstraints();
		gbc_controlpanel.gridwidth = 5;
		gbc_controlpanel.insets = new Insets(0, 0, 5, 5);
		gbc_controlpanel.fill = GridBagConstraints.BOTH;
		gbc_controlpanel.gridx = 1;
		gbc_controlpanel.gridy = 7;
		repanel.add(controlpanel, gbc_controlpanel);
		
		//作者按钮
		JButton btnNewButton1 = new JButton("\u4F5C\u8005");
		btnNewButton1.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		btnNewButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cardLayout.show(cardpanel, "author");
			}
		});
		controlpanel.add(btnNewButton1);
		
		//期刊按钮
		JButton btnNewButton2 = new JButton("\u671F\u520A");
		btnNewButton2.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		btnNewButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(cardpanel, "journal");
			}
		});
		controlpanel.add(btnNewButton2);
		
		//机构按钮
		JButton btnNewButton3 = new JButton("\u673A\u6784");
		btnNewButton3.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		btnNewButton3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(cardpanel, "institution");
			}
		});
		controlpanel.add(btnNewButton3);
		
	
	}
	//判断数据库中是否存在辅助表的方法
	public static boolean validateTableNameExist(String tableName) throws ClassNotFoundException, SQLException {         
		
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		Connection con=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);	
		
		ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);         
		if (rs.next()) {
			return true;
			}
		else {
			return false;
			}
		}
	
	//查询关键词两两共现的topN
	public void keyword_co(int n) {
		try {
			
			
			model.setRowCount(0);
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
			//实现同频次排名
			String sql1="select Frequency from keywordcooccurrence group by Frequency order by Frequency desc";
			PreparedStatement stmt1=conn.prepareStatement(sql1);
			ResultSet rs1=stmt1.executeQuery();
			int site=1;
			int result=1;
			while(rs1.next()) {
				if(site==n)
				{
					result=rs1.getInt(1);
					break;
				}
				site++;
			}
			//查询频次topN的关键词
			String sql2="select Keyword,Frequency from keywordcooccurrence where frequency>='"+result+"' order by Frequency desc" ;
			PreparedStatement stmt2=conn.prepareStatement(sql2);
			ResultSet rs2=stmt2.executeQuery();
			int id=1;
			int row=0;
			while(rs2.next())
			{
				String keywords=rs2.getString(1);
				String []AA={};
				if(keywords!=null) {
					AA=keywords.split("[;]+");   //分割含有分号的字符串，将Keywords字段分行存入辅助表中
				}
				String kw1,kw2;
				kw1=AA[0];
				kw2=AA[1];
				int freq=rs2.getInt(2);
				if(row>0) {
					if(freq<Integer.parseInt(table.getValueAt(row-1, 3).toString())) {     //同频次排名
						id++;
					}
				}
				model.addRow(new Object[] {id,kw1,kw2,freq});  //向表格中添加一行
				row++;
			}
			
		} 
	catch (ClassNotFoundException |SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}

	//查询关键词对应的前十名的作者
    public void keyword_author(String word) {

    	try {
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
    	
		//如果存在辅助表，先删除
		if(validateTableNameExist("assistant"))
		{  
		    
			String sql_clean="drop table assistant";
		    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
		    stmt_end.executeUpdate();
		}
		
		conn.setAutoCommit(false);
     //用search接收用户输入的关键词
	 String search=word;         
	 int length=search.length();
	 String correct=new String(search+"*");
	
	//先从源数据中模糊匹配关键词，减少工作量
	 String sql1="select Keywords,Author from metadata where Keywords like '*"+search+"*'";
	 PreparedStatement stmt1=conn.prepareStatement(sql1);
	 ResultSet rs1=stmt1.executeQuery(); 
	 ArrayList<String> list=new ArrayList<String>();
	//将关键词、作者分行，使每个关键词对应一个作者
	 while(rs1.next()){           
			String keyword=rs1.getString(1); 
			String author=rs1.getString(2);
			String []kw={};
			String []au={};
			if(keyword!=null) {
				kw=keyword.split("[;]+");   //分割含有分号的关键词字符串，存入数组中
			}
			if(author!=null) {
				au=author.split("[;]+");   //分割含有分号的作者字符串，存入数组中
			}
	
			
	        for(int i=0;i<kw.length;i++)
	    	    for(int j=0;j<au.length;j++)
	    	       list.add(kw[i]+"*"+au[j]);  }
	  
	 //精确筛选出关键词对应的全部作者，将结果（作者名）写入辅助表中
	 Statement stmt=conn.createStatement();
	 String prepare1="create table assistant(author varchar(150),counts int not null)"; 
	 stmt.executeUpdate(prepare1);
	 //在辅助表中插入数据		
	 String sql2="insert into assistant(author,counts) values(?,?)";
	 PreparedStatement stmt2=conn.prepareStatement(sql2);
	 for (int i=0; i<list.size(); i++)
	 {    String rs=list.get(i);
	      String s1=new String();
	    if(rs.length()>length)
	     {s1=rs.substring(0,length+1);}
	      
	 	if(s1.equals(correct)==true)	
	 	{   String s2=rs.substring(length+1);
	 	    
	 	    stmt2.setString(1,s2);
			stmt2.setInt(2,1);   
			stmt2.addBatch();			
	 	}	
	 } 
	    stmt2.executeBatch();
	  //输出前十名的作者
	    String sql3="select author,sum(counts) as frequency from assistant group by author order by sum(counts) desc";
		PreparedStatement stmt3=conn.prepareStatement(sql3);
		ResultSet rs3=stmt3.executeQuery();   
		int i=1;
	    while(rs3.next()&&i<=10)	
	    { 
	    	String result1=rs3.getString(1);
	    	String result2=rs3.getString(2);
	    	model1.addRow(new Object[] {i,result1,result2});
	    	i++;
	    }
    conn.commit();
    }

catch (ClassNotFoundException |SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}				
}
    
    //查询关键词对应的前十名的期刊
    public void keyword_journal(String word) {
    	try {
    		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
        	
			
			//如果存在辅助表，先删除
				if(validateTableNameExist("assistant"))
				{  
				    
					String sql_clean="drop table assistant";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				
				conn.setAutoCommit(false);
		     //用search接收用户输入的关键词	
			 String search=word;
			 int length=search.length();
			 String correct=word;
			
			//创建辅助表 
				Statement stmt=conn.createStatement();
				String prepare1="create table assistant(keyword varchar(150),journal varchar(150),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				
				//先从源数据中模糊匹配关键词，减少工作量
			    String sql1="select keywords, journal from metadata where keywords like '*"+search+"*'";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();   
				//在辅助表中插入数据		
				String sql2="insert into assistant(keyword,journal,counts) values(?,?,?)";
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				while(rs1.next()){           
					String kw=rs1.getString(1); 
					String jn=rs1.getString(2); 
					String []AA={};
					if(kw!=null) {
						AA=kw.split("[;]+");   //分割含有分号的字符串，将Keywords字段分行存入辅助表中
					}
					for(int i=0;i<AA.length;i++) {  
						if(AA[i].equals(correct)==true)
						//将期刊名写入分行后的keyword对应的字段中
						{stmt2.setString(1, AA[i]);
						exist++;
						stmt2.setString(2,jn);
						stmt2.setInt(3,1);
						stmt2.addBatch();}
					}				
				}
				if(exist!=0) 
				{stmt2.executeBatch();
				
			    //输出前十名的机构
			    String sql3="select journal,sum(counts) as frequency from assistant where keyword='"+search+"' group by journal order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();   
				int i=1;
			    while(rs3.next()&&i<=10)	
			    { 
			    	String result1=rs3.getString(1);
			    	String result2=rs3.getString(2);
			    	model2.addRow(new Object[] {i,result1,result2});
			    	i++;
			    }
		    conn.commit();
		    }
			 else {
				 JOptionPane.showMessageDialog(null, "关键词不存在！", "", JOptionPane.WARNING_MESSAGE);
			 }
			 
		
		}
			
		catch(ClassNotFoundException | SQLException e) {
			 System.out.println(e);
			}
    }
    
    //查询关键词对应的前十名的机构
    public void keyword_institution(String word) {
    	
    	try {
    		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);	
			
			//如果存在辅助表，先删除
				if(validateTableNameExist("assistant"))
				{  
				    
					String sql_clean="drop table assistant";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				
				conn.setAutoCommit(false);
		     //用search接收用户输入的关键词	
			 String search=word;
			 int length=search.length();
			 String correct=new String(search+"*");
			
			//先从源数据中模糊匹配关键词，减少工作量
			 String sql1="select Keywords,[Author Address] from metadata where keywords like '*"+search+"*'";
			 PreparedStatement stmt1=conn.prepareStatement(sql1);
			 ResultSet rs1=stmt1.executeQuery(); 
			 ArrayList<String> list=new ArrayList<String>();
			//将关键词、关键词分行，使每个关键词对应一个机构
			 while(rs1.next()){           
					String keyword=rs1.getString(1); 
					String institution=rs1.getString(2);
					String []kw={};
					String []ins={};
					if(keyword!=null) {
						kw=keyword.split("[;]+");   //分割含有分号的关键词字符串，存入数组中
					}
					if(institution!=null) {
						ins=institution.split("[;]+");   //分割含有分号的机构字符串，存入数组中
					}
			
					
			        for(int i=0;i<kw.length;i++)
			    	    for(int j=0;j<ins.length;j++)
			    	       list.add(kw[i]+"*"+ins[j]);  }
			  
			 //筛选出关键词对应的全部机构，将结果（机构名）写入辅助表中
			 Statement stmt=conn.createStatement();
			 String prepare1="create table assistant(institution varchar(150),counts int not null)"; 
			 stmt.executeUpdate(prepare1);
			 //在辅助表中插入数据		
			 String sql2="insert into assistant(institution,counts) values(?,?)";
			 PreparedStatement stmt2=conn.prepareStatement(sql2);
			 for (int i=0; i<list.size(); i++)
			 {    String rs=list.get(i);
			      String s1=new String();
			    if(rs.length()>length)
			      {s1=rs.substring(0,length+1);}
			      
			 	if(s1.equals(correct)==true)	
			 	{   String s2=rs.substring(length+1);
			 	    exist++;
			 	    stmt2.setString(1,s2);
					stmt2.setInt(2,1);   
					stmt2.addBatch();			
			 	}	
			 } if(exist!=0) {
			    stmt2.executeBatch();
			  //输出前十名的机构
			    String sql3="select institution,sum(counts) as frequency from assistant group by institution order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();   
				int i=1;
			    while(rs3.next()&&i<=10)	
			    { 
			    	String result1=rs3.getString(1);
			    	String result2=rs3.getString(2);
			    	model3.addRow(new Object[] {i,result1,result2});
			    	i++;
			    }
			    conn.commit();
			    }
			 
		   
			 else {
				 JOptionPane.showMessageDialog(null, "关键词不存在！", "", JOptionPane.WARNING_MESSAGE);
			 }
			 
		
		}
			
		catch(ClassNotFoundException | SQLException e) {
			 System.out.println(e);
			}
    }
}
