package com.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.etsia.test.R;
import com.map.view.KMLMapActivity;

public class InicioActivity extends Activity {
	
	
	private static final int MENU_EXIT = 5;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button boton = (Button) findViewById(R.id.buttonMap);
		boton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Uri uri1 = Uri
						.parse("geo:0,180?q=http://code.google.com/apis/kml/documentation/KML_Samples.kml");
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1);
				mapIntent.setData(uri1);
				
				startActivity(Intent.createChooser(mapIntent, "Sample Map "));

				
			}
		});

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intentMap = new Intent(InicioActivity.this,
						KMLMapActivity.class);
				startActivity(intentMap);

			}
		});

	}
	
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		
		menu.add(0, InicioActivity.MENU_EXIT, 0, R.string.salir).setIcon(android.R.drawable.ic_lock_power_off);

		return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case InicioActivity.MENU_EXIT:

			System.exit(0);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
}