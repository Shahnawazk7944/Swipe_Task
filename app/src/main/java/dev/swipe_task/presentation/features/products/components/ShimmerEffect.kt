package dev.swipe_task.presentation.features.products.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.designsystem.theme.Swipe_TaskTheme
import dev.designsystem.theme.spacing

fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition(label = "")
    val alpha = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    ).value
    // background(color = Color.LightGray.copy(alpha = alpha))
    background(color = MaterialTheme.colorScheme.primary.copy(alpha = alpha))
}


@Composable
fun ProductsShimmerEffect(innerPadding: PaddingValues) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 20.dp, start = 12.dp, end = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = MaterialTheme.spacing.extraLarge)
                    .clip(RoundedCornerShape(60.dp))
                    .shimmerEffect()
            )
            Spacer(Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                items(10) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .shimmerEffect(),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(all = 20.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .size(285.dp, 243.dp)
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.extraLarge)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(15.dp))
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .width(100.dp)
                                .padding(start = MaterialTheme.spacing.extraLarge)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(15.dp))
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .width(100.dp)
                                .padding(start = MaterialTheme.spacing.extraLarge)
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(20.dp))

                    }
                }
            }
        }

}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ShimmerEffectPreview() {
    Swipe_TaskTheme {
        ProductsShimmerEffect(PaddingValues())
    }
}