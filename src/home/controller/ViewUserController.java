package home.controller;

import home.resource.animatefx.animation.FadeInRight;
import home.resource.animatefx.animation.FadeOutRight;
import home.resource.animatefx.animation.SlideInLeft;
import home.resource.animatefx.animation.SlideOutLeft;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import home.databasecontroller.Database;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import home.model.User;
import home.util.Dialog;
import javafx.scene.layout.VBox;

public class ViewUserController implements Initializable {
    public static ViewUserController obj;
    JFXTreeTableView<User> treeView;
    int flag=0;
    ProfileController pc;
    Database db=Database.getInstance();
    ResultSet rs;
    ObservableList<User> list=FXCollections.observableArrayList();
    @FXML
    private StackPane sp;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXTextField searchtf;
    @FXML
    private Label searchlbl;
    @FXML
    private Label size;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obj=this;
     searchtf.setOpacity(0);
      searchtf.focusedProperty().addListener(event->{
      if(!searchtf.isFocused())closeTF();
        });
     initTable();        
     }  
    @FXML
    void view(ActionEvent event) {             
       User u=treeView.getSelectionModel().getSelectedItem().getValue();
       rs=db.executeQuery("select * from user where userid='"+u.userid.getValue()+"'");
        try {
            rs.next();    
/////////prepare phone 1
        Node []node1={
                new Label(" ID              "),
                new Label(" Name            "),
                new Label(" Address         "),
                new Label(" Phone           "),
                new Label(" Email           "),                
                new Label(" Entry Date      "),
                new Label(" Entry time      "),
                new Label(" Permissions     ") 
        };
//////////////prepare phone 2
                String str=":  ";
                for(String s:Util.getUserPermission(u.userid.getValue())){
                    str+=s+"\n   ";
                }
        Node []node2={
                new Label(":  "+u.userid.getValue()),
                new Label(":  "+u.username.getValue()),
                new Label(":  "+u.addr.getValue()),
                new Label(":  "+u.phone.getValue()),
                new Label(":  "+u.email.getValue()),                
                new Label(":  "+rs.getString("date")),
                new Label(":  "+rs.getString("time")),
                new Label(str)
                
        };                                      

            ProfileController.setValues(new Image(new File("img\\user\\"+u.userid.getValue()+".jpg").toURI().toString()),
                u.username.getValue(),
                u.email.getValue(),
                node1,
                node2);
            
           /* Parent root=FXMLLoader.load((getClass().getResource("/home/fxml/Profile.fxml")));
            Scene scene=new Scene(root);                   
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
           */
            VBox pane=FXMLLoader.load(getClass().getResource("/home/fxml/Profile.fxml"));            
          Dialog.loadDialogPane1(sp, pane, ProfileController.close, JFXDialog.DialogTransition.CENTER);
            
        } catch (SQLException ex) {
            Logger.getLogger(ViewUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void initTable(){
        
        JFXTreeTableColumn<User, String> addrcol = new JFXTreeTableColumn<>("userid");
        addrcol.setPrefWidth(150);
        addrcol.setCellValueFactory((param) ->{
        if(addrcol.validateValue(param)) return param.getValue().getValue().userid;
        else return addrcol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<User, String> usernamecol = new JFXTreeTableColumn<>("username");
        usernamecol.setPrefWidth(150);
        usernamecol.setCellValueFactory((param) ->{
        if(usernamecol.validateValue(param)) return param.getValue().getValue().username;
        else return usernamecol.getComputedValue(param);
        });

        JFXTreeTableColumn<User, String> useridcol = new JFXTreeTableColumn<>("addr");
        useridcol.setPrefWidth(150);
        useridcol.setCellValueFactory((param) ->{
        if(useridcol.validateValue(param)) return param.getValue().getValue().addr;
        else return useridcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<User, String> phonecol = new JFXTreeTableColumn<>("phone");
        phonecol.setPrefWidth(150);
         phonecol.setCellValueFactory((param) ->{
        if(phonecol.validateValue(param)) return param.getValue().getValue().phone;
        else return phonecol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<User, String> emailcol = new JFXTreeTableColumn<>("email");
        emailcol.setPrefWidth(150);
         emailcol.setCellValueFactory((param) ->{
        if(emailcol.validateValue(param)) return param.getValue().getValue().email;
        else return emailcol.getComputedValue(param);
        });
// Adding data
        //ObservableList<User> users = FXCollections.observableArrayList();
        loadData();
// build tree
        final TreeItem<User> root = new RecursiveTreeItem<User>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<User>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(addrcol,usernamecol,useridcol,phonecol,emailcol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().userid.get().contains(newVal)
                || user.getValue().username.get().contains(newVal)
                || user.getValue().phone.get().contains(newVal)
                || user.getValue().email.get().contains(newVal)
                || user.getValue().addr.get().contains(newVal));
        });
 
//Label size = new Label();
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeView.getCurrentItemsCount(),
               treeView.currentItemsCountProperty()));

                treeView.setPrefSize(976,551);
                useridcol.setPrefWidth(196);
                usernamecol.setPrefWidth(196);                
                addrcol.setPrefWidth(196);
                phonecol.setPrefWidth(196);
                emailcol.setPrefWidth(196);
               
                anchor.getChildren().setAll(treeView);                  
                //list=users;    
    }
    
     //~~~~~~~~~~~~~~~~~~~~~~~~~~ search animation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @FXML
    private void searchExit(MouseEvent event) {
        closeTF();
    }
    public void closeTF() {
        new SlideInLeft(searchlbl).play();
        new FadeOutRight(searchtf).play();
        flag=0;        
    }
    @FXML
    private void searchEnter(MouseEvent event) {
        if(flag==0){
        searchtf.requestFocus();
        searchtf.clear();
        new SlideOutLeft(searchlbl).play();
        new FadeInRight(searchtf).play();
        flag=1;        
        }        
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public void loadData(){
        list.clear();
        rs=db.executeQuery("select * from user");
        try {
            while(rs.next()){
             list.add(new User(rs.getString("userid"),rs.getString("username"),rs.getString("addr"),rs.getString("phone"),rs.getString("email")));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}



