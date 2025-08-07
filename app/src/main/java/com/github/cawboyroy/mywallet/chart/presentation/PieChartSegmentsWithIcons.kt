package com.github.cawboyroy.mywallet.chart.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import java.lang.Math.toRadians
import java.math.BigDecimal
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChartSegmentsWithIcons(
    composableSize: Int,
    modifier: Modifier = Modifier,
    records: PersistentList<FinancialRecordChartUi>,

    outerRadiusRatio: Float = 0.8f,
    innerRadiusRatio: Float = 0.4f,
    gapAngle: Float = 1f,
    onClick: (FinancialRecordChartUi) -> Unit
) {
    if (records.isEmpty())
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.no_data))
        }
    else
        PieChartInner(
            composableSize,
            modifier,
            records,
            outerRadiusRatio,
            innerRadiusRatio,
            gapAngle,
            onClick
        )
}

@Composable
private fun PieChartInner(
    composableSize: Int,
    modifier: Modifier = Modifier,
    records: PersistentList<FinancialRecordChartUi>,
    outerRadiusRatio: Float = 0.8f,
    innerRadiusRatio: Float = 0.4f,
    gapAngle: Float = 1f,
    onClick: (FinancialRecordChartUi) -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val canvasSize = Size(composableSize * density.density, composableSize * density.density)
    val iconPositions by remember(records) {
        derivedStateOf {
            val size = canvasSize
            val totalPercentage = //todo optimize by percentage() just from it.percentage()
                records.sumOf { it.percentage().toDouble() }.toFloat()
            val positions = mutableListOf<Offset>()

            val fullDiameter = size.minDimension
            val outerDiameter = fullDiameter * outerRadiusRatio
            val outerRadius = outerDiameter / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            var currentStartAngle = -90f
            val totalGapAngle = if (records.size > 1) gapAngle * records.size else 1f
            val availableAngleForSegments = 360f - totalGapAngle

            records.forEach { segment ->
                val sweepAngle =
                    (segment.percentage() / totalPercentage) * availableAngleForSegments
                if (sweepAngle > 0f) {
                    val middleAngle = currentStartAngle + sweepAngle / 2f
                    val angleRad = toRadians(middleAngle.toDouble())
                    val iconOffset = Offset(
                        x = center.x + outerRadius * cos(angleRad).toFloat(),
                        y = center.y + outerRadius * sin(angleRad).toFloat()
                    )
                    positions.add(iconOffset)
                }
                currentStartAngle += sweepAngle + gapAngle
            }
            positions
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val totalPercentage = records.sumOf { it.percentage().toDouble() }.toFloat()

            val fullDiameter = size.minDimension
            val outerDiameter = fullDiameter * outerRadiusRatio
            val outerRadius = outerDiameter / 2f
            val innerRadius = outerRadius * innerRadiusRatio

            val center = Offset(size.width / 2f, size.height / 2f)

            var currentStartAngle = -90f
            val totalGapAngle = if (records.size > 1) gapAngle * records.size else 1f
            val availableAngleForSegments = 360f - totalGapAngle

            records.forEachIndexed { index, segment ->
                val sweepAngle =
                    (segment.percentage() / totalPercentage) * availableAngleForSegments

                if (sweepAngle > 0f) {
                    val path = Path().apply {
                        val cos: Float = cos(toRadians(currentStartAngle.toDouble())).toFloat()
                        val sin: Float = sin(toRadians(currentStartAngle.toDouble())).toFloat()
                        moveTo( //move to outer circle straight
                            center.x + outerRadius * cos,
                            center.y + outerRadius * sin
                        )
                        arcTo(//outer arc to the right side
                            rect = Rect(
                                center.x - outerRadius,
                                center.y - outerRadius,
                                center.x + outerRadius,
                                center.y + outerRadius
                            ),
                            startAngleDegrees = currentStartAngle,
                            sweepAngleDegrees = sweepAngle,
                            forceMoveTo = false
                        )
                        lineTo(//from outer to inner circle straight line
                            center.x + innerRadius * cos(toRadians((currentStartAngle + sweepAngle).toDouble())).toFloat(),
                            center.y + innerRadius * sin(toRadians((currentStartAngle + sweepAngle).toDouble())).toFloat()
                        )
                        arcTo(//inner arc to the start
                            rect = Rect(
                                center.x - innerRadius,
                                center.y - innerRadius,
                                center.x + innerRadius,
                                center.y + innerRadius
                            ),
                            startAngleDegrees = currentStartAngle + sweepAngle,
                            sweepAngleDegrees = -sweepAngle,
                            forceMoveTo = false
                        )
                        close()
                    }

                    drawPath(path = path, color = segment.pieSegment(context).color, style = Fill)
                }

                currentStartAngle += sweepAngle + gapAngle
            }
        }

        val iconSize = 48.dp
        iconPositions.forEachIndexed { index, offset ->
            val pieSegment: PieSegment = records[index].pieSegment(context)
            if (index < records.size)
                Box(
                    modifier = Modifier
                        .testTag("PieChartSegmentIcon ${pieSegment.label}")
                        .size(iconSize)
                        .absoluteOffset {
                            with(density) {
                                IntOffset(
                                    (offset.x.toDp() - iconSize / 2).roundToPx(),
                                    (offset.y.toDp() - iconSize / 2).roundToPx()
                                )
                            }
                        }
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            clip = true
                        )
                        .background(
                            color = colorResource(R.color.white),
                            shape = CircleShape
                        )
                        .clickable {
                            onClick.invoke(records[index])
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(pieSegment.imageVectorResId),
                        contentDescription = pieSegment.label,
                        modifier = Modifier.size(iconSize * 0.6f),
                        tint = Color.Black
                    )
                }
        }
    }
}

data class PieSegment(
    val color: Color,
    @DrawableRes val imageVectorResId: Int,
    val label: String
)

@Preview(showBackground = true)
@Composable
fun PieChartWithIconsPreview() {
    val percentages = listOf(
        40f, 30f, 20f, 10f
    )
    val segments = listOf(
        PieSegment(Color(0xFFEF5350), R.drawable.ic_category_gifts, "Gifts"),
        PieSegment(Color(0xFF42A5F5), R.drawable.ic_category_kids, "Kids"),
        PieSegment(Color(0xFFFFCA28), R.drawable.ic_checkbook, "Groceries"),
        PieSegment(Color(0xFF66BB6A), R.drawable.ic_category_taxes, "Other")
    )

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                val configuration = LocalConfiguration.current
                val screenWidthDp = configuration.screenWidthDp

                PieChartInner(
                    screenWidthDp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenWidthDp.dp),
                    records = segments.mapIndexed { index, it ->
                        FinancialRecordChartUi.CategoryHeader(
                            true,
                            "$",
                            it.label,
                            BigDecimal.ZERO.toString(),
                            true,
                            percentages[index]
                        )
                    }.toPersistentList(),
                ) {
                }
            }
        }
    }
}