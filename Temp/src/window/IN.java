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
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);;
					
				//������ڻ���Ƶ�α���ɾ��
				if(validateTableNameExist("institutionfrequency",filepath))
				{  
				    
					String sql_clean="drop table institutionfrequency";
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
				
			
				//��ԭʼ���ж�ȡ�����ߵ�ַ���ֶε�ֵ
				String sql1="select [Author Address] from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();
				
				//׼������������������
				String prepare1="create table assistant(id int identity, institution varchar(150),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//�ڸ������в�������		
				String sql2="insert into assistant(id,institution,counts) values(?,?,?)";
				PreparedStatement stmt2=conn.prepareStatement(sql2);
				int assistant_id=0;
				while(rs1.next()){           
					String institution=rs1.getString(1); 
					String []AA={};
					if(institution!=null) {
						AA=institution.split("[;]+");   //�ָ�зֺŵ��ַ�������Author Adress�ֶη��д��븨������
					}
					for(int i=0;i<AA.length;i++) {
						stmt2.setInt(1,assistant_id++);   
						stmt2.setString(2, AA[i]);
						stmt2.setInt(3, 1);
						stmt2.addBatch();
					}				
				}
				stmt2.executeBatch();
			
				
				//�Ӹ������ж�ȡ������Ϣ�����ܼ�¼�����������Ƶ��
				String sql3="select institution,sum(counts) as frequency from assistant group by institution order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//׼����������������Ƶ�α�
				String prepare2="create table institutionfrequency(ID int identity,Institution varchar(150),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//����Ϣд�����Ƶ�α�
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
		
		//�������ַ���
		public void countcooccurrence() {

			try {
				
				 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				 Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+filepath);
				
				 //������ڻ������ֱ���ɾ��
				if(validateTableNameExist("institutioncooccurrence",filepath))
				{  
					String sql_clean="drop table institutioncooccurrence";
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
				
				//��ԭʼ���ж�ȡ�����ߵ�ַ���ֶε�ֵ
				String sql1="select [Author Address] from metadata";
				PreparedStatement stmt1=conn.prepareStatement(sql1);
				ResultSet rs1=stmt1.executeQuery();							
				//׼������������������
				String prepare1="create table assistant(id int identity, institution varchar(200),counts int not null)"; 
				stmt.executeUpdate(prepare1);
				//�����������������븨������		
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
				
				//�Ӹ������ж�ȡ������Ϣ�����ܼ�¼����,�����������Ƶ��
				String sql3="select institution,sum(counts) from assistant group by institution order by sum(counts) desc";
				PreparedStatement stmt3=conn.prepareStatement(sql3);
				ResultSet rs3=stmt3.executeQuery();
				//׼�������������������ֱ�
				String prepare2="create table institutioncooccurrence(ID int identity,Institution varchar(200),Frequency int not null)"; 
				stmt.executeUpdate(prepare2);
				//������������Ƶ��д��������ֱ���
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
