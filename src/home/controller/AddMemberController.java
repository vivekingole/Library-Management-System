package home.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.fxml.model.IDCardController;
import home.fxml.model.ImportMembersController;
import home.resource.barcode.Barcode_Image;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
public class AddMemberController implements Initializable {

    String imgpath1="/home/img/user.png",imgpath2,barcodepath,imgname;
    Database db=Database.getInstance();
    @FXML
    private Circle img;
    @FXML
    private ImageView barcode;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXComboBox<String> course;
    @FXML
    private JFXTextArea addr;
    @FXML
    private JFXTextField phone;
    @FXML
    private StackPane root;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField email;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXRadioButton student;
    @FXML
    private ToggleGroup member;
    @FXML
    private JFXRadioButton teacher;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXButton generate_card_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {     
       img.setFill(new ImagePattern(new Image(imgpath1)));     
        time.setValue(LocalTime.now());
        date.setValue(LocalDate.now());   
        setMemberType();
    }    

    @FXML
    private void reset_event(ActionEvent event) {
        try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/AddMember.fxml"));
            root.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void add_event(ActionEvent event) {
         try{            
            db.execute("insert into member(id,name,course,addr,phone,email,date,time) values("
                    + "'"+id.getText()+"',"
                    + "'"+name.getText()+"',"
                    + "'"+course.getValue()+"',"
                    + "'"+addr.getText()+"',"
                    + "'"+phone.getText()+"',"
                    + "'"+email.getText()+"',"                   
                    + "'"+date.getValue()+"',"                   
                    + "'"+time.getValue()+"'"                   
                    + ")");
            Util.copyFile(imgpath1, imgpath2);        
            Util.copyFile(barcodepath,"img\\member\\barcode\\"+imgname+".png");
            new File("img\\member\\barcode\\temp\\"+imgname+".png").delete();
            Util.notify("Member Added", "Memberid = "+imgname+" added", Util.Notification.success);                             
            ViewMemberController.obj.loadData();
            EditMemberController.obj.loadData();
            generate_card_btn.setDisable(false);         
        } catch (Exception ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    @FXML
    private void barcode_event(ActionEvent event) {
        imgname=id.getText();
        if(imgname.equals("")){
             Util.notify("Code generation fails", "First enter member id to create barcode", Util.Notification.warning);       
        }
        else{
        barcodepath="img\\member\\barcode\\temp\\"+imgname+".png";
        imgpath2="img\\member\\img\\"+imgname+".jpg";
        Barcode_Image.createImage(barcodepath,imgname);
         barcode.setImage(new Image(new File(barcodepath).toURI().toString()));   
        }
        
    }

    @FXML
    private void load_img(MouseEvent event) {
       FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(anchor.getScene().getWindow());
        if(file2!=null){
            imgpath1=file2.getAbsolutePath();
            img.setFill(new ImagePattern(new Image(file2.toURI().toString())));                   
        }       
    }

    @FXML
    private void setMemberType(ActionEvent event) {
        setMemberType();
    }
    @FXML
    private void importMember(ActionEvent event) {
        try {
            StackPane sp=FXMLLoader.load(getClass().getResource("/home/fxml/model/ImportMembers.fxml"));
            Dialog.loadDialogPane(root,sp ,ImportMembersController.close, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setMemberType(){        
        if(student.isSelected()){
            course.setPromptText("Course");
            course.getItems().setAll(Util.getColumn("course","course"));
           ResultSet rs=db.executeQuery("select max(id) from member where id like 'S%'");
          try {
           rs.next();        
           id.setText(Util.generateCode("S",1+Util.decodeInt(rs.getString(1))));
          }  catch (NullPointerException ex) {
           id.setText("S000001");
          }
             catch (SQLException ex) {}
        }else{
            course.setPromptText("Deprtment");
              course.getItems().setAll(Util.getColumn("dept","dept"));
            ResultSet rs=db.executeQuery("select max(id) from member where id like 'T%'");
          try {
           rs.next();        
           id.setText(Util.generateCode("T",1+Util.decodeInt(rs.getString(1))));
          }  catch (NullPointerException ex) {
           id.setText("T000001");
          }
             catch (SQLException ex) {}
        }
    }

    @FXML
    private void generateCard(ActionEvent event) {
         try{
            int d=date.getValue().getYear();
            IDCardController.setValues((String)Util.getFirstRowData("setting", "idcard_title", 2), id.getText(), name.getText(),d+" - "+(d+1),"31 / 5 / "+(d+1),phone.getText(), addr.getText());
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/IDCard.fxml"));
            Dialog.loadDialogPane(root, pane, IDCardController.close_btn, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
