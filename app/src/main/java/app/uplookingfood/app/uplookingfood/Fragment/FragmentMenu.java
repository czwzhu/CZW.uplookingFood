package app.uplookingfood.app.uplookingfood.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import app.slidingmenu.SlidingmenuActivity;
import app.uplookingfood.LoginActivity;
import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.View.CircleImageView;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;
import app.uplookingfood.app.uplookingfood.tools.UserPrefrence;

/**
 * Created by czw on 2015/11/6.
 */
public class FragmentMenu extends Fragment implements View.OnClickListener {
    private Class<?>[] Fragments;
    private int[] mCheckedIds;
    private CheckedTextView [] mCheckedTvs;
    private CircleImageView mHeadImg;
    private TextView mHeadTextView;
    private CheckedTextView mMenu_home;
    private CheckedTextView mMenu_menu;
    private CheckedTextView mMenu_photo;
    private CheckedTextView mMenu_setup;
    private SlidingmenuActivity slidingmenuActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_menu, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        slidingmenuActivity=(SlidingmenuActivity)getActivity();
        initViews();

    }

    @Override
    public void onClick(View v) {
        if (v==mHeadImg){
            /**
             * 如果已经登录了 进入个人信息里面
             */
            if(UserPrefrence.isLogin(getActivity())){
                /**
                 * 登录了 可以不用任何操作，或者跳转，待定中.
                 */
                Toast.makeText(getActivity(),"表示已经登录了，就要跳转到个人信息里面去",Toast.LENGTH_LONG).show();
            }else{
                /**
                 * 如果没登录，则跳转到登录界面
                 */
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                IntentUtil.startActivity(getActivity(),intent);
            }
        }else{
            for(int i=0;i<mCheckedTvs.length;i++){
                if(v==mCheckedTvs[i]){
                    slidingmenuActivity.switchFragment(Fragments[i]);
                    mCheckedTvs[i].setChecked(true);
                }else{
                    mCheckedTvs[i].setChecked(false);
                }
            }
        }
    }

    private void initViews() {
        /**
         * 初始化控件
         */
        Fragments = new Class[]{FragmentHome.class, FragmentCategory.class, FragmentPhoto.class, FragmentSteup.class};
        mCheckedIds=new int[]{R.id.menu_home,R.id.menu_menu,R.id.menu_photo,R.id.menu_setup};
        mCheckedTvs= new CheckedTextView[mCheckedIds.length];
        for (int i=0;i<mCheckedTvs.length;i++){
            mCheckedTvs[i]= (CheckedTextView) getActivity().findViewById(mCheckedIds[i]);
            mCheckedTvs[i].setOnClickListener(this);
        }
        mHeadImg=(CircleImageView)getActivity().findViewById(R.id.menu_user_img);
        mHeadImg.setOnClickListener(this);
        mHeadTextView=(TextView)getActivity().findViewById(R.id.menu_user_nickname_text);
        /**
         * 如果用户登录了，就获取用户的名字和头像显示在界面上
         */
        if (UserPrefrence.isLogin(getActivity())){
            mHeadTextView.setText(UserPrefrence.getNickName(getActivity()));
            String photo=UserPrefrence.getHeadPhoto(getActivity());
            if (TextUtils.isEmpty(photo)){
                /**
                 * 如果用户头像没有设置，则设置成系统默认的头像
                 */
                mHeadImg.setImageResource(R.drawable.default_user_photo);
            }
        }else{
            /**
             * 如果没有登录，头像设置成系统默认
             */
            mHeadImg.setImageResource(R.drawable.person_icon);
        }
    }
    /**
     * 选择主FragmentHome
     */
    public void checkHome() {
        onClick(mCheckedTvs[0]);
    }
}
