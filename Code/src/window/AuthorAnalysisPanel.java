package window;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AuthorAnalysisPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	int exist; 
	public static String filepath="d:/temp/database1.mdb";
	
	private JTextField textField1;
	private JScrollPane scrollpane;
	private DefaultTableModel model;
	private JTable table;

	public void setFilepath(String path) {
		filepath = path;
	}
	public AuthorAnalysisPanel() {
		setBounds(0,0,1500,1000);
		JPanel copanel = new JPanel();
		copanel.setBackground(SystemColor.menu);
		copanel.setBorder(null);
		//copanel.setBounds(0,0,1500,1000);
		add(copanel);
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
		
		JLabel lblTopN = new JLabel("\u8BF7\u8F93\u5165\u6570\u5B57\uFF0C\u67E5\u8BE2\u7684\u4F5C\u8005\u5171\u73B0\u9891\u6B21\u7684Top N");
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
					author_co(n);
					scrollpane.setVisible(true);
					scrollpane.setBounds(175,180,1306,595);
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
				author_co(n);
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
		
		model=new DefaultTableModel(new Object[][] {},new String[] {"排名", "作者", "作者", "合著次数"});
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

//查询作者两两共现的topN
public void author_co(int n) {
	try {
		
		
		model.setRowCount(0);
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
		//实现同频次排名
		String sql1="select Frequency from AuthorCooccurrence group by Frequency order by Frequency desc";
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
		//查询频次topN的作者合作信息
		String sql2="select AuthorName,Frequency from AuthorCooccurrence where frequency>='"+result+"' order by Frequency desc" ;
		PreparedStatement stmt2=conn.prepareStatement(sql2);
		ResultSet rs2=stmt2.executeQuery();
		int id=1;
		int row=0;
		while(rs2.next())
		{
			String institutions=rs2.getString(1);
			String []AA={};
			if(institutions!=null) {
				AA=institutions.split("[;]+");   //分割含有分号的字符串，将Keywords字段分行存入辅助表中
			}
			String au1,au2;
			au1=AA[0];
			au2=AA[1];
			int freq=rs2.getInt(2);
			if(row>0) {
				if(freq<Integer.parseInt(table.getValueAt(row-1, 3).toString())) {     //同频次排名
					id++;
				}
			}
			model.addRow(new Object[] {id,au1,au2,freq});  //向表格中添加一行
			row++;
		}
		
	} 
catch (ClassNotFoundException |SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();}	
	}			
}
