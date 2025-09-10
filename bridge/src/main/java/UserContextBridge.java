/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.bridge;

/**
 *
 * @author S2026080
 */
public final class UserContextBridge {

    private static String currentUser;

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser != null ? currentUser : "system";
    }
}
