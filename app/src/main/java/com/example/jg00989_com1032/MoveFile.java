package com.example.jg00989_com1032;

import java.io.File;
import java.util.concurrent.Semaphore;
import org.apache.commons.io.FileUtils;

public class MoveFile {


    private File sourceLocation;
    private File targetLocation;
    static Semaphore semaphore = new Semaphore(1);

    public MoveFile(){
        super();
    }
    public void moveFile(File file){
        sourceLocation=new File("/sdcard/Source/"+file.getName());
        targetLocation=new File("/sdcard/Destination/"+file.getName());

        try {
            FileUtils.copyFile(sourceLocation,targetLocation);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File[] getDirectories(String str){
        File file = new File(""+str);
        File[] files = file.listFiles();
        return files;

    }
}
