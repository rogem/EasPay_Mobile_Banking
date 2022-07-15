package com.example.prototype10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FacultySignUp extends AppCompatActivity {
    FirebaseAuth mAuth;

    EditText Fname,Lname,gender,age,emplyeenum,ConNumber,email,password,balance,userstatus;
    CheckBox TermANDCondition;
    private TextView button;
    ImageView uploadimage;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_sign_up);

        button = findViewById(R.id.btn_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


       mAuth = FirebaseAuth.getInstance();
        Fname =(EditText) findViewById(R.id.EdtTxtFacultyFname);
        Lname =(EditText) findViewById(R.id.EdtTxtFacultyLname);
        gender =(EditText) findViewById(R.id.EdtTxtFacultyGender);
        age =(EditText) findViewById(R.id.EdtTxtFacultyAge);
        emplyeenum =(EditText) findViewById(R.id.EdtTxtFacultyEmploeeNumber);
        ConNumber =(EditText) findViewById(R.id.EdtTxtFacultyContactNumber);
        email =(EditText) findViewById(R.id.EdtTxFacultyEmail);
        password =(EditText) findViewById(R.id.EdtTxFacultyPassword);
        balance =(EditText) findViewById(R.id.Balance);
        userstatus = (EditText) findViewById(R.id.UserStatus);

        TermANDCondition = (CheckBox) findViewById(R.id.CheckBoxFacultyTermAndCondition);

        uploadimage =(ImageView) findViewById(R.id.ImageViewUpload);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });
    }
    public void openDialog(){
    ExampleDialog exampleDialog = new ExampleDialog();
    exampleDialog.show(getSupportFragmentManager(),"Example Dialog");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            uploadimage.setImageURI(imageUri);
        }
    }

    public void signup(View view) {
        String FirstName =Fname.getText().toString().trim();
        String LastName =Lname.getText().toString().trim();
        String Gender =gender.getText().toString().trim();
        String Age =age.getText().toString().trim();
        String EmployeeNumber =emplyeenum.getText().toString().trim();
        String ContactNumber =ConNumber.getText().toString().trim();
        String Email =email.getText().toString().trim();
        String Password =password.getText().toString().trim();
        String Balance =balance.getText().toString().trim();
        String AUserStatus =userstatus.getText().toString().trim();

        Bundle bundle = new Bundle();
        bundle.putString("Email", Email);

        if(Balance.isEmpty()){
            return;
        }
        if(FirstName.isEmpty()){
            Fname.setError("FirstName is required");
            Fname.requestFocus();
            return;
        }
        if(LastName.isEmpty()){
            Lname.setError("LastName is required");
            Lname.requestFocus();
            return;
        }
        if(Gender.isEmpty()){
            gender.setError("Gender is required");
            gender.requestFocus();
            return;
        }
        if(Age.isEmpty()){
            age.setError("Age is required");
            age.requestFocus();
            return;
        }if(EmployeeNumber.isEmpty()){
            emplyeenum.setError("Employee Number is required");
            emplyeenum.requestFocus();
            return;
        }if(ContactNumber.isEmpty()){
            ConNumber.setError("Contact Number is required");
            ConNumber.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Please valid email");
            email.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(Password.length() < 8){
            password.setError("Min password length should be 8 character");
            password.requestFocus();
            return;
        }
        boolean ischeck = TermANDCondition.isChecked();
        if(ischeck){
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User facultytuser = new User(FirstName,LastName,Gender,Age,EmployeeNumber,ContactNumber,Email,Password,Balance,AUserStatus);
                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirstName)
//                                FirebaseAuth.getInstance().getCurrentUser().getUid()
                                        .setValue(facultytuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (imageUri != null){
                                                    uploadToFirebase(imageUri);

                                                }else {
                                                    Toast.makeText(FacultySignUp.this,"Please select Image",Toast.LENGTH_SHORT).show();
                                                }

                                                if (task.isSuccessful()){

//                                                    long points = 0;
//                                                    points++;
//                                                    String userpoint = String.valueOf(points);
                                                    String userpoint = "0";
                                                    DatabaseReference referencePoints = FirebaseDatabase.getInstance().getReference("Points").child(FirstName);
                                                    PointsModal pointsModal = new PointsModal(userpoint);
                                                    referencePoints.setValue(pointsModal);

                                                    Toast.makeText(FacultySignUp.this,"Student User has been registered successfully",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
//                                                    startActivity(new Intent(FacultySignUp.this,HomeScreen.class));
                                                }else {
                                                    Toast.makeText(FacultySignUp.this,"Failed to register! try again!",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }else {
                                Toast.makeText(FacultySignUp.this,"Failed to register!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else {
//            Toast.makeText(this, "Please agree to our term and condition", Toast.LENGTH_LONG).show();
            Toast.makeText(FacultySignUp.this,"Please agree to our term and condition",Toast.LENGTH_LONG).show();
        }

    }

    private void uploadToFirebase(Uri uri){
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString());
                        String modelId = root.push().getKey();
                        Toast.makeText(FacultySignUp.this,"uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FacultySignUp.this,"uploading Failed!!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void btnupload(View view) {
        Toast.makeText(this, "Upload Clicked", Toast.LENGTH_SHORT).show();
    }

    public void TermsandCondition(View view) {

    }
}