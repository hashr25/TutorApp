package com.cs410tutorgroup.tutorme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hashr25 on 3/24/15.
 */
public class Tutor
{
    //Attributes
    public int tutorID;
    public String firstName;
    public String lastName;
    public String emailAddress;
    public String subject;
    public String bio;
    public String pictureURL;
    public Drawable picture;

    //Methods

    /**
     *
     * @param jsonObj This JSON Object comes from the ApiConnector from querying for tutors
     * @return  Returns the Tutor Object
     */
    public static Tutor loadFromJsonObject(JSONObject jsonObj)
    {
        Tutor tutor = new Tutor();

        try
        {
            tutor.tutorID = jsonObj.getInt("tutor_id");
            tutor.firstName = jsonObj.getString("first_name");
            tutor.lastName = jsonObj.getString("last_name");
            tutor.emailAddress = jsonObj.getString("email");
            tutor.bio = jsonObj.getString("bio");
            tutor.pictureURL = jsonObj.getString("picture");
            Globals.tempPicURL = tutor.pictureURL;
            Log.d("PictureURL", tutor.pictureURL);
            tutor.new LoadPictureTask().execute(tutor.pictureURL);

            while(tutor.picture == null)
            {
                if(Globals.tempPic != null)
                {
                    //This is to wait for the background process from the LoadPictureTask
                    tutor.picture = Globals.tempPic;
                }
            }

            Globals.tempPic = null;

        }
        catch (Exception e)
        {
            Log.d("TutorFail","Failed loading at the Tutor level");
            e.printStackTrace();
        }

        return tutor;
    }

    /**
     * This Class is used to load pictures from URL to Bitmap for Tutors
     */
    private class LoadPictureTask extends AsyncTask<String, Void, Drawable>
    {
        Drawable image;

        /**
         * @param params URL link(s) for photo. Received from inside Tutor Class
         * @return This returns the bitmap image
         */
        @Override
        protected Drawable doInBackground(String... params)
        {
            String urldisplay = params[0];
            Drawable image = null;
            Log.d("LoadingFrom: ", urldisplay);

            try
            {
                Log.d("Breakpoint1", "Passing by first attempt at loading image" );

                Globals.tempPic = drawableFromUrl(urldisplay);
                if(Globals.tempPic == null)
                {
                    Log.d("TutorImage", "The global image is null");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return image;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            Globals.tempPic = drawable;
        }

        private Drawable drawableFromUrl(String url) throws IOException
        {
            Bitmap x = null;

            try
            {

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);
            }
            catch (Exception e)
            {
                Log.d("CreatingDrawable", "Failed loaded the image from url");
                e.printStackTrace();
            }

            return new BitmapDrawable(x);
        }
    }

    private class LoadPhotoTask extends AsyncTask<ApiConnector, Long, Drawable>
    {

        @Override
        protected Drawable doInBackground(ApiConnector... params)
        {



            return null;
        }
    }
}