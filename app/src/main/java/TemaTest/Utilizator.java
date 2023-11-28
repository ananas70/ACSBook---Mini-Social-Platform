package TemaTest;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
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
        //Utilizator valid
        String extractedUsername = args[0].substring(4, args[0].length()-1);
        String extractedParola = args[1].substring(4, args[1].length()-1);
        Utilizator newUser = createUser(extractedUsername, extractedParola);
        //3. Utilizatorul există în sistem
        if(!verifyUserAlreadyExists(newUser, "Users.txt")) {
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
                BufferedWriter fileOut = new BufferedWriter(new FileWriter(file));
                fileOut.write(User.username + "," + User.parola);
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static boolean verifyUserAlreadyExists(Utilizator User, String file) {
            try {
                BufferedReader fileIn = new BufferedReader(new FileReader(file));
                String line;
                while ((line = fileIn.readLine()) != null){
                    String[] credentials = line.split(",");
                    if(User.username.equals(credentials[0]) || User.parola.equals(credentials[1]))
                        return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        return true;
        }



    }