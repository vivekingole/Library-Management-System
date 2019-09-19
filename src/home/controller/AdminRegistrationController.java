package home.controller;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import home.databasecontroller.Database;
import home.model.QuestionData;
import home.util.Util;
import home.util.Validator;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AdminRegistrationController implements Initializable {

     ArrayList<QuestionData> quelist=new ArrayList<>(); 
    String path1,path2;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXPasswordField pass1;
    @FXML
    private JFXPasswordField pass2;
    @FXML
    private StackPane sp;
     Database db=Database.getInstance();
    @FXML
    private Circle img;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXComboBox<String> question;
    @FXML
    private JFXTextField answer;
    @FXML
    private JFXTextField email;
    @FXML
    private AnchorPane anchor;
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        path1=".\\img\\user.png";
        img.setFill(new ImagePattern(new Image(new File(path1).toURI().toString())));  
        question.getItems().setAll(Util.getColumn("security_question", "que"));
    }    

    @FXML
    private void register(ActionEvent event) {
        try{
        String username=user.getText();
        String password1=pass1.getText();
        String password2=pass2.getText();
        System.out.println("value of string "+username);
        if(!Validator.checkRequiredValidator(user,email,phone)){
             JOptionPane.showMessageDialog(null,"Invalid", "Please fill all fields !"
                    + "", JOptionPane.WARNING_MESSAGE);               
        }
        else{
            if(pass1.getText().equals(pass2.getText())){
                path2="img\\admin\\admin.jpg";
                new Util().copyFile(path1,path2);
                
            db.execute("insert into admin(id,name,password,email,phone) values('A000001',"
                   + "'"+user.getText()+"',"
                   + "'"+Util.encryptData(pass1.getText())+"',"                  
                   + "'"+email.getText()+"',"
                   + "'"+phone.getText()+"'"
                   + ")");
            for(QuestionData q:quelist)
             db.execute("insert into security_answer(userid,queid,ans) values('"+q.userid+"','"+q.queid+"','"+q.ans+"')");            
                StartingTabController.registertab.getTabPane().getSelectionModel().select(1);
                Database.registration=true;
           }
            else{
                 JOptionPane.showMessageDialog(null,"Invalid", "Password does not matched."
                        + "", JOptionPane.WARNING_MESSAGE);               
            } 
        }
        }catch(Exception ex){    }
    }

    @FXML
    private void openimg(MouseEvent event) {                
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(anchor.getScene().getWindow());
        if(file2!=null){
            path1=file2.getAbsolutePath();
            img.setFill(new ImagePattern(new Image(file2.toURI().toString())));                   
        }              
    }

    @FXML
    private void addQuestion(ActionEvent event) {       
       int index=question.getSelectionModel().getSelectedIndex()+1;
       String qid="q"+index;
       quelist.add(new QuestionData("A000001",qid,answer.getText()));
       question.getItems().remove(index-1);
       question.getSelectionModel().clearSelection();
       answer.clear();
       Util.notify("question Added", "Question q"+index+" added", Util.Notification.success);
    }

    @FXML
    private void clear_question(ActionEvent event) {
         question.getItems().setAll(Util.getColumn("security_question", "que"));
         quelist.clear();
         answer.clear();
    }
    
}
