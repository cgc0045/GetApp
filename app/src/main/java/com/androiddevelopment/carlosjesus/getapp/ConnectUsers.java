package com.androiddevelopment.carlosjesus.getapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectUsers extends AppCompatActivity {

    ConnectDB jdbc;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_users);

        EditText your = findViewById(R.id.yourName);
        EditText couple = findViewById(R.id.coupleName);

        final String yName = your.getText().toString();
        final String cName = couple.getText().toString();

        jdbc = new ConnectDB(yName, cName);

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jdbc.execute();
                boolean flag = jdbc.getCheck();

                if (!flag){
                    Context context = getApplicationContext();
                    CharSequence text = "One username is already used";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public class ConnectDB extends AsyncTask<Integer, Integer, Integer> {

        String login = "carlosjesus";
        String pass = "android";
        String url = "jdbc:mysql://db4free.net:3307/androiddev";
        private PreparedStatement ps;
        private ResultSet rs;
        private Connection conn;
        String yName;
        String cName;
        boolean check;

        public ConnectDB(String yName, String cName){
            this.yName = yName;
            this.cName = cName;
        }

        private void connectDatabase(){
            try {
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(url, login, pass);

                if (conn == null){
                    throw new Exception("Error al conectar base de datos");
                }





            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean checkUsernames(String yName, String cName){

            check = true;
            try {
                ps = conn.prepareStatement("SELECT * FROM alarm");

                rs = ps.executeQuery();

                rs.last();
                Log.e("Size rs: ", String.valueOf(rs.getRow()));
                rs.beforeFirst();

                if(rs.isBeforeFirst()){
                    Log.e("Dentro", String.valueOf(rs.getRow()));
                    //while(!rs.isAfterLast()){

                        Log.e("yName", yName);
                        Log.e("cName", cName);
                        if (rs.getString("User1").equals(yName)){
                            check = false;
                        }

                        if (rs.getString("User2").equals(cName)){
                            check = false;
                        }
                    //}
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Log.e("Names used?", (String.valueOf(check)));
            return check;
        }

        private void closeConnection(){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Integer doInBackground(Integer... i) {
            connectDatabase();
            Log.e("User1", yName);
            checkUsernames(yName, cName);

            return 1;
        }

        @Override
        protected void onPostExecute(Integer i){


            //Log.e("Names used post?", (String.valueOf(check)));
            //closeConnection();
        }

        public boolean getCheck(){
            //Log.e("Names used?", (String.valueOf(check)));
            return check;
        }
    }
}
