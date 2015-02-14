/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.chromecastplayer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 *
 * @author xix
 */
public class Playlist extends JList<ListEntry> {

    private final static Logger LOGGER = Logger.getLogger(Playlist.class.getName());

    private int playingIndex = 0;

    public Playlist() {
        super(new DefaultListModel<ListEntry>());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setDragEnabled(true);
        setTransferHandler(new ListTransferHandler());
        setDropMode(DropMode.INSERT);
    }

    @Override
    public DefaultListModel<ListEntry> getModel() {
        return (DefaultListModel<ListEntry>) super.getModel();
    }

    public void addElement(final String url) {
        getModel().addElement(new ListEntry(url, url));
    }

    public void clear() {
        setModel(new DefaultListModel<ListEntry>());
    }

    public String getSelectedTrackUrl() {
        LOGGER.log(Level.INFO, "getting track url for current playlist entry...");

        String selectedTrackUrl = null;
        final int selectedIndex = getSelectedIndex();

        LOGGER.log(Level.INFO, "selected index {0} ", selectedIndex);
        if (selectedIndex >= 0) {
            selectedTrackUrl = getModel().get(selectedIndex).getFilePath();
            playingIndex = selectedIndex;
        }
        LOGGER.log(Level.INFO, "selected playlist track {0}", selectedTrackUrl);
        return selectedTrackUrl;
    }

    public String getNextSelectedTrackUrl() {
        LOGGER.log(Level.INFO, "getting track url for next selected playlist entry...");
        String nextSelectedTrackUrl = null;
        int selectedIndex = getSelectedIndex();

        if (selectedIndex >= 0) {
            if (selectedIndex == getModel().getSize()) {
                selectedIndex = 0;
            } else {
                selectedIndex++;
            }
            playingIndex = selectedIndex;
            setSelectedIndex(selectedIndex);
            nextSelectedTrackUrl = getModel().get(selectedIndex).getFilePath();
        }
        LOGGER.log(Level.INFO, "next playlist track {0}", nextSelectedTrackUrl);
        return nextSelectedTrackUrl;
    }

    public String getNextTrackUrl() {
        LOGGER.log(Level.INFO, "getting track url for next playlist entry...");
        String nextTrackUrl = null;
        if (getModel().getSize() > 0 && playingIndex >= 0) {
            playingIndex++;
            if (playingIndex >= getModel().getSize()) {
                playingIndex = 0;
            }
            setSelectedIndex(playingIndex);
            nextTrackUrl = getModel().get(playingIndex).getFilePath();
        }
        return nextTrackUrl;
    }

    void selectAll() {
        final DefaultListModel<ListEntry> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            int[] indicies = new int[model.getSize()];
            indicies[i] = i;
            setSelectedIndices(indicies);
        }
    }

    void removeSelected() {
        final DefaultListModel<ListEntry> model = getModel();
        final List<ListEntry> selectedValuesList = getSelectedValuesList();
        for (ListEntry entry: selectedValuesList) {
            model.removeElement(entry);
        }
    }
}
