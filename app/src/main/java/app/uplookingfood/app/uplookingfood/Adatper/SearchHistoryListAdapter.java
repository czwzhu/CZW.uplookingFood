package app.uplookingfood.app.uplookingfood.Adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.SearchHistoryEntity;
public class SearchHistoryListAdapter extends BaseAdapter {
    private List<SearchHistoryEntity> list;
    private LayoutInflater mInflater;
    public SearchHistoryListAdapter(List<SearchHistoryEntity> list, Context context) {
        super();
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public SearchHistoryEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_search_history, null);
            holder = new ViewHolder();
            holder.typeTv = (TextView) convertView.findViewById(R.id.item_search_type);
            holder.contentTv = (TextView) convertView.findViewById(R.id.item_search_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeTv.setText("[" + getItem(position).getType() + "]");
        holder.contentTv.setText(getItem(position).getContent());
        return convertView;
    }
    private class ViewHolder {
        TextView typeTv;
        TextView contentTv;
    }
}
