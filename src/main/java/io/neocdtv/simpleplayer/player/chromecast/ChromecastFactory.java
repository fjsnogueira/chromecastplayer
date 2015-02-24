/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player.chromecast;

import io.neocdtv.simpleplayer.ui.ComboBoxFactory;
import io.neocdtv.simpleplayer.ui.CombolistEntry;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import su.litvak.chromecast.api.v2.ChromeCast;

import su.litvak.chromecast.api.v2.MediaStatus;

/**
 *
 * @author xix
 */
public class ChromecastFactory {

    private final static Logger LOGGER = Logger.getLogger(ChromecastFactory.class.getName());
    private final static String CHROME_CAST_DEFAULT_APP_ID = "CC1AD845";
    private final static List<MediaStatusUpdateListener> listeners = new ArrayList<>();
    private static ChromeCast CHROME_CAST;
    private static MediaStatusUpdateThread MEDIA_STATUS_UPDATE_THREAD;

    public static ChromeCast instance() throws IOException, GeneralSecurityException, InterruptedException {
        if (CHROME_CAST == null) {
            LOGGER.log(Level.INFO, "initializing chromecast...");
            final String chromeCastDomainName = ((CombolistEntry)ComboBoxFactory.instance().getSelectedItem()).getValue();
            LOGGER.log(Level.INFO, "chromecast domain name: {0}", chromeCastDomainName);
            CHROME_CAST = new ChromeCast(chromeCastDomainName);
            LOGGER.log(Level.INFO, "chromecast: {0}", CHROME_CAST);
            CHROME_CAST.connect();
            if (!CHROME_CAST.getRunningApp().id.equals(CHROME_CAST_DEFAULT_APP_ID)) {
                CHROME_CAST.stopApp();
            }
            CHROME_CAST.launchApp(CHROME_CAST_DEFAULT_APP_ID);
            if (MEDIA_STATUS_UPDATE_THREAD == null) {
                listeners.add(new MediaStatusUpdateHandler());
                MEDIA_STATUS_UPDATE_THREAD = new MediaStatusUpdateThread();
            }
        }        
        return CHROME_CAST;
    }

    public static void shutdown() throws IOException {
        if (MEDIA_STATUS_UPDATE_THREAD != null) {
            MEDIA_STATUS_UPDATE_THREAD.cancel();
        }
        if (CHROME_CAST != null) {
            if (CHROME_CAST.isAppRunning(CHROME_CAST_DEFAULT_APP_ID)) {
                CHROME_CAST.stopApp();
            }
            CHROME_CAST.disconnect();
        }
    }

    private static class MediaStatusUpdateThread extends TimerTask {

        public MediaStatusUpdateThread() {
            Timer timer = new Timer();
            timer.schedule(this, 0, 1000);
        }

        @Override
        public void run() {
            if (listeners != null) {
                try {
                    final MediaStatus mediaStatus = ChromecastFactory.instance().getMediaStatus();
                    LOGGER.log(Level.INFO, "mediastatus {0}", mediaStatus);
                    for (MediaStatusUpdateListener listener : listeners) {
                        listener.handleEvent(new MediaStatusUpdateEvent(mediaStatus));
                    }
                } catch (IOException | GeneralSecurityException | InterruptedException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
