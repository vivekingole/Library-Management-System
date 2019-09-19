/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import home.controller.MainController;
import home.launcher.StartingTab;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class UserSlidePaneController implements Initializable,EventHandler<ActionEvent> {
    public static JFXToggleButton auto_issue_submit_btn;
    @FXML
    private AnchorPane sidepane2;
    @FXML
    private Circle active_user;
    @FXML
    private Label active_user_name;
    @FXML
    private JFXToggleButton auto_issue_submit;    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        auto_issue_submit_btn=auto_issue_submit;
    }    


    @FXML
    private void changePassword(ActionEvent event) {
        Dialog.loadInputDialog(MainController.sp, 400.0, 300.0, new Text("Enter Password :"), "Change Password", 150.0, 40.0, JFXDialog.DialogTransition.CENTER, this, "Old Password", "New Passwword");
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/StartingTab.fxml"));
            StartingTab.root.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void updatePhoto(ActionEvent event) {
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(sidepane2.getScene().getWindow());
        if(file2!=null){
            System.out.println(file2.getPath()+"       "+ ".\\img\\user\\"+MainController.currentuser+".jpg");
           Util.copyFile(file2.getPath(), ".\\img\\user\\"+MainController.currentuser+".jpg");
           active_user.setFill(new ImagePattern(new Image(file2.toURI().toString())));
           MainController.user_image.setFill(new ImagePattern(new Image(file2.toURI().toString())));    
        }              
    }

    @Override
    public void handle(ActionEvent event) {
        JFXButton btn=(JFXButton)event.getSource();
        if(btn.getText().equals("Change Password")){
            String old=(String)Util.getRowData("user", "password", 2, "userid", MainController.currentuser);
            old=Util.decryptData(old);
           if(old.equals(Dialog.tf.getText()))
           {
                String new_pass=Util.encryptData(Dialog.tf1.getText());
                Util.update("user", "password", new_pass, "userid", MainController.currentuser, 2);
                Util.notify("password change successful", "your password has been changed ! to "+Dialog.tf1.getText(), Util.Notification.info);
           }else{
               Util.notify("Change password fails", "Sorrry ! password you entered does not matched.", Util.Notification.error);
           }
        }
                Dialog.dialog.close();
    }

    
}
