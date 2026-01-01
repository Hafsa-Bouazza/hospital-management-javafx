package utils;

import java.util.prefs.Preferences;

public class ThemeManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
    private static boolean darkMode = prefs.getBoolean("darkMode", false); // valeur au démarrage

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean value) {
        darkMode = value;
        prefs.putBoolean("darkMode", value); // sauvegarde
    }

    public static String getCurrentThemeStylesheet() {
        return darkMode ? "/styles/dark.css" : "/styles/light.css";
    }
}
