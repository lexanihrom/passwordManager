package com.example.fingerprintsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mButtonCreate;
    private Button mButtonOpenFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonCreate = (Button) findViewById(R.id.buttonCreate);
        mButtonOpenFile = (Button) findViewById(R.id.buttonOpenFile);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLogin();
            }
        });
        mButtonOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
    }

    private void createLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void openFile() {
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("application/*");
        startActivityForResult(Intent.createChooser(intent1, "Select file"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String name = data.getData().getPath();
        Intent intent = new Intent(this, OpenActivity.class);
        intent.putExtra("fname", name);
        startActivity(intent);
    }
}
