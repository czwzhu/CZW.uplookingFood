package app.uplookingfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.app.uplookingfood.Adatper.PagerAdapter;

public class GuideActivity extends Activity {
    ViewPager viewPager;
    List<View> views;
    int images[]={R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        /**
         * 创建一个View的集合 来装载图片
         */
        viewPager=(ViewPager)findViewById(R.id.vpager);
         views=new ArrayList<>();

        for (int i=0;i<images.length+1;i++){
            ImageView imageView=new ImageView(this);
            if(i!=images.length){
                imageView.setImageResource(images[i]);
                views.add(imageView);
            }else{
                views.add(imageView);
            }

        }
        PagerAdapter pagerAdapter =new PagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==(views.size()-1)){
                    Intent intent=new Intent(GuideActivity.this,ContentActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


}
