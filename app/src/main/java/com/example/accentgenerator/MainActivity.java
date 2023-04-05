package com.example.accentgenerator;
import androidx.appcompat.app.AppCompatActivity;
import android.speech.tts.UtteranceProgressListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import com.google.android.material.textfield.TextInputEditText;
public class MainActivity extends AppCompatActivity {

    private TextInputEditText mEditText;
    private TextToSpeech mTTS;
    private Button button;
    public Locale l;
    private Spinner age;
    float speed = (float)(1.5);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spi = findViewById(R.id.sp);
        age= findViewById(R.id.ages);
        ArrayList<String> lan = new ArrayList<>();
        lan.add("UK");
        lan.add("US");
        lan.add("Telugu");
        lan.add("Hindi");
        lan.add("Japanese");
        lan.add("Spanish");
        lan.add("Russian");
        lan.add("Chinese");
        lan.add("German");
        lan.add("French");

        ArrayList<Pair<String,Locale>> items = new ArrayList<Pair<String,Locale>>();
        items.add(new Pair<>("UK",Locale.UK));
        items.add(new Pair<>("US",Locale.US));
        items.add(new Pair<>("Telugu",Locale.forLanguageTag("te")));
        items.add(new Pair<>("Hindi",Locale.forLanguageTag("hi")));
        items.add(new Pair<>("Japanese",Locale.JAPANESE));
        items.add(new Pair<>("Spanish",Locale.forLanguageTag("es")));
        items.add(new Pair<>("Russian",Locale.forLanguageTag("ru")));
        items.add(new Pair<>("Chinese",Locale.CHINESE));
        items.add(new Pair<>("German",Locale.GERMAN));
        items.add(new Pair<>("French",Locale.FRENCH));
        items.add(new Pair<>("English",Locale.JAPAN));

        ArrayList<String> ag=new ArrayList<>();
        ag.add("Young");
        ag.add("Adult");
        ag.add("old");

        ArrayAdapter<String> ageadapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ag);
        age.setAdapter(ageadapter);
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    speed=(float)(1.4);
                }
                if(position==1){
                    speed=(float)(1.1);
                }
                if(position==2){
                    speed=(float)(0.8);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lan);
        spi.setAdapter(adapter);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l=items.get((position)).second;
                System.out.println("Gender and voices "+mTTS.getVoices()+"\n");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mTTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(Locale.UK);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "Language not supported");
                        Toast.makeText(MainActivity.this, "Not supportede language", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        button.setEnabled(true);
                    }
                }else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });



        button = findViewById(R.id.button_speak);
        mEditText = findViewById(R.id.edit_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.setLanguage(l);
                speak();
            }
        });
    }


    private Locale mCurrentLanguage;
    private void speak() {
        String text = mEditText.getText().toString();
        float pitch = 1.0f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        // Set the language to Telugu
        Locale l = new Locale("te", "IN");
        mTTS.setLanguage(l);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }

        // Get the language of the TTS object
        Locale currentLanguage = mTTS.getLanguage();

        // Check if the language has changed
        if (!currentLanguage.equals(mCurrentLanguage)) {
            Log.d("TTS", "Language changed to: " + currentLanguage.getDisplayName());
            mCurrentLanguage = currentLanguage;
        }
    }







    @Override
    protected void onDestroy() {
        if (mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}