package app.uplookingfood.app.uplookingfood.tools;
import android.content.Context;
public class UserPrefrence {
    private static final String SHARED_NAME = "duoduo";

    public static void putUserName(Context context, String userName) {
        SharedPrefrenceTool.put(context, SHARED_NAME, "user_name", userName);
    }

    public static String getUserName(Context context) {
        return SharedPrefrenceTool.getStr(context, SHARED_NAME, "user_name");
    }

    public static void putPassword(Context context, String password) {
        SharedPrefrenceTool.put(context, SHARED_NAME, "password", password);
    }

    public static String getPassword(Context context) {
        return SharedPrefrenceTool.getStr(context, SHARED_NAME, "password");
    }

    public static boolean isLogin(Context context) {
        return SharedPrefrenceTool.getBoolean(context, SHARED_NAME, "login");
    }

    public static void setLogin(Context context, boolean login) {
        SharedPrefrenceTool.putBoolean(context, SHARED_NAME, "login", login);
        if (loginStateChangeListener != null) {
            loginStateChangeListener.onLoginStateChange(login);
        }
    }

    public static interface OnLoginStateChangeListener {
        void onLoginStateChange(boolean login);
    }

    private static OnLoginStateChangeListener loginStateChangeListener;

    public static void registOnLoginStateChangeListener(OnLoginStateChangeListener loginStateChangeListener) {
        UserPrefrence.loginStateChangeListener = loginStateChangeListener;
    }

    public static void putNickName(Context context, String nickName) {
        SharedPrefrenceTool.put(context, SHARED_NAME, "nick_name", nickName);
    }

    public static String getNickName(Context context) {
        return SharedPrefrenceTool.getStr(context, SHARED_NAME, "nick_name");
    }

    public static void putHeadPhoto(Context context, String headPhoto) {
        SharedPrefrenceTool.put(context, SHARED_NAME, "head_photo", headPhoto);
    }

    public static String getHeadPhoto(Context context) {
        return SharedPrefrenceTool.getStr(context, SHARED_NAME, "head_photo");
    }

    public static void putUserInfoJson(Context applicationContext, String json) {
        SharedPrefrenceTool.put(applicationContext, SHARED_NAME, "user_info", json);
    }

    public static String getUserInfoJson(Context context) {
        return SharedPrefrenceTool.getStr(context, SHARED_NAME, "user_info");
    }
}
