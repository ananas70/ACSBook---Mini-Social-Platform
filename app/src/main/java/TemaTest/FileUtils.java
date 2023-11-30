package TemaTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileUtils {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static void copyFile(String sourceFilePath, String destinationFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printContent(String file){
        try{
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmptyFile(String filename) {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            if (fileIn.read() == -1)
                return true;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return false;
    }

    public static void myprintArrayList(ArrayList<Postare> PostsArray) {
        for(Postare postare : PostsArray)
            System.out.println(postare.getUsername() + postare.getText() + postare.getTimestamp() + postare.getId());
    }

    public static void printArrayList(ArrayList<Postare> PostsArray) {
        for(Postare postare : PostsArray){
            System.out.print("{'post_id' : '"+postare.getId()+"', 'post_text' : '"+postare.getText()+"', 'post_date' : '" + dateFormat.format(postare.getTimestamp())+ "'}");
            if(!(PostsArray.indexOf(postare) == PostsArray.size()-1))
                System.out.print(",");
    }

    }

}
