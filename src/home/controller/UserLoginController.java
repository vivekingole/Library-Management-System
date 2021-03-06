package home.controller;




import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import home.databasecontroller.Database;
import home.launcher.StartingTab;
import home.resource.animatefx.animation.Shake;
import home.util.Dialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
public class UserLoginController implements Initializable {

    public static String id;
    public static String password;
    public static String username;
    public static Label name1;
    public static Circle img1;
    Database db=Database.getInstance();
    @FXML
    private JFXPasswordField pass;     
    @FXML
    private JFXButton btn;    
    @FXML
    private Label status;
    @FXML
    private StackPane stack;
    @FXML
    private Circle img;
    @FXML
    private JFXButton back;
    @FXML
    private Label name;
    @Override    
    public void initialize(URL url, ResourceBundle rb) {     
        
        //***********updete forget pssword *******************??
      name1=name;
      img1=img;
    }    
    public static void setSelectedUser(){
       img1.setFill(new ImagePattern(new Image(new File("img\\user\\"+id+".jpg").toURI().toString())));        
       name1.setText(username);
    }
    @FXML
    private void login(ActionEvent event) {
        try {                  
            String temppass=pass.getText();                        
            if(temppass.equals("")){
             status.setText("Please fill all the fields !");             
             new Shake(btn).play();
            }else{                            
               if(temppass.equals(password)){
                   MainController.currentuser=id;
                   StackPane root=FXMLLoader.load(getClass().getResource("/home/fxml/Main.fxml"));
                   StartingTab.root.getChildren().setAll(root);
                }else{
                 status.setText("invalid userid or password !");
               }               
            }                                          
        } catch (Exception ex) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @FXML
    private void backEvent(ActionEvent event) {
         StartingTabController.registertab.getTabPane().getSelectionModel().select(2);
    }

    @FXML
    private void forgetPassword(ActionEvent event) {
         try {
            ForgetPasswordController.id=id;
            StackPane sp=FXMLLoader.load(getClass().getResource("/home/fxml/ForgetPassword.fxml"));
            Dialog.loadDialogPane(stack,sp ,ForgetPasswordController.close, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ex) {
            Logger.getLogger(AdminLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openimg(MouseEvent event) {
    }
   
}
