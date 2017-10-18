package dream.use_viewflow.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

class ViewFlowIndicator extends View {

    private static final int DEFAULT_SIZE = 12;
    private static final int DEFAULT_INTERVAL = 8;

    private float density;
    private Drawable unchecked;
    private Drawable checked;
    private int interval;
    private int count;
    private int currentPos;
    private float offset;

    ViewFlowIndicator(Context context) {
        super(context);
        density = getResources().getDisplayMetrics().density;
        unchecked = new ColorDrawable(0xffaaaaaa);
        checked = new ColorDrawable(0xff333333);
        interval = (int) (density * DEFAULT_INTERVAL);
    }

    void setDrawable(Drawable unchecked, Drawable checked, int intervalDip) {
        this.unchecked = unchecked;
        this.checked = checked;
        interval = (int) (density * intervalDip);
        requestLayout();
    }

    void setCount(int count) {
        this.count = count;
        requestLayout();
    }

    void setCurrentPos(int position, float offset) {
        this.currentPos = position % count;
        this.offset = offset;
        invalidate();
    }

    private int getDotWidth() {
        int width = unchecked.getIntrinsicWidth();
        if (width <= 0) {
            width = (int) (density * DEFAULT_SIZE);
        }
        return width;
    }

    private int getDotHeight() {
        int height = unchecked.getIntrinsicHeight();
        if (height <= 0) {
            height = (int) (density * DEFAULT_SIZE);
        }
        return height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (getDotWidth() + interval) * count - interval;
        setMeasuredDimension(width, getDotHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int dotWidth = getDotWidth();
        int dotHeight = getDotHeight();
        int left = 0;
        for (int i = 0; i < count; i++) {
            unchecked.setBounds(left, 0, left + dotWidth, dotHeight);
            unchecked.draw(canvas);
            left += dotWidth + interval;
        }
        left = (int)((dotWidth + interval) * (currentPos + offset));
        checked.setBounds(left, 0, left + dotWidth, dotHeight);
        checked.draw(canvas);
    }
}
