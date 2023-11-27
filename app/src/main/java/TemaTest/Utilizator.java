package TemaTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

    public static void extractUserCredentials(java.lang.String[] args) {
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
        //3. Utilizatorul există în sistem


        //4. Totul a mers bine
        //Extragem username si parola
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        Utilizator newUser = createUser(extractedUsername, extractedParola);
        writeUserToFile(newUser, "Users.txt");
        System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
    }

        public static Utilizator createUser (String username, String parola) {
            return new Utilizator(username, parola);
        }
        public static void writeUserToFile(Utilizator User, String file) {
            try {
                BufferedWriter fileOut = new BufferedWriter(new FileWriter(file));
                fileOut.write(User.username + "," + User.parola);
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }