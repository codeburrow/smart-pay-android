package com.codeburrow.android.smart_pay;

import android.util.Log;
import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/*
    build.gradle(Module:app)

    android{
        useLibrary 'org.apache.http.legacy'
    }
 */

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String routeName = "";
    static int routeID;
    static ArrayList<String> allRoutesArray = new ArrayList<>();
    static ArrayList<String> stationPointNameArray = new ArrayList<>();
    static ArrayList<String> stationPointTimeArray = new ArrayList<>();
    static ArrayList<Double> stationPointLatsArray = new ArrayList<>();
    static ArrayList<Double> stationPointLngsArray = new ArrayList<>();
    static ArrayList<LatLng> stationPointLatLngsArray = new ArrayList<>();
    static ArrayList<Double> snappedPointLatsArray = new ArrayList<>();
    static ArrayList<Double> snappedPointLngsArray = new ArrayList<>();
    static ArrayList<String> snappedPointOriginalIndexArray = new ArrayList<>();
    static ArrayList<String> snappedPointPlaceIDArray = new ArrayList<>();
    static ArrayList<LatLng> snappedPointLatLngsArray = new ArrayList<>();
    static ArrayList<String> startingTimesArray = new ArrayList<>();

    // constructor
    public JsonParser () {

    }


    public JSONObject getJSONFromUrl(final String url) {

        // Making HTTP request
        try {
            // Construct the client and the HTTP request.
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            // Execute the POST request and store the response locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // Extract data from the response.
            HttpEntity httpEntity = httpResponse.getEntity();
            // Open an inputStream with the data content.
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Create a BufferedReader to parse through the inputStream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            // Declare a string builder to help with the parsing.
            StringBuilder sb = new StringBuilder();
            // Declare a string to store the JSON object data in string form.
            String line = null;

            // Build the string until null.
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // Close the input stream.
            is.close();
            // Convert the string builder data to an actual string.
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Try to parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Return the JSON Object.
        return jObj;

    }


    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                //Log.e("Antony", is.toString());

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "UTF-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject loadJSONFromAssets(Activity activity, String jsonFile) {
        try {
            InputStream is = activity.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jObj;
    }

    public ArrayList<String> getRoutes(JSONObject jsonObject){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            allRoutesArray = new ArrayList<>();
            for (int i = 0; i < routesArray.length(); i++) {
                // Get the JSON object representing the route
                JSONObject routeObject = routesArray.getJSONObject(i);
                // Fill the headers/titles of the expandable list view
                // Get the JSON object representing the name of the route
                allRoutesArray.add(routeObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return allRoutesArray;
    }

    public String getRouteName(JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            routeName = routeObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeName;
    }

    public int getRouteID(JSONObject jsonObject, String routeName){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if ( routeName.equalsIgnoreCase(routesArray.getJSONObject(i).getString("name"))){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            routeID = routeObject.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeID;
    }

    public ArrayList<String> getStartingTimes(JSONObject jsonObject){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            startingTimesArray = new ArrayList<>();
            for (int i = 0; i < routesArray.length(); i++) {
                // Get the JSON object representing the route
                JSONObject routeObject = routesArray.getJSONObject(i);
                // Get the JSON array representing the stationPoints of the routes
                JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");
                // Get the JSON object representing the FIRST stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(0);

                startingTimesArray.add(stationPointObject.getString("stopTime").substring(0, 5));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return startingTimesArray;
    }


    public ArrayList<String> getStationNames (JSONObject jsonObject, int routeID){
        try{
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }

            // Get the JSON array representing the stationPoints of the routes
            JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");

            stationPointNameArray = new ArrayList<>();
            for (int i = 0; i < stationPointsArray.length(); i ++){
                // Get the JSON object representing the stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(i);
                stationPointNameArray.add(stationPointObject.getString("nameOfStop"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.d("JsonParser", "stationOPointNameArray.size():" + stationPointNameArray.size());
        return stationPointNameArray;
    }

    public ArrayList<String> getStationTimes (JSONObject jsonObject, int routeID){
        try{
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }

            // Get the JSON object representing the stationPoints of the routes
            JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");

            stationPointTimeArray = new ArrayList<>();
            for (int i = 0; i < stationPointsArray.length(); i ++){
                // Get the JSON object representing the stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(i);
                stationPointTimeArray.add(stationPointObject.getString("stopTime").substring(0,5));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return stationPointTimeArray;
    }

    public ArrayList<LatLng> getStationLatLngs (JSONObject jsonObject, int routeID){
        try{
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the stationPoints of the routes
            JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");

            stationPointLatLngsArray = new ArrayList<>();
            for (int i = 0; i < stationPointsArray.length(); i ++){
                // Get the JSON object representing the stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(i);
                stationPointLatLngsArray.add(new LatLng(stationPointObject.getDouble("lat"),
                        stationPointObject.getDouble("lng")));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return stationPointLatLngsArray;
    }

    public ArrayList<Double> getStationLats (JSONObject jsonObject, int routeID){
        try{
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the stationPoints of the routes
            JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");

            stationPointLatsArray = new ArrayList<>();
            for (int i = 0; i < stationPointsArray.length(); i ++){
                // Get the JSON object representing the stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(i);
                stationPointLatsArray.add(stationPointObject.getDouble("lat"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return stationPointLatsArray;
    }

    public ArrayList<Double> getStationLngs (JSONObject jsonObject, int routeID){
        try{
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the stationPoints of the routes
            JSONArray stationPointsArray = routeObject.getJSONArray("routeStops");

            stationPointLngsArray = new ArrayList<>();
            for (int i = 0; i < stationPointsArray.length(); i ++){
                // Get the JSON object representing the stationPoint
                JSONObject stationPointObject = stationPointsArray.getJSONObject(i);
                stationPointLngsArray.add(stationPointObject.getDouble("lng"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return stationPointLngsArray;
    }



    public ArrayList<LatLng> getSnappedLatLngs (JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the snappedPoints of the routes
            JSONArray snappedPointsArray = routeObject.getJSONArray("snappedPoints");

            snappedPointLatLngsArray = new ArrayList<>();
            for (int i = 0; i < snappedPointsArray.length(); i ++){
                // Get the JSON object representing the snappedPoint
                JSONObject snappedPointObject = snappedPointsArray.getJSONObject(i);
                // Get the JSON object representing the location of the snappedPoint
                JSONObject snappedLocationObject = snappedPointObject.getJSONObject("location");
                snappedPointLatLngsArray.add(new LatLng(snappedLocationObject.getDouble("latitude"),
                        snappedLocationObject.getDouble("longitude")));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return snappedPointLatLngsArray;
    }


    public ArrayList<Double> getSnappedLats (JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the snappedPoints of the routes
            JSONArray snappedPointsArray = routeObject.getJSONArray("snappedPoints");

            snappedPointLatsArray = new ArrayList<>();
            for (int i = 0; i < snappedPointsArray.length(); i ++){
                // Get the JSON object representing the snappedPoint
                JSONObject snappedPointObject = snappedPointsArray.getJSONObject(i);
                // Get the JSON object representing the location of the snappedPoint
                JSONObject snappedLocationObject = snappedPointObject.getJSONObject("location");
                snappedPointLatsArray.add(snappedLocationObject.getDouble("latitude"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return snappedPointLatsArray;
    }

    public ArrayList<Double> getSnappedLngs (JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the snappedPoints of the routes
            JSONArray snappedPointsArray = routeObject.getJSONArray("snappedPoints");

            snappedPointLngsArray = new ArrayList<>();
            for (int i = 0; i < snappedPointsArray.length(); i ++){
                // Get the JSON object representing the snappedPoint
                JSONObject snappedPointObject = snappedPointsArray.getJSONObject(i);
                // Get the JSON object representing the location of the snappedPoint
                JSONObject snappedLocationObject = snappedPointObject.getJSONObject("location");
                snappedPointLngsArray.add(snappedLocationObject.getDouble("longitude"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return snappedPointLngsArray;
    }

    public ArrayList<String> getSnappedOriginalIndexes (JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the snappedPoints of the routes
            JSONArray snappedPointsArray = routeObject.getJSONArray("snappedPoints");

//            Log.e("SnappedPoints.length: ", snappedPointsArray.length() + "");

            snappedPointOriginalIndexArray = new ArrayList<>();
            int counter = 0;
            int sum = 0;
            for (int i = 0; i < snappedPointsArray.length(); i++){
                // Get the JSON object representing the snappedPoint
                JSONObject snappedPointObject = snappedPointsArray.getJSONObject(i);
//                Log.e("OriginalIndex", snappedPointObject.toString());
                if (!snappedPointObject.has("originalIndex")){
                    snappedPointOriginalIndexArray.add("a");
//                    Log.e("OriginalIndex", "null");
                } else {
                    if (snappedPointObject.getInt("originalIndex") <= 75 && counter ==0){
                        snappedPointOriginalIndexArray
                                .add(String.valueOf(snappedPointObject.getInt("originalIndex")));
                        if (snappedPointObject.getInt("originalIndex") == 75) {
                            counter = 1;
                        }
//                    Log.e("OriginalIndex", "else");
                    } else {
                        sum = 76+snappedPointObject.getInt("originalIndex");
                        snappedPointOriginalIndexArray
                                .add(String.valueOf(sum));
                    }

                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return snappedPointOriginalIndexArray;
    }

    public ArrayList<String> getSnappedPlaceIDs (JSONObject jsonObject, int routeID){
        try {
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            // Get the JSON object representing the route with this ID
            JSONObject routeObject = new JSONObject();
            for (int i = 0; i < routesArray.length(); i++){
                if (routeID == routesArray.getJSONObject(i).getInt("ID")){
                    routeObject = routesArray.getJSONObject(i);
                }
            }
            // Get the JSON object representing the snappedPoints of the routes
            JSONArray snappedPointsArray = routeObject.getJSONArray("snappedPoints");

            snappedPointPlaceIDArray = new ArrayList<>();
            for (int i = 0; i < snappedPointsArray.length(); i ++){
                // Get the JSON object representing the snappedPoint
                JSONObject snappedPointObject = snappedPointsArray.getJSONObject(i);
                snappedPointPlaceIDArray.add(snappedPointObject.getString("placeId"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return snappedPointPlaceIDArray;
    }

}