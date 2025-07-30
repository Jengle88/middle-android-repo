package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp


/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?
) {
    // Начальные значения для анимации и видимости дочерних элементов
    var firstChildOffsetYAnimation by remember { mutableStateOf(0.dp) }
    var secondChildOffsetYAnimation by remember { mutableStateOf(0.dp) }
    var childrenVisibility by remember { mutableStateOf(false) }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        childrenVisibility = true
    }

    // Основной контейнер
    Layout(
        content = {
            // Контент
            firstChild?.let {
                val firstChildTransitionY by animateDpAsState(
                    firstChildOffsetYAnimation,
                    animationSpec = tween(OFFSET_ANIMATION_DURATION),
                )

                AnimatedVisibility(
                    visible = childrenVisibility,
                    modifier = Modifier
                        .offset(y = firstChildTransitionY),
                    enter = fadeIn(tween(VISIBILITY_ANIMATION_DURATION))
                ) {
                    firstChild()
                }
            }

            secondChild?.let {
                val secondChildTransitionY by animateDpAsState(
                    secondChildOffsetYAnimation,
                    animationSpec = tween(OFFSET_ANIMATION_DURATION),
                )

                AnimatedVisibility(
                    visible = childrenVisibility,
                    modifier = Modifier
                        .offset(y = secondChildTransitionY),
                    enter = fadeIn(tween(VISIBILITY_ANIMATION_DURATION))
                ) {
                    secondChild()
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val height = constraints.maxHeight
        val width = constraints.maxWidth

        // Измеряем дочерние элементы с учетом ограничений
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        layout(width, height) {
            val parentHeight = height.toDp()
            val centerY = height / 2
            val centerX = width / 2

            // Задаем дочерним элементам размер, расположение и анимацию
            for ((index, placeable) in placeables.withIndex()) {
                if (index % 2 == 0) {
                    val topFirst = centerY - placeables[index].height
                    val leftFirst = centerX - placeables[index].width / 2
                    val firstHeight = placeables[index].height.toDp()

                    placeable.place(leftFirst, topFirst)
                    firstChildOffsetYAnimation = -(parentHeight / 2 - firstHeight)
                } else {
                    val topSecond = centerY
                    val leftSecond = centerX - placeables[index].width / 2
                    val secondHeight = placeables[index].height.toDp()

                    placeable.place(leftSecond, topSecond)
                    secondChildOffsetYAnimation = (parentHeight / 2 - secondHeight)
                }
            }
        }
    }
}

private const val OFFSET_ANIMATION_DURATION = 5000
private const val VISIBILITY_ANIMATION_DURATION = 2000
