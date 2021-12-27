package agh.gui;
import agh.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class App extends Application implements ISymPosChangeObserver {
    private final int sceneWidth = 1200, sceneHeight = 800;
    public Scene scene;
    private AbstractWorldMap map1,map2;
    private GridPane gridStrict,gridBorderless,gridGenes;
    private BorderPane borderPane;
    private SimulationEngine engineStrict,engineBorderless;
    private final Graf grafStrict=new Graf("Strict"), grafBorderless=new Graf("Borderless");
    private int mapWidth, mapHeight, startEnergy, moveEnergy, jungleRatio, plantEnergy, dayStrict=0,dayBorderless=0;
    private boolean magicFive=false,clicked1=false,clicked2=false;
    Button btn;
    TextField widthTxt, heightTxt, startEnergyTxt, moveEnergyTxt, jungleRatioTxt, plantEnergyTxt;
    Genes dominantGenStrict, dominantGenBorderless;
    SaveCSV saveCsvStrict=new SaveCSV("StrictStats"),saveCsvBorderless=new SaveCSV("BorderlesstStats");


    public void init() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("I WASTED A LOT OF PRECIOUS TIME :)");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        grid.add(scenetitle, 0, 0, 6, 1);

        Label widthL = new Label("Width:");
        grid.add(widthL, 0, 1);

        widthTxt = new TextField();
        grid.add(widthTxt, 1, 1);

        Label heightL = new Label("Height:");
        grid.add(heightL, 0, 2);

        heightTxt = new TextField();
        grid.add(heightTxt, 1, 2);

        Label startEnergyL = new Label("startEnergy:");
        grid.add(startEnergyL, 0, 3);

        startEnergyTxt = new TextField();
        grid.add(startEnergyTxt, 1, 3);

        Label moveEnergyL = new Label("moveEnergy:");
        grid.add(moveEnergyL, 0, 4);

        moveEnergyTxt = new TextField();
        grid.add(moveEnergyTxt, 1, 4);

        Label plantEnergyL = new Label("plantEnergy:");
        grid.add(plantEnergyL, 0, 5);

        plantEnergyTxt = new TextField();
        grid.add(plantEnergyTxt, 1, 5);

        Label jungleRatioL = new Label("jungleRatio:");
        grid.add(jungleRatioL, 0, 6);

        jungleRatioTxt = new TextField();
        grid.add(jungleRatioTxt, 1, 6);

        btn = new Button("SUBMIT");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 8);

        scene = new Scene(grid, sceneWidth, sceneHeight);
    }


    @Override
    public void start(Stage primaryStage) {
        btn.setOnAction(event -> {
            mapWidth=Integer.parseInt(widthTxt.getText());
            mapHeight=Integer.parseInt(heightTxt.getText());
            startEnergy=Integer.parseInt(startEnergyTxt.getText());
            moveEnergy=Integer.parseInt(moveEnergyTxt.getText());
            jungleRatio=Integer.parseInt(jungleRatioTxt.getText());
            plantEnergy=Integer.parseInt(plantEnergyTxt.getText());

            map1 = new StrictMap(mapWidth,mapHeight, jungleRatio);
            map2 = new FlexMap(mapWidth,mapHeight, jungleRatio);
            engineStrict = new SimulationEngine(map1, startEnergy, mapWidth, mapHeight ,moveEnergy, plantEnergy, magicFive);
            engineBorderless = new SimulationEngine(map2, startEnergy, mapWidth, mapHeight ,moveEnergy, plantEnergy, magicFive);
            engineStrict.addMap(this);
            engineBorderless.addMap(this);

            Thread engineThread1 = new Thread(engineStrict);
            Thread engineThread2 = new Thread(engineBorderless);
            engineThread1.start();
            engineThread2.start();

            gridStrict = new GridPane();
            gridBorderless = new GridPane();
            for(int i=0; i<=mapWidth+1; i++){
                gridStrict.getColumnConstraints().add(new ColumnConstraints(20));
                gridBorderless.getColumnConstraints().add(new ColumnConstraints(20));}
            for(int i=0; i<=mapHeight+1; i++){
                gridStrict.getRowConstraints().add(new RowConstraints(20));
                gridBorderless.getRowConstraints().add(new RowConstraints(20));}

            drawGrid(gridStrict,map1);
            drawGrid(gridBorderless,map2);
            borderPane = new BorderPane();

            
            Button stopBtn1 = new Button("STOP/START Strict");
            stopBtn1.setOnAction(e -> {
                if(clicked1){ engineThread1.resume(); clicked1=false; }
                else{ engineThread1.suspend(); clicked1=true; }
            });
            HBox hbBtn1 = new HBox(10);
            hbBtn1.setAlignment(Pos.BOTTOM_CENTER);
            hbBtn1.getChildren().add(stopBtn1);
            GridPane gridStrict1 = new GridPane();
            gridStrict1.add(gridStrict,0,0);
            gridStrict1.add(hbBtn1,0,1);
            borderPane.setLeft(gridStrict1);

            
            Button stopBtn2 = new Button("STOP/START Borderless");
            stopBtn2.setOnAction(e -> {
                if(clicked2){ engineThread2.resume(); clicked2=false; }
                else{ engineThread2.suspend(); clicked2=true; }
            });
            HBox hbBtn2 = new HBox(10);
            hbBtn2.setAlignment(Pos.BOTTOM_CENTER);
            hbBtn2.getChildren().add(stopBtn2);
            GridPane gridBorderless2 = new GridPane();
            gridBorderless2.add(gridBorderless,0,0);
            gridBorderless2.add(hbBtn2,0,1);
            borderPane.setRight(gridBorderless2);

            
            GridPane gridStats = new GridPane();
            gridStats.add(grafStrict.getLineChart(),0,0);
            gridStats.add(grafBorderless.getLineChart(),1,0);
            gridStats.setAlignment(Pos.BASELINE_CENTER);
            borderPane.setCenter(gridStats);


            gridGenes = new GridPane();
            gridGenes.setAlignment(Pos.BASELINE_CENTER);
            drawGenes();
            borderPane.setBottom(gridGenes);


            scene = new Scene(borderPane, sceneWidth, sceneHeight);
            primaryStage.setScene(scene);
            primaryStage.show();

        });
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    public void drawGenes(){
        gridGenes.getChildren().clear();
        gridGenes.setHgap(30);
        dominantGenStrict = map1.genDomination();
        dominantGenBorderless = map2.genDomination();
        Label label1 = new Label(dominantGenStrict.toString());
        GridPane.setHalignment(label1, HPos.CENTER);
        gridGenes.add(label1,0,0);
        Label label2 = new Label(dominantGenBorderless.toString());
        GridPane.setHalignment(label2, HPos.CENTER);
        gridGenes.add(label2,1,0);
    }


    public void drawGrid(GridPane grid,AbstractWorldMap m){
        grid.getChildren().clear();
        grid.setGridLinesVisible(false);
        grid.setGridLinesVisible(true);

        grid.add(new Label("X/Y"), 0, 0);
        for(int i=1; i<=mapWidth+1; i++){
            int q=i-1;
            Label label = new Label(Integer.toString(q));
            GridPane.setHalignment(label, HPos.CENTER);
            grid.add(label, i, 0);
        }
        for(int j=0; j<=mapHeight; j++) {
            int q = mapHeight - j;
            Label label = new Label(Integer.toString(q));
            GridPane.setHalignment(label, HPos.CENTER);
            grid.add(label, 0, j+1);
        }

        for(int i=0; i<=mapWidth+1; i++) {
            for (int j = 0; j <= mapHeight + 1; j++) {
                Vector2d v=new Vector2d(i - 1, mapHeight - j + 1);
                if (m.isOccupied(v)) {
                    VBox vbox = new GuiElementBox((IMapElement) m.objectAt(v)).elementVisualization();
                    grid.add(vbox, i, j);
                }
            }
        }
    }


    @Override
    public void positionChanged(AbstractWorldMap m){
        if(m.isStrict()) {
            Platform.runLater( () -> {drawGrid(gridStrict,m);drawGenes();} );
            dayStrict++;
            int[] data= {dayStrict,map1.getAnimalsQuantity(),map1.getGrassQuantity(),map1.averageEnergy(),map1.averageLifeTime()};
            grafStrict.newAnimalData(dayStrict, data[1]);
            grafStrict.newGrassData(dayStrict, data[2]);
            grafStrict.newEnergyData(dayStrict, data[3]);
            grafStrict.newLiveTimeData(dayStrict, data[4]);
            saveCsvStrict.saveToCsv(data);
            dominantGenStrict = map1.genDomination();
        }
        else{
            Platform.runLater( () -> drawGrid(gridBorderless,m) );
            dayBorderless++;
            int[] data= {dayBorderless ,map2.getAnimalsQuantity(),map2.getGrassQuantity(),map2.averageEnergy(),map2.averageLifeTime()};
            grafBorderless.newAnimalData(dayBorderless, data[1]);
            grafBorderless.newGrassData(dayBorderless, data[2]);
            grafBorderless.newEnergyData(dayBorderless, data[3]);
            grafBorderless.newLiveTimeData(dayBorderless, data[4]);
            saveCsvBorderless.saveToCsv(data);
            dominantGenBorderless = map2.genDomination();
        }
    }

}