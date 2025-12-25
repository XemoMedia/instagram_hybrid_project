package com.xmedia.social.schedular.dispatcher;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class DefaultHttpClient implements HttpClient  {

	@Override
	public HttpURLConnection open(URL url) throws Exception {
		return (HttpURLConnection) url.openConnection();
	}

}
