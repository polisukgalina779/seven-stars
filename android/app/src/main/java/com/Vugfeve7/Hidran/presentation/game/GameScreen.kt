package com.Vugfeve7.Hidran.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.config.GameConfig
import com.Vugfeve7.Hidran.core.di.ServiceLocator
import com.Vugfeve7.Hidran.core.theme.AccentCrimson
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.BgBase
import com.Vugfeve7.Hidran.core.theme.DividerLine
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.theme.WinGreen
import com.Vugfeve7.Hidran.core.ui.ScreenBackground
import com.Vugfeve7.Hidran.core.ui.StatCard
import com.Vugfeve7.Hidran.core.ui.SymbolCell
import com.Vugfeve7.Hidran.core.ui.formatNumber
import com.Vugfeve7.Hidran.core.ui.roomNameRes
import com.Vugfeve7.Hidran.core.ui.streakLabel
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.model.RiskMode
import com.Vugfeve7.Hidran.domain.model.SlotSymbol
import com.Vugfeve7.Hidran.presentation.dialog.PauseDialog
import kotlin.math.floor
import kotlinx.coroutines.launch

@Composable
fun GameRoute(
    roomIndex: Int,
    mode: RiskMode,
    token: Long,
    onRunComplete: () -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        gameViewModel.startRun(roomIndex, mode, token)
    }

    LaunchedEffect(token) {
        gameViewModel.events.collect { event ->
            if (event is GameEvent.RunComplete && event.token == token) onRunComplete()
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        onSpin = { gameViewModel.spin() },
        onPause = { gameViewModel.pause() },
        onResume = { gameViewModel.resume() },
        onRestart = { gameViewModel.restartRun() },
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onSpin: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(overlay = Palette.gameOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause)

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                val frame = 12.dp
                val gap = 6.dp
                val widthBudget = maxWidth.coerceAtMost(352.dp)
                val cellByWidth = (widthBudget - frame * 2 - gap * 2) / GameConfig.REELS
                val cellByHeight = (maxHeight - frame * 2 - gap * 2) / GameConfig.ROWS
                val rawCell = if (cellByWidth < cellByHeight) cellByWidth else cellByHeight
                val cell = floor(rawCell.value).dp.coerceIn(40.dp, 124.dp)
                Cabinet(
                    state = state,
                    cell = cell,
                    gap = gap,
                    frame = frame
                )
                BigWinBanner(visible = state.bigWin, reduceMotion = state.reduceMotion)
            }

            SplitPanel(
                state = state,
                onSpin = onSpin
            )
        }

        if (state.paused) {
            PauseDialog(
                onResume = onResume,
                onRestart = onRestart,
                onMenu = onMenu
            )
        }
    }
}

@Composable
private fun BigWinBanner(visible: Boolean, reduceMotion: Boolean) {
    val appear by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = if (reduceMotion) 300 else 180, easing = FastOutSlowInEasing),
        label = "bigWinAppear"
    )
    val pop by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = if (reduceMotion) {
            tween(durationMillis = 300, easing = FastOutSlowInEasing)
        } else {
            spring(dampingRatio = 0.45f, stiffness = 500f)
        },
        label = "bigWinPop"
    )
    if (appear > 0.02f) {
        Text(
            text = stringResource(R.string.big_win),
            style = MaterialTheme.typography.displayLarge.copy(brush = Palette.chromeRim),
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer {
                alpha = appear
                scaleX = pop
                scaleY = pop
            }
        )
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        color = BgBase.copy(alpha = 0.55f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(roomNameRes(state.roomIndex)),
                    color = AccentGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    maxLines = 1
                )
                Text(
                    text = stringResource(R.string.run_counter, state.runNumber, GameConfig.SPINS_PER_RUN),
                    color = TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = AccentGoldLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = formatNumber(state.coins),
                        color = AccentGoldLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
                if (state.lastWin > 0) {
                    Text(
                        text = stringResource(R.string.last_win, formatNumber(state.lastWin)),
                        color = WinGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun Cabinet(
    state: GameUiState,
    cell: Dp,
    gap: Dp,
    frame: Dp
) {
    val density = LocalDensity.current
    val stepPx = with(density) { (cell + gap).toPx() }
    val cellPx = with(density) { cell.toPx() }
    val gapPx = with(density) { gap.toPx() }

    val boardWidth = cell * GameConfig.REELS + gap * 2 + frame * 2
    val boardHeight = cell * GameConfig.ROWS + gap * 2 + frame * 2

    val offsets = remember { List(GameConfig.REELS) { Animatable(0f) } }

    LaunchedEffect(state.spinToken) {
        if (state.spinToken == 0) return@LaunchedEffect
        offsets.forEachIndexed { index, animatable ->
            launch {
                animatable.snapTo(0f)
                animatable.animateTo(
                    targetValue = (GameConfig.REEL_REVOLUTIONS[index] * GameConfig.STRIP_LENGTH).toFloat(),
                    animationSpec = tween(
                        durationMillis = state.reelDurations.getOrElse(index) { 1200 },
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.15f, 1f)
                    )
                )
                animatable.snapTo(0f)
            }
        }
    }

    val pulse = remember { Animatable(0f) }
    LaunchedEffect(state.spinToken, state.status) {
        if (state.status != GameStatus.Winning) {
            pulse.snapTo(0f)
            return@LaunchedEffect
        }
        pulse.snapTo(0f)
        repeat(2) {
            pulse.animateTo(1f, tween(160, easing = FastOutSlowInEasing))
            pulse.animateTo(0f, tween(160, easing = FastOutSlowInEasing))
        }
    }
    val flash = 1f + 0.16f * pulse.value
    val linePulse = 0.3f + 0.7f * pulse.value
    val tiltGlow by animateFloatAsState(
        targetValue = if (state.wildBoost) 1f else 0f,
        animationSpec = tween(400),
        label = "tiltGlow"
    )

    val borderBrush = if (state.wildBoost) {
        Brush.linearGradient(
            listOf(
                AccentCrimson.copy(alpha = 0.45f + 0.55f * tiltGlow),
                AccentGoldLight,
                AccentCrimson.copy(alpha = 0.45f + 0.55f * tiltGlow)
            )
        )
    } else {
        Palette.chromeRim
    }

    Box(
        modifier = Modifier
            .size(width = boardWidth, height = boardHeight)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = AccentGold.copy(alpha = 0.28f),
                spotColor = AccentGold.copy(alpha = 0.45f)
            )
            .background(BgBase.copy(alpha = 0.88f), RoundedCornerShape(24.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(frame)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                repeat(GameConfig.REELS) { column ->
                    val strip = state.strips.getOrElse(column) { emptyList() }
                    ReelColumn(
                        strip = strip,
                        cell = cell,
                        gap = gap,
                        stepPx = stepPx,
                        offset = offsets[column],
                        winningRows = winningRowsFor(state, column),
                        flash = if (state.status == GameStatus.Winning) flash else 1f
                    )
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                PayLineRows.forEachIndexed { lineIndex, rows ->
                    val active = state.winningLines.contains(lineIndex)
                    val points = rows.mapIndexed { column, row ->
                        Offset(
                            x = column * (cellPx + gapPx) + cellPx / 2f,
                            y = row * (cellPx + gapPx) + cellPx / 2f
                        )
                    }
                    for (segment in 0 until points.size - 1) {
                        drawLine(
                            color = if (active) AccentGoldLight.copy(alpha = linePulse) else AccentGold.copy(alpha = 0.14f),
                            start = points[segment],
                            end = points[segment + 1],
                            strokeWidth = if (active) 5f else 2.5f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            if (state.streakStep > 0) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(50),
                    color = AccentCrimson.copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, AccentGoldLight)
                ) {
                    Text(
                        text = stringResource(R.string.streak_pill, streakLabel(state.streakPercent)),
                        color = AccentGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                brush = borderBrush,
                cornerRadius = CornerRadius(66f, 66f),
                style = Stroke(width = 5f)
            )
        }
    }
}

@Composable
private fun ReelColumn(
    strip: List<SlotSymbol>,
    cell: Dp,
    gap: Dp,
    stepPx: Float,
    offset: Animatable<Float, *>,
    winningRows: Set<Int>,
    flash: Float
) {
    val length = GameConfig.STRIP_LENGTH
    Box(
        modifier = Modifier
            .width(cell)
            .height(cell * GameConfig.ROWS + gap * 2)
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier.graphicsLayer {
                translationY = -((offset.value % length) * stepPx)
            },
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            repeat(length * 2) { index ->
                val slot = index % length
                val symbol = strip.getOrElse(slot) { SlotSymbol.COMMON }
                val highlighted = slot < GameConfig.ROWS && winningRows.contains(slot)
                SymbolCell(
                    symbol = symbol,
                    size = cell,
                    highlighted = highlighted,
                    spriteScale = if (highlighted) flash else 1f
                )
            }
        }
    }
}

@Composable
private fun SplitPanel(
    state: GameUiState,
    onSpin: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = SurfaceCard.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.27f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    label = stringResource(R.string.stat_stake),
                    value = formatNumber(state.stake),
                    accent = AccentGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_run_goal),
                    value = formatNumber(state.goal),
                    accent = AccentGoldLight,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_won),
                    value = formatNumber(state.runWon),
                    accent = WinGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            SpinButton(state = state, onSpin = onSpin)

            if (state.showPayTable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SampleData.symbols.forEach { symbol ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(symbolDrawable(symbol)),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(R.string.payout_multiplier, symbol.payout),
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpinButton(state: GameUiState, onSpin: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val enabled = state.canSpin
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 900f),
        label = "spinPress"
    )
    val spinningState = stringResource(R.string.state_spinning)
    val readyState = stringResource(R.string.state_ready)
    val brokeState = stringResource(R.string.not_enough_coins)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onSpin,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .shadow(
                    elevation = if (enabled) 16.dp else 0.dp,
                    shape = CircleShape,
                    spotColor = AccentGold.copy(alpha = 0.6f),
                    ambientColor = AccentGold.copy(alpha = 0.4f)
                )
                .semantics {
                    stateDescription = when {
                        state.notEnoughCoins -> brokeState
                        !enabled -> spinningState
                        else -> readyState
                    }
                },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(3.dp, if (enabled) AccentGoldLight else DividerLine),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = BgBase,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = TextMuted
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (enabled) {
                            Brush.radialGradient(listOf(AccentGoldLight, AccentGold, AccentCrimson))
                        } else {
                            Brush.radialGradient(listOf(DividerLine, DividerLine))
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_play),
                        contentDescription = null,
                        tint = if (enabled) BgBase else TextMuted,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = stringResource(R.string.spin),
                        color = if (enabled) BgBase else TextMuted,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
        if (state.notEnoughCoins) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.not_enough_coins),
                color = AccentCrimson,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

private val PayLineRows: List<List<Int>> = listOf(
    listOf(1, 1, 1),
    listOf(0, 1, 2),
    listOf(2, 1, 0)
)

private fun winningRowsFor(state: GameUiState, column: Int): Set<Int> =
    state.winningCells
        .filter { it / GameConfig.ROWS == column }
        .map { it % GameConfig.ROWS }
        .toSet()
