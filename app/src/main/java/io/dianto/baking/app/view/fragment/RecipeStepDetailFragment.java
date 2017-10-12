package io.dianto.baking.app.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.App;
import io.dianto.baking.app.R;
import io.dianto.baking.app.events.Recipe_Step_Event;
import io.dianto.baking.app.model.Steps;
import io.dianto.baking.app.util.Constant;

import static io.dianto.baking.app.util.Constant.Data.EXTRA_STEP;
import static io.dianto.baking.app.util.Constant.Data.EXTRA_STEP_FIRST;
import static io.dianto.baking.app.util.Constant.Data.EXTRA_STEP_LAST;
import static io.dianto.baking.app.util.Constant.Data.EXTRA_STEP_NUMBER;

public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.detail_step_instruction)
    TextView mDetailStepInstruction;

    @BindView(R.id.detail_step_video)
    SimpleExoPlayerView mDetailStepVideo;

    @BindView(R.id.detail_step_nav_prev)
    Button mDetailStepPrev;

    @BindView(R.id.detail_step_nav_next)
    Button mDetailStepNext;

    @BindView(R.id.detail_step_image)
    ImageView mDetailStepImage;

    private SimpleExoPlayer mPlayer;
    private Steps mSteps;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady;
    private int mNumber;
    private boolean mFirst;
    private boolean mLast;

    public RecipeStepDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        initView(rootView);

        String strStepJson = getArguments().getString(EXTRA_STEP);
        mSteps = App.getInstance().getGson().fromJson(strStepJson, Steps.class);
        mNumber = getArguments().getInt(EXTRA_STEP_NUMBER);
        mFirst = getArguments().getBoolean(EXTRA_STEP_FIRST);
        mLast = getArguments().getBoolean(EXTRA_STEP_LAST);

        mDetailStepInstruction.setText(mSteps.getDescription());

        mDetailStepImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(mSteps.getThumbnailUrl()) && !mSteps.getThumbnailUrl().substring(mSteps.getThumbnailUrl().length() - 4, mSteps.getThumbnailUrl().length()).equals(".mp4")) {
            mDetailStepImage.setVisibility(View.VISIBLE);
            Constant.Function.setImageResource(getContext(), mSteps.getThumbnailUrl(), mDetailStepImage);
        }

        mDetailStepPrev.setVisibility(View.VISIBLE);
        mDetailStepNext.setVisibility(View.VISIBLE);

        if (mFirst) mDetailStepPrev.setVisibility(View.GONE);
        if (mLast) mDetailStepNext.setVisibility(View.GONE);

        return rootView;
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    private void initView(View rootView) {
        ButterKnife.bind(this, rootView);

        mDetailStepPrev.setOnClickListener(this);
        mDetailStepNext.setOnClickListener(this);
    }

    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mDetailStepVideo.setPlayer(mPlayer);

        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

        if (TextUtils.isEmpty(mSteps.getVideoUrl()) && TextUtils.isEmpty(mSteps.getThumbnailUrl())) {
            mDetailStepVideo.setVisibility(View.GONE);
        } else {
            mDetailStepVideo.setVisibility(View.VISIBLE);
            Uri uri = null;
            if (!TextUtils.isEmpty(mSteps.getVideoUrl())) {
                uri = Uri.parse(mSteps.getVideoUrl());
            } else if (!TextUtils.isEmpty(mSteps.getThumbnailUrl()) && mSteps.getThumbnailUrl().substring(mSteps.getThumbnailUrl().length() - 4, mSteps.getThumbnailUrl().length()).equals(".mp4")) {
                uri = Uri.parse(mSteps.getThumbnailUrl());
            }
            MediaSource mediaSource = buildMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_step_nav_prev:
                previousStep();
                break;
            case R.id.detail_step_nav_next:
                nextStep();
                break;
            default:
                break;
        }
    }

    private void previousStep() {
        EventBus eventBus = App.getInstance().getEventBus();
        Recipe_Step_Event event = new Recipe_Step_Event();
        event.setSelectedPosition(mNumber - 1);
        eventBus.post(event);
    }

    private void nextStep() {
        EventBus eventBus = App.getInstance().getEventBus();
        Recipe_Step_Event event = new Recipe_Step_Event();
        event.setSelectedPosition(mNumber + 1);
        eventBus.post(event);
    }
}