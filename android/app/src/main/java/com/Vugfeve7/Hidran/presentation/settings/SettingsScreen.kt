package com.Vugfeve7.Hidran.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
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
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary
import com.Vugfeve7.Hidran.core.ui.DisclaimerChip
import com.Vugfeve7.Hidran.core.ui.ScreenScaffold
import com.Vugfeve7.Hidran.core.ui.SegmentedOption
import com.Vugfeve7.Hidran.core.ui.modeNameRes
import com.Vugfeve7.Hidran.core.ui.speedNameRes
import com.Vugfeve7.Hidran.data.sample.SampleData
import com.Vugfeve7.Hidran.presentation.dialog.ResetProgressDialog

@Composable
fun SettingsRoute(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onSelectMode = { index -> settingsViewModel.selectMode(SampleData.modes[index]) },
        onSelectSpeed = { index -> settingsViewModel.selectSpeed(SampleData.speeds[index]) },
        onReduceMotion = { enabled -> settingsViewModel.toggleReduceMotion(enabled) },
        onPayTable = { enabled -> settingsViewModel.togglePayTable(enabled) },
        onAskReset = { settingsViewModel.askReset() },
        onCancelReset = { settingsViewModel.cancelReset() },
        onConfirmReset = { settingsViewModel.confirmReset() },
        onMenu = onMenu
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onSelectMode: (Int) -> Unit,
    onSelectSpeed: (Int) -> Unit,
    onReduceMotion: (Boolean) -> Unit,
    onPayTable: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.settings),
        overlay = Palette.settingsOverlay,
        onBack = onMenu,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                SettingBlock(label = stringResource(R.string.setting_mode)) {
                    SegmentedOption(
                        labels = SampleData.modes.map { stringResource(modeNameRes(it)) },
                        captions = SampleData.modes.map { mode ->
                            stringResource(R.string.stake_value, mode.stake)
                        },
                        selectedIndex = SampleData.modes.indexOf(state.defaultMode).coerceAtLeast(0),
                        onSelect = onSelectMode
                    )
                }
            }

            item {
                SettingBlock(label = stringResource(R.string.setting_speed)) {
                    SegmentedOption(
                        labels = SampleData.speeds.map { stringResource(speedNameRes(it)) },
                        captions = emptyList(),
                        selectedIndex = SampleData.speeds.indexOf(state.reelSpeed).coerceAtLeast(0),
                        onSelect = onSelectSpeed
                    )
                }
            }

            item {
                SwitchRow(
                    label = stringResource(R.string.setting_reduce_motion),
                    checked = state.reduceMotion,
                    onChange = onReduceMotion
                )
            }

            item {
                SwitchRow(
                    label = stringResource(R.string.setting_pay_table),
                    checked = state.showPayTable,
                    onChange = onPayTable
                )
            }

            item {
                OutlinedButton(
                    onClick = onAskReset,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AccentCrimson.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SurfaceCard.copy(alpha = 0.88f),
                        contentColor = AccentCrimson
                    )
                ) {
                    Text(
                        text = stringResource(R.string.reset_progress),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.4.sp
                    )
                }
            }

            item {
                DisclaimerChip(modifier = Modifier.fillMaxWidth())
            }

            item {
                Text(
                    text = stringResource(R.string.disclaimer_long),
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 15.sp
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

    if (state.confirmReset) {
        ResetProgressDialog(
            onConfirm = onConfirmReset,
            onCancel = onCancelReset
        )
    }
}

@Composable
private fun SettingBlock(label: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard.copy(alpha = 0.88f),
        border = BorderStroke(1.dp, DividerLine)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.4.sp
            )
            content()
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    val onState = stringResource(R.string.state_on)
    val offState = stringResource(R.string.state_off)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .semantics { stateDescription = if (checked) onState else offState },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard.copy(alpha = 0.88f),
        border = BorderStroke(1.dp, DividerLine)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BgBase,
                    checkedTrackColor = AccentGoldLight,
                    checkedBorderColor = AccentGold,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = SurfaceAlt,
                    uncheckedBorderColor = DividerLine
                )
            )
        }
    }
}
