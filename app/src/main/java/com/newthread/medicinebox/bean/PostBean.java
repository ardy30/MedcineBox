package com.newthread.medicinebox.bean;

import java.util.List;

/**
 * 用于帖子的解析
 * Created by 张浩 on 2016/3/21.
 */
public class PostBean{
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<content> getContents() {
        return contents;
    }

    public void setContents(List<content> contents) {
        this.contents = contents;
    }

    private String code;
    private String message;
    private List<content> contents;
    public static class content{
        public int getCommunicateId() {
            return communicateId;
        }

        public void setCommunicateId(int communicateId) {
            this.communicateId = communicateId;
        }

        public String getCommunicateTopic() {
            return communicateTopic;
        }

        public void setCommunicateTopic(String communicateTopic) {
            this.communicateTopic = communicateTopic;
        }

        public int getCommunicateZhuan() {
            return communicateZhuan;
        }

        public void setCommunicateZhuan(int communicateZhuan) {
            this.communicateZhuan = communicateZhuan;
        }

        public String getCommunicateTime() {
            return communicateTime;
        }

        public void setCommunicateTime(String communicateTime) {
            this.communicateTime = communicateTime;
        }

        public int communicateId;

        public String getCommunitcateContent() {
            return communitcateContent;
        }

        public void setCommunitcateContent(String communitcateContent) {
            this.communitcateContent = communitcateContent;
        }

        private String communitcateContent;
        private String communicateTopic;
        private int communicateZhuan;
        private String communicateTime;

        public List<String> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<String> pictureList) {
            this.pictureList = pictureList;
        }

        private List<String> pictureList;
        public CurrentUser userInfo;
        public static class CurrentUser{
            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserVirtualName() {
                return userVirtualName;
            }

            public void setUserVirtualName(String userVirtualName) {
                this.userVirtualName = userVirtualName;
            }

            public String getUserPicture() {
                return userPicture;
            }

            public void setUserPicture(String userPicture) {
                this.userPicture = userPicture;
            }

            public String getUserAge() {
                return userAge;
            }

            public void setUserAge(String userAge) {
                this.userAge = userAge;
            }

            public String getUserPossition() {
                return userPossition;
            }

            public void setUserPossition(String userPossition) {
                this.userPossition = userPossition;
            }

            public String getUserRegisterTime() {
                return userRegisterTime;
            }

            public void setUserRegisterTime(String userRegisterTime) {
                this.userRegisterTime = userRegisterTime;
            }

            public String getUserLastModify() {
                return userLastModify;
            }

            public void setUserLastModify(String userLastModify) {
                this.userLastModify = userLastModify;
            }

            private String userName;
            private String userVirtualName;
            private String userPicture;
            private String userAge;
            private String userPossition;
            private String userRegisterTime;
            private String userLastModify;
        }
        }
    }

