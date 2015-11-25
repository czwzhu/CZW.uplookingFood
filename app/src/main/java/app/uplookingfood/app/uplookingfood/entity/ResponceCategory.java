package app.uplookingfood.app.uplookingfood.entity;
import java.util.List;

/**
 * 菜谱所有分类数据对应的实体类
 *
 * @author Administrator
 */
public class ResponceCategory {
    private List<CategoryParent> result;

    public List<CategoryParent> getResult() {
        return result;
    }
    public void setResult(List<CategoryParent> result) {
        this.result = result;
    }
    public class CategoryParent {
        private String name;
        private int parentId;
        private String img;
        private List<Category> list;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getParentId() {
            return parentId;
        }
        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
        public String getImg() {
            return img;
        }
        public void setImg(String img) {
            this.img = img;
        }
        public List<Category> getList() {
            return list;
        }
        public void setList(List<Category> list) {
            this.list = list;
        }
    }
    public class Category {
        private int id;
        private String name;
        private int parentId;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getParentId() {
            return parentId;
        }
        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
    }
}
