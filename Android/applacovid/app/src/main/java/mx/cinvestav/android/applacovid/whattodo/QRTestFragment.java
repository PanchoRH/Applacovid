package mx.cinvestav.android.applacovid.whattodo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.dpppt.android.sdk.backend.models.ExposeeAuthMethodJson;
import org.dpppt.android.sdk.internal.backend.models.ExposeeRequest;
import org.dpppt.android.sdk.internal.crypto.CryptoModule;
import org.dpppt.android.sdk.internal.util.DayDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import mx.cinvestav.android.applacovid.R;

import static android.content.Context.MODE_PRIVATE;
import static mx.cinvestav.android.applacovid.MainActivity.MY_PREFS_FILE_NAME;
import static mx.cinvestav.android.applacovid.MainActivity.UUID_FIELD_NAME;

/*
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
*/

public class QRTestFragment extends Fragment {

    private String uuidApp;
    private ExposeeRequest exposeeRequest;

    public static QRTestFragment newInstance() {
        return new QRTestFragment();
    }

    public QRTestFragment() {
        super( R.layout.fragment_qr);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.wtd_test_toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        Log.d("QR-SHARE", "QR-content = " + generateContent());

        Bitmap qr_code = generateQR(generateContent());

        ((ImageView) view.findViewById( R.id.qr_image )).setImageBitmap(qr_code);
        view.findViewById(R.id.qr_share_button).setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, saveImage(qr_code));
            startActivity(Intent.createChooser(intent , "Share"));
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // (gusorh) Code for supporting visited places tracking. Backend receives a unique identifier (app instance level)
        if(uuidApp == null) {
            SharedPreferences prefs = context.getSharedPreferences( MY_PREFS_FILE_NAME, MODE_PRIVATE );
            if (prefs.getString( UUID_FIELD_NAME, "" ).length() == 0) {
                uuidApp= UUID.randomUUID().toString();
                SharedPreferences.Editor editor = context.getSharedPreferences( MY_PREFS_FILE_NAME, MODE_PRIVATE ).edit();
                editor.putString( UUID_FIELD_NAME,  uuidApp);
                editor.apply();
                editor.commit();
            }else{
                uuidApp = prefs.getString( UUID_FIELD_NAME, "" );
            }
        }
        if(exposeeRequest==null)
            exposeeRequest = CryptoModule.getInstance(context).getSecretKeyForPublishing( new DayDate( Calendar.getInstance().getTime().getTime()), new ExposeeAuthMethodJson("")); // Value = "" because not auth method is used. It corresponds to the covid code supposedly provided by health authorities

    }


    private static Bitmap generateQR(String content) {

        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType,  Object> hints;
        hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, 250, 250, hints);

        } catch (WriterException e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(matrix.getWidth(), matrix.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }


        return bitmap;

    }


    private String generateContent(){
        String key = "";
        String date = "";
        String uuid = uuidApp;
        try { // Just in case
            key = exposeeRequest.getKey();
            date = exposeeRequest.getKeyDate() + "";
            uuid = uuidApp;
        }catch(Exception ex){
        }
        return "https://pakal.cs.cinvestav.mx/admin/console/newrecord?key=" + key + "&dateKey=" + date + "&uuid=" + uuid;
    }


    private Uri saveImage(Bitmap image) {
        //TODO - Should be processed in another thread
        File imagesFolder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(getContext(), "mx.cinvestav.android.applacovid.fileprovider", file);

        } catch (IOException e) {
            Log.d("QR-SHARE", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }



}
