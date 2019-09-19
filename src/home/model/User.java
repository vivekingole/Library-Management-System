
package home.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends RecursiveTreeObject<User>{
    public StringProperty userid;
    public StringProperty username;
    public StringProperty addr;
    public StringProperty phone;
    public StringProperty email;
    
    public User(String userid, String username, String addr,String phone,String email) {
        this.userid = new SimpleStringProperty(userid);
        this.username = new SimpleStringProperty(username);
        this.addr = new SimpleStringProperty(addr) ;
        this.phone = new SimpleStringProperty(phone) ;        
        this.email = new SimpleStringProperty(email) ;        
    }      
}
