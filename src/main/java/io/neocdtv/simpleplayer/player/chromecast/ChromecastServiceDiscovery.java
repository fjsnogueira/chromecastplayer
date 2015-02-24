/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.simpleplayer.player.chromecast;

import io.neocdtv.simpleplayer.ui.ComboBoxFactory;
import io.neocdtv.simpleplayer.ui.CombolistEntry;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author xix
 */
public class ChromecastServiceDiscovery {

    private final static Logger LOGGER = Logger.getLogger(ChromecastServiceDiscovery.class.getName());
    private static final String serviceType = "_googlecast._tcp.local.";

    private ChromecastServiceDiscovery() {
    }

    public static void start() throws IOException {
        final DefaultComboBoxModel<CombolistEntry> comboBoxModel = ComboBoxFactory.instance();
        JmDNS jmDns = JmDNS.create();
        jmDns.addServiceListener(serviceType, new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent event) {
                final String mDNSName = buildmDNSName(event.getInfo());
                LOGGER.log(Level.INFO, "found chromecast device {0} -> adding to list", mDNSName);
                comboBoxModel.addElement(new CombolistEntry(mDNSName, ChromecastPlayer.instance()));
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                final String mDNSName = buildmDNSName(event.getInfo());
                LOGGER.log(Level.INFO, "removing chromecast device {0} from list", mDNSName);
                System.out.println("Service removed " + event.getInfo());
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                System.out.println("Service resolved" + event.getInfo());
            }

            private String buildmDNSName(ServiceInfo serviceInfo) {
                return serviceInfo.getName() + "." + serviceInfo.getDomain();
            }
        });
    }
}