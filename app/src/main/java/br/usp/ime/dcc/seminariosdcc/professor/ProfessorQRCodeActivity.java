package br.usp.ime.dcc.seminariosdcc.professor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import br.usp.ime.dcc.seminariosdcc.R;

public class ProfessorQRCodeActivity extends AppCompatActivity {
    public String seminarId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_qrcode);
        seminarId = getIntent().getStringExtra("seminar_id");
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Bitmap bitmap = generateQRBitmap(seminarId, size.x, size.x);
        imageView.setImageBitmap(bitmap);
    }


    // Inspired from answers to this question at Stack Overflow:
    // http://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    public static Bitmap generateQRBitmap(String content, int width, int height) {
        Bitmap bitmap = null;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
