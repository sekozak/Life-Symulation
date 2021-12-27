package agh;
import java.io.*;

public class SaveCSV {
    String[] names={"Day: ",", animals: ",", grass: ",", averageEnergy: ",", avgLifeTime: "};
    File file;
    int animalsAvg=0,grassAvg=0,energyAvg=0,days=0,lifeAvg=0;

    public SaveCSV(String nazwaPliku) {
        this.file =new File(nazwaPliku+".csv");
    }


    public void saveToCsv(int[] dane) {
        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
            Writer writer = new BufferedWriter(osw);
            StringBuilder sb = new StringBuilder();

            int i=0;
            for(String s : names){
                sb.append(s);
                sb.append(dane[i]);
                i++;
            }
            sb.append("\n");

            days=dane[0];
            animalsAvg+=dane[1];
            grassAvg+=dane[2];
            energyAvg+=dane[3];
            lifeAvg+=dane[4];

            writer.append(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addAvg() {
        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
            Writer writer = new BufferedWriter(osw);
            StringBuilder sb = new StringBuilder();

            sb.append("AvgAnimals: ");
            sb.append(animalsAvg/days);
            sb.append(", AvgGrass: ");
            sb.append(grassAvg/days);
            sb.append(", AvgEnergy: ");
            sb.append(energyAvg/days);
            sb.append(", AvgLifeTime: ");
            sb.append(lifeAvg/days);

            writer.append(sb.toString());
            writer.close();
            System.out.println("done!");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
