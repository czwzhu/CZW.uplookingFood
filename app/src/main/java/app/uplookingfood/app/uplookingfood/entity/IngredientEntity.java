package app.uplookingfood.app.uplookingfood.entity;

/**
 * Created by czw on 2015/11/10.
 */
public class IngredientEntity {
    public String name;
    public String imageName;
    public IngredientEntity(String name,String imageName){
        this.name=name;
        this.imageName=imageName;
    }


    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }
}
