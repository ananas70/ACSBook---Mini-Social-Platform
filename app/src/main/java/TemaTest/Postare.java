package TemaTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
public class Postare {
    private Utilizator user;
    private String text;

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
    public static void extractPostCredentials(java.lang.String[] args) {
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
//        Utilizator.printUsers();
        if(!post.verifyCorrectUser(newUser, "Users.txt")) {
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



    }
    public boolean verifyCorrectUser(Utilizator User, String file) {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            if (fileIn.read() == -1)
                return false;
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Open a new reader to reset the file pointer
            BufferedReader fileIn = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileIn.readLine()) != null) {
                String[] credentials = line.split(",");
                if (User.getUsername().equals(credentials[0]) && User.getParola().equals(credentials[1]))
                    return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



}