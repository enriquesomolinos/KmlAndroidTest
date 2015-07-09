package com.map.services;

import java.io.InputStream;

import com.map.modelImpl.NavigationDataSet;

public interface MapService {

	public NavigationDataSet getNavigationDataSet(InputStream is);
	
	public NavigationDataSet getNavigationDataSet(String url);
}
