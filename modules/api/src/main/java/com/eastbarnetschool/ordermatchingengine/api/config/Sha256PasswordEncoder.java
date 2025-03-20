package com.eastbarnetschool.ordermatchingengine.api.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Sha256PasswordEncoder implements PasswordEncoder {
    private static final int SALT_LENGTH = 16;

    // Generates a random 32 character salt and hashes
    // the password and the salt together
    // returns 'salt:hash'
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            String salt = generateSalt();
            String hashedPassword = hashPassword(rawPassword.toString(), salt);
            return salt + ":" + hashedPassword; // Combine salt and hash
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    // Checks if the provided encodedPassword matches the rawPassword
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String[] parts = encodedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            String salt = parts[0];
            String storedHash = parts[1];
            String computedHash = hashPassword(rawPassword.toString(), salt);
            return storedHash.equals(computedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    // Creates a SHA-256 hash for the password
    private String hashPassword(String password, String saltHex) throws NoSuchAlgorithmException {
        byte[] salt = hexToBytes(saltHex);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        md.update(password.getBytes());
        byte[] hash = md.digest();
        return bytesToHex(hash);
    }

    // converts a byte array to a hex string
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // converts a hexadecimal string to a byte array
    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) +
                    Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
