/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player.chromecast;

import io.neocdtv.service.UrlBuilder;
import io.neocdtv.simpleplayer.player.Player;
import io.neocdtv.simpleplayer.player.PlayerException;
import io.neocdtv.simpleplayer.ui.PlaylistUI;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import su.litvak.chromecast.api.v2.ChromeCast;

/**
 *
 * @author xix
 */
public class ChromecastPlayer implements Player {

    private final static Logger LOGGER = Logger.getLogger(PlaylistUI.class.getName());
    
    private static final ChromecastPlayer INSTANCE = new ChromecastPlayer();
    
    private ChromecastPlayer() {
        
    }
    
    public static ChromecastPlayer instance() {
        return INSTANCE;
    }
    
    @Override
    public void play(String url) throws PlayerException {
        try {
            final Path path = FileSystems.getDefault().getPath(url);
            final String mimeType = Files.probeContentType(path);
            final String urlToBePlayed = UrlBuilder.build(url);
            LOGGER.log(Level.INFO, "trying to play url {0}", urlToBePlayed);
            final ChromeCast chromecast = ChromecastFactory.instance();
            LOGGER.log(Level.INFO, "chromecast {0}", chromecast);
            chromecast.load("",
                    "",
                    urlToBePlayed,
                    mimeType);
        } catch (IOException | GeneralSecurityException  | InterruptedException ex) {
            throw new PlayerException(ex);
        }
    }

    @Override
    public void pause() throws PlayerException {
        try {
            final ChromeCast chromecast = ChromecastFactory.instance();
            chromecast.pause();
        } catch (IOException | GeneralSecurityException | InterruptedException ex) {
            throw new PlayerException(ex);
        }
    }

    @Override
    public void next() throws PlayerException {
       
    }
    
    @Override
    public void volumeUp() throws PlayerException {
        try {
            final ChromeCast chromecast = ChromecastFactory.instance();
            final Float currentVolume = chromecast.getStatus().volume.level;
            if (currentVolume < 1) {
                chromecast.setVolume(currentVolume + 0.1F);
            }
        } catch (IOException | GeneralSecurityException | InterruptedException ex) {
            throw new PlayerException(ex);
        }
    }

    @Override
    public void volumeDown() throws PlayerException {
        try {
            final ChromeCast chromecast = ChromecastFactory.instance();
            final Float currentVolume = chromecast.getStatus().volume.level;
            if (currentVolume > 0) {
                    chromecast.setVolume(currentVolume - 0.1F);
            }
        } catch (IOException |GeneralSecurityException | InterruptedException ex) {
            throw new PlayerException(ex);
        }
    }

    @Override
    public void play() throws PlayerException {
        try {
            final ChromeCast chromecast = ChromecastFactory.instance();
            chromecast.play();
        } catch (IOException | GeneralSecurityException | InterruptedException ex) {
            throw new PlayerException(ex);
        }
        
    }

    @Override
    public void shutdown() throws PlayerException{
        try {
            ChromecastFactory.shutdown();
        } catch (IOException ex) {
            throw new PlayerException(ex);
        }
    }
}
