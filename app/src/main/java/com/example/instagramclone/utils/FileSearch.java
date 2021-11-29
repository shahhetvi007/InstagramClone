package com.example.instagramclone.utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {
    //Search folder from storage
    public static ArrayList<String> getFolderPath(String folder){
        ArrayList<String> path = new ArrayList<>();
        File file = new File(folder);
        File[] listOfFiles = file.listFiles();
        for (int i=0; i<listOfFiles.length; i++){
            if (listOfFiles[i].isDirectory()){
                path.add(listOfFiles[i].getAbsolutePath());
            }
        }
        return path;
    }

    //Search file from folder
    public static ArrayList<String> getFilePath(String folder){
        ArrayList<String> path = new ArrayList<>();
        File file = new File(folder);
        File[] listOfFiles = file.listFiles();
        for (int i=0; i<listOfFiles.length; i++){
            if (listOfFiles[i].isFile()){
                path.add(listOfFiles[i].getAbsolutePath());
            }
        }
        return path;
    }
}
