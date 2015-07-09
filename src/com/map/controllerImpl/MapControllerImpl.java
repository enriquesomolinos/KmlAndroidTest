package com.map.controllerImpl;

import java.io.InputStream;

import com.map.controller.MapController;
import com.map.modelImpl.NavigationDataSet;
import com.map.services.MapService;
import com.map.servicesImpl.MapServiceImpl;

public class MapControllerImpl implements MapController {

	private MapService mapService = new MapServiceImpl();
	
	public NavigationDataSet getNavigationDataSet(InputStream is){
		return mapService.getNavigationDataSet(is);
	}
	
	public NavigationDataSet getNavigationDataSet(String url){
		return mapService.getNavigationDataSet(url);
	}
	
	
}
