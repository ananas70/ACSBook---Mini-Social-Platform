package TemaTest;

import java.io.*;

public class Postare {
    private Utilizator user;
    private String text;
    private static int id = 0;

    public Postare (){}
    public Postare(Utilizator user, String text) {
        this.user = user;
        this.text = text;
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

        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        String extractedText;
        if(args.length == 3)
            extractedText = args[2].substring(7,args[2].length() - 1);
        else
            extractedText = "";
        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);

        //2. Username nu există, sau username și parola sunt greșite

        Postare post = new Postare(newUser, extractedText);
        if(!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Postarea nu include nici un text
        if(args.length < 3) {
            System.out.println("{ 'status' : 'error', 'message' : 'No text provided'}");
            return;
        }

        //4.Postarea are peste 300 de caractere
        if(extractedText.length() > 300) {
            System.out.println("{ 'status' : 'error', 'message' : 'Post text length exceeded'}");
            return;
        }
        //5.Totul a mers bine
        writePostToFile(post, "Posts.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Post added successfully'}");
//        printPosts();
    }
    public static void deletePostById(java.lang.String[] args) {
        //–delete-post-by-id -u ‘my_username’ -p ‘my_password’ -id ‘post_ id1’
        //"-u 'test'", "-p 'test'", "-id '1'"
        //1. Paramentrii -u sau -p lipsa
        if (args.length == 0 || args.length == 1) {
            System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
            return;
        }
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);

        Utilizator newUser = Utilizator.createUser(extractedUsername, extractedParola);
        //2. Username nu există, sau username și parola sunt greșite

        if(!Utilizator.verifyUserByCredentials(newUser, "Users.txt")) {
            System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
            return;
        }
        //3. Id not found
        String extractedId = args[2].substring(5,args[2].length() - 1);
        int givenId = Integer.parseInt(extractedId);
        String emptyString = "", foundPost;
        foundPost = (verifyPostById(givenId, "Posts.txt"));
        if(foundPost.equals(emptyString)) {
            System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
            return;
        }
        // 4. succes
        deletePostFromFile(foundPost, "Posts.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'Post deleted successfully'}");
    }




    public static void writePostToFile(Postare postare, String file) {
        id++;
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(file, true));
//            fileOut.write("User:" + postare.getUsername() + "\n");
            fileOut.write(postare.getText() + '\n');
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String verifyPostById(int givenId, String file) {
        String emptyString = "";
        //empty file
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            if (fileIn.read() == -1)
                return emptyString;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            BufferedReader fileIn = new BufferedReader(new FileReader("Posts.txt"));
            String line;
            while ((line = fileIn.readLine()) != null && givenId > 0) {
                givenId--;
                if(givenId == 0)
                    return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyString;
    }

    public static void deletePostFromFile(String text, String file) {
        File inputFile = new File("myFile.txt");
        File temporaryFile = new File("myTempFile.txt");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            String line;
            while ((line = reader.readLine()) != null)
             if(!line.equals(text))
                writer.write(line + "\n");
            boolean s = temporaryFile.renameTo(inputFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void printPosts(){
        try{
            BufferedReader fileIn = new BufferedReader(new FileReader("Posts.txt"));
            String line;
            while ((line = fileIn.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}