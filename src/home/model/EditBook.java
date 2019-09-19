
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditBook extends RecursiveTreeObject<EditBook>{
   public  StringProperty id;
   public  StringProperty name;
   public  StringProperty author;
   public  StringProperty price;
   public  StringProperty column;
   public  StringProperty photo;
    
    public EditBook(String id, String name, String author,String price,String column,String photo) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author) ;
        this.price = new SimpleStringProperty(price) ;        
        this.column = new SimpleStringProperty(column) ;    
        this.photo=new SimpleStringProperty(photo);    
    }
     
}
