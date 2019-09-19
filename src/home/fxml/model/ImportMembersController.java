
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.model.MemberNode;
import home.resource.barcode.Barcode_Image;
import home.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ImportMembersController implements Initializable {
    Database db=Database.getInstance();
    LocalDate date;
    LocalTime time;
    public static JFXButton close;
    int rec_count=0,id;
    ArrayList<MemberNode> members=new ArrayList<>();
    AnchorPane pane;
    @FXML
    private JFXListView<Node> list;
    @FXML
    private StackPane stack;
    @FXML
    private Label status;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXButton closebtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    close=closebtn;
    }    
    @FXML
    private void loadSheet(ActionEvent event) {
         FileChooser fc=new FileChooser();
         File file=fc.showOpenDialog(stack.getScene().getWindow());
        spinner.setVisible(true);
        status.setVisible(true);
        status.setText("Loading Members. . .");
        Runnable emailSendTask = ()->{
         String arr[]=new String[6];
         String id="";
         int idx=0;         
        if(file!=null){
        FileInputStream inputStream = null;
             try {
                 ResultSet rs=db.executeQuery("select max(id) from member");
                 rs.next();
                 id=rs.getString(1);                   
                 inputStream = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(inputStream);
                 Sheet firstSheet = workbook.getSheetAt(0);
                 Iterator<Row> iterator = firstSheet.iterator();
                 iterator.next(); //to skip titles of the sheet.
                  
                 List<? extends PictureData> photos = workbook.getAllPictures();
                  Iterator it = photos.iterator();
                  
                  l: while (iterator.hasNext()) {                      
                     Row nextRow = iterator.next();
                     Iterator<Cell> celliterator = nextRow.cellIterator(); 
                     idx=0;
                          while (celliterator.hasNext()) {
                                Cell cell = celliterator.next();
                                CellType type=cell.getCellType();
                                switch(type){
                                    case NUMERIC: arr[idx++]=""+(int)cell.getNumericCellValue(); break;
                                    case STRING: arr[idx++]=cell.getStringCellValue(); break;
                                    case BLANK: Util.notify("Invalid Excel", "check all fields are inserted properly", Util.Notification.error); break l;
                                    case ERROR: Util.notify("Invalid Excel", "Error in fields", Util.Notification.error);  break l;
                                }
                          }//while
                          MemberNode mn=new MemberNode();
                          try{
                             id=mn.id=Util.incrementID(id,"M");
                          }catch(Exception e){
                             id=mn.id="M000001";
                          }
                         // System.out.println(mn.id);
                          mn.name=arr[0];
                          mn.address=arr[1];
                          mn.course=arr[2];
                          mn.phone=arr[3];                          
                          mn.email=arr[4];
                          members.add(mn);                     
                          pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/ExcelItemNode.fxml"));
                          Circle img=((Circle)pane.getChildren().get(0));
                            PictureData pict = (PictureData)it.next();
                            byte[] data = pict.getData();
                            FileOutputStream out = new FileOutputStream(".\\img\\member\\img\\temp\\"+mn.id+".jpg");                            
                            out.write(data);                          
                            out.close();
                            img.setFill(new ImagePattern(new Image(new File(".\\img\\member\\img\\temp\\"+mn.id+".jpg").toURI().toString())));
                           HBox hbox=((HBox)pane.getChildren().get(1));
                           ((Label)hbox.getChildren().get(0)).setText(mn.name);
                           ((Label)hbox.getChildren().get(1)).setText(mn.address);
                           ((Label)hbox.getChildren().get(2)).setText(mn.course);
                           ((Label)hbox.getChildren().get(3)).setText(mn.phone);
                           ((Label)hbox.getChildren().get(4)).setText(mn.email);
                           list.getItems().add(pane);
                  }            
             } catch (Exception ex) {
                 Logger.getLogger(ImportMembersController.class.getName()).log(Level.SEVERE, null, ex);
             } finally {
                 try {
                     inputStream.close();
                 } catch (IOException ex) {
                     Logger.getLogger(ImportMembersController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         }
                spinner.setVisible(false);
                status.setVisible(false);
        };
        Thread bookloader = new Thread(emailSendTask, "MEMBER-LOADER");
        bookloader.setPriority(Thread.MAX_PRIORITY);        
        bookloader.start();        
    }

    @FXML
    private void insertRecords(ActionEvent event) {
        spinner.setVisible(true);
        status.setVisible(true);
        status.setText("Inserting Members into Database. . .");
        time=LocalTime.now();
        date=LocalDate.now();
        Runnable emailSendTask = ()->{
            String bid="";
            for(MemberNode member:members){
                try{
                    db.execute("insert into member(id,name,course,addr,phone,email,date,time) values("
                    + "'"+member.id+"',"
                    + "'"+member.name+"',"
                    + "'"+member.course+"',"
                    + "'"+member.address+"',"
                    + "'"+member.phone+"',"
                    + "'"+member.email+"',"                   
                    + "'"+date+"',"                   
                    + "'"+time+"'"                   
                    + ")");
                    Util.copyFile("img\\member\\img\\temp\\"+member.id+".jpg", "img\\member\\img\\"+bid+".jpg");
                    Barcode_Image.createImage("img\\member\\barcode\\"+bid+".png",bid);                              
                    new File("img\\member\\img\\temp\\"+member.id+".jpg").delete();
                }catch(Exception e){            
                    System.out.println(e.getMessage());
                }
            }
            spinner.setVisible(false);
            status.setVisible(false);
      
        };
        Thread bookinserter = new Thread(emailSendTask, "MEMBER-INSERTER");
        bookinserter.setPriority(Thread.MAX_PRIORITY);        
        bookinserter.start();       
    }
    @FXML
    private void clear(ActionEvent event) {
        list.getItems().clear();
        members.clear();
    }
    
}
