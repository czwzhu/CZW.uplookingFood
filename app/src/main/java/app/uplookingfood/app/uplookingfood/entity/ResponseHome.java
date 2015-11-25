package app.uplookingfood.app.uplookingfood.entity;
import java.util.ArrayList;
/**
 * Created by czw on 2015/11/9.
 */
public class ResponseHome {
    public HeadObject headObject;
    public Recipe_object recipe_object;
    public static class HeadObject {
        public ArrayList<Food> list;

        public static class Food {
            public int recipeId;
            public String name;
            public String img;

            public Food(int recipeId, String name, String img) {
                this.recipeId = recipeId;
                this.name = name;
                this.img = img;

            }
        }
    }

    public static class Recipe_object {
        public ArrayList<HotFood> list;

        public static class HotFood {
            public int id;
            public String title;
            public String img;
            public HotFood(int id,String title,String img){
                this.id=id;
                this.title=title;
                this.img=img;

            }
        }
    }

    public HeadObject getHeadObject() {
        return headObject;
    }

    public Recipe_object getRecipe_object() {
        return recipe_object;
    }
}
