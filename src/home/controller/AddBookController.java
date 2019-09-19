package home.controller;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import home.resource.barcode.Barcode_Image;  //without jar file
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.fxml.model.ImportBooksController;
import home.model.Book;
import home.util.Dialog;
import home.util.Util;
import home.util.Validator;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
public class AddBookController implements Initializable {
    
    int flag=0;
    String imgpath1="/home/img/icon1.png",imgpath2,barcodepath,imgname,photo;
    Database db=Database.getInstance();
    @FXML
    private ImageView barcode;
    @FXML
    private JFXTextField id;
    @FXML
    private StackPane root;
    @FXML
    private JFXTextField name;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField copies;
    @FXML
    private Circle img1;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXComboBox<String> column;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXSpinner mainspinner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        img1.setFill(new ImagePattern(new Image(imgpath1)));  
        time.setValue(LocalTime.now());
        date.setValue(LocalDate.now());   
        column.getItems().setAll(Util.getColumn("column", "column"));
        ResultSet rs=db.executeQuery("select max(id) from book");
          try {
         rs.next();        
           id.setText(Util.generateCode("B",1+Util.decodeInt(rs.getString(1))));
          }  catch (NullPointerException ex) {
           id.setText("B000001");
          }
             catch (SQLException ex) {}
    }    

    @FXML
    private void reset_event(ActionEvent event) {
        try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/AddBook.fxml"));
            root.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void add_event(ActionEvent event){
            //copies cannot be 0//            
       mainspinner.setVisible(true);
            String bid="";
            if(Validator.checkRequiredValidator(id,name,author,copies,price)
           &&Validator.checkRequiredValidator(column)){           
            int n=Util.decodeInt(id.getText()); 
           try{
               for(int i=0;i<Integer.parseInt(copies.getText());i++){
                   bid=Util.generateCode("B",n);
                   add(bid);
                   if(i>0)
                      Barcode_Image.createImage("img\\book\\barcode\\"+bid+".png",bid);
                   n++;
               }                 
               Util.copyFile(imgpath1, imgpath2);        
               Util.copyFile(barcodepath,"img\\book\\barcode\\"+imgname+".png");
               new File("img\\book\\barcode\\temp\\"+imgname+".png").delete();
               Util.notify("Book Added", "Bookid = "+imgname+" added", Util.Notification.success);       
               reset_event(event);
           }catch(Exception e){
               System.out.println(e.getMessage());            
           }
        }else{
            
        }
    }
   
    private void add(String bid)throws Exception
    {           
            db.execute("insert into book(id,name,author,price,date,time,column,photo,available) values("
                    + "'"+bid+"',"
                    + "'"+name.getText()+"',"
                    + "'"+author.getText()+"',"
                    + price.getText()+","
                    + "'"+date.getValue()+"',"
                    + "'"+time.getValue()+"',"                            
                    + "'"+column.getValue()+"',"                            
                    + "'"+id.getText()+"',"                   
                    + "'Available'"                   
                    + ")");
            ViewBookController.obj.loadData();
            EditBookController.obj.loadData();
      }

    @FXML
    private void barcode_event(ActionEvent event) {
        spinner.setVisible(true);
        Runnable barcode_thread=()->{
        imgname=id.getText();
        if(imgname.equals("")){
            Util.notify("Code generation fails", "First enter book id to create barcode", Util.Notification.warning);       
        }
        else{
        barcodepath="img\\book\\barcode\\temp\\"+imgname+".png";
        imgpath2="img\\book\\img\\"+imgname+".jpg";
        Barcode_Image.createImage(barcodepath,imgname);
         barcode.setImage(new Image(new File(barcodepath).toURI().toString()));   
        }  
        spinner.setVisible(false);
      };//thread
      new Thread(barcode_thread,"BARCODE_THREAD").start();                
    }
    
    @FXML
    private void load_img(MouseEvent event) {
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(anchor.getScene().getWindow());
        if(file2!=null){
            imgpath1=file2.getAbsolutePath();
            img1.setFill(new ImagePattern(new Image(file2.toURI().toString())));                   
        }              
    }

    @FXML
    private void importBook(ActionEvent event) {
        try {
            StackPane sp=FXMLLoader.load(getClass().getResource("/home/fxml/model/ImportBooks.fxml"));
            Dialog.loadDialogPane(root,sp ,ImportBooksController.close, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
