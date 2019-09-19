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
import home.model.UnsubmittedBookModel;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
public class UnsubmittedBookController implements Initializable {
    public static UnsubmittedBookController obj;
    JFXTreeTableView<UnsubmittedBookModel> treeView;
    int flag=0;
    ProfileController pc;
    public static Database db=Database.getInstance();
    ResultSet rs;
    public ObservableList<UnsubmittedBookModel> list=FXCollections.observableArrayList();;
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
        
        JFXTreeTableColumn<UnsubmittedBookModel, String> issue_datecol = new JFXTreeTableColumn<>("Issue_date");
        issue_datecol.setPrefWidth(150);
        issue_datecol.setCellValueFactory(( param) ->{
        if(issue_datecol.validateValue(param)) return param.getValue().getValue().issue_date;
        else return issue_datecol.getComputedValue(param);
        });
                
        JFXTreeTableColumn<UnsubmittedBookModel, String> bidcol = new JFXTreeTableColumn<>("Book id");
        bidcol.setPrefWidth(150);
        bidcol.setCellValueFactory(( param) ->{
        if(bidcol.validateValue(param)) return param.getValue().getValue().bid;
        else return bidcol.getComputedValue(param);
        });

        JFXTreeTableColumn<UnsubmittedBookModel, String> midcol = new JFXTreeTableColumn<>("Member id");
        midcol.setPrefWidth(150);
        midcol.setCellValueFactory(( param) ->{
        if(midcol.validateValue(param)) return param.getValue().getValue().mid;
        else return midcol.getComputedValue(param);
        });
        
         JFXTreeTableColumn<UnsubmittedBookModel, String> renew_datecol = new JFXTreeTableColumn<>("Renew Date");
        renew_datecol.setPrefWidth(150);
         renew_datecol.setCellValueFactory(( param) ->{
        if(renew_datecol.validateValue(param)) return param.getValue().getValue().renew_date;
        else return renew_datecol.getComputedValue(param);
        });
                
         JFXTreeTableColumn<UnsubmittedBookModel, String> dayscol = new JFXTreeTableColumn<>("Days");
        dayscol.setPrefWidth(150);
         dayscol.setCellValueFactory(( param) ->{
        if(dayscol.validateValue(param)) return param.getValue().getValue().days;
        else return dayscol.getComputedValue(param);
        });
// Adding data
        //ObservableList<UnsubmittedBookModel> books = 
        loadData();
// build tree
        final TreeItem<UnsubmittedBookModel> root = new RecursiveTreeItem<UnsubmittedBookModel>(list, RecursiveTreeObject::getChildren);         
        treeView = new JFXTreeTableView<UnsubmittedBookModel>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(bidcol,midcol,issue_datecol,renew_datecol,dayscol);
        searchtf.textProperty().addListener((o,oldVal,newVal)->{
        treeView.setPredicate(user -> user.getValue().mid.get().contains(newVal)
                || user.getValue().bid.get().contains(newVal)
                || user.getValue().renew_date.get().contains(newVal)
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
                renew_datecol.setPrefWidth(anchor.getPrefWidth()/5);
                dayscol.setPrefWidth(anchor.getPrefWidth()/5);
               
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
             Date d1=Date.valueOf(rs.getString("renew_date"));            
             int days=(int)ChronoUnit.DAYS.between(d1.toLocalDate(),LocalDate.now());
             
             if(days>=0){                 
               list.add(new UnsubmittedBookModel(rs.getString("mid"),rs.getString("bid"),rs.getString("renew_date"),d1.toString(),""+days));   
             }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnsubmittedBookController.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
