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
	    //�ж����ݿ����Ƿ���ڱ�
		public static boolean validateTableNameExist(String tableName,String filepath) throws ClassNotFoundException, SQLException {         
			
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection con=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
			
			ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);         
			if (rs.next()) {return true;}
			else {return false;}
			
			}
		
		//����Ƶ��ͳ��
		public void countfrequency() {

			try {
				
				 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
					
				//�����������Ƶ�α���ɾ��
				if(validateTableNameExist("authorfrequency",filepath))
				{  
				    
					String sql_clean="drop table authorfrequency";
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
				
			
				//��ԭʼ���ж�ȡ�����ߡ��ֶε�ֵ
				String sql1="select Author from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();
				
				//׼������������������
				String prepare1="create table assistant(id int identity, author varchar(150),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//�ڸ������в�������		
				String sql2="insert into assistant(id,author,counts) values(?,?,?)";
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int assistant_id=0;
				while(rs1.next()){           
					String author=rs1.getString(1); 
					String []AA={};
					if(author!=null) {
						AA=author.split("[;]+");   //�ָ�зֺŵ��ַ�������Author�ֶη��д��븨������
					}
					for(int i=0;i<AA.length;i++) {
						stmt2.setInt(1,assistant_id++);   
						stmt2.setString(2, AA[i]);
						stmt2.setInt(3, 1);
						stmt2.addBatch();
					}				
				}
				stmt2.executeBatch();
			
				
				//�Ӹ������ж�ȡ������Ϣ�����ܼ�¼������������Ƶ��
				String sql3="select author,sum(counts) as frequency from assistant group by author order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//׼����������������Ƶ�α�
				String prepare2="create table authorfrequency(ID int identity,Author varchar(150),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//����Ϣд������Ƶ�α�
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
		
		//���߹��ַ���
		public void countcooccurrence() {

			try {			
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);

				//�ж����ߺ������������Ƿ���ڣ�������ɾ��
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
				
				//׼���������������ߺ�����������
				String tablecreate1="create table AuthorCooccurrence(ID int identity,AuthorName varchar(50) ,Frequency int)";
				PreparedStatement stmttc1=conn.prepareStatement(tablecreate1);
				stmttc1.execute();
				String tablecreate2="create table assistant(id int identity,author varchar(50),frequency int)";
				PreparedStatement stmttc2=conn.prepareStatement(tablecreate2);
				stmttc2.execute();
				conn.setAutoCommit(false);	
				
				//��Դ�����ж�ȡ������Ϣ
				String sql1="select Author from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();							
			    		
				//�����������������븨������		
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
				//�Ӹ������ж�ȡ������Ϣ�����ܼ�¼����,�������ߺ���Ƶ��
				String sql3="select author,sum(frequency) from assistant group by author order by sum(frequency) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//�����ߡ�����Ƶ��д��������ֱ���
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
				
				
				//ɾ��������
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