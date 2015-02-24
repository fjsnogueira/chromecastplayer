/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player.chromecast;

import io.neocdtv.simpleplayer.player.PlayerState;
import io.neocdtv.simpleplayer.ui.PlayerStateFieldFactory;
import io.neocdtv.simpleplayer.worker.PlayNextWorker;
import java.util.logging.Level;
import java.util.logging.Logger;
import su.litvak.chromecast.api.v2.MediaStatus;

/**
 *
 * @author xix
 */
public class MediaStatusUpdateHandler implements MediaStatusUpdateListener{
    private final static Logger LOGGER = Logger.getLogger(MediaStatusUpdateHandler.class.getName());

    @Override
    public void handleEvent(MediaStatusUpdateEvent mediaStatusUpdateEvent) {
        LOGGER.log(Level.INFO, "handling chromecast media status update event...");
        final MediaStatus mediaStatus = mediaStatusUpdateEvent.getMediaStatus();
        LOGGER.log(Level.INFO, "mediastatus {0}", mediaStatus);
        if (mediaStatus == null) {
            if (PlayerState.getInstance().isInStatePlaying()) {
                new PlayNextWorker().execute();
            }
        } else {
            PlayerStateFieldFactory.instance().setText(mediaStatus.playerState.name());
        }
    }
    
}
