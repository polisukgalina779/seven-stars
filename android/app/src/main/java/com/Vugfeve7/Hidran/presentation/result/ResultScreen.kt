package com.Vugfeve7.Hidran.presentation.result

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.di.ServiceLocator
import com.Vugfeve7.Hidran.core.theme.AccentCrimson
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.BgBase
import com.Vugfeve7.Hidran.core.theme.DividerLine
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.theme.WinGreen
import com.Vugfeve7.Hidran.core.ui.GlassCard
import com.Vugfeve7.Hidran.core.ui.PrimaryButton
import com.Vugfeve7.Hidran.core.ui.ScreenBackground
import com.Vugfeve7.Hidran.core.ui.SecondaryButton
import com.Vugfeve7.Hidran.core.ui.StarRow
import com.Vugfeve7.Hidran.core.ui.formatNumber
import com.Vugfeve7.Hidran.core.ui.streakLabel
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.domain.model.RiskMode
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ResultRoute(
    onSpinAgain: (Int, RiskMode) -> Unit,
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit
) {
    val resultViewModel: ResultViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by resultViewModel.state.collectAsStateWithLifecycle()
    ResultScreen(
        state = state,
        onSpinAgain = {
            resultViewModel.startNextRun()
            onSpinAgain(state.roomIndex, state.mode)
        },
        onClaim = { resultViewModel.claimRefill() },
        onLevelSelect = onLevelSelect,
        onMenu = onMenu
    )
}

@Composable
fun ResultScreen(
    state: ResultUiState,
    onSpinAgain: () -> Unit,
    onClaim: () -> Unit,
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var counting by remember { mutableStateOf(false) }
    LaunchedEffect(state.net) {
        counting = true
    }
    val countedNet by animateFloatAsState(
        targetValue = if (counting) state.net.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = (abs(state.net) * 8).coerceIn(300, 1600),
            easing = FastOutSlowInEasing
        ),
        label = "netCount"
    )
    val starPop by animateFloatAsState(
        targetValue = if (counting) 1f else 0.6f,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "starPop"
    )

    ScreenBackground(overlay = Palette.resultOverlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(verdictRes(state)),
                    style = MaterialTheme.typography.headlineMedium.copy(brush = Palette.chromeRim),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )

                Spacer(modifier = Modifier.height(18.dp))

                StarRow(
                    earned = state.stars,
                    total = 3,
                    size = 40.dp,
                    gap = 10.dp,
                    modifier = Modifier.scale(starPop)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.bust) {
                    RefillPanel(
                        claimed = state.refillClaimed,
                        onClaim = onClaim
                    )
                } else {
                    BreakdownCard(
                        state = state,
                        net = countedNet.roundToInt()
                    )
                }

                if (state.newCards.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CollectionFlash(state = state)
                }

                if (state.setBonus > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = AccentGold.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, AccentGoldLight.copy(alpha = 0.7f))
                    ) {
                        Text(
                            text = stringResource(R.string.room_set_complete, formatNumber(state.setBonus)),
                            color = AccentGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrimaryButton(
                    text = stringResource(R.string.spin_again),
                    onClick = onSpinAgain,
                    enabled = state.canSpinAgain
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecondaryButton(
                    text = stringResource(R.string.level_select),
                    onClick = onLevelSelect
                )

                Spacer(modifier = Modifier.height(6.dp))

                TextButton(
                    onClick = onMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
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
    }
}

@Composable
private fun BreakdownCard(state: ResultUiState, net: Int) {
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
            BreakdownRow(
                label = stringResource(R.string.row_staked),
                value = formatNumber(state.staked),
                valueColor = TextPrimary
            )
            BreakdownRow(
                label = stringResource(R.string.row_won),
                value = formatNumber(state.won),
                valueColor = AccentGoldLight
            )
            BreakdownRow(
                label = stringResource(R.string.row_net),
                value = if (net >= 0) {
                    stringResource(R.string.net_positive, formatNumber(net))
                } else {
                    stringResource(R.string.net_negative, formatNumber(abs(net)))
                },
                valueColor = if (net >= 0) WinGreen else AccentCrimson
            )
            if (state.bestPayout > 0) {
                BreakdownRow(
                    label = stringResource(R.string.row_best_line),
                    value = stringResource(R.string.payout_multiplier, state.bestPayout),
                    valueColor = TextPrimary
                )
            }
            if (state.streakPeakPercent > 100) {
                BreakdownRow(
                    label = stringResource(R.string.row_streak_peak),
                    value = stringResource(R.string.streak_pill, streakLabel(state.streakPeakPercent)),
                    valueColor = AccentCrimson
                )
            }
        }
    }
}

@Composable
private fun BreakdownRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun RefillPanel(claimed: Boolean, onClaim: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCard.copy(alpha = 0.92f),
        border = BorderStroke(1.dp, AccentCrimson.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.refill_title),
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onClaim,
                enabled = !claimed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGold,
                    contentColor = BgBase,
                    disabledContainerColor = DividerLine,
                    disabledContentColor = TextMuted
                )
            ) {
                Text(
                    text = if (claimed) {
                        stringResource(R.string.claimed)
                    } else {
                        stringResource(R.string.claim_refill)
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.4.sp
                )
            }
        }
    }
}

@Composable
private fun CollectionFlash(state: ResultUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            state.newCards.forEach { symbol ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(AccentGold.copy(alpha = 0.16f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(symbolDrawable(symbol)),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (state.newCards.size == 1) {
                stringResource(R.string.new_card)
            } else {
                stringResource(R.string.new_cards, state.newCards.size)
            },
            color = AccentGoldLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.4.sp
        )
    }
}

private fun verdictRes(state: ResultUiState): Int = when {
    state.bust -> R.string.verdict_bust
    state.stars >= 3 -> R.string.verdict_three
    state.stars == 2 -> R.string.verdict_two
    state.stars == 1 -> R.string.verdict_one
    else -> R.string.verdict_zero
}
