package com.protogab.chikungunya;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.I18NBundle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.protogab.chikungunya.ActionResolver;
import com.protogab.chikungunya.Chikungunya;




public class AndroidLauncher extends AndroidApplication implements ActionResolver, GameHelperListener  {
	
	String developerPublisherId = "PROTOGAB";
    int backKeyCount = 0;
    I18NBundle langBundle;//Internationalization
    int optionSelected = -1;
    String comment = "";
    GameHelper gameHelper;
    Preferences preferences;
	
	protected View gameView;
	private InterstitialAd interstitialAd;
	private InterstitialAd interstitialAdExit;
	private String AD_UNIT_ID_INTERSTITIAL; 
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
	    // Do the stuff that initialize() would do for you
	    //https://github.com/TheInvader360/tutorial-libgdx-google-ads/blob/0a5ea376d4eb92b8e87c13a03245adb40b53e811/tutorial-libgdx-google-ads-android/src/com/theinvader360/tutorial/libgdx/google/ads/MainActivity.java
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
	    RelativeLayout layout = new RelativeLayout(this);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	    layout.setLayoutParams(params);
	    
	    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	    
	    //Create GameView
	    View gameView;
	    gameView = initializeForView(new Chikungunya(this), config);
	    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.BELOW, 12345); // this is an arbitrary id, allows for relative positioning in createGameView()
	    gameView.setLayoutParams(params2);
	    
	    
	    layout.addView(gameView);
	    
	    setContentView(layout);
		
	    
	    //Init Google Analytics
	     //Nothing needed here so far
		
		//Init Google Ads
	    AD_UNIT_ID_INTERSTITIAL = getResources().getString(R.string.interstitial_ad_id);
		interstitialAd = new InterstitialAd(this);		
	    interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
	    interstitialAd.setAdListener(new AdListener() {
	      @Override
		  public void onAdLeftApplication() {
				preferences.putBoolean( Chikungunya.PREF_AD_CLICKED, true);
				preferences.flush();
	    	  //Toast.makeText(getApplicationContext(), "AD clicked???", Toast.LENGTH_SHORT).show(); 
	      }
	      @Override
	      public void onAdLoaded() {
	        //Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
	      }
	      @Override
	      public void onAdClosed() {
	        //Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
	    	  //Let's load the add for the next future request
	    	  //AdRequest interstitialRequest = new AdRequest.Builder().build();
	          //interstitialAd.loadAd(interstitialRequest);
	      }
	    });
	    
	    interstitialAdExit = new InterstitialAd(this);
		interstitialAdExit.setAdUnitId(getResources().getString(R.string.interstitial_ad_id_exit));
		AdRequest interstitialRequest = new AdRequest.Builder().build();
        interstitialAdExit.loadAd(interstitialRequest);
	    
	    
	    
	    //Init Google Game Services	    
	    preferences = Gdx.app.getPreferences(Chikungunya.PREFS_NAME);//THIS MUST BE AFTER THE INITIALIZATION START
	    if (gameHelper == null) {
	        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
	        gameHelper.enableDebugLog(true);
	     }
	     gameHelper.setConnectOnStart(false);///FORCED NOT AUTOCONNECTION
	     gameHelper.setup(this);
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	////////////////////////////////////////////////////BASE SYSTEM FUNCTIONS BEGIN////////////////////////////////////////////////
	
	  @Override
    public void setLangBundle( I18NBundle langBundle) {          
        this.langBundle = langBundle;
    }
    
     public void showAlertBoxNeutral(final String alertBoxTitle, final String alertBoxMessage, final String alertBoxButtonText) {
  	   	runOnUiThread(new Runnable() {
                     public void run() {
                             new AlertDialog.Builder(AndroidLauncher.this)
                                             .setTitle(alertBoxTitle)
                                             .setMessage(alertBoxMessage)
                                             .setNeutralButton(alertBoxButtonText,
                                                             new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog,
                                                                                     int whichButton) {
                                                                     }
                                                             }).create().show();
                     }
             });
     }
     
     public boolean showAlertBoxYesNo(final String alertBoxTitle, final String alertBoxMessage) {
 	   	runOnUiThread(new Runnable() {
                    public void run() {
                            new AlertDialog.Builder(AndroidLauncher.this)
                                            .setTitle(alertBoxTitle)
                                            .setMessage(alertBoxMessage)
                                            .setNeutralButton(android.R.string.ok,
                                                             new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog, int whichButton) {
                                                                     }
                                                             })
                                            .create().show();
                            
                    }
                    
            });
 	   		return true;
    }


    
     public void openUri(String uri) {

             if(uri.toLowerCase().contains("https://facebook.com")){
          	   try {
		   	            
		   	          String fburi = uri.replace("https:", "facebook:");
		   	          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fburi));
		   	          AndroidLauncher.this.startActivity(intent);
		   	         } catch (Exception e) {
		   	        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		   	        	AndroidLauncher.this.startActivity(intent);
		   	         }
             }else{
          	   
                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                 AndroidLauncher.this.startActivity(intent);
             }
     }
     
     public boolean shareContent(String subject, String message, String chooserTitle) {

     	Intent intent = new Intent(Intent.ACTION_SEND);
         intent.setType("text/plain");
         intent.putExtra(Intent.EXTRA_SUBJECT, subject);
         intent.putExtra(Intent.EXTRA_TEXT, message);
         
         AndroidLauncher.this.startActivity(Intent.createChooser(intent, chooserTitle));
         return true;
     }

	
	public void showToast(final CharSequence toastMessage, final int toastDuration) {
		 
		runOnUiThread(new Runnable() {
             public void run() {
            	 	//There are only two toast leanghs ...short or long
            	 	int lenghtToast;
            	 	if(toastDuration==0) 
            	 		lenghtToast = Toast.LENGTH_SHORT;
            	 	else
            	 		lenghtToast = Toast.LENGTH_LONG;
            	 	
                     Toast.makeText(AndroidLauncher.this, toastMessage, lenghtToast).show();
             }
		 });				
	}



	@Override
	public void appStoreInfo() {
		
		try {
			AndroidLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AndroidLauncher.this.getPackageName())));
		} catch (ActivityNotFoundException e) {
			String str ="https://play.google.com/store/apps/details?id=" + AndroidLauncher.this.getPackageName();
		 	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
		 	AndroidLauncher.this.startActivity(browserIntent);        	    	    	
		}
		
	}



	@Override
	public void appStoreCatalog() {
		try {
			AndroidLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + developerPublisherId)));
		} catch (ActivityNotFoundException e) {
			String str ="https://play.google.com/store/apps/developer?id=" + developerPublisherId;
		 	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
		 	AndroidLauncher.this.startActivity(browserIntent);        	    	    	
		}
		
	}
	
	  
	
	
	
	////////////////////////////////////////////////////BASE SYSTEM FUNCTIONS END////////////////////////////////////////////////
	
	  
	  
	//////////////////////////////////////////////////GOOGLE ANALYTICS BEGIN////////////////////////////////////////////////
	  
	  @Override
		public void initialize() {
			 //Google Analytics. Get tracker.
			
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
			
		    //Tracker splineTime = ((MyApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);
		    
		    //http://stackoverflow.com/questions/22611295/android-google-analytics-availability-in-google-play-services
		    //THIS IS THE REAL: http://developer.android.com/reference/com/google/android/gms/analytics/GoogleAnalytics.html
		    //It reads on AndroidManifest.xml:  <meta-data android:name="com.google.android.gms.analytics.globalConfigResource" android:resource="@xml/app_tracker" /> 
		    
		    //OLD EasyTracker.getInstance(this).activityStart(this);  // Google Analytics
			Activity a = (Activity) this.getContext();
		    GoogleAnalytics.getInstance(this.getContext()).reportActivityStart(a); 
		    
		    //GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);//**************************
		    //GoogleAnalytics.getInstance(this).setDispatchPeriod(5); //GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(5);
		    //GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(5);
		    
		    // Set screen name.
		    // Where splinePath is a String representing the screen name.
		    t.setScreenName(null);
		    // Send a screen view. (And session)
		    //https://developers.google.com/analytics/devguides/collection/android/v4/sessions
		    //http://stackoverflow.com/questions/19254023/short-session-lengths-in-google-analytics-for-android
		    //splineTime.send(new HitBuilders.AppViewBuilder().setNewSession().build());
		    t.send(new HitBuilders.AppViewBuilder().build());
			
		}


		@Override
		public void trackPageView(String path) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
			
			 // Set screen name.
	        t.setScreenName(path);

	        // Send a screen view.
	        t.send(new HitBuilders.AppViewBuilder().build());
			
		}


		@Override
		public void trackEvent(String category, String subCategory) {
			//Report Event to Google Analytics
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
		  	  	
			t.send(new HitBuilders.EventBuilder()
		        .setCategory(category)
		        .setAction(subCategory)	        
		        .build());
			
		}
		
		@Override
		public void trackEvent(String category, String subCategory, String label, int value) {
			//Report Event to Google Analytics
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
		  	  	
			t.send(new HitBuilders.EventBuilder()
		        .setCategory(category)
		        .setAction(subCategory)
		        .setLabel(label)
		        .setValue(value)
		        .build());
			
		}

		
		@Override
		public void stop() {
			
			Activity a = (Activity) this.getContext();
			GoogleAnalytics.getInstance(a).reportActivityStop(a);
			
		}
	//////////////////////////////////////////////////GOOGLE ANALYTICS END////////////////////////////////////////////////
	
	//////////////////////////////////////////////////GOOGLE ADS SERVICES BEGIN////////////////////////////////////////////////
	  
	@Override
	public void showAds(boolean show) {
		
		      runOnUiThread(new Runnable() {
		        public void run() {
		new AlertDialog.Builder(AndroidLauncher.this)
      .setTitle("test")
      .setMessage("testing")
      .setNeutralButton("okkk",
                      new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog,
                                              int whichButton) {
                              }
                      }).create().show();
		        }
		      });
		    
	}

	@Override
	public void displayInterstitial() {
		try {
		      runOnUiThread(new Runnable() {
		        public void run() {
		        	 if (interstitialAdExit.isLoaded()) {
		 	            interstitialAdExit.show();
		 	            //Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
		 	          }		
		        }
		      });
		    } catch (Exception e) {
		    }
	    
	}

	@Override
	  public void showOrLoadInterstital() {
	    try {
	      runOnUiThread(new Runnable() {
	        public void run() {
	        	//Toast.makeText(getApplicationContext(), "Interstitial Invoked", Toast.LENGTH_SHORT).show();
	        	
	          if (interstitialAd.isLoaded()) {
	            interstitialAd.show();
	            //Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
	          } 
	          else {
	            AdRequest interstitialRequest = new AdRequest.Builder().build();
	            interstitialAd.loadAd(interstitialRequest);
	            //Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
	          }
	        }
	      });
	    } catch (Exception e) {
	    }
	  }
	
	
	//////////////////////////////////////////////////GOOGLE ADS SERVICES END////////////////////////////////////////////////	
	
	
	
	////////////////////////////////////////////////////GOOGLE GAME SERVICES BEGIN////////////////////////////////////////////////
	//http://theinvader360.blogspot.co.uk/2013/10/google-play-game-services-tutorial-example.html
	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}
	
	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	
	}
	
	@Override
	public void submitScoreGPGS(String leaderboardid, int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardid, score);
	
	}
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	
	}
	
	@Override
	public void getLeaderboardGPGS(String leaderboardid) {
		if (gameHelper.isSignedIn()) {
		    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderboardid), 100);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	
	}
	
	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
		    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	
	}
	
	@Override
	public void onSignInFailed() {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onSignInSucceeded() {
		//Record that the user uses GPGS and also publish its first score after login
		if(preferences.getBoolean(Chikungunya.PREF_GAMESERVICES_USED) == false) {
			
								
				//Publish first user score
				submitScoreGPGS( Chikungunya.LEADERBOARD_ID, preferences.getInteger(Chikungunya.PREF_CURRENT_SCORE, 0));
				
				//Open the leaderboard again(Google Game Services does not opens it af first login(register))
				getLeaderboardGPGS(Chikungunya.LEADERBOARD_ID);
				
				preferences.putBoolean( Chikungunya.PREF_GAMESERVICES_USED, true);
				preferences.flush();
				
			
		}
	
	}
	
	////////////////////////////////////////////////////GOOGLE GAME SERVICES END////////////////////////////////////////////////
	
		
}
