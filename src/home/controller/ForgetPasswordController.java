/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import home.databasecontroller.Database;
import home.resource.animatefx.animation.Shake;
import home.util.Dialog;
import home.util.Email;
import home.util.Util;
import home.util.Validator;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ForgetPasswordController implements Initializable {
    public static String id="A000001";
    public static JFXButton close;
    String otp;
    String emailid;
    String username;
    public static Label statuslbl1;
    public static JFXProgressBar statusbar1;
    Database db=Database.getInstance();
    @FXML
    private StackPane stack;
    @FXML
    private JFXTabPane tabpane;
    @FXML
    private JFXComboBox<String> question;
    @FXML
    private JFXTextField answer;
    @FXML
    private JFXTextField adminpass;
    @FXML
    private Label email;
    @FXML
    private JFXTextField t1;
    @FXML
    private JFXTextField t2;
    @FXML
    private JFXTextField t3;
    @FXML
    private JFXTextField t4;
    @FXML
    private JFXButton recoverbtn;
    @FXML
    private JFXButton closebtn;
    @FXML
    private Label recovery_method3_lbl;
    @FXML
    private Label statuslbl;
    @FXML
    private JFXProgressBar statusbar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        close=closebtn;
        statusbar1=statusbar;
        statuslbl1=statuslbl;
                
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Selecting Security questions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        try{
             ResultSet rs=db.executeQuery("select que from security_question sq inner join "
                    + "security_answer sa on sq.id=sa.queid where sa.userid='"+id+"'");
        while(rs.next()){
           question.getItems().add(rs.getString(1));
        }
        }catch(Exception ex){System.err.println("Exception in select secuttiy question :"+ex.getMessage());}        
        
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Adding listeners to OTP textfield ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        t1.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(t1.getText().length()==1)t2.requestFocus();           
             if(t1.getText().length()>1)t1.setText(oldvalue);
        });
        t2.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(t2.getText().length()==1)t3.requestFocus(); 
             else if(oldvalue.length()>newvalue.length())t1.requestFocus();                                              
             if(t2.getText().length()>1)t2.setText(oldvalue);
        });
        t3.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(t3.getText().length()==1)t4.requestFocus(); 
             else if(oldvalue.length()>newvalue.length())t2.requestFocus();                                              
             if(t3.getText().length()>1)t3.setText(oldvalue);
        });
        t4.textProperty().addListener((observable,oldvalue,newvalue)->{
             if(t4.getText().length()==1)recoverbtn.requestFocus(); 
             else if(oldvalue.length()>newvalue.length())t3.requestFocus();                                              
             if(t4.getText().length()>1)t4.setText(oldvalue);
        });       
        
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Selecting email ID ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        if(id.startsWith("U")){
            emailid=(String)Util.getRowData("user", "email", 2, "userid", id);
            username=(String)Util.getRowData("user", "username", 2, "userid", id);
            email.setText(emailid);
        }else{
            emailid=(String)Util.getFirstRowData("admin", "email", 2);
            username=(String)Util.getFirstRowData("admin", "name", 2);
            email.setText(emailid);
            recovery_method3_lbl.setText("Recovery Method 2");
        }
    }    

    @FXML
    private void sendOTP(ActionEvent event) {
        Random r=new Random();
        int n=r.nextInt(9999);
        while(n<999)n*=10;
        otp=""+n;
        Email.sendMail(emailid, "OTP verification","Hi "+username+",\n\nGreetings of the day.\n\nYou have requested to password recovery for "+username+" account.\n\n"
                + "so the OTP for account verification is : "+otp
                +"\n\nThanks, Have a nice day !\n\nRegards,\nLibrarian"
        );
        statuslbl1.setText("Sending Email . . .");        
        statuslbl1.setVisible(true);        
        statusbar1.setVisible(true);  

    }

    @FXML
    private void recoverPassword(ActionEvent event) {
        if(tabpane.getSelectionModel().getSelectedIndex()==0){
            if(Validator.checkRequiredValidator(question) && 
               Validator.checkRequiredValidator(answer)){
                String que=question.getSelectionModel().getSelectedItem();
               try{
                  ResultSet rs=db.executeQuery("select ans from security_answer where "
                          + "userid='"+id+"' and queid=(select id from security_question where "
                                  + "que='"+que+"')");
                  rs.next();
                  if(rs.getString(1).equals(answer.getText())){
                      showPassword();
                  }else{
                      answer.clear();
                      new Shake(answer).play();
                  }
               }catch(Exception ex){System.err.println("Exception in checking secuttiy answer :"+ex.getMessage());} 
            }
        }else if(tabpane.getSelectionModel().getSelectedIndex()==1){
            if(Validator.checkRequiredValidator(adminpass)){
                String pass=(String)Util.getFirstRowData("admin", "password", 2);
                pass=Util.decryptData(pass);
                  if(pass.equals(adminpass.getText())){
                      showPassword();
                  }else{
                      adminpass.clear();
                      new Shake(adminpass).play();
                  }
            }
        }else if(tabpane.getSelectionModel().getSelectedIndex()==2 ){
            if(Validator.checkRequiredValidator(t1,t2,t3,t4)){
                String pass=t1.getText()+t2.getText()+t3.getText()+t4.getText();
                  if(pass.equals(otp)){
                      showPassword();
                  }else{
                      t1.clear();t2.clear();t3.clear();t4.clear();                      
                      new Shake(t1).play();
                      new Shake(t2).play();
                      new Shake(t3).play();
                      new Shake(t4).play();
                      t1.requestFocus();
                  }
            }
        }
    }
    public void showPassword(){
        String pass="";
        if(id.startsWith("U"))
          pass=(String)Util.getRowData("user", "password", 2, "userid", id);
        else
          pass=(String)Util.getFirstRowData("admin", "password", 2);  
        pass=Util.decryptData(pass);
        Dialog.loadDialog(stack, new Text("Your Password :"), new Text("Password recovery successful.\n\nYour password is : "+pass), "ok", 100.0, 40.0, JFXDialog.DialogTransition.CENTER);
    }

    @FXML
    private void left(ActionEvent event) {
        int idx=tabpane.getSelectionModel().getSelectedIndex();
        if(id.startsWith("U")){
            if((idx-1) >= 0  && --idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to --idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression --idx 
            tabpane.getSelectionModel().select(idx);
        }else tabpane.getSelectionModel().select(0);        
    }

    @FXML
    private void right(ActionEvent event) {
        int idx=tabpane.getSelectionModel().getSelectedIndex();
        if(id.startsWith("U")){
            if((idx+1) <= 2  && ++idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to ++idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression ++idx 
            tabpane.getSelectionModel().select(idx);
        }else tabpane.getSelectionModel().select(2);        
    }
    
}
