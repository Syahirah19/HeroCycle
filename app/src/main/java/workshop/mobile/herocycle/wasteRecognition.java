package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

import workshop.mobile.herocycle.ml.Model;
import workshop.mobile.herocycle.model.Collection;

public class wasteRecognition extends AppCompatActivity {

    Button takePicture;
    ImageView imgWaste;
    TextView result;
    String uid;
    String itemUID;
    String currentPhotoPath;

    private Uri mImageUri;
    int imageSize = 224;
    Bitmap image;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage;

    Collection collection = new Collection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste_recognition);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        uid = prefs.getString("uid","");

        storage = FirebaseStorage.getInstance().getReference("Collections/");
        result = findViewById(R.id.result);
        imgWaste = findViewById(R.id.imgWaste);
        takePicture = findViewById(R.id.takePicture);

        takePicture.setOnClickListener(view -> {
            // Launch camera if we have permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchIntent();
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException exception) {
                Log.e("Error",exception.toString());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(this,
                        "workshop.mobile.herocycle.file-provider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(cameraIntent, 1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),mImageUri);
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imgWaste.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int [imageSize * imageSize];
            image.getPixels(intValues, 0 , image.getWidth(), 0 , 0 , image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++){
                if (confidence[i] > maxConfidence){
                    maxConfidence = confidence[i];
                    maxPos = i;

                }
            }
            String[] classes = {"Plastic", "Glass", "Paper", "Others"};

            result.setText(classes[maxPos]);

            uploadToDatabase(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch ( IOException e) {
            // TODO Handle the exception
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadToDatabase(String aClass) {

        db.collection("Item").whereEqualTo("itemName", aClass).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()){
                    itemUID = document.getId();
                }
            }
        });

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        StorageReference fileReference = storage.child(uid+"_"+timeStamp +"."+getFileExtension(mImageUri));

        // Upload commands
        StorageTask<UploadTask.TaskSnapshot> mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                    collection.setClcDate("");
                    collection.setClcDesc("");
                    collection.setClcPrice("");
                    collection.setClcStatus("Requested");
                    collection.setItemID(itemUID);
                    collection.setItemImage(task.getResult().toString());
                    collection.setReqDate("");
                    collection.setUserID(uid);
                    collection.setWeight("");

                    db.collection("Collection").add(collection).addOnCompleteListener(task1 -> {
                        Intent intent = new Intent(wasteRecognition.this, LocationDelivery.class);
                        intent.putExtra("clcID",task1.getResult().getId());

                        startActivity(intent);
                        finish();
                    });
                }));
    }
}