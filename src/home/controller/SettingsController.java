package home.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import home.databasecontroller.Database;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class SettingsController implements Initializable,EventHandler<ActionEvent> {
    JFXPopup pop=new JFXPopup();
    Database db=Database.getInstance();
  
    @FXML
    private StackPane stack;
    @FXML
    private JFXComboBox<String> course;
    @FXML
    private JFXButton delete_department;
    @FXML
    private JFXButton add_department;
    @FXML
    private JFXComboBox<String> column;
    @FXML
    private JFXButton delete_column;
    @FXML
    private JFXButton add_column;
    @FXML
    private JFXButton delete_course;
    @FXML
    private JFXButton add_course;
    @FXML
    private JFXComboBox<String> dept;
    @FXML
    private JFXTextField student_renew_days;
    @FXML
    private JFXTextField teacher_renew_days;
    @FXML
    private JFXTextField fine_rate;
    @FXML
    private JFXTextField student_max_issue;
    @FXML
    private JFXTextField teacher_max_issue;
    @FXML
    private JFXToggleButton after_issue;
    @FXML
    private JFXToggleButton after_submit;
    @FXML
    private JFXToggleButton submit_alert;
    @FXML
    private JFXTextField id_title;
    @FXML
    private ImageView logo;
    @FXML
    private JFXTextArea rules;
    @FXML
    private JFXTextField college_name;
    @FXML
    private ScrollPane scroll;
    @FXML
    private AnchorPane anchor;
    @FXML
    private VBox vbox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ issue / submit ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
          student_renew_days.setText(""+(int)Util.getFirstRowData("setting", "srenew_day", 1));
          teacher_renew_days.setText(""+(int)Util.getFirstRowData("setting", "trenew_day", 1));
          student_max_issue.setText(""+(int)Util.getFirstRowData("setting", "max_sissue", 1));
          teacher_max_issue.setText(""+(int)Util.getFirstRowData("setting", "max_tissue", 1));
          fine_rate.setText(""+(int)Util.getFirstRowData("setting", "rate", 1));
          if((boolean)Util.getFirstRowData("setting", "email_after_issue", 0))
              after_issue.setSelected(true);
          else
              after_issue.setSelected(false);
          if((boolean)Util.getFirstRowData("setting", "email_after_submission", 0))
              after_submit.setSelected(true);
          else
              after_submit.setSelected(false);
          if((boolean)Util.getFirstRowData("setting", "email_submission_alert", 0))
              submit_alert.setSelected(true);
          else
              submit_alert.setSelected(false);
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Add new ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        dept.getItems().setAll(Util.getColumn("dept","dept"));
        course.getItems().setAll(Util.getColumn("course","course"));
        column.getItems().setAll(Util.getColumn("column","column"));
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ID card ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
          id_title.setText((String)Util.getFirstRowData("setting", "idcard_title", 2));
          rules.setText((String)Util.getFirstRowData("setting", "idcard_rules", 2));
          logo.setImage(new Image(new File(".\\img\\id_logo.png").toURI().toString()));
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ General ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
          
          college_name.setText((String)Util.getFirstRowData("setting", "college_name", 2));
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ General ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        scroll.widthProperty().addListener(listener->{
         anchor.setPrefWidth(scroll.getWidth());
        });
        }
    @Override   ////// Eventhandler 
    public void handle(ActionEvent event) {  
        JFXButton src=(JFXButton)event.getSource();
        if(src.getText().equals("Add course")){
               course.getItems().add(Dialog.tf.getText());                
                Dialog.dialog.close();
       }else if(src.getText().equals("Add column")){
          column.getItems().add(Dialog.tf.getText());
        Dialog.dialog.close();           
        }else if(src.getText().equals("Add Department")){
          dept.getItems().add(Dialog.tf.getText());
        Dialog.dialog.close();           
        }
    }
     //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Add /remove Item handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
    @FXML
    private void add_course(ActionEvent event) {
        Dialog.loadInputDialog(stack, 400.0, 300.0, new Text("Enter Value"), "Add course", 200.0, 50.0, JFXDialog.DialogTransition.CENTER,this);        
    }
    @FXML
    private void add_column(ActionEvent event) {
       Dialog.loadInputDialog(stack, 400.0, 300.0, new Text("Enter Column name"), "Add column", 200.0, 50.0, JFXDialog.DialogTransition.CENTER,this);        
    }
    @FXML
    private void add_dept(ActionEvent event) {
        Dialog.loadInputDialog(stack, 400.0, 300.0, new Text("Enter Department"), "Add Department", 200.0, 50.0, JFXDialog.DialogTransition.CENTER,this);        
    }
    @FXML
    private void delete_course(ActionEvent event) {        
        Util.notify("Course Removed",course.getValue()+" Removed" , Util.Notification.info);        
        course.getItems().remove(course.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void delete_column(ActionEvent event) {
       Util.notify("Column Removed",column.getValue()+" Removed" , Util.Notification.info);        
       column.getItems().remove(column.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void delete_dept(ActionEvent event) {           
       Util.notify("Department Removed",dept.getValue()+" Removed" , Util.Notification.info);        
       dept.getItems().remove(dept.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void issueSubmitUpdate(ActionEvent event) {
        try{
           db.execute("update setting set srenew_day='"+student_renew_days.getText()+"'");
           db.execute("update setting set trenew_day='"+teacher_renew_days.getText()+"'");
           db.execute("update setting set max_sissue='"+student_max_issue.getText()+"'");
           db.execute("update setting set max_tissue='"+teacher_max_issue.getText()+"'");
           db.execute("update setting set rate='"+rules.getText()+"'");
           db.execute("update setting set email_after_issue='"+after_issue.isSelected()+"'");
           db.execute("update setting set email_after_submission='"+after_submit.isSelected()+"'");
           db.execute("update setting set email_submission_alert='"+submit_alert.isSelected()+"'");
           Util.notify("Successful", "Issue Submit details successfully Updated !", Util.Notification.info);
        }catch(Exception e){
            System.err.println(e.getMessage());   
        }
    }

    @FXML
    private void addRemoveitemsUpdate(ActionEvent event) {
           db.execute("delete from course");
           db.execute("delete from dept");
           db.execute("delete from column");
          for(String course:course.getItems())
              db.execute("insert into course(course) values('"+course+"')");
          for(String dept:dept.getItems())
              db.execute("insert into dept(dept) values('"+dept+"')");
          for(String column:column.getItems())
              db.execute("insert into column(column) values('"+column+"')");
          Util.notify("Successful", "Items successfully Updated !", Util.Notification.info);
    }

    @FXML
    private void idUpdate(ActionEvent event) {
        db.execute("update setting set idcard_title='"+id_title.getText()+"',idcard_rules='"+rules.getText()+"'");
        Util.notify("Successful", "IDcard details successfully Updated !", Util.Notification.info);
    }

    @FXML
    private void loadLogo(ActionEvent event) {
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(stack.getScene().getWindow());
        if(file2!=null){
            System.out.println(file2.getPath());
           Util.copyFile(file2.getPath(), ".\\img\\id_logo.png");
           logo.setImage(new Image(new File(".\\img\\id_logo.png").toURI().toString()));  
           Util.notify("Successful", "Logo successfully Updated !", Util.Notification.info);
        }else{
            Util.notify("Updation Failure", "Logo loding failure !", Util.Notification.error);
        }             
    }

    @FXML
    private void general_update(ActionEvent event) {
        db.execute("update setting set college_name='"+college_name.getText()+"'");
        Util.notify("Successful", "General details successfully Updated !", Util.Notification.info);
    }

    @FXML
    private void themeColorUpdate(ActionEvent event) {
    }

}
