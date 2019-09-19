package home.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import home.databasecontroller.Database;
import home.fxml.model.UserSlidePaneController;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class IssueBookController implements Initializable {

    private Database db=Database.getInstance();
    ResultSet rs;
    @FXML
    private JFXTextField hidden1;
    @FXML
    private JFXTextField hidden2;
    @FXML
    private JFXTextField mid;
    @FXML
    private JFXTextField bname;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTextArea addr;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXRadioButton student;
    @FXML
    private ToggleGroup member;
    @FXML
    private JFXRadioButton teacher;
    @FXML
    private Circle mimg;
    @FXML
    private Circle bimg;
    @FXML
    private JFXTextField bid;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField copies;
    @FXML
    private JFXTextField column;
    @FXML
    private StackPane root;
    @FXML
    private JFXTextField mname;
    @FXML
    private JFXToggleButton checkScanner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {      
      mimg.setFill(new ImagePattern(new Image("/home/img/user.png")));     
      bimg.setFill(new ImagePattern(new Image("/home/img/icon1.png"))); 
      time.setValue(LocalTime.now());
      date.setValue(LocalDate.now());         
      hidden1.textProperty().bindBidirectional(mid.textProperty());
      hidden2.textProperty().bindBidirectional(bid.textProperty());
      hidden1.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(hidden1.getText().length()==7)
               loadMember();
      });
      hidden2.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(hidden2.getText().length()==7)
               loadBook();
             
      }); 
      hidden1.focusedProperty().addListener(listener->{
          if(!hidden1.isFocused()){
              checkScanner.setSelected(false);
          }
      });     
      hidden2.focusedProperty().addListener(listener->{
          if(hidden2.isFocused()){
              checkScanner.setSelected(true);
          }
      });
        }             
   
    public void loadMember(){        
        try{
            rs=db.executeQuery("select * from member where id='"+mid.getText()+"'");        
            rs.next();            
            mname.setText(rs.getString("name"));
            addr.setText(rs.getString("addr"));
            email.setText(rs.getString("email"));
            phone.setText(rs.getString("phone"));
            if(mid.getText().startsWith("S")) student.setSelected(true);
            else teacher.setSelected(true);
            hidden2.requestFocus();
            mimg.setFill(new ImagePattern(new Image(new File(".\\img\\member\\img\\"+
                    mid.getText()+".jpg").toURI().toString())));     
        } catch (SQLException ex) {
            System.out.println("Member not Found...!");                        
        }
    }
    public void loadBook(){
         try{
            rs=db.executeQuery("select * from book where id='"+bid.getText()+"'");        
            rs.next();            
            bname.setText(rs.getString("name"));
            author.setText(rs.getString("author"));
            price.setText(rs.getString("price"));
            copies.setText(Util.searchCount("book", "photo", rs.getString("photo"))+"");
            column.setText(rs.getString("column"));            
            bimg.setFill(new ImagePattern(new Image(new File(".\\img\\book\\img\\"+
                    rs.getString("photo")+".jpg").toURI().toString())));    
            hidden1.requestFocus();
            if(UserSlidePaneController.auto_issue_submit_btn.isSelected()){
                issue_event(new ActionEvent());
            }
        } catch (SQLException ex) {
            System.out.println("Book not Found...!");                        
        }
    }
    @FXML
    private void reset_event(ActionEvent event) {
          try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/IssueBook.fxml"));
            root.getChildren().setAll(pane);                        
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }         
    }

    @FXML
    private void issue_event(ActionEvent event) {
        if(!mid.getText().equals("") || !bid.getText().equals("")){                   
        int book_count=(int)Util.getRowData("member", "book_count", 1, "id", mid.getText());
        int n=0,issued_count=0;
        time.setValue(LocalTime.now());
        LocalDate d=date.getValue();    
        if(student.isSelected()){
             n=(int)Util.getFirstRowData("setting", "srenew_day", 1);
             issued_count=(int)Util.getFirstRowData("setting", "max_sissue", 1);
        }
        else{
             n=(int)Util.getFirstRowData("setting", "trenew_day", 1);
             issued_count=(int)Util.getFirstRowData("setting", "max_tissue", 1);
        }
        if(book_count < issued_count){
        try{
            db.execute("insert into issue_book (mid,bid,issue_date,issue_time,renew_date)"
                    + " values ("
                    + "'"+mid.getText()+"',"
                    + "'"+bid.getText()+"',"
                    + "'"+date.getValue()+"',"
                    + "'"+time.getValue()+"',"                   
                    + "'"+d.plusDays(n)+"'"
                    + ")");
            db.execute("update book set available='Unavailable' where id='"+bid.getText()+"'");
            db.execute("update member set book_count='"+(book_count+1)+"' where id='"+mid.getText()+"'");
            Util.notify("Book Issued", "Book "+bid.getText()+" is Successfully issued", Util.Notification.info);
            reset_event(event);
        }catch(Exception e){
            System.out.println("Issue event"+e.getMessage());
        }
        }else{
            Dialog.loadDialog(root, new Text("Issue book denied"), new Text("Sorry ! Member can't issue more than limit"), "ok", 100.0, 40.0, JFXDialog.DialogTransition.CENTER);
        }
        }else{
            Util.notify("Syntax error", "Please load both Member and Book", Util.Notification.error);
        }
    }

    @FXML
    private void openimg(MouseEvent event) {
    }

    @FXML
    private void checkToScan(ActionEvent event) {
         if(checkScanner.isSelected()){
            hidden1.requestFocus();
        }
    }
    
}                    