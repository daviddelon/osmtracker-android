package me.guillaumin.android.osmtracker.activity;

import java.util.UUID;

import me.guillaumin.android.osmtracker.OSMTracker;
import me.guillaumin.android.osmtracker.R;
import me.guillaumin.android.osmtracker.db.NomAdapter;
import me.guillaumin.android.osmtracker.db.SQLiteBdtfxAssistant;
import me.guillaumin.android.osmtracker.db.TrackContentProvider.Schema;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class POIEditor extends Activity { 


	/**
	 * Id of the track the activity  will add this waypoint to
	 */
	private long wayPointTrackId;
	private SQLiteBdtfxAssistant sQLiteBdtfxAssistant;  // database adapter / helper
	private NomAdapter mCursorAdapter;  // cursor adapter
	private Cursor mItemCursor;  // cursor
	private  AutoCompleteTextView textView= null;	 
	
	// TODO : nettoyer essais avec autocomplete ok
	// TODO : repasser en api 8 + emulateur ok
	// TODO : comprend le premier appel curseur ...
	// TODO : rechercher a partir de 3 lettres ? Ou algo de reche. carnet.  ok 
	// TODO : recuperer valeur saisie ok
	// TODO : mettre en gras et en premier les noms retenus
	// TODO : rendre compatible api 8 http://rakhi577.wordpress.com/2012/06/26/buttons-on-list-view-with-easy-searching-in-android/ ok
	// TODO : recherche optimisé sqlite fts3
	// TODO : gerer la mise en sommeil 
	// TODO : saisie de plus de 4 ???
	// TODO 
	// TODO 


	

	//private SearchView searchView=null;

	// private ListView mListView =null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {


        Log.i("PoiEditor", "On create");
        
		// Recuperation d'un reference à  base de reference
		// Mise en place de l'affichage.

		// initialisation des actions sur les boutons

		// add
		// appelle save : operation d'ajout
		// et reinitialisation du UUID

		// Cancel 
		// suppression encours
		// reinit UUID




		super.onCreate(savedInstanceState);


		setContentView(R.layout.poieditor);

		//  	searchView = (SearchView) findViewById(R.id.search);
		//   searchView.setIconifiedByDefault(false);
		//      searchView.setOnQueryTextListener(this);
		//   searchView.setOnCloseListener(this);

		//     mListView = (ListView) findViewById(R.id.list);


	

	
		

		final Button btnOk = (Button) findViewById(R.id.poieditor_btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});

		
		final Button btnCancel = (Button) findViewById(R.id.poieditor_btn_cancel);

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Just close the dialog
				finish();				
			}
		});

		// Do not show soft keyboard by default
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




		//	  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, countries);
		//      textView.setAdapter(adapter);


		/*
		OSMTracker osmtracker = (OSMTracker) getApplication();

	     countries = osmtracker.getAllTaxons();

	      textView = (AutoCompleteTextView) findViewById(R.id.autocompleteCountry);


		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, countries);
	       textView.setAdapter(adapter);*/



		// Get the message from the intent

		wayPointTrackId = getIntent().getExtras().getLong(Schema.COL_TRACK_ID);


		sQLiteBdtfxAssistant = new SQLiteBdtfxAssistant(this);
		sQLiteBdtfxAssistant.openDB(); // Charge le fichier et l'ouvre.



	}


	// initialize the cursor adapter
	private void initCursorAdapter()
	{

		Log.i("PoiEditor", "InitCursoAdapter");

	    mItemCursor =  sQLiteBdtfxAssistant.searchNoms(""); // Recherche vide

		sQLiteBdtfxAssistant.getReadableDatabase();

		startManagingCursor(mItemCursor);

		mCursorAdapter = new NomAdapter(getApplicationContext(), mItemCursor,sQLiteBdtfxAssistant);

	}




	private void initItemFilter()
	{
		Log.i("PoiEditor", "InitItemFilter");
		textView = (AutoCompleteTextView) findViewById(R.id.filter);
		
		textView.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                   // in.hideSoftInputFromWindow(autoEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                   //Commented line is for hide keyboard. Just make above code as comment and test your requirement
                   //It will work for your need. I just putted that line for your understanding only
                   //You can use own requirement here also.
                	save();
                	
                    
                }
                return true;
            }
        });
		
		 

		textView.setAdapter(mCursorAdapter);
		textView.setThreshold(3); // recherche a 3 car.
	}  




	protected void save() {

		Log.i("PoiEditor", "Save");
		
		if (!textView.getText().toString().equals("")) {

			String  wayPointUuid = UUID.randomUUID().toString();
			Intent intent = new Intent(OSMTracker.INTENT_TRACK_WP);
			intent.putExtra(Schema.COL_TRACK_ID, wayPointTrackId);
			intent.putExtra(OSMTracker.INTENT_KEY_UUID, wayPointUuid);
			intent.putExtra(OSMTracker.INTENT_KEY_NAME, textView.getText().toString());
			sendBroadcast(intent);
			

		}
		
		textView.setText(""); // FIXME  on pourrait faire mieux pour nettoyer ....

	}




	/* 
	 * 
	 */
	@Override
	protected void onResume() {


		Log.i("PoiEditor", "On resume");

		initCursorAdapter();
		initItemFilter(); 

			

		super.onResume();
	}


	@Override
	protected void onPause() {
		
		Log.i("PoiEditor", "On pause");
		mItemCursor.close();
		super.onPause();
	}




	
	@Override
	protected void onDestroy() {
		Log.i("PoiEditor", "On destroy");
		
	    super.onDestroy();
	    if (sQLiteBdtfxAssistant != null) {
	    	sQLiteBdtfxAssistant.close();
	    }
	 
	}







}
