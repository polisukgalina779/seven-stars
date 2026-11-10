package com.Vugfeve7.Hidran.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.di.ServiceLocator
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.ui.DisclaimerChip
import com.Vugfeve7.Hidran.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "splash")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathe"
    )
    val ring by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "ring"
    )
    val glint by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "glint"
    )
    val glow by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    ScreenBackground(overlay = Palette.splashOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(234.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(214.dp)
                        .rotate(ring)
                ) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(AccentGoldLight, AccentGold, SurfaceAlt, AccentGoldLight)
                        ),
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 3f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(214.dp)
                        .rotate(glint)
                ) {
                    GlintDiamond(Alignment.TopCenter, glow)
                    GlintDiamond(Alignment.CenterEnd, glow)
                    GlintDiamond(Alignment.BottomCenter, glow)
                    GlintDiamond(Alignment.CenterStart, glow)
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(168.dp)
                        .scale(breathe)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displayLarge.copy(brush = Palette.chromeRim),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_tagline),
                color = TextSecondary,
                fontSize = 11.sp,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .size(width = 180.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = AccentGold,
                trackColor = SurfaceAlt
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.loading),
                color = TextMuted,
                fontSize = 11.sp,
                letterSpacing = 4.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(bottom = 28.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            DisclaimerChip()
        }
    }
}

@Composable
private fun BoxScope.GlintDiamond(alignment: Alignment, glow: Float) {
    Box(
        modifier = Modifier
            .align(alignment)
            .size(9.dp)
            .rotate(45f)
            .alpha(glow)
            .background(AccentGoldLight)
    )
}
