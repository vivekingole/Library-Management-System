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
import home.model.EditBook;
import home.resource.animatefx.animation.FadeInRight;
import home.resource.animatefx.animation.FadeOutRight;
import home.resource.animatefx.animation.SlideInLeft;
import home.resource.animatefx.animation.SlideOutLeft;
import home.util.Dialog;
import home.util.Util;
import java.io.File;
import java.sql.ResultSet;
import javafx.beans.binding.Bindings;
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
 * @author sangolacollege
 */
public class EditBookController implements Initializable {
    public static EditBookController obj;
    int flag=0;
    JFXTreeTableView<EditBook> treeview;
    JFXDialog dialog;
    ObservableList<EditBook> list=FXCollections.observableArrayList();
    int editcolumnflag=0;
    Database db=Database.getInstance();
    JFXComboBox<String> editcolumn=new JFXComboBox<String>();
    @FXML
    private StackPane stack;
    @FXML
    private AnchorPane anchor;
   @FXML
    private AnchorPane anchor1;
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
         //~~~~~~~~~~~~making dialog box~~~~~~~~~~~~~~~~//
               
            editcolumn.setPrefSize(200,50);           
            JFXButton set=new JFXButton(" Update Column ");                       
            set.setPrefSize(150, 50);
            VBox vb=new VBox(editcolumn);
            vb.setPadding(new Insets(10, 0, 0, 50));
            vb.setPrefSize(250, 80);
            JFXDialogLayout layout=new JFXDialogLayout();
             layout.setHeading(new Text("Select Column"));
             layout.setBody(vb);   
             layout.setPrefSize(400,200);
             layout.setPadding(new Insets(20));
             dialog=new JFXDialog(stack, layout,JFXDialog.DialogTransition.TOP);             
             set.setOnAction(e->{
                    String newval=editcolumn.getSelectionModel().getSelectedItem();
                    if(Util.update("book", "column", newval, "id", treeview.getSelectionModel()
                    .getSelectedItem().getValue().id.getValue(), 2)==true){
                        treeview.getSelectionModel().getSelectedItem().getValue().column.set(newval);        
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
            editcolumn.getItems().setAll(Util.getColumn("column", "column"));
            list.clear();
            ResultSet rs=db.executeQuery("select * from book");            
            while(rs.next()){
                list.add(new EditBook(rs.getString("id"),
                                       rs.getString("name"),
                                       rs.getString("author"),
                                       rs.getString("price"),
                                       rs.getString("column"),
                                       rs.getString("photo")
                ));
            }
        } catch (Exception ex) {
            System.out.println("####Exception at load data");
          //  Logger.getLogger(EditBookController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    public void initTable(){             
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ creating column ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
          JFXTreeTableColumn<EditBook, String> idcol = new JFXTreeTableColumn<>("id");
        idcol.setPrefWidth(150);
        idcol.setCellValueFactory((param) ->{
        if(idcol.validateValue(param)) return param.getValue().getValue().id;
        else return idcol.getComputedValue(param);
        });
        
                
        JFXTreeTableColumn<EditBook, String> namecol = new JFXTreeTableColumn<>("name");
        namecol.setPrefWidth(150);
        namecol.setCellValueFactory((param) ->{
        if(namecol.validateValue(param)) return param.getValue().getValue().name;
        else return namecol.getComputedValue(param);
        });

        JFXTreeTableColumn<EditBook, String> authorcol = new JFXTreeTableColumn<>("author");
        authorcol.setPrefWidth(150);
        authorcol.setCellValueFactory((param) ->{
        if(authorcol.validateValue(param)) return param.getValue().getValue().author;
        else return authorcol.getComputedValue(param);
        });
 
          JFXTreeTableColumn<EditBook, String> pricecol = new JFXTreeTableColumn<>("price");
        pricecol.setPrefWidth(150);
        pricecol.setCellValueFactory((param) ->{
        if(pricecol.validateValue(param)) return param.getValue().getValue().price;
        else return pricecol.getComputedValue(param);
        });
        
          JFXTreeTableColumn<EditBook, String> columncol = new JFXTreeTableColumn<>("column");
        columncol.setPrefWidth(150);
        columncol.setCellValueFactory((param) ->{
        if(columncol.validateValue(param)) return param.getValue().getValue().column;
        else return columncol.getComputedValue(param);
        });                     

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Making cell Editable ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        namecol.setCellFactory(param -> new GenericEditableTreeTableCell<EditBook,String>(new TextFieldEditorBuilder()));
        namecol.setOnEditCommit(t->{            
        ((EditBook) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).name.set(t.getNewValue());
         EditBook b=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("book", "name", t.getNewValue(), "photo", b.photo.getValue(), 2)==true){
           Util.notify("Updatation Successful","Book name Updated to "+t.getNewValue(),Util.Notification.success);    
           loadData();
         }
        });
 
        authorcol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditBook,String>(new TextFieldEditorBuilder()));
        authorcol.setOnEditCommit((t)->{
        ((EditBook) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).author.set(t.getNewValue());
         EditBook b=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("book", "author", t.getNewValue(), "photo", b.photo.getValue(), 2)==true){
           Util.notify("Updation Successful","Book author Updated to "+t.getNewValue(),Util.Notification.success);    
           loadData();
         }
        });
 
        pricecol.setCellFactory((param) -> new GenericEditableTreeTableCell<EditBook,String>(new TextFieldEditorBuilder()));
        pricecol.setOnEditCommit((t)->{
        ((EditBook) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).price.set(t.getNewValue());
         EditBook b=treeview.getSelectionModel().getSelectedItem().getValue();
         if(Util.update("book", "price", t.getNewValue(), "photo", b.photo.getValue(), 2)==true){
           Util.notify("Updation Successful","Book price Updated to "+t.getNewValue(),Util.Notification.success);    
           loadData();
         }        
        });
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Loading data ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        loadData();
        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ building table ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
        final TreeItem<EditBook> root = new RecursiveTreeItem<EditBook>(list, RecursiveTreeObject::getChildren);         
        treeview = new JFXTreeTableView<EditBook>(root);
        treeview.setShowRoot(false);
        treeview.setEditable(true);
        treeview.getColumns().setAll(idcol, authorcol, namecol,pricecol,columncol); 
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeview.setPredicate(user -> user.getValue().id.get().contains(newVal)
                || user.getValue().author.get().contains(newVal)
                || user.getValue().name.get().contains(newVal)
                || user.getValue().price.get().contains(newVal)
                || user.getValue().column.get().contains(newVal));               
        });
 
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeview.getCurrentItemsCount(),
        treeview.currentItemsCountProperty()));

                treeview.setPrefSize(976,551);
                authorcol.setPrefWidth(anchor1.getPrefWidth()/5);
                idcol.setPrefWidth(anchor1.getPrefWidth()/5);
                namecol.setPrefWidth(anchor1.getPrefWidth()/5); 
                columncol.setPrefWidth(anchor1.getPrefWidth()/5); 
                pricecol.setPrefWidth(anchor1.getPrefWidth()/5); 
                
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
    void columnEdit(ActionEvent event) {
        try{
            if(!treeview.getSelectionModel().isEmpty()){
            editcolumn.getSelectionModel().selectFirst();
            dialog.show();      
            }else{
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }
        }catch(Exception e){ 
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }  
    }
    @FXML
    void delete(ActionEvent event) {
        try{
        if(!treeview.getSelectionModel().isEmpty()){         
                String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
                if(!Util.checkRow("issue_book", "bid",selectedrowid)){
                if(db.execute("delete from book where id='"+selectedrowid+"'")){
                Util.notify("Removation Successful", "Book Removed", Util.Notification.success); 
                loadData();
                }
                }else{
                    String issuedid=(String)Util.getRowData("issue_book", "mid", 2, "bid", selectedrowid);
                    Dialog.loadDialog(stack, new Text("Book Removation fails"),new Text("This book is already issued to member \" "+issuedid+" \""), "Ok", 200.0, 50.0, JFXDialog.DialogTransition.CENTER);
                }      
        }else{
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }
        }catch(Exception e){ 
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }  
    }
    @FXML
    void photoEdit(ActionEvent event) {
         try{
        if(!treeview.getSelectionModel().isEmpty()){         
            String selectedrowid=treeview.getSelectionModel().getSelectedItem().getValue().id.getValue();
            String id=(String)Util.getRowData("book", "photo", 2, "id", selectedrowid);
            FileChooser fc=new FileChooser();
            File file2=fc.showOpenDialog(anchor.getScene().getWindow());
            if(file2!=null){
                Util.copyFile(file2.getAbsolutePath(),".\\img\\book\\img\\"+id+".jpg" );
                Util.notify("Updation Successful", "Image Updated", Util.Notification.success);       
            }       
        }else{
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }
        }catch(Exception e){ 
             Util.notify("Invalid Operation","First Select the row",Util.Notification.error);                
        }          
    }
    @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}
