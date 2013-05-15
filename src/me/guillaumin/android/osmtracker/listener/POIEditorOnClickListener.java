package me.guillaumin.android.osmtracker.listener;

import me.guillaumin.android.osmtracker.activity.POIEditor;
import me.guillaumin.android.osmtracker.activity.TrackLogger;
import me.guillaumin.android.osmtracker.db.TrackContentProvider.Schema;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author David Delon / Nicolas Guillaumin 
 *
 */
public class POIEditorOnClickListener implements OnClickListener {

	
//	private TrackLogger tl;
	private long currentTrackId;
	
	
	//public POIEditorOnClickListener(TrackLogger trackLogger, long trackId) {
	public POIEditorOnClickListener(long trackId) {
	//	tl = trackLogger;
		currentTrackId=trackId; // TODO verifier origine
	}
	
	@Override
	public void onClick(final View v) {
		// let the TrackLogger activity open and control the dialog
		
		// TODO FIXME :
		// Demarrer activity avec un look de dialogue.
		// Using android Dialog theme for activity:
			Context context = v.getContext();
		
			Intent intent = new Intent(context, POIEditor.class);
			intent.putExtra(Schema.COL_TRACK_ID, currentTrackId);
			context.startActivity(intent);
		    // Do something in response to button
		
	}

}
