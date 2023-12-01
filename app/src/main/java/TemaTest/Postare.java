package TemaTest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Postare implements Likeable {
    private Utilizator user;
    private String text;
    private int likes, id, commentsCounter;
    private Date timestamp;
    private static int idCounter = 0;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static ArrayList<Postare> PostsArray = new ArrayList<>();

    public Postare() {
    }

    public Postare(Utilizator user, String text) {
        this.user = user;
        this.text = text;
        this.likes = 0;
        this.timestamp = new Date();
        this.commentsCounter = 0;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return user.getUsername();
    }
    public Utilizator getUser() {
        return user;
    }

    public String getparola() {
        return user.getParola();
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public int getCommentsCounter() {
        return commentsCounter;
    }

    public void incrementCommentsCounter() {
        this.commentsCounter++;
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

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
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
        Postare post = new Postare(newUser, extractedText);
        PostsArray.add(post);
        writePostToFile(post, "Posts.txt");
//        FileUtils.printArrayList(PostsArray);
        System.out.println("{ 'status' : 'ok', 'message' : 'Post added successfully'}");
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

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
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
        String parts[] = foundPost.split(","); //textul propriu-zis
        Postare postare = new Postare(newUser,parts[2].substring(5));
        deletePostFromArrayList(postare);
        System.out.println("{ 'status' : 'ok', 'message' : 'Post deleted successfully'}");
    }

  public static void deletePostFromArrayList(Postare postare) {
        for(Postare aux : PostsArray)
            if(aux.getUsername().equals(postare.getUsername()) && aux.getText().equals(postare.getText())) {
                PostsArray.remove(aux);
                return;
            }
  }

    public static void writePostToFile(Postare postare, String file) {
        //empty file - reset id
        idCounter++;
       if(FileUtils.isEmptyFile(file))
           idCounter = 1;
       postare.id = idCounter;
       try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write("USER:" + postare.getUsername() + ",ID:" + idCounter + ",POST:" + postare.getText() + '\n');
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

    public static Postare getPostByIdARRAYLIST(int givenId) {
        if(PostsArray == null){
            System.out.println("Eroare la accesarea PostsArray");
            return null;
        }
        for(Postare post : PostsArray)
            if(post.getId() == givenId)
                return post;
        return null;
    }


    public static Postare getPostByIdFILE(int givenId) {
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
                    Utilizator user = Utilizator.getUserByUsernameFILE(username);
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

        if (!Utilizator.verifyUserByCredentialsARRAY(newUser)) {
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
        String emptyString = "", foundPostLine;
        foundPostLine = (verifyPostById(givenId, "Posts.txt"));
        if (foundPostLine.equals(emptyString)) {
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
//        this.likes++;
        Postare foundPost = getPostByIdARRAYLIST(givenId);
        if(foundPost == null) {
            System.out.println("Eroare la cautarea postarii in baza de date");
            System.exit(1);
        }
        foundPost.likes++;
//        foundPost.user.incrementLikes(); DE CE NU MERGE? PENTRU CA NU E REFERINTA ALUIA BRE
        incrementUserLikes(foundPost);
        writePostLikeToFile(extractedUsername, givenId, "PostLikes.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");

    }

    public static void incrementUserLikes(Postare foundPost) {
        for(Utilizator utilizator : Utilizator.UsersArray)
            if(utilizator.getUsername().equals(foundPost.user.getUsername()) && utilizator.getParola().equals(foundPost.user.getParola()))
                utilizator.incrementLikes();
    }

    public static void decrementUserLikes(Postare foundPost) {
        for(Utilizator utilizator : Utilizator.UsersArray)
            if(utilizator.getUsername().equals(foundPost.user.getUsername()) && utilizator.getParola().equals(foundPost.user.getParola()))
                utilizator.decrementLikes();
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

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
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
        String emptyString = "", foundPostLine;
        foundPostLine = (verifyPostById(givenId, "Posts.txt"));
        if (foundPostLine.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
            return;
        }
        //Postarea pentru unlike nu este corecta (sau această postare este deja unliked)
        if (!verifyPostAlreadyLiked(extractedUsername, givenId, "PostLikes.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to unlike was not valid'}");
            return;
        }
        //Totul a mers bine
        Postare foundPost = getPostByIdARRAYLIST(givenId);
        if(foundPost == null) {
            System.out.println("Eroare la cautarea postarii in baza de date");
            System.exit(1);
        }
        foundPost.likes--;
        decrementUserLikes(foundPost);
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

    public static void getUserPosts(java.lang.String[] args){
        //"-u 'test'", "-p 'test'", "-username 'test2'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Username-ul pentru listare postări nu a fost găsit
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No username to list posts was provided'}");
            return;
        }
        //4. Username-ul pentru listare postări nu este corect (sau acest username este deja unfollowed)
        String followedUser = args[2].substring(11, args[2].length()-1);
        if(!Utilizator.verifyUserByUsernameFILE(followedUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to list posts was not valid'}");
            return;
        }
        if(!Utilizator.verifyAlreadyFollowed(extractedUsername, followedUser, "Followers.txt")){
            System.out.println("{ 'status' : 'error', 'message' : 'The username to list posts was not valid'}");
            return;
        }
        //5. Totul a mers bine
        //bagam toate postarile intr-un alt ArrayList
        ArrayList<Postare> userPosts = new ArrayList<>();
        for(Postare post : PostsArray) {
            if(post.getUsername().equals(followedUser))
                userPosts.add(post);
        }
        //sortare yayy
        Collections.sort(userPosts, Collections.reverseOrder(Comparator.comparing(Postare::getTimestamp)));
        //afisare yayy
        System.out.print("{'status' : 'ok', 'message' :" + " [");
        FileUtils.printPostDetails(userPosts);
        System.out.print("]}");
    }

    public static void getPostDetails(java.lang.String[] args){
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

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Identificatorul pentru postare nu a fost găsit
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No post identifier was provided'}");
            return;
        }
        //4. Identificatorul pentru postare nu este corect (sau acest username este deja unfollowed)
        int givenId = Integer.parseInt(args[2].substring(10, args[2].length()-1));
//        String emptyString = "";
//        if(verifyPostById(givenId, "Posts.txt").equals(emptyString)){
//            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier was not valid'}");
//            return;
//        }

        Postare post = new Postare();
        post = getPostByIdARRAYLIST(givenId);
        if(post == null) {
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier was not valid'}");
            return;
        }
        if(!(extractedUsername.equals(post.getUsername())))
            if(!Utilizator.verifyAlreadyFollowed(extractedUsername, post.getUsername(), "Followers.txt")){
//            System.out.println("u1: "+extractedUsername + " u2:" + post.getUsername());
            System.out.println("{ 'status' : 'error', 'message' : 'The post identifier was not valid'}");
            return;
        }
        //5. Totul a mers bine
        System.out.print("{'status' : 'ok', 'message' : [{'post_text' : '"+post.text+"', 'post_date' :'" + dateFormat.format(post.timestamp) + "', 'username' : '"+post.user+"', 'number_of_likes' :" +" '"+post.likes+"', ");
//       System.out.print("'comments' : [{'comment_id' : '1' ," +" 'comment_text' : 'Felicitari', 'comment_date' : '" + currentDateAsString + "', " +"'username' : 'test2', 'number_of_likes' : '0'}] }] }");
        //ne trebuie un array cu comentariile
        ArrayList <Comentariu> postComments = Comentariu.getPostComments(post);
        //sortare yayy
        Collections.sort(postComments, Collections.reverseOrder(Comparator.comparing(Comentariu::getTimestamp)));
        //afisare yayy
        FileUtils.printPostComments(postComments);
        System.out.print("] }] }");
    }

    public static void getMostLikedPosts(String[] args) {
        //"-u 'test'", "-p 'test'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Totul a mers bine
        ArrayList <Postare> mostLikedPosts = PostsArray;
        Collections.sort(mostLikedPosts, Comparator.comparingInt(Postare::getLikes).reversed());
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printMostLikedPosts(mostLikedPosts);
        System.out.println(" ]}");
    }

    public static void getMostCommentedPosts(String[] args) {
        //"-u 'test'", "-p 'test'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Totul a mers bine
        ArrayList <Postare> mostCommentedPosts = PostsArray;
        Collections.sort(mostCommentedPosts, Comparator.comparingInt(Postare::getCommentsCounter).reversed());
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printMostCommentedPosts(mostCommentedPosts);
        System.out.println("]}");
    }

    public static void getFollowingsPosts(java.lang.String[] args){
        //"-u 'test'", "-p 'test'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length() - 1);
        String extractedParola = args[1].substring(4, args[1].length() - 1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if (!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Totul a mers bine
        //bagam toate postarile intr-un alt ArrayList
        ArrayList<Postare> followingsPosts = new ArrayList<>();
        for(Postare post : PostsArray) {
            if(Utilizator.verifyAlreadyFollowed(extractedUsername, post.getUsername(), "Followers.txt"))
                followingsPosts.add(post);
        }
        //sortare yayy
        Collections.sort(followingsPosts, Collections.reverseOrder(Comparator.comparing(Postare::getTimestamp)));
        //afisare yayy
        System.out.print("{'status' : 'ok', 'message' :" + " [");

        FileUtils.printFollowingsPosts(followingsPosts);
        System.out.print("]}");
    }


}