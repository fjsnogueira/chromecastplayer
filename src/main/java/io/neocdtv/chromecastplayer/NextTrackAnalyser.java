/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.chromecastplayer;

import java.io.IOException;
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
public class NextTrackAnalyser extends TimerTask {

    private final static Logger logger = Logger.getLogger(NextTrackAnalyser.class.getName());
    private final ChromeCast chromeCast;
    private final Player player;

    public NextTrackAnalyser(ChromeCast chromeCast, Player player) {
        this.chromeCast = chromeCast;
        this.player = player;
        Timer timer = new Timer();
        timer.schedule(this, 0, 500);
    }

    @Override
    public void run() {
        if (player != null && chromeCast != null) {
            try {
                final MediaStatus mediaStatus = chromeCast.getMediaStatus();
                if (PlayerState.PLAYING.equals(player.getPlayerStatus()) && mediaStatus == null) {
                    player.playNextTrack();
                }
            } catch (IOException ex) {
                Logger.getLogger(NextTrackAnalyser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
