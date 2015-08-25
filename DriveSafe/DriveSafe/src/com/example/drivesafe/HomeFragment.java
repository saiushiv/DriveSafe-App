package com.example.drivesafe;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class HomeFragment extends Fragment {
	
private View mDecorView;

private double mySpeed;

private static LocationManager locMan;
private static LocationListener locListener;


private TextView speedTextView;

public static boolean appEnabled;
private boolean isSilenced;

private static AudioManager audiomanage;

public static Context context;

ToggleButton  tb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	super.onCreateView(inflater, container, savedInstanceState);
        
		appEnabled = false;
		isSilenced = false;
		
    	View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    	
    	speedTextView=(TextView)rootView.findViewById(R.id.textView2);
    	Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/digital-7.ttf");
    	 speedTextView.setTypeface(typeFace);
        
     	mDecorView = getActivity().getWindow().getDecorView();
    	
     	tb = (ToggleButton)rootView.findViewById(R.id.toggleButton1);
     	
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //The toggle is enabled
                     mDecorView.setSystemUiVisibility(
		                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		                  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		                  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		                  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		                  | View.SYSTEM_UI_FLAG_FULLSCREEN
		                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 
		                  | View.INVISIBLE);  
                     
					 appEnabled = true;
					 
									
                } else {
                    // The toggle is disabled
                	mDecorView.setSystemUiVisibility(0);
					appEnabled=false;
                
                }  
            }
		
        });
		
		mySpeed = 0;
        locMan = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locListener = new SpeedLocationListener();
        
        if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            //tView.setText("GPS debug");
        } 
        else 
        {
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            //tView.setText("Network debug");
        }
        return rootView;
    }
	
	private class SpeedLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc)
		{
			if(loc!=null)
			{
				//if(loc.hasSpeed())
				//{
					mySpeed = loc.getSpeed();
					mySpeed *= 2.2369362920544;	//converting m/s to mph
					speedTextView.setText(String.format("%.1f",mySpeed));
					
					if(appEnabled)
					{
						//if(mySpeed >= 20)
						//{
							if(!isSilenced)
							{
								isSilenced = true;
	/*AudioManager*/ audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                     audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
							}
                     	//}
                     		
					}
					else
					{
						if(isSilenced)
						{
							isSilenced = false;
	/*AudioManager*/ audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                    audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						}
					}
				//}
			}
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{	
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		
		@Override
		public void onStatusChanged(String provider,int status,Bundle extras)
		{
		}
	}
	

}