package jp.mkzm.android.oauthtest;

import oauth.signpost.OAuth;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterLogin extends Activity {
	
	private final static String CONSUMER_KEY = "";
	private final static String CONSUMER_SEACRET = "";
	private final static String REQUEST_TOKEN_URL = "http://twitter.com/oauth/request_token";
	private final static String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	private final static String AUTHORIZE_URL = "http://twitter.com/oauth/authorize";
	private final static String CALLBACK_URL = "myapp://activity";
	private final static String TAG = "TwitterLogin";

	CommonsHttpOAuthConsumer consumer = null;
	DefaultOAuthProvider provider = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_login);

		try {
			consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SEACRET);
			provider =
				new DefaultOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
	
			// îFèÿópURL
			String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);

			WebView webview = (WebView)findViewById(R.id.webview);
			webview.setWebViewClient(new ViewClient());
			webview.loadUrl(authUrl);

		} catch(Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private class ViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			Log.d(TAG, url);
			
			if(url != null && url.startsWith(CALLBACK_URL)) {
				String verifier =
					Uri.parse(url).getQueryParameter(OAuth.OAUTH_VERIFIER);
				try {
					provider.retrieveAccessToken(consumer, verifier);
					
					Log.d(TAG, "token : " + consumer.getToken());
					Log.d(TAG, "tokenSecret : " + consumer.getTokenSecret());
				} catch(Exception e) {
					Toast.makeText(
							TwitterLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		}
	}
}
