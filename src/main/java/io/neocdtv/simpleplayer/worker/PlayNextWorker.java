/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.worker;

import io.neocdtv.simpleplayer.ui.PlaylistUI;
import java.util.logging.Logger;

/**
 *
 * @author xix
 */
public class PlayNextWorker extends AbstractPlayWorker {

    @Override
    protected String getUrl() {
        return PlaylistUI.getInstance().getNextTrackUrl();
    }

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(PlayNextWorker.class.getName());
    }
}
