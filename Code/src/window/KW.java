package window;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KW implements AnalyseAble{
	String filepath=new String();
	public void setfilepath(String path) {
		filepath=path;
	}
	//判断数据库中是否存在表
	public static boolean validateTableNameExist(String tableName,String filepath) throws ClassNotFoundException, SQLException {         
		
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
	
	//关键词频次统计
	public void countfrequency() {
		
		try {
			 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
				
			//如果存在关键词频次表，先删除
			if(validateTableNameExist("keywordfrequency",filepath))
			{  
				String sql_clean="drop table keywordfrequency";
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
			
		
			//从原始表中读取Keywords字段的值
			String sql1="select [Keywords] from metadata";
			PreparedStatement stmt1=conn.prepareStatement(sql1);
			ResultSet rs1=stmt1.executeQuery();
			
			//准备工作：创建辅助表
			String prepare1="create table assistant(id int identity,keyword varchar(150),counts int not null)"; 
			stmt.executeUpdate(prepare1);
			//在辅助表中插入数据		
			String sql2="insert into assistant(id,keyword,counts) values(?,?,?)";
			PreparedStatement stmt2=conn.prepareStatement(sql2);
			int assistant_id=0;
			while(rs1.next()){           
				String keywords=rs1.getString(1); 
				String []AA={};
				if(keywords!=null) {
					AA=keywords.split("[;]+");   //分割含有分号的字符串，将Keywords字段分行存入辅助表中
				}
				for(int i=0;i<AA.length;i++) {
					stmt2.setInt(1,assistant_id++);   
					stmt2.setString(2, AA[i]);
					stmt2.setInt(3, 1);
					stmt2.addBatch();
				}				
			}
			stmt2.executeBatch();
			
			//从辅助表中读取关键词信息，加总记录条数计算关键词频次
			String sql3="select keyword,sum(counts) as frequency from assistant group by keyword order by sum(counts) desc";
			PreparedStatement stmt3=conn.prepareStatement(sql3);
			ResultSet rs3=stmt3.executeQuery();
			//准备工作：创建关键词频次表
			String prepare2="create table keywordfrequency(ID int identity,Keyword varchar(150),Frequency int not null)"; 
			stmt.executeUpdate(prepare2);
			//将信息写入关键词频次表
			String sql4="insert into keywordfrequency(ID,Keyword,Frequency) values(?,?,?)";
			PreparedStatement stmt4=conn.prepareStatement(sql4);
			int institution_id=1;
			while(rs3.next()) {
				String kw=rs3.getString(1);
				//String at=rs3.getString(3);
				int freq=rs3.getInt(2);	
				stmt4.setInt(1,institution_id++);
				stmt4.setString(2, kw);
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
	
	//关键词共现分析
	public void countcooccurrence() {	

		try {
			 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
			
			 //如果存在机构共现表，先删除
			if(validateTableNameExist("keywordcooccurrence",filepath))
			{  
				String sql_clean="drop table keywordcooccurrence";
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
			
			//从原始表中读取Keywords字段的值
			String sql1="select [Keywords] from metadata";
			PreparedStatement stmt1=conn.prepareStatement(sql1);
			ResultSet rs1=stmt1.executeQuery();							
			//准备工作：创建辅助表
			String prepare1="create table assistant(id int identity, keyword varchar(150),counts int not null)"; 
			stmt.executeUpdate(prepare1);
			//将同一文章中的关键词两两放入辅助表中		
			String sql2="insert into assistant(id,keyword,counts) values(?,?,?)";			
			PreparedStatement stmt2=conn.prepareStatement(sql2);
			int assistant_id=0;
			while(rs1.next()){           
				String keyword=rs1.getString(1);
				String []AA={};
				if(keyword!=null) {
					AA=keyword.split("[;]+");
				}
				String keyword_co;
				for(int i=0;i<AA.length;i++) {									
					for(int j=i+1;j<AA.length;j++) {
						if(AA[i].compareTo(AA[j])<0) {
							keyword_co=AA[i]+";"+AA[j];
						}
						else {
							keyword_co=AA[j]+";"+AA[i];
						}	
						stmt2.setInt(1, assistant_id++);
						stmt2.setString(2,keyword_co);
						stmt2.setInt(3, 1);
						stmt2.addBatch();
					}
				}				
			}
			stmt2.executeBatch();
			
			//从辅助表中读取关键词信息，加总记录条数,计算关键词共现频次
			String sql3="select keyword,sum(counts) from assistant group by keyword order by sum(counts) desc";
			PreparedStatement stmt3=conn.prepareStatement(sql3);
			ResultSet rs3=stmt3.executeQuery();
			//准备工作：创建关键词共现表
			String prepare2="create table keywordcooccurrence(ID int identity,Keyword varchar(150),Frequency int not null)"; 
			stmt.executeUpdate(prepare2);
			//将机构、共现频次写入关键词共现表中
			String sql4="insert into keywordcooccurrence(ID,Keyword,Frequency) values(?,?,?)";
			PreparedStatement stmt4=conn.prepareStatement(sql4);
			int keyword_id=1;
			while(rs3.next()) {
				String keyword;
				keyword=rs3.getString(1);
				int freq=rs3.getInt(2);
				stmt4.setInt(1, keyword_id++);
				stmt4.setString(2, keyword);
				stmt4.setInt(3, freq);
				stmt4.addBatch();
			}
			stmt4.executeBatch();
			conn.setAutoCommit(true);
			conn.setAutoCommit(false);
			
			
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
