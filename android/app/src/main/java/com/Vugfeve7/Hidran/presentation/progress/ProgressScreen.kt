package com.Vugfeve7.Hidran.presentation.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.theme.WinGreen
import com.Vugfeve7.Hidran.core.ui.CoinBadge
import com.Vugfeve7.Hidran.core.ui.GlassCard
import com.Vugfeve7.Hidran.core.ui.ScreenScaffold
import com.Vugfeve7.Hidran.core.ui.SecondaryButton
import com.Vugfeve7.Hidran.core.ui.StarRow
import com.Vugfeve7.Hidran.core.ui.StatCard
import com.Vugfeve7.Hidran.core.ui.formatNumber
import com.Vugfeve7.Hidran.core.ui.modeNameRes
import com.Vugfeve7.Hidran.core.ui.roomNameRes
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.domain.model.RiskMode

@Composable
fun ProgressRoute(
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit
) {
    val progressViewModel: ProgressViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by progressViewModel.state.collectAsStateWithLifecycle()
    ProgressScreen(
        state = state,
        onLevelSelect = onLevelSelect,
        onMenu = onMenu
    )
}

@Composable
fun ProgressScreen(
    state: ProgressUiState,
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.progress),
        overlay = Palette.progressOverlay,
        onBack = onMenu,
        modifier = modifier,
        trailing = { CoinBadge(coins = state.coins) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                HeroBand(stars = state.stars)
            }

            if (state.empty) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.progress_empty),
                            color = TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        SecondaryButton(
                            text = stringResource(R.string.level_select),
                            onClick = onLevelSelect,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_total_spins),
                            value = formatNumber(state.totalSpins),
                            accent = AccentGold,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_best_win),
                            value = formatNumber(state.bestWin),
                            accent = WinGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_rooms_unlocked),
                            value = formatNumber(state.roomsUnlocked),
                            accent = AccentChrome,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_cards_found),
                            value = formatNumber(state.cardsFound),
                            accent = AccentGoldLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    MasteryCard(mastery = state.mastery)
                }
            }

            item {
                LadderCard(ladder = state.ladder)
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
private fun HeroBand(stars: Int) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.stars_hero, stars, GameConfig.TOTAL_STARS),
                style = MaterialTheme.typography.displaySmall.copy(brush = Palette.chromeRim)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(SurfaceAlt)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((stars.toFloat() / GameConfig.TOTAL_STARS).coerceIn(0f, 1f))
                        .height(10.dp)
                        .background(Palette.goldTrack)
                )
            }
        }
    }
}

@Composable
private fun MasteryCard(mastery: List<ModeMastery>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, AccentChrome.copy(alpha = 0.24f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            mastery.forEach { entry ->
                val accent = accentFor(entry.mode)
                val reference = mastery.maxOfOrNull { it.spins } ?: 0
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(modeNameRes(entry.mode)),
                            color = accent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = stringResource(
                                R.string.mastery_row,
                                entry.spins,
                                formatNumber(entry.bestRun)
                            ),
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(SurfaceAlt)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    if (reference <= 0) {
                                        0f
                                    } else {
                                        (entry.spins.toFloat() / reference).coerceIn(0f, 1f)
                                    }
                                )
                                .height(6.dp)
                                .background(accent)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LadderCard(ladder: List<RoomLadderEntry>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ladder.forEach { entry ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(
                            symbolDrawable(SampleData.symbols[entry.roomIndex % SampleData.symbols.size])
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(if (entry.unlocked) 1f else 0.35f)
                    )
                    Text(
                        text = stringResource(roomNameRes(entry.roomIndex)),
                        color = if (entry.unlocked) TextPrimary else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    StarRow(earned = entry.stars, total = 3, size = 13.dp)
                    Text(
                        text = stringResource(R.string.goal_value, entry.goal),
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun accentFor(mode: RiskMode): Color = when (mode) {
    RiskMode.CALM -> AccentChrome
    RiskMode.BOLD -> AccentGold
    RiskMode.WILD -> AccentCrimson
}
