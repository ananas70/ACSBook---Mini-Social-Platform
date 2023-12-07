package TemaTest;

import java.io.*;

public interface Likeable {
    void like(java.lang.String[] args);
    void unlike(java.lang.String[] args);
    default boolean verifyAlreadyLiked(String userLikes, int givenId, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null) {
                //testLIKES1
                String[] parts = line.split("LIKES");
                int foundId = Integer.parseInt(parts[1]);
                if (userLikes.equals(parts[0]) && givenId == foundId)
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    default void unlikeFromFile(String userLikes, int givenId, String file) {
        File inputFile = new File(file);
        File temporaryFile = new File("TempFile.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            String line;
            while ((line = reader.readLine()) != null){
                //"userLIKESid"
                String[] parts = line.split("LIKES");
                if ((parts[0].equals(userLikes) && Integer.parseInt(parts[1]) == givenId))
                    writer.write(line + "\n");
            }
            FileUtils.copyFile("TempFile.txt", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
