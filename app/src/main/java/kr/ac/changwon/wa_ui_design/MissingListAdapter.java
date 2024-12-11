package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MissingListAdapter extends ArrayAdapter<MissingData> {

    private Context context;
    private List<MissingData> missingDataList;

    public MissingListAdapter(Context context, List<MissingData> data) {
        super(context, R.layout.missing_list, data);
        this.context = context;
        this.missingDataList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.missing_list, parent, false);
        }

        MissingData data = missingDataList.get(position);

        ImageView photo = convertView.findViewById(R.id.missing_list_photo);
        TextView name = convertView.findViewById(R.id.missing_list_name);
        TextView species = convertView.findViewById(R.id.missing_list_species);
        TextView date = convertView.findViewById(R.id.missing_list_date);

        if (data.getPhotoUri() != null) {
            photo.setImageURI(Uri.parse(data.getPhotoUri()));
        }
        name.setText(data.getName());
        species.setText(data.getSpecies());
        date.setText(data.getDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MissingPageActivity.class);
                intent.putExtra("missingData", data);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


}
