package be.lifeisbananas.security.utility;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptSomePasswords {

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        String[] passwords = {"banana123", "warehouse123", "multi123"};
        for (String password : passwords) {
            System.out.printf("%s -> %s%n", password, hash(password));
        }
    }
}
