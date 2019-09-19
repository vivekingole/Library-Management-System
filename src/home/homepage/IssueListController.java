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
import home.model.IssueListModel;
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
 * @issue_date sangolacollege
 */
public class IssueListController implements Initializable {
    public static IssueListController obj;
    JFXTreeTableView<IssueListModel> treeView;
    int flag=0;
    ProfileController pc;
    public static Database db=Database.getInstance();
    ResultSet rs;
    public ObservableList<IssueListModel> list=FXCollections.observableArrayList();;
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
        
        JFXTreeTableColumn<IssueListModel, String> issue_datecol = new JFXTreeTableColumn<>("issue_date");
        issue_datecol.setPrefWidth(150);
        issue_datecol.setCellValueFactory(( param) ->{
        if(issue_datecol.validateValue(param)) return param.getValue().getValue().issue_date;
        else return issue_datecol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<IssueListModel, String> bidcol = new JFXTreeTableColumn<>("bid");
        bidcol.setPrefWidth(150);
        bidcol.setCellValueFactory(( param) ->{
        if(bidcol.validateValue(param)) return param.getValue().getValue().bid;
        else return bidcol.getComputedValue(param);
        });

        JFXTreeTableColumn<IssueListModel, String> midcol = new JFXTreeTableColumn<>("mid");
        midcol.setPrefWidth(150);
        midcol.setCellValueFactory(( param) ->{
        if(midcol.validateValue(param)) return param.getValue().getValue().mid;
        else return midcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<IssueListModel, String> issue_timecol = new JFXTreeTableColumn<>("issue_time");
        issue_timecol.setPrefWidth(150);
         issue_timecol.setCellValueFactory(( param) ->{
        if(issue_timecol.validateValue(param)) return param.getValue().getValue().issue_time;
        else return issue_timecol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<IssueListModel, String> renew_datecol = new JFXTreeTableColumn<>("renew_date");
        renew_datecol.setPrefWidth(150);
         renew_datecol.setCellValueFactory(( param) ->{
        if(renew_datecol.validateValue(param)) return param.getValue().getValue().renew_date;
        else return renew_datecol.getComputedValue(param);
        });
// Adding data
        //ObservableList<IssueListModel> books = 
        loadData();
// build tree
        final TreeItem<IssueListModel> root = new RecursiveTreeItem<IssueListModel>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<IssueListModel>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(bidcol,midcol,issue_datecol,issue_timecol,renew_datecol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().mid.get().contains(newVal)
                || user.getValue().bid.get().contains(newVal)
                || user.getValue().issue_time.get().contains(newVal)
                || user.getValue().issue_date.get().contains(newVal));
        });
 
//Label size = new Label();
        size.textProperty().bind(Bindings.createStringBinding(()->"Total Records "+treeView.getCurrentItemsCount(),
               treeView.currentItemsCountProperty()));

               // treeView.setPrefSize(anchor1.getPrefWidth(), anchor1.getPrefHeight());
                treeView.setPrefSize(976,551);
                midcol.setPrefWidth(anchor.getPrefWidth()/5);
                bidcol.setPrefWidth(anchor.getPrefWidth()/5);                
                issue_datecol.setPrefWidth(anchor.getPrefWidth()/5);
                issue_timecol.setPrefWidth(anchor.getPrefWidth()/5);
                renew_datecol.setPrefWidth(anchor.getPrefWidth()/5);
               
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
        rs=db.executeQuery("select * from issue_book");
        try {
            while(rs.next()){
             list.add(new IssueListModel(rs.getString("mid"),rs.getString("bid"),rs.getString("issue_date"),rs.getString("issue_time"),rs.getString("renew_date")));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(IssueListController.class.getName()).log(Level.SEVERE, null, ex);
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
