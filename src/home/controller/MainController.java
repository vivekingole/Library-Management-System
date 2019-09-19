package home.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import home.databasecontroller.Database;
import home.launcher.StartingTab;
import home.resource.animatefx.animation.FadeInDown;
import home.resource.animatefx.animation.FadeInUp;
import home.resource.animatefx.animation.FadeOutDown;
import home.resource.animatefx.animation.FadeOutUp;
import home.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class MainController implements Initializable{
    
    public static String currentuser="A000001";
    public static Label statuslbl;
    public static Label user_name_;
    public static StackPane sp;
    public static JFXProgressBar statusbar;
    public static Circle user_image;
    public static JFXTabPane _homepane;    
    //public static String
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Constant variables ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
    int BUTTON_COUNT=8, TAB_SIZE=5, HOME=1, BOOK=2, ISSUE_SUBMIT=3, MEMBER=4, USER=5, SETTING=6;
    
   //~~~~~~~~~~~~~~~~~~~~~~~~~~ Stores data of current selected frame~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public static String selectedmenu;
    public static int selectedindex=1;
    public static JFXTabPane selectednode;
    
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Holds side pane object~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
     VBox side; 
     VBox side2;
    
   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ data about main scene~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
     
    JFXTabPane panes[]=new JFXTabPane[BUTTON_COUNT];
    StackPane stackpane[][]=new StackPane[BUTTON_COUNT][TAB_SIZE];
    Tab tab[][]=new Tab[BUTTON_COUNT][TAB_SIZE];
    int currenttabcount[]=new int[BUTTON_COUNT];
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ sidepane fields~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    ObservableList<Node> sidepanenodes;
    ObservableList<Node> btnlist;
    JFXButton btns[]=new JFXButton[BUTTON_COUNT];int btnindex=1;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    Database db=Database.getInstance();
    @FXML
    private StackPane stack;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Label title;
    @FXML
    private ImageView title_icon;
    @FXML
    private JFXTabPane homepane;    
    @FXML
    private JFXTabPane bookpane;
    @FXML
    private JFXTabPane issuesubmitpane;
    @FXML
    private JFXTabPane memberpane;
    @FXML
    private JFXTabPane userpane;
    @FXML
    private JFXTabPane settingpane;
    @FXML
    private AnchorPane anchor1;
    @FXML
    private Label statuslbl1;
    @FXML
    private JFXProgressBar statusbar1;
    @FXML
    private Circle user_img;
    @FXML
    private Label user_name;
    @FXML
    private JFXDrawer drawer1;
    @FXML
    private JFXHamburger hamburger2;

     @Override
    public void initialize(URL url, ResourceBundle rb){
        sp=stack;
        user_image=user_img;
        user_name_=user_name;
        _homepane=homepane;
        try {
            side= FXMLLoader.load(getClass().getResource("/home/fxml/model/SlidePane.fxml"));
            side2= FXMLLoader.load(getClass().getResource("/home/fxml/model/UserSlidePane.fxml"));
            drawer.setSidePane(side); 
            drawer1.setSidePane(side2); 
            drawer1.close();
	    drawer1.setDisable(true);       
            drawer.close();
	    drawer.setDisable(true);       
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        statusbar=statusbar1;
        statuslbl=statuslbl1;
        bookpane.setOpacity(0);
        issuesubmitpane.setOpacity(0);
        memberpane.setOpacity(0);
        userpane.setOpacity(0);
        settingpane.setOpacity(0);
        selectednode=homepane;
        homepane.toFront();
         
        HamburgerSlideCloseTransition hamburgerberger=new HamburgerSlideCloseTransition(hamburger);
         HamburgerBasicCloseTransition hamburgerberger2=new HamburgerBasicCloseTransition(hamburger2);
      /*  StartingTab.stage.widthProperty().addListener((obs, oldVal, newVal) -> {
               // side.setPrefWidth(newVal.intValue());
            });
         */   StartingTab.stage.heightProperty().addListener((obs, oldVal, newVal) -> {    //***********######IMP######***********
                side.setPrefHeight(newVal.intValue());   
                side2.setPrefHeight(newVal.intValue());  
            });
       if(currentuser.startsWith("A")){           
            ImagePattern img=new ImagePattern(new Image(new File(".\\img\\admin\\admin.jpg").toURI().toString()));  
            user_img.setFill(img);     
            user_name.setText(((String)Util.getFirstRowData("admin", "name",2)));
            ((Circle)((AnchorPane)side2.getChildren().get(0)).getChildren().get(0)).setFill(img);
            ((Label)side2.getChildren().get(1)).setText(((String)Util.getFirstRowData("admin", "name",2)));
            user_name.setText(((String)Util.getFirstRowData("admin", "name",2)));
       }else{
            String username=((String)Util.getRowData("user", "username",2, "userid",currentuser));
            user_name.setText(username);
            ((Label)side2.getChildren().get(1)).setText(username);
            ImagePattern img=new ImagePattern(new Image(new File(".\\img\\user\\"+currentuser+".jpg").toURI().toString()));  
            user_img.setFill(img);                
            ((Circle)((AnchorPane)side2.getChildren().get(0)).getChildren().get(0)).setFill(img);
       }
        loadFxml();
        
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ listener evvents for side pane 1 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
            
           
        try {             
            sidepanenodes=side.getChildren();
            btnlist=((VBox)sidepanenodes.get(1)).getChildren();
            for(Node node:btnlist){                 
                if(node.getAccessibleText()!=null){    //to check who is accessible because getchildres returns all elements
                    btns[btnindex++]=(JFXButton)node;
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
                        JFXButton btn=(JFXButton) e.getSource();
                         if(btn.getAccessibleText().equals("home")){                             
                             int pos=1;
                             new FadeOutDown(selectednode).play();  
                             new FadeInDown(homepane).play();
                            selectedindex=pos;
                            selectednode=homepane;
                            homepane.toFront();
                            title.setText("Home");
                            title_icon.setImage(new Image("/home/img/home1.png"));
                            // SlidePaneController.line1.setVisible(true);
                         }                                          
                         else if(btn.getAccessibleText().equals("book")){
                            int pos=2; 
                            if(pos < selectedindex){
                                new FadeOutDown(selectednode).play();  
                                new FadeInDown(bookpane).play();                                 
                            }else{
                                 new FadeOutUp(selectednode).play();
                                 new FadeInUp(bookpane).play();  
                            }
                            selectedindex=pos;
                            selectednode=bookpane;
                            bookpane.toFront();
                            title.setText("Book");
                            title_icon.setImage(new Image("/home/img/books.png"));
                         }
                         else if(btn.getAccessibleText().equals("issuesubmit")){
                            int pos=3; 
                            if(pos < selectedindex){
                                new FadeOutDown(selectednode).play();  
                                new FadeInDown(issuesubmitpane).play();                                 
                            }else{
                                 new FadeOutUp(selectednode).play();
                                 new FadeInUp(issuesubmitpane).play();  
                            }
                             
                            selectedindex=pos;
                            selectednode=issuesubmitpane;
                            issuesubmitpane.toFront();
                            title.setText("Issue / Submit");
                            title_icon.setImage(new Image("/home/img/add-book.png"));
                         }                                        
                         else if(btn.getAccessibleText().equals("member")){
                            int pos=4; 
                            if(pos < selectedindex){
                                new FadeOutDown(selectednode).play();  
                                new FadeInDown(memberpane).play();                                 
                            }else{
                                 new FadeOutUp(selectednode).play();
                                 new FadeInUp(memberpane).play();  
                            }                             
                            selectedindex=pos;
                            selectednode=memberpane;
                            memberpane.toFront();
                            title.setText("Member");
                            title_icon.setImage(new Image("/home/img/humm.png"));
                         }                                        
                         else if(btn.getAccessibleText().equals("user")){
                            int pos=5; 
                            if(pos < selectedindex){
                                new FadeOutDown(selectednode).play();  
                                new FadeInDown(userpane).play();                                 
                            }else{
                                 new FadeOutUp(selectednode).play();
                                 new FadeInUp(userpane).play();  
                            }                             
                            selectedindex=pos;
                            selectednode=userpane;
                            userpane.toFront();
                            title.setText("User");
                            title_icon.setImage(new Image("/home/img/user2.png"));
                         }                                        
                         else if(btn.getAccessibleText().equals("setting")){
                            int pos=6; 
                            if(pos < selectedindex){
                                new FadeOutDown(selectednode).play();  
                                new FadeInDown(settingpane).play();                                 
                            }else{
                                 new FadeOutUp(selectednode).play();
                                 new FadeInUp(settingpane).play();  
                            }                                                          
                            selectedindex=pos;
                            selectednode=settingpane;                          
                            settingpane.toFront();
                            title.setText("Setting");
                            title_icon.setImage(new Image("/home/img/setting1.png"));
                         }                                        
                         else if(btn.getAccessibleText().equals("logout")){
                            System.out.println("logout");  
                            System.exit(1);
                         } 
                         hamburgerberger.setRate(hamburgerberger.getRate()* -1);
                         hamburgerberger.play();
                         drawer.close();
                         drawer.setDisable(true);                      
                    });                            
                }
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ HamBurger event handler ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
            
            
            hamburgerberger.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{                
                hamburgerberger.setRate(hamburgerberger.getRate()* -1);
                hamburgerberger.play();
                if(hamburgerberger.getRate()==-1){
                   drawer.close();
	           drawer.setDisable(true);                      
                }
                else
                {
                    drawer.open();
	            drawer.setVisible(true);
	            drawer.setDisable(false);                 
                }                
            }); 
            hamburgerberger2.setRate(-1);
            hamburger2.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{                
                hamburgerberger2.setRate(hamburgerberger2.getRate()* -1);
                hamburgerberger2.play();
                if(hamburgerberger2.getRate()==-1){
                   drawer1.close();
	           drawer1.setDisable(true);                      
                }
                else
                {
                    drawer1.open();
	            drawer1.setVisible(true);
	            drawer1.setDisable(false);                 
                }                
            }); 
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ anchor1 change size listener ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
            
               anchor1.widthProperty().addListener(event->{
                   homepane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[HOME]);
                   bookpane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[BOOK]);
                   issuesubmitpane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[ISSUE_SUBMIT]);
                   memberpane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[MEMBER]);
                   userpane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[USER]);
                   settingpane.tabMinWidthProperty().set(anchor1.getWidth()/currenttabcount[SETTING]);
                   //HomePageController.bgimg.setFitWidth(anchor1.getWidth());
               });
               anchor1.heightProperty().addListener(event->{
                  // HomePageController.bgimg.setFitHeight(anchor1.getHeight());
                   //side.setLayoutY(0);
               });
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ anchor1 change size listener ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        } catch (Exception ex) {System.out.println(ex);}        
    }//initialise
    private void loadFxml() {
        String permission="";
        if(currentuser.startsWith("A"))
           permission=String.join(" ",Util.getUserPermission(currentuser));
        else
           permission=String.join(" ",Util.getUserPermission(currentuser));
          
         if(permission.contains("Issue Book") || permission.contains("Submit Book")){                  
            int n=0;
              try {
                   if(permission.contains("Issue Book")){
                    StackPane sp1=FXMLLoader.load(getClass().getResource("/home/fxml/IssueBook.fxml"));
                   Tab t1=new Tab("Issue Book",sp1);
                    issuesubmitpane.getTabs().add(t1);
                    n++;
                   }
                   if(permission.contains("Submit Book")){
                    StackPane sp2=FXMLLoader.load(getClass().getResource("/home/fxml/SubmitBook.fxml"));
                    Tab t2=new Tab("Submit Book",sp2);
                    issuesubmitpane.getTabs().add(t2);
                    n++;
                   }                 
                } catch (Exception ex){ System.out.println(ex);}                                             
              currenttabcount[ISSUE_SUBMIT]=n+1;
         }else{
             try {
                 AnchorPane ap=FXMLLoader.load(getClass().getResource("/home/fxml/model/NotAccess.fxml"));
                 Tab t1=new Tab("",ap);
                 issuesubmitpane.getTabs().add(t1);
                 currenttabcount[ISSUE_SUBMIT]=2;
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         if(permission.contains("Member")){
             int n=0;
              try {
                   if(permission.contains("Add Member")){
                    StackPane sp1=FXMLLoader.load(getClass().getResource("/home/fxml/AddMember.fxml"));
                   Tab t1=new Tab("Add Member",sp1);
                    memberpane.getTabs().add(t1);                    
                    n++;
                   }
                   if(permission.contains("View Member")){
                    StackPane sp2=FXMLLoader.load(getClass().getResource("/home/fxml/ViewMember.fxml"));
                    Tab t2=new Tab("View Member",sp2);
                    memberpane.getTabs().add(t2);
                    n++;
                   }
                   if(permission.contains("Edit Member")){
                    StackPane sp3=FXMLLoader.load(getClass().getResource("/home/fxml/EditMember.fxml"));
                    Tab t3=new Tab("Edit Member",sp3);
                    memberpane.getTabs().add(t3);
                    n++;
                   }
                } catch (Exception ex){ System.out.println(ex);}                                             
              currenttabcount[MEMBER]=n+1;
         }else{
             try {
                 AnchorPane ap=FXMLLoader.load(getClass().getResource("/home/fxml/model/NotAccess.fxml"));
                 Tab t1=new Tab("",ap);
                 memberpane.getTabs().add(t1);
                 currenttabcount[MEMBER]=2;
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         if(permission.contains("Add Book") || permission.contains("View Book") || permission.contains("Edit Book")){
             int n=0;
            try {                   
                   if(permission.contains("Add Book")){
                    StackPane sp1=FXMLLoader.load(getClass().getResource("/home/fxml/AddBook.fxml"));
                    Tab t1=new Tab("Add Book",sp1);
                    bookpane.getTabs().add(t1);
                    n++;
                   }
                   if(permission.contains("View Book")){
                       System.out.println("view book");
                    StackPane sp2=FXMLLoader.load(getClass().getResource("/home/fxml/ViewBook.fxml"));
                    Tab t2=new Tab("View Book",sp2);
                    bookpane.getTabs().add(t2);
                    n++;
                   }
                   if(permission.contains("Edit Book")){
                       System.out.println("edit book");
                    StackPane sp3=FXMLLoader.load(getClass().getResource("/home/fxml/EditBook.fxml"));
                    Tab t3=new Tab("Edit Book",sp3);
                    bookpane.getTabs().add(t3);
                    n++;
                   }
                } catch (Exception ex){ System.out.println(ex);} 
              currenttabcount[BOOK]=n+1;
         }else{
             try {
                 AnchorPane ap=FXMLLoader.load(getClass().getResource("/home/fxml/model/NotAccess.fxml"));
                 Tab t1=new Tab("",ap);
                 bookpane.getTabs().add(t1);
                 currenttabcount[BOOK]=2;                                  
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         if(permission.contains("User")){
            int n=0;
            try {                   
                   if(permission.contains("Add User")){
                    StackPane sp1=FXMLLoader.load(getClass().getResource("/home/fxml/AddUser.fxml"));
                    Tab t1=new Tab("Add User",sp1);
                    userpane.getTabs().add(t1);
                    n++;
                   }
                   if(permission.contains("View User")){
                    StackPane sp2=FXMLLoader.load(getClass().getResource("/home/fxml/ViewUser.fxml"));
                    Tab t2=new Tab("View User",sp2);
                    userpane.getTabs().add(t2);
                    n++;
                   }
                   if(permission.contains("Edit User")){
                    StackPane sp3=FXMLLoader.load(getClass().getResource("/home/fxml/EditUser.fxml"));
                    Tab t3=new Tab("Edit User",sp3);
                    userpane.getTabs().add(t3);
                    n++;
                   }
                } catch (Exception ex){ System.out.println(ex);} 
              currenttabcount[USER]=n+1;
         }else{
             try {
                 AnchorPane ap=FXMLLoader.load(getClass().getResource("/home/fxml/model/NotAccess.fxml"));
                 Tab t1=new Tab("",ap);
                 userpane.getTabs().add(t1);
                 currenttabcount[USER]=2;                                  
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         if(permission.contains("Edit Setting")){
             int n=0;
             try {
                 StackPane sp=FXMLLoader.load(getClass().getResource("/home/fxml/Settings.fxml"));
                 Tab t1=new Tab("",sp);
                 settingpane.getTabs().add(t1);
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }         
                 currenttabcount[SETTING]=n+1;                                  
         }else{
             try {
                 AnchorPane ap=FXMLLoader.load(getClass().getResource("/home/fxml/model/NotAccess.fxml"));
                 Tab t1=new Tab("",ap);
                 settingpane.getTabs().add(t1);
                 currenttabcount[SETTING]=2;                                  
             } catch (IOException ex) {
                 Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
          int n=0;
              try {
                   StackPane sp2=FXMLLoader.load(getClass().getResource("/home/fxml/HomePage.fxml"));
                    Tab t2=new Tab("Home Page",sp2);
                    homepane.getTabs().add(t2);
                    n++;                                    
                } catch (Exception ex){ System.out.println(ex);}                                             
              currenttabcount[HOME]=n+1;
    }


 
}
