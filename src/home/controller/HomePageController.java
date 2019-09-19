
package home.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import home.databasecontroller.Database;
import home.fxml.model.IDCardBackSideController;
import home.fxml.model.IDCardController;
import home.fxml.model.InfoController;
import home.fxml.model.PieChartController;
import home.launcher.StartingTab;
import home.model.ChartData;
import home.util.Dialog;
import home.util.Util;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


public class HomePageController implements Initializable,EventHandler<ActionEvent> {

    Database db=Database.getInstance();
    StackPane sp;
    public static StackPane stack_pane;
    public static ImageView bgimg;
    @FXML
    private StackPane stack;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     stack_pane=stack;
     bgimg=image;
        try {
            sp=FXMLLoader.load(getClass().getResource("/home/fxml/model/Info.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void usersMembersList(ActionEvent event) {
        try {
            ResultSet rs;
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/PieChart.fxml"));
            int users=Util.getCount("user", "userid");
            rs=db.executeQuery("select count(id) from member where id like 'S%'");
            rs.next();
            int students=rs.getInt(1);
            rs=db.executeQuery("select count(id) from member where id like 'T%'");
            rs.next();
            int teachers=rs.getInt(1);
            PieChartController.setPiechartData("Total ("+(users+students+teachers)+")",new ChartData("Users ("+users+")",users),
                new ChartData("Students ("+students+")",students),
                new ChartData("Teachers ("+teachers+")",teachers));
            
            Dialog.loadDialogPane(stack, pane, PieChartController.close, JFXDialog.DialogTransition.CENTER);
        } catch (Exception ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void booksAvailable(ActionEvent event) {
        try {
            ResultSet rs;
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/PieChart.fxml"));
            int books=Util.getCount("book", "id");
            rs=db.executeQuery("select count(id) from book where available='Available'");
            rs.next();
            int available=rs.getInt(1);
            int issued=books-available;
            PieChartController.setPiechartData("Total Books ("+books+")",new ChartData("Available ("+available+")",available),
                new ChartData("Issued ("+issued+")",issued));
            Dialog.loadDialogPane(stack, pane, PieChartController.close, JFXDialog.DialogTransition.CENTER);
        } catch (Exception ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void idCard(ActionEvent event) {
         Dialog.loadInputDialog(stack, 400.0, 300.0, new Text("Enter Member ID"), "Generate ID", 200.0, 50.0, JFXDialog.DialogTransition.CENTER,this);        
    }

    @FXML
    private void totalFine(ActionEvent event) {
         try{
             StackPane root=FXMLLoader.load(getClass().getResource("/home/homepage/TotalFine.fxml"));
             stack.getChildren().setAll(root);            
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void issuedBooks(ActionEvent event) {
        try{
             StackPane root=FXMLLoader.load(getClass().getResource("/home/homepage/IssueList.fxml"));
             stack.getChildren().setAll(root);            
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void unsubmittedBooks(ActionEvent event) {
        try{
             StackPane root=FXMLLoader.load(getClass().getResource("/home/homepage/UnsubmittedBook.fxml"));
             stack.getChildren().setAll(root);            
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void idcardBackSide(ActionEvent event) {
        try{
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/IDCardBackSide.fxml"));
            Dialog.loadDialogPane(stack, pane, IDCardBackSideController.close_btn, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void resetSystem(ActionEvent event) {
    }

    @FXML
    private void aboutUs(ActionEvent event) {
        Dialog.loadDialogPane(stack,sp ,InfoController.close, JFXDialog.DialogTransition.CENTER);
        InfoController.runAnimation1();
    }

    @Override
    public void handle(ActionEvent event) {
       JFXButton src=(JFXButton)event.getSource();
        if(src.getText().equals("Generate ID")){
            try{
            ResultSet rs=db.executeQuery("select * from member where id='"+Dialog.tf.getText()+"'");
            rs.next();
             String s=rs.getString("date");
             Date dt=Date.valueOf(s);
             int d=dt.toLocalDate().getYear();
             IDCardController.setValues((String)Util.getFirstRowData("setting", "idcard_title", 2),Dialog.tf.getText() , rs.getString("name"),d+" - "+(d+1),"31 / 5 / "+(d+1),rs.getString("phone"),rs.getString("addr"));
            StackPane pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/IDCard.fxml"));
            Dialog.loadDialogPane(stack, pane, IDCardController.close_btn, JFXDialog.DialogTransition.CENTER);
            Dialog.dialog.close();
           } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
              Util.notify("Not Found !", "Member ID not found !", Util.Notification.error);
           }
        }
          
    }
    
}
