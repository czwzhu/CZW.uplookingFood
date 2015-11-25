package app.uplookingfood.app.uplookingfood.Adatper;

import java.util.List;

public class ResponceRecipeList {

    private int total_count;
    private List<RecipeSimple> list;

    public static class RecipeSimple{
        private int cookbook_id;
        private String name;
        private String introduce;
        private String img;
        private int step_count;
        private int views;
        private int collection_count;
        private int comment_count;
        public int getCookbook_id() {
            return cookbook_id;
        }
        public void setCookbook_id(int cookbook_id) {
            this.cookbook_id = cookbook_id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getIntroduce() {
            return introduce;
        }
        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }
        public String getImg() {
            return img;
        }
        public void setImg(String img) {
            this.img = img;
        }

        public int getStep_count() {
            return step_count;
        }
        public void setStep_count(int step_count) {
            this.step_count = step_count;
        }
        public int getViews() {
            return views;
        }
        public void setViews(int views) {
            this.views = views;
        }
        public int getCollection_count() {
            return collection_count;
        }
        public void setCollection_count(int collection_count) {
            this.collection_count = collection_count;
        }
        public int getComment_count() {
            return comment_count;
        }
        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public RecipeSimple(int cookbook_id, String name, String introduce,
                            String img, int step_count, int views, int collection_count,
                            int comment_count) {
            super();
            this.cookbook_id = cookbook_id;
            this.name = name;
            this.introduce = introduce;
            this.img = img;
            this.step_count = step_count;
            this.views = views;
            this.collection_count = collection_count;
            this.comment_count = comment_count;
        }
        public RecipeSimple() {
            super();
            // TODO Auto-generated constructor stub
        }

    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<RecipeSimple> getList() {
        return list;
    }

    public void setList(List<RecipeSimple> list) {
        this.list = list;
    }

    public ResponceRecipeList(int total_count, List<RecipeSimple> list) {
        super();
        this.total_count = total_count;
        this.list = list;
    }

    public ResponceRecipeList() {
        super();
        // TODO Auto-generated constructor stub
    }


}