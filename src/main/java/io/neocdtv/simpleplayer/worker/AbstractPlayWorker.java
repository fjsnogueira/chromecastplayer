/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.worker;

import io.neocdtv.simpleplayer.player.Player;
import io.neocdtv.simpleplayer.player.PlayerException;
import io.neocdtv.simpleplayer.player.PlayerFactory;
import io.neocdtv.simpleplayer.player.PlayerState;
import io.neocdtv.simpleplayer.ui.ButtonsFactory;
import java.util.logging.Level;

/**
 *
 * @author xix
 */
public abstract class AbstractPlayWorker extends AbstractWorker{

    private boolean enableButtonsInDone = true;

    @Override
    protected Void doInBackground() {
        try {
            ButtonsFactory.enableButtons(false);
            final String url = getUrl();
            final Player currentPlayer = PlayerFactory.getCurrentPlayer();
            currentPlayer.play(url);
            PlayerState.getInstance().setToStatePlaying();
            return null;
        } catch (PlayerException ex) {
            enableButtonsInDone = false;
            new PlayNextWorker().execute();
            getLogger().log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void additionalAction() {
        ButtonsFactory.enableButtons(enableButtonsInDone);
    }
    
    protected abstract String getUrl();
}
