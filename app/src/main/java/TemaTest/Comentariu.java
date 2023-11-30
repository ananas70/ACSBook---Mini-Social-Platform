package TemaTest;

import java.io.*;

public class Comentariu implements Likeable {
    private Utilizator user;
    private String text;
    private int likes;
    private Postare parentPost;

    private static int id = 0;

    public Comentariu(){}
    public Comentariu(Utilizator User, String comment, Postare parentPost){
        this.user = User;
        this.text = comment;
        this.likes = 0;
        this.parentPost = parentPost;
    }
    public String getUsername() {
        return user.getUsername();
    }

    public String getparola() {
        return user.getParola();
    }

    public String getText() {
        return text;
    }

    public int getLikes() {
        return likes;
    }

    public Postare getParentPost() {
        return parentPost;
    }

    public static void createSystemComment(java.lang.String[] args) {
        //"-u 'test'", "-p 'test'",  "-post-id '1'", "-text 'Sunt interese mari'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }

        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);
        String extractedText;
        if (args.length == 4)
            extractedText = args[3].substring(7, args[3].length() - 1);
        else
            extractedText = "";
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Comentariul nu include nici un text
        if (args.length <= 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");
            return;
        }
        int givenId = Integer.parseInt(args[2].substring(10, args[2].length() - 1));
        Postare parentPost = Postare.getPostById(givenId);
        //4.Comentariul are peste 300 de caractere
        if (extractedText.length() > 300) {
            System.out.println("{ 'status' : 'error', 'message' : 'Comment text length exceeded'}");
            return;
        }
        //5.Totul a mers bine
        Comentariu comentariu = new Comentariu(newUser,extractedText,parentPost);
        writeCommentToFile(comentariu, "Comments.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Comment added successfully'}");
    }

    public static void writeCommentToFile(Comentariu comentariu, String file) {
        id++;
        if(FileUtils.isEmptyFile(file))
            id=1;
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write("USER:" + comentariu.getUsername() + ",POST_ID:" + id + ",COMMENT_ID:" + id + ",COMMENT:" + comentariu.getText() + '\n');
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCommentById(java.lang.String[] args) {
        //"-u 'test2'", "-p 'test2'", "-id '1'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }

        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Id not provided
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No identifier was provided'}");
            return;
        }
        //4. Id not found
        int givenId = Integer.parseInt(args[2].substring(5, args[2].length() - 1));
        String emptyString = "", foundComment;
        foundComment = (verifyCommentById(givenId, "Comments.txt"));
        if (foundComment.equals(emptyString) || !(PermissionToDelete(foundComment, extractedUsername))) {
            System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
            return;
        }
        // 5. succes
        deleteCommentFromFile(foundComment);
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    public static boolean PermissionToDelete(String line, String username) {
        //line e USER:user,POST_ID:id,COMMENT_ID:id,COMMENT:text (o linie intreaga din comments)
        String parts[] = line.split(",");
            if(!(parts[0].substring(5).equals(username)))
                return false; // permission denied
        return true;
    }
    public static String verifyCommentById(int givenId, String file) {
        String emptyString = "";
        //empty file
        if(FileUtils.isEmptyFile(file))
            return emptyString;
        try {
            //USER:user,POST_ID:id,COMMENT_ID:id,COMMENT:text
            BufferedReader fileIn = new BufferedReader(new FileReader("Comments.txt"));
            String line;
            while ((line = fileIn.readLine()) != null) {
                String parts[] = line.split(",");
                if (Integer.parseInt(parts[2].substring(11)) == givenId)
                        return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyString;
    }

    public static void deleteCommentFromFile(String text) {
        File inputFile = new File("Comments.txt");
        File temporaryFile = new File("TempFile.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            writer.write("");
            String line;
            while ((line = reader.readLine()) != null)
                if (!line.equals(text))
                    writer.write(line + "\n");
            FileUtils.copyFile("TempFile.txt","Posts.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void like(String[] args) {
        //"-like-comment", "-u 'test'", "-p 'test'", "-comment-id '1'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Comment Id not provided
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No comment identifier to like was provided'}");
            return;
        }
        //4. Id not found
        int givenId = Integer.parseInt(args[2].substring(13, args[2].length() - 1));
        String emptyString = "", foundComment;
        foundComment = (verifyCommentById(givenId, "Comments.txt"));
        if (foundComment.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
            return;
        }
        //Comentariul de apreciat nu este corect (sau acest comenatriu este deja apreciat, sau este al utilizatorului curent)
//        if (verifyUserLikesHisComment(extractedUsername, givenId, "Comments.txt")) {
//            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
//            return;
//        }
        if (verifyCommentAlreadyLiked(extractedUsername, givenId, "CommentLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier to like was not valid'}");
            return;
        }
        //Totul a mers bine
        this.likes++;
        writeCommentLikeToFile(extractedUsername, givenId, "CommentLikes.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    public static boolean verifyUserLikesHisComment(String userLikes, int givenId, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
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
    public static void writeCommentLikeToFile(String userLikes, int givenId, String file) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write(userLikes + "LIKES" + givenId + "\n");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean verifyCommentAlreadyLiked(String userLikes, int givenId, String file) {
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


    @Override
    public void unlike(String[] args) {

    }
}
