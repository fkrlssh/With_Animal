package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MissingActivity extends AppCompatActivity {
    private List<MissingData> missingDataList;
    private MissingListAdapter missingListAdapter;
    private ListView missingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing);

        missingDataList = new ArrayList<>();
        missingListAdapter = new MissingListAdapter(this, missingDataList);

        missingListView = findViewById(R.id.missing_listview);
        missingListView.setAdapter(missingListAdapter);

        missingDataList = new ArrayList<>();
        missingListAdapter = new MissingListAdapter(this, missingDataList);

        missingListView = findViewById(R.id.missing_listview);
        missingListView.setAdapter(missingListAdapter);

        missingListView.setOnItemClickListener((parent, view, position, id) -> {
            MissingData selectedData = missingDataList.get(position);

            Intent intent = new Intent(MissingActivity.this, MissingPageActivity.class);
            intent.putExtra("missingData", selectedData);
            startActivity(intent);
        });

        ImageButton missingReturnHome = findViewById(R.id.missing_return_home);
        missingReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 등록버튼
        Button missingWriteButton= findViewById(R.id.missing_writeButton);
        missingWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MissingActivity.this, MissingWriteActivity.class);
                startActivityForResult(intent, 1); // 요청 코드 1
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            MissingData newData = (MissingData) data.getSerializableExtra("newData");
            if (newData != null) {
                missingDataList.add(0, newData);
                missingListAdapter.notifyDataSetChanged();
            }
        }
    }


}
