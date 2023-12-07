package TemaTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Comentariu implements Likeable {
    private Utilizator user;
    private String text;
    private int likes, id;
    private Postare parentPost;
    private Date timestamp;
    private static int idCounter = 0;
    static ArrayList<Comentariu> CommentsArray = new ArrayList<>();

    public Comentariu(){}
    public Comentariu(Utilizator User, String comment, Postare parentPost){
        this.user = User;
        this.text = comment;
        this.likes = 0;
        this.parentPost = parentPost;
        this.timestamp = new Date();
    }
    public String getUsername() {
        return user.getUsername();
    }
    public String getText() {
        return text;
    }
    public int getLikes() {
        return likes;
    }
    public int getId() {
        return id;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public static void createSystemComment(java.lang.String[] args) {
        //"-u 'test'", "-p 'test'",  "-post-id '1'", "-text 'Sunt interese mari'"
        if(!Utilizator.verifyAuthenticated(args))
            return;
        Utilizator newUser = new Utilizator().createUserFromInput(args);
        if(newUser == null)
            return;
        String extractedText;
        if (args.length == 4)
            extractedText = args[3].substring(7, args[3].length() - 1);
        else
            extractedText = "";
        //3. Comentariul nu include nici un text
        if (args.length <= 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");
            return;
        }
        int givenId = Integer.parseInt(args[2].substring(10, args[2].length() - 1));
        Postare parentPost = new Postare().getPostById(givenId);
        if(parentPost == null) {
            System.out.println("Eroare la cautarea postarii in baza de date");
            System.exit(1);
        }
        //4.Comentariul are peste 300 de caractere
        if (extractedText.length() > 300) {
            System.out.println("{ 'status' : 'error', 'message' : 'Comment text length exceeded'}");
            return;
        }
        //5.Totul a mers bine
        Comentariu comentariu = new Comentariu(newUser,extractedText,parentPost);
        CommentsArray.add(comentariu);
        parentPost.incrementCommentsCounter();
        comentariu.writeCommentToFile(comentariu, "Comments.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Comment added successfully'}");
    }
    public void writeCommentToFile(Comentariu comentariu, String file) {
        idCounter++;
        if(FileUtils.isEmptyFile(file))
            idCounter=1;
        comentariu.id = idCounter;
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write("USER:" + comentariu.getUsername() + ",POST_ID:" + comentariu.parentPost.getId() + ",COMMENT_ID:" + comentariu.id + ",COMMENT:" + comentariu.getText() + '\n');
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteCommentById(java.lang.String[] args) {
        //"-u 'test2'", "-p 'test2'", "-id '1'"
        if(!Utilizator.verifyAuthenticated(args))
            return;
        Utilizator newUser = new Utilizator().createUserFromInput(args);
        if(newUser == null)
            return;
        // Id not provided
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No identifier was provided'}");
            return;
        }
        // Comment Id not found
        int givenId = Integer.parseInt(args[2].substring(5, args[2].length() - 1));
        String foundCommentLine;
        Comentariu foundComment = new Comentariu().getCommentById(givenId);
        if (foundComment == null|| !(foundComment.PermissionToDelete(foundComment, newUser.getUsername()))) {
            System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
            return;
        }
        foundCommentLine = (foundComment.getCommentLineById(givenId, "Comments.txt"));
        // Succes
        //Formatul din fisier = USER:username,POST_ID:id,COMMENT_ID:id,COMMENT:text
        foundComment.deleteCommentFromArrayList(foundComment);
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
        FileUtils.deleteLineFromFile(foundCommentLine, "Comments.txt");
    }
    public boolean PermissionToDelete(Comentariu comentariu, String username) {
        //Verifica daca username-urile coincid
        return comentariu.getUsername().equals(username);
    }
    public String getCommentLineById(int givenId, String file) {
        //Returneaza linia din fisier unde a fost gasit comentariul cu id-ul specificat
        String emptyString = "";
        //empty file
        if(FileUtils.isEmptyFile(file))
            return emptyString;
        try {
            //USER:user,POST_ID:id,COMMENT_ID:id,COMMENT:text
            BufferedReader fileIn = new BufferedReader(new FileReader("Comments.txt"));
            String line;
            while ((line = fileIn.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[2].substring(11)) == givenId)
                    return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyString;
    }
    public void deleteCommentFromArrayList(Comentariu comentariu) {
        for(Comentariu aux : CommentsArray)
            if(aux.getUsername().equals(comentariu.getUsername()) && aux.getText().equals(comentariu.getText())) {
                CommentsArray.remove(aux);
                return;
            }
    }
    private Comentariu getCommentById(int givenId) {
        if(CommentsArray == null){
            System.out.println("Eroare la accesarea PostsArray");
            return null;
        }
        for(Comentariu comentariu : CommentsArray)
            if(comentariu.getId() == givenId)
                return comentariu;
        return null;
    }
    @Override
    public void like(String[] args) {
        //"-like-comment", "-u 'test'", "-p 'test'", "-comment-id '1'"
        if(!Utilizator.verifyAuthenticated(args))
            return;
        Utilizator newUser = new Utilizator().createUserFromInput(args);
        if(newUser == null)
            return;
        // Comment Id not provided
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No comment identifier to like was provided'}");
            return;
        }
        // Id not found
        int givenId = Integer.parseInt(args[2].substring(13, args[2].length() - 1));
        Comentariu foundComment = new Comentariu().getCommentById(givenId);
        if(foundComment == null){
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
            return;
        }
        //Comentariul de apreciat nu este corect (sau acest comenatriu este deja apreciat, sau este al utilizatorului curent)
        if (verifyUserLikesHisComment(newUser.getUsername(), givenId)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
            return;
        }
        if (verifyAlreadyLiked(newUser.getUsername(), givenId, "CommentLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
            return;
        }
        //Totul a mers bine
        foundComment.likes++;
        incrementUserLikes(foundComment);
        writeCommentLikeToFile(newUser.getUsername(), givenId);
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }
    private boolean verifyUserLikesHisComment(String userLikes, int givenId) {
        if(FileUtils.isEmptyFile("Comments.txt"))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("Comments.txt"));
            String line;
            while ((line = fileIn.readLine()) != null) {
                //"USER:username,POST_ID:id,COMMENT_ID:id,COMMENT:text
                String[] parts = line.split(",");
                String userLiked = parts[0].substring(5);
                int likedId = Integer.parseInt(parts[1].substring(8));
                if (likedId == givenId && userLikes.equals(userLiked))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void writeCommentLikeToFile(String userLikes, int givenId) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter("CommentLikes.txt", true));
            fileOut.write(userLikes + "LIKES" + givenId + "\n");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void unlike(String[] args) {
        //"-like-comment", "-u 'test'", "-p 'test'", "-comment-id '1'"
        if(!Utilizator.verifyAuthenticated(args))
            return;
        Utilizator newUser = new Utilizator().createUserFromInput(args);
        if(newUser == null)
            return;
        //3. Comment Id not provided
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No comment identifier to unlike was provided'}");
            return;
        }
        //4. Id not found
        int givenId = Integer.parseInt(args[2].substring(13, args[2].length() - 1));
        Comentariu foundComment = getCommentById(givenId);
        if (foundComment == null) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to unlike was not valid'}");
            return;
        }
        //Comentariul de unlike nu este corect (sau acest comenatriu este deja unliked)
        if (!verifyAlreadyLiked(newUser.getUsername(), givenId, "CommentLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to unlike was not valid'}");
            return;
        }
        //Totul a mers bine
        foundComment.likes++;
        decrementUserLikes(foundComment);
        unlikeFromFile(newUser.getUsername(), givenId, "CommentLikes.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }
    public ArrayList<Comentariu> getPostComments(Postare parentPost) {
        ArrayList <Comentariu> postComments = new ArrayList<>();
        for(Comentariu comentariu : CommentsArray)
            if(comentariu.parentPost.equals(parentPost)){
                postComments.add(comentariu);
            }
        return postComments;
    }
    private void incrementUserLikes(Comentariu foundComment) {
        for(Utilizator utilizator : Utilizator.UsersArray)
            if(utilizator.getUsername().equals(foundComment.user.getUsername()) && utilizator.getParola().equals(foundComment.user.getParola()))
                utilizator.incrementLikes();
    }
    private void decrementUserLikes(Comentariu foundComment) {
        for(Utilizator utilizator : Utilizator.UsersArray)
            if(utilizator.getUsername().equals(foundComment.user.getUsername()) && utilizator.getParola().equals(foundComment.user.getParola()))
                utilizator.decrementLikes();
    }
}
