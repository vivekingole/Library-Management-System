/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author sai
 */
public class ProfileController implements Initializable {
    //// 
        static Image image;
        static String titlestring,subtitlestring;
        static Node nodelist1[];
        static Node nodelist2[];
        public static JFXButton close;
    ////    
    @FXML
    private AnchorPane ancher;
    @FXML
    private Circle img;
    private VBox vbox;
    @FXML
    private Label title;
    @FXML
    private Label subtitle;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private GridPane gridpane;
    @FXML
    private JFXButton close_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        close=close_btn;
        title.setText(titlestring);
        subtitle.setText(subtitlestring);
        img.setFill(new ImagePattern(image));
           // vbox.getChildren().setAll(node);
         gridpane.addColumn(0, nodelist1);
         gridpane.addColumn(1, nodelist2);
    }    
    public static void setValues(Image img,String title,String subtitle,Node []node1,Node []node2){
       image=img;
       titlestring=title;
       subtitlestring=subtitle;
       nodelist1=node1;
       nodelist2=node2;
    }

    @FXML
    private void close(ActionEvent event) {
        ((Stage)ancher.getScene().getWindow()).close();
    }
}
