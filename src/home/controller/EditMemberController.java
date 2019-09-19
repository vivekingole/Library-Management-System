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
import home.resource.animatefx.animation.FadeInRight;
import home.resource.animatefx.animation.FadeOutRight;
import home.resource.animatefx.animation.SlideInLeft;
import home.resource.animatefx.animation.SlideOutLeft;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
/**
 *
 * @addr sangolacollege
 */
public class EditMemberController implements Initializable {
    public static EditMemberController obj;
    int flag=0;
    JFXTreeTableView<EditMember> treeview;
    JFXDialog dialog;
    ObservableList<EditMember> list=FXCollections.observableArrayList();
    JFXComboBox<String> editcourse=new JFXComboBox<String>();
    Database db=Database.getInstance();
    @FXML
    private AnchorPane anchor;
    @FXML
    private AnchorPane anchor1;
    @FXML
    private JFXTextField searchtf;    
    @FXML
    private Label searchlbl;
    @FXML
    private StackPane stack;
    @FXML
    private Label size;   
    @FXML
    private JFXButton editphoto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obj=this;
        searchtf.setOpacity(0);
        searchtf.focusedProperty().addListener(event->{
        if(!searchtf.isFocused())closeTF();
        });
                //~~~~~~~~~~~~making dialog box~~~~~~~~~~~~~~~~//
               
            editcourse.setPrefSize(200,50);           
            JFXButton set=new JFXButton(" Update Course ");                       
            set.setPrefSize(150, 50);
            VBox vb=new VBox(editcourse);
            vb.setPadding(new Insets(10, 0, 0, 50));
            vb.setPrefSize(250, 80);
            JFXDialogLayout layout=new JFXDialogLayout();
             layout.setHeading(new Text("Select Course"));
             layout.setBody(vb);   
             layout.setPrefSize(400,200);
             layout.setPadding(new Insets(20));
             dialog=new JFXDialog(stack, layout,JFXDialog.DialogTransition.TOP);             
             set.setOnAction(e->{
                    String newval=editcourse.getSelectionModel().getSelectedItem();
                        if(Util.update("member", "course", newval, "id", treeview.getSelectionModel()
                            .getSelectedItem().getValue().id.getValue(), 2)==true){
                        treeview.getSelectionModel().getSelectedItem().getValue().course.set(newval);        
                        Util.notify("Updation Successful", "Book column updated to "+newval, Util.Notification.success);
                        }
                    dialog.close();                   
             });
             layout.setActions(set);        
             
             //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
     initTable();        
     }  
    
    public void loadData(){
        try{
            list.clear();
            ResultSet rs=db.executeQuery("select * from member");            
            while(rs.next()){
                list.add(new EditMember(rs.getString("id"),
                                       rs.getString("name"),
                                       rs.getString("course"),
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
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ making columns ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                
        JFXTreeTableColumn<EditMember, String> idcol = new JFXTreeTableColumn<>("id");
        idcol.setPrefWidth(150);
        idcol.setCellValueFactory((param) ->{
        if(idcol.validateValue(param)) return param.getValue().getValue().id;
        else return idcol.getComputedValue(param);
        });
                        
        JFXTreeTableColumn<EditMember, String> namecol = new JFXTreeTableColumn<>("name");
        namecol.setPrefWidth(150);
        namecol.setCellValueFactory((param) ->{
        if(namecol.validateValue(param)) return param.getValue().getValue().name;
        else return namecol.getComputedValue(param);
        });

        JFXTreeTableColumn<EditMember, String> coursecol = new JFXTreeTableColumn<>("course");
        coursecol.setPrefWidth(150);
        coursecol.setCellValueFactory((param) ->{
        if(coursecol.validateValue(param)) return param.getValue().getValue().course;
        else return coursecol.getComputedValue(param);
        });

        JFXTreeTableColumn<EditMember, String> addrcol = new JFXTreeTableColumn<>("addr");
        addrcol.setPrefWidth(150);
        addrcol.setCellValueFactory((param) ->{
        if(addrcol.validateValue(param)) return param.getValue().getValue().addr;
        else return addrcol.getComputedValue(param);
        });
 
          JFXTreeTableColumn<EditMember, String> phonecol = new JFXTreeTableColumn<>("phone");
        phonecol.setPrefWidth(150);
        phonecol.setCellValueFactory((param) ->{
        if(phonecol.validateValue(param)) return param.getValue().getValue().phone;
        else return phonecol.getComputedValue(param);
        });
        
          JFXTreeTableColumn<EditMember, String> emailcol = new JFXTreeTableColumn<>("email");
        emailcol.setPrefWidth(150);
        emailcol.setCellValueFactory((param) ->{
        if(emailcol.validateValue(param)) return param.getValue().getValue().email;
        else return emailcol.getComputedValue(param);
        });
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ making cell editable ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                 
        namecol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditMember,String>(new TextFieldEditorBuilder()));
        namecol.setOnEditCommit((t)->{
        ((EditMember) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).name.set(t.getNewValue());
         EditMember m=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("member", "name", t.getNewValue(), "id", m.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","Member name Updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
         } 
        });
 
        addrcol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditMember,String>(new TextFieldEditorBuilder()));
        addrcol.setOnEditCommit((t)->{
        ((EditMember) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).addr.set(t.getNewValue());
         EditMember m=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("member", "addr", t.getNewValue(), "id", m.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","Member address Updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
         } 
        });
 
        phonecol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditMember,String>(new TextFieldEditorBuilder()));
        phonecol.setOnEditCommit((t)->{
        ((EditMember) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).phone.set(t.getNewValue());
         EditMember m=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("member", "phone", t.getNewValue(), "id", m.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","Member phone Updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
         } 
        });
        
        emailcol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditMember,String>(new TextFieldEditorBuilder()));
        emailcol.setOnEditCommit((t)->{
        ((EditMember) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).phone.set(t.getNewValue());
         EditMember m=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("member", "email", t.getNewValue(), "id", m.id.getValue(), 2)==true){
            Util.notify("Updatation Successful","Member email Updated to "+t.getNewValue(),Util.Notification.success);    
            loadData();            
         } 
        });
 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Loading table ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
 
       loadData();
       
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Building table ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

       final TreeItem<EditMember> root = new RecursiveTreeItem<EditMember>(list, RecursiveTreeObject::getChildren);
        treeview = new JFXTreeTableView<EditMember>(root);    
        treeview.setShowRoot(false);
        treeview.setEditable(true);
        treeview.getColumns().setAll(idcol, addrcol, namecol ,coursecol,phonecol,emailcol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeview.setPredicate(user -> user.getValue().id.get().contains(newVal)
                || user.getValue().addr.get().contains(newVal)
                || user.getValue().name.get().contains(newVal)
                || user.getValue().course.get().contains(newVal)
                || user.getValue().phone.get().contains(newVal)
                || user.getValue().email.get().contains(newVal));               
        });
 
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeview.getCurrentItemsCount(),
        treeview.currentItemsCountProperty()));

                treeview.setPrefSize(976,551);
                addrcol.setPrefWidth(anchor1.getPrefWidth()/6);
                coursecol.setPrefWidth(anchor1.getPrefWidth()/6);
                emailcol.setPrefWidth(anchor1.getPrefWidth()/6);
                idcol.setPrefWidth(anchor1.getPrefWidth()/6);
                namecol.setPrefWidth(anchor1.getPrefWidth()/6);
                phonecol.setPrefWidth(anchor1.getPrefWidth()/6);
               
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
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @FXML
    void course_edit(ActionEvent event) {
            String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();            
            if(selectedrowid.startsWith("S")){
                editcourse.getItems().setAll(Util.getColumn("course", "course"));
            }else{
                editcourse.getItems().setAll(Util.getColumn("dept", "dept"));
            }
            editcourse.getSelectionModel().selectFirst();
            
            dialog.show();
    }
    @FXML
    void delete(ActionEvent event) {
        int fine=0;
        String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
        if(!Util.checkRow("issue_book", "mid",selectedrowid)){
            if((fine=(int)Util.getRowData("member", "fine", 1, "id", selectedrowid))==0){
                if(db.execute("delete from member where id='"+selectedrowid+"'")){
                    Util.notify("Removation Successful", selectedrowid+" Member Removed", Util.Notification.success); 
                    loadData();
                }
            }else{
                Dialog.loadDialog(stack, new Text("Member Removation fails"),new Text("This member has \" "+fine+" Rs \" remains to pay !"), "Ok", 200.0, 50.0, JFXDialog.DialogTransition.CENTER);     
            }    
        }else{
            String s[]=Util.getColumn("issue_book", "bid", "mid", selectedrowid);
            String s2="";
            for(String s1:s)s2+=s1+" ";
            String issuedid=(String)Util.getRowData("issue_book", "bid", 2, "mid", selectedrowid);
            Dialog.loadDialog(stack, new Text("Member Removation fails"),new Text("This member has already issued \" "+s2+"\" Books"), "Ok", 150.0, 40.0, JFXDialog.DialogTransition.CENTER);
        }                    
    }
    @FXML
    void photoEdit(ActionEvent event) {
        String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
        FileChooser fc=new FileChooser();
        File file2=fc.showOpenDialog(anchor.getScene().getWindow());
        if(file2!=null){
            Util.copyFile(file2.getAbsolutePath(),".\\img\\member\\img\\"+selectedrowid+".jpg" );
            Util.notify("Updation Successful", "Image Updated", Util.Notification.success);       
        }        
    }
    @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}
class EditMember extends RecursiveTreeObject<EditMember>{
    StringProperty id;
    StringProperty name;    
    StringProperty course;    
    StringProperty addr;
    StringProperty phone;
    StringProperty email;
    
    public EditMember(String id, String name,String course, String addr,String phone,String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.course = new SimpleStringProperty(course);
        this.addr = new SimpleStringProperty(addr) ;
        this.phone = new SimpleStringProperty(phone) ;        
        this.email = new SimpleStringProperty(email) ;               
    }
     
}
