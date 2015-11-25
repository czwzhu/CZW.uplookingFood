package app.uplookingfood.app.uplookingfood.Handler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.RecipedetaActivity;
import app.uplookingfood.app.uplookingfood.entity.ResponseHome;
import app.uplookingfood.app.uplookingfood.tools.BitmapHelper;
import app.uplookingfood.app.uplookingfood.tools.IntentUtil;

/**
 * Created by czw on 2015/11/10.
 */
public class HotFoodHanlder implements AdapterView.OnItemClickListener {
    private List<ResponseHome.Recipe_object.HotFood> list;
    private Context context;
    public  AdapterView adapterView;
    private BitmapUtils bitmapUtils;
    private GridView mgridView;

    /**
     * 调用参数传过来~~ 然后进行setAdpter
     * @param context
     * @param list
     * @param gridView
     */
    public void inited(Context context, List<ResponseHome.Recipe_object.HotFood> list,GridView gridView){
        this.context=context;
        this.list=list;
        this.mgridView=gridView;
        adapterView =new AdapterView(list,context);
        mgridView.setAdapter(adapterView);
        mgridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, RecipedetaActivity.class);
        intent.putExtra("recipe_id",adapterView.getItem(position).id);
        IntentUtil.startActivity(context, intent);
    }

    public  class AdapterView extends BaseAdapter{
        public List<ResponseHome.Recipe_object.HotFood> list;
        public Context context;
        public AdapterView( List<ResponseHome.Recipe_object.HotFood> list,Context context){
            this.list=list;
            this.context=context;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ResponseHome.Recipe_object.HotFood getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            ViewHodler hodler;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.item_home_hot_recipe,null);
                hodler=new ViewHodler();
                hodler.tvTiltle=(TextView)view.findViewById(R.id.item_home_head_recipe_title);
                hodler.ivName=(ImageView)view.findViewById(R.id.item_home_head_recipe_img);
                view.setTag(hodler);
            }else{
                hodler=(ViewHodler)view.getTag();
            }
            bitmapUtils= BitmapHelper.getBitmapUtils(context.getApplicationContext());
            bitmapUtils.display(hodler.ivName,list.get(position).img);
            hodler.tvTiltle.setText(list.get(position).title);

            return view;
        }
    }
    private class ViewHodler{
        TextView tvTiltle;
        ImageView ivName;
    }
}
