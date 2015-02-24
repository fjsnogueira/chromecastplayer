/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player;

/**
 *
 * @author xix
 */
public interface Player {
    public void play() throws PlayerException;
    public void play(final String url) throws PlayerException;
    public void pause() throws PlayerException;
    public void next() throws PlayerException;
    public void volumeUp() throws PlayerException;
    public void volumeDown() throws PlayerException;
    public void shutdown() throws PlayerException;
}
