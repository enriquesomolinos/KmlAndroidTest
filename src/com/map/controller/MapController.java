package com.map.controller;

import java.io.InputStream;

import com.map.modelImpl.NavigationDataSet;

public interface MapController {

	public NavigationDataSet getNavigationDataSet(InputStream is);
	
	public NavigationDataSet getNavigationDataSet(String url);
}
