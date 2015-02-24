/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xix
 */

public class PlayerState {

    private final static Logger LOGGER = Logger.getLogger(PlayerState.class.getName());
    private static PlayerState INSTANCE;
    private PlayerStateType currentState;

    public static PlayerState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerState();
        }
        return INSTANCE;
    }
    
    private PlayerState() {
        final PlayerStateType initialPlayerState = PlayerStateType.LAUNCHED;
        LOGGER.log(Level.INFO, "initial player state: {0}", initialPlayerState);
        currentState = initialPlayerState;
    }

    private void setState(PlayerStateType newState) {
        StringBuilder msg = new StringBuilder("changing state: ");
        msg.append(currentState)
                .append(" -> ")
                .append(newState);
        LOGGER.log(Level.INFO, msg.toString());
        this.currentState = newState;
    }

    public void setToStateLaunched() {
        setState(PlayerStateType.LAUNCHED);
    }
    
    public void setToStatePlaying() {
        setState(PlayerStateType.PLAYING);
    }
    
    public void setToStatePausing() {
        setState(PlayerStateType.PAUSING);
    }
        
    public boolean isInStatePlaying() {
        return PlayerStateType.PLAYING.equals(currentState);
    }
    
    public boolean isInStagePausing() {
        return PlayerStateType.PAUSING.equals(currentState);
    }

    public boolean isInStateLaunched() {
        return PlayerStateType.LAUNCHED.equals(currentState);
    }

    private enum PlayerStateType {

        LAUNCHED,
        PLAYING,
        PAUSING;
    }
}
