package home.controller;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import home.databasecontroller.Database;
import home.launcher.StartingTab;
import home.resource.animatefx.animation.Shake;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javax.swing.JOptionPane;
public class AdminLoginController implements Initializable {

    public static String admin_name;
    public static String admin_pass;
    @FXML
    private JFXPasswordField pass;     
    Database db=Database.getInstance();
    @FXML
    private JFXButton btn;
    private JFXSpinner sp;
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
        try{
            img.setFill(new ImagePattern(new Image(new File("img\\admin\\admin.jpg").toURI().toString())));        
            name.setText(admin_name);
        }catch(Exception ex){System.out.println("admin image");}
    }    

    @FXML
    private void login(ActionEvent event) {
        try {                  
            String password=pass.getText();                        
            if(password.equals("")){
             status.setText("Please fill all the fields !");             
             new Shake(btn).play();
            }else{                            
            //ResultSet rs=db.executeQuery("select * from admin");
            //rs.next();
            //String pass=Util.decryptData(rs.getString("password"));
               if(password.equals(admin_pass)){
                   MainController.currentuser="A000001";
                   StackPane root=FXMLLoader.load(getClass().getResource("/home/fxml/Main.fxml"));
                   StartingTab.root.getChildren().setAll(root);            
                }else{
                 status.setText("invalid userid or password !");
                 new Shake(btn).play();
               }               
            }                                          
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null,e.getMessage(), "Exception"
                    + "", JOptionPane.WARNING_MESSAGE);               
        }
    }
    @FXML
    private void backEvent(ActionEvent event) {
         StartingTabController.registertab.getTabPane().getSelectionModel().select(2);
    }

    @FXML
    private void forgetPassword(ActionEvent event) {
        try {
            ForgetPasswordController.id="A000001";
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
