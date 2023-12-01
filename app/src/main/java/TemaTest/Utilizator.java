package TemaTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Utilizator {
    private String username, parola;
    private int likes; //suma like-uri postări + comentarii
    private int followers;
    static ArrayList<Utilizator> UsersArray = new ArrayList<>();
    public Utilizator(String username, String parola) {
        this.username = username;
        this.parola = parola;
        this.likes = 0;
        this.followers = 0;
    }

    public String getUsername() {
        return username;
    }
    public String getParola() {
        return parola;
    }
    public int getLikes() {
        return likes;
    }
    public int getUserFollowers() {
        return followers;
    }
    public void incrementLikes() {
        this.likes++;
    }
    public void decrementLikes() {
        this.likes--;
    }
    public void incrementFollowers() {
        this.followers++;
    }
    public void decrementFollowers() {
        this.followers--;
    }


    @Override
    public String toString() {
        return this.username; // Pt ca altfel afisa Utilizator@352ff4da
    }

    public static void createSystemUser(java.lang.String[] args) {
        // "-u ‘username’"     "-p ‘password’"

        //1. Paramentrul -u nu este furnizat
        if (args.length == 0) {
            System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
            return;
        }
        //2. Paramentrul -p nu este furnizat
        if(args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
            return;
        }
        //Utilizator valid
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        Utilizator newUser = createUser(extractedUsername, extractedParola);
        //3. Utilizatorul există în sistem
        if(verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
            return;
        }
        //4. Totul a mers bine
        //Extragem username si parola
        writeUserToFile(newUser, "Users.txt");
        UsersArray.add(newUser);
        System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
    }
    public static Utilizator createUser (String username, String parola) {
        return new Utilizator(username, parola);
    }
    public static void writeUserToFile(Utilizator User, String file) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write(User.username + "," + User.parola + "\n");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeFollowersToFile(String usFollows, String usFollowed, String file) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
            fileOut.write(usFollows + "FOLLOWS" + usFollowed + "\n");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean verifyUserByUsernameFILE(String Username, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null){
                String[] credentials = line.split(",");
                if(Username.equals(credentials[0]))
                    return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean verifyUserByUsernameARRAY(String Username) {
        if(UsersArray.isEmpty())
            return false;
        for(Utilizator utilizator : UsersArray)
            if(utilizator.username.equals(Username))
                return true;
        return false;
    }

    public static Utilizator getUserByUsernameFILE(String Username) {
        if(FileUtils.isEmptyFile("Users.txt"))
            return null;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("Users.txt"));
            String line;
            while ((line = fileIn.readLine()) != null){
                String[] parts = line.split(",");
                if(Username.equals(parts[0]))
                    return createUser(Username, parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Utilizator getUserByUsernameARRAY(String Username) {
        if(UsersArray.isEmpty())
            return null;
        for(Utilizator utilizator : UsersArray)
            if(utilizator.username.equals(Username))
                return utilizator;
        return null;
    }

    public static boolean verifyUserByCredentialsFILE(Utilizator User, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null){
                String[] credentials = line.split(",");
                if(User.username.equals(credentials[0]) && User.parola.equals(credentials[1]))
                    return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyUserByCredentialsARRAY(Utilizator User) {
        if(UsersArray.isEmpty())
            return false;
        for(Utilizator utilizator : UsersArray)
            if(utilizator.username.equals(User.username) && utilizator.parola.equals(User.parola))
                return true;
        return false;
    }
    public static void createSystemFollowers(java.lang.String[] args) {
        //"-u 'test'", "-p 'test'", "-username 'test2'"
        //1. -u, -p nu este furnizat
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        //2. Username nu există, sau username și parola sunt greșite
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        if(!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Username-ul de urmărit nu a fost găsit
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No username to follow was provided'}");
            return;
        }
        String userToFollow = args[2].substring(11, args[2].length()-1);
        //4. Username-ul de urmărit nu este corect
        if(!verifyUserByUsernameFILE(userToFollow, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
            return;
        }
        //sau acest username este deja urmărit
        if(verifyAlreadyFollowed(extractedUsername, userToFollow, "Followers.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
            return;
        }
        //totul a mers bine
        writeFollowersToFile(extractedUsername, userToFollow, "Followers.txt");
        Utilizator foundUser = getUserByUsernameARRAY(userToFollow);
        if(foundUser == null) {
            System.out.println("Eroare la cautarea utilizatorului");
            System.exit(1);
        }
        foundUser.incrementFollowers();
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");

    }

    public static void unfollowUserByUsername(java.lang.String[] args) {
        //"-unfollow-user-by-username", "-u 'test'", "-p 'test'", "-username 'test2'"
        //1. -u, -p nu este furnizat
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        //2. Username nu există, sau username și parola sunt greșite
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        if(!Utilizator.verifyUserByCredentialsFILE(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Username-ul pentru unfollow nu a fost găsit
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No username to unfollow was provided'}");
            return;
        }
        String userToUnfollow = args[2].substring(11, args[2].length()-1);
        //4. Username-ul de urmărit nu este corect
        if(!verifyUserByUsernameFILE(userToUnfollow, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
            return;
        }
        //sau acest username este deja unfollowed
        if(!verifyAlreadyFollowed(extractedUsername, userToUnfollow, "Followers.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
            return;
        }
        //Totul a mers bine
        deleteFollower(extractedUsername, userToUnfollow);
        Utilizator foundUser = getUserByUsernameARRAY(userToUnfollow);
        if(foundUser == null) {
            System.out.println("Eroare la cautarea utilizatorului");
            System.exit(1);
        }
        foundUser.decrementFollowers();
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    public static void deleteFollower(String usFollows, String usFollowed) {
        File inputFile = new File("Followers.txt");
        File temporaryFile = new File("TempFile.txt");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String users[] = line.split("FOLLOWS");
                if(!(users[0].equals(usFollows) && users[1].equals(usFollowed)))
                    writer.write(line + "\n");
            }
            boolean s = temporaryFile.renameTo(inputFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean verifyAlreadyFollowed(String usFollows, String usFollowed, String file) {
        if(FileUtils.isEmptyFile(file))
            return false;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null){
                String users[] = line.split("FOLLOWS");
                if(usFollows.equals(users[0]) && usFollowed.equals(users[1]))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getMostLikedUsers(String[] args) {
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
        ArrayList<Utilizator> mostLikedUsers = UsersArray;
        Collections.sort(mostLikedUsers, Comparator.comparingInt(Utilizator::getLikes).reversed());
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printMostLikedUsers(mostLikedUsers);
        System.out.println("]}");
    }

    public static void getFollowing (String[] args) {
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
        ArrayList<Utilizator> following = getFollowingArray(extractedUsername, "Followers.txt");
        if(following == null) {
            System.out.println("Eroare la cautarea follower-ilor");
            System.exit(1);
        }
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printFollowingUsers(following);
        System.out.println("]}");
    }
    public static ArrayList<Utilizator> getFollowingArray(String username, String file) {
        ArrayList<Utilizator> following = new ArrayList<>();
        if(FileUtils.isEmptyFile(file))
            return null;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null){
                String users[] = line.split("FOLLOWS");
                if(username.equals(users[0])) {
                    Utilizator followed = getUserByUsernameARRAY(users[1]);
                    following.add(followed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return following;
    }

    public static void getFollowers (String[] args) {
        //"-u 'test'", "-p 'test'", "-username 'test'"
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
        //3. Username-ul pentru unfollow nu a fost găsit
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No username to list followers was provided'}");
            return;
        }
        String givenUser = args[2].substring(11, args[2].length()-1);
        //4. Username-ul de urmărit nu este corect
        if(!verifyUserByUsernameFILE(givenUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to list followers was not valid'}");
            return;
        }
        //5. Totul a mers bine
        ArrayList<Utilizator> followers = getFollowersArray(givenUser, "Followers.txt");
        if(followers == null) {
            System.out.println("Eroare la cautarea follower-ilor");
//            System.exit(1);
            return;
        }
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printFollowingUsers(followers);
        System.out.println("]}");
    }
    public static ArrayList<Utilizator> getFollowersArray(String username, String file) {
        ArrayList<Utilizator> followers = new ArrayList<>();
        if(FileUtils.isEmptyFile(file))
            return null;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null){
                String users[] = line.split("FOLLOWS");
                if(username.equals(users[1])) {
                    Utilizator following = getUserByUsernameARRAY(users[0]);
                    followers.add(following);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return followers;
    }

    public static void getMostFollowedUsers(String[] args) {
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
        ArrayList<Utilizator> mostFollowedUsers = UsersArray;
        Collections.sort(mostFollowedUsers, Comparator.comparingInt(Utilizator::getUserFollowers).reversed());
        System.out.print("{ 'status' : 'ok', 'message' : [");
        FileUtils.printMostFollowedUsers(mostFollowedUsers);
        System.out.println(" ]}");
    }

}