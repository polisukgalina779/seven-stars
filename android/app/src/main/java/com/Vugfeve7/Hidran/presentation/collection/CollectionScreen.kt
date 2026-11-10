package com.Vugfeve7.Hidran.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.DividerLine
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.ui.CoinBadge
import com.Vugfeve7.Hidran.core.ui.ScreenScaffold
import com.Vugfeve7.Hidran.core.ui.SecondaryButton
import com.Vugfeve7.Hidran.core.ui.roomNameRes
import com.Vugfeve7.Hidran.core.ui.symbolDrawable
import com.Vugfeve7.Hidran.core.ui.symbolTierRes

@Composable
fun CollectionRoute(
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit
) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    CollectionScreen(
        state = state,
        onLevelSelect = onLevelSelect,
        onMenu = onMenu
    )
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    onLevelSelect: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.collection),
        overlay = Palette.collectionOverlay,
        onBack = onMenu,
        modifier = modifier,
        trailing = { CoinBadge(coins = state.coins) }
    ) {
        Text(
            text = stringResource(R.string.cards_progress, state.earnedTotal, GameConfig.TOTAL_CARDS),
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                        (state.earnedTotal.toFloat() / GameConfig.TOTAL_CARDS).coerceIn(0f, 1f)
                    )
                    .height(6.dp)
                    .background(Palette.goldTrack)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (state.earnedTotal == 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.collection_empty),
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                SecondaryButton(
                    text = stringResource(R.string.level_select),
                    onClick = onLevelSelect,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(state.rooms, key = { room -> room.roomIndex }) { room ->
                    RoomSection(room = room)
                }
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
private fun RoomSection(room: RoomCardSet) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, AccentGold.copy(alpha = if (room.complete) 0.6f else 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(roomNameRes(room.roomIndex)),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.room_cards,
                            room.earned,
                            GameConfig.CARDS_PER_ROOM
                        ),
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (room.complete) {
                        Text(
                            text = stringResource(R.string.room_complete_tick),
                            color = AccentGoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                room.symbols.forEachIndexed { index, symbol ->
                    CardTile(
                        owned = room.owned.getOrElse(index) { false },
                        payout = symbol.payout,
                        drawable = symbolDrawable(symbol),
                        tier = stringResource(symbolTierRes(symbol))
                    )
                }
            }

            Text(
                text = stringResource(R.string.collection_task),
                color = TextMuted,
                fontSize = 10.sp,
                letterSpacing = 0.6.sp
            )
        }
    }
}

@Composable
private fun CardTile(
    owned: Boolean,
    payout: Int,
    drawable: Int,
    tier: String
) {
    val earnedState = stringResource(R.string.state_earned)
    val lockedState = stringResource(R.string.state_locked)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics { stateDescription = if (owned) earnedState else lockedState }
    ) {
        Box(
            modifier = Modifier
                .size(width = 54.dp, height = 68.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceAlt)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = if (owned) tier else null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(36.dp)
                    .alpha(if (owned) 1f else 0.14f)
            )
            if (!owned) {
                Text(
                    text = stringResource(R.string.card_locked),
                    color = AccentChrome,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = if (owned) {
                stringResource(R.string.payout_multiplier, payout)
            } else {
                stringResource(R.string.card_hint)
            },
            color = if (owned) AccentGoldLight else DividerLine,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
