package friendlyitsolution.com.cit;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

public class profile extends AppCompatActivity {

    CircleImageView iv;
    TextView edit;
    Uri imguri = null;
    ProgressDialog pd;
    MaterialEditText et;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.super.onBackPressed();
            }
        });


        pd = new ProgressDialog(profile.this);
        pd.setCancelable(false);
        pd.setMessage("please wait");
        btn = (Button) findViewById(R.id.btn);
        iv = (CircleImageView) findViewById(R.id.profile_image);
        edit = (TextView) findViewById(R.id.edit);
        edit.setVisibility(View.GONE);
        et = (MaterialEditText) findViewById(R.id.etname);
        setprofile();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(profile.this)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {

                                // Toast.makeText(getApplicationContext(),"get : "+uri,Toast.LENGTH_LONG).show();
                                if (uri != null) {
                                  //  imguri = uri;
                                  //  iv.setImageURI(uri);
                                  //  edit.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(getSupportFragmentManager());

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(imguri);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().toString().equals("")) {
                    et.setError("please enter this");
                } else {
                    MyApp.ref.child("users").child(MyApp.mynumber).child("name").setValue(et.getText().toString());
                }
            }
        });
    }

    private void uploadImage(final Uri uri) {
        //if there is a file to upload

        if (uri != null) {
            //  bnp.setVisibility(View.VISIBLE);
            pd.show();
            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            StorageReference mStorageRef = mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference riversRef = mStorageRef.child("img/" + MyApp.mynumber + "/" + MyApp.mynumber + ".jpg");

            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUri = uri + "";

                                    pd.dismiss();
                                    edit.setVisibility(View.GONE);
                                    MyApp.ref.child("users").child(MyApp.mynumber).child("imgurl").setValue("" + downloadUri);


                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();

                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return riversRef.getDownloadUrl();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Try again ..", Toast.LENGTH_LONG).show();

        }
    }


    void setprofile() {
        try {

            if (!MyApp.userdata.get("imgurl").toString().equals(" ")) {


                Glide.with(iv.getContext()).load(MyApp.userdata.get("imgurl").toString())
                        .override(100, 100)
                        .fitCenter()
                        .into(iv);
            }
            et.setText(MyApp.userdata.get("name") + "");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong please reopen app", Toast.LENGTH_LONG).show();
        }

    }
}