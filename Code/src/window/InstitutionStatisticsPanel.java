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
import java.awt.GridLayout;

public class InstitutionStatisticsPanel extends JPanel {
	private JTextField textField;
	private JTable table1;
	private JTable table2;
	private DefaultTableModel model1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextArea textField_4;
	private JTextField textField_5;
	private JButton btnNewButton_1;
	JButton button_1;
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
	public InstitutionStatisticsPanel() {		
		
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
		
		
		model1=new DefaultTableModel(new Object[][] {},new String[] {"","名次","机构", "频次"});  //展示查询结果（topN个机构）		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();  
		r.setHorizontalAlignment(JLabel.CENTER);   
        
		
		
		/**
		 * TopN机构选项卡
		 **/
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.menu);
				items.clear();
				AutoCompleteComponet acc1=new AutoCompleteComponet();
				items=acc1.draw("institution", "institutionfrequency",filepath);
				repaint();
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		tabbedPane.addTab("\u673A\u6784\u9891\u6B21\u6392\u540D\uFF1ATop N", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_panel.rowHeights = new int[]{50, 30, 15, 500, 25, 40, 275, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		JButton button = new JButton("\u67E5\u8BE2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.addMouseListener(new MouseAdapter() {    //点击读取文本框中用户输入的N，从数据库中读取频次前N的机构，以表格形式展现
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String input=textField.getText();
				int n=Integer.parseInt(input);
				Query(n);	
				scrollPane.setVisible(true);
				button_1.setVisible(true);
			}
		});
		textField = new JTextField();
		textField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {     //在文本框中回车实现从机构频次表中读取topN个关键词
				if(arg0.getSource()==textField) {
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
						String input=textField.getText();
						int n=Integer.parseInt(input);
						Query(n);
						scrollPane.setVisible(true);
						button_1.setVisible(true);
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
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 4;
		panel.add(separator_1, gbc_separator_1);
		
		//放合并、删除按钮的panel
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.gridwidth = 5;
		gbc_panel_5.insets = new Insets(0, 0, 5, 5);
		gbc_panel_5.fill = GridBagConstraints.VERTICAL;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 5;
		panel.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 130, 87, 80, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		//放修正按钮的panel		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 5;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 6;
		panel.add(panel_2, gbc_panel_2);
		button_1 = new JButton("\u4FEE\u6B63");  
		button_1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		button_1.setVisible(false);
		button_1.addMouseListener(new MouseAdapter() {    
			@Override
			public void mouseClicked(MouseEvent e) {
				//点击修正按钮跳出合并、删除按钮
				
				//增加一列复选框
				table1.getColumnModel().getColumn(0).setWidth(80); 
				table1.getColumnModel().getColumn(0).setPreferredWidth(80); 
				table1.getColumnModel().getColumn(1).setPreferredWidth(280); 
				table1.getColumnModel().getColumn(2).setPreferredWidth(640); 
				table1.getColumnModel().getColumn(3).setPreferredWidth(280); 
				
				String input=textField.getText();
				int n=Integer.parseInt(input);
				panel_2.removeAll();				
				JSplitPane splitPane = new JSplitPane();
				splitPane.setResizeWeight(0.5);
				GridBagConstraints gbc_splitPane = new GridBagConstraints();
				gbc_splitPane.insets = new Insets(0, 0, 5, 5);
				gbc_splitPane.fill = GridBagConstraints.BOTH;
				gbc_splitPane.gridx = 2;
				gbc_splitPane.gridy = 4;
				panel.add(splitPane, gbc_splitPane);
				
				/*
				 * 放置合并按钮的panel
				 */
				JPanel panel_3 = new JPanel();
				panel_3.setBorder(null);
				splitPane.setLeftComponent(panel_3);
				panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
							
				/*
				 * 合并按钮
				 */				
				JButton button_2 = new JButton("\u5408\u5E76");	
				button_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
				button_2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {         //点击合并按钮，获取已选中复选框所在行的值
						int count_before = model1.getRowCount();		        
						String[] kw_before=new String[count_before];           //要合并的机构
						int[] freq_before=new int[count_before];               //要合并的机构频次  
						int[]row=new int[count_before];                        //要合并的机构所在行
						for (int i = 0; i < count_before; i++) {               //获取被选中的机构及频次
						    if (model1.getValueAt(i, 0).equals(true)){      
						    	selected_before++;	
						    	kw_before[selected_before]=model1.getValueAt(i, 2).toString();
						    	freq_before[selected_before]=Integer.parseInt(model1.getValueAt(i, 3).toString());
						    	row[selected_before-1]=i;
					        }
					    }											
						if(selected_before>=1){    //选择的机构大于等于1，用户输入合并到的词
							
							JLabel label_2 = new JLabel("合并至：");
							label_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
							GridBagConstraints gbc_label_2 = new GridBagConstraints();
							gbc_label_2.anchor = GridBagConstraints.EAST;
							gbc_label_2.insets = new Insets(0, 0, 0, 5);
							gbc_label_2.gridx = 0;
							gbc_label_2.gridy = 0;
							panel_5.add(label_2, gbc_label_2);
							
							
							/*
							 * 用于输入机构的文本框，可根据数据库内容自动补全
							 */
							textField_2 = new JTextField();
							textField_2.setFont(new Font("微软雅黑", Font.PLAIN, 20));
					        textField_2.addKeyListener(new KeyAdapter() {
								@Override
								public void keyPressed(KeyEvent arg0) {   //回车
								
									String input=textField_2.getText();
									if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
									try {
										Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
										Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
										conn.setAutoCommit(false);
										String sql_seek="select frequency from institutionfrequency where institution='"+input+"'";
										PreparedStatement stmt_seek=conn.prepareStatement(sql_seek);
										ResultSet rs_seek=stmt_seek.executeQuery();
										int freq_after;
										int freq_new;										
										if(rs_seek.next()) {
											freq_after=rs_seek.getInt(1);
											freq_new=freq_after;
											Object[] options = { "OK ", "CANCEL " }; 
							    			int n=JOptionPane.showOptionDialog(null, "您将要把所选机构的频次加到"+input+"中", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
							    			if(n==JOptionPane.YES_OPTION) {
							    				String sql_delete="delete * from institutionfrequency where institution=?";
												String sql_combine="update institutionfrequency set Frequency=? where institution='"+input+"'";
												PreparedStatement stmt_delete=conn.prepareStatement(sql_delete);
												PreparedStatement stmt_combine=conn.prepareStatement(sql_combine);
												for(int i=0;i<=selected_before;i++) {
													stmt_delete.setString(1,kw_before[i]);
													stmt_delete.addBatch();
													freq_new+=freq_before[i];
												}
												stmt_combine.setInt(1, freq_new);
												stmt_delete.executeBatch();
												stmt_combine.executeUpdate();
												conn.commit();
												conn.setAutoCommit(true);
												conn.close();
												JOptionPane.showMessageDialog(null, "合并成功！", "", JOptionPane.INFORMATION_MESSAGE); 
												
												//合并后刷新表格
												String number=textField.getText();
												int m=Integer.parseInt(number);
												Query(m);		
												panel_5.removeAll();
							    			}						    			
										}
										else {
						    				JOptionPane.showMessageDialog(null, "输入的机构不存在！", "", JOptionPane.ERROR_MESSAGE); 
										}
									} 
									catch (ClassNotFoundException |SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									}
								}
								});
							GridBagConstraints gbc_textField_2 = new GridBagConstraints();
							gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
							gbc_textField_2.insets = new Insets(0, 0, 5, 5);
							gbc_textField_2.gridx = 1;
							gbc_textField_2.gridy = 0;
							panel_5.add(textField_2, gbc_textField_2);					
							items.clear();
							AutoCompleteComponet acc=new AutoCompleteComponet();
							items=acc.draw("institution", "institutionfrequency",filepath);
						    acc.setupAutoComplete(textField_2, items);
							textField_2.setColumns(100);
							
							/*
							 * 确定按钮，点击读取文本框内容，并实现合并功能
							 */
							JButton button_4 = new JButton("\u786E\u5B9A");			
							button_4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
							button_4.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent arg0) {
									String input=textField_2.getText();
									try {
										Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
										Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
										conn.setAutoCommit(false);
										String sql_seek="select frequency from institutionfrequency where institution='"+input+"'";
										PreparedStatement stmt_seek=conn.prepareStatement(sql_seek);
										ResultSet rs_seek=stmt_seek.executeQuery();
										int freq_after;
										int freq_new;										
										if(rs_seek.next()) {
											freq_after=rs_seek.getInt(1);
											freq_new=freq_after;
											Object[] options = { "OK ", "CANCEL " }; 
							    			int n=JOptionPane.showOptionDialog(null, "您将要把所选机构的频次加到"+input+"中", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
							    			if(n==JOptionPane.YES_OPTION) {
							    				String sql_delete="delete * from institutionfrequency where institution=?";
												String sql_combine="update institutionfrequency set Frequency=? where institution='"+input+"'";
												PreparedStatement stmt_delete=conn.prepareStatement(sql_delete);
												PreparedStatement stmt_combine=conn.prepareStatement(sql_combine);
												for(int i=0;i<=selected_before;i++) {
													stmt_delete.setString(1,kw_before[i]);
													stmt_delete.addBatch();
													freq_new+=freq_before[i];
												}
												stmt_combine.setInt(1, freq_new);
												stmt_delete.executeBatch();
												stmt_combine.executeUpdate();
												conn.commit();
												conn.setAutoCommit(true);
												conn.close();
												JOptionPane.showMessageDialog(null, "合并成功！", "", JOptionPane.INFORMATION_MESSAGE); 
												
												//合并后刷新表格
												String number=textField.getText();
												int m=Integer.parseInt(number);
												Query(m);		
												panel_5.removeAll();
							    			}						    			
										}
										else {
						    				JOptionPane.showMessageDialog(null, "输入的机构不存在！", "", JOptionPane.ERROR_MESSAGE); 
										}
									} 
									catch (ClassNotFoundException |SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							GridBagConstraints gbc_button_4 = new GridBagConstraints();
							gbc_button_4.insets = new Insets(0, 0, 5, 5);
							gbc_button_4.gridx = 2;
							gbc_button_4.gridy = 0;
							panel_5.add(button_4, gbc_button_4);
							
							/*
							 * 取消按钮，点击退出合并功能
							 */		
							JButton Button_5= new JButton("\u53D6\u6D88");
							Button_5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
							Button_5.addMouseListener(new MouseAdapter() {
					            @Override
					            public void mouseClicked(MouseEvent e) {
					    			panel_5.removeAll();
					    			String number=textField.getText();
									int m=Integer.parseInt(number);
									Query(m);
					    		}
					    	});
							GridBagConstraints gbc_Button_5 = new GridBagConstraints();
							gbc_Button_5.insets = new Insets(0, 0, 0, 5);
							gbc_Button_5.gridx = 3;
							gbc_Button_5.gridy = 0;
							panel_5.add(Button_5, gbc_Button_5);
							panel_5.revalidate();
						
						  }	
				    	}
				});
				panel_3.add(button_2);
				
				/*
				 * 放置删除按钮的panel
				 */
				JPanel panel_4 = new JPanel();
				panel_4.setBorder(null);
				splitPane.setRightComponent(panel_4);
				panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
				/*
				 * 删除按钮
				 */
				JButton button_3 = new JButton("\u5220\u9664");
				button_3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
				panel_4.add(button_3);
				button_3.addMouseListener(new MouseAdapter() {
		    		@Override
		    		public void mouseClicked(MouseEvent ag0) {
		    			int count = model1.getRowCount();
		    			String[] kw= new String[count];
		    			int selected=1;
						for (int i = 0; i < count; i++) {
						    if (model1.getValueAt(i, 0).equals(true)){
						    	kw[selected]=model1.getValueAt(i,2).toString();
						    	selected++;
						        }
						    }
						selected--;
						if(selected>0) {
						try {
							Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
							Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
							conn.setAutoCommit(false);
							String sql5="delete * from institutionfrequency where institution=?";
							PreparedStatement stmt5=conn.prepareStatement(sql5);
							for(int j=0;j<kw.length;j++) {
								stmt5.setString(1, kw[j]);
								stmt5.addBatch();
							}
							Object[] options = { "OK ", "CANCEL " }; 
			    			int n=JOptionPane.showOptionDialog(null, "您将要删除"+selected+"个机构", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
			    			if(n==JOptionPane.YES_OPTION) {
							    stmt5.executeBatch();
							    conn.commit();
							    JOptionPane.showMessageDialog(null, "删除成功！", "", JOptionPane.INFORMATION_MESSAGE);
			    			}
			    			stmt5.clearBatch();
			    			//删除后刷新表格
			    			String input=textField.getText();
							int m=Integer.parseInt(input);
							Query(m);
							conn.setAutoCommit(true);
						    conn.close();					     
						}
						catch (ClassNotFoundException |SQLException e) {
							e.printStackTrace();
						}
						}
						else {
							JOptionPane.showMessageDialog(null, "请选择机构！", "", JOptionPane.WARNING_MESSAGE);
						}
		    			}
		    		});
				
				panel_2.add(splitPane);
				panel_2.revalidate();
				
			}
			
		});
		panel_2.add(button_1);
	
		tabbedPane.addTab("\u673A\u6784\u6307\u5B9A\u67E5\u8BE2", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{175, 325, 500, 30, 100, 325, 175, 0};
		gbl_panel_1.rowHeights = new int[]{50, 30, 15, 200, 25, 40, 575, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		

		/*
		 * 查询按钮
		 */		
		JButton btnNewButton = new JButton("\u67E5\u8BE2");
		btnNewButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {   //点击读取文本框中用户输入的机构，从数据库中匹配
			    input1=textField_1.getText();
			    freq_before1=Query_1(input1);
			    if(freq_before1>0) {
			    textField_4.setVisible(true);
			    btnNewButton_1.setVisible(true);
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
					        btnNewButton_1.setVisible(true);
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
		items=acc2.draw("institution", "institutionfrequency",filepath);
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
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.gridwidth = 5;
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 5;
		panel_1.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 0, 5);
		gbc_panel_3.gridwidth = 5;
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 6;
		panel_1.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0};
		gbl_panel_3.rowHeights = new int[]{40, 0};
		gbl_panel_3.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
	    
	
		/*
		 * 修正按钮
		 */
		btnNewButton_1 = new JButton("\u4FEE\u6B63");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
		btnNewButton_1.setVisible(false);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				panel_3.removeAll();
				JSplitPane splitPane = new JSplitPane();
				splitPane.setResizeWeight(0.5);
				GridBagConstraints gbc_splitPane = new GridBagConstraints();
				gbc_splitPane.insets = new Insets(0, 0, 5, 0);
				gbc_splitPane.fill = GridBagConstraints.VERTICAL;
				gbc_splitPane.gridx = 0;
				gbc_splitPane.gridy = 0;
				panel_3.add(splitPane, gbc_splitPane);
				
				/*
				 * 合并按钮
				 */
				JButton button_2 = new JButton("\u5408\u5E76");
				button_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
				splitPane.setLeftComponent(button_2);
				button_2.addMouseListener(new MouseAdapter() {
		    		@Override
		    		public void mouseClicked(MouseEvent ag0) {     //点击合并按钮跳出输入框
		    			
		    			JLabel label_2 = new JLabel("合并至：");
		    			label_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		    			GridBagConstraints gbc_label_2 = new GridBagConstraints();
		    			gbc_label_2.insets = new Insets(0, 0, 5, 5);
		    			gbc_label_2.anchor = GridBagConstraints.EAST;
		    			gbc_label_2.gridx = 0;
		    			gbc_label_2.gridy = 0;
		    			panel_4.add(label_2, gbc_label_2);
		    			
		    			/*
		    			 * 输入框
		    			 */
		    			textField_5 = new JTextField();
		    			textField_5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		    			textField_5.setBorder(null);
		    		    textField_5.addKeyListener(new KeyAdapter() {
		    				@Override
		    				public void keyPressed(KeyEvent arg0) {     //回车
		    					if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
		    						String input=textField_5.getText();
					    			if(freq_before1>0) {
					    				try {
											Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
											Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
											conn.setAutoCommit(false);
											String sql_seek="select frequency from institutionfrequency where institution='"+input+"'";
											PreparedStatement stmt_seek=conn.prepareStatement(sql_seek);
											ResultSet rs_seek=stmt_seek.executeQuery();
											int freq_after;
											int freq_new;										
											if(rs_seek.next()) {
												freq_after=rs_seek.getInt(1);
												freq_new=freq_after+freq_before1;
												Object[] options = { "OK ", "CANCEL " }; 
								    			int n=JOptionPane.showOptionDialog(null, "您将要把该机构的频次加到"+input+"中", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
								    			if(n==JOptionPane.YES_OPTION) {
								    				String sql_delete="delete * from institutionfrequency where institution='"+input1+"'";
													String sql_combine="update institutionfrequency set Frequency='"+freq_new+"' where institution='"+input+"'";
													PreparedStatement stmt_delete=conn.prepareStatement(sql_delete);
													PreparedStatement stmt_combine=conn.prepareStatement(sql_combine);
													stmt_delete.executeUpdate();
													stmt_combine.executeUpdate();
													conn.commit();
													conn.setAutoCommit(true);
													conn.close();
													JOptionPane.showMessageDialog(null, "合并成功！", "", JOptionPane.INFORMATION_MESSAGE); 
							                        //在展示框中显示合并后的关键词
													textField_1.setText("");
													Query_1(input);		
													panel_4.removeAll();
													repaint();
								    			}						    			
											}
											else {
							    				JOptionPane.showMessageDialog(null, "输入的机构不存在！", "", JOptionPane.ERROR_MESSAGE); 
											}
										} 
										catch (ClassNotFoundException |SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
					    			}
		    					}
		    					}
		    					});
		    			GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		    			gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		    			gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		    			gbc_textField_5.gridwidth=3;
		    			gbc_textField_5.gridx = 1;
		    			gbc_textField_5.gridy = 0;
		    			panel_4.add(textField_5, gbc_textField_5);
		    			items.clear();
		    			AutoCompleteComponet acc2=new AutoCompleteComponet();
		    			items=acc2.draw("institution", "institutionfrequency",filepath);
    					acc2.setupAutoComplete(textField_5, items);      //可根据数据库内容自动补全
		    			textField_5.setColumns(20);
		    			
		    			/*
		    			 * 确定按钮
		    			 */
		    			JButton button_4 = new JButton("\u786E\u5B9A");
		    			button_4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		    			button_4.addMouseListener(new MouseAdapter() {
				    		@Override
				    		public void mouseClicked(MouseEvent ag0) {
				    			String input=textField_5.getText();
				    			if(freq_before1>0) {
				    				try {
										Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
										Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
										conn.setAutoCommit(false);
										String sql_seek="select frequency from institutionfrequency where institution='"+input+"'";
										PreparedStatement stmt_seek=conn.prepareStatement(sql_seek);
										ResultSet rs_seek=stmt_seek.executeQuery();
										int freq_after;
										int freq_new;										
										if(rs_seek.next()) {
											freq_after=rs_seek.getInt(1);
											freq_new=freq_after+freq_before1;
											Object[] options = { "OK ", "CANCEL " }; 
							    			int n=JOptionPane.showOptionDialog(null, "您将要把该机构的频次加到"+input+"中", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
							    			if(n==JOptionPane.YES_OPTION) {
							    				String sql_delete="delete * from institutionfrequency where institution='"+input1+"'";
												String sql_combine="update institutionfrequency set Frequency='"+freq_new+"' where institution='"+input+"'";
												PreparedStatement stmt_delete=conn.prepareStatement(sql_delete);
												PreparedStatement stmt_combine=conn.prepareStatement(sql_combine);
												stmt_delete.executeUpdate();
												stmt_combine.executeUpdate();
												conn.commit();
												conn.setAutoCommit(true);
												conn.close();
												JOptionPane.showMessageDialog(null, "合并成功！", "", JOptionPane.INFORMATION_MESSAGE); 
						                        //在展示框中显示合并后的关键词
												textField_1.setText("");
												Query_1(input);		
												panel_4.removeAll();
												repaint();
							    			}						    			
										}
										else {
						    				JOptionPane.showMessageDialog(null, "输入的机构不存在！", "", JOptionPane.ERROR_MESSAGE); 
										}
									} 
									catch (ClassNotFoundException |SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				    			}
				    		}
				    		});
		    			GridBagConstraints gbc_button_4 = new GridBagConstraints();
		    			gbc_button_4.insets = new Insets(0, 0, 5, 5);
		    			gbc_button_4.gridwidth = 1;
		    			gbc_button_4.gridx = 4;
		    			gbc_button_4.gridy = 0;
		    			panel_4.add(button_4, gbc_button_4);
		    			
		    			/*
		    			 * 取消按钮
		    			 */
		    			JButton button_5 = new JButton("\u53D6\u6D88");
		    			button_5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		    			button_5.addMouseListener(new MouseAdapter() {
				    		@Override
				    		public void mouseClicked(MouseEvent ag0) {
				    			panel_4.removeAll();
				    			panel_4.repaint();
				    			textField_4.setText("");
				    		}
				    		});	 
		    			GridBagConstraints gbc_button_5 = new GridBagConstraints();
		    			gbc_button_5.insets = new Insets(0, 0, 5, 0);
		    			gbc_button_5.gridx = 5;
		    			gbc_button_5.gridy = 0;
		    			panel_4.add(button_5, gbc_button_5);
		    			panel_4.revalidate();
		    		}
				});
				/*
				 * 删除按钮
				 */
				JButton button_3 = new JButton("\u5220\u9664");
				button_3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
				splitPane.setRightComponent(button_3);
				button_3.addMouseListener(new MouseAdapter() {
		    		@Override
		    		public void mouseClicked(MouseEvent ag0) {
		    			try {
							Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
							Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
							String sql="delete * from institutionfrequency where institution='"+input1+"'";
							PreparedStatement stmt=conn.prepareStatement(sql);
							Object[] options = { "OK ", "CANCEL " }; 
							int n=JOptionPane.showOptionDialog(null, "您将要删除'"+input1+"'", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
			    			if(n==JOptionPane.YES_OPTION) {
			    				stmt.executeUpdate();
			    				JOptionPane.showMessageDialog(null, "删除成功！", "", JOptionPane.INFORMATION_MESSAGE);
			    				textField_4.setText("");
			    				textField_5.setText("");
			    			}		   
			    			conn.close();
			    			}						
		    			catch (ClassNotFoundException |SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
		});
				
	    }
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panel_3.add(btnNewButton_1, gbc_btnNewButton_1);
		

	}

	
	//查询topN个关键词
	public void Query(int n) {
		try {
			
			model1.setRowCount(0);
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);		
			//实现同频次排名
			String sql1="select Frequency from institutionfrequency group by Frequency order by Frequency desc";
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
			String sql2="select institution,Frequency from institutionfrequency  where frequency>='"+result+"' order by Frequency desc" ;
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
			String sql_1="select Frequency from institutionfrequency where institution='"+s+"'";
			PreparedStatement stmt_1=conn.prepareStatement(sql_1);
			ResultSet rs_1=stmt_1.executeQuery();
			if(rs_1.next()) {
				int freq=rs_1.getInt(1);
				textField_4.setText(s+"的出现频次为："+freq);
				return freq;
			}
			else {
				JOptionPane.showMessageDialog(null, "输入的机构不存在！", "", JOptionPane.ERROR_MESSAGE); 
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
