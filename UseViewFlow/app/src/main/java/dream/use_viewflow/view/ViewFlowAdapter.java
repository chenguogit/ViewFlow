package dream.use_viewflow.view;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 循环的ViewPager的Adapter
 *
 * @author cg
 */
class ViewFlowAdapter extends PagerAdapter {

    private static final int CYCLE_COUNT = 1000000;

    private List<? extends View> mListViews;

    /**
     * @param viewList mListViews
     */
    ViewFlowAdapter(List<? extends View> viewList) {
        mListViews = viewList;
    }

    void setViewList(List<? extends View> viewList) {
        mListViews = viewList;
        notifyDataSetChanged();
    }

    void setStartPosition(ViewPager viewPager) {
        int count = mListViews.size();
        if (count > 2) {
            viewPager.setCurrentItem(CYCLE_COUNT / 2 * count, false);
        } else {
            viewPager.setCurrentItem(0, false);
        }
    }

    int getCurrentDataPosition(ViewPager viewPager) {
        return viewPager.getCurrentItem() % mListViews.size();
    }

    void setCurrentDataPosition(ViewPager viewPager, int dataPosition) {
        int viewPagerPosition = viewPager.getCurrentItem();
        int currentDataPosition = viewPagerPosition % mListViews.size();
        viewPager.setCurrentItem(dataPosition - currentDataPosition + viewPagerPosition);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mListViews.get(position % mListViews.size());
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        int count = mListViews.size();
        if (count > 2) {
            count *= CYCLE_COUNT;
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
