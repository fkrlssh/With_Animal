package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MissingPageActivity extends AppCompatActivity {

    Button boardfind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_page);

        Button boardfind = (Button) findViewById(R.id.board_find);
        Button missingPageFindButton = findViewById(R.id.missing_page_findButton);
        boardfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             missingPageFindButton.setEnabled(false);
             missingPageFindButton.setText("발견 완료되었습니다.");
            }
        });

        ImageButton missingpageReturnMissingB = findViewById(R.id.missingpage_return_missingB);
        missingpageReturnMissingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        missingPageFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissingPageActivity.this, MissingFindActivity.class);
                startActivity(intent);
                finish();
            }
        });



        Intent intent = getIntent();
        MissingData data = (MissingData) intent.getSerializableExtra("missingData");

        if (data != null) {
            ((TextView) findViewById(R.id.missing_page_name)).setText(data.getName());
            ((TextView) findViewById(R.id.missing_page_spicies)).setText(data.getSpecies());
            ((TextView) findViewById(R.id.missing_page_age)).setText(String.valueOf(data.getAge()));
            ((TextView) findViewById(R.id.missing_page_gender)).setText(data.getGender());
            ((TextView) findViewById(R.id.missing_page_place)).setText(data.getPlace());
            ((TextView) findViewById(R.id.missing_page_date)).setText(data.getDate());
            ((TextView) findViewById(R.id.missing_page_char)).setText(data.getDescription());

            ImageView photo = findViewById(R.id.missing_page_petphoto);
            if (data.getPhotoUri() != null) {
                photo.setImageURI(Uri.parse(data.getPhotoUri()));
            }
        }
    }
}
