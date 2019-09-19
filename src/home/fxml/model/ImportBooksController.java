
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTimePicker;
import home.databasecontroller.Database;
import home.model.BookNode;
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


public class ImportBooksController implements Initializable {
    Database db=Database.getInstance();
    LocalDate date;
    LocalTime time;
    public static JFXButton close;
    int rec_count=0,id;
    ArrayList<BookNode> books=new ArrayList<>();
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
        //spinner.setVisible(true);
       // status.setVisible(true);
        status.setText("Loading Books . . .");
        Runnable emailSendTask = ()->{
         String arr[]=new String[6];
         String id="";
         int idx=0;         
        if(file!=null){
        FileInputStream inputStream = null;
             try {
                 ResultSet rs=db.executeQuery("select max(id) from book");
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
                          BookNode bn=new BookNode();
                          try{
                             id=bn.id=Util.incrementID(id,"B",Integer.valueOf(arr[3]));
                          }catch(Exception e){
                             id=bn.id="B000001";
                          }
                          System.out.println(bn.id);
                          bn.name=arr[0];
                          bn.author=arr[1];
                          bn.price=arr[2];
                          bn.copies=arr[3];                          
                          bn.column=arr[4];
                          books.add(bn);                     
                          pane=FXMLLoader.load(getClass().getResource("/home/fxml/model/ExcelItemNode.fxml"));
                          Circle img=((Circle)pane.getChildren().get(0));
                            PictureData pict = (PictureData)it.next();
                            byte[] data = pict.getData();
                            FileOutputStream out = new FileOutputStream(".\\img\\book\\img\\temp\\"+bn.id+".jpg");                            
                            out.write(data);                          
                            out.close();
                            img.setFill(new ImagePattern(new Image(new File(".\\img\\book\\img\\temp\\"+bn.id+".jpg").toURI().toString())));
                           HBox hbox=((HBox)pane.getChildren().get(1));
                           ((Label)hbox.getChildren().get(0)).setText(bn.name);
                           ((Label)hbox.getChildren().get(1)).setText(bn.author);
                           ((Label)hbox.getChildren().get(2)).setText(bn.price);
                           ((Label)hbox.getChildren().get(3)).setText(bn.copies);
                           ((Label)hbox.getChildren().get(4)).setText(bn.column);
                           list.getItems().add(pane);
                          // record_count.setText("Total "+(++rec_count)+" Records loaded");
                  }            
             } catch (Exception ex) {
                 Logger.getLogger(ImportBooksController.class.getName()).log(Level.SEVERE, null, ex);
             } finally {
                 try {
                     inputStream.close();
                 } catch (IOException ex) {
                     Logger.getLogger(ImportBooksController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         }
              //  spinner.setVisible(false);
               // status.setVisible(false);
        };
        Thread bookloader = new Thread(emailSendTask, "BOOK-LOADER");
        bookloader.setPriority(Thread.MAX_PRIORITY);        
        bookloader.start();        
    }

    @FXML
    private void insertRecords(ActionEvent event) {
        spinner.setVisible(true);
        status.setVisible(true);
        status.setText("Inserting Books into Database. . .");
        time=LocalTime.now();
        date=LocalDate.now();
        id=Util.decodeInt(books.get(0).id);
        Runnable emailSendTask = ()->{
            String bid="";
            for(BookNode book:books){
                try{
                    for(int i=0;i<Integer.parseInt(book.copies);i++){
                        bid=Util.generateCode("B",id++);
                        add(bid,book);                                              
                        Util.copyFile("img\\book\\img\\temp\\"+book.id+".jpg", "img\\book\\img\\"+bid+".jpg");
                        new File("img\\book\\img\\temp\\"+book.id+".jpg").delete();
                    }
                }catch(Exception e){            
                    System.out.println(e.getMessage());
                }
            }
            spinner.setVisible(false);
            status.setVisible(false);
           
        };
        Thread bookinserter = new Thread(emailSendTask, "BOOK-INSERTER");
        bookinserter.setPriority(Thread.MAX_PRIORITY);        
        bookinserter.start();       
    }
    private void add(String bid,BookNode book)throws Exception{
          db.execute("insert into book(id,name,author,price,date,time,column,photo,available) values("
                    + "'"+bid+"',"
                    + "'"+book.name+"',"
                    + "'"+book.author+"',"
                    + book.price+","
                    + "'"+date+"',"
                    + "'"+time+"',"                            
                    + "'"+book.column+"',"                            
                    + "'"+book.id+"',"                   
                    + "'Available'"                   
                    + ")");
                  Barcode_Image.createImage("img\\book\\barcode\\"+bid+".png",bid);                              
    }
    
    @FXML
    private void clear(ActionEvent event) {
        list.getItems().clear();
        books.clear();
    }
    
}
