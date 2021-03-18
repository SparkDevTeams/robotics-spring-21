package com.example.robotics_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;

/*
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraint.widget.ConstraintSet;
*/

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private WebView webView;
    private TextView messagesText;
    private PubNub pubnub;
    private String theChannel = "motors";
    private String sensorChannel = "sensors";


    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(this, "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);


        JsonObject message = new JsonObject();
        message.addProperty("humidity", 0);
        message.addProperty("tempF", 0);
        message.addProperty("MQ7", 0);
        message.addProperty("MQ3", 0);
        addHeaders(message);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("example.com");

        PNConfiguration pnConfiguration = new PNConfiguration();

        pnConfiguration.setPublishKey("xx");
        pnConfiguration.setSubscribeKey("xx");
        pnConfiguration.setUuid("theClientUUID");

        pubnub = new PubNub(pnConfiguration);


        pubnub.addListener(new SubscribeCallback()
        {


            @Override
            public void message(PubNub pubnub, PNMessageResult event)
            {
                JsonObject message = event.getMessage().getAsJsonObject();
                addHeaders(message);


//                displayMessage("[Sensors]", "Humidity: " + humidity + "\nTemperature (F): " + temp + "\nGas: " + gas + "\nMQ3: " + mq3);
            }



            @Override
            public void status(PubNub pubnub, PNStatus event) {
                  /*
               displayMessage("[STATUS: " + event.getCategory() + "]",
                        "connected to channels: " + event.getAffectedChannels());

                if (event.getCategory().equals(PNStatusCategory.PNConnectedCategory)){
                    submitUpdate(theEntry, "Harmless.");
                }
                 */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult event)
            {

                displayMessage("[PRESENCE: " + event.getEvent() + ']',
                        "uuid: " + event.getUuid() + ", channel: " + event.getChannel());
            }

            // even if you don't need these handler, you still have include them
            // because we are extending an Abstract class
            @Override
            public void signal(PubNub pubnub, PNSignalResult event) { }

            @Override
            public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) { }

            @Override
            public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) { }

            @Override
            public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) { }

            @Override
            public void messageAction(PubNub pubnub, PNMessageActionResult event) { }


            @Override
            public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) { }
        });

        pubnub.subscribe().channels(Arrays.asList(sensorChannel)).withPresence().execute();

        messagesText = findViewById(R.id.messages_text);


    }// end onCreate


/*
    protected void submitUpdate(String anEntry, String anUpdate) {
        JsonObject entryUpdate = new JsonObject();
        entryUpdate.addProperty("entry", anEntry);
        entryUpdate.addProperty("update", anUpdate);

        pubnub.publish().channel(theChannel).message(entryUpdate).async(
                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                        /*
                        else {
                            displayMessage("[PUBLISH: sent]",
                                    "timetoken: " + result.getTimetoken());
                        }

                    }
                });
    }

    */



    public void stop(View view)
    {

        JsonObject entry = new JsonObject();

        entry.addProperty("Input1", false);
        entry.addProperty("Input2", false);
        entry.addProperty("Input3", false);
        entry.addProperty("Input4", false);

        pubnub.publish().channel(theChannel).message(entry).async(


                new PNCallback<PNPublishResult>() {

                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                        /*
                        else {
                            displayMessage("[PUBLISH: sent]",
                                    "timetoken: " + result.getTimetoken());
                        }

                         */
                    }

                }

        );


    }

    public void sendDown(View view)
    {

        Button button = (Button)findViewById(R.id.down_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Input1", true);
                    entry.addProperty("Input2", false);
                    entry.addProperty("Input3", true);
                    entry.addProperty("Input4", false);

                    pubnub.publish().channel(theChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>()
                                    {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                            /*
                                            else {
                                                displayMessage("[PUBLISH: sent]",
                                                        "timetoken: " + result.getTimetoken());
                                            }

                                             */
                                        }
                                    }
                            );

                }else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    stop(view);

                }
                return false;
            }
        });

    }// end sendDown

    public void sendLeft(View view)
    {
        Button button = (Button)findViewById(R.id.left_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Input1", true);
                    entry.addProperty("Input2", false);
                    entry.addProperty("Input3", false);
                    entry.addProperty("Input4", true);

                    pubnub.publish().channel(theChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>()
                                    {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                            /*
                                            else {
                                                displayMessage("[PUBLISH: sent]",
                                                        "timetoken: " + result.getTimetoken());
                                            }

                                             */
                                        }
                                    }
                            );

                }else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    stop(view);

                }
                return false;
            }
        });

    }//end sendLeft

    public void sendRight(View view)
    {

        Button button = (Button)findViewById(R.id.right_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Input1", false);
                    entry.addProperty("Input2", true);
                    entry.addProperty("Input3", true);
                    entry.addProperty("Input4", false);

                    pubnub.publish().channel(theChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>()
                                    {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                            /*
                                            else {
                                                displayMessage("[PUBLISH: sent]",
                                                        "timetoken: " + result.getTimetoken());
                                            }

                                             */
                                        }
                                    }
                            );

                }else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    stop(view);

                }
                return false;
            }
        });
    }
    public void sendUp(View view)
    {

        Button button = (Button)findViewById(R.id.up_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Input1", false);
                    entry.addProperty("Input2", true);
                    entry.addProperty("Input3", false);
                    entry.addProperty("Input4", true);

                    pubnub.publish().channel(theChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>()
                                    {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                            /*
                                            else {
                                                displayMessage("[PUBLISH: sent]",
                                                        "timetoken: " + result.getTimetoken());
                                            }

                                             */
                                        }
                                    }
                            );

                }else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    stop(view);

                }
                return false;
            }
        });
    }

    /**
     button.setOnTouchListener(new View.OnTouchListener() )
     {
     public boolean onTouch(View v, MotionEvent event)
     {
     if (event.getAction() == MotionEvent.ACTION_DOWN)
     {
     sendUp(v);
     }
     }
     };
     */

    protected void displayMessage(String messageType, String aMessage) {
        String newLine = "\n";

        final StringBuilder textBuilder = new StringBuilder()
                .append(messageType)
                .append(newLine)
                .append(aMessage)
                .append(newLine).append(newLine)
                .append(messagesText.getText().toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesText.setText(textBuilder.toString());
            }
        });
    }

    // might delete
//    protected void addData() {
//
//        TableLayout tl = findViewById(R.id.table);
//        for (int i = 0; i < 4 ; i++) {
//            TableRow tr = new TableRow(this);
//            tr.setLayoutParams(getLayoutParams());
//            tr.addView(getTextView(i + 1, companies[i], Color.WHITE, Typeface.NORMAL));
//            tr.addView(getTextView(i + numCompanies, os[i], Color.WHITE, Typeface.NORMAL));
//            tl.addView(tr, getTblLayoutParams());
//        }
//    }

    public void addHeaders(JsonObject message) {
        String humidity = message.get("humidity").getAsString();
        String temp = message.get("tempF").getAsString();
        String gas = message.get("MQ7").getAsString();
        String mq3 = message.get("MQ3").getAsString();

        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        // Sensors
        tr.addView(getTextView(0, "HUMIDITY", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(1, "humidity", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tl.addView(tr, getTblLayoutParams());

        tr.addView(getTextView(0, "TEMPERATURE", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(1, "temp", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tl.addView(tr, getTblLayoutParams());

//        tr.addView(getTextView(0, "GAS", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(1, gas, Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tl.addView(tr, getTblLayoutParams());
//
//        tr.addView(getTextView(0, "MQ3", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(1, mq3, Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tl.addView(tr, getTblLayoutParams());

        // Data
//        tl.addView(tr, getTblLayoutParams());
    }

}
