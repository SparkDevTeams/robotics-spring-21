package com.example.robotics_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Arrays;



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
    private String theChannel = "motors"; // channel for motors communication
    private String sensorChannel = "sensors"; // channel for sensors communication
    private String clawChannel = "claw"; // channel for claw communication
    private String pubKey = BuildConfig.PUB_KEY; // Pubnub publish key
    private String subKey = BuildConfig.SUB_KEY; // Pubnub subscribe key


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        messagesText = findViewById(R.id.messages_text);

        // Initialize table with dummy data
        JsonObject message = new JsonObject();
        message.addProperty("humidity", 0);
        message.addProperty("tempF", 0);
        message.addProperty("MQ7", 0);
        message.addProperty("MQ3", 0);
        addValues(message);

        // WebView client that will display camera feed
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://99.177.203.235:8000/index.html");

        // Set font for Action Bar
        actionBarFont();

        // Pubnub setup
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        pnConfiguration.setUuid("theClientUUID");

        pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback()
        {

            @Override
            public void message(PubNub pubnub, PNMessageResult event)
            {
                JsonObject message = event.getMessage().getAsJsonObject();
                runOnUiThread(() -> addValues(message));
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
    }// end onCreate


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
                    }

                }

        );


    }// stop


    @SuppressLint("ClickableViewAccessibility")
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


    @SuppressLint("ClickableViewAccessibility")
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


    @SuppressLint("ClickableViewAccessibility")
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
    }//end sendRight


    @SuppressLint("ClickableViewAccessibility")
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
    }//end sendUp

    @SuppressLint("ClickableViewAccessibility")
    public void clawGrab(View view) {

        Button button = (Button) findViewById(R.id.grab);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Grab", true);

                    pubnub.publish().channel(clawChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>() {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                        }
                                    }
                            );

                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void clawRelease(View view) {

        Button button = (Button) findViewById(R.id.release);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    JsonObject entry = new JsonObject();

                    entry.addProperty("Release", true);

                    pubnub.publish().channel(clawChannel).message(entry).async
                            (
                                    new PNCallback<PNPublishResult>() {
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            if (status.isError()) {
                                                status.getErrorData().getThrowable().printStackTrace();
                                            }
                                        }
                                    }
                            );

                }
                return false;
            }
        });
    }

    protected void displayMessage(String messageType, String aMessage) {
        String newLine = "\n";

        String textBuilder = messageType +
                newLine +
                aMessage +
                newLine + newLine +
                messagesText.getText().toString();
        runOnUiThread(() -> messagesText.setText(textBuilder));
    }


    private TextView getTextView(int id, String title) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(40, 40, 40, 40);
        Typeface face = getResources().getFont(R.font.firacode_regular);
        tv.setTypeface(face);
        tv.setBackgroundColor(Color.rgb(219, 71, 132));
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


    protected void addValues(JsonObject message) {
        String humidity = message.get("humidity").getAsString();
        String temp = message.get("tempF").getAsString();
        String gas = message.get("MQ7").getAsString();
        String mq3 = message.get("MQ3").getAsString();

        String[] sensors = {humidity, temp, gas, mq3};
        String[] titles = {"HUMIDITY", "TEMPERATURE", "GAS", "MQ3"};

        TableLayout tl = findViewById(R.id.table);

        tl.removeAllViews();

        for (int i = 0; i < sensors.length; i++){
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i, titles[i]));
            tr.addView(getTextView(i + sensors.length, sensors[i]));
            tl.addView(tr, getTblLayoutParams());
        }

    }


    protected void actionBarFont(){
        ActionBar ab = getActionBar();
        TextView actionBarTv = new TextView(getApplicationContext());
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, // Width of TextView
                LayoutParams.WRAP_CONTENT);
        actionBarTv.setLayoutParams(lp);
        Typeface face = getResources().getFont(R.font.firacode_regular);
        actionBarTv.setTypeface(face);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(this, "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
