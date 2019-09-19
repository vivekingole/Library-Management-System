
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UnsubmittedBookModel extends RecursiveTreeObject<UnsubmittedBookModel>{
    public StringProperty mid;
    public StringProperty bid;
    public StringProperty renew_date;
    public StringProperty issue_date;
    public StringProperty days;
    
    public UnsubmittedBookModel(String _mid, String _bid,String _renew_date, String _issue_date,String _days) {
        this.mid = new SimpleStringProperty(_mid);
        this.bid = new SimpleStringProperty(_bid);
        this.renew_date = new SimpleStringProperty(_renew_date) ;        
        this.issue_date = new SimpleStringProperty(_issue_date) ;
        this.days = new SimpleStringProperty(_days) ;        
    }      
}