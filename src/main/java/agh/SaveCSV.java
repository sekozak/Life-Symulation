package agh;
import java.io.*;

public class SaveCSV {
    String names="Day, animals, grass, averageEnergy, avgLifeTime, avgChildren \n";
    File file;
    int animalsAvg=0,grassAvg=0,energyAvg=0,days=0,lifeAvg=0,childAvg=0;

    public SaveCSV(String nazwaPliku) {


        try {
            this.file =new File(nazwaPliku+".csv");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
            Writer writer = new BufferedWriter(osw);
            StringBuilder sb = new StringBuilder();
            writer.append(names);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveToCsv(int[] dane) {
        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
            Writer writer = new BufferedWriter(osw);
            StringBuilder sb = new StringBuilder();

            for(int d : dane){
                sb.append(d);
                sb.append(',');
            }
            sb.append("\n");

            writer.append(sb.toString());
            writer.close();

            days=dane[0];
            animalsAvg+=dane[1];
            grassAvg+=dane[2];
            energyAvg+=dane[3];
            lifeAvg+=dane[4];
            childAvg+=dane[5];
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

            sb.append( "Avg," );
            sb.append( animalsAvg/days );
            sb.append( ",");
            sb.append( grassAvg/days );
            sb.append( ",");
            sb.append( energyAvg/days );
            sb.append( ",");
            sb.append( lifeAvg/days );
            sb.append( ",");
            sb.append( childAvg/days );
            sb.append( ",");

            writer.append(sb.toString());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
