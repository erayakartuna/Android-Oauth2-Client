package github.erayakartuna.oauth2client.Tools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by eray on 12/04/17.
 */

public class HttpRequest {

    protected RequestFinishListener listener;
    protected String targetURL;
    protected String method;
    protected HashMap<String,String> urlParameters;

    public HttpRequest(String targetURL,String method,HashMap<String,String> urlParameters,RequestFinishListener listener)
    {
        this.listener = listener;
        this.targetURL = targetURL;
        this.method = method;
        this.urlParameters = urlParameters;
    }

    public HttpRequest(String targetURL,String method,HashMap<String,String> urlParameters)
    {
        this.targetURL = targetURL;
        this.method = method;
        this.urlParameters = urlParameters;
    }

    public HttpRequest(String targetURL,String method,RequestFinishListener listener)
    {
        this.listener = listener;
        this.targetURL = targetURL;
        this.method = method;
    }

    public HttpRequest(String targetURL,String method)
    {
        this.targetURL = targetURL;
        this.method = method;
    }

    public HttpRequest(String targetURL)
    {
        this.targetURL = targetURL;
        this.method = "get";
    }

    public RequestFinishListener getListener() {
        return listener;
    }

    public void setListener(RequestFinishListener listener) {
        this.listener = listener;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, String> getUrlParameters() {
        return urlParameters;
    }

    public void setUrlParameters(HashMap<String, String> urlParameters) {
        this.urlParameters = urlParameters;
    }

    public void executeRequest() {
        AsyncHttpRequest task = new AsyncHttpRequest(this.targetURL,this.method,this.urlParameters);
        task.execute();
    }

    public void finish(String Response)
    {
        this.listener.onFinishRequest(Response);
    }

    public class AsyncHttpRequest extends AsyncTask<String, Void, String> {

        private final String targetURL;
        private final String method;
        private final HashMap<String,String> urlParameters;

        AsyncHttpRequest(String targetURL,String method,HashMap<String,String> urlParameters) {
            this.targetURL = targetURL;
            this.method = method;
            this.urlParameters = urlParameters;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader rd = null;

            try {

                StringBuilder parameters = new StringBuilder();
                for(HashMap.Entry<String, String> e : urlParameters.entrySet()){
                    if(parameters.length() > 0){
                        parameters.append('&');
                    }
                    parameters.append(URLEncoder.encode(e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
                }

                URL url = new URL(this.targetURL);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod(this.method);
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(parameters.toString().getBytes().length));

                connection.setUseCaches (false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream (
                        connection.getOutputStream ());
                wr.writeBytes (parameters.toString());
                wr.flush ();
                wr.close ();

                //Get Response
                InputStream is;
                if (connection.getResponseCode() / 100 == 2) { // 2xx code means success
                    is = connection.getInputStream();
                } else {
                    is = connection.getErrorStream();
                }

                rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(final String response) {
           finish(response);
        }

        @Override
        protected void onCancelled() {

        }
    }
}
