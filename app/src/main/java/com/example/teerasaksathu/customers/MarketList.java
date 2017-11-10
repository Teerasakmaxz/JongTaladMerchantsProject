package com.example.teerasaksathu.customers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MarketList extends AppCompatActivity {

    ListView marketList;
    String[] nameMarket, URLimage, marketAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_list);

        initInstances();

        loadMarketList loadMarketList = new loadMarketList();
        loadMarketList.execute();
    }


    public void initInstances() {
        marketList = (ListView) findViewById(R.id.marketList);

    }

    private class loadMarketList extends AsyncTask<Void, Void, String> {
        public static final String URL = "http://www.jongtalad.com/doc/load_market_list.php";

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error - " + e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                nameMarket = new String[jsonArray.length()];
                URLimage = new String[jsonArray.length()];
                marketAddress = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameMarket[i] = (jsonObject.getString("name"));
//                    URLimage[i] = (jsonObject.getString("picture_url"));
                    marketAddress[i] = (jsonObject.getString("address"));


                }

            } catch (JSONException e) {
                nameMarket = new String[1];
                URLimage = new String[1];
                marketAddress = new String[1];

                nameMarket[0] = "none";
                URLimage[0] = "none";
                marketAddress[0] = "none";

                e.printStackTrace();
            }

            MarketListAdapter marketListAdapter = new MarketListAdapter(MarketList.this, nameMarket, URLimage, marketAddress);
            marketList.setAdapter(marketListAdapter);
        }
    }

}
