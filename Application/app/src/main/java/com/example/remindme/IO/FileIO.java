package com.example.remindme.IO;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO<T> {

    public boolean fileExists(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public void writeFileToInternalStorage(Context context, T object, String fileName) throws IOException {
        FileOutputStream file = new FileOutputStream(context.getFilesDir() +"/"+ fileName);
        ObjectOutputStream objectOut = new ObjectOutputStream(file);
        objectOut.writeObject(object);
        objectOut.close();
    }

    public T readFileInInternalStorage(Context context, String fileName) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(context.getFilesDir() +"/"+ fileName);
        ObjectInputStream objectIn = new ObjectInputStream(file);
        T object = (T) objectIn.readObject();
        objectIn.close();
        return object;
    }

}
