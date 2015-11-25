package app.uplookingfood.app.uplookingfood.entity;


public class SearchHistoryEntity {

    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SearchHistoryEntity(String type, String content) {
        super();
        this.type = type;
        this.content = content;
    }

    public SearchHistoryEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

}
