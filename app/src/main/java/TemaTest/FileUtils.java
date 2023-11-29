package TemaTest;

import java.io.*;

public class FileUtils {

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

}
