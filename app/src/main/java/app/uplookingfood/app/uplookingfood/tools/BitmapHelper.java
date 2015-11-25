package app.uplookingfood.app.uplookingfood.tools;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;
public class BitmapHelper {
    private BitmapHelper() {
    }

    private static BitmapUtils bitmapUtils;

    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
        }
        return bitmapUtils;
    }
}
