package app.uplookingfood.app.uplookingfood.Adatper;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by czw on 2015/11/5.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private List<View> view;

    public PagerAdapter(List<View> views) {
        this.view = views;
    }

    @Override
    public int getCount() {
        return view.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = view.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = view.get(position);
        container.removeView(v);

    }
}
