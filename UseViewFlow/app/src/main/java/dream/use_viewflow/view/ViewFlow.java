package dream.use_viewflow.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.List;

public class ViewFlow extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private MyViewPager viewPager;
    private ViewFlowAdapter pagerAdapter;

    private ViewFlowIndicator indicator;

    private static final int MSG = 1;
    private static final int MSG_DELAY = 5000;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG) {
                int total = msg.arg1;
                SoftReference<MyViewPager> reference = (SoftReference<MyViewPager>) msg.obj;
                MyViewPager viewPager = reference.get();
                if (viewPager != null) {
                    int currentPos = viewPager.getCurrentItem();
                    if (currentPos < total - 1) {
                        viewPager.myScroller.useFixedTime = true;
                        viewPager.setCurrentItem(currentPos + 1);
                        Message message = obtainMessage();
                        message.what = MSG;
                        message.arg1 = total;
                        message.obj = new SoftReference<>(viewPager);
                        sendMessageDelayed(message, MSG_DELAY);
                    }
                }
            }
        }
    };

    public ViewFlow(Context context) {
        super(context);
        addViewPagerAndIndicator(context);
    }

    public ViewFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        addViewPagerAndIndicator(context);
    }

    private void addViewPagerAndIndicator(Context context) {
        viewPager = new MyViewPager(context);
        viewPager.addOnPageChangeListener(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(viewPager, lp);

        indicator = new ViewFlowIndicator(context);
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        lp.addRule(CENTER_HORIZONTAL);
        lp.bottomMargin = (int) (getResources().getDisplayMetrics().density * 12);
        addView(indicator, lp);
    }

    public void setViewList(List<? extends View> viewList) {
        if (pagerAdapter == null) {
            pagerAdapter = new ViewFlowAdapter(viewList);
            viewPager.setAdapter(pagerAdapter);
            pagerAdapter.setStartPosition(viewPager);
            if (viewList.size() > 2) {
                startScroll();
            }
        } else {
            stopScroll();
            pagerAdapter.setViewList(viewList);
            pagerAdapter.setStartPosition(viewPager);
            if (viewList.size() > 2) {
                startScroll();
            }
        }
        indicator.setCount(viewList.size());
    }

    public void setCurrentPosition(int position) {
        pagerAdapter.setCurrentDataPosition(viewPager, position);
    }

    public int getCurrentPosition() {
        return pagerAdapter.getCurrentDataPosition(viewPager);
    }

    public void setDrawable(int uncheckedResId, int checkedResId, int intervalDip) {
        Resources.Theme theme = getContext().getTheme();
        Resources res = getResources();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            setDrawable(res.getDrawable(uncheckedResId, theme), res.getDrawable(checkedResId, theme), intervalDip);
        } else {
            setDrawable(res.getDrawable(uncheckedResId), res.getDrawable(checkedResId), intervalDip);
        }
    }

    public void setDrawable(Drawable unchecked, Drawable checked, int intervalDip) {
        indicator.setDrawable(unchecked, checked, intervalDip);
    }

    public void startScroll() {
        stopScroll();
        Message message = handler.obtainMessage();
        message.what = MSG;
        message.arg1 = pagerAdapter.getCount();
        message.obj = new SoftReference<>(viewPager);
        handler.sendMessageDelayed(message, MSG_DELAY);
    }

    public void stopScroll() {
        handler.removeMessages(MSG);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.setCurrentPos(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyViewPager extends ViewPager {

        MyScroller myScroller;

        public MyViewPager(Context context) {
            super(context);
            myScroller = new MyScroller(context);
            try {
                Field field = ViewPager.class.getDeclaredField("mScroller");
                field.setAccessible(true);
                field.set(this, myScroller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyScroller extends Scroller {

        private boolean useFixedTime;

        private MyScroller(Context context) {
            super(context, new LinearInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            if (useFixedTime) {
                useFixedTime = false;
                duration = 600;
            }
            super.startScroll(startX, startY, dx, dy, duration);
        }
    }
}
