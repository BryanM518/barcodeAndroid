package com.example.barcodescannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;

import cafsoft.colombianidinfo.ColombianIDInfo;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> actResLauncherBarcodeScan;
    private Button btnScan = null;
    private EditText Id = null;
    private EditText Type = null;
    private EditText FirstName = null;
    private EditText OthersName = null;
    private EditText FirstFamilyName = null;
    private EditText SecondFamilyName = null;
    private EditText Birthday = null;
    private EditText Gender = null;
    private EditText Rh = null;


    private void initViews() {

        btnScan = findViewById(R.id.btnScan);
        Id = findViewById(R.id.editId);
        Type = findViewById(R.id.editType);
        FirstName = findViewById(R.id.editFirstName);
        OthersName = findViewById(R.id.editOtherNames);
        FirstFamilyName = findViewById(R.id.editFirtsFamilyName);
        SecondFamilyName = findViewById(R.id.editSecondFamilyName);
        Birthday = findViewById(R.id.editBirthday);
        Gender = findViewById(R.id.editGender);
        Rh = findViewById(R.id.editRh);

    }

    private void initEvents() {
        btnScan.setOnClickListener(view -> {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);

            actResLauncherBarcodeScan.launch(intent);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();

        ActivityResultContracts.StartActivityForResult actForRes = null;
        actForRes = new ActivityResultContracts.StartActivityForResult();
        actResLauncherBarcodeScan = registerForActivityResult(actForRes, result -> {
            if (result.getResultCode() == RESULT_OK) {
                assert result.getData() != null;
                String data = result.getData().getStringExtra("BarcodeData");
                int format = result.getData().getIntExtra("BarcodeFormat", -1);

                if (format == Barcode.FORMAT_PDF417) {
                    try {
                        ColombianIDInfo card = ColombianIDInfo.decode(data);

                        Type.setText(card.getDocumentType().name().replaceAll("_", " "));

                        Id.setText("" + card.getDocumentNumber());
                        FirstName.setText(card.getFirstName());
                        OthersName.setText(card.getOtherNames());

                        FirstFamilyName.setText(card.getFirstFamilyName());
                        SecondFamilyName.setText(card.getSecondFamilyName());

                        Birthday.setText(card.getDateOfBirth().substring(0,4) + '/' + card.getDateOfBirth().substring(4, 6) + '/' + card.getDateOfBirth().substring(6));

                        //birthplace.setText(card.getBirthplaceCode());

                        Rh.setText(card.getBloodType());

                        if (card.getGender().equals("M")) {
                            Gender.setText("Hombre");
                        } else if (card.getGender().equals("F")) {
                            Gender.setText("Mujer");
                        }
                    } catch (Exception ignored) {
                        System.out.println("No sale");
                    }
                }
            }
        });
    }
}
