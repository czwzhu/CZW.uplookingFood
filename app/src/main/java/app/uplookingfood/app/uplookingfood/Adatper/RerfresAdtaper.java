package app.uplookingfood.app.uplookingfood.Adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.RecicpeEntity;

/**
 * Created by czw on 2015/11/16.
 */
public class RerfresAdtaper extends BaseAdapter {
    private Context context;
    private List<RecicpeEntity.Comments.User> list;
    public RerfresAdtaper(Context context,List<RecicpeEntity.Comments.User> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        ViewHolder holder;
        if(view==null){
             view= LayoutInflater.from(context).inflate(R.layout.item_recipe_comment,null);
            holder=new ViewHolder();
            holder.headImg=(ImageView)view.findViewById(R.id.item_recipe_comment_headImg);
            holder.nick_name=(TextView)view.findViewById(R.id.item_recipe_comment_nickNameTv);
            holder.content_tv = (TextView) view.findViewById(R.id.item_recipe_comment_contentTv);
            holder.date_tv=(TextView)view.findViewById(R.id.item_recipe_comment_dateTv);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.nick_name.setText(list.get(position).userNickName);
        holder.content_tv.setText(list.get(position).content);
        holder.date_tv.setText(list.get(position).commentDate);
        return view;
    }
    private class ViewHolder{
        TextView nick_name;
        ImageView headImg;
        TextView content_tv;
        TextView date_tv;
    }
    public void addDatas(List<RecicpeEntity.Comments.User> list1){
        list.addAll(list1);
    }

}
