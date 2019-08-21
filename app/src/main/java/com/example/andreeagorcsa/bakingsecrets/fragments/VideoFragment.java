package com.example.andreeagorcsa.bakingsecrets.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {

    public static final String LOG_TAG = VideoFragment.class.getSimpleName();

    // constants for the onSaveInstanceState()
    private static final String PLAYBACK_POSITION = "playbackPosition";
    private static final String STEP_INDEX = "mStepIndex";
    private static final String CURRENT_WINDOW = "currentWindow";
    private static final String PLAY_WHEN_READY = "playWhenReady";
    private static final String LOGO_IMAGE = "mLogoImage";

    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.step_switcher)
    TextSwitcher mStepSwitcher;
    @BindView(R.id.button_next)
    Button mNextButton;
    @BindView(R.id.button_previous)
    Button mPreviousButton;
    @BindView(R.id.image_logo)
    ImageView mLogoImage;
    @BindBool(R.bool.is_tablet)
    boolean mIsTablet;
    @BindBool(R.bool.is_landscape)
    boolean mIsLandscape;
    private ExoPlayer exoPlayer;
    private boolean mPlayWhenReady = true;
    private int mCurrentWindow = 0;
    private long mPlaybackPosition = 0;
    private String mVideoURL;
    private String mThumbnailURL;
    private int mStepIndex;
    private ArrayList<Step> mStepList;
    private Step mStep;
    private TextView mTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);

        // for tablet
        if (mIsTablet) {
            Bundle bundle = getArguments();

            if (bundle != null) {

                mStep = bundle.getParcelable(StepFragment.STEP_KEY);
                mVideoURL = mStep.getVideoURL();
                mThumbnailURL = mStep.getThumbnailURL();
                mStepIndex = bundle.getInt(StepFragment.STEP_POSITION_KEY);
                mStepList = bundle.getParcelableArrayList(StepFragment.STEP_LIST_KEY);

                // no video
                if (mVideoURL.isEmpty() && mThumbnailURL.isEmpty()) {
                    showImage();
                    // show toast
                    StyleableToast
                            .makeText(
                                    getActivity().getBaseContext(),
                                    getResources().getString(R.string.no_video),
                                    R.style.toast_style)
                            .show();
                } else {
                    showPlayer();
                }
            }

            // for phone
        } else {

            mStep = getActivity().getIntent().getParcelableExtra(StepFragment.STEP_KEY);
            mVideoURL = mStep.getVideoURL();
            mThumbnailURL = mStep.getThumbnailURL();
            mStepIndex = getActivity().getIntent().getIntExtra(StepFragment.STEP_POSITION_KEY, 0);
            mStepList = getActivity().getIntent().getParcelableArrayListExtra(StepFragment.STEP_LIST_KEY);
        }
        // at device rotation
        if (savedInstanceState != null) {

            mStepIndex = savedInstanceState.getInt(STEP_INDEX);
            mStep = savedInstanceState.getParcelable(StepFragment.STEP_KEY);
            mVideoURL = mStep.getVideoURL();
            mThumbnailURL = mStep.getThumbnailURL();
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);

            // no video
            if (mVideoURL.isEmpty() && mThumbnailURL.isEmpty()) {
                int logoImage = savedInstanceState.getInt(LOGO_IMAGE);
                mLogoImage.setImageResource(logoImage);
                view.setBackgroundColor(Color.parseColor("#FFF0AE"));
                showImage();
                // show toast
                StyleableToast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.no_video), R.style.toast_style).show();
            }
        }

        // hide previous button at the beginning of the step list
        if (mStepIndex == 0) {
            mPreviousButton.setVisibility(View.INVISIBLE);
            // hide next button at the end of the step list
        } else if (mStepIndex == mStepList.size() - 1) {
            mNextButton.setVisibility(View.INVISIBLE);
        }

        // next button navigation
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // end of the list
                if (mStepIndex == mStepList.size() - 2) {
                    mNextButton.setVisibility(View.INVISIBLE);
                    // beginning of the list
                } else if (mStepIndex == 0) {
                    mPreviousButton.setVisibility(View.VISIBLE);
                    // default case
                } else {
                    mStepIndex++;
                    loadNewVideo(mStepIndex);
                }
            }
        });

        // previous button navigation
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // beginning of the list
                if (mStepIndex == 1) {
                    mPreviousButton.setVisibility(View.INVISIBLE);
                    // end of the list
                } else if (mStepIndex == mStepList.size() - 1) {
                    mNextButton.setVisibility(View.VISIBLE);
                } else {
                    mStepIndex--;
                    loadNewVideo(mStepIndex);
                }
            }
        });

        // creates the TextView, in which the TextSwitcher will display its text, programmatically
        mStepSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                mTextView = new TextView(getActivity().getBaseContext());
                mTextView.setTextSize(15);
                mTextView.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                mTextView.setGravity(Gravity.CENTER);
                return mTextView;
            }
        });

        mStep = mStepList.get(mStepIndex);
        mStepSwitcher.setText(mStep.getDescription());

        return view;
    }

    /**
     * shows the placeholder image in case of no video,
     * hides the player
     */
    private void showImage() {
        playerView.setVisibility(View.GONE);
        mLogoImage.setVisibility(View.VISIBLE);
        mStepSwitcher.setVisibility(View.VISIBLE);
        mPreviousButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    /**
     * shows the player
     * hides the placeholder image
     */
    private void showPlayer() {
        playerView.setVisibility(View.VISIBLE);
        mLogoImage.setVisibility(View.GONE);
        mPreviousButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    /**
     * loads the video according to which step we are
     *
     * @param stepIndex
     */
    private void loadNewVideo(int stepIndex) {

        // gets the video selected by the new position
        mStep = mStepList.get(stepIndex) ;

        // Extract the video URL from the selected step
        mVideoURL = mStep.getVideoURL();

        // shows the new video description on the screen
        mStepSwitcher.setText(mStep.getDescription());

        // prepares the player based on the selected video
        Uri uri = Uri.parse(mVideoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        exoPlayer.prepare(mediaSource, true, false);

        // if there is not a video available and we are not on a tablet,
        // shows a Toast to let the user know
        if (mVideoURL.isEmpty() && mThumbnailURL.isEmpty()) {
            if (!mIsTablet) {
                StyleableToast
                        .makeText(
                                getActivity().getBaseContext(),
                                getResources().getString(R.string.no_video),
                                R.style.toast_style)
                        .show();
            }
            showImage();
        } else {
            showPlayer();
        }
    }

    /**
     * initializes the ExoPlayer
     */
    private void initializePlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity().getBaseContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(exoPlayer);

        // creates an Uri object from a String
        Uri uri = Uri.parse(mVideoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        exoPlayer.prepare(mediaSource, true, false);

        // after preparing the player
        exoPlayer.setPlayWhenReady(mPlayWhenReady);
        exoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
    }

    /**
     * creates the media source
     *
     * @param uri
     * @return
     */
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-recipe")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (exoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int orientation = getResources().getConfiguration().orientation;
        // if orientation landscape
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && mIsTablet == false) {
            hideSystemUi();
            if ((exoPlayer == null)) {
                initializePlayer();
            }
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            releaseExoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            releaseExoPlayer();
        }
    }

    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            mPlaybackPosition = exoPlayer.getCurrentPosition();
            mCurrentWindow = exoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    // onSaveInstanceState
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_INDEX, mStepIndex);
        // the mVideoUrl and the mDescription we will extract from the mStep
        outState.putParcelable(StepFragment.STEP_KEY, mStep);
        outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
        outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
        // save the state of the image, in case of no video available
        outState.putInt(LOGO_IMAGE, R.drawable.logo);
        //repush
    }
}
