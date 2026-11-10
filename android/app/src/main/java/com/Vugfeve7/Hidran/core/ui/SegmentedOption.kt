package com.Vugfeve7.Hidran.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.theme.AccentGoldLight
import com.Vugfeve7.Hidran.core.theme.BgBase
import com.Vugfeve7.Hidran.core.theme.DividerLine
import com.Vugfeve7.Hidran.core.theme.Palette
import com.Vugfeve7.Hidran.core.theme.SurfaceAlt
import com.Vugfeve7.Hidran.core.theme.TextSecondary

@Composable
fun SegmentedOption(
    labels: List<String>,
    captions: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedState = stringResource(R.string.state_selected)
    val idleState = stringResource(R.string.state_not_selected)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val active = index == selectedIndex
            val shape = RoundedCornerShape(14.dp)
            Button(
                onClick = { onSelect(index) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .semantics { stateDescription = if (active) selectedState else idleState },
                shape = shape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(if (active) 2.dp else 1.dp, if (active) AccentGoldLight else DividerLine),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (active) BgBase else TextSecondary
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (active) Palette.goldTrack else Brush.horizontalGradient(listOf(SurfaceAlt, SurfaceAlt)),
                            shape = shape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp,
                            color = if (active) BgBase else TextSecondary
                        )
                        val caption = captions.getOrNull(index)
                        if (caption != null) {
                            Text(
                                text = caption,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (active) BgBase.copy(alpha = 0.78f) else TextSecondary.copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }
        }
    }
}
