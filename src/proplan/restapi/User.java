package proplan.restapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Date;


public class User {
    public final static String LOGIN_URL = "http://localhost:5000/todo";
    private String email;
    public JsonObject mainData;
    private Time expiredTime;

    public JsonObject signIn(String email, String password) throws Exception {
        this.email = email;

        JsonObject body = new JsonObject();
        body.addProperty("type", "new");
        body.addProperty("email", email);
        body.addProperty("password", password);
        body.addProperty("refresh_token", "");

        expiredTime = new Time(new Date().getTime() + 60000*50);
        JsonObject data = request(body, LOGIN_URL, "PUT", true);
        if (data == null) {
            return null;
        } else {
            this.mainData = data;
            return this.mainData;
        }
    }

    public JsonObject getData() throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("token", this.mainData.get("idToken").getAsString());
        body.addProperty("id", 0);
        body.addProperty("title", "");
        body.addProperty("body", "");
        body.addProperty("section", "");
        body.addProperty("type", "get");

        JsonObject data = request(body, LOGIN_URL, "POST", true);


        return data == null? null : data;
    }

    public JsonObject request(JsonObject body, String urlString, String method, boolean isParse) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        System.out.println(body.toString());

        OutputStream out = connection.getOutputStream();
        out.write(body.toString().getBytes());
        out.flush();
        System.out.println(connection.getResponseCode());
        if(connection.getResponseCode()!= HttpURLConnection.HTTP_OK) {
            return null;
        }
        if(isParse) {
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            JsonReader reader = new JsonReader(bufferedReader);

            JsonObject result = new JsonParser().parse(reader).getAsJsonObject();
            inputStreamReader.close();
            bufferedReader.close();
            return result;
        }
        connection.disconnect();
        return null;
    }
}
