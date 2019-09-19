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
import home.model.Book;
import home.util.Dialog;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
/**
 *
 * @author sangolacollege
 */
public class ViewBookController implements Initializable {
    public static ViewBookController obj;
    JFXTreeTableView<Book> treeView;
    int flag=0;
    ProfileController pc;
    public static Database db=Database.getInstance();
    ResultSet rs;
    public ObservableList<Book> list=FXCollections.observableArrayList();;
    @FXML
    private StackPane sp;
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
     initTable();                   
     }  
    @FXML
    void view(ActionEvent event) {             
       Book b=treeView.getSelectionModel().getSelectedItem().getValue();
       rs=db.executeQuery("select * from book where id='"+b.id.getValue()+"'");
        try {
            rs.next();    
/////////prepare column 1
        Node []node1={
                new Label(" ID              "),
                new Label(" Name            "),
                new Label(" Author          "),
                new Label(" Price           "),
                new Label(" Entry Date      "),
                new Label(" Entry time      "),
                new Label(" Availability    "),                
                new Label(" Copies          "),                
                new Label(" Column details  ") 
        };
//////////////prepare column 2
        Node []node2={
                new Label(":  "+rs.getString("id")),
                new Label(":  "+rs.getString("name")),
                new Label(":  "+rs.getString("author")),
                new Label(":  "+rs.getInt("price")),
                new Label(":  "+rs.getString("date")),
                new Label(":  "+rs.getString("time")),
                new Label(":  "+b.available.getValue()),                
                new Label(":  "+Util.searchCount("book", "photo",rs.getString("photo"))),                
                new Label(":  "+rs.getString("column"))
        };                                      

            ProfileController.setValues(new Image(new File("img\\book\\img\\"+rs.getString("photo")+".jpg").toURI().toString()),
                rs.getString("name"),
                rs.getString("author"),
                node1,
                node2);
            
         /*   Parent root=FXMLLoader.load((getClass().getResource("/home/fxml/Profile.fxml")));
            Scene scene=new Scene(root);                   
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();           
           */
          VBox pane=FXMLLoader.load(getClass().getResource("/home/fxml/Profile.fxml"));            
          Dialog.loadDialogPane1(sp, pane, ProfileController.close, JFXDialog.DialogTransition.CENTER);
        } catch (SQLException ex) {
            Logger.getLogger(ViewBookController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void initTable(){
        
        JFXTreeTableColumn<Book, String> authorcol = new JFXTreeTableColumn<>("id");
        authorcol.setPrefWidth(150);
        authorcol.setCellValueFactory(( param) ->{
        if(authorcol.validateValue(param)) return param.getValue().getValue().id;
        else return authorcol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<Book, String> namecol = new JFXTreeTableColumn<>("name");
        namecol.setPrefWidth(150);
        namecol.setCellValueFactory(( param) ->{
        if(namecol.validateValue(param)) return param.getValue().getValue().name;
        else return namecol.getComputedValue(param);
        });

        JFXTreeTableColumn<Book, String> idcol = new JFXTreeTableColumn<>("author");
        idcol.setPrefWidth(150);
        idcol.setCellValueFactory(( param) ->{
        if(idcol.validateValue(param)) return param.getValue().getValue().author;
        else return idcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<Book, String> columncol = new JFXTreeTableColumn<>("column");
        columncol.setPrefWidth(150);
         columncol.setCellValueFactory(( param) ->{
        if(columncol.validateValue(param)) return param.getValue().getValue().column;
        else return columncol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<Book, String> availablecol = new JFXTreeTableColumn<>("available");
        availablecol.setPrefWidth(150);
         availablecol.setCellValueFactory(( param) ->{
        if(availablecol.validateValue(param)) return param.getValue().getValue().available;
        else return availablecol.getComputedValue(param);
        });
// Adding data
        //ObservableList<Book> books = 
        loadData();
// build tree
        final TreeItem<Book> root = new RecursiveTreeItem<Book>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<Book>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(authorcol,namecol,idcol,columncol,availablecol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().id.get().contains(newVal)
                || user.getValue().name.get().contains(newVal)
                || user.getValue().column.get().contains(newVal)
                || user.getValue().author.get().contains(newVal));
        });
 
//Label size = new Label();
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeView.getCurrentItemsCount(),
               treeView.currentItemsCountProperty()));

               // treeView.setPrefSize(anchor1.getPrefWidth(), anchor1.getPrefHeight());
                treeView.setPrefSize(976,551);
                idcol.setPrefWidth(anchor.getPrefWidth()/5);
                namecol.setPrefWidth(anchor.getPrefWidth()/5);                
                authorcol.setPrefWidth(anchor.getPrefWidth()/5);
                columncol.setPrefWidth(anchor.getPrefWidth()/5);
                availablecol.setPrefWidth(anchor.getPrefWidth()/5);
               
                anchor1.getChildren().setAll(treeView);                  
               // list=books;    
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
        new home.resource.animatefx.animation.SlideOutLeft(searchlbl).play();
        new home.resource.animatefx.animation.FadeInRight(searchtf).play();
        flag=1;        
        }        
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public void loadData(){
        list.clear();
        rs=db.executeQuery("select * from book");
        try {
            while(rs.next()){
             list.add(new Book(rs.getString("id"),rs.getString("name"),rs.getString("author"),rs.getString("column"),rs.getString("available")));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
}
