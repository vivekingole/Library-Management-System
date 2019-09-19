/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import home.util.Dialog;
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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;


public class IDCardController implements Initializable {
    
    public static JFXButton close_btn;
    WritableImage imagelayout=new WritableImage(460,285);
    public static String title1,id1,name1,year1,valid1,phone1,address1;
    @FXML
    private AnchorPane idpane;
    @FXML
    private Text title;
    @FXML
    private ImageView barcode;
    @FXML
    private Text id;
    @FXML
    private Text name;
    @FXML
    private Text year;
    @FXML
    private Text valid;
    @FXML
    private Text phone;
    @FXML
    private Text address;
    @FXML
    private ImageView image;
    @FXML
    private AnchorPane anchor2;
    @FXML
    private JFXButton close;
    
    public static void setValues(String title,String id,String name,String year,String valid,String phone,String address){
       title1=title;      
       id1=id;
       name1=name;
       year1=year;
       valid1=valid;
       phone1=phone;
       address1=address; 
    }
    @FXML
    private StackPane stack_pane;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {                
        close_btn=close;
       image.setImage(new Image(new File(".\\img\\member\\img\\"+id1+".jpg").toURI().toString()));
       barcode.setImage(new Image(new File(".\\img\\member\\barcode\\"+id1+".png").toURI().toString()));
       title.setText(title1);
       id.setText(id1);
       name.setText(name1);
       year.setText(year1);
       valid.setText(valid1);
       phone.setText(phone1);
       address.setText(address1);       
       //idpane.snapshot(null, imagelayout);
       anchor2.snapshot(null, imagelayout);
       File outFile = new File(".\\img\\member\\idcard\\"+id1+".jpg");       
       try {
            ImageIO.write(SwingFXUtils.fromFXImage(imagelayout, null), "png", outFile);
        } catch (Exception e) {System.err.println(e);}              
    }        

   

    @FXML
    private void print(ActionEvent event) {
    }
}
