/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.worker;

import io.neocdtv.simpleplayer.player.Player;
import io.neocdtv.simpleplayer.player.PlayerFactory;
import io.neocdtv.simpleplayer.ui.ButtonsFactory;
import io.neocdtv.simpleplayer.player.PlayerState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xix
 */
public class PauseWorker extends AbstractWorker {


    @Override
    protected Void doInBackground() throws Exception {
        ButtonsFactory.enableButtons(false);
        final PlayerState playerState = PlayerState.getInstance();
        final Player currentPlayer = PlayerFactory.getCurrentPlayer();
        if (playerState.isInStatePlaying()) {
            playerState.setToStatePausing();
            getLogger().log(Level.INFO, "pausing...");
            currentPlayer.pause();
        } else {
            if (playerState.isInStagePausing()) {
                playerState.setToStatePlaying();
                getLogger().log(Level.INFO, "playing...");
                currentPlayer.play();
            }
        }
        return null;
    }

    @Override
    protected void additionalAction() {
        ButtonsFactory.enableButtons(true);
    }

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(PauseWorker.class.getName());
    }
}
