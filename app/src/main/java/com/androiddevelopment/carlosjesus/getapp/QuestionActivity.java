package com.androiddevelopment.carlosjesus.getapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {

    Intent arIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        arIntent = new Intent(this, AlarmReceiver.class);

        arIntent.putExtra("notification", true);
        arIntent.putExtra("extra", false);

        loadQuestion();
    }

    private void loadQuestion(){
        ArrayList<ArrayList<String>> prueba = readQuestions();

        for (ArrayList<String> s : prueba){
            Log.e("CSV", s.get(0));
        }

        int question_number = (new Random()).nextInt(2 - 0 + 1);

        TextView question = (TextView) findViewById(R.id.question);
        question.setText(prueba.get(question_number).get(0));

        /*TextView ans1 = (TextView) findViewById(R.id.ans1_txt);
        TextView ans2 = (TextView) findViewById(R.id.ans2_txt);
        TextView ans3 = (TextView) findViewById(R.id.ans3_txt);
        TextView ans4 = (TextView) findViewById(R.id.ans4_txt);

        ans1.setText(prueba.get(question_number).get(1));
        ans2.setText(prueba.get(question_number).get(2));
        ans3.setText(prueba.get(question_number).get(3));
        ans4.setText(prueba.get(question_number).get(4));*/

        //new GetContacts().execute();

        final Button ans1 = (Button) findViewById(R.id.ans1);
        final Button ans2 = (Button) findViewById(R.id.ans2);
        final Button ans3 = (Button) findViewById(R.id.ans3);
        final Button ans4 = (Button) findViewById(R.id.ans4);

        ans1.setText(prueba.get(question_number).get(1));
        ans2.setText(prueba.get(question_number).get(2));
        ans3.setText(prueba.get(question_number).get(3));
        ans4.setText(prueba.get(question_number).get(4));

        final int correct = Integer.parseInt(prueba.get(question_number).get(5));
        Log.e("Correct", String.valueOf(correct));

        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans1, 1, correct);
            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans2, 2, correct);
            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans3, 3, correct);
            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorrect(ans4, 4, correct);
            }
        });
    }

    private void isCorrect(Button btn, int answer, int correct){
        if(answer == correct){
            btn.setBackgroundColor(Color.parseColor("#006400"));

            try {
                TimeUnit.MILLISECONDS.sleep(1);
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sendBroadcast(arIntent);
        }else{
            btn.setBackgroundColor(Color.RED);

            try {TimeUnit.MILLISECONDS.sleep(1);
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            btn.setBackgroundColor(Color.TRANSPARENT);
            loadQuestion();
        }
    }

    private void turnOff(){
        Intent arIntent = new Intent(this, AlarmReceiver.class);

        arIntent.putExtra("notification", true);
        arIntent.putExtra("extra", false);

        sendBroadcast(arIntent);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://opentdb.com/api.php?amount=1&difficulty=easy&type=multiple";
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();



            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = null;
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();

                try {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }

                    inputStream.close();
                    result = sBuilder.toString();

                } catch (Exception e) {
                    Log.e("StringBuilding", "Error converting result " + e.toString());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            //parse JSON data
            try {
                JSONObject jArray = new JSONObject(result);
                JSONArray lol = jArray.getJSONArray("results");
                Log.e("Results", lol.getString(3));
                //JSONArray questions = jArray.getJSONArray(1);
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)
    }

    private ArrayList<ArrayList<String>> readQuestions(){
        InputStream input = getResources().openRawResource(R.raw.questions);
        ArrayList<ArrayList<String>> questions = new ArrayList<ArrayList<String>>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String csvLine;
            boolean header = true;
            while ((csvLine = reader.readLine()) != null) {
                if (!header) {
                    List<String> row = Arrays.asList(csvLine.split(","));
                    ArrayList<String> row_array = new ArrayList<String>();
                    row_array.addAll(row);
                    questions.add(row_array);
                }else{
                    header = false;
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                input.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        return questions;
    }
}
