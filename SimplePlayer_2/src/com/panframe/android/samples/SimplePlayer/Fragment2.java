package com.panframe.android.samples.SimplePlayer;

import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.panframe.android.lib.PFAsset;
import com.panframe.android.lib.PFAssetObserver;
import com.panframe.android.lib.PFAssetStatus;
import com.panframe.android.lib.PFNavigationMode;
import com.panframe.android.lib.PFObjectFactory;
import com.panframe.android.lib.PFView;

public class Fragment2 extends Fragment implements PFAssetObserver
{
    
    View mView;
    
    PFView mPfview;
    
    PFAsset mPfasset;
    
    Timer mScrubberMonitorTimer;
    
    RelativeLayout mLayout;
    
    SimplePlayerActivity activity;
    
    Boolean isPlaying = false;
    
    float mCurrentTime = 0;
    
    PFNavigationMode mCurrentNavigationMode = PFNavigationMode.MOTION;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_2, container, false);
        
        mLayout = (RelativeLayout) mView.findViewById(R.id.relate);
        
        activity = (SimplePlayerActivity) getActivity();
        
        mPfview = PFObjectFactory.view(activity);
        mPfasset =
            PFObjectFactory.assetFromUri(activity,
                Uri.parse("android.resource://" + activity.getPackageName()
                    + "/" + R.raw.skyrim360),
                this);
        
        mPfview.displayAsset(mPfasset);
        mPfview.setNavigationMode(mCurrentNavigationMode);
        
        mLayout.addView(mPfview.getView(), 0);
        
        Log.i("1111111", "android.resource://" + activity.getPackageName()
            + "/" + R.raw.skyrim360);
        
        //        mPfasset.play();
        
        return mView;
    }
    
    @Override
    public void onStatusMessage(final PFAsset asset, PFAssetStatus status)
    {
        // TODO Auto-generated method stub
        switch (status)
        {
            case LOADED:
                break;
            case DOWNLOADING:
                break;
            case DOWNLOADED:
                break;
            case DOWNLOADCANCELLED:
                break;
            case PLAYING:
                isPlaying = true;
                activity.getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                activity.mScrubber.setEnabled(true);
//                activity.mScrubber.setMax((int) asset.getDuration());
                activity.mPlayButton.setText("pause");
//                mScrubberMonitorTimer = new Timer();
//                final TimerTask task = new TimerTask()
//                {
//                    public void run()
//                    {
//                        if (activity.isUpdateThumb)
//                        {
//                            activity.mScrubber.setProgress((int) asset.getPlaybackTime());
//                        }
//                        
//                    }
//                };
//                mScrubberMonitorTimer.schedule(task, 0, 50);
                break;
            case PAUSED:
                Log.d("SimplePlayer", "Paused");
                isPlaying = false;
                activity.mPlayButton.setText("play");
                break;
            case STOPPED:
                Log.d("SimplePlayer", "Stopped");
                isPlaying = false;
                activity.mPlayButton.setText("play");
                if (mScrubberMonitorTimer != null)
                {
                    mScrubberMonitorTimer.cancel();
                    mScrubberMonitorTimer = null;
                }
                activity.mScrubber.setProgress(0);
                activity.mScrubber.setEnabled(false);
                activity.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;
            case COMPLETE:
                Log.d("SimplePlayer", "Complete");
                activity.mPlayButton.setText("play");
                if (mScrubberMonitorTimer != null)
                {
                    mScrubberMonitorTimer.cancel();
                    mScrubberMonitorTimer = null;
                }
                activity.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;
            case ERROR:
                Log.d("SimplePlayer", "Error");
                break;
        }
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        if (mPfasset != null)
        {
            if (mPfasset.getStatus() == PFAssetStatus.PLAYING)
                mPfasset.pause();
        }
    }
    
    @Override
    public void onDestroyView()
    {
        // TODO Auto-generated method stub
        super.onDestroyView();
        
        mPfasset.stop();
    }
}
