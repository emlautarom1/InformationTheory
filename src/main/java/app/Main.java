package app;

import com.sun.javafx.application.LauncherImpl;

public class Main {
    public static void main(String[] args) {
        LauncherImpl.launchApplication(Bootstrap.class, SplashLauncher.class, args);
    }
}
