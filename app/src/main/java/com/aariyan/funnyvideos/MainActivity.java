package com.aariyan.funnyvideos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.funnyvideos.Model.VideoModel;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private void addVideos() {
        // We will add all videos here

        //Video Details Here Here
        hashMap = new HashMap<>();
        hashMap.put("vdo_id", "upHtrn4_4fw");
        hashMap.put("vdo_title", "Allah di kasam tu mainu enna pyara ho gaya");
        hashMap.put("vdo_desciption", "Teri har cheez jannat ae -hasna vi jannt ae");
        arrayList.add(hashMap);
        //------------->>>>>>>>>>>>>>>>>


        hashMap = new HashMap<>();
        hashMap.put("vdo_id", "aNcxgxHcGYg");
        hashMap.put("vdo_title", "Heer - Full Song | Jab Tak Hai Jaan");
        hashMap.put("vdo_desciption", "Shah Rukh Khan | Katrina Kaif | Harshdeep Kaur | A. R.");
        arrayList.add(hashMap);
        //------------->>>>>>>>>>>>>>>>>


        hashMap = new HashMap<>();
        hashMap.put("vdo_id", "m8cMzbs9-i8");
        hashMap.put("vdo_title", "Khoma Koro Ami Valo Nei");
        hashMap.put("vdo_desciption", "ক্ষমা করো আমি ভালো নেই | Bangla Lyrics | Dev | Anupam Ray | Sad Song 2021");
        arrayList.add(hashMap);
        //------------->>>>>>>>>>>>>>>>>


        hashMap = new HashMap<>();
        hashMap.put("vdo_id", "Gi050faPB3I");
        hashMap.put("vdo_title", "Zero 2 Hero in Android (Season 1)");
        hashMap.put("vdo_desciption", "I have become one of the rare people who dont know how to quit!");
        arrayList.add(hashMap);


        //------------->>>>>>>>>>>>>>>>>
        hashMap = new HashMap<>();
        hashMap.put("vdo_id", "HWjQGO_tjSI");
        hashMap.put("vdo_title", "Motivational Lines You Must Listen");
        hashMap.put("vdo_desciption", "Best Motivational Audio Compilation | Reprogram Your Mind");
        arrayList.add(hashMap);


    }

    //=======================================================
    //====================================================================
    //====================================================================

    //    TextView tvDate;
//    LinearLayout layoutContainer;
    FloatingActionButton fabButton;
    RelativeLayout _rootLay;
    //AdView mAdView;
    InterstitialAd mInterstitialAd;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer myYoutubePlayer;
    MyPlaybackEventListener myPlaybackEventListener;
    LinearLayout layPlayer;
    ImageView imngClosePlayer, imgPlayPause, imgPrevious, imgNext;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;
    int PLAYING_NOW = 0;
    //View viewShade;
    boolean playingVideo = false;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private TextView liveBtn, allBtn;
    private DatabaseReference videoRef;
    private List<VideoModel> videoList = new ArrayList<>();
    private TemplateView templateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FirebaseApp.initializeApp(MainActivity.this);

        videoRef = FirebaseDatabase.getInstance().getReference().child("Video");

//        tvDate = findViewById(R.id.tvDate);
//        layoutContainer = findViewById(R.id.layoutContainer);
        fabButton = findViewById(R.id.fabButton);
//        mAdView = findViewById(R.id.mAdView);
        _rootLay = findViewById(R.id._rootLay);
        youTubePlayerView = findViewById(R.id.youTubePlayerView);
        layPlayer = findViewById(R.id.layPlayer);
        imngClosePlayer = findViewById(R.id.imngClosePlayer);
        imgPlayPause = findViewById(R.id.imgPlayPause);
        imgNext = findViewById(R.id.imgNext);
        imgPrevious = findViewById(R.id.imgPrevious);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressbar);

        liveBtn = findViewById(R.id.liveBtn);
        allBtn = findViewById(R.id.allBtn);
        //viewShade = findViewById(R.id.viewShade);

        // Load Admob Ads
        //loadBannerAd();
        loadFullscreenAd();


        //init Youtube Player View
        youTubePlayerView.initialize("ABCD", MainActivity.this);
        myPlaybackEventListener = new MyPlaybackEventListener();

        //addVideos();
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeListView("All");
                allBtn.setBackground(getResources().getDrawable(R.drawable.button_selected_background));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.button_background));
            }
        });

        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeListView("Live");
                allBtn.setBackground(getResources().getDrawable(R.drawable.button_background));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.button_selected_background));
            }
        });

        makeListView("All");


        //Fab Button onClick
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Check "+getString(R.string.app_name)+" app ♥ It's awesome! \n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        imngClosePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePlayer();

                //loadBannerAd();

//                viewShade.setVisibility(View.VISIBLE);
//                mAdView.setVisibility(View.VISIBLE);
                fabButton.setVisibility(View.VISIBLE);

                playingVideo = false;


            }
        });

        imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null && v.getTag() != null) {
                    String tag = v.getTag().toString();
                    if (tag.contains("PLAYING")) {
                        if (myYoutubePlayer != null) myYoutubePlayer.pause();
                        else
                            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_LONG).show();

                    } else if (tag.contains("PAUSED")) {
                        if (myYoutubePlayer != null) myYoutubePlayer.play();
                        else
                            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        });

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousVideo();
            }
        });


//        templateView = findViewById(R.id.templateView);
//        showNativeAds();


        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/live-sportsb/home"));
                startActivity(browserIntent);
            }
        });
    } // End of onCreate Bundle

    private void showNativeAds(Adapter.ViewHolder holder) {
        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, "ca-app-pub-4690204733818590/5906517620")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        holder.templateView.setNativeAd(nativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
    private void makeListView(String type) {

        if (type.equals("All")) {
            videoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        videoList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            VideoModel model = dataSnapshot.getValue(VideoModel.class);
                            videoList.add(model);
                        }
                        Collections.sort(videoList, Collections.reverseOrder());

                        createDummyList(videoList);

                        Adapter adapter = new Adapter(MainActivity.this, videoList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        if (type.equals("Live")) {
            videoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        videoList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            VideoModel model = dataSnapshot.getValue(VideoModel.class);
                            if (model.getType().equals("Live"))
                                videoList.add(model);
                        }
                        if (videoList.size() == 0) {
                            Toast.makeText(MainActivity.this, "No Data Found!", Toast.LENGTH_SHORT).show();
                        }
                        Collections.sort(videoList);
                        createDummyList( videoList);
                        Adapter adapter = new Adapter(MainActivity.this, videoList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No Data Found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void createDummyList(List<VideoModel> videoList) {
        VideoModel model = new VideoModel(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        videoList.add(0,model);
    }


//    private void loadBannerAd(){
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//                loadBannerAd();
//            }
//        });
//
//    }
    ///==============================================
    ///==============================================


    //==============================================
    private void showInterstitial() {
        // Show the ad if it's ready.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }
    //============================================


    // loadFullscreenAd method starts here.....
    private void loadFullscreenAd() {
        //Requesting for a fullscreen Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-4690204733818590/3342760004", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;

                //Fullscreen callback || Requesting again when an ad is shown already
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        //User dismissed the previous ad. So we are requesting a new ad here
                        loadFullscreenAd();

                        if (playingVideo == true && myYoutubePlayer != null && !myYoutubePlayer.isPlaying()) {

                            Handler handler = new Handler(Looper.getMainLooper());

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    myYoutubePlayer.play();
                                    playingVideo = false;

                                }
                            }, 500);

                        }


                    }


                }); // FullScreen Callback Ends here


            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }


        });

    }

    // loadFullscreenAd method ENDS  here..... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    ///==============================================
    ///==============================================


    ///==============================================
    ///==============================================


    ///==============================================
    ///==============================================

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Context context;
        List<VideoModel> list;

        public Adapter(Context context, List<VideoModel> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            VideoModel model = list.get(position);

            if (position != 0) {
                holder.title.setText(model.getVideoName());
                holder.description.setText(model.getVideoDescription());

                // Youtube thumnail link is like
                //https://i.ytimg.com/vi/<VIDEO ID>/0.jpg
                String thumb_link = "https://i.ytimg.com/vi/" + model.getVideoUrl() + "/0.jpg";
                Picasso.get().
                        load("" + thumb_link)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.thumbnailImage);

                holder.layItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PLAYING_NOW = position;
                        playVideo(model.getVideoUrl());

                        showInterstitial();
                    }
                });
            }


            if (position == 0) {
                holder.templateView.setVisibility(View.VISIBLE);
                holder.layItem.setVisibility(View.GONE);
                showNativeAds(holder);
            } else {
                holder.templateView.setVisibility(View.GONE);
                holder.layItem.setVisibility(View.VISIBLE);
            }
        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title, description;
            private ImageView thumbnailImage;
            private RelativeLayout layItem;

            private TemplateView templateView;

            public ViewHolder(@NonNull View convertView) {
                super(convertView);

                title = convertView.findViewById(R.id.tvTitle);
                description = convertView.findViewById(R.id.tvDescription);
                thumbnailImage = convertView.findViewById(R.id.imgThumb);
                layItem = convertView.findViewById(R.id.layItem);

                templateView = convertView.findViewById(R.id.templateView);
            }
        }
    }




    //================================================
    private void playVideo(String video_id) {

        if (myYoutubePlayer != null) {
            layPlayer.setVisibility(View.VISIBLE);
            layPlayer.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            myYoutubePlayer.loadVideo(video_id);
            myYoutubePlayer.play();
            playingVideo = true;
        } else {
            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
        }
    }

    //================================================
    private void closePlayer() {
        if (myYoutubePlayer != null && myYoutubePlayer.isPlaying()) myYoutubePlayer.pause();
        layPlayer.setVisibility(View.GONE);
        layPlayer.clearAnimation();
    }


    //==============================================
    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
        public void onBuffering(boolean arg0) {
            //updateLog("onBuffering(): " + String.valueOf(arg0));
            if (imgPlayPause != null) {
                imgPlayPause.setImageResource(R.drawable.buffer);
                imgPlayPause.setTag("BUFFERING");
            }
        }

        public void onPaused() {
            //updateLog("onPaused()");

            if (imgPlayPause != null) {
                imgPlayPause.setImageResource(R.drawable.icon_play);
                imgPlayPause.setTag("PAUSED");
            }

            //Toast.makeText(getApplicationContext(), "Paused", Toast.LENGTH_SHORT).show();
        }

        public void onPlaying() {
            //updateLog("onPlaying()");
            // Toast.makeText(getApplicationContext(), "Paying", Toast.LENGTH_SHORT).show();

            if (imgPlayPause != null) {
                imgPlayPause.setImageResource(R.drawable.icon_pause);
                imgPlayPause.setTag("PLAYING");
            }

        }

        public void onSeekTo(int arg0) {
            //updateLog("onSeekTo(): " + String.valueOf(arg0));
        }

        public void onStopped() {

            if (imgPlayPause != null) {
                imgPlayPause.setImageResource(R.drawable.icon_play);
            }

        }

    }

    //*******************************************************************


    //=================================================================


    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        public void onAdStarted() {
            //updateLog("onAdStarted()");
        }

        public void onError(
                com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
            //updateLog("onError(): " + arg0.toString());
            Toast.makeText(getApplicationContext(), "" + arg0.toString(), Toast.LENGTH_SHORT).show();
            //Log.d("loveRana", ""+arg0.ordinal());

            /*
            if (arg0.toString().contains("NOT_PLAYABLE") && YOUTUBE_PLAYER ){
                webViewPlayer();
            }
            else if (arg0.toString().contains("INTERNAL_ERROR") && YOUTUBE_PLAYER ){
                webViewPlayer();
            }

             */


        }

        public void onLoaded(String arg0) {
            //updateLog("onLoaded(): " + arg0);
        }

        public void onLoading() {
            //updateLog("onLoading()");
        }

        public void onVideoEnded() {
            // Toast.makeText(getApplicationContext(), "ended", Toast.LENGTH_LONG).show();
            playNextVideo();


        }


        public void onVideoStarted() {
            //updateLog("onVideoStarted()");
        }

    }
    //==============================================================


    //=================================================
    private void playNextVideo() {
        if (PLAYING_NOW >= (videoList.size() - 1))
            PLAYING_NOW = 0;
        else PLAYING_NOW++;

        VideoModel model = videoList.get(PLAYING_NOW);
        //HashMap<String, String> hashMap = arrayList.get(PLAYING_NOW);
        //String vdo_id = hashMap.get("vdo_id");
        //playVideo(vdo_id);
        playVideo(model.getVideoUrl());
    }


    private void playPreviousVideo() {
        if (PLAYING_NOW > 0) {
            PLAYING_NOW--;
//            HashMap<String, String> hashMap = arrayList.get(PLAYING_NOW);
//            String vdo_id = hashMap.get("vdo_id");
//            playVideo(vdo_id);

            VideoModel model = videoList.get(PLAYING_NOW);
            //HashMap<String, String> hashMap = arrayList.get(PLAYING_NOW);
            //String vdo_id = hashMap.get("vdo_id");
            //playVideo(vdo_id);
            playVideo(model.getVideoUrl());
        } else {
            Toast.makeText(MainActivity.this, "Playing the first video", Toast.LENGTH_LONG).show();
        }

    }


    ///==============================================
    ///==============================================
    //===================================================

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //==========================================================================
//==========================================================================


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        myYoutubePlayer = youTubePlayer;
        myYoutubePlayer.setPlaybackEventListener(myPlaybackEventListener);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        myYoutubePlayer = null;
    }


    ///====================================================
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
    private long mBackPressed;

    // When user click bakpress button this method is called
    @Override
    public void onBackPressed() {
        // When user press back button

        if (layPlayer.getVisibility() == View.GONE) {

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {

                Toast.makeText(getBaseContext(), "Press again to exit",
                        Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

        } else {
            closePlayer();
        }


    } // end of onBackpressed method

    //#############################################################################################

    @Override
    protected void onDestroy() {

        //if(mAdView!=null) mAdView.destroy();

        super.onDestroy();
    }


    ///==============================================
}