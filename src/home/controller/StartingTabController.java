/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.controller;

import com.jfoenix.controls.JFXTabPane;
import home.databasecontroller.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author SATISH
 */
public class StartingTabController implements Initializable {
    static Tab registertab,welcometab,listtab,logintab;
    StackPane pane;
    Database db=Database.getInstance();
    @FXML
    private Tab register;
    @FXML
    private Tab welcome;
    @FXML
    private Tab list;
    @FXML
    private Tab login;
    @FXML
    private StackPane sp1;
    @FXML
    private StackPane sp2;
    @FXML
    private StackPane sp3;
    @FXML
    private StackPane sp4;
    @FXML
    private JFXTabPane tabpane;
    @FXML
    private Tab adminlogin;
    @FXML
    private StackPane sp5;

  @Override
    public void initialize(URL url, ResourceBundle rb) {
        registertab=register;
        welcometab=welcome;
        listtab=list;
        logintab=login;
         register.getTabPane().getSelectionModel().select(1);
          try {
            pane=FXMLLoader.load(getClass().getResource("/home/fxml/AdminRegistration.fxml"));
            sp1.getChildren().setAll(pane);  
            pane=FXMLLoader.load(getClass().getResource("/home/fxml/Welcome.fxml"));
            sp2.getChildren().setAll(pane);
            pane=FXMLLoader.load(getClass().getResource("/home/fxml/LoginUserList.fxml"));
            sp3.getChildren().setAll(pane);
            pane=FXMLLoader.load(getClass().getResource("/home/fxml/UserLogin.fxml"));
            sp4.getChildren().setAll(pane);            
            pane=FXMLLoader.load(getClass().getResource("/home/fxml/AdminLogin.fxml"));
            sp5.getChildren().setAll(pane);            
        } catch (IOException ex) {
            Logger.getLogger(StartingTabController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }    
    
}
