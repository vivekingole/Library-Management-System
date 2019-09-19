/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import com.jfoenix.controls.JFXButton;
import home.model.ChartData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;


public class PieChartController implements Initializable {
    public static ObservableList<PieChart.Data> list=FXCollections.observableArrayList();
    public static PieChart piechart;
    public static String title;
    public static PieChartController obj;
    public static JFXButton close;
    @FXML
    private PieChart pie;
    @FXML
    private JFXButton closebtn;

     @Override
    public void initialize(URL url, ResourceBundle rb) {     
      piechart=pie;
      obj=this;
      close=closebtn;
      pie.setData(list); 
      pie.setTitle(title);
      
    }    
    public static void setPiechartData(String titlename,ChartData ...data){        
        title=titlename;
        list.clear();
        for(ChartData d:data)
            list.add(new PieChart.Data(d.name,d.value));                          
    }
}
