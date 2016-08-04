package upgrade.ntv.bangsoccer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import upgrade.ntv.bangsoccer.dao.DBNewsFeed;


/**
 * Created by ramnemilio1 on 7/21/2016.
 */
public class FacebookClass {

    public FacebookClass(){

    }


    public void getPost(String user, final int qty){

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
                                String message= obj.optString("message");
                                String imageURL= obj.optString("full_picture");
                                String date= obj.optString("created_time");
                                String story= obj.optString("story");


                                //Cleaning Date/Time
                                date = date.split("T")[0]+" ("+date.split("T")[1].substring(0,5)+")";

                                if(imageURL!=null){

                                    DownloadImage(username, imageURL, message, story, date);
                                }

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,posts.limit("+(qty+1)+"){full_picture,message,created_time,story}");

        request.setParameters(parameters);
        //  request.executeAsync();
        request.executeAndWait();

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
    private  void DownloadImage(String username, String url, String msg, String story, String date) {


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
        newsFeed.setUserName(username);
        newsFeed.setMessage(msg);
        newsFeed.setStory(story);
        newsFeed.setDate(date);
        newsFeed.setPicture(bitmapToByte(mIcon11));
        AppicationCore.getDbNewsFeedDao().insertInTx(newsFeed);

    }


}
