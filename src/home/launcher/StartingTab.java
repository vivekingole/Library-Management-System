package home.launcher;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import home.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ECS-III
 */           
public class StartingTab extends Application {
    
     public static Stage stage;
     public static StackPane root;
     MainController mc=new MainController();
    @Override
    public void start(Stage stage) throws Exception {
         StartingTab.stage=stage;       
        Parent root = FXMLLoader.load(getClass().getResource("/home/fxml/StartingTab.fxml"));        
       StartingTab.root=(StackPane)root;
        Scene scene = new Scene(root,1020,770);        
        stage.setScene(scene);
       // stage.setIconified(true);
        stage.centerOnScreen();     
       // stage.initStyle(StageStyle.UNDECORATED);
        stage.show();       
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
