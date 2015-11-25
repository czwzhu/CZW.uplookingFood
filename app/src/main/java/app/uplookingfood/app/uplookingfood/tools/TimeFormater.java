package app.uplookingfood.app.uplookingfood.tools;
import java.util.Calendar;
/**
 * 时间转换的工具类
 * 将一个long型的时间转成：
 * 刚刚 （1分钟以内）
 * 3分钟前（60分钟以内）
 * 今天 12:44
 * 昨天 18:50
 * 前天 20:11
 * 7月20号 13:22
 * 2014年2月1日
 *
 * @author Administrator
 */
public class TimeFormater {

    public static String formatTime(String dateTime) {
        long time = Long.parseLong(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        long curTime = System.currentTimeMillis();
        long offset = curTime - time;//评论的时间和现在的时间相差的毫秒数
        if (offset < 60 * 1000) {
            return "刚刚";
        }
        if (offset < 60 * 60 * 1000) {
            return offset / 60000 + "分钟前";
        }
        if (offset < 24 * 60 * 60 * 1000 && calendar.get(Calendar.DAY_OF_YEAR) == curCalendar.get(Calendar.DAY_OF_YEAR)) {
            return "今天" + hour + ":" + min;
        }
        if (offset < 2 * 24 * 60 * 60 * 1000 && calendar.get(Calendar.DAY_OF_YEAR) == -1 + curCalendar.get(Calendar.DAY_OF_YEAR)) {
            return "昨天" + hour + ":" + min;
        }
        if (offset < 3 * 24 * 60 * 60 * 1000 && calendar.get(Calendar.DAY_OF_YEAR) == -2 + curCalendar.get(Calendar.DAY_OF_YEAR)) {
            return "前天" + hour + ":" + min;
        }
        if (calendar.get(Calendar.YEAR) == curCalendar.get(Calendar.YEAR)) {
            return month + "月" + day + "日 " + hour + ":" + min;
        }
        return year + "年" + month + "月" + day + "日 ";
    }
}
