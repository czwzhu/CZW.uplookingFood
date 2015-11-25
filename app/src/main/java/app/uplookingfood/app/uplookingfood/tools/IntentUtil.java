package app.uplookingfood.app.uplookingfood.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.view.View;

import app.uplookingfood.R;

public class IntentUtil {

    public static void startActivity(Context context,Intent intent){

//		View view = aty.findViewById(android.R.id.content);
//		Bitmap bitmap = getBitmapFromView(view);
//		MyApplication app = (MyApplication)aty.getApplication();
//		app.bitmap = bitmap;
        Activity aty = (Activity) context;
        context.startActivity(intent);
        aty.overridePendingTransition(R.anim.anim_slide_activity_open_enter,
                R.anim.anim_slide_activity_open_exit);
    }
    public static void startActivityForResult(Context context,Intent intent,int requstCode){

//		View view = aty.findViewById(android.R.id.content);
//		Bitmap bitmap = getBitmapFromView(view);
//		MyApplication app = (MyApplication) aty.getApplication();
//		app.bitmap = bitmap;
        Activity aty = (Activity) context;
        aty.startActivityForResult(intent, requstCode);
        aty.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    /**
     * 获取View对象的Bitmap
     * @param view
     * @return
     */
    private static Bitmap getBitmapFromView(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}
