package com.newthread.medicinebox.bean;

import java.util.List;

/**
 * 用于评论的解析
 * Created by 张浩 on 2016/4/1.
 */
public class PostCommentBean {
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

    public List<CommentContent> getContents() {
        return contents;
    }

    public void setContents(List<CommentContent> contents) {
        this.contents = contents;
    }

    private String code;
    private String message;
    private List<CommentContent> contents;
    public static class CommentContent{
        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCommentZan() {
            return commentZan;
        }

        public void setCommentZan(String commentZan) {
            this.commentZan = commentZan;
        }

        public String getCommunicationId() {
            return communicationId;
        }

        public void setCommunicationId(String communicationId) {
            this.communicationId = communicationId;
        }

        public String getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }

        private String commentId;
        private String commentContent;
        private String commentZan;
        private String userName;
        private String communicationId;
        private String commentTime;
        @Override
        public  String toString(){
            return commentId+" "+commentContent+" "+
                    commentZan+" "+userName+" "+communicationId+" "+commentTime;
        }
    }

}
