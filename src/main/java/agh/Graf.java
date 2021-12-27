package agh;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Graf {
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final XYChart.Series animals = new XYChart.Series();
    private final XYChart.Series grass = new XYChart.Series();
    private final XYChart.Series energy = new XYChart.Series();
    private final XYChart.Series lifeTime = new XYChart.Series();
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
    public void newEnergyData(int day,int quantity){
        energy.getData().add(new XYChart.Data(day, quantity));
    }
    public void newLiveTimeData(int day,int quantity){
        lifeTime.getData().add(new XYChart.Data(day, quantity));
    }

    public Graf(String type){
        lineChart.setMaxSize(300,300);
        //defining the axes
        xAxis.setLabel("Days");
        //creating the chart
        lineChart.setTitle("WYKRES "+type);
        //defining a animals
        animals.setName("Animals");
        energy.setName("averageEnergy");
        grass.setName("Grass");
        lifeTime.setName("lifeTime");

        lineChart.getData().add(animals);
        lineChart.getData().add(energy);
        lineChart.getData().add(grass);
        lineChart.getData().add(lifeTime);

    }

}
