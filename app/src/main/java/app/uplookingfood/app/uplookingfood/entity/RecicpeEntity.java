package app.uplookingfood.app.uplookingfood.entity;

import java.util.List;

/**
 * Created by czw on 2015/11/11.
 */
public class RecicpeEntity {
    public String name;
    public String introduce;
    public String img;
    public String main_materials;
    public String assist_materials;
    public List<Step> steps;
    public String tips;
    public int views;
    public int collection_count;
    public int comment_count;
    public Comments comments;
    public String comment_time_stamp;
    public class Step{
        public String title;
        public String img;
        public Step(String title,String img){
            this.title=title;
            this.img=img;
        }
    }
    public static class Comments{
        public int totalCount;
        public List<User> list;
        public String timeStamp;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;

        }



        public static class User{
            public String userNickName;
            public String content;
            public String commentDate;

            public void setContent(String content) {
                this.content = content;
            }

            public void setCommentDate(String commentDate) {
                this.commentDate = commentDate;
            }

            public void setUserHeadPhoto(String userHeadPhoto) {
                this.userHeadPhoto = userHeadPhoto;
            }

            public void setUserNickName(String userNickName) {
                this.userNickName = userNickName;
            }

            public String userHeadPhoto;
        }

        public List<User> getList() {
            return list;
        }
    }
    public List<Step> getSteps() {
        return steps;
    }
    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }
}
