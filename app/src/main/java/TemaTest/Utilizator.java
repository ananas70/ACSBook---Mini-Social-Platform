package TemaTest;

import java.io.*;

public class Utilizator {
    private String username, parola;

    public Utilizator(String username, String parola) {
        this.username = username;
        this.parola = parola;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
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
        if(verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
            return;
        }
        //4. Totul a mers bine
        //Extragem username si parola
        writeUserToFile(newUser, "Users.txt");
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
    public static boolean verifyUserByUsername(String Username, String file) {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            if (fileIn.read() == -1)
                return false;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static boolean verifyUserByCredentials(Utilizator User, String file) {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            if (fileIn.read() == -1)
                return false;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if(!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
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
        if(!verifyUserByUsername(userToFollow, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
            return;
        }
        //sau acest username este deja urmărit
        if(searchAlreadyFollowed(extractedUsername, userToFollow, "Followers.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to follow was not valid'}");
            return;
        }
        //totul a mers bine
        writeFollowersToFile(extractedUsername, userToFollow, "Followers.txt");
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
        if(!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
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
        if(!verifyUserByUsername(userToUnfollow, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
            return;
        }
        //sau acest username este deja unfollowed
        if(!searchAlreadyFollowed(extractedUsername, userToUnfollow, "Followers.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'The username to unfollow was not valid'}");
            return;
        }
        //Totul a mers bine
        deleteFollower(extractedUsername, userToUnfollow);
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

    public static boolean searchAlreadyFollowed(String usFollows, String usFollowed, String file) {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            if (fileIn.read() == -1)
                return false;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}