package com.zengqy.net;

import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @包名: com.zengqy.net
 * @USER: zengqy
 * @DATE: 2022/4/17 12:47
 * @描述:
 */
public class HttpTest {
    public static void main(String[] args) {

        postHttp();

    }


    public static void postHttp(){
        try {
            URL url = new URL("http://localhost:9102/post/comment");

            HttpURLConnection httpURLConnection  = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            httpURLConnection.setRequestProperty("Accept","*/*");


            CommentItem commentItem = new CommentItem("123456", "这是一个评论，测试");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



class CommentItem{
    private String articleId;
    private String commentContent;

    public CommentItem(String articleId, String commentContent) {
        this.articleId = articleId;
        this.commentContent = commentContent;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}