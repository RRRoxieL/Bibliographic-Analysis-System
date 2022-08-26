package window;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableColumn;

import java.awt.Insets;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AuthorStatisticsPanel extends JPanel {
	private JTextField textField;
	private JTable table1;
	private JTable table2;
	private DefaultTableModel model1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextArea textField_4;
	private JTextField textField_5;
	public static String filepath="d:/temp/database1.mdb";
	int selected_before=0;
	int freq_before1;
	String input1;
	ArrayList<String> items;
	private JTextField textField_1;
	/**
	 * Create the panel.
	 */
	public void setFilepath(String path) {
		filepath=path;    	
	}
	public AuthorStatisticsPanel() {		
		
		setBounds(0,0,1500,1000);
		items = new ArrayList<String>();	
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{601, 0};
		gridBagLayout.rowHeights = new int[]{438, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(SystemColor.inactiveCaption);
		tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 26));
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		add(tabbedPane, gbc_tabbedPane);
		
		
		/*
		 * 结果展示表格
		 */	
		model1=new DefaultTableModel(new Object[][] {},new String[] {"","名次","作者", "频次"});  //展示查询结果（topN个作者）		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();  
		r.setHorizontalAlignment(JLabel.CENTER);   
       
		
		/**
		 * TopN作者选项卡
		 **/
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.menu);
				items.clear();
				AutoCompleteComponet acc1=new AutoCompleteComponet();
				items=acc1.draw("author", "authorfrequency",filepath);
				repaint();
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		tabbedPane.addTab("\u4F5C\u8005\u9891\u6B21\u6392\u540D\uFF1ATop N", null, panel, null);
		tabbedPane.setBackgroundAt(0, SystemColor.menu);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_panel.rowHeights = new int[]{50, 30, 50, 600, 175, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		JButton button = new JButton("\u67E5\u8BE2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.addMouseListener(new MouseAdapter() {    //点击读取文本框中用户输入的N，从数据库中读取频次前N的作者，以表格形式展现
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String input=textField.getText();
				int n=Integer.parseInt(input);
				Query(n);	
				scrollPane.setVisible(true);
			}
		});
		textField = new JTextField();
		textField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {     //在文本框中回车实现从作者频次表中读取topN个关键词
				if(arg0.getSource()==textField) {
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
						String input=textField.getText();
						int n=Integer.parseInt(input);
						Query(n);
						scrollPane.setVisible(true);
					}
				}
			}
		});
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		button.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.WEST;
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 4;
		gbc_button.gridy = 1;
		panel.add(button, gbc_button);		
		
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 2;
		panel.add(separator, gbc_separator);
		scrollPane.setBorder(null);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		panel.add(scrollPane, gbc_scrollPane);
		table1 = new JTable();
		table1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		table1.setShowHorizontalLines(false);
		table1.setShowGrid(false);
		table1.setBorder(null);
		table1.setRowHeight(35);
		table1.setModel(model1);
		table1.getTableHeader().setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
		table1.setDefaultRenderer(Object.class, r);		
		table1.getColumnModel().getColumn(0).setMaxWidth(0); 
		table1.getColumnModel().getColumn(0).setMinWidth(0); 
		table1.getColumnModel().getColumn(0).setPreferredWidth(0);
		table1.getColumnModel().getColumn(0).setWidth(0);
		table1.getColumnModel().getColumn(1).setWidth(320); 
		table1.getColumnModel().getColumn(2).setWidth(640); 
		table1.getColumnModel().getColumn(3).setWidth(320); 
		scrollPane.setViewportView(table1);
		scrollPane.setVisible(false);
	
		tabbedPane.addTab("\u4F5C\u8005\u6307\u5B9A\u67E5\u8BE2", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_panel_1.rowHeights = new int[]{50, 30, 15, 200, 25, 40, 575, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		

		/*
		 * 查询按钮
		 */		
		JButton btnNewButton = new JButton("\u67E5\u8BE2");
		btnNewButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {   //点击读取文本框中用户输入的作者，从数据库中匹配
			    input1=textField_1.getText();
			    freq_before1=Query_1(input1);
			    if(freq_before1>0) {
			    textField_4.setVisible(true);
			}
			}
		});
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				    //回车				
					
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
						//textField_1.requestFocusInWindow();
						input1=textField_1.getText();
					    freq_before1=Query_1(input1);
					    
					    if( freq_before1>0) {
					        textField_4.setVisible(true);
					        	}
					          }
					     }
			
		});
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 1;
		panel_1.add(textField_1, gbc_textField_1);
		items.clear();
		AutoCompleteComponet acc2=new AutoCompleteComponet();
		items=acc2.draw("author", "authorfrequency",filepath);
		acc2.setupAutoComplete(textField_1, items);	
		textField_1.setColumns(10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 1;
		panel_1.add(btnNewButton, gbc_btnNewButton);
		
		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 1;
		gbc_separator_2.gridy = 2;
		panel_1.add(separator_2, gbc_separator_2);
		textField_4 = new JTextArea();
		textField_4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 26));
		textField_4.setBackground(Color.WHITE);
		textField_4.setEditable(false);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.gridwidth = 5;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 3;
		panel_1.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		textField_4.setVisible(false);
		

	}

	
	//查询topN个关键词
	public void Query(int n) {
		try {
			
			model1.setRowCount(0);
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
			//实现同频次排名
			String sql1="select Frequency from authorfrequency group by Frequency order by Frequency desc";
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
			String sql2="select author,Frequency from authorfrequency  where frequency>='"+result+"' order by Frequency desc" ;
			PreparedStatement stmt2=conn.prepareStatement(sql2);
			ResultSet rs2=stmt2.executeQuery();
			int id=1;
			int row=0;
			while(rs2.next())
			{
				String ins=rs2.getString(1);
				int freq=rs2.getInt(2);
				if(row>0) {
					if(freq<Integer.parseInt(table1.getValueAt(row-1, 3).toString())) {     //同频次排名
						id++;
					}
				}
				TableColumn tc = table1.getColumnModel().getColumn(0);
				tc.setCellEditor(table1.getDefaultEditor(Boolean.class));
				tc.setCellRenderer(table1.getDefaultRenderer(Boolean.class));
				model1.addRow(new Object[] {new Boolean(false),id,ins,freq});  //向表格中添加一行
				row++;
			}
			conn.close();
			
		} 
	catch (ClassNotFoundException |SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	//查询单个关键词
	public int Query_1(String s) {
		try {
			
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
			//实现同频次排名
			String sql_1="select Frequency from authorfrequency where author='"+s+"'";
			PreparedStatement stmt_1=conn.prepareStatement(sql_1);
			ResultSet rs_1=stmt_1.executeQuery();
			if(rs_1.next()) {
				int freq=rs_1.getInt(1);
				textField_4.setText(s+"的出现频次为："+freq);
				return freq;
			}
			else {
				JOptionPane.showMessageDialog(null, "输入的作者不存在！", "", JOptionPane.ERROR_MESSAGE); 
			}
			conn.close();
		} 
	catch (ClassNotFoundException |SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;			
	}
	
		
}
