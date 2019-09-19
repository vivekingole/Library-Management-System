package home.databasecontroller;

import home.util.Util;
import static home.util.Util.db;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class Database {

    private static Connection con;
    private static Statement stmt;     
    private static ResultSet res;
    private static PreparedStatement prepstmt;        
    private static boolean flag=false;
    private static Database db;
    public static boolean registration=true;
    private Database(){ 
        
     }
    public static Database getInstance(){
        if(flag==false){
            db=new Database();
            db.createConnection();
            flag=true;
        }
        return db;
    } 
    public void createConnection(){
        try{
             //String path=System.getenv("database");
            // DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            // con=DriverManager.getConnection("jdbc:ucanaccess:database\\db.accdb");
             Class.forName("org.sqlite.JDBC");
             con=DriverManager.getConnection("jdbc:sqlite:database\\db.sqlite");
            System.out.println("Connection established !!!");       
            try{
            System.out.println("Cheacking registration");
            ResultSet rs=executeQuery("select * from admin");
            rs.next();
            if(rs.getString("name").equals(""))
               registration=false; 
            System.out.println("reg="+registration);
            }catch(Exception e){System.out.println(e.getMessage());
              registration=false;             
            }
        }catch(Exception e){
            System.out.println("Connection failed #####");
            //JOptionPane.showMessageDialog(null,e.getMessage(), "Exception in Connecting to"
            //        + "the database", JOptionPane.ERROR_MESSAGE);
            Alert alert=new Alert(Alert.AlertType.ERROR, e.getMessage(),ButtonType.OK);
            alert.showAndWait();
            System.exit(1);
        }
    }
    public boolean execute(String query){       
        try{
            stmt=con.createStatement();
            stmt.execute(query);
           // con.commit();
            System.out.println("Query: "+query+" execution successful !!!");            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("Exception in Query: "+query+" #####");
             System.out.println("Exception : "+ex.getMessage());
             return false;                         
        }
        return true;
    }
    public ResultSet executeQuery(String query){       
        try{
            stmt=con.createStatement();            
            res=stmt.executeQuery(query);
            
           // con.commit();
            System.out.println("Query: "+query+" execution successful !!!");            
            return res;            
        }catch(Exception e){
               System.out.println("Exception in Query: "+query+" #####");                                                      
               System.out.println("Exception : "+e.getMessage());
        }        
        return null;
    }
    public PreparedStatement getPreparedStatement(String query){       
        try{
            prepstmt=con.prepareStatement(query);
         }catch(Exception e){
               System.out.println("Exception in getPreparedstatement(): "+query+" #####");
               System.out.println("Exception : "+e.getMessage());
               return null;                         
        }
        return prepstmt;
    }
    public ResultSet getScrollableResultSet(String query){       
        try{
            stmt=con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);  // feature not supported in SQLite
            res=stmt.executeQuery(query);           
         }catch(Exception e){
               System.out.println("Exception in getScrollablestatement(): "+query+" #####");
               System.out.println("Exception : "+e.getMessage());
               return null;                         
        }
        return res;
    }
    public Connection getConnection(){
        return con;
    }
    public static void main(String[] args) throws SQLException {     
        Database db=Database.getInstance();
        db.execute("insert into column(column) values('abc')");
        ResultSet rs=db.executeQuery("select * from member");
        while(rs.next()){
            System.out.println(rs.getString(1));
        }
        
    }
    
}
