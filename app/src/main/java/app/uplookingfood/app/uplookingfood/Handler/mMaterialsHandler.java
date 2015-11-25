package app.uplookingfood.app.uplookingfood.Handler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.uplookingfood.R;
import app.uplookingfood.app.uplookingfood.entity.MateriaslEntity;

/**
 * Created by czw on 2015/11/12.
 */
public class mMaterialsHandler {
    private Context context;
    private TextView mMaterialsName;
    private TextView mMaterialsAmount;
    private  List<MateriaslEntity> list;
    public mMaterialsHandler(Context context) {
        this.context = context;
    }

    private List<MateriaslEntity> getList(String materisals) {
        list=new ArrayList<>();
        /**
         * 先通过分号来分离对象，再通过逗号来分离对象
         */
        String[] str = materisals.split(";");
        for (String str1 : str) {
            String[] str2 = str1.split(",");
            if (str2.length == 1) {
                list.add(new MateriaslEntity(str2[0], ""));
            } else if (str2.length == 2) {
                list.add(new MateriaslEntity(str2[0], str2[1]));
            }
        }
        return list;
    }

    public void setDatas(LinearLayout container, String main_Materials, String assets_Materials) {
        list=new ArrayList<>();
        if (!TextUtils.isEmpty(main_Materials)) {
            list.addAll(getList(main_Materials));
        }
        if (!TextUtils.isEmpty(assets_Materials)) {
            list.addAll(getList(assets_Materials));
        }
        /**
         * 如果没有数据，则隐藏该控件
         */
        if(list.size()==0){
            container.setVisibility(View.GONE);
        }

        for(MateriaslEntity materiaslEntity:list){
            View view = LayoutInflater.from(context).inflate(R.layout.v_recipe_detail_food_material_item, null);
            mMaterialsName = (TextView) view.findViewById(R.id.recipe_detail_food_material_name);
            mMaterialsAmount = (TextView) view.findViewById(R.id.recipe_detail_food_material_amount);
            mMaterialsName.setText(materiaslEntity.materialsName);
            if(!TextUtils.isEmpty(materiaslEntity.amount)){
                mMaterialsAmount.setText(materiaslEntity.amount);
            }
            container.addView(view);

        }
    }



}
