package home.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.fxml.model.UserSlidePaneController;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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

public class SubmitBookController implements Initializable {

    private Database db=Database.getInstance();
    ResultSet rs;
    @FXML
    private JFXTextField hidden1;
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
    private JFXTextField late_days;
    @FXML
    private JFXTextField fine;
    @FXML
    private JFXTextField total_fine;
    @FXML
    private JFXTextField issued_date;
    @FXML
    private JFXTextField issued_time;
    @FXML
    private JFXButton pay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {      
      mimg.setFill(new ImagePattern(new Image("/home/img/user.png")));     
      bimg.setFill(new ImagePattern(new Image("/home/img/icon1.png"))); 
      time.setValue(LocalTime.now());
      date.setValue(LocalDate.now());         
      hidden1.textProperty().bindBidirectional(bid.textProperty());
      hidden1.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(hidden1.getText().length()==7){
               loadBook();
               String member_id=(String)Util.getRowData("issue_book", "mid", 2, "bid", bid.getText());
               mid.setText(member_id);
               loadMember();
               if(UserSlidePaneController.auto_issue_submit_btn.isSelected()){
                   submit_event(new ActionEvent());
               }
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
            mimg.setFill(new ImagePattern(new Image(new File(".\\img\\member\\img\\"+
                    mid.getText()+".jpg").toURI().toString())));  
            
            
        } catch (SQLException ex) {
            System.out.println("Member not Found...!");                        
        }
    }
    public void loadBook(){
        if(((String)Util.getRowData("book", "available", 2, "id", bid.getText())).equals("Available")){                
            return;
        }         
            try{
            String member_id="";
            int limit=0;
            rs=db.executeQuery("select * from book where id='"+bid.getText()+"'");        
            rs.next();            
            bname.setText(rs.getString("name"));
            author.setText(rs.getString("author"));
            price.setText(rs.getString("price"));
            copies.setText(Util.searchCount("book", "photo", rs.getString("photo"))+"");
            column.setText(rs.getString("column"));            
            bimg.setFill(new ImagePattern(new Image(new File(".\\img\\book\\img\\"+
                    rs.getString("photo")+".jpg").toURI().toString())));                 
            rs=db.executeQuery("select * from issue_book where bid='"+bid.getText()+"'");
            rs.next();
            member_id=rs.getString("mid");
            mid.setText(member_id);            
            String s=rs.getString("issue_date");
            Date d=Date.valueOf(s);
            issued_date.setText(s);        
            issued_time.setText(rs.getString("issue_time"));               
                System.out.println(d.toLocalDate());
            //int days=(int)ChronoUnit.DAYS.between(date.getValue(),Util.toLocalDate(issued_date.getText(), "YYYY-MM-DD"));
            if(member_id.startsWith("S")) limit=(int)Util.getFirstRowData("setting", "srenew_day", 1);
            else limit=(int)Util.getFirstRowData("setting", "trenew_day", 1);
            int days=(int)ChronoUnit.DAYS.between(d.toLocalDate(),date.getValue());
            days-=limit;
            late_days.setText(""+days);
            if(days<0)days=0;
            int fineval=days*(int)Util.getFirstRowData("setting", "rate", 1);
            
            fine.setText(""+fineval);
            int totalfine=fineval+(int)Util.getRowData("member", "fine", 1, "id", member_id);
            total_fine.setText(""+totalfine);
            
            db.execute("update member set fine='"+totalfine+"' where id='"+mid.getText()+"'");
            if(totalfine>0)pay.setDisable(false);
            else pay.setDisable(true);
        } catch (SQLException ex) {
            System.out.println("Book not Found...!");  
            Logger.getLogger(SubmitBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void reset_event(ActionEvent event) {
          try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/SubmitBook.fxml"));
            root.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void submit_event(ActionEvent event) {
        db.execute("update book set available='Available' where id='"+bid.getText()+"'");
        db.execute("delete from issue_book where bid='"+bid.getText()+"'");
        Util.notify("Book Submitted", "Book "+bid.getText()+" is Successfully Submitted", Util.Notification.info);
        reset_event(event);
    }

    @FXML
    private void renew_event(ActionEvent event) {
       int n=0;
       LocalDate ld=date.getValue();
       if(student.isSelected())
             n=(int)Util.getFirstRowData("setting", "srenew_day", 1);
        else
             n=(int)Util.getFirstRowData("setting", "trenew_day", 1);
       db.execute("update issue_book set renew_date='"+ld.plusDays(n)+"' where bid='"+bid.getText()+"'");
       db.execute("update issue_book set issue_date='"+date.getValue()+"' where bid='"+bid.getText()+"'");
       db.execute("update issue_book set issue_time='"+time.getValue()+"' where bid='"+bid.getText()+"'");
        Util.notify("Book Renewed", "Book "+bid.getText()+" is Successfully Renewed", Util.Notification.info);
        reset_event(event);
    }

    @FXML
    private void pay_event(ActionEvent event) {
        Dialog.loadInputDialog(root, 400.0, 300.0, new Text("Enter amount to pay"), "Pay Fine", 100.0, 40.0, JFXDialog.DialogTransition.CENTER, new EventHandler() {
            @Override
            public void handle(Event event) {
                 int amount=Integer.parseInt(Dialog.tf.getText());
                 int final_amount=Integer.parseInt(total_fine.getText());
                 if(final_amount < amount){
                     Util.notify("Invalid Payment ", "Please enter payment <= total", Util.Notification.error);
                      Dialog.dialog.close();
                     return;
                 }else{
                     final_amount-=amount;
                      db.execute("update member set fine="+final_amount+" where id='"+mid.getText()+"'");
                      total_fine.setText(""+final_amount);
                 }
                Dialog.dialog.close();
            }
        });
    }

    @FXML
    private void openimg(MouseEvent event) {
    }
    
}                    