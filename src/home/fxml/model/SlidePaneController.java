/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author sai
 */
public class SlidePaneController implements Initializable {
    public static VBox box2;
    public static SlidePaneController obj;
    public static JFXButton home,book,issuesubmit,member,user,setting,logout;
    @FXML
    private VBox vbox2;
    @FXML
    private JFXButton home1;
    @FXML
    private JFXButton book1;
    @FXML
    private JFXButton issuesubmit1;
    @FXML
    private JFXButton member1;
    @FXML
    private JFXButton user1;
    @FXML
    private JFXButton setting1;
    @FXML
    private VBox vbox1;
    @FXML
    private JFXButton logout1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obj=this;
       box2=vbox2;
       home=home1;
       book=book1;
       issuesubmit=issuesubmit1;
       member=member1;
       user=user1;
       setting=setting1;
       logout=logout;
    }    

    
}
