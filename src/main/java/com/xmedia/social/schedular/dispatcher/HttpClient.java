package com.xmedia.social.schedular.dispatcher;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpClient {
	HttpURLConnection open(URL url) throws Exception;
}
