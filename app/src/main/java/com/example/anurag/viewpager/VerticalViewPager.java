package com.example.anurag.viewpager;

/**
 * Created by Android Studio.
 * User: Anurag Singh
 * Date: 20/4/18
 * Time: 3:12 PM
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalViewPager extends ViewPager {


    float mStartDragX;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @return {@code false} since a vertical view pager can never be scrolled horizontally
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    /**
     * @return {@code true} iff a normal view pager would support horizontal scrolling at this time
     */
    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollHorizontally(direction);
    }

    private void init() {
        // Make page transit vertical
        setPageTransformer(true, new VerticalPageTransformer());
        // Get rid of the overscroll drawing that happens on the left and right (the ripple)
     //   setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean toIntercept = super.onInterceptTouchEvent(flipXY(ev));
        // Return MotionEvent to normal


        float x = ev.getX();

        flipXY(ev);
        int direction=1;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x ) {
                    direction=-1;
                } else if (mStartDragX > x) {
                    direction=1;
                }
                MainActivity.MyPagerAdapter adapter = (MainActivity.MyPagerAdapter)this.getAdapter();
                if (adapter.getCurrentFragment() instanceof FragmentViewPager){
                    FragmentViewPager fragmentViewPager = (FragmentViewPager)adapter.getCurrentFragment();
                    toIntercept = !fragmentViewPager.getNestedScrollView().canScrollVertically(direction);
                    int range = fragmentViewPager.getNestedScrollView().computeVerticalScrollRange();
                    int offset = fragmentViewPager.getNestedScrollView().computeVerticalScrollOffset();
                    int extent = fragmentViewPager.getNestedScrollView().computeVerticalScrollExtent();
                    int percentage = (int)(100.0 * offset / (float)(range - extent));
                    
                    System.out.println("  range: "+range);
                    System.out.print("  offset: "+offset);
                    System.out.print("  extent: "+extent);
                    System.out.print("  percentage: "+percentage);
                    System.out.print("  direction: "+direction);

                    if (fragmentViewPager.getNestedScrollView().computeVerticalScrollOffset()==0 && direction==-1){
                        //top
                        return true;
                    }else if (percentage>99 && direction==1){
                        //bottom
                        return true;
                    }else {
                        //scroll viewpager
                        return false;
                    }

                }

                break;
        }


        return toIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final boolean toHandle = super.onTouchEvent(flipXY(ev));
        // Return MotionEvent to normal
        flipXY(ev);
        return toHandle;
    }



    private MotionEvent flipXY(MotionEvent ev) {
        final float width = getWidth();
        final float height = getHeight();
        final float x = (ev.getY() / height) * width;
        final float y = (ev.getX() / width) * height;
        ev.setLocation(x, y);
        return ev;
    }



    private static final class VerticalPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            final int pageWidth = view.getWidth();
            final int pageHeight = view.getHeight();
            if (position < -1) {
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) {
                view.setAlpha(1);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // set Y position to swipe in from top
                float yPosition = position * pageHeight;
                view.setTranslationY(yPosition);
            } else {
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
