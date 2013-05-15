package me.guillaumin.android.osmtracker.db;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.AndroidRuntimeException;


public abstract class ExternalStorageReadOnlyOpenHelper{
     private SQLiteDatabase database;
     private File dbFile;
     private SQLiteDatabase.CursorFactory factory;

     public ExternalStorageReadOnlyOpenHelper(
         String dbFileName, SQLiteDatabase.CursorFactory factory) {
        this.factory = factory;
        
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new AndroidRuntimeException(
                "External storage (SD-Card) not mounted");
        } 
        File appDbDir = new File(
            Environment.getExternalStorageDirectory(),            
            "osmtracker/db");
        if (!appDbDir.exists()) {
            appDbDir.mkdirs();
        }
        this.dbFile = new File(appDbDir, dbFileName);
     }
     
     public boolean databaseFileExists() {
        return dbFile.exists();
     }
     
     private void open() {
         if (dbFile.exists()) {
             database = SQLiteDatabase.openDatabase(
                 dbFile.getAbsolutePath(), 
                 factory, 
                 SQLiteDatabase.OPEN_READONLY);     
         }
     }
        
     public synchronized void close() {
         if (database != null ) {
            database.close();
            database = null;
        }
     }

     public synchronized SQLiteDatabase getReadableDatabase() {
         return getDatabase();
     }

     private SQLiteDatabase getDatabase() {
        if (database==null) {
            open();
        }
        return database;
     }         
}