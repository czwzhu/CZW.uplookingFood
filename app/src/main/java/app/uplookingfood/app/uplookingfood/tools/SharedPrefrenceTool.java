package app.uplookingfood.app.uplookingfood.tools;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefrenceTool {

    /**
     * 用SharedPreferences保存一个String字符串
     *
     * @param context
     * @param sharedName SharedPreferences xml文件名
     * @param key
     * @param content
     */
    public static void put(Context context, String sharedName, String key, String content) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        preferences.edit().putString(key, content).commit();
    }

    /**
     * 从SharedPreferences里获取一个字符串
     *
     * @param context
     * @param sharedName
     * @param key
     * @return
     */
    public static String getStr(Context context, String sharedName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static void putBoolean(Context context, String sharedName, String key, boolean content) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, content).commit();
    }

    public static boolean getBoolean(Context context, String sharedName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
