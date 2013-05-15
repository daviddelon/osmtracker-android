package me.guillaumin.android.osmtracker.db;

 
import android.database.*;
import android.database.sqlite.*;
import android.content.Context;
import android.util.Log;
 
public class SQLiteBdtfxAssistant extends ExternalStorageReadOnlyOpenHelper
{
    private static final String DB_NAME = "bdtfx.sqlite";

 
    private SQLiteDatabase sqliteDBInstance = null;
 
    public SQLiteBdtfxAssistant(Context context)
    {
        super(DB_NAME, null);
    }
    


 
    public void openDB() throws SQLException
    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getReadableDatabase();
        }
    }
 
    public void closeDB()
    {
    	
        Log.i("CloseDB", "Close sqliteDBInstance...");
        
        if(this.sqliteDBInstance != null)
        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }


    
    public Cursor searchNoms(String inputText)
    {
    	
        if (inputText.equals("")) {
        	inputText="@@@@";
        }
        
        
        String genre = inputText;
    	String espece = "";
        
    	String[] strings = inputText.split(" ", 2);
        
        
        // On peut faire mieux FIXME
        if (strings.length > 1) {
        	 genre = strings[0];
        	 espece = strings[1];
        }
        

    	
        Log.w("SearchTaxons", inputText);
        String query = "SELECT _id, nom"
        + " from bdtfx " + " where nom like '" + genre +"%' AND nom like '%"+espece+"%' limit 25;";
     
        
        //+ " from bdtfx " + " where nom = '" + inputText + "';";
        
        Log.w("SearchTaxons", query);
        
        Cursor mCursor = sqliteDBInstance.rawQuery(query,null);
 
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    	
      }
    
}