package TemaTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void myprintArrayListPosts(ArrayList<Postare> PostsArray) {
        for(Postare postare : PostsArray)
            System.out.println(postare.getUsername() + postare.getText() + postare.getTimestamp() + postare.getId());
    }
    public static void myprintArrayListComments(ArrayList<Comentariu> CommentsArray) {
        System.out.println("MEAP");
        for(Comentariu comentariu: CommentsArray)
            System.out.println(comentariu.getUsername() + comentariu.getText() + comentariu.getTimestamp());
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

}
