package home.controller;


import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.model.QuestionData;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
public class AddUserController implements Initializable {

    String imgpath1="/home/img/user.png",imgpath2,imgname;
    Database db=Database.getInstance();
    JFXCheckBox check[][]=new JFXCheckBox[4][];
    ArrayList<QuestionData> quelist=new ArrayList<>();    
    @FXML
    private Circle img;
    @FXML
    private JFXTextArea addr;
    @FXML
    private JFXTextField phone;
    @FXML
    private StackPane root;
    @FXML
    private JFXTextField email;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXPasswordField pass1;
    @FXML
    private JFXPasswordField pass2;
    @FXML
    private JFXCheckBox book;
    @FXML
    private JFXCheckBox badd;
    @FXML
    private JFXCheckBox bedit;
    @FXML
    private JFXCheckBox bissue;
    @FXML
    private JFXCheckBox bsubmit;
    @FXML
    private JFXCheckBox bview;
    @FXML
    private JFXCheckBox member;
    @FXML
    private JFXCheckBox madd;
    @FXML
    private JFXCheckBox medit;
    @FXML
    private JFXCheckBox mview;
    @FXML
    private JFXCheckBox user;
    @FXML
    private JFXCheckBox uadd;
    @FXML
    private JFXCheckBox uedit;
    @FXML
    private JFXCheckBox uview;
    @FXML
    private JFXCheckBox setting;
    @FXML
    private JFXCheckBox view;
    @FXML
    private JFXCheckBox all;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTimePicker time;
    @FXML
    private JFXComboBox<String> question;
    @FXML
    private JFXTextField answer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ScrollPane sp=new ScrollPane(root);
        img.setFill(new ImagePattern(new Image(imgpath1)));  
        check[0]=new JFXCheckBox[6];
        check[1]=new JFXCheckBox[4];
        check[2]=new JFXCheckBox[4];
        check[3]=new JFXCheckBox[3];
        check[0][0]=book;
        check[0][1]=badd;
        check[0][2]=bedit;
        check[0][3]=bissue;
        check[0][4]=bsubmit;
        check[0][5]=bview;
        
        check[1][0]=member;
        check[1][1]=madd;
        check[1][2]=medit;
        check[1][3]=mview;
                            
        check[2][0]=user;
        check[2][1]=uadd;
        check[2][2]=uedit;
        check[2][3]=uview;
        
        check[3][0]=setting;
        check[3][1]=view;
        check[3][2]=all;
      
        time.setValue(LocalTime.now());
        date.setValue(LocalDate.now()); 
        question.getItems().setAll(Util.getColumn("security_question", "que"));
         ResultSet rs=db.executeQuery("select max(userid) from user");
          try {
           rs.next();        
           id.setText(Util.generateCode("U",1+Util.decodeInt(rs.getString(1))));
          }  catch (NullPointerException ex) {
           id.setText("U000001");
          }
             catch (SQLException ex) {}
    }    

    @FXML
    private void reset_event(ActionEvent event) {
        try {
            StackPane pane =FXMLLoader.load(getClass().getResource("/home/fxml/AddUser.fxml"));
            root.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void add_event(ActionEvent event) {
        try{            
            db.execute("insert into user(userid,username,password,addr,phone,email,date,time) values("
                    + "'"+id.getText()+"',"
                    + "'"+username.getText()+"',"
                    + "'"+Util.encryptData(pass1.getText())+"',"
                    + "'"+addr.getText()+"',"
                    + "'"+phone.getText()+"',"
                    + "'"+email.getText()+"',"
                    + "'"+date.getValue()+"',"                   
                    + "'"+time.getValue()+"'"                   
                    + ")");
            Util.copyFile(imgpath1,"img\\user\\"+id.getText()+".jpg");        
            Util.notify("User Added", "Userid = "+id.getText()+" added", Util.Notification.success);
            
         String s="";
                   for(int i=0;i<=3;i++)
                        for(int j=0;j<check[i].length;j++){                            
                            s+= ""+check[i][j].isSelected()+"";  ///////////////////check
                            if(!(i==3 && j==2))
                                s+=",";
                        }
                   //(id,book,badd,bedit,bissue,bsubmit,bview,member,madd,medit,mview,user,uadd,"
                  //+ "uedit,uview,setting,view,all)
          db.execute("insert into user_permission values("
                    + "'"+id.getText()+"',"
                    +s
                    + ")");
          for(QuestionData q:quelist)
             db.execute("insert into security_answer(userid,queid,ans) values('"+q.userid+"','"+q.queid+"','"+q.ans+"')");
           ViewUserController.obj.loadData();
           EditUserController.obj.loadData();
            reset_event(event);
        }catch(Exception e){
            System.out.println("add member exception "+e.getMessage());
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
    private void permission(ActionEvent event) {
        if(check[3][2].isSelected()){
            for(int i=0;i<=3;i++)
                for(JFXCheckBox c:check[i])
                    c.setSelected(true);
        }
        if(check[3][1].isSelected()){
            check[0][5].setSelected(true);
            check[1][3].setSelected(true);
            check[2][3].setSelected(true);
        }
        if(check[0][0].isSelected()){
            for(int i=1;i<=5;i++)
                check[0][i].setSelected(true);
        }
        if(check[1][0].isSelected()){
            for(int i=1;i<=3;i++)
                check[1][i].setSelected(true);
        }
        if(check[2][0].isSelected()){
            for(int i=1;i<=3;i++)
                check[2][i].setSelected(true);
        }
    }

    @FXML
    private void addQuestion(ActionEvent event) {       
       int index=question.getSelectionModel().getSelectedIndex()+1;
       String qid="q"+index;
       quelist.add(new QuestionData(id.getText(),qid,answer.getText()));
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
