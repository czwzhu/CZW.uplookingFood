package app.uplookingfood.app.uplookingfood.Handler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.RecicpeEntity;
import app.uplookingfood.app.uplookingfood.tools.TimeFormater;

/**
 * Created by czw on 2015/11/13.
 */
public class ContinarHanlder implements View.OnClickListener {
    protected Context context;
    protected ImageView headImg;
    protected TextView nick_Name;
    protected TextView date_tv;
    protected TextView content_tv;
    private LinearLayout convertView;
    protected int recipseId;
    protected int totalCount;
    protected TextView convert_tv;
    protected Button convert_btn;
    private LinearLayout Containerr;
    private int originalCount = 0;
    public ContinarHanlder(Context context){
        this.context=context;
    }
    public void setDatas(View view,RecicpeEntity.Comments comments,int recipeId){
        this.convertView=(LinearLayout)view;
        this.recipseId=recipeId;
        if(comments!=null && comments.totalCount!=0){
            initViews();
            totalCount=comments.totalCount;
            convert_tv.setText(String.format(context.getString(R.string.togeter_speak), totalCount));
            for (RecicpeEntity.Comments.User user :comments.list)
            {
                convertView.addView(getContentItem(user));
            }
        }
    }
    private  void initViews(){
        convertView.setVisibility(View.VISIBLE);

        Containerr = (LinearLayout) convertView.findViewById(R.id.v_recipe_detail_comment_layout_commentContainer);
        convert_tv=(TextView)convertView.findViewById(R.id.v_recipe_detail_comment_layout_commentCount);
        convert_btn=(Button)convertView.findViewById(R.id.v_recipe_detail_comment_layout_commentAllBtn);
    }
    private View getContentItem(RecicpeEntity.Comments.User user){
        View view= LayoutInflater.from(context).inflate(R.layout.item_recipe_comment,null);
        headImg=(ImageView)view.findViewById(R.id.item_recipe_comment_headImg);
        nick_Name=(TextView)view.findViewById(R.id.item_recipe_comment_nickNameTv);
        content_tv = (TextView) view.findViewById(R.id.item_recipe_comment_contentTv);
        date_tv=(TextView)view.findViewById(R.id.item_recipe_comment_dateTv);
        String headImgUrl=user.userHeadPhoto;
        if(!TextUtils.isEmpty(headImgUrl)){
            //显示网络头像
        }
        nick_Name.setText(user.userNickName);
        content_tv.setText(user.content);
        date_tv.setText(TimeFormater.formatTime(user.commentDate));
        return view;
    }


    @Override
    public void onClick(View v) {

    }
    private int addCount = 0;
    //？？？？？？
//    public void addComment(RecicpeEntity.Comments.User comment) {
//        addCount++;
//        Log.e("yy2y2y2","进入了吗");
//        if (convertView.getVisibility()!= View.VISIBLE) {
//            Log.e("y1y1y1", "进入了吗");
//            //initViews();
//            Containerr.addView(getContentItem(comment));
//        } else if (Containerr.getChildCount() < 3) {
//            Containerr.addView(getContentItem(comment), 0);
//            convert_tv.setText(String.format(context.getString(R.string.togeter_speak),
//                    Containerr.getChildCount()));
//        } else {
//            Containerr.removeViewAt(2);
//            Containerr.addView(getContentItem(comment), 0);
//        }
//        convert_tv.setText(String.format(context.getString(R.string.togeter_speak), addCount + originalCount));
//    }
}
