package dev.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.designsystem.R as Res

@Composable
private fun outfitFontFamily(): FontFamily = FontFamily(
    Font(Res.font.outfit_bold, weight = FontWeight.Bold),
    Font(Res.font.outfit_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.outfit_medium, weight = FontWeight.Medium),
)

@Composable
private fun poppinsFontFamily(): FontFamily = FontFamily(
    Font(Res.font.poppins_regular, weight = FontWeight.Normal),
    Font(Res.font.poppins_bold, weight = FontWeight.Bold)
)

@Composable
private fun interFontFamily(): FontFamily = FontFamily(
    Font(Res.font.inter_regular, weight = FontWeight.Normal)
)

private val defaultTypography = Typography()

@Composable
internal fun myCustomTypography(): Typography {
    val outfitFontFamily = outfitFontFamily()
    val interFontFamily = interFontFamily()
    val poppinsFontFamily = poppinsFontFamily()

    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        displayMedium = defaultTypography.displayMedium.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        displaySmall = defaultTypography.displaySmall.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = interFontFamily,
        ),
        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = interFontFamily,
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = interFontFamily,
        ),
        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = outfitFontFamily,
            fontWeight = FontWeight.Bold,
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = interFontFamily,
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = interFontFamily,
        ),
    )
}

