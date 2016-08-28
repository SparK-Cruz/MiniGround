package br.adm.goldberg.miniground;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_SOURCE ="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new DiscoveryRhunaTask().execute("http://www.goldberg.adm.br/CommonGround/rhuna.txt");

    }

    private void updateRhuna(String _l) {

        Log.v(LOG_SOURCE, "Found rhuna instalation:"+_l);

        TextView status = (TextView) findViewById(R.id.login_status);

        if(_l != null) {
            status.append((CharSequence) String.format("\n"+this.getString(R.string.msg_connecting_to),  _l ));
        } else {
            status.append((CharSequence) "\n" + this.getString(R.string.error_rhuna_not_found));
        }


    }

    public void login(View _view) {

    }

    private class DiscoveryRhunaTask extends AsyncTask<String, Void, String> {

        private static final String LOG_SOURCE ="DiscoveryRhunaTask";

        protected String doInBackground(String... _u) {
            HttpURLConnection urlConnection=null;
            String rhunas=null;

            Log.v(LOG_SOURCE,"Trying to connect to "+_u[0]);

            try {

                URL url = new URL(_u[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuffer buf = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                if (reader != null) {
                    String str;
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n" );
                    }
                }
                rhunas = buf.toString();

            } catch (Exception e) {
                Log.e(LOG_SOURCE, "Connection error:" + e);
            }
            finally {
                urlConnection.disconnect();
            }

            return rhunas;
        }

  /*  protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    } */

        protected void onPostExecute(String _r) {
            Log.v(LOG_SOURCE,"Yoda Rhuna Instalation must be at "+_r);
            updateRhuna(_r);
        }
    }

}


