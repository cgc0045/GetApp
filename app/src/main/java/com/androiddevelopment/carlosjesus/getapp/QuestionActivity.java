package com.androiddevelopment.carlosjesus.getapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    Intent arIntent;
    ArrayList<String> questions;
    GetQuestions lol = new GetQuestions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        arIntent = new Intent(this, AlarmReceiver.class);

        arIntent.putExtra("notification", true);
        arIntent.putExtra("extra", false);


        Log.e("Execute?", lol.getStatus().toString());

        lol.execute();


        Log.e("Status", lol.getStatus().toString());


    }

    @SuppressLint("StaticFieldLeak")
    public class GetQuestions extends AsyncTask<Void, Void, Void> {

        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://opentdb.com/api.php?amount=1&difficulty=easy&type=multiple&encode=base64";
            ArrayList<NameValuePair> param = new ArrayList<>();


            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse;
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();

                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line).append("\n");
                    }

                    inputStream.close();
                    result = sBuilder.toString();

                } catch (Exception e) {
                    Log.e("StringBuilding", "Error converting result " + e.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            questions = new ArrayList<>();
            parseJSON();
            loadQuestion();

        } // protected void onPostExecute(Void v)

        private void parseJSON(){
            //parse JSON data
            try {
                JSONObject jArray = new JSONObject(result);
                JSONArray lol = jArray.getJSONArray("results");
                //Log.e("Results", lol.getJSONObject(0).getString("question"));
                //JSONArray questions = jArray.getJSONArray(1);


                questions.add(new String(Base64.decode(lol.getJSONObject(0).getString("question").getBytes(), 0)));
                questions.add(new String(Base64.decode(lol.getJSONObject(0).getJSONArray("incorrect_answers").getString(0).getBytes(), 0)));
                questions.add(new String(Base64.decode(lol.getJSONObject(0).getJSONArray("incorrect_answers").getString(1).getBytes(), 0)));
                questions.add(new String(Base64.decode(lol.getJSONObject(0).getJSONArray("incorrect_answers").getString(2).getBytes(), 0)));

                int random = (new Random()).nextInt(4 - 1 + 1) + 1;

                questions.add(random, new String(Base64.decode(lol.getJSONObject(0).getString("correct_answer").getBytes(), 0)));

                Log.e("Questions size", String.valueOf(questions.size()));

                questions.add(new String(Base64.decode(lol.getJSONObject(0).getString("correct_answer").getBytes(), 0)));

                for (String s : questions) {
                    Log.e("Results", s);
                }


            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }


    }

    private void loadQuestion(){

        while(lol.isCancelled()){
            lol.cancel(true);
        }

        TextView question = findViewById(R.id.question);
        question.setText(questions.get(0));

        Log.e("Size array", String.valueOf(questions.size()));

        final Button ans1 = findViewById(R.id.ans1);
        final Button ans2 = findViewById(R.id.ans2);
        final Button ans3 = findViewById(R.id.ans3);
        final Button ans4 = findViewById(R.id.ans4);

        ans1.setText(questions.get(1));
        ans2.setText(questions.get(2));
        ans3.setText(questions.get(3));
        ans4.setText(questions.get(4));

        final String correct = questions.get(5);

        Log.e("Correct", String.valueOf(correct));

        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans1, ans1.getText().toString(), correct);
            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans2, ans2.getText().toString(), correct);
            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans3, ans3.getText().toString(), correct);
            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans4, ans4.getText().toString(), correct);
            }
        });
    }

    private void isCorrect(Button btn, String answer, String correct){
        if(answer.equals(correct)){
            btn.setBackgroundColor(Color.parseColor("#006400"));

            sendBroadcast(arIntent);
        }else{
            btn.setBackgroundColor(Color.RED);

            btn.setBackgroundColor(Color.TRANSPARENT);
            while(!lol.isCancelled()) {
                lol.cancel(true);
            }
            lol = new GetQuestions();
            lol.execute();
        }
    }
}
