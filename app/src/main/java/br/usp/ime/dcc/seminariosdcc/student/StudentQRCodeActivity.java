package br.usp.ime.dcc.seminariosdcc.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.usp.ime.dcc.seminariosdcc.R;

public class StudentQRCodeActivity extends AppCompatActivity implements View.OnClickListener {

        private Button scanBtn;
        private TextView tvScanFormat, tvScanContent;
        private LinearLayout llSearch;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_qrcode);
            scanBtn = (Button) findViewById(R.id.scan_button);
            tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
            tvScanContent = (TextView) findViewById(R.id.tvScanContent);
            llSearch = (LinearLayout) findViewById(R.id.llSearch);
            scanBtn.setOnClickListener(this);
        }

        public void onClick(View v) {
            llSearch.setVisibility(View.GONE);
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a QRcode");
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.initiateScan();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {
                if (result.getContents() == null) {
                    llSearch.setVisibility(View.GONE);
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    llSearch.setVisibility(View.VISIBLE);
                    tvScanContent.setText(result.getContents());
                    tvScanFormat.setText(result.getFormatName());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
}