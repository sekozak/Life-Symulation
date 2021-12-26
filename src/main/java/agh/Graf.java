package agh;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Graf {
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series animals = new XYChart.Series();
    private XYChart.Series grass = new XYChart.Series();
    final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);


    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    public void newAnimalData(int day,int quantity){
        animals.getData().add(new XYChart.Data(day, quantity));
    }
    public void newGrassData(int day,int quantity){
        grass.getData().add(new XYChart.Data(day, quantity));
    }

    public Graf(String type){
        lineChart.setMaxSize(300,300);
        //defining the axes
        xAxis.setLabel("Days");
        //creating the chart
        lineChart.setTitle("WYKRES "+type);
        //defining a animals
        animals.setName("Animals");
        grass.setName("Grass");

        lineChart.getData().add(animals);
        lineChart.getData().add(grass);
    }

}
