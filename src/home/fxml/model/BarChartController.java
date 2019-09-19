/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.fxml.model;

import home.model.ChartData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class BarChartController implements Initializable {
    static BarChart<?, ?> barchart;
    static XYChart.Series set= new XYChart.Series<>();
    static String title;
    @FXML
    private BarChart<?, ?> bar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       barchart=bar;       
       bar.setTitle(title);
       bar.getData().addAll(set);
    }    
    public static void setBarChartData(String titlename,ChartData ...data){
        title=titlename;
        for(ChartData d:data)
           set.getData().add(new XYChart.Data(d.name,d.value));
    }
}
