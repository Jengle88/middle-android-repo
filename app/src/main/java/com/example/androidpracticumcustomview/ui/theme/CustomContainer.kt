package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // Измеряем каждый дочерний элемент
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
            }
        }

        // Определяем размеры контейнера
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = determineSize(widthMode, widthSize)
        val height = determineSize(heightMode, heightSize)

        setMeasuredDimension(width, height)
    }

    private fun determineSize(receivedMode: Int, receivedSize: Int) = when (receivedMode) {
        MeasureSpec.EXACTLY -> receivedSize
        MeasureSpec.AT_MOST -> minOf(receivedSize, receivedSize)
        MeasureSpec.UNSPECIFIED -> receivedSize
        else -> receivedSize
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // Получаем размеры дочерних элементов контейнера
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.isGone) continue

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val childLeft = (width - childWidth) / 2
            val childHeightIfEven = if (i % 2 == 0) childHeight else 0
            val childTop = (height) / 2 - childHeightIfEven

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
        }
    }

    override fun addView(child: View) {
        if (childCount >= 2) {
            throw IllegalStateException("CustomContainer can only have up to 2 child views")
        }

        // Подготовка дочернего элемента к анимации
        child.alpha = 0f
        super.addView(child)

        child.post { // запускаем анимацию после того, как произойдет измерение и размещение
            child.animate()
                .alpha(1f) // Анимация появления
                .setDuration(VISIBILITY_ANIMATION_DURATION)
                .start()

            val indexOfChild = indexOfChild(child)
            val translationValue = height / 2 - child.measuredHeight
            val translation = if (indexOfChild % 2 == 0) {
                -translationValue // Если индекс четный, перемещаем вверх
            } else {
                translationValue // Если индекс нечетный, перемещаем вниз
            }
            child.animate()
                .translationY(translation.toFloat()) // Анимация перемещения
                .setDuration(TRANSLATION_ANIMATION_DURATION)
                .start()
        }
    }

    private companion object {
        private const val TRANSLATION_ANIMATION_DURATION = 5000L
        private const val VISIBILITY_ANIMATION_DURATION = 2000L
    }
}
