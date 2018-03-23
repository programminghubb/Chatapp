package com.programminghub.simplechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    EditText emailEt,passwordEt;
    Button signUp;
    String email,password;
    FirebaseAuth auth;
    ImageView showUserProfile;
    private final Integer PICK_IMAGE_REQUEST=1;
    Bitmap bitmap;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        defineView();
        addCLicklistener();
    }

    private void defineView(){
        emailEt=findViewById(R.id.email_et);
        passwordEt=findViewById(R.id.password_et);
        signUp=findViewById(R.id.signup_btn);
        showUserProfile=findViewById(R.id.show_user_profile);

    }
    private void initView(){
        auth=FirebaseAuth.getInstance();

    }

    private boolean validate(){
        boolean isValid=false;
        email=emailEt.getText().toString();
        password=passwordEt.getText().toString();
        if(TextUtils.isEmpty(email))
            emailEt.setError("Required");
        else if(TextUtils.isEmpty(password))
            passwordEt.setError("Required");
        else if(uri==null)
            Toast.makeText(this, "Please select the image", Toast.LENGTH_SHORT).show();
        else
            isValid=true;
        return isValid;
    }
    private void addCLicklistener(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                    registerUserToDatabse();
            }
        });
        showUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                    // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                    // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

    }
    private void registerUserToDatabse(){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(SignupActivity.this, "succesfully created user::email is:"+ task.getResult().getUser().getEmail(), Toast.LENGTH_SHORT).show();
                addUserInDatabse(task.getResult().getUser());
            }
        });


    }

    private void addUserInDatabse(final FirebaseUser firebaseUser){

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] data = bytes.toByteArray();

        FirebaseStorage.getInstance().getReference().child("simpleChat").child(firebaseUser.getUid())
                .child("profilePic")
                .putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url=taskSnapshot.getDownloadUrl().toString();
                        User user=new User(firebaseUser.getEmail(),firebaseUser.getUid(),url);
                        FirebaseDatabase.getInstance().getReference().child("simpleChat").child("users")
                                .child(firebaseUser.getUid()).setValue(user);

                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                Toast.makeText(this, "hey you selected image" + bitmap, Toast.LENGTH_SHORT).show();
                showUserProfile.setImageBitmap(bitmap);
                //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
