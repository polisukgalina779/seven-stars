package com.Vugfeve7.Hidran.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Vugfeve7.Hidran.R
import com.Vugfeve7.Hidran.core.theme.AccentCrimson
import com.Vugfeve7.Hidran.core.theme.SurfaceCard
import com.Vugfeve7.Hidran.core.theme.TextPrimary
import com.Vugfeve7.Hidran.core.theme.TextSecondary

@Composable
fun ResetProgressDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(20.dp),
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.reset_title),
                color = AccentCrimson,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.4.sp
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_body),
                color = TextSecondary,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentCrimson,
                    contentColor = TextPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.reset),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp
                )
            }
        }
    )
}
