package agh;

import java.util.ArrayList;
import static java.lang.Math.random;

public class Genes {
    private final ArrayList<Integer> gen;

    public Genes() {
        this.gen  = new ArrayList<>();
        generategen();
    }
    public Genes(int x) {
        this.gen  = new ArrayList<>();
        for(int i=0;i<32;i++) gen.add(0);
    }
    public Genes(Genes mother, Genes father, Integer fPower) {
        this.gen  = new ArrayList<>();
        double rand = random();
        for( int i=0; i<32; i++) {
            if( rand<0.5) {
                if( i<fPower ) gen.add(father.gen.get(i));
                else gen.add(mother.gen.get(i));
            }
            else {
                if( i<fPower ) gen.add(mother.gen.get(i));
                else gen.add(father.gen.get(i));
            }
        }
//        gen.sort();
    }

    private void generategen(){
        int empty=24;
        for( int i=0; i<8; i++) {
            gen.add(i);
            int q = (int) (empty*random());
            if( i==7 ) q=empty;
            for (int j = 0; j < q; j++) gen.add(i);
            empty-=q;
        }
    }

    public Integer direction(){
        return gen.get( (int) (random()*32) );
    }

    @Override
    public String toString(){ return "Gen="+ gen; }
}
