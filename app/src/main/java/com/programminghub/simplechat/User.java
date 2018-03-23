package com.programminghub.simplechat;

import java.io.Serializable;

/**
 * Created by Administrator on 3/11/2018.
 */

public class User implements Serializable {

    private String emailAddress;
    private String userId;
    private String profilePic;

    public User() {
    }

    public User(String emailAddress, String userId, String profilePic) {
        this.emailAddress = emailAddress;
        this.userId = userId;
        this.profilePic = profilePic;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
