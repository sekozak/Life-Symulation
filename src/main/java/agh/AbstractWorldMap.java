package agh;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, ArrayList<Animal> > animalsmap = new HashMap<>();
    protected Map<Vector2d, Grass> grassmap = new HashMap<>();
    protected Vector2d ll = new Vector2d(0,0), ur, jungleLowerLeft, jungleUpRight;
    protected int ratio;

    protected int deadAnimals=0,deadSum=0;

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal pet){
        removeAnimal(oldPosition, pet);
        addAnimal(newPosition, pet);
    }

    public void removeAnimal(Vector2d position, Animal pet){
        ArrayList<Animal> list = animalsmap.get(position);
        list.remove(pet);
        if( list.size()==0 ) animalsmap.remove(position);
    }
    public void addAnimal(Vector2d position, Animal pet){
        if( objectAt(position) instanceof Animal ) {
            ArrayList<Animal> list = animalsmap.get(position);
            list.add(pet);
        }
        else{
            ArrayList<Animal> l = new ArrayList<>();
            l.add(pet);
            animalsmap.put(position, l );
        }
    }

    @Override
    public void removeDeadAnimals(ArrayList<Animal> animalslist,int day){
        ArrayList<Animal> dead = new ArrayList<>();
        for(Animal animal : animalslist){
            if( animal.isDead() ) {
                dead.add(animal);
                animal.removeObserver(this);
                removeAnimal(animal.getPosition(), animal);
            }
        }
        for(Animal animal : dead){
            animalslist.remove(animal);
            deadSum+=day-animal.getBirthDay();
        }
        deadAnimals+=dead.size();
    }

    @Override
    public void copulation(ArrayList<Animal> animals,int startEnergy, int day){
        for( ArrayList<Animal> list : animalsmap.values() ){
            if( list.size()>1 ) {
                int fatherE=0, motherE=0;
                for (Animal animal : list) {
                    int e = animal.getEnergy();
                    if (e > fatherE) {
                        motherE = fatherE;
                        fatherE = e;
                    } else if (e > motherE) {
                        motherE = e;
                    }
                }

                Animal father=list.get(0), mother=list.get(1);
                for (Animal animal : list) {
                 if(animal.getEnergy()==fatherE) father=animal;
                 if(animal.getEnergy()==motherE) mother=animal;
                }

                if(motherE>startEnergy*0.5) {
                    Animal child = father.sex(mother,day);
                    animalsmap.get(child.getPosition()).add(child);
                    child.addObserver(this);
                    animals.add(child);
                    father.setChildren(father.getChildren()+1);
                    mother.setChildren(mother.getChildren()+1);
                }
            }
        }
    }

    public void magic5(ArrayList<Animal> animals,int startEnergy, int day){
        ArrayList<Animal> tmplist = new ArrayList<>(animals);
        for(Animal animal : tmplist){
            if( freeSpace().size()>0 ){
                Animal duplicate = new Animal(this, randomFreeSpace(freeSpace()), startEnergy, day);
                duplicate.setGen(animal.getGen());

                place(duplicate);
                animals.add(duplicate);
            }
        }
    }

    @Override
    public void eat(int  plantEnergy){
        ArrayList<Vector2d> eaten = new ArrayList<>();
        for(Vector2d pos : grassmap.keySet()){
            if( animalsmap.containsKey(pos) ) {
                ArrayList<Animal> list = animalsmap.get(pos);
                int maxenergy=0, count=0;
                for( Animal animal : list){
                    int e=animal.getEnergy();
                    if( e>maxenergy ){ maxenergy = e; count=1; }
                    else if( e==maxenergy ) count+=1;
                }
                for( Animal animal : list){
                    int e=animal.getEnergy();
                    if( e==maxenergy ){ eaten.add(animal.getPosition()); animal.eat( plantEnergy/count);}
                }
            }
        }
        for(Vector2d pos : eaten) grassmap.remove(pos);
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d q = animal.getPosition();
        if ( isOccupied(q) || !q.precedes(ur) || !q.follows(ll) ) return false;
        ArrayList<Animal> list = new ArrayList<>();
        list.add(animal);
        animalsmap.put(q, list );
        animal.addObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if( animalsmap.containsKey(position) ) return true;
        return grassmap.containsKey(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if( isOccupied(position)) {
            if (animalsmap.containsKey(position)) return animalsmap.get(position).get(0);
            return grassmap.get(position);
        }
        return null;
    }

    public void place(Grass grass) {
        Vector2d q = grass.getPosition();
        if ( isOccupied(q) || !q.follows(ll) || !q.precedes(ur) ) return;
        grassmap.put(q,grass);
    }

    @Override
    public void plantgrass(){
        if( freeSpaceJg().size()>0 ) {
            place( new Grass(randomFreeSpace(freeSpaceJg()),jungleLowerLeft,jungleUpRight) );
        }
        if( freeSpaceSawanna().size()>0 ) {
            place(new Grass(randomFreeSpace(freeSpaceSawanna()),jungleLowerLeft,jungleUpRight) );
        }
    }

    private ArrayList<Vector2d> freeSpaceJg(){
        ArrayList<Vector2d> freeVectors = new ArrayList<>();
        for(int i=jungleLowerLeft.x; i<=jungleUpRight.x; i++){
            for(int j=jungleLowerLeft.y; j<=jungleUpRight.y; j++){
                Vector2d q=new Vector2d(i,j);
                if( !isOccupied(q) ) freeVectors.add(q);
            }
        }
        return freeVectors;
    }
    private ArrayList<Vector2d> freeSpaceSawanna(){
        ArrayList<Vector2d> freeVectors = new ArrayList<>();
        ArrayList<Vector2d> freeVectorsJg =freeSpaceJg();
        ArrayList<Vector2d> freeVectorsAll = freeSpace();
        for(Vector2d vector : freeVectorsAll){
            if( !freeVectorsJg.contains(vector) ) freeVectors.add(vector);
        }

        return freeVectors;
    }
    private ArrayList<Vector2d> freeSpace(){
        ArrayList<Vector2d> freeVectors = new ArrayList<>();
        for(int i=0; i<=ur.x; i++){
            for(int j=0; j<=ur.y; j++){
                Vector2d q=new Vector2d(i,j);
                if( !isOccupied(q) ) freeVectors.add(q);
            }
        }
        return freeVectors;
    }
    private Vector2d randomFreeSpace(ArrayList<Vector2d> freeVectors){
        int l=freeVectors.size(),rand;
        rand=(int)(Math.random()*l);

        return freeVectors.get(rand);
    }

    public void jungleSize(){
        int a=(ur.x+1)/ratio, b=(ur.y+1)/ratio;
        jungleLowerLeft = new Vector2d((ur.x-a+1)/2,(ur.y-b+1)/2);
        jungleUpRight = new Vector2d( jungleLowerLeft.x+a-1 ,jungleLowerLeft.y+b-1);
    }


    @Override
    public String toString() {
        return new MapVisualizer(this).draw(ll,ur);
    }

//    ----------statystyki-----------
    public int getAnimalsQuantity() {
        int count=0;
        for(ArrayList<Animal> list: animalsmap.values()){ count+=list.size(); }
        return count;
    }
    public int getGrassQuantity() {
        return grassmap.size();
    }

    public Genes genDomination(){
        Map<Genes, Integer> map = new HashMap<>();
        int max=0;
        Genes gen = new Genes(0);
        for(ArrayList<Animal> list: animalsmap.values() ){
            for(Animal animal : list){
                Genes g=animal.getGen();
                if( !map.containsKey(g) ) map.put(g,1);
                else map.put(g,map.get(g)+1);
                if( map.get(g)>max){ max=map.get(g); gen=g; }
            }
        }
        return gen;
    }

    public int averageEnergy(){
        int count=0,sum=0;
        if(animalsmap.size()==0) return 0;
        for(ArrayList<Animal> list: animalsmap.values() ) {
            for (Animal animal : list) {
                count++;
                sum+=animal.getEnergy();
            }
        }
        return sum/count;
    }

    public int averageLifeTime(){
        if(deadAnimals==0) return 0;
        return deadSum/deadAnimals;
    }

    public int averageChildren(){
        int count=0,sum=0;
        if(animalsmap.size()==0) return 0;
        for(ArrayList<Animal> list: animalsmap.values() ) {
            for (Animal animal : list) {
                count++;
                sum+=animal.getChildren();
            }
        }
        return sum/count;
    }
}
