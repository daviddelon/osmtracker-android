package me.guillaumin.android.osmtracker.db;

import me.guillaumin.android.osmtracker.R;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class NomAdapter extends CursorAdapter
{
    private SQLiteBdtfxAssistant dbAdapter = null;
 
    public NomAdapter(Context context, Cursor c, SQLiteBdtfxAssistant sQLiteBdtfxAssistant)
    {
        super(context, c);
        dbAdapter  = sQLiteBdtfxAssistant;

    }
    
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        String item = createItem(cursor);      
        ((TextView) view).setText(item);      
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(R.layout.list_item, parent, false);
        
        String item = createItem(cursor);
        view.setText(item);
        return view;
    }
 
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
    	
    	Log.i("NomAdapter", "runQueryOnBackgroundThread");
        Cursor currentCursor = null;
        
        if (getFilterQueryProvider() != null)
        {
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        String args = "";
        
        if (constraint != null)
        {
            args = constraint.toString();      
        }
        
        Log.w("NomAdapter args", args);
 
        currentCursor = dbAdapter.searchNoms(args);
 
        return currentCursor;
    }
    
    
    private String createItem(Cursor cursor)
    {
        String item = cursor.getString(1);      
        return item;
    }
    
    public void close()
    {
        dbAdapter.close();
    }

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#convertToString(android.database.Cursor)
	 */
	@Override
	public CharSequence convertToString(Cursor cursor) {
		
		 return cursor.getString(1)+"/"+cursor.getString(0);
	
	}
    
    
}