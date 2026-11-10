package com.Vugfeve7.Hidran.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.theme.AccentGold
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextMuted

@Composable
fun DisclaimerChip(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = SurfaceCard.copy(alpha = 0.80f),
        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.20f))
    ) {
        Box(
            modifier = Modifier.padding(PaddingValues(horizontal = 12.dp, vertical = 5.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.disclaimer_short),
                color = TextMuted,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
