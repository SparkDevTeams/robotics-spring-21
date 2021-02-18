package com.example.robotics_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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


public class MainActivity extends AppCompatActivity
{

    private TextView messagesText;

    private PubNub pubnub;
    private String theChannel = "motors";
    private String sensorChannel = "sensors";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PNConfiguration pnConfiguration = new PNConfiguration();

        pnConfiguration.setPublishKey("pub-c-1e84af4f-2372-4027-940a-d3cb459fdcbe");
        pnConfiguration.setSubscribeKey("sub-c-997c14f8-2a2a-11eb-8e02-129fdf4b0d84");
        pnConfiguration.setUuid("theClientUUID");

        pubnub = new PubNub(pnConfiguration);


        pubnub.addListener(new SubscribeCallback()
        {

            @Override
            public void message(PubNub pubnub, PNMessageResult event)
            {
                JsonObject message = event.getMessage().getAsJsonObject();
                String humidity = message.get("humidity").getAsString();
                String temp = message.get("tempF").getAsString();
                String gas = message.get("MQ7").getAsString();
                String mq3 = message.get("MQ3").getAsString();

//                displayMessage("Humidity: ", humidity);
//                displayMessage("Temperature (F): ", temp);
//                displayMessage("Gas: ", gas);
//                displayMessage("MQ3: ", mq3);
                displayMessage("[Sensors]", "Humidity: " + humidity + "\nTemperature (F): " + temp + "\nGas: " + gas + "\nMQ3: " + mq3);
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

}