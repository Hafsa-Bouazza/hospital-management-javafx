package models;

import java.util.prefs.Preferences;

public class SessionManager {

    private static User currentUser;

    // Stocke l'utilisateur connecté dans la session
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Retourne l'utilisateur connecté (ou null si personne n'est connecté)
    public static User getCurrentUser() {
        return currentUser;
    }

    // Vide la session (déconnexion)
    public static void clearSession() {
        currentUser = null;
    }

    // Vérifie si le mot de passe entré correspond à celui de l'utilisateur connecté
    public static boolean verifyPassword(String enteredPassword) {
        if (currentUser == null) return false;
        String storedPassword = currentUser.getMotDePasse();
        if (storedPassword == null) return false;
        return enteredPassword.equals(storedPassword);
    }
    public static void saveThemePreference(boolean isDarkMode) {
        Preferences prefs = Preferences.userNodeForPackage(SessionManager.class);
        prefs.putBoolean("darkMode", isDarkMode);
    }

    public static boolean loadThemePreference() {
        Preferences prefs = Preferences.userNodeForPackage(SessionManager.class);
        return prefs.getBoolean("darkMode", false); // false par défaut (mode clair)
    }
}
