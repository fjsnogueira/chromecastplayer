/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.init;

import io.neocdtv.simpleplayer.ui.PlayerUI;
import io.neocdtv.service.StreamingService;
import io.neocdtv.simpleplayer.player.chromecast.ChromecastServiceDiscovery;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author xix
 */
public class Starter {    
    private final static Logger LOGGER = Logger.getLogger(Starter.class.getName());

    public static void main(String[] args) throws Exception {
        ChromecastServiceDiscovery.start();
        configureLookAndFeel();
        configureAndStartStreamingService();
        createAndShowGui();
    }

    private static void createAndShowGui() throws InvocationTargetException, InterruptedException {
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    new PlayerUI();
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
}
