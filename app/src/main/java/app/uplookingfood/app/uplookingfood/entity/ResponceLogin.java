package app.uplookingfood.app.uplookingfood.entity;

/**
 * Created by czw on 2015/11/23.
 */
public class ResponceLogin {
    private int loginState;
    private ResponceUserInfo userInfo;
    public int getLoginState() {
        return loginState;
    }
    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }
    public ResponceUserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(ResponceUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
