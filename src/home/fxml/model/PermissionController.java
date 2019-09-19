package home.fxml.model;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import home.controller.EditBookController;
import home.controller.EditUserController;
import home.databasecontroller.Database;
import home.util.Dialog;
import static home.util.Util.db;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sai
 */
public class PermissionController implements Initializable,EventHandler<ActionEvent>  {

    JFXCheckBox check[][]=new JFXCheckBox[4][];
    Database db=Database.getInstance();
    public static String id;
    @FXML
    private JFXCheckBox book;
    @FXML
    private JFXCheckBox badd;
    @FXML
    private JFXCheckBox bedit;
    @FXML
    private JFXCheckBox bissue;
    @FXML
    private JFXCheckBox bsubmit;
    @FXML
    private JFXCheckBox bview;
    @FXML
    private JFXCheckBox member;
    @FXML
    private JFXCheckBox madd;
    @FXML
    private JFXCheckBox medit;
    @FXML
    private JFXCheckBox mview;
    @FXML
    private JFXCheckBox user;
    @FXML
    private JFXCheckBox uadd;
    @FXML
    private JFXCheckBox uedit;
    @FXML
    private JFXCheckBox uview;
    @FXML
    private JFXCheckBox setting;
    @FXML
    private JFXCheckBox view;
    @FXML
    private JFXCheckBox all;
    @FXML
    private AnchorPane anchor;
    @FXML
    private StackPane stack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        check[0]=new JFXCheckBox[6];
        check[1]=new JFXCheckBox[4];
        check[2]=new JFXCheckBox[4];
        check[3]=new JFXCheckBox[3];
        
        check[0][0]=book;
        check[0][1]=badd;
        check[0][2]=bedit;
        check[0][3]=bissue;
        check[0][4]=bsubmit;
        check[0][5]=bview;       
       
        check[1][0]=member;
        check[1][1]=madd;
        check[1][2]=medit;
        check[1][3]=mview;
                            
        check[2][0]=user;
        check[2][1]=uadd;
        check[2][2]=uedit;
        check[2][3]=uview;
        
        check[3][0]=setting;
        check[3][1]=view;
        check[3][2]=all;
        
         try {
            int x=1;
            ResultSet rs;
            rs=db.executeQuery("select * from user_permission where id='"+id+"'");
            rs.next();
        book.setSelected(rs.getBoolean("book"));
        badd.setSelected(rs.getBoolean("badd"));
        bedit.setSelected(rs.getBoolean("bedit"));
        bissue.setSelected(rs.getBoolean("bissue"));
        bsubmit.setSelected(rs.getBoolean("bsubmit"));
        bview.setSelected(rs.getBoolean("bview"));
       
        member.setSelected(rs.getBoolean("member"));
        madd.setSelected(rs.getBoolean("madd"));
        medit.setSelected(rs.getBoolean("medit"));
        mview.setSelected(rs.getBoolean("mview"));
                            
        user.setSelected(rs.getBoolean("user"));
        uadd.setSelected(rs.getBoolean("uadd"));
        uedit.setSelected(rs.getBoolean("uedit"));
        uview.setSelected(rs.getBoolean("uview"));
        
        setting.setSelected(rs.getBoolean("setting"));
        view.setSelected(rs.getBoolean("view"));
        all.setSelected(rs.getBoolean("all"));
        
         } catch (SQLException ex) {
            Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }    

    @FXML
    private void permission(ActionEvent event) {
         if(check[3][2].isSelected()){
            for(int i=0;i<=3;i++)
                for(JFXCheckBox c:check[i])
                    c.setSelected(true);
        }
        if(check[3][1].isSelected()){
            check[0][5].setSelected(true);
            check[1][3].setSelected(true);
            check[2][3].setSelected(true);
        }
        if(check[0][0].isSelected()){
            for(int i=1;i<=5;i++)
                check[0][i].setSelected(true);
        }
        if(check[1][0].isSelected()){
            for(int i=1;i<=3;i++)
                check[1][i].setSelected(true);
        }
        if(check[2][0].isSelected()){
            for(int i=1;i<=3;i++)
                check[2][i].setSelected(true);
        }
    }
    @FXML
    private void close(ActionEvent event) {
        ((Stage)anchor.getScene().getWindow()).close();    
    }

    @FXML
    private void update(ActionEvent event) {
        try {
            int x=1;
             db.execute("delete from user_permission where id='"+id+"'");
             String s="";
                   for(int i=0;i<=3;i++)
                        for(int j=0;j<check[i].length;j++){                            
                            s+= ""+check[i][j].isSelected()+"";  ///////////////////check
                            if(!(i==3 && j==2))
                                s+=",";
                        }
                 db.execute("insert into user_permission values("
                    + "'"+id+"',"
                    +s
                    + ")");
            Dialog.loadDialog(stack, new Text("Permission Updated"),new Text("Permissions for the user id \" "+id+" \" is updated.\n"
                    + "To update changes in the system you have to \nrestart it."), "Ok", 160.0, 40.0, JFXDialog.DialogTransition.CENTER,this);            
        } catch (Exception ex) {
            System.out.println("###error in permission update");
            Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override //this method gets called when press "ok" button of dialog box
    public void handle(ActionEvent event) {       
       close(event);
    }
    
}
