package io.neocdtv.chromecastplayer;

import io.neocdtv.service.UrlBuilder;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import org.apache.commons.io.IOUtils;
import su.litvak.chromecast.api.v2.ChromeCast;

public class Player {

    private final static Logger LOGGER = Logger.getLogger(Playlist.class.getName());
    private PlayerState playerStatus = PlayerState.LAUNCHED;
    private final Playlist playList = new Playlist();

    private final ChromeCast chromeCast;

    public PlayerState getPlayerStatus() {
        return playerStatus;
    }

    public Player(final ChromeCast chromeCast) throws IOException, GeneralSecurityException {
        this.chromeCast = chromeCast;
        JFrame frame = new JFrame();
        frame.setTitle("Chromecast Player");
        defineBehaviourForClosingWindow(frame);
        final Container contentPane = frame.getContentPane();

        buildPlayListWithScrollBar(contentPane);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6));

        JButton playButton = new JButton("play");
        JButton pauseButton = new JButton("pause");
        JButton nextButton = new JButton("next");
        JButton clearButton = new JButton("clear");
        JButton volumeUpButton = new JButton("+");
        JButton volumeDownButton = new JButton("-");

        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(volumeDownButton);
        buttonPanel.add(volumeUpButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    final String selectedTrackUrl = playList.getSelectedTrackUrl();
                    if (selectedTrackUrl != null) {
                        play(selectedTrackUrl);
                    }
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pause();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    play(playList.getNextSelectedTrackUrl());
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    playList.clear();
                    chromeCast.pause();
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        volumeUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    final Float currentVolume = chromeCast.getStatus().volume.level;
                    if (currentVolume < 1) {
                        chromeCast.setVolume(currentVolume + 0.1F);
                    };
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        volumeDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    final Float currentVolume = chromeCast.getStatus().volume.level;
                    if (currentVolume > 0) {
                        chromeCast.setVolume(currentVolume - 0.1F);
                    };
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 400);
        frame.setResizable(true);
        frame.setVisible(true);

    }

    private void pause() {
        if (PlayerState.PLAYING.equals(playerStatus)) {
            playerStatus = PlayerState.PAUSING;
            try {
                chromeCast.pause();
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (PlayerState.PAUSING.equals(playerStatus)) {
                try {
                    playerStatus = PlayerState.PLAYING;
                    chromeCast.play();
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void buildPlayListWithScrollBar(final Container contentPane) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setViewportView(playList);
        playList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                LOGGER.log(Level.INFO, "keyTyped: {0}:", e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                LOGGER.log(Level.INFO, "keyPressed: {0}:", e.getKeyCode());
                if ((e.getKeyCode()
                        == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    try {
                        final StringReader valueFromClipboardReader = getValueFromClipboard(DataFlavor.plainTextFlavor);
                        final String valueFromString = IOUtils.toString(valueFromClipboardReader);
                        LOGGER.log(Level.INFO, "fromClipBoard: {0}:", valueFromString);
                        playList.addElement(valueFromString);
                    } catch (IOException ex) {
                        Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if ((e.getKeyCode()
                        == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    playList.selectAll();
                }
                if (e.getKeyCode()
                        == KeyEvent.VK_DELETE) {
                    playList.removeSelected();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                LOGGER.log(Level.INFO, "keyReleased: {0}:", e.getKeyCode());
            }
        });
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        final TitledBorder createTitledBorder = BorderFactory.createTitledBorder("Playlist");
        createTitledBorder.setTitleJustification(TitledBorder.CENTER);
        scrollPane.setBorder(createTitledBorder);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private <T> T getValueFromClipboard(final DataFlavor flavor) {
        T valueFromClipboard = null;
        try {
            valueFromClipboard = (T) Toolkit.getDefaultToolkit().getSystemClipboard().getData(flavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valueFromClipboard;
    }

    private void defineBehaviourForClosingWindow(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    chromeCast.stopApp();
                    chromeCast.disconnect();
                    e.getWindow().dispose();
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void playNextTrack() throws IOException {
        play(playList.getNextTrackUrl());
    }

    private void play(final String trackUrl) throws IOException {
        final String urlToBePlayed = UrlBuilder.build(trackUrl);
        final Path path = FileSystems.getDefault().getPath(trackUrl);
        final String mimeType = Files.probeContentType(path);
        LOGGER.log(Level.INFO, "trying to play url {0}", urlToBePlayed);
        chromeCast.load("not implemented",
                "not implemented",
                urlToBePlayed,
                mimeType);
        playerStatus = PlayerState.PLAYING;

    }
}
