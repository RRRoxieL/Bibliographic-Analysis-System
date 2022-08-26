package window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.*;

	public class AutoCompleteComponet {
		private static String name;
		private static String table;
		private static String filepath;
		private static ArrayList items=new ArrayList();
		public void setinfo(String s1,String s2,String s3) {
			name=s1;
			table=s2;
			filepath=s3;
		}
		//输入框自动补全
	   
	    private static boolean isAdjusting(JComboBox cbInput) {
	        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
	            return (Boolean) cbInput.getClientProperty("is_adjusting");
	        }
	        return false;
	    }

	    private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
	        cbInput.putClientProperty("is_adjusting", adjusting);
	    }

	    public void setupAutoComplete(JTextField txtInput, ArrayList<String> items) {
	        final DefaultComboBoxModel model = new DefaultComboBoxModel();
	        final JComboBox cbInput = new JComboBox(model) {
	            public Dimension getPreferredSize() {
	                return new Dimension(super.getPreferredSize().width, 0);
	            }
	        };
	        cbInput.setBackground(SystemColor.inactiveCaptionBorder);
	        cbInput.setOpaque(false);
	        
	        setAdjusting(cbInput, false);
	        
	        for (String item : items) {
	            model.addElement(item);
	        }
	        cbInput.setSelectedItem(null);
	        cbInput.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (!isAdjusting(cbInput)) {
	                    if (cbInput.getSelectedItem() != null) {
	                    	
	                        txtInput.setText(cbInput.getSelectedItem().toString());
	                    }
	                }
	            }
	        });

	        txtInput.addKeyListener(new KeyAdapter() {

	            @Override
	            public void keyPressed(KeyEvent e) {
	            	txtInput.requestFocusInWindow();
	                setAdjusting(cbInput, true);
	                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	                  /*  if (cbInput.isPopupVisible()) {
	                        e.setKeyCode(KeyEvent.VK_ENTER);
	                    }
	                }
	              /*  if (e.getKeyCode() == KeyEvent.VK_ENTER
	                        || e.getKeyCode() == KeyEvent.VK_UP
	                        || e.getKeyCode() == KeyEvent.VK_DOWN) {
	                    e.setSource(cbInput);
	                    cbInput.dispatchEvent(e);*/
	                  /*  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                        txtInput.setText(cbInput.getSelectedItem().toString());
	                        cbInput.setPopupVisible(false);
	                    }
	                }*/
	                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                    cbInput.setPopupVisible(false);
	                }
	                setAdjusting(cbInput, false);
	            }}
	        });
	        txtInput.getDocument().addDocumentListener(new DocumentListener() {
	            public void insertUpdate(DocumentEvent e) {
	                updateList();
	            }

	            public void removeUpdate(DocumentEvent e) {
	                updateList();
	            }

	            public void changedUpdate(DocumentEvent e) {
	                updateList();
	            }

	            private void updateList() {
	                setAdjusting(cbInput, true);
	                model.removeAllElements();
	                String input = txtInput.getText();
	                if (!input.isEmpty()) {
	                    for (String item : items) {
	                        if (item.toLowerCase().startsWith(input.toLowerCase())) {
	                            model.addElement(item);
	                        }
	                    }
	                }
	                cbInput.setPopupVisible(model.getSize() > 0);
	                setAdjusting(cbInput, false);
	            }
	        });
	        txtInput.setLayout(new BorderLayout());
	        txtInput.add(cbInput, BorderLayout.SOUTH);
	        Font font=new Font("Microsoft YaHei UI", Font.PLAIN, 22);
	        cbInput.setFont(font);
	    }

	    /*
	         * 获取数据库中的关键词
	     */
	   public Map getStationMap() {
	        Map map = new HashMap();
	        
	        try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				Connection con=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
				String sql="select "+name+" from "+table+"";
				PreparedStatement stmt=con.prepareStatement(sql);
				ResultSet rs=stmt.executeQuery();
				int id=1;
				while(rs.next()) {
					String keyword=rs.getString(1);
					map.put(keyword,id++);
				}
				con.close();
			}
	        catch (ClassNotFoundException |SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return map;
	    }
	   
	 //读取数据库内容至ArraryList
		public ArrayList draw(String field ,String table,String filepath) {
			
			setinfo(field, table,filepath);
			Map map = getStationMap();
		    Iterator iterator = map.keySet().iterator();
		    while (iterator.hasNext()) {
		        String stationName = iterator.next().toString();
		        items.add(stationName);
		    }
			return items;
		    }
	

}
