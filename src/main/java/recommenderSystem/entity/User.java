package recommenderSystem.entity;

public class User {
    /**
     * 用户id
     */
    Integer userId;
    /**
     * 用户名称
     */
    String userName;
    /**
     * 用户密码
     */
    String userPassword;

    /**
     * 是否是管理员 false为用户 true为管理员
     */
    Boolean isAdministration;
}
