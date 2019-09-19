/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;


public class IDCardBackSideController implements Initializable {
    
    public static JFXButton close_btn;
    WritableImage imagelayout=new WritableImage(460,285);   
    @FXML
    private AnchorPane idpane;
   @FXML
    private AnchorPane anchor2;
    @FXML
    private Text rules;
    @FXML
    private JFXButton close;

    @Override
    public void initialize(URL url, ResourceBundle rb) {           
        close_btn=close;
       rules.setText((String)Util.getFirstRowData("setting", "idcard_rules", 2));
       anchor2.snapshot(null, imagelayout);
       File outFile = new File(".\\img\\member\\idcard\\backside\\backside.jpg");       
       try {
            ImageIO.write(SwingFXUtils.fromFXImage(imagelayout, null), "png", outFile);
        } catch (Exception e) {System.err.println(e);}              
    }        


    @FXML
    private void print(ActionEvent event) {
    }
}
