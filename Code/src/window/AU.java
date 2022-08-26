package window;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AU implements AnalyseAble{
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
		
		//作者频次统计
		public void countfrequency() {

			try {
				
				 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
					
				//如果存在作者频次表，先删除
				if(validateTableNameExist("authorfrequency",filepath))
				{  
				    
					String sql_clean="drop table authorfrequency";
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
				
			
				//从原始表中读取“作者”字段的值
				String sql1="select Author from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();
				
				//准备工作：创建辅助表
				String prepare1="create table assistant(id int identity, author varchar(150),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//在辅助表中插入数据		
				String sql2="insert into assistant(id,author,counts) values(?,?,?)";
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int assistant_id=0;
				while(rs1.next()){           
					String author=rs1.getString(1); 
					String []AA={};
					if(author!=null) {
						AA=author.split("[;]+");   //分割含有分号的字符串，将Author字段分行存入辅助表中
					}
					for(int i=0;i<AA.length;i++) {
						stmt2.setInt(1,assistant_id++);   
						stmt2.setString(2, AA[i]);
						stmt2.setInt(3, 1);
						stmt2.addBatch();
					}				
				}
				stmt2.executeBatch();
			
				
				//从辅助表中读取作者信息，加总记录条数计算作者频次
				String sql3="select author,sum(counts) as frequency from assistant group by author order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//准备工作：创建作者频次表
				String prepare2="create table authorfrequency(ID int identity,Author varchar(150),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//将信息写入作者频次表
				String sql4="insert into authorfrequency(ID,Author,Frequency) values(?,?,?)";
				PreparedStatement stmt4=conn.prepareStatement(sql4);
				int author_id=1;
				while(rs3.next()) {
					String au=rs3.getString(1);
					int freq=rs3.getInt(2);	
					stmt4.setInt(1,author_id++);
					stmt4.setString(2, au);
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
		
		//作者共现分析
		public void countcooccurrence() {

			try {			
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);

				//判断作者合著表、辅助表是否存在，存在则删除
				if(validateTableNameExist("AuthorCooccurrence",filepath)) {					
					String tabledrop1="drop table AuthorCooccurrence";
					PreparedStatement stmttd1=conn.prepareStatement(tabledrop1);
					stmttd1.execute();
				}
				if(validateTableNameExist("assistant",filepath)) {					
					String tabledrop2="drop table assistant";
					PreparedStatement stmttd2=conn.prepareStatement(tabledrop2);
					stmttd2.execute();
				}
				
				//准备工作：创建作者合著表、辅助表
				String tablecreate1="create table AuthorCooccurrence(ID int identity,AuthorName varchar(50) ,Frequency int)";
				PreparedStatement stmttc1=conn.prepareStatement(tablecreate1);
				stmttc1.execute();
				String tablecreate2="create table assistant(id int identity,author varchar(50),frequency int)";
				PreparedStatement stmttc2=conn.prepareStatement(tablecreate2);
				stmttc2.execute();
				conn.setAutoCommit(false);	
				
				//从源数据中读取作者信息
				String sql1="select Author from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();							
			    		
				//将合著作者两两放入辅助表中		
				String sql2="insert into assistant(id,author,frequency) values(?,?,?)";			
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int count=1;
				int id=1;
				while(rs1.next()){           
					String author=rs1.getString(1);
					String []authors={};
					if(author!=null) {
						authors=author.split("[;]+");
					}
					String author_co;
					for(int i=0;i<authors.length;i++) {										
						for(int j=i+1;j<authors.length;j++) {
							if(authors[i].compareTo(authors[j])<0) {
								author_co=authors[i]+";"+authors[j];
							}
							else {
								author_co=authors[j]+";"+authors[i];
							}	
							stmt2.setInt(1, id++);
							stmt2.setString(2,author_co);
							stmt2.setInt(3, count);
							stmt2.addBatch();
						}
					}				
				}
				stmt2.executeBatch();
				stmt2.clearBatch();
				conn.commit();					
				//从辅助表中读取作者信息，加总记录条数,计算作者合著频次
				String sql3="select author,sum(frequency) from assistant group by author order by sum(frequency) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//将作者、合著频次写入机构共现表中
				String sql4="insert into AuthorCooccurrence(ID,AuthorName,Frequency) values(?,?,?)";
				PreparedStatement stmt4=conn.prepareStatement(sql4);
				int ID=1;
				while(rs3.next()) {
					String authorname;
					authorname=rs3.getString(1);
					int fq;
					fq=rs3.getInt(2);
					stmt4.setInt(1, ID++);
					stmt4.setString(2, authorname);
					stmt4.setInt(3, fq);
					stmt4.addBatch();
				}
				stmt4.executeBatch();
				stmt4.clearBatch();
				conn.commit();
				
				
				//删除辅助表
				String tabledrop3="drop table assistant";
				PreparedStatement stmttd3=conn.prepareStatement(tabledrop3);
				stmttd3.execute();			
				conn.commit();			
				conn.setAutoCommit(true);
				conn.close();
			}
			
			catch(ClassNotFoundException | SQLException e) {
				System.out.println(e);
			}
			
		}
}