package com.example.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PageOneFragment extends Fragment {

    private Button btnPickDate, btnSelectImage, btnStartSpeech, btnStopSpeech;
    private EditText checkerEditText, contractorEditText, presentEditText;
    private ImageView imageView;
    private ImageButton btnDeleteImage;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Void> captureImageLauncher;

    // --- דיבור ---
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private StringBuilder buffer1 = new StringBuilder();
    private StringBuilder buffer2 = new StringBuilder();
    private StringBuilder buffer3 = new StringBuilder();
    private int currentTarget = 0; // 0=אף אחד, 1=בודק, 2=קבלן, 3=נוכח
    private boolean updateMode = false; // אם אמר "שנה", אז נשנה במקום להוסיף
    private boolean awaitingDate = false; // אם אמר "תאריך" מחכה להזנת תאריך

    private static final int REQUEST_RECORD_AUDIO = 1;

    // --- מפה של חודשים בעברית ---
    private final Map<String, Integer> monthMap = new HashMap<String, Integer>(){{
        put("ינואר",1); put("פברואר",2); put("מרץ",3); put("אפריל",4);
        put("מאי",5); put("יוני",6); put("יולי",7); put("אוגוסט",8);
        put("ספטמבר",9); put("אוקטובר",10); put("נובמבר",11); put("דצמבר",12);
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_page_one, container, false);

        // --- שדות ---
        btnPickDate = root.findViewById(R.id.btnPickDate);
        btnSelectImage = root.findViewById(R.id.btnSelectImage);
        checkerEditText = root.findViewById(R.id.checkerEditText);
        contractorEditText = root.findViewById(R.id.contractorEditText);
        presentEditText = root.findViewById(R.id.presentEditText);
        imageView = root.findViewById(R.id.imageView);
        btnDeleteImage = root.findViewById(R.id.btnDeleteImage);
        btnStartSpeech = root.findViewById(R.id.btnStartSpeech);
        btnStopSpeech = root.findViewById(R.id.btnStopSpeech);

        // --- ActivityResultLauncher ---
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                (ActivityResultCallback<Uri>) result -> {
                    if(result != null){
                        imageView.setImageURI(result);
                        btnDeleteImage.setVisibility(View.VISIBLE);
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                (ActivityResultCallback<Bitmap>) result -> {
                    if(result != null){
                        imageView.setImageBitmap(result);
                        btnDeleteImage.setVisibility(View.VISIBLE);
                    }
                });

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnSelectImage.setOnClickListener(v -> showImageOptions());
        btnDeleteImage.setOnClickListener(v -> {
            imageView.setImageDrawable(null);
            btnDeleteImage.setVisibility(View.GONE);
        });

        // --- דיבור ---
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he-IL");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onError(int error) {
                Toast.makeText(getContext(), "שגיאה בהקלטה: " + error, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null && !matches.isEmpty()) handleSpokenText(matches.get(0));
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> partial = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(partial != null && !partial.isEmpty()) handleSpokenText(partial.get(0));
            }
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        btnStartSpeech.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
                return;
            }
            currentTarget = 0;
            updateMode = false;
            awaitingDate = false;
            speechRecognizer.startListening(speechRecognizerIntent);
            Toast.makeText(getContext(),"הקלטה התחילה",Toast.LENGTH_SHORT).show();
        });

        btnStopSpeech.setOnClickListener(v -> {
            speechRecognizer.stopListening();
            Toast.makeText(getContext(),"הקלטה הופסקה",Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    private void handleSpokenText(String spokenText){
        String lower = spokenText.toLowerCase();

        // --- עצור ---
        if(lower.contains("עצור")){
            currentTarget = 0;
            awaitingDate = false;
            return;
        }

        // --- שינוי או הוספה ---
        if(lower.contains("שנה")) updateMode = true;
        if(lower.contains("הוסף")) updateMode = false;

        // --- תאריך: מחכים למילת "תאריך" ---
        if(lower.contains("תאריך")){
            awaitingDate = true;
            return;
        }

        if(awaitingDate){
            for(Map.Entry<String,Integer> entry : monthMap.entrySet()){
                if(lower.contains(entry.getKey())){
                    String[] parts = lower.split("\\s+");
                    int day = -1, month = entry.getValue(), year = -1;
                    for(int i=0;i<parts.length;i++){
                        if(parts[i].equals(entry.getKey())){
                            if(i>0) try { day = Integer.parseInt(parts[i-1]); } catch (Exception ignored){}
                            if(i<parts.length-1) try { year = Integer.parseInt(parts[i+1]); } catch (Exception ignored){}
                            break;
                        }
                    }
                    if(day>0 && year>0){
                        btnPickDate.setText(day+"/"+month+"/"+year);
                        awaitingDate = false;
                        return;
                    }
                }
            }
        }

        // --- מילים לפי שדה ---
        String[] words = spokenText.split("\\s+");
        for(String word : words){
            String w = word.toLowerCase().replace(" ","");
            if(w.equals("בודק")) currentTarget = 1;
            else if(w.equals("קבלן")) currentTarget = 2;
            else if(w.equals("נוכח")) currentTarget = 3;
            else if(w.equals("עצור")) currentTarget = 0;
            else {
                if(currentTarget == 1){
                    if(updateMode) buffer1.setLength(0);
                    buffer1.append(word).append(" ");
                    checkerEditText.setText(buffer1.toString());
                } else if(currentTarget == 2){
                    if(updateMode) buffer2.setLength(0);
                    buffer2.append(word).append(" ");
                    contractorEditText.setText(buffer2.toString());
                } else if(currentTarget == 3){
                    if(updateMode) buffer3.setLength(0);
                    buffer3.append(word).append(" ");
                    presentEditText.setText(buffer3.toString());
                }
            }
        }
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, y, m, d) -> btnPickDate.setText(d + "/" + (m + 1) + "/" + y),
                year, month, day);

        dialog.show();
    }

    private void showImageOptions() {
        String[] options = {"בחר מהגלריה", "צלם תמונה"};
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("בחר תמונה")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) pickImageLauncher.launch("image/*");
                    else captureImageLauncher.launch(null);
                })
                .show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(speechRecognizer != null) speechRecognizer.destroy();
    }
}
