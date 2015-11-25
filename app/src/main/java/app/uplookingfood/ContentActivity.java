package app.uplookingfood;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import app.slidingmenu.SlidingmenuActivity;
import app.uplookingfood.app.uplookingfood.Fragment.FragmentCategory;
import app.uplookingfood.app.uplookingfood.Fragment.FragmentHome;
import app.uplookingfood.app.uplookingfood.Fragment.FragmentMenu;

public class ContentActivity extends SlidingmenuActivity {
    private FragmentMenu fragmentMenu;
    private FragmentHome fragmentHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentMenu=new FragmentMenu();
        fragmentHome=new FragmentHome();
        this.initFragments(fragmentMenu, fragmentHome);

    }
    @Override
    public void onBackPressed() {
        if(!menuIsOpen()){//菜单关闭
            if(getCurFragment().getClass().getName().equals(FragmentHome.class.getName())){
                //菜单关闭--如果现在处于主Fragment
                if(exit){
                    finish();
                }else{
                    exit = true;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(0, 2000);
                }
            }else{//菜单关闭--如果现在处于其它Fragment
                if(getCurFragment().getClass().getName().equals(FragmentCategory.class.getName())){
                    //如果处于菜谱分类的Fragment时，需要判断是否有展开项
                    FragmentCategory fragmentCategory = (FragmentCategory) getCurFragment();
                    if(fragmentCategory.isExpand()){
                        fragmentCategory.collapse();
                    }else{
                        openMenu();
                    }
                }else{
                    openMenu();
                }
            }
        }else{
            fragmentMenu.checkHome(); //切换到FragmentHome
        }
    }
    private boolean exit;
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            exit = false;
            return false;
        }
    });

}
