package com.example.mymusicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class SongListActivity extends AppCompatActivity {
    String[]items;
    ListView listView;
    ArrayList<File> arrayList1;

    Toolbar toolbar;
    int colorStatus;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextView header;
    View headerview;
    View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        toolbar=findViewById (R.id.toolbarId);
        drawerLayout=findViewById (R.id.drawerLayoutId);

        if (toolbar!=null){
            setSupportActionBar (toolbar);
        }


        listView=findViewById(R.id.listView);
        arrayList1=new ArrayList<>();
        runTimePermission();



        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(
                SongListActivity.this,drawerLayout,toolbar,R.string.open,R.string.closed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText (SongListActivity.this, "Open", Toast.LENGTH_SHORT).show ();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText (SongListActivity.this, "Closed", Toast.LENGTH_SHORT).show ();

            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);


    }

    public void runTimePermission(){
        Dexter.withContext(SongListActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    ArrayList<File> findSong(File file){
        ArrayList<File>  arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for (File singleFile:files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }else {
                if (singleFile.getName().endsWith(".mp3")  || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }

        arrayList1.addAll(arrayList);
        return arrayList;
    }
    void displaySong(){
        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        items=new String[mySongs.size()];
        if (mySongs.size()>0){
            for (int i=0;i<mySongs.size();i++){
                items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            }
        }

        SongListActivity.CustomAdapter customAdapter=new SongListActivity.CustomAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String songName= (String) listView.getItemAtPosition(position);

                startActivity(new Intent(SongListActivity.this,MainActivity.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songName",songName)
                        .putExtra("pos",position));

            }
        });
    }
    class CustomAdapter extends BaseAdapter {

        ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView songNameTextView=myView.findViewById(R.id.songNameTextViewId);
            TextView durationTextView=myView.findViewById(R.id.songDurationTextViewId);
            ImageView img=myView.findViewById(R.id.img);
            songNameTextView.setSelected(true);
            songNameTextView.setText(items[position]);


            Uri uri=Uri.parse(mySongs.get(position).toString());

            mmr.setDataSource(SongListActivity.this, uri);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String title =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
             mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String image =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE);

           Bitmap bm = StringToBitMap(image);
            if (bm!=null){
                img.setImageBitmap(bm);
            //MyPhoto is image control
            }



            int millSecond = Integer.parseInt(duration);
            durationTextView.setSelected(true);
            durationTextView.setText(title +"\t"+createTime(millSecond));

            return myView ;
        }
    }
    //time conversion int value to time format
    public String createTime(int duration) {
        String time="";
        int dur = (int) duration;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            time = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            time = String.format("%02d:%02d", mns, scs);
        }
        return time;
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}