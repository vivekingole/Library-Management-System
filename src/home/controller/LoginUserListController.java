package home.controller;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXSpinner;
import home.databasecontroller.Database;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
public class LoginUserListController implements Initializable {

  
    Database db=Database.getInstance();
    @FXML
    private StackPane stack;
    @FXML
    private JFXListView<Node> list;
    @Override    
    public void initialize(URL url, ResourceBundle rb) {                
             try{
                 ResultSet rs=null;
                AnchorPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/UserNode.fxml"));
                Circle img=(Circle)pane.getChildren().get(0);
                Label name=(Label)pane.getChildren().get(1);                
                Label password=(Label)pane.getChildren().get(2);                
                rs=db.executeQuery("select * from admin");
                img.setFill(new ImagePattern(new Image(new File(".\\img\\admin\\admin.jpg").toURI().toString()))); 
                name.setText(rs.getString("name"));
                password.setText(Util.decryptData(rs.getString("password")));
                list.getItems().add(pane);
                
                rs=db.executeQuery("select * from user");
                while(rs.next()){                   
                pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/UserNode.fxml"));
                img=(Circle)pane.getChildren().get(0);
                name=(Label)pane.getChildren().get(1);                
                password=(Label)pane.getChildren().get(2);                
                img.setFill(new ImagePattern(new Image(new File(".\\img\\user\\"+rs.getString("userid")+".jpg").toURI().toString()))); 
                name.setText(rs.getString("username"));
                password.setText(Util.decryptData(rs.getString("password")));
                list.getItems().add(pane);      
                }
            } catch (SQLException ex) {
            Logger.getLogger(LoginUserListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginUserListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      

    @FXML
    private void continue_event(ActionEvent event) {
        //UserLoginController.selecteduser=(AnchorPane)list.getSelectionModel().getSelectedItem();
        AnchorPane p=(AnchorPane)list.getSelectionModel().getSelectedItem();
      
        if(list.getSelectionModel().getSelectedIndex()!=0){
        UserLoginController.username=((Label)p.getChildren().get(1)).getText();
        UserLoginController.password=((Label)p.getChildren().get(2)).getText();
        UserLoginController.id=(String)Util.getRowData("user", "userid", 2, "username", UserLoginController.username);
        UserLoginController.setSelectedUser();
        }else{
            AdminLoginController.admin_name=((Label)p.getChildren().get(1)).getText();
            AdminLoginController.admin_pass=((Label)p.getChildren().get(2)).getText();
        }
        if(list.getSelectionModel().getSelectedIndex()==0)
           StartingTabController.registertab.getTabPane().getSelectionModel().select(4);
        else{
           StartingTabController.registertab.getTabPane().getSelectionModel().select(3);           
        }
    }

    @FXML
    private void back(ActionEvent event) {
        StartingTabController.registertab.getTabPane().getSelectionModel().select(1);
    }
    
}
