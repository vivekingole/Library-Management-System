package home.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import home.databasecontroller.Database;
import home.fxml.model.PermissionController;
import home.resource.animatefx.animation.FadeInRight;
import home.resource.animatefx.animation.FadeOutRight;
import home.resource.animatefx.animation.SlideInLeft;
import home.resource.animatefx.animation.SlideOutLeft;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 *
 * @addr sangolacollege
 */
public class EditUserController implements Initializable {
    public static EditUserController obj;
    int flag=0;
    JFXTreeTableView<EditUser> treeview;
    JFXDialog dialog;  
    StackPane pane;
    public static StackPane stackpane;
    ObservableList<EditUser> list= FXCollections.observableArrayList();
    Database db=Database.getInstance();
    @FXML
    private AnchorPane anchor;
   @FXML
    private AnchorPane anchor1;
   @FXML
    private Label size;
     @FXML
    private JFXTextField searchtf;    
    @FXML
    private Label searchlbl;
    @FXML
    private StackPane stack;
    @FXML
    private JFXComboBox<String> editpassword;

    @FXML
    private JFXButton editphoto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obj=this;
      stackpane=stack;
      searchtf.setOpacity(0);
      searchtf.focusedProperty().addListener(event->{
      if(!searchtf.isFocused())closeTF();
        });
     initTable();        
    }  
    
    public void loadData(){
        try{            
            list.clear();
            ResultSet rs=db.executeQuery("select * from user");            
            while(rs.next()){
                list.add(new EditUser(rs.getString("userid"),
                                       rs.getString("username"),
                                       rs.getString("password"),
                                       rs.getString("addr"),
                                       rs.getString("phone"),
                                       rs.getString("email")        
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    public void initTable(){
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Making column ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        JFXTreeTableColumn<EditUser, String> idcol = new JFXTreeTableColumn<>("id");
        idcol.setPrefWidth(150);
        idcol.setCellValueFactory((param) ->{
        if(idcol.validateValue(param)) return param.getValue().getValue().id;
        else return idcol.getComputedValue(param);
        });
                        
        JFXTreeTableColumn<EditUser, String> usernamecol = new JFXTreeTableColumn<>("username");
        usernamecol.setPrefWidth(150);
        usernamecol.setCellValueFactory((param) ->{
        if(usernamecol.validateValue(param)) return param.getValue().getValue().username;
        else return usernamecol.getComputedValue(param);
        });

        JFXTreeTableColumn<EditUser, String> passwordcol = new JFXTreeTableColumn<>("password");
        passwordcol.setPrefWidth(150);
        passwordcol.setCellValueFactory((param) ->{
        if(passwordcol.validateValue(param)) return param.getValue().getValue().password;
        else return passwordcol.getComputedValue(param);
        });

        JFXTreeTableColumn<EditUser, String> addrcol = new JFXTreeTableColumn<>("addr");
        addrcol.setPrefWidth(150);
        addrcol.setCellValueFactory((param) ->{
        if(addrcol.validateValue(param)) return param.getValue().getValue().addr;
        else return addrcol.getComputedValue(param);
        });
 
          JFXTreeTableColumn<EditUser, String> phonecol = new JFXTreeTableColumn<>("phone");
        phonecol.setPrefWidth(150);
        phonecol.setCellValueFactory((param) ->{
        if(phonecol.validateValue(param)) return param.getValue().getValue().phone;
        else return phonecol.getComputedValue(param);
        });
        
          JFXTreeTableColumn<EditUser, String> emailcol = new JFXTreeTableColumn<>("email");
        emailcol.setPrefWidth(150);
        emailcol.setCellValueFactory((param) ->{
        if(emailcol.validateValue(param)) return param.getValue().getValue().email;
        else return emailcol.getComputedValue(param);
        });
              
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Making cell editable ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
        usernamecol.setCellFactory(param -> new GenericEditableTreeTableCell<EditUser,String>(new TextFieldEditorBuilder()));
        usernamecol.setOnEditCommit((t)->{
        ((EditUser) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).username.set(t.getNewValue());
           EditUser u=treeview.getSelectionModel().getSelectedItem().getValue();
           if(Util.update("user", "username", t.getNewValue(), "userid", u.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","Username Updated to "+t.getNewValue(),Util.Notification.success);  
            MainController.user_name_.setText(t.getNewValue());            
            loadData();            
           } 
        });
 
        passwordcol.setCellFactory(param -> new GenericEditableTreeTableCell<EditUser,String>(new TextFieldEditorBuilder()));
        passwordcol.setOnEditCommit((t)->{
        ((EditUser) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).password.set(t.getNewValue());
           EditUser u=treeview.getSelectionModel().getSelectedItem().getValue();
           if(Util.update("user", "password", t.getNewValue(), "userid", u.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","User password updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
           } 
        });
 
        addrcol.setCellFactory(param -> new GenericEditableTreeTableCell<EditUser,String>(new TextFieldEditorBuilder()));
        addrcol.setOnEditCommit((t)->{
        ((EditUser) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).addr.set(t.getNewValue());
           EditUser u=treeview.getSelectionModel().getSelectedItem().getValue();
           if(Util.update("user", "addr", t.getNewValue(), "userid", u.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","User address updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
           } 
        });
 
        phonecol.setCellFactory(param -> new GenericEditableTreeTableCell<EditUser,String>(new TextFieldEditorBuilder()));
        phonecol.setOnEditCommit((t)->{
        ((EditUser) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).phone.set(t.getNewValue());
           EditUser u=treeview.getSelectionModel().getSelectedItem().getValue();
           if(Util.update("user", "phone", t.getNewValue(), "userid", u.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","User phone updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
           } 
        });
        
        emailcol.setCellFactory(param -> new GenericEditableTreeTableCell<EditUser,String>(new TextFieldEditorBuilder()));
        emailcol.setOnEditCommit((t)->{
        ((EditUser) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).email.set(t.getNewValue());
           EditUser u=treeview.getSelectionModel().getSelectedItem().getValue();
           if(Util.update("user", "email", t.getNewValue(), "userid", u.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","User email updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
           } 
        });
 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ load data ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
     
       loadData();  

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~ Building table ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
       final TreeItem<EditUser> root = new RecursiveTreeItem<EditUser>(list, RecursiveTreeObject::getChildren);         
        treeview = new JFXTreeTableView<EditUser>(root);    
        treeview.setShowRoot(false);
        treeview.setEditable(true);
        treeview.getColumns().setAll(idcol, usernamecol,passwordcol,addrcol,phonecol,emailcol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeview.setPredicate(user -> user.getValue().id.get().contains(newVal)
                || user.getValue().username.get().contains(newVal)
                || user.getValue().phone.get().contains(newVal)
                || user.getValue().addr.get().contains(newVal)
                || user.getValue().password.get().contains(newVal)
                || user.getValue().email.get().contains(newVal));               
        });
 
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeview.getCurrentItemsCount(),
        treeview.currentItemsCountProperty()));

                treeview.setPrefSize(976,551);
               // addrcol.setPrefWidth(anchor1.getPrefWidth()/3);
             //   idcol.setPrefWidth(anchor1.getPrefWidth()/3);
             //   usernamecol.setPrefWidth(anchor1.getPrefWidth()/3);
               
                anchor1.getChildren().setAll(treeview);                                 
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
    @FXML
    void permission_edit(ActionEvent event) {
        try {       
            PermissionController.id=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();                    
            Parent root=FXMLLoader.load((getClass().getResource("/home/fxml/model/Permission.fxml")));
            Scene scene=new Scene(root);                   
            Stage stage=new Stage();
            stage.setScene(scene);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EditUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void delete(ActionEvent event) {
        String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
        if(db.execute("delete from user where userid='"+selectedrowid+"'") &&
           db.execute("delete from user_permission where id='"+selectedrowid+"'")){
             Util.notify("Removation Successful", selectedrowid+" User Removed", Util.Notification.success); 
                    loadData();
                    ViewUserController.obj.loadData();
                }
    }
    @FXML
    void photoEdit(ActionEvent event) {
        String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(anchor.getScene().getWindow());
        if(file2!=null){
            Util.copyFile(file2.getAbsolutePath(),".\\img\\user\\img\\"+selectedrowid+".jpg" );
            Util.notify("Updation Successful", "Image Updated", Util.Notification.success);       
        }      
    }
    @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}
class EditUser extends RecursiveTreeObject<EditUser>{
    StringProperty id;
    StringProperty username;    
    StringProperty password;    
    StringProperty addr;
    StringProperty phone;
    StringProperty email;
    
    public EditUser(String id, String username,String password, String addr,String phone,String email) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.addr = new SimpleStringProperty(addr) ;
        this.phone = new SimpleStringProperty(phone) ;        
        this.email = new SimpleStringProperty(email) ;               
    }
     
}
