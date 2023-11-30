package TemaTest;

import java.io.*;

public class Postare implements Likeable {
    private Utilizator user;
    private String text;
    private int likes;
    private static int id = 0;

    public Postare() {
    }

    public Postare(Utilizator user, String text) {
        this.user = user;
        this.text = text;
        this.likes = 0;
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

    public static void createSystemPost(java.lang.String[] args) {
        //"-u 'test'", "-p 'test'", "-text 'Astazi ma simt bine'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }

        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);
        String extractedText;
        if (args.length == 3)
            extractedText = args[2].substring(7, args[2].length() - 1);
        else
            extractedText = "";
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);

        //2. Username nu există, sau username și parola sunt greșite

        Postare post = new Postare(newUser, extractedText);
        if (!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Postarea nu include nici un text
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");
            return;
        }

        //4.Postarea are peste 300 de caractere
        if (extractedText.length() > 300) {
            System.out.println("{ 'status' : 'error', 'message' : 'Post text length exceeded'}");
            return;
        }
        //5.Totul a mers bine
        writePostToFile(post, "Posts.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Post added successfully'}");
//        Utilizator.printContent("Posts.txt");
    }

    public static void deletePostById(java.lang.String[] args) {
        //"-u 'test'", "-p 'test'", "-id '1'"
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
        String extractedId = args[2].substring(5, args[2].length() - 1);
        int givenId = Integer.parseInt(extractedId);
        String emptyString = "", foundPost;
        foundPost = (verifyPostById(givenId, "Posts.txt"));
        if (foundPost.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
            return;
        }
        // 5. succes
        deletePostFromFile(foundPost);
        System.out.println("{ 'status' : 'ok', 'message' : 'Post deleted successfully'}");
    }

    public static void writePostToFile(Postare postare, String file) {
        id++;
        //empty file - reset id
       if(FileUtils.isEmptyFile(file))
           id=1;
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write("USER:" + postare.getUsername() + ",ID:" + id + ",POST:" + postare.getText() + '\n');
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String verifyPostById(int givenId, String file) {
        String emptyString = "";
        //empty file
        if(FileUtils.isEmptyFile(file))
            return emptyString;
        try {
            //"USER:username,ID:id,POST:postare
            BufferedReader fileIn = new BufferedReader(new FileReader("Posts.txt"));
            String line;
            while ((line = fileIn.readLine()) != null && givenId > 0) {
               String parts[] = line.split(",");
                if (Integer.parseInt(parts[1].substring(3)) == givenId)
                    return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyString;
    }

    public static Postare getPostById(int givenId) {
        //empty file
        if(FileUtils.isEmptyFile("Posts.txt"))
            return null;
        try {
            //"USER:username,ID:id,POST:postare
            BufferedReader fileIn = new BufferedReader(new FileReader("Posts.txt"));
            String line;
            while ((line = fileIn.readLine()) != null && givenId > 0) {
                String[] parts = line.split(",");
                int foundId = Integer.parseInt(parts[1].substring(3));
                if(givenId == foundId) {
                    String username = parts[0].substring(5);
                    Utilizator user = Utilizator.getUserByUsername(username);
                    if(user == null)
                    {
                        System.out.println("Eroare la gasirea utilizatorului in baza de date");
                        System.exit(1);
                    }
                    String text = parts[2].substring(5);
                    return new Postare(user, text);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void deletePostFromFile(String text) {
        File inputFile = new File("Posts.txt");
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
        //"-u 'test'", "-p 'test'", "-post-id '1'"
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
        //3. Post Id not provided
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No post identifier to like was provided'}");
            return;
        }
        //4. Post Id not found
        String extractedId = args[2].substring(10, args[2].length() - 1);
        int givenId = Integer.parseInt(extractedId);
        String emptyString = "", foundPost;
        foundPost = (verifyPostById(givenId, "Posts.txt"));
        if (foundPost.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to like was not valid'}");
            return;
        }
        //Postarea de apreciat nu este corectă (sau această postare este deja apreciată, sau este a utilizatorului curent)
        if(verifyUserLikesHisPost(extractedUsername, givenId, "Posts.txt")){
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to like was not valid'}");
            return;
        }
        if (verifyPostAlreadyLiked(extractedUsername, givenId, "PostLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to like was not valid'}");
            return;
        }
        //Totul a mers bine
        this.likes++;
        writePostLikeToFile(extractedUsername, givenId, "PostLikes.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");

    }

    public static boolean verifyUserLikesHisPost(String userLikes, int givenId, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null) {
                //"USER:user,ID:id,POST:post"
                String[] parts = line.split(",");
                String userLiked = parts[0].substring(5);
                String likedIdString = parts[1].substring(3);
                int likedId = Integer.parseInt(likedIdString);
                if (likedId == givenId && userLikes.equals(userLiked))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyPostAlreadyLiked(String userLikes, int givenId, String file) {
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

    public static void writePostLikeToFile(String userLikes, int givenId, String file) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write(userLikes + "LIKES" + givenId + "\n");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlike(String[] args) {
        //"-u 'test'", "-p 'test'", "-post-id '1'"
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
        //3. Post Id not provided
        if (args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No post identifier to unlike was provided'}");
            return;
        }
        //4. Post Id not found
        String extractedId = args[2].substring(10, args[2].length() - 1);
        int givenId = Integer.parseInt(extractedId);
        String emptyString = "", foundPost;
        foundPost = (verifyPostById(givenId, "Posts.txt"));
        if (foundPost.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
            return;
        }
        //Postarea pentru unlike nu este corecta (sau această postare este deja unliked)
        if (!verifyPostAlreadyLiked(extractedUsername, givenId, "PostLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
            return;
        }
        //Totul a mers bine
        this.likes--;
        unlikePostFromFile(extractedUsername, givenId);
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    private static void unlikePostFromFile(String userLikes, int givenId) {
        File inputFile = new File("PostLikes.txt");
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
            FileUtils.copyFile("TempFile.txt", "PostLikes.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}