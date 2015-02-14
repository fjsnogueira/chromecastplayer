/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.neocdtv.chromecastplayer;

/**
 *
 * @author xix
 */
public class ListEntry {
    private final String fileName;
    private final String filePath;

    public ListEntry(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
    
    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
