package tr.com.mis49m.asynctask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button btn;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read references
        tvResult = (TextView) findViewById(R.id.tv_result);
        btn=(Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] urls = new String[2];
                urls[0] = "url1";
                urls[1] = "url2";

                // call async task
                new BackgroundTask().execute(urls);
            }
        });
    }

    // Async Task
    private class BackgroundTask extends AsyncTask<String, Integer, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            // Sets the current progress
            progressDialog.setProgress(0);
            // Sets the maximum allowed progress value
            progressDialog.setMax(10);
            progressDialog.setCancelable(true);
            // Sets the style of this ProgressDialog, either STYLE_SPINNER or STYLE_HORIZONTAL
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // cancel async task
                    cancel(true);
                }
            });

            // Creates and shows a ProgressDialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            int size = urls.length;
            progressDialog.setMax(size);

            for(int i=0; i<size; i++){
                try {
                    // wait 3 seconds
                    Thread.sleep(3000);
                    // check for cancel
                    if(isCancelled())
                        return "canceled!";
                    // write log to console
                    Log.i("URL:", urls[i]);

                    // update progress dialog
                    publishProgress(i+1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            // return value to onPostExecute
            return "ok";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Integer currentProggress = values[0];
            // update progress bar
            progressDialog.setProgress(currentProggress);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // close progress dialog
            progressDialog.dismiss();
            // set tvResult value to "ok"
            tvResult.setText(result);
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            // close progress dialog
            progressDialog.dismiss();
            // set tvResult value to "canceled"
            tvResult.setText(result);
        }
    }

}
