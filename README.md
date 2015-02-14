# chromecastplayer
This project is a first alpha approach to write a simple media player, which is using googles chromecast. This project is using Swing for UI.

##What's working (most of the time)
- Drag and drop local files into the playlist, also recursive - for playlist entry name the file name is used.

##Install and Usage
```bash
git clone https://github.com/neocdtv/streamingservice
cd streamingservice
mvn clean install
git clone https://github.com/neocdtv/chromecastplayer
cd chromecastplayer
mvn clean install
java -jar target/ChromeCastPlayer-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

##Thanks goes to:
Vitaly Litvak, the creater of the project - [chromecast-java-api-v2](https://github.com/vitalidze/chromecast-java-api-v2) 
- Christian Stegmann, who motivated me to push this project to github ;)


