package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.*
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.TrackSelectionDialogBuilder
import com.example.myapplication.R
import com.example.myapplication.base.TestApplication
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.player.generateQualityList
import com.example.myapplication.ui.adapter.MainAdapter
import com.example.myapplication.viewmodel.TestViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Player.Listener {


    private lateinit var trackSelector: DefaultTrackSelector
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    @Inject
    lateinit var mainAdapter: MainAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var viewModel: TestViewModel

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private val playbackStateListener: Player.Listener = playbackStateListener()
    var qualityList = ArrayList<Pair<String, TrackSelectionParameters.Builder>>()
    private var qualityPopUp: PopupMenu?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        TestApplication.getApplication()?.appComponent?.inject(this)
        setContentView(viewBinding.root)
        initView()
        observerData()
        viewModel.fetchListData()
    }

    private fun initializePlayer() {
         trackSelector = DefaultTrackSelector(this, AdaptiveTrackSelection.Factory()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        val mediaItem = MediaItem.Builder()
                      .setMimeType(MimeTypes.APPLICATION_M3U8)
                      .setUri("https://d1qdjm885g506f.cloudfront.net/93e0fe37-b625-4cf3-8ba2-74c2f7a466bf/hls/PPiO2SBZ7Rw.m3u8")


        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
//        val hlsMediaSource =  HlsMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(mediaItem)



        val mediaSourceList = listOf(
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem.setUri("https://d1qdjm885g506f.cloudfront.net/93e0fe37-b625-4cf3-8ba2-74c2f7a466bf/hls/PPiO2SBZ7Rw.m3u8").build()),
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem.setUri("https://d1qdjm885g506f.cloudfront.net/cfdce1cf-d53c-4d24-964b-93bc4fab2993/hls/YBVMGThniE4.m3u8").build()),
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem.setUri("https://d1qdjm885g506f.cloudfront.net/e991d655-26b8-4790-b856-9906a12f1352/hls/KK01P_1c_Pc.m3u8").build())
        )

// Create a ConcatenatingMediaSource from the list of media sources
        val mediaSource = ConcatenatingMediaSource(*mediaSourceList.toTypedArray())

// Load the ConcatenatingMediaSource into the ExoPlayer instance
        player?.prepare(mediaSource)

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                viewBinding.videoView.player = exoPlayer
                exoPlayer.setMediaSources(mediaSourceList)
               // exoPlayer.setMediaSource(hlsMediaSource)
                exoPlayer.addListener(playbackStateListener)

//                val secondMediaItem = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
//                exoPlayer.addMediaItem(secondMediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> {
                    viewBinding.loading.visibility = View.VISIBLE
                    "ExoPlayer.STATE_BUFFERING -"}
                ExoPlayer.STATE_READY -> {
                    viewBinding.loading.visibility = View.GONE
                    trackSelector?.generateQualityList()?.let {
                        qualityList = it
                        setUpQualityList()
                    }
                    "ExoPlayer.STATE_READY     -"}
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }
            Log.d("Fan Player ", "changed state to $stateString")
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            println("TRACK CHANGED")
            println(tracks.groups)
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            super.onVideoSizeChanged(videoSize)
            println("Current video quality: ${videoSize.height}")
        }
    }

    private fun setUpQualityList() {
        qualityPopUp = PopupMenu(this, viewBinding.videoView.findViewById<ImageView>(R.id.exo_quality) )
        qualityList.let {
            for ((i, videoQuality) in it.withIndex()) {
                qualityPopUp?.menu?.add(0, i, 0, videoQuality.first)
            }
        }
        qualityPopUp?.setOnMenuItemClickListener { menuItem ->
            qualityList[menuItem.itemId].let {
                  trackSelector.setParameters(
                          it.second.build())
            }
            true
        }
    }


    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    private fun observerData() {
         // viewModel.productList.observe(lifecycleScope){}
    }

    private fun initView() {
       //binding.recyclerView.adapter = mainAdapter
        var fullScreenIcon = viewBinding.videoView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        fullScreenIcon.setOnClickListener {
            requestedOrientation = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }

        viewBinding.videoView.findViewById<ImageView>(R.id.exo_quality).setOnClickListener {
            qualityPopUp?.show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}