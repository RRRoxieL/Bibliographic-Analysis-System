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
	//�ж����ݿ����Ƿ���ڱ�
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
	
	//�ؼ���Ƶ��ͳ��
	public void countfrequency() {
		
		try {
			 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
				
			//������ڹؼ���Ƶ�α���ɾ��
			if(validateTableNameExist("keywordfrequency",filepath))
			{  
				String sql_clean="drop table keywordfrequency";
			    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
			    stmt_end.executeUpdate();
			}
			//������ڸ�������ɾ��
			if(validateTableNameExist("assistant",filepath))
			{  
			    
				String sql_clean="drop table assistant";
			    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
			    stmt_end.executeUpdate();
			}
			
			conn.setAutoCommit(false);
			Statement stmt=conn.createStatement();
			
		
			//��ԭʼ���ж�ȡKeywords�ֶε�ֵ
			String sql1="select [Keywords] from metadata";
			PreparedStatement stmt1=conn.prepareStatement(sql1);
			ResultSet rs1=stmt1.executeQuery();
			
			//׼������������������
			String prepare1="create table assistant(id int identity,keyword varchar(150),counts int not null)"; 
			stmt.executeUpdate(prepare1);
			//�ڸ������в�������		
			String sql2="insert into assistant(id,keyword,counts) values(?,?,?)";
			PreparedStatement stmt2=conn.prepareStatement(sql2);
			int assistant_id=0;
			while(rs1.next()){           
				String keywords=rs1.getString(1); 
				String []AA={};
				if(keywords!=null) {
					AA=keywords.split("[;]+");   //�ָ�зֺŵ��ַ�������Keywords�ֶη��д��븨������
				}
				for(int i=0;i<AA.length;i++) {
					stmt2.setInt(1,assistant_id++);   
					stmt2.setString(2, AA[i]);
					stmt2.setInt(3, 1);
					stmt2.addBatch();
				}				
			}
			stmt2.executeBatch();
			
			//�Ӹ������ж�ȡ�ؼ�����Ϣ�����ܼ�¼��������ؼ���Ƶ��
			String sql3="select keyword,sum(counts) as frequency from assistant group by keyword order by sum(counts) desc";
			PreparedStatement stmt3=conn.prepareStatement(sql3);
			ResultSet rs3=stmt3.executeQuery();
			//׼�������������ؼ���Ƶ�α�
			String prepare2="create table keywordfrequency(ID int identity,Keyword varchar(150),Frequency int not null)"; 
			stmt.executeUpdate(prepare2);
			//����Ϣд��ؼ���Ƶ�α�
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
			
			
			
			
			//������������������ɾ������������
			String sql_end2="drop table assistant";
			PreparedStatement stmt_end2=conn.prepareStatement(sql_end2);
			stmt_end2.executeUpdate();

			conn.commit(); //������
			
			stmt2.clearBatch();
			stmt4.clearBatch();
			
			conn.close();
			
			conn.setAutoCommit(true);
			
		}
		
		catch(ClassNotFoundException | SQLException e) {
			System.out.println(e);
		}
	}
	
	//�ؼ��ʹ��ַ���
	public void countcooccurrence() {	

		try {
			 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
			
			 //������ڻ������ֱ���ɾ��
			if(validateTableNameExist("keywordcooccurrence",filepath))
			{  
				String sql_clean="drop table keywordcooccurrence";
			    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
			    stmt_end.executeUpdate();
			}
			//������ڸ�������ɾ��
			if(validateTableNameExist("assistant",filepath))
			{  
			    
				String sql_clean="drop table assistant";
			    PreparedStatement stmt_end=conn.prepareStatement(sql_clean);
			    stmt_end.executeUpdate();
			}
			
			conn.setAutoCommit(false);
			Statement stmt=conn.createStatement();
			
			//��ԭʼ���ж�ȡKeywords�ֶε�ֵ
			String sql1="select [Keywords] from metadata";
			PreparedStatement stmt1=conn.prepareStatement(sql1);
			ResultSet rs1=stmt1.executeQuery();							
			//׼������������������
			String prepare1="create table assistant(id int identity, keyword varchar(150),counts int not null)"; 
			stmt.executeUpdate(prepare1);
			//��ͬһ�����еĹؼ����������븨������		
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
			
			//�Ӹ������ж�ȡ�ؼ�����Ϣ�����ܼ�¼����,����ؼ��ʹ���Ƶ��
			String sql3="select keyword,sum(counts) from assistant group by keyword order by sum(counts) desc";
			PreparedStatement stmt3=conn.prepareStatement(sql3);
			ResultSet rs3=stmt3.executeQuery();
			//׼�������������ؼ��ʹ��ֱ�
			String prepare2="create table keywordcooccurrence(ID int identity,Keyword varchar(150),Frequency int not null)"; 
			stmt.executeUpdate(prepare2);
			//������������Ƶ��д��ؼ��ʹ��ֱ���
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
			
			
			//������������������ɾ������������
			String sql_end2="drop table assistant";
			PreparedStatement stmt_end2=conn.prepareStatement(sql_end2);
			stmt_end2.executeUpdate();
			
			conn.commit(); //������
			
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
