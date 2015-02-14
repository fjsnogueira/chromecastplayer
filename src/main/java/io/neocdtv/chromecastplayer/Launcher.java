/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.chromecastplayer;

import io.neocdtv.service.StreamingService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.Status;

/**
 *
 * @author xix
 */
public class Launcher {
    
    private final static Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    private static final String CHROME_CAST_DEFAULT_APP_ID = "CC1AD845";
    private static Player player;
    private static ChromeCast chromeCast;
    private static NextTrackAnalyser nextTrackAnalyser;
    
    public static void main(String[] args) throws Exception {
        configureLookAndFeel();
        configureAndStartStreamingService();
        configureChromeCastAndStartDefaultPlayerApp();
        startGui();
        nextTrackAnalyser = new NextTrackAnalyser(chromeCast, player);
    }

    private static void startGui() throws InterruptedException, InvocationTargetException {
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    player = new Player(chromeCast);
                } catch (IOException | GeneralSecurityException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private static void configureAndStartStreamingService() throws Exception {
        StreamingService.start();
    }

    private static void configureLookAndFeel() throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException {
        LookAndFeelConfigurator.configure();
    }

    private static void configureChromeCastAndStartDefaultPlayerApp() throws IOException, GeneralSecurityException {
        chromeCast = new ChromeCast("Chromecast");
        chromeCast.connect();
        Status status = chromeCast.getStatus();
        if (chromeCast.isAppAvailable(CHROME_CAST_DEFAULT_APP_ID) && !status.isAppRunning(CHROME_CAST_DEFAULT_APP_ID)) {
            chromeCast.launchApp(CHROME_CAST_DEFAULT_APP_ID);
        }
    }
}
