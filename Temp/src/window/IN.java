package window;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IN {
	
	    String filepath=new String();
    	public void setfilepath(String path) {
		    filepath=path;
	    }
	    //判断数据库中是否存在表
		public static boolean validateTableNameExist(String tableName,String filepath) throws ClassNotFoundException, SQLException {         
			
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection con=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
			
			ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);         
			if (rs.next()) {return true;}
			else {return false;}
			
			}
		
		//机构频次统计
		public void countfrequency() {

			try {
				
				 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);;
					
				//如果存在机构频次表，先删除
				if(validateTableNameExist("institutionfrequency",filepath))
				{  
				    
					String sql_clean="drop table institutionfrequency";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				//如果存在辅助表，先删除
				if(validateTableNameExist("assistant",filepath))
				{  
				    
					String sql_clean="drop table assistant";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				
				conn.setAutoCommit(false);
				Statement stmt=conn.createStatement();
				
			
				//从原始表中读取“作者地址”字段的值
				String sql1="select [Author Address] from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();
				
				//准备工作：创建辅助表
				String prepare1="create table assistant(id int identity, institution varchar(150),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//在辅助表中插入数据		
				String sql2="insert into assistant(id,institution,counts) values(?,?,?)";
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int assistant_id=0;
				while(rs1.next()){           
					String institution=rs1.getString(1); 
					String []AA={};
					if(institution!=null) {
						AA=institution.split("[;]+");   //分割含有分号的字符串，将Author Adress字段分行存入辅助表中
					}
					for(int i=0;i<AA.length;i++) {
						stmt2.setInt(1,assistant_id++);   
						stmt2.setString(2, AA[i]);
						stmt2.setInt(3, 1);
						stmt2.addBatch();
					}				
				}
				stmt2.executeBatch();
			
				
				//从辅助表中读取机构信息，加总记录条数计算机构频次
				String sql3="select institution,sum(counts) as frequency from assistant group by institution order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//准备工作：创建机构频次表
				String prepare2="create table institutionfrequency(ID int identity,Institution varchar(150),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//将信息写入机构频次表
				String sql4="insert into institutionfrequency(ID,Institution,Frequency) values(?,?,?)";
				PreparedStatement stmt4=conn.prepareStatement(sql4);
				int institution_id=1;
				while(rs3.next()) {
					String ins=rs3.getString(1);
					int freq=rs3.getInt(2);	
					stmt4.setInt(1,institution_id++);
					stmt4.setString(2, ins);
					stmt4.setInt(3, freq);
					stmt4.addBatch();
				}
				stmt4.executeBatch();		
				
				
				
				//结束工作，将辅助表删除，减少冗余
				String sql_end2="drop table assistant";
				PreparedStatement stmt_end2=conn.prepareStatement(sql_end2);
				stmt_end2.executeUpdate();
				
				conn.commit(); //事务处理
				
				stmt2.clearBatch();
				stmt4.clearBatch();
				
				
				conn.close();			
				conn.setAutoCommit(true);
				
			}
			
			catch(ClassNotFoundException | SQLException e) {
				System.out.println(e);
			}
		}
		
		//机构共现分析
		public void countcooccurrence() {

			try {
				
				 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
				
				 //如果存在机构共现表，先删除
				if(validateTableNameExist("institutioncooccurrence",filepath))
				{  
					String sql_clean="drop table institutioncooccurrence";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				//如果存在辅助表，先删除
				if(validateTableNameExist("assistant",filepath))
				{  
				    
					String sql_clean="drop table assistant";
				    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
				    stmt_end.executeUpdate();
				}
				
				conn.setAutoCommit(false);
				Statement stmt=conn.createStatement();
				
				//从原始表中读取“作者地址”字段的值
				String sql1="select [Author Address] from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();							
				//准备工作：创建辅助表
				String prepare1="create table assistant(id int identity, institution varchar(200),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//将合作机构两两放入辅助表中		
				String sql2="insert into assistant(id,institution,counts) values(?,?,?)";			
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int assistant_id=0;
				while(rs1.next()){           
					String institution=rs1.getString(1);
					String []AA={};
					if(institution!=null) {
						AA=institution.split("[;]+");
					}
					String institution_co;
					for(int i=0;i<AA.length;i++) {									
						for(int j=i+1;j<AA.length;j++) {
							if(AA[i].compareTo(AA[j])<0) {
								institution_co=AA[i]+";"+AA[j];
							}
							else {
								institution_co=AA[j]+";"+AA[i];
							}	
							stmt2.setInt(1, assistant_id++);
							stmt2.setString(2,institution_co);
							stmt2.setInt(3, 1);
							stmt2.addBatch();
						}
					}				
				}
				stmt2.executeBatch();
				
				//从辅助表中读取机构信息，加总记录条数,计算机构共现频次
				String sql3="select institution,sum(counts) from assistant group by institution order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//准备工作：创建机构共现表
				String prepare2="create table institutioncooccurrence(ID int identity,Institution varchar(200),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//将机构、共现频次写入机构共现表中
				String sql4="insert into institutioncooccurrence(ID,Institution,Frequency) values(?,?,?)";
				PreparedStatement stmt4=conn.prepareStatement(sql4);
				int institution_id=1;
				while(rs3.next()) {
					String institution;
					institution=rs3.getString(1);
					int freq=rs3.getInt(2);
					stmt4.setInt(1, institution_id++);
					stmt4.setString(2, institution);
					stmt4.setInt(3, freq);
					stmt4.addBatch();
				}
				stmt4.executeBatch();
				
				
				
				//结束工作，将辅助表删除，减少冗余
				String sql_end2="drop table assistant";
				PreparedStatement stmt_end2=conn.prepareStatement(sql_end2);
				stmt_end2.executeUpdate();
				
				conn.commit(); //事务处理
				
				stmt2.clearBatch();
				stmt4.clearBatch();
				
				conn.close();
				
				conn.setAutoCommit(true);
			}
			
			catch(ClassNotFoundException | SQLException e) {
				System.out.println(e);
			}			
		}
}
