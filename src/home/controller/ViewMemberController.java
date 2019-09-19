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
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import static home.controller.ViewBookController.db;
import home.databasecontroller.Database;
import home.model.Book;
import home.util.Dialog;
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
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 *
 * @course sangolacollege
 */
public class ViewMemberController implements Initializable {
    public static ViewMemberController obj;
    JFXTreeTableView<Member> treeView;
    int flag=0;
    ProfileController pc;
    Database db=Database.getInstance();
    ResultSet rs;
    ObservableList<Member> list=FXCollections.observableArrayList();
    @FXML
    private StackPane sp;
    @FXML
    private AnchorPane anchor;
    @FXML
    private AnchorPane outer_anchor;
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
     /*outer_anchor.widthProperty().addListener(listener->{
         System.out.println(outer_anchor.getWidth());
        anchor.setPrefWidth(outer_anchor.getWidth());
        treeView.setPrefWidth(outer_anchor.getWidth());
     });*/
     }  
    @FXML
    void view(ActionEvent event) {             
       Member m=treeView.getSelectionModel().getSelectedItem().getValue();
       rs=db.executeQuery("select * from member where id='"+m.id.getValue()+"'");
        try {
            rs.next();    
/////////prepare node 1
        Node []node1={
                new Label(" ID              "),
                new Label(" Name            "),
                new Label(" Course/dept     "),
                new Label(" Address         "),
                new Label(" Phone           "),
                new Label(" Email           "),
                new Label(" Entry date      "),                
                new Label(" Entry time      ")                
        };
//////////////prepare node 2
        Node []node2={
                new Label(":  "+m.id.getValue()),
                new Label(":  "+m.name.getValue()),
                new Label(":  "+m.course.getValue()),
                new Label(":  "+rs.getString("addr")),
                new Label(":  "+m.phone.getValue()),
                new Label(":  "+m.email.getValue()),                
                new Label(":  "+rs.getString("date")),
                new Label(":  "+rs.getString("time"))
        };                                      

            ProfileController.setValues(new Image(new File("img\\member\\img\\"+m.id.getValue()+".jpg").toURI().toString()),
                m.name.getValue(),
                m.email.getValue(),
                node1,
                node2);
            
          /*  Parent root=FXMLLoader.load((getClass().getResource("/home/fxml/Profile.fxml")));
            Scene scene=new Scene(root);                   
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();           
          */
           VBox pane=FXMLLoader.load(getClass().getResource("/home/fxml/Profile.fxml"));            
          Dialog.loadDialogPane1(sp, pane, ProfileController.close, JFXDialog.DialogTransition.CENTER);
            
        } catch (SQLException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void initTable(){
        
        JFXTreeTableColumn<Member, String> coursecol = new JFXTreeTableColumn<>("id");
        coursecol.setPrefWidth(150);
        coursecol.setCellValueFactory((param) ->{
        if(coursecol.validateValue(param)) return param.getValue().getValue().id;
        else return coursecol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<Member, String> namecol = new JFXTreeTableColumn<>("name");
        namecol.setPrefWidth(150);
        namecol.setCellValueFactory((param) ->{
        if(namecol.validateValue(param)) return param.getValue().getValue().name;
        else return namecol.getComputedValue(param);
        });

        JFXTreeTableColumn<Member, String> idcol = new JFXTreeTableColumn<>("course/dept");
        idcol.setPrefWidth(150);
        idcol.setCellValueFactory((param) ->{
        if(idcol.validateValue(param)) return param.getValue().getValue().course;
        else return idcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<Member, String> phonecol = new JFXTreeTableColumn<>("phone");
        phonecol.setPrefWidth(150);
         phonecol.setCellValueFactory((param) ->{
        if(phonecol.validateValue(param)) return param.getValue().getValue().phone;
        else return phonecol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<Member, String> emailcol = new JFXTreeTableColumn<>("email");
        emailcol.setPrefWidth(150);
         emailcol.setCellValueFactory((param) ->{
        if(emailcol.validateValue(param)) return param.getValue().getValue().email;
        else return emailcol.getComputedValue(param);
        });
// Adding data
       // ObservableList<Member> members = FXCollections.observableArrayList();
        loadData();
// build tree
        final TreeItem<Member> root = new RecursiveTreeItem<Member>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<Member>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(coursecol,namecol,idcol,phonecol,emailcol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().id.get().contains(newVal)
                || user.getValue().name.get().contains(newVal)
                || user.getValue().course.get().contains(newVal)
                || user.getValue().email.get().contains(newVal)
                || user.getValue().phone.get().contains(newVal));
        });
 
//Label size = new Label();
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeView.getCurrentItemsCount(),
               treeView.currentItemsCountProperty()));

                treeView.setPrefSize(976,551);
                idcol.setPrefWidth(196);
                namecol.setPrefWidth(196);                
                coursecol.setPrefWidth(196);
                phonecol.setPrefWidth(196);
                emailcol.setPrefWidth(196);
               
                anchor.getChildren().setAll(treeView);                  
               
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
       rs=db.executeQuery("select * from member");
        try {
            while(rs.next()){
             list.add(new Member(rs.getString("id"),rs.getString("name"),rs.getString("course"),rs.getString("phone"),rs.getString("email")));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}
    
class Member extends RecursiveTreeObject<Member>{
    StringProperty id;
    StringProperty name;
    StringProperty course;
    StringProperty phone;
    StringProperty email;
    
    public Member(String id, String name, String course,String phone,String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.course = new SimpleStringProperty(course) ;
        this.phone = new SimpleStringProperty(phone) ;        
        this.email = new SimpleStringProperty(email) ;        
    }      
}