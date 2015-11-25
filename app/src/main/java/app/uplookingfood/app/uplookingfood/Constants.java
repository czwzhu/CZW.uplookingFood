package app.uplookingfood.app.uplookingfood;
import android.os.Environment;
public interface Constants {
    String SERVER_HOST = "http://123.56.145.151:8080/CookBook-server";
    String IMG_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()+
            "/meihaoshiguang/img";
    /**获取菜谱分类的接口地址**/
    String URL_CATEGORY = SERVER_HOST+"/category";
    /**获取主页数据的接口地址**/
    String URL_HOME = SERVER_HOST+"/home";
    /**获取分类菜谱列表的接口地址**/
    String URL_RECIPE_LIST = SERVER_HOST+"/cooklist?id=_id&page=_page&count=10";
    /**获取菜谱详情的接口**/
    String URL_RECIPE_DETAIL = SERVER_HOST+"/caipu?id=_id";
    /**根据菜谱名字查询菜谱的接口**/
    String URL_SEARCH_BY_NAME = SERVER_HOST + "/search_name?key=_key&page=_page&count=10";
    /**根据食材查询菜谱的接口**/
    String URL_SEARCH_BY_MATERIAL = SERVER_HOST + "/search_material?key=_key&page=_page&count=10";
    /**获取菜谱简短评论的接口**/
    String URL_GET_BRIEF_COOK_COMMENTS = SERVER_HOST + "/cookbook/brief_comments?cook_id=_cook_id&time_stamp=_time_stamp";
    /**获取菜谱all评论的接口**/
    String URL_GET_RECIPE_ALL_COMMENTS = SERVER_HOST + "/cookbook/all_comments?cook_id=_cook_id&page=_page&count=10";

    /**登入接口**/
    String URL_LOGIN = SERVER_HOST + "/login";
    int TAG_CATEGORY = 1;
    int TAG_SEARCH_BY_RECIPE_NAME = 2;
    int TAG_SEARCH_BY_INGREDIENT = 3;
    /**评论菜谱的接口**/
    String URL_ADD_COOK_COMMENT = SERVER_HOST + "/cookbook/add_comment";

    /**mob平台app-key**/
    String MOB_APP_KEY = "95c0d5588af8";
    /**mob平台app-密钥**/
    String MOB_APP_SECRET = "14c32b38e015d4c8d51ff43e081b109f";
    /**检测手机号是否已注册的接口**/
    String URL_CHECK_PHONE = SERVER_HOST + "/regist/check-phone?phone=_phone";

    String URL_REGISTER_BY_PHONE = SERVER_HOST + "/regist/phone";
}
