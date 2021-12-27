package agh;

import agh.gui.App;
import java.util.ArrayList;
import java.util.Random;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map;
    private final ArrayList<Animal>  animals = new ArrayList<>();
    private final int startEnergy, width, height, moveEnergy, plantEnergy, days, amountOfA,delay;
    private final ArrayList<App> observers = new ArrayList<>();
    private final boolean magicFive;


    public SimulationEngine(AbstractWorldMap map, int startEnergy, int width, int height, int moveEnergy, int plantEnergy, boolean magicFive){
        this.map = map;
        this.startEnergy = startEnergy;
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.magicFive=magicFive;
        days=200;
        amountOfA=4;
        delay=1000;

        createAdamAndEva();
    }

    private void positionChanged(){
        for(App a : observers){
            a.positionChanged(map);
        }
    }

    public void addMap(App m){
        observers.add(m);
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

    @Override
    public void run() {
        map.plantgrass();
        System.out.print(map);
        for (int j=0; j<days; j++) {
            System.out.println(j+1);
            map.removeDeadAnimals(animals,j);
            animals.removeIf(Animal::isDead);

            for(Animal a : animals) {
                int direction = a.getGen().direction();
                a.move(direction,moveEnergy);
            }

            map.eat(plantEnergy);

            if(magicFive){
                if(animals.size()==5) map.magic5(startEnergy,j);
            }
            else {
                map.copulation(animals, startEnergy, j);
            }

            map.plantgrass();
            System.out.print(map);

            positionChanged();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
