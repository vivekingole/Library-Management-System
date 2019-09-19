package home.homepage;

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
import home.controller.HomePageController;
import home.controller.MainController;
import home.controller.ProfileController;
import home.databasecontroller.Database;
import home.model.TotalFineModel;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
/**
 *
 * @fine sangolacollege
 */
public class TotalFineController implements Initializable {
    public static TotalFineController obj;
    JFXTreeTableView<TotalFineModel> treeView;
    int flag=0;
    ProfileController pc;
    public static Database db=Database.getInstance();
    ResultSet rs;
    public ObservableList<TotalFineModel> list=FXCollections.observableArrayList();;
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
   
    public void initTable(){
        
        JFXTreeTableColumn<TotalFineModel, String> finecol = new JFXTreeTableColumn<>("fine");
        finecol.setPrefWidth(150);
        finecol.setCellValueFactory(( param) ->{
        if(finecol.validateValue(param)) return param.getValue().getValue().fine;
        else return finecol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<TotalFineModel, String> namecol = new JFXTreeTableColumn<>("name");
        namecol.setPrefWidth(150);
        namecol.setCellValueFactory(( param) ->{
        if(namecol.validateValue(param)) return param.getValue().getValue().name;
        else return namecol.getComputedValue(param);
        });

        JFXTreeTableColumn<TotalFineModel, String> midcol = new JFXTreeTableColumn<>("id");
        midcol.setPrefWidth(150);
        midcol.setCellValueFactory(( param) ->{
        if(midcol.validateValue(param)) return param.getValue().getValue().mid;
        else return midcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<TotalFineModel, String> phonecol = new JFXTreeTableColumn<>("phone");
        phonecol.setPrefWidth(150);
         phonecol.setCellValueFactory(( param) ->{
        if(phonecol.validateValue(param)) return param.getValue().getValue().phone;
        else return phonecol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<TotalFineModel, String> emailcol = new JFXTreeTableColumn<>("email");
        emailcol.setPrefWidth(150);
         emailcol.setCellValueFactory(( param) ->{
        if(emailcol.validateValue(param)) return param.getValue().getValue().email;
        else return emailcol.getComputedValue(param);
        });
// Adding data
        //ObservableList<TotalFineModel> books = 
        loadData();
// build tree
        final TreeItem<TotalFineModel> root = new RecursiveTreeItem<TotalFineModel>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<TotalFineModel>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(midcol,namecol,phonecol,emailcol,finecol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().mid.get().contains(newVal)
                || user.getValue().name.get().contains(newVal)
                || user.getValue().phone.get().contains(newVal)
                || user.getValue().fine.get().contains(newVal));
        });
 
//Label size = new Label();
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeView.getCurrentItemsCount(),
               treeView.currentItemsCountProperty()));

               // treeView.setPrefSize(anchor1.getPrefWidth(), anchor1.getPrefHeight());
                treeView.setPrefSize(976,551);
                midcol.setPrefWidth(anchor.getPrefWidth()/5);
                namecol.setPrefWidth(anchor.getPrefWidth()/5);                
                finecol.setPrefWidth(anchor.getPrefWidth()/5);
                phonecol.setPrefWidth(anchor.getPrefWidth()/5);
                emailcol.setPrefWidth(anchor.getPrefWidth()/5);
               
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
        new SlideOutLeft(searchlbl).play();
        new FadeInRight(searchtf).play();
        flag=1;        
        }        
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public void loadData(){
        list.clear();
        rs=db.executeQuery("select * from member where fine>0");
        try {
            while(rs.next()){
             list.add(new TotalFineModel(rs.getString("id"),rs.getString("name"),rs.getString("phone"),rs.getString("email"),rs.getString("fine")));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(TotalFineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void refresh(ActionEvent event) {
          loadData();                 
    }
    @FXML
    void back(ActionEvent event) {
        try{
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/HomePage.fxml"));
            Tab t=(MainController._homepane.getTabs()).get(0);
            t.setContent(pane);
            //HomePageController.stack_pane.getChildren().setAll(pane.getChildren());            
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
