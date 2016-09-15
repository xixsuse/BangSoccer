package upgrade.ntv.bangsoccer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import upgrade.ntv.bangsoccer.dao.DBNewsFeed;


/**
 * Created by ramnemilio1 on 7/21/2016.
 */
public class FacebookClass {

    private String postIDs="";
    private int newPost;
    private boolean success;

    public FacebookClass(){
        List<DBNewsFeed> newsFeeds = AppicationCore.getAllNewsFeed();

        for(int i=0; i<newsFeeds.size(); i++){
            postIDs=postIDs+", "+newsFeeds.get(i).getPostID();
        }

    }


    public int getPost(String user, final int qty){

        newPost=0;

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                user,
                new GraphRequest.Callback() {

                    @Override
                    public void onCompleted(GraphResponse response) {


                        try {

                            JSONObject json = response.getJSONObject();
                            String username= json.getString("name");
                            JSONObject jsonPOST = json.getJSONObject("posts");
                            JSONArray jarray = jsonPOST.getJSONArray("data");

                            for(int i = 0; i < qty; i++){
                                JSONObject obj = jarray.getJSONObject(i);
                                String id= obj.optString("id");
                                String message= obj.optString("message");
                                String imageURL= obj.optString("full_picture");
                                String date= obj.optString("created_time");
                                String story= obj.optString("story");


                                //Cleaning Date/Time
                                date = date.split("T")[0]+" ("+date.split("T")[1].substring(0,5)+")";

                                if(imageURL!=null && imageURL.length()>2 && !postIDs.contains(id)){
                                    newPost++;
                                    DownloadImage(id, username, imageURL, message, story, date);
                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,posts.limit("+(qty+1)+"){id,full_picture,message,created_time,story}");

        request.setParameters(parameters);
        //  request.executeAsync();
        request.executeAndWait();

        return newPost;
    }



    private byte[] bitmapToByte(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



    /**
     * Not Async, so needs to be called from a non UI thread
     */
    private  void DownloadImage(String id, String username, String url, String msg, String story, String date) {


        String urldisplay = url;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        DBNewsFeed newsFeed = new DBNewsFeed();
        newsFeed.setPostID(id);
        newsFeed.setUserName(username);
        newsFeed.setMessage(msg);
        newsFeed.setStory(story);
        newsFeed.setDate(date);
        newsFeed.setPicture(bitmapToByte(mIcon11));
        newsFeed.setLike(false);
        AppicationCore.getDbNewsFeedDao().insertInTx(newsFeed);

    }



    public boolean likePost(String postID){

        success=false;

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                postID+"/likes",
                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if (response!=null) {
                            if (response.getRawResponse().contains("true")) {
                                success = true;
                            }
                        }
                    }
                }
        ).executeAndWait();

        return success;
    }



    public boolean disLikePost(String postID){

        success=false;

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                postID+"/likes",
                null,
                HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if (response.getRawResponse().contains("true")){
                            success=true;
                        }
                    }
                }
        ).executeAndWait();

        return success;
    }


}

