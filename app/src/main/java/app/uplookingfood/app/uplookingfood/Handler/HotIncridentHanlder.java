package app.uplookingfood.app.uplookingfood.Handler;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.View.CircleImageView;
import app.uplookingfood.app.uplookingfood.entity.IngredientEntity;

/**
 * Created by czw on 2015/11/10.
 */
public class HotIncridentHanlder implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private Context context;
    private AdpterView adpterView;
    private AssetManager am;
    private List<Drawable> drawableList;
    private  ArrayList<IngredientEntity> ingredientsList;

    public void initIncrident(GridView gridView, Context context) throws IOException {
        this.mGridView = gridView;
        this.context = context;
        am = context.getAssets();
        adpterView = new AdpterView();
        adpterView.ReadyDatas();
        mGridView.setAdapter(adpterView);
    }

    public class AdpterView extends BaseAdapter {

        public void ReadyDatas() throws IOException {
             ingredientsList = new ArrayList<>();
            ingredientsList.add(new IngredientEntity("萝卜", "luobo"));
            ingredientsList.add(new IngredientEntity("白菜", "baicai"));
            ingredientsList.add(new IngredientEntity("羊肉", "yangrou"));
            ingredientsList.add(new IngredientEntity("排骨", "paigu"));
            ingredientsList.add(new IngredientEntity("豆腐", "doufu"));
            ingredientsList.add(new IngredientEntity("黑木耳", "heimuer"));
            ingredientsList.add(new IngredientEntity("鸡翅", "jichi"));
            ingredientsList.add(new IngredientEntity("鸡肉", "jirou"));
            ingredientsList.add(new IngredientEntity("南瓜", "nangua"));
            ingredientsList.add(new IngredientEntity("牛肉", "niurou"));
            ingredientsList.add(new IngredientEntity("茄子", "qiezi"));
            drawableList = new ArrayList<>();
            for (IngredientEntity ingredient : ingredientsList) {
                drawableList.add(Drawable.createFromStream(am.open("shicai/" + ingredient.getImageName() + ".jpg"), ""));
            }
        }

        @Override
        public int getCount() {
            return ingredientsList.size();
        }

        @Override
        public Object getItem(int position) {
            return ingredientsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * LIstView,GridView优化
             */
            View view=convertView;
            ViewHolder holder;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.item_home_ingredient,null);
                holder=new ViewHolder();
                holder.titleName=(TextView)view.findViewById(R.id.item_home_head_ingredient_text);
                holder.imageName=(CircleImageView)view.findViewById(R.id.item_home_head_ingredient_img);
                view.setTag(holder);
            }else {
                holder=(ViewHolder)view.getTag();
            }
            holder.titleName.setText(ingredientsList.get(position).name);
            holder.imageName.setImageDrawable(drawableList.get(position));

            return view;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private  class ViewHolder{
        TextView titleName;
        CircleImageView imageName;
    }
}
