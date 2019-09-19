package home.resource.com.chillyfacts.com;

import home.resource.barcode.Barcode_Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Generate_Dynamic_Bar_Code {
	public static void main(String[] args) {
		 	PreparedStatement ps=null;
			Connection connection=null;
			DBConnection obj_DBConnection=new DBConnection();
			connection=obj_DBConnection.getConnection();
			ResultSet rs=null;
		try { 
			String query="select * from user";
			ps = connection.prepareStatement(query);
			rs=ps.executeQuery();
			while(rs.next()){
				Barcode_Image.createImage(rs.getString("mobile")+".png",rs.getString("mobile"));				
				System.out.println("Creating Barcode for "+rs.getString("mobile"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(connection!=null){
				try {
						connection.close();
					}
				 catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			if(ps!=null){
				try {
					ps.close();
					}
				 catch (Exception e2) {
					 e2.printStackTrace();
				}
			}
			
		}
		 
		}	
  
}
