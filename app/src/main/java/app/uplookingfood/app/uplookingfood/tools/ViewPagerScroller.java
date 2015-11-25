package app.uplookingfood.app.uplookingfood.tools;

import android.content.Context;
import android.widget.Scroller;

public class ViewPagerScroller extends Scroller {

    public ViewPagerScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        duration = 1000;
        super.startScroll(startX, startY, dx, dy, duration);
    }
}
