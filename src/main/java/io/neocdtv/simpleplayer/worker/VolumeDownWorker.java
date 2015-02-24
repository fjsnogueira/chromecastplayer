/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.worker;

import io.neocdtv.simpleplayer.player.Player;
import io.neocdtv.simpleplayer.player.PlayerFactory;
import java.util.logging.Logger;

/**
 *
 * @author xix
 */
public class VolumeDownWorker extends AbstractWorker {

    @Override
    protected Void doInBackground() throws Exception {
        final Player currentPlayer = PlayerFactory.getCurrentPlayer();
        currentPlayer.volumeDown();
        return null;
    }

    @Override
    protected void additionalAction() {
        
    }

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(VolumeDownWorker.class.getName());
    }
}
