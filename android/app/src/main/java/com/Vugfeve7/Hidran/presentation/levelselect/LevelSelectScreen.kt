package com.Vugfeve7.Hidran.presentation.levelselect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
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
import com.Vugfeve7.Hidran.core.theme.BgBase
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.ui.CoinBadge
import com.Vugfeve7.Hidran.core.ui.ScreenScaffold
import com.Vugfeve7.Hidran.core.ui.SegmentedOption
import com.Vugfeve7.Hidran.core.ui.StarRow
import com.Vugfeve7.Hidran.core.ui.formatNumber
import com.Vugfeve7.Hidran.core.ui.modeNameRes
import com.Vugfeve7.Hidran.core.ui.roomNameRes
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.model.RiskMode

@Composable
fun LevelSelectRoute(
    onEnterRoom: (Int, RiskMode) -> Unit,
    onMenu: () -> Unit
) {
    val levelSelectViewModel: LevelSelectViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by levelSelectViewModel.state.collectAsStateWithLifecycle()
    LevelSelectScreen(
        state = state,
        onSelectMode = { mode -> levelSelectViewModel.selectMode(mode) },
        onUnlock = { index -> levelSelectViewModel.unlock(index) },
        onEnter = { index ->
            levelSelectViewModel.enter(index)
            onEnterRoom(index, state.mode)
        },
        onMenu = onMenu
    )
}

@Composable
fun LevelSelectScreen(
    state: LevelSelectUiState,
    onSelectMode: (RiskMode) -> Unit,
    onUnlock: (Int) -> Unit,
    onEnter: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modes = SampleData.modes
    ScreenScaffold(
        title = stringResource(R.string.level_select),
        overlay = Palette.levelOverlay,
        onBack = onMenu,
        modifier = modifier,
        trailing = { CoinBadge(coins = state.coins) }
    ) {
        SegmentedOption(
            labels = modes.map { stringResource(modeNameRes(it)) },
            captions = modes.map { mode ->
                stringResource(R.string.stake_value, mode.stake)
            },
            selectedIndex = modes.indexOf(state.mode).coerceAtLeast(0),
            onSelect = { index -> onSelectMode(modes[index]) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = state.warning,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AccentCrimson.copy(alpha = 0.22f),
                border = BorderStroke(1.dp, AccentCrimson.copy(alpha = 0.6f))
            ) {
                Text(
                    text = stringResource(R.string.not_enough_coins_yet),
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 10.dp)
        ) {
            items(state.rooms, key = { room -> room.index }) { room ->
                RoomCard(
                    room = room,
                    onUnlock = { onUnlock(room.index) },
                    onEnter = { onEnter(room.index) }
                )
            }
        }

        TextButton(
            onClick = onMenu,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = stringResource(R.string.menu),
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.6.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun RoomCard(
    room: RoomCardState,
    onUnlock: () -> Unit,
    onEnter: () -> Unit
) {
    val lockedState = stringResource(R.string.state_locked)
    val unlockedState = stringResource(R.string.state_unlocked)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(98.dp)
            .semantics { stateDescription = if (room.unlocked) unlockedState else lockedState },
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard.copy(alpha = 0.88f),
        border = BorderStroke(1.dp, if (room.unlocked) AccentGold.copy(alpha = 0.34f) else AccentChrome.copy(alpha = 0.26f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Palette.gloss)
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(symbolDrawable(SampleData.symbols[room.index % SampleData.symbols.size])),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(42.dp)
                            .alpha(if (room.unlocked) 1f else 0.38f)
                    )
                    if (!room.unlocked) {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null,
                            tint = AccentChrome,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = stringResource(roomNameRes(room.index)),
                        color = if (room.unlocked) TextPrimary else TextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 2
                    )
                    Text(
                        text = stringResource(R.string.goal_spins, room.goal, GameConfig.SPINS_PER_RUN),
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    if (room.unlocked) {
                        StarRow(earned = room.stars, total = 3, size = 14.dp)
                    }
                }

                if (room.unlocked) {
                    OutlinedButton(
                        onClick = onEnter,
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceAlt,
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.enter_room),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onUnlock,
                        enabled = room.affordable,
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGold,
                            contentColor = BgBase,
                            disabledContainerColor = SurfaceAlt,
                            disabledContentColor = TextMuted
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.unlock_room, formatNumber(room.price)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
