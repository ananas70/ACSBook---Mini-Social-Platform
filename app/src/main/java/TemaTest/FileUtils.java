package TemaTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*Clasa folosita pentru a implementa metode ce țin de afisarea datelor conform formatelor specificate, precum si de manipularea fisierelor (copiere, stergere)*/
public class FileUtils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
    public static void printPostDetails(ArrayList<Postare> PostsArray) {
        //formats as requested in tests
        for(Postare postare : PostsArray){
            System.out.print("{'post_id' : '"+postare.getId()+"', 'post_text' : '"+postare.getText()+"', 'post_date' : '" + dateFormat.format(postare.getTimestamp())+ "'}");
            if(!(PostsArray.indexOf(postare) == PostsArray.size()-1))
                System.out.print(",");
        }
    }
    public static void printPostComments(ArrayList<Comentariu> CommentsArray) {
        //formats as requested in tests
        //       System.out.print("{'comment_id' : '1' ," +" 'comment_text' : 'Felicitari', 'comment_date' : '" + currentDateAsString + "', " +"'username' : 'test2', 'number_of_likes' : '0'}");
        System.out.print("'comments' : [");
        for (Comentariu comentariu : CommentsArray) {
            System.out.print("{'comment_id' : '"+comentariu.getId()+"' ," +" 'comment_text' : '"+comentariu.getText()+"', 'comment_date' : '" + dateFormat.format(comentariu.getTimestamp()) + "', " +"'username' : '"+comentariu.getUsername()+"', 'number_of_likes' : '"+comentariu.getLikes()+"'}");
            if(!(CommentsArray.indexOf(comentariu) == CommentsArray.size()-1))
                System.out.print(",");
        }

    }
    public static void printMostLikedPosts(ArrayList<Postare> PostsArray) {
        //formats as requested in tests
        for(Postare postare : PostsArray){
            System.out.print("{'post_id' : '"+postare.getId()+"','post_text' : '"+postare.getText()+"', 'post_date' : '"+ dateFormat.format(postare.getTimestamp()) + "', 'username' : '"+postare.getUsername()+"', 'number_of_likes' : '"+postare.getLikes()+"' }");
            if(!(PostsArray.indexOf(postare) == PostsArray.size()-1))
                System.out.print(",");
        }
    }
    public static void printMostCommentedPosts(ArrayList<Postare> PostsArray) {
        //formats as requested in tests

        for(Postare postare : PostsArray){
            System.out.print("{'post_id' : '"+postare.getId()+"','post_text' : '"+postare.getText()+"', 'post_date' : '"+ dateFormat.format(postare.getTimestamp()) + "', 'username' : '"+postare.getUsername()+"', 'number_of_comments' : '"+postare.getCommentsCounter()+"' }");
            if(!(PostsArray.indexOf(postare) == PostsArray.size()-1))
                System.out.print(",");
        }
    }
    public static void printMostLikedUsers(ArrayList<Utilizator> UsersArray) {
        //formats as requested in tests
        //{'username' : 'test2','number_of_likes' : '3' }
        boolean isFirst = true;
        for(Utilizator utilizator : UsersArray){
            if(!isFirst && utilizator.getLikes()!=0)
                System.out.print(",");
            if(!(utilizator.getLikes() == 0))
             System.out.print("{'username' : '"+utilizator.getUsername()+"','number_of_likes' : '"+utilizator.getLikes()+"' }");
            isFirst = false;
        }
    }
    public static void printMostFollowedUsers(ArrayList<Utilizator> UsersArray) {
        //formats as requested in tests
        boolean isFirst = true;
        for(Utilizator utilizator : UsersArray){
            if(!isFirst && utilizator.getUserFollowers()!=0)
                System.out.print(",");
            if(!(utilizator.getUserFollowers() == 0))
                System.out.print("{'username' : '"+utilizator.getUsername()+"','number_of_followers' : ' "+utilizator.getUserFollowers()+"' }");
            isFirst = false;
        }
    }
    public static void printFollowingsPosts(ArrayList<Postare> PostsArray) {
        //formats as requested in tests
        for(Postare postare : PostsArray){
            System.out.print("{'post_id' : '"+postare.getId()+"','post_text' : '"+postare.getText()+"', 'post_date' : '"+ dateFormat.format(postare.getTimestamp()) + "', 'username' : '"+postare.getUsername()+"' }");
            if(!(PostsArray.indexOf(postare) == PostsArray.size()-1))
                System.out.print(",");
        }
    }
    public static void printFollowingUsers(ArrayList<Utilizator> UsersArray) {
        //formats as requested in tests

        for(Utilizator utilizator : UsersArray){
            System.out.print("'"+utilizator.getUsername()+"'");
            if(!(UsersArray.indexOf(utilizator) == UsersArray.size()-1))
                System.out.print(",");
            else System.out.print(" ");
        }
    }
    public static void deleteFileContents(String filename) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(filename));
            fileOut.write("");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteLineFromFile(String text, String file) {
        //Sterge linia gasita in fisier
        File inputFile = new File(file);
        File temporaryFile = new File("TempFile.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            writer.write("");
            String line;
            while ((line = reader.readLine()) != null)
                if (!line.equals(text))
                    writer.write(line + "\n");
            FileUtils.copyFile("TempFile.txt",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
