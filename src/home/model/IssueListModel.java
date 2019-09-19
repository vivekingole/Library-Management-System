
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IssueListModel extends RecursiveTreeObject<IssueListModel>{
    public StringProperty mid;
    public StringProperty bid;
    public StringProperty issue_date;
    public StringProperty issue_time;
    public StringProperty renew_date;
    
    public IssueListModel(String _mid, String _bid, String _issue_date,String _issue_time,String _renew_date) {
        this.mid = new SimpleStringProperty(_mid);
        this.bid = new SimpleStringProperty(_bid);
        this.issue_date = new SimpleStringProperty(_issue_date) ;
        this.issue_time = new SimpleStringProperty(_issue_time) ;        
        this.renew_date = new SimpleStringProperty(_renew_date) ;        
    }      
}