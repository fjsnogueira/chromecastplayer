# chromecastplayer
This project is a first alpha approach to write a simple media player, which is using googles chromecast. This project is using Swing for UI.

##What's working (most of the time)
- Drag and drop local files into the playlist, also recursive - for playlist entry name the file name is used.
- Select playlist entries and delete them with key DEL, CTRL+A also works
- Play, Pause selected playlist entry
- Volume Up/Down
- Paste (CTRL+V) a url from clipboard

##What's not working
- Since autodiscovery was not working in my local network, the player is trying to connect to 'Chromecast' and resolve the domain name to a IP-Address (This is hardcoded by now)
- If Chromecast is not supporting a media coded, the playlist won't jump to next entry
- The Player native Swing UI was testet only on Linux Mint 17 with Mate (Gnome2-Fork). So if the player looks ugly on platforms like Windows, MacOSX and other Linux Distros, please give me a hint.

##What's planned
- Make stable

##Install and Usage
```bash
git clone https://github.com/neocdtv/streamingservice
cd streamingservice
mvn clean install
git clone https://github.com/neocdtv/chromecastplayer
cd chromecastplayer
mvn clean install
java -jar target/ChromeCastPlayer-0.3-jar-with-dependencies.jar
```

##Thanks goes to:
- Vitaly Litvak, the creater of the project [chromecast-java-api-v2](https://github.com/vitalidze/chromecast-java-api-v2), which is used by this project 
- Christian Stegmann, who motivated me to push this project to github ;)


