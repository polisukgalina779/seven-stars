package com.Vugfeve7.Hidran.presentation.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.core.di.ServiceLocator
import com.Vugfeve7.Hidran.core.theme.AccentChrome
import com.Vugfeve7.Hidran.core.theme.AccentCrimson
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.BgBase
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.theme.WinGreen
import com.Vugfeve7.Hidran.core.ui.CoinBadge
import com.Vugfeve7.Hidran.core.ui.DisclaimerChip
import com.Vugfeve7.Hidran.core.ui.GlassCard
import com.Vugfeve7.Hidran.core.ui.ScreenBackground
import com.Vugfeve7.Hidran.core.ui.StatCard
import com.Vugfeve7.Hidran.core.ui.formatNumber
import com.Vugfeve7.Hidran.core.ui.modeNameRes
import com.Vugfeve7.Hidran.core.ui.roomNameRes
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.core.ui.symbolTierRes
import com.Vugfeve7.Hidran.domain.model.SlotSymbol
import com.Vugfeve7.Hidran.domain.model.RiskMode

@Composable
fun MenuRoute(
    onPlay: (Int, RiskMode) -> Unit,
    onLevelSelect: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.activeRoom, state.mode) },
        onLevelSelect = onLevelSelect,
        onCollection = onCollection,
        onProgress = onProgress,
        onSettings = onSettings
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevelSelect: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }

    ScreenBackground(overlay = Palette.menuOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp)
                )
                Text(
                    text = stringResource(R.string.splash_title),
                    color = AccentGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp,
                    modifier = Modifier.weight(1f)
                )
                CoinBadge(coins = state.coins)
            }

            DisclaimerChip(modifier = Modifier.fillMaxWidth())

            SymbolRibbon(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(222.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayTile(
                    state = state,
                    appear = appearValue(entered, 0),
                    onPlay = onPlay,
                    modifier = Modifier
                        .weight(1.55f)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BentoTile(
                        title = stringResource(R.string.level_select),
                        caption = stringResource(
                            R.string.rooms_progress,
                            state.roomsUnlocked,
                            GameConfig.TOTAL_ROOMS
                        ),
                        accent = AccentChrome,
                        appear = appearValue(entered, 1),
                        onClick = onLevelSelect,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_home),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    BentoTile(
                        title = stringResource(R.string.collection),
                        caption = stringResource(
                            R.string.cards_progress,
                            state.cards,
                            GameConfig.TOTAL_CARDS
                        ),
                        accent = AccentGoldLight,
                        appear = appearValue(entered, 2),
                        onClick = onCollection,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_3),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BentoTile(
                    title = stringResource(R.string.progress),
                    caption = stringResource(
                        R.string.stars_progress,
                        state.stars,
                        GameConfig.TOTAL_STARS
                    ),
                    accent = AccentGoldLight,
                    appear = appearValue(entered, 3),
                    onClick = onProgress,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = AccentGoldLight,
                        modifier = Modifier.size(22.dp)
                    )
                }
                BentoTile(
                    title = stringResource(R.string.settings),
                    caption = stringResource(modeNameRes(state.mode)),
                    accent = AccentChrome,
                    appear = appearValue(entered, 4),
                    onClick = onSettings,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = null,
                        tint = AccentChrome,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            if (state.bestWin > 0 && state.totalSpins > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_best_win),
                        value = formatNumber(state.bestWin),
                        accent = WinGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_total_spins),
                        value = formatNumber(state.totalSpins),
                        accent = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun SymbolRibbon(modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.ribbon_title),
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.4.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                SlotSymbol.entries.forEach { symbol ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(symbolDrawable(symbol)),
                            contentScale = ContentScale.Fit,
                            contentDescription = stringResource(symbolTierRes(symbol)),
                            modifier = Modifier.size(58.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.payout_multiplier, symbol.payout),
                            color = if (symbol.isWild) AccentCrimson else AccentGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            text = stringResource(symbolTierRes(symbol)),
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun appearValue(entered: Boolean, index: Int): Float {
    val value by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 320, delayMillis = index * 60, easing = FastOutSlowInEasing),
        label = "appear"
    )
    return value
}

@Composable
private fun PlayTile(
    state: MenuUiState,
    appear: Float,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    val sheen = remember { Animatable(-0.3f) }
    LaunchedEffect(state.reduceMotion) {
        if (!state.reduceMotion) {
            sheen.snapTo(-0.3f)
            sheen.animateTo(1.3f, tween(1100, easing = LinearEasing))
        }
    }
    Button(
        onClick = onPlay,
        modifier = modifier
            .graphicsLayer {
                alpha = appear
                translationY = (1f - appear) * 36f
            }
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = AccentGold.copy(alpha = 0.4f),
                spotColor = AccentGold.copy(alpha = 0.6f)
            ),
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = BgBase
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(Palette.goldCta, shape)
        ) {
            if (!state.reduceMotion && sheen.value < 1.29f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(46.dp)
                        .graphicsLayer { translationX = sheen.value * 620f }
                        .rotate(16f)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.16f), Color.Transparent)
                            )
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = null,
                    tint = BgBase,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.play),
                    color = BgBase,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(roomNameRes(state.activeRoom)),
                    color = BgBase.copy(alpha = 0.76f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(
                        R.string.mode_stake,
                        stringResource(modeNameRes(state.mode)),
                        state.stake
                    ),
                    color = BgBase.copy(alpha = 0.66f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BentoTile(
    title: String,
    caption: String,
    accent: Color,
    appear: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            alpha = appear
            translationY = (1f - appear) * 36f
        },
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.3f)),
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceCard.copy(alpha = 0.86f),
            contentColor = TextPrimary
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .alpha(0.9f)
                    .background(Palette.gloss)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                icon()
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = caption,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
