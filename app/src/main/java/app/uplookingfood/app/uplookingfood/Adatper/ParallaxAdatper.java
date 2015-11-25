package app.uplookingfood.app.uplookingfood.Adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.RecicpeEntity;
import app.uplookingfood.app.uplookingfood.tools.BitmapHelper;



/**
 * Created by czw on 2015/11/16.
 */
public class ParallaxAdatper extends BaseAdapter {
    private Context context;
    private List<RecicpeEntity.Step> steps;
    private Bitmap bitmap;
    private BitmapUtils bitmapUtils;
    private  ColorDrawable TRANS;

    public ParallaxAdatper(Context context, List<RecicpeEntity.Step> steps) {
        this.context = context;
        this.steps = steps;
        bitmap = null;
        bitmapUtils = BitmapHelper.getBitmapUtils(context.getApplicationContext());
        TRANS=new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
        bitmapUtils.configDefaultLoadingImage(bitmap);
    }


    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public RecicpeEntity.Step getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_recipe_step, null);
            holder = new ViewHolder();
            holder.step_tv_position = (TextView) view.findViewById(R.id.recipe_detail_step_position);
            holder.step_tv_content = (TextView) view.findViewById(R.id.recipe_detail_step_content);
            holder.step_img = (ImageView) view.findViewById(R.id.recipe_detail_step_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.step_tv_position.setText((position+1)+"");
        holder.step_tv_content.setText(Html.fromHtml(getItem(position).title));
        holder.step_img.setImageBitmap(null);
        bitmapUtils.display(holder.step_img, getItem(position).img,new BitmapCustomCallBack());
        return view;
    }

    /**
     * 回调方法
     */
    private class BitmapCustomCallBack extends DefaultBitmapLoadCallBack<ImageView> {
        @Override
        public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
            FadeInDisplay(imageView,bitmap);
        }
    }

    private void FadeInDisplay(ImageView imageView, Bitmap bitmap) {
        /**
         * 设置一个淡入淡出效果,并且将图片转成Drawable的类型
         */
        TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[]{TRANS, new BitmapDrawable(imageView.getResources(), bitmap)});
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }

    private class ViewHolder {
        TextView step_tv_position;
        TextView step_tv_content;
        ImageView step_img;

    }
}
