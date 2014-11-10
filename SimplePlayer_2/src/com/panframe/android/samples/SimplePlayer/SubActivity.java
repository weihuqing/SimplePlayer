package com.panframe.android.samples.SimplePlayer;

import java.util.Timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;

public class SubActivity extends FragmentActivity implements
    OnSeekBarChangeListener
{
    
    //    PFView mPfview;
    //    
    //    PFAsset mPfasset;
    
    PFNavigationMode mCurrentNavigationMode = PFNavigationMode.MOTION;
    
    boolean isUpdateThumb = true;
    
    Timer mScrubberMonitorTimer;
    
    ViewGroup mFrameContainer;
    
    Button mStopButton;
    
    Button mPlayButton;
    
    Button mTouchButton;
    
    SeekBar mScrubber;
    
    Fragment3 fragment3;
    
    Fragment4 fragment4;
    
    /**
     * Creation and initalization of the Activitiy.
     * Initializes variables, listeners, and starts request of a movie list.
     *
     * @param  savedInstanceState  a saved instance of the Bundle
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_2);
        
        fragment3 =
            (Fragment3) getSupportFragmentManager().findFragmentById(R.id.frag_1);
        fragment4 =
            (Fragment4) getSupportFragmentManager().findFragmentById(R.id.frag_2);
        
        mFrameContainer = (ViewGroup) findViewById(R.id.framecontainer);
        mFrameContainer.setBackgroundColor(0xFF000000);
        
        mPlayButton = (Button) findViewById(R.id.playbutton);
        mStopButton = (Button) findViewById(R.id.stopbutton);
        mTouchButton = (Button) findViewById(R.id.touchbutton);
        mScrubber = (SeekBar) findViewById(R.id.scrubber);
        
        mPlayButton.setOnClickListener(playListener);
        mStopButton.setOnClickListener(stopListener);
//        mTouchButton.setOnClickListener(touchListener);
        //        mScrubber.setOnSeekBarChangeListener(this);
        
        mScrubber.setEnabled(false);
        showControls(true);
        
    }
    
    /**
     * Show/Hide the playback controls
     *
     * @param  bShow  Show or hide the controls. Pass either true or false.
     */
    public void showControls(boolean bShow)
    {
        int visibility = View.GONE;
        
        if (bShow)
            visibility = View.VISIBLE;
        
        mPlayButton.setVisibility(visibility);
        mStopButton.setVisibility(visibility);
        mTouchButton.setVisibility(visibility);
        mScrubber.setVisibility(visibility);
    }
    
    /**
     * Click listener for the play/pause button
     *
     */
    private OnClickListener playListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            if (fragment3.mPfasset.getStatus() == PFAssetStatus.PLAYING)
            {
                fragment3.mPfasset.pause();
            }
            else
                fragment3.mPfasset.play();
            
            if (fragment4.mPfasset.getStatus() == PFAssetStatus.PLAYING)
            {
                fragment4.mPfasset.pause();
            }
            else
                fragment4.mPfasset.play();
        }
    };
    
    /**
     * Click listener for the stop/back button
     *
     */
    private OnClickListener stopListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            fragment3.mPfasset.stop();
            fragment4.mPfasset.stop();
        }
    };
    
    /**
     * Click listener for the navigation mode (touch/motion (if available))
     *
     */
    private OnClickListener touchListener = new OnClickListener()
    {
        public void onClick(View v)
        {
//            Intent intent = new Intent();
//            intent.setClass(SimplePlayerActivity.this, SubActivity.class);
//            startActivity(intent);
            
            //            Button touchButton = (Button) findViewById(R.id.touchbutton);
            //            if (mCurrentNavigationMode == PFNavigationMode.TOUCH)
            //            {
            //                mCurrentNavigationMode = PFNavigationMode.MOTION;
            //                touchButton.setText("motion");
            //            }
            //            else
            //            {
            //                mCurrentNavigationMode = PFNavigationMode.TOUCH;
            //                touchButton.setText("touch");
            //            }
            //            fragment1.mPfview.setNavigationMode(mCurrentNavigationMode);
            //            fragment2.mPfview.setNavigationMode(mCurrentNavigationMode);
        }
    };
    
    /**
     * Setup the options menu
     *
     * @param menu The options menu
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Called when pausing the app.
     * This function pauses the playback of the asset when it is playing.
     *
     */
    public void onPause()
    {
        super.onPause();
        if (fragment3.mPfasset != null)
        {
            if (fragment3.mPfasset.getStatus() == PFAssetStatus.PLAYING)
                fragment3.mPfasset.pause();
        }
        
        if (fragment4.mPfasset != null)
        {
            if (fragment4.mPfasset.getStatus() == PFAssetStatus.PLAYING)
                fragment4.mPfasset.pause();
        }
    }
    
    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * 
     * @param seekbar The SeekBar whose progress has changed
     * @param progress The current progress level.
     * @param fromUser True if the progress change was initiated by the user.
     * 
     */
    public void onProgressChanged(SeekBar seekbar, int progress,
        boolean fromUser)
    {
        if (!(fragment3.mPfasset.getPlaybackTime() > 0 && fragment4.mPfasset.getPlaybackTime() > 0))
        {
            fragment3.mPfasset.setPLaybackTime(0);
            fragment4.mPfasset.setPLaybackTime(0);
        }
        if (fragment3.mPfasset.getPlaybackTime() != fragment3.mPfasset.getPlaybackTime())
        {
            fragment3.mPfasset.setPLaybackTime(seekbar.getProgress() / 1000);
            fragment4.mPfasset.setPLaybackTime(seekbar.getProgress() / 1000);
        }
    }
    
    /**
     * Notification that the user has started a touch gesture.
     * In this function we signal the timer not to update the playback thumb while we are adjusting it.
     * 
     * @param seekbar The SeekBar in which the touch gesture began
     * 
     */
    public void onStartTrackingTouch(SeekBar seekbar)
    {
        isUpdateThumb = false;
    }
    
    /**
     * Notification that the user has finished a touch gesture. 
     * In this function we request the asset to seek until a specific time and signal the timer to resume the update of the playback thumb based on playback.
     * 
     * @param seekbar The SeekBar in which the touch gesture began
     * 
     */
    public void onStopTrackingTouch(SeekBar seekbar)
    {
        Log.i("11111",
            fragment3.mPfasset.getPlaybackTime() + "@@@@@"
                + seekbar.getProgress());
        fragment3.mPfasset.setPLaybackTime(seekbar.getProgress() / 1000);
        fragment4.mPfasset.setPLaybackTime(seekbar.getProgress() / 1000);
        isUpdateThumb = true;
    }
    
    public void onStartCommand(Intent intent, int flags, int startId)
    {
        fragment3.mPfasset.play();
        fragment4.mPfasset.play();
    }
    
}
