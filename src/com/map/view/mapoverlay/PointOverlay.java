package com.map.view.mapoverlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.etsia.test.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PointOverlay extends Overlay {

	public static final int TYPE_BUBBLE = 1;
	public static final int TYPE_BUBBLE_OFF = 2;
	public static final int TYPE_ANDROID = 3;

	public static final int PADDING_X = 10;
	public static final int PADDING_Y = 8;
	public static final int RADIUS_BUBBLES = 5;
	public static final int DISTANCE_BUBBLE = 15;
	public static final int SIZE_SELECTOR_BUBBLE = 10;

	private Bitmap drawIcon;

	private GeoPoint point;

	private String text = new String();
	private int type = -1;

	private MapView mapView;

	private boolean selected = false;

	public PointOverlay(GeoPoint point, String text, int type, MapView mapView) {
		super();
		this.point = point;
		this.text = text;
		this.type = type;
		this.mapView = mapView;
		setType(type);
		mapView.setClickable(true);
	}

	private void setType(int type) {
		this.type = type;
		switch (type) {
		case TYPE_BUBBLE:
			drawIcon = BitmapFactory.decodeResource(mapView.getResources(),
					R.drawable.bubble);

			break;
		case TYPE_ANDROID:
			drawIcon = BitmapFactory.decodeResource(mapView.getResources(),
					R.drawable.android);

			break;
		default:
			drawIcon = BitmapFactory.decodeResource(mapView.getResources(),
					R.drawable.bubble_off);
			break;
		}
	}

	public boolean getHit(int offsetx, int offsety, float event_x, float event_y) {
		if (getHRectFIcon(offsetx, offsety).contains(event_x, event_y)) {
			return true;
		}
		return false;
	}

	public RectF getHRectFIcon(int offsetx, int offsety) {
		RectF rectf = new RectF();
		rectf.set(-drawIcon.getWidth()/2,-drawIcon.getHeight(),drawIcon.getWidth()/2,0);
		rectf.offset(offsetx, offsety);
		return rectf;
	}
	
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Point p = new Point();

			mapView.getProjection().toPixels(this.point, p);
			if (getHit(p.x, p.y, event.getX(), event.getY())) {
				if (!selected) {
					selected = true;
				} else {
					selected = false;
				}
			}

		}

		return false;
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Point p = new Point();
		mapView.getProjection().toPixels(this.point, p);
		canvas.drawBitmap(this.drawIcon, p.x - this.drawIcon.getWidth() / 2,
				p.y - this.drawIcon.getHeight(), null);

		if (selected) {
			drawBubble(canvas, mapView, shadow);
		}

	}

	public void drawBubble(Canvas canvas, MapView mapView, boolean shadow) {
		Point p = new Point();
		mapView.getProjection().toPixels(point, p);

		Paint textPaint = new Paint();
		textPaint.setARGB(255, 0, 0, 0);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(12);

		int wBox = (int) (textPaint.measureText(this.text) + (PADDING_X * 2));
		int hBox = (int) (textPaint.descent() + (PADDING_Y * 2));

		RectF boxRect = new RectF(0, 0, wBox, hBox);
		int offsetX = p.x - wBox / 2;
		int offsetY = p.y - hBox - drawIcon.getHeight() - DISTANCE_BUBBLE;
		boxRect.offset(offsetX, offsetY);

		Path pathBubble = new Path();
		pathBubble.addRoundRect(boxRect, RADIUS_BUBBLES, RADIUS_BUBBLES,
				Direction.CCW);
		pathBubble.moveTo(offsetX + (wBox / 2) - (SIZE_SELECTOR_BUBBLE / 2),
				offsetY + hBox);
		pathBubble.lineTo(offsetX + (wBox / 2), offsetY + hBox
				+ SIZE_SELECTOR_BUBBLE);
		pathBubble.lineTo(offsetX + (wBox / 2) + (SIZE_SELECTOR_BUBBLE / 2),
				offsetY + hBox);

		Paint innerPaint, borderPaint;
		innerPaint = new Paint();
		innerPaint.setARGB(255, 255, 255, 255);
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 0, 0, 0);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(4);

		canvas.drawPath(pathBubble, borderPaint);
		canvas.drawPath(pathBubble, innerPaint);

		canvas.drawText(this.text, p.x - textPaint.measureText(this.text) / 2,
				p.y - drawIcon.getHeight() - DISTANCE_BUBBLE - hBox / 2
						- textPaint.ascent() / 2, textPaint);
	}
}
