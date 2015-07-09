package com.map.view.mapoverlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class TextOverlay extends Overlay {

	private GeoPoint point; 
	
	private String text = new String();
	
	
	public TextOverlay(GeoPoint punto, String texto) {
		super();
		this.point = punto;
		if(texto != null){
			this.text= texto;
		}
	}

	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		 Point p = new Point();
		  mapView.getProjection().toPixels(this.point, p);
		
		
		Paint textPaint = new Paint();
		textPaint.setARGB(255, 0, 0, 0);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(12);
		canvas.drawText( this.text, p.x, p.y,  textPaint);
		
		
		super.draw(canvas, mapView, shadow);
	
	}

	
}
