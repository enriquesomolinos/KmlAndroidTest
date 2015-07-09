package com.map.servicesImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.map.modelImpl.NavigationDataSet;
import com.map.services.MapService;

public class MapServiceImpl implements MapService
{

	
	public static final int MODE_ANY = 0;
	public static final int MODE_CAR = 1;
	public static final int MODE_WALKING = 2;

	private static String inputStreamToString(InputStream in) throws IOException
	{
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;)
		{
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	
	public NavigationDataSet getNavigationDataSet(String url) {

	    
	    NavigationDataSet navigationDataSet = null;
	    try
	        {           
	        final URL aUrl = new URL(url);
	        final URLConnection conn = aUrl.openConnection();
	        conn.setReadTimeout(15 * 1000);  // timeout for reading the google maps data: 15 secs
	        conn.connect();

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance(); 
	        SAXParser sp = spf.newSAXParser(); 

	        /* Get the XMLReader of the SAXParser we created. */
	        XMLReader xr = sp.getXMLReader();

	        /* Create a new ContentHandler and apply it to the XML-Reader*/ 
	        NavigationSaxHandler navSax2Handler = new NavigationSaxHandler(); 
	        xr.setContentHandler(navSax2Handler); 

	        /* Parse the xml-data from our URL. */ 
	        xr.parse(new InputSource(aUrl.openStream()));

	        /* Our NavigationSaxHandler now provides the parsed data to us. */ 
	        navigationDataSet = navSax2Handler.getParsedData(); 

	        

	    } catch (Exception e) {
	        // Log.e(myapp.APP, "error with kml xml", e);
	        navigationDataSet = null;
	    }   

	    return navigationDataSet;
	}

	/**
	 * Retrieve navigation data set from either remote URL or String
	 * 
	 * @param url
	 * @return navigation set
	 */
	public NavigationDataSet getNavigationDataSet(InputStream is)
	{

		NavigationDataSet navigationDataSet = null;
		try
		{
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Create a new ContentHandler and apply it to the XML-Reader */
			NavigationSaxHandler navSax2Handler = new NavigationSaxHandler();
			xr.setContentHandler(navSax2Handler);

			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource (is));

			/* Our NavigationSaxHandler now provides the parsed data to us. */
			navigationDataSet = navSax2Handler.getParsedData();

			/* Set the result to be displayed in our GUI. */
			Log.d("EtsiaKML",
			        "navigationDataSet: " + navigationDataSet.toString());

		}
		catch (Exception e)
		{
			Log.e("EtsiaKML", "error with kml xml", e);
			navigationDataSet = null;
		}

		return navigationDataSet;
	}

}