package com.map.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.etsia.test.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.map.controller.MapController;
import com.map.controllerImpl.MapControllerImpl;
import com.map.modelImpl.NavigationDataSet;
import com.map.modelImpl.Placemark;
import com.map.view.mapoverlay.PointOverlay;
import com.map.view.mapoverlay.RouteOverlay;
import com.map.view.mapoverlay.RoutePathOverlay;
import com.map.view.mapoverlay.TextOverlay;

public class KMLMapActivity extends MapActivity {
	
	private MapController controller = new MapControllerImpl();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		AssetManager assetManager = this.getAssets();

		InputStream is;
		try {
			is = assetManager.open("GR_10_Etapa_4__Recorrido_desde_H.kml");

			NavigationDataSet navSet =controller.getNavigationDataSet(is);
			drawPath(navSet, Color.RED, mapView);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Does the actual drawing of the route, based on the geo points provided in
	 * the nav set
	 * 
	 * @param navSet
	 *            Navigation set bean that holds the route information, incl.
	 *            geo pos
	 * @param color
	 *            Color in which to draw the lines
	 * @param mMapView01
	 *            Map view to draw onto
	 */
	public void drawPath(NavigationDataSet navSet, int color, MapView mMapView01) {

		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		Collection overlaysToAddAgain = new ArrayList();
		for (Iterator iter = mMapView01.getOverlays().iterator(); iter
				.hasNext();) {
			Object o = iter.next();
			Log.d("EtsiaKML", "overlay type: " + o.getClass().getName());
			if (!RouteOverlay.class.getName().equals(o.getClass().getName())) {
				overlaysToAddAgain.add(o);
			}
		}
		mMapView01.getOverlays().clear();
		mMapView01.getOverlays().addAll(overlaysToAddAgain);

		int totalNumberOfOverlaysAdded = 0;
		for (Placemark placemark : navSet.getPlacemarks()) {
			String path = placemark.getCoordinates();
			if (path != null && path.trim().length() > 0) {
				String[] pairs = path.trim().split(" ");

				String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude
														// lngLat[1]=latitude
														// lngLat[2]=height
				try {
					if (lngLat.length > 1 && !lngLat[0].equals("")
							&& !lngLat[1].equals("")) {
						GeoPoint startGP = new GeoPoint(
								(int) (Double.parseDouble(lngLat[1]) * 1E6),
								(int) (Double.parseDouble(lngLat[0]) * 1E6));

						GeoPoint gp1;
						GeoPoint gp2 = startGP;

						geoPoints = new ArrayList<GeoPoint>();
						geoPoints.add(startGP);

						if (pairs.length == 1) {
							//pintamos el punto
							mMapView01
									.getOverlays()
									.add(new PointOverlay(
											new GeoPoint(
													(int) (Double
															.parseDouble(lngLat[1]) * 1E6),
													(int) (Double
															.parseDouble(lngLat[0]) * 1E6)),placemark.getTitle(),PointOverlay.TYPE_BUBBLE,mMapView01));

						}

						for (int i = 1; i < pairs.length; i++) {
							lngLat = pairs[i].split(",");

							gp1 = gp2;
							if (lngLat.length >= 2 && gp1.getLatitudeE6() != 0
									&& gp1.getLongitudeE6() != 0
									&& gp2.getLatitudeE6() != 0
									&& gp2.getLongitudeE6() != 0) {

								// for GeoPoint, first:latitude,
								// second:longitude
								gp2 = new GeoPoint(
										(int) (Double.parseDouble(lngLat[1]) * 1E6),
										(int) (Double.parseDouble(lngLat[0]) * 1E6));

								if (gp2.getLatitudeE6() != 22200000) {
									geoPoints.add(gp2);

								}
							}
						}

						totalNumberOfOverlaysAdded++;
						mMapView01.getOverlays().add(
								new RoutePathOverlay(geoPoints));
					}

				} catch (NumberFormatException e) {
					Log.e("EtsiaKML", "Cannot draw route.", e);
				}
			}
		}

		Log.d("EtsiaKML", "Total overlays: " + totalNumberOfOverlaysAdded);
		mMapView01.setEnabled(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}