package com.example;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends ListActivity
{
    private ArrayAdapter<String> aa;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        List<String> messages = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        setListAdapter(aa);
        aa.add("one");
        new NotificationService().execute();
    }
    
    public void onSend(View v) {
        String request = ((EditText)findViewById(R.id.text)).getText().toString();
        makeRequest(request, "http://10.1.13.64:5000/f");
        //aa.add();
    }

    public void onClear(View v) {
        aa.clear();
        clientNotification.getCookieStore().clear();
        //aa.add();
    }

    private class NotificationService extends AsyncTask{

        @Override
        protected Object doInBackground(Object... objects) {
            while (true) {
                String response = makeRequest(null, "http://10.1.13.64:5000/n");
                publishProgress(response);
            }
        }


        @Override
        protected void onProgressUpdate(Object... values) {
            String response = (String) values[0];
            if (!response.isEmpty())
                aa.insert(response, 0);
            if (aa.getCount() > 10)
                aa.remove(aa.getItem(10));
            System.err.println("response = " + values[0]);
        }
    }
    private DefaultHttpClient client;
    private DefaultHttpClient clientRequest = new DefaultHttpClient();
    private DefaultHttpClient clientNotification = new DefaultHttpClient();

    private HttpEntity getResponseEntity(AbstractHttpMessage method) throws Exception {
        HttpResponse response = client.execute((HttpUriRequest) method);
        return response.getEntity();
    }

    private String makeRequest(String request, String url)  {
        System.err.printf("REQUEST: %s --> %s\n", url, request);
        try {
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("head1", "iamheader");
        if (request != null) {
                httppost.setEntity(new StringEntity(request, "UTF-8"));
        }
        client =  (url.endsWith("/n")) ? clientNotification : clientRequest;
        HttpEntity entity = getResponseEntity(httppost);
        return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
