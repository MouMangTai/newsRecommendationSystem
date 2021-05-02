package recommenderSystem.entity;

import lombok.Data;

@Data
public class typeDomain {
    private String userName;
    private String prefer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrefer() {
        return prefer;
    }

    public void setPrefer(String prefer) {
        this.prefer = prefer;
    }
}
