package com.dpk.hammoq.Fragments.cameraUtils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.dpk.hammoq.R;

import java.util.List;

public class CamerasSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    Activity context;
    List<Camera> cameras;
    public CamerasSpinnerAdapter(Activity context, List<Camera> cameras) {
        this.context = context;
        this.cameras = cameras;
    }
    @Override
    public int getCount() {
        return cameras.size();
    }

    @Override
    public String getItem(int pos) {
        return cameras.get(pos).getName();
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return obtainView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return obtainView(position, parent);
    }
    public Camera obtainCamera(int position){
        return cameras.get(position);
    }
    private View obtainView(int position, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_spiner_cameras, parent, false);
        TextView text = row.findViewById(R.id.cameraName);
        text.setText(getItem(position));
        return text;
    }
    public int obtainPositionOnAdapter(String cameraId){
        int count = 0;
        for (Camera c: cameras) {
            if(cameraId.equals(c.getId())){
                return count;
            }
            count++;
        }
        return -1;
    }
}
