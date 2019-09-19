
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TotalFineModel extends RecursiveTreeObject<TotalFineModel>{
    public StringProperty mid;
    public StringProperty name;
    public StringProperty phone;
    public StringProperty email;
    public StringProperty fine;
    
    public TotalFineModel(String _mid, String _name, String _phone,String _email,String _fine) {
        this.mid = new SimpleStringProperty(_mid);
        this.name = new SimpleStringProperty(_name);
        this.phone = new SimpleStringProperty(_phone) ;
        this.email = new SimpleStringProperty(_email) ;        
        this.fine = new SimpleStringProperty(_fine) ;        
    }      
}