package com.silverorange.videoplayer

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ui.StyledPlayerView
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var markwon: Markwon
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        markwon = Markwon.create(this)
        setContent {
            MaterialTheme {
                var currentLifecycleState by remember {
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val lifecycleObserver = LifecycleEventObserver { _, event ->
                        currentLifecycleState = event
                    }
                    lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                    }
                }
                val viewModel: VideoPlayerViewModel = hiltViewModel()

                Scaffold(
                    topBar = { TopBar() },
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) { paddingValues ->
                    when (val state = viewModel.state) {
                        is DataState.Error -> ErrorLayout { viewModel.loadVideos() }
                        is DataState.Ready -> {
                            Column(
                                Modifier
                                    .padding(paddingValues)
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        StyledPlayerView(context).also {
                                            it.player = viewModel.player
                                            it.setShowFastForwardButton(false)
                                            it.setShowRewindButton(false)
                                            it.setShowMultiWindowTimeBar(false)
                                        }
                                    }, update = {
                                        when (currentLifecycleState) {
                                            Lifecycle.Event.ON_PAUSE -> {
                                                it.onPause()
                                                viewModel.player.pause()
                                            }
                                            Lifecycle.Event.ON_RESUME -> {
                                                it.onResume()
                                            }
                                            else -> Unit
                                        }
                                    }, modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 9f)
                                )
                                Column(
                                    Modifier.verticalScroll(
                                        rememberScrollState()
                                    )
                                ) {
                                    Text(
                                        text = state.data.title,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                                        color = MaterialTheme.colors.onBackground
                                    )
                                    Text(
                                        text = state.data.authorName,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(bottom = 16.dp),
                                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                                    )
                                    AndroidView(factory = { context ->
                                        TextView(context).apply {
                                            textSize = 12.sp.value
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                            )
                                            setTextColor(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.black
                                                )
                                            )
                                        }
                                    }, update = {
                                        markwon.setMarkdown(it, state.data.description)
                                    }, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                        else -> Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorLayout(onTryAgain: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(stringResource(id = R.string.oops_something_went_wrong), fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))
                Button(onClick = onTryAgain) {
                    Text(stringResource(id = R.string.try_again_upper))
                }
            }
        }
    }
}

@Preview
@Composable
fun TopBar() {
    TopAppBar(title = {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.app_name))
        }
    })
}