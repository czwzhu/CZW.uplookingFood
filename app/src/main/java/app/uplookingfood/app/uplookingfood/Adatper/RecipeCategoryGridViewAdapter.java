package app.uplookingfood.app.uplookingfood.Adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.ResponceCategory;

public class RecipeCategoryGridViewAdapter extends BaseAdapter {
    /**
     * 数据源
     **/
    private List<ResponceCategory.Category> list;
    private LayoutInflater mInflater;

    public RecipeCategoryGridViewAdapter(Context context) {
        super();
        list = new ArrayList<ResponceCategory.Category>();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ResponceCategory.Category getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recipe_category_gridview, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.item_recipe_category_gridview_nameTv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(getItem(position).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView tv;
    }

    /**
     * 重置数据源
     *
     * @param list
     */
    public void resetDatas(List<ResponceCategory.Category> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
