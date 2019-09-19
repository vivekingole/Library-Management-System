package home.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import home.databasecontroller.Database;
import home.launcher.model.Info;
import home.fxml.model.InfoController;
import home.launcher.StartingTab;
import home.launcher.Welcome;
import home.resource.animatefx.animation.Pulse;
import home.resource.animatefx.animation.ZoomIn;
import home.util.Dialog;
import home.util.Util;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController implements Initializable {

    private StackPane sp;
    @FXML
    private StackPane stack;
    Database db=Database.getInstance();  
    @FXML
    private ImageView backimg;
    @FXML
    private ImageView iconimg;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXButton info;
    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        try {
            //Stage stage= Welcome.stage;// at the time of execute single
            sp=FXMLLoader.load(getClass().getResource("/home/fxml/model/Info.fxml"));
            Pulse infoanimation=new Pulse(info);
            infoanimation.setCycleCount(-1);
             infoanimation.play();
            ZoomIn z1=new ZoomIn(c1);
            z1.setCycleCount(-1);
            z1.setCycleDuration(200);
            z1.play();
            ZoomIn z2=new ZoomIn(c2);
            z2.setCycleCount(-1);
            z2.setDelay(Duration.millis(200));
            z2.play();
            
            Stage stage=StartingTab.stage;
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                anchor.setPrefWidth(newVal.intValue());
                backimg.setFitWidth(newVal.doubleValue());
                
            });
            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                anchor.setPrefHeight(newVal.intValue());
                backimg.setFitHeight(newVal.doubleValue());
                
            });
        } catch (IOException ex) {
            Logger.getLogger(WelcomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }    

    @FXML
    private void start(ActionEvent event) throws IOException {
           if(Database.getInstance().registration==false)   
            StartingTabController.registertab.getTabPane().getSelectionModel().select(0); //root =FXMLLoader.load(getClass().getResource("AdminRegistration.fxml"));
           else
             StartingTabController.registertab.getTabPane().getSelectionModel().select(2);//root =FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
           //stack.getChildren().setAll(root);                         
    }

    private void minimise(ActionEvent event) {
         Stage s1=(Stage)((JFXButton)event.getSource())
                 .getScene()
                 .getWindow();
         s1.setIconified(true);
        
    }
    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void info(ActionEvent event) {        
        Dialog.loadDialogPane(stack,sp ,InfoController.close, JFXDialog.DialogTransition.CENTER);
        InfoController.runAnimation1();
    }
    
}                    