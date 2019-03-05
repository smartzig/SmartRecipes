package recipes.smartzig.com.smartrecipes;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import recipes.smartzig.com.smartrecipes.to.Step;
import recipes.smartzig.com.smartrecipes.utils.Constants;

public class RecipeStepsFragment extends Fragment {

    public static final String POSITION = "position";
    public static final String READY = "ready";
    @BindView(R.id.step_thumbnail_image)
    ImageView thumbnail;
    @BindView(R.id.instruction_text)
    TextView mTvInstructions;
    @BindView(R.id.exoplayer_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.nested_scrollView)
    NestedScrollView scrollView;
    private SimpleExoPlayer player;
    private long currentPosition = 0;
    private boolean readyToPlay = true;
    private Step step;

    // FIXME: 7/2/2018 fix the part when there is no video
    public RecipeStepsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Constants.STEP_SELECTED_KEY)) {
            step = getArguments().getParcelable(Constants.STEP_SELECTED_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_steps_fragment, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION)) {
            currentPosition = savedInstanceState.getLong(POSITION);
            readyToPlay = savedInstanceState.getBoolean(READY);
        }
        mTvInstructions.setText(step.getDescription());
        initializePlayer(Uri.parse(step.getVideoURL()));

        if (!step.getThumbnailURL().isEmpty()) {
            Picasso.get()
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.ic_cake_black_24dp)
                    .into(thumbnail);

            thumbnail.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void initializePlayer(Uri uri) {
        if (player == null) {
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), selector, control);
            playerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(Objects.requireNonNull(getContext()), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);

            if (currentPosition != 0) {
                player.seekTo(currentPosition);
            }
            player.setPlayWhenReady(readyToPlay);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            currentPosition = player.getCurrentPosition();
            readyToPlay = player.getPlayWhenReady();
        }

        if (player != null) {
            player.stop();
            player.release();
        }
        player = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(step.getVideoURL())) {
            initializePlayer(Uri.parse(step.getVideoURL()));
        } else {
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION, currentPosition);
        outState.putBoolean(READY, readyToPlay);
    }
}
