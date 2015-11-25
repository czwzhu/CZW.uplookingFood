package app.uplookingfood.app.uplookingfood.Adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.Constants;

public class RecipeListAdapter extends BaseAdapter{
    private List<ResponceRecipeList.RecipeSimple> list;
    private LayoutInflater inflater;
    private BitmapUtils mBitmapUtils;
    public RecipeListAdapter(Context context,List<ResponceRecipeList.RecipeSimple> list) {
        super();
        this.list = list;
        inflater = LayoutInflater.from(context);
        mBitmapUtils = new BitmapUtils(context, Constants.IMG_CACHE_PATH, 0.5f);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ResponceRecipeList.RecipeSimple getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_recipe_list, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.recipe_name);
            holder.stepCountTv = (TextView) convertView.findViewById(R.id.step_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //填充控件
        ResponceRecipeList.RecipeSimple data = getItem(position);
        holder.nameTv.setText(data.getName());
        holder.stepCountTv.setText(data.getStep_count()+"个步骤");
        //TODO
        //处理图片
        mBitmapUtils.display(holder.img, data.getImg());
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView nameTv;
        TextView stepCountTv;
    }

    public void addDatas(List<ResponceRecipeList.RecipeSimple> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
