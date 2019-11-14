package models;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notify extends AsyncTask<Void,Void,Void>
{
    private String token;
    private String title;
    private String message;

    public Notify(String token, String title, String message) {
        this.token = token;
        this.title = title;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key=AIzaSyBW3ke7mwLzHAj31hijzQuei5JZ7hEAc4I");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("to", token);

            JSONObject info = new JSONObject();
            info.put("title", ""+title);   // Notification title
            info.put("body", ""+message); // Notification body

            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }
        return null;
    }
}
