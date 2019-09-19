
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book extends RecursiveTreeObject<Book>{
    public StringProperty id;
    public StringProperty name;
    public StringProperty author;
    public StringProperty column;
    public StringProperty available;
    
    public Book(String id, String name, String author,String column,String available) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author) ;
        this.column = new SimpleStringProperty(column) ;        
        this.available = new SimpleStringProperty(available) ;        
    }      
}