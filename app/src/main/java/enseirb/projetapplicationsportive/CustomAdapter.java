/*package enseirb.projetapplicationsportive;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
    private static int ITEM_LAYOUT = R.layout.activity_login;
    private Context context;
    private String[] users;

    public CustomAdapter(Context context, String[] users){
        super(context, ITEM_LAYOUT);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(ITEM_LAYOUT, null);
        }

        String currentUser = users[position];

        TextView itemLabel = convertView.findViewById(R.id.user);
        itemLabel.setText(currentUser);

        return convertView;
    }
}*/