/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.worker;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author xix
 */
public abstract class AbstractWorker extends SwingWorker<Void, Void> {

    @Override
    protected void done() {
        additionalAction();
        try {
            get();
        } catch (InterruptedException | ExecutionException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void additionalAction();

    protected abstract Logger getLogger();
}
