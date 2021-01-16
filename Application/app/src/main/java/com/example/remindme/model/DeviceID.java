package com.example.remindme.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.remindme.IO.FileIO;
import java.io.IOException;
import java.util.UUID;

public class DeviceID {

    private static DeviceID instance;
    private String uniqueID;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static final String fileName = "deviceID";

    private DeviceID() {
        uniqueID = null;
    }

    private static DeviceID getInstance(){
        if(instance == null){
            instance = new DeviceID();
        }
        return instance;
    }

    public static String getDeviceID(Context context) throws IOException, ClassNotFoundException {
        if(getInstance().uniqueID == null) {
            FileIO<String> io = new FileIO<String>();
            if (io.fileExists(context, fileName)) {
                getInstance().uniqueID = io.readFileInInternalStorage(context, fileName);
            } else {
                String id = id(context);
                io.writeFileToInternalStorage(context, id, fileName);
                getInstance().uniqueID = id;
            }
        }
        return getInstance().uniqueID;
    }

    private synchronized static String id(Context context) {
        String id = getInstance().uniqueID;
        if (id == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            id = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (id == null) {
                id = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, id);
                editor.commit();
            }
            id =  "_device_" + id;
        }
        return id;
    }

}
