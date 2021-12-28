package agh;

import agh.gui.App;
import java.util.ArrayList;
import java.util.Random;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map;
    private final ArrayList<Animal>  animals = new ArrayList<>();
    private final int startEnergy, width, height, moveEnergy, plantEnergy, amountOfA,delay;
    private final ArrayList<App> observers = new ArrayList<>();
    private final boolean magicFive;
    private int magic5count=0,days=0;


    public SimulationEngine(AbstractWorldMap map, int startEnergy, int width, int height, int moveEnergy, int plantEnergy, int amountOfA, boolean magicFive){
        this.map = map;
        this.startEnergy = startEnergy;
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.magicFive=magicFive;
        this.amountOfA=amountOfA;
        delay=1000;

        createAdamAndEva();
    }

    private void createAdamAndEva(){
        Random generator = new Random();
        for ( int i=0; i<amountOfA; i++) {
            int a = generator.nextInt( width +1);
            int b = generator.nextInt( height +1);
            Animal animal = new Animal(map, new Vector2d(a,b), startEnergy ,0);
            if ( map.place(animal) ) animals.add(animal);
            else i--;
        }
    }

    private void positionChanged(){
        for(App a : observers){
            a.positionChanged(map);
        }
    }
    private void magicFiveAlert(){
        for(App a : observers){
            a.magicFiveAlert(map);
        }
    }

    public void addMap(App m){
        observers.add(m);
    }

    @Override
    public void run() {
        System.out.print(map);
        while(true) {
//            -----usunięcie martwych zwierząt z mapy-----
            map.removeDeadAnimals(animals,days);
            animals.removeIf(Animal::isDead);

//            ------poruszanie sie zwierzat w danym dniu------
            for(Animal a : animals) {
                int direction = a.getGen().direction();
                a.move(direction,moveEnergy);
            }

//            -------jedzenie-------
            map.eat(plantEnergy);


//            -----rozmnażanie zwierząt-----
            if(magicFive){
                if(animals.size()==5){ map.magic5(animals, startEnergy,days); magic5count++; }
                if(magic5count==3){ magicFiveAlert(); }
            }
            else{ map.copulation(animals, startEnergy, days); }

//            ------dodanie nowych roślin do mapy------
            map.plantgrass();
            System.out.print(map);


            positionChanged();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            days++;
        }
    }
}
