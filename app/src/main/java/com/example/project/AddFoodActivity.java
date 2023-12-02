package com.example.project;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.databinding.ActivityAddFoodBinding;
import com.google.android.material.chip.Chip;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity {
    private static final String TAG = "AddFoodActivity";
    private static final int REQUEST_STORAGE_CODE = 100;

    private ActivityAddFoodBinding binding;

    private Uri photouri = null;
    private String date = null;
    private String time = null;
    private String loadmonth = null;
    private Bitmap bitmap;
    private byte[] bytemap;

    private MyObserver observer;
    public interface OnDataInsertListener {
        void onDataInserted();
    }

    private OnDataInsertListener onDataInsertListener;
    public void setOnDataInsertListener(OnDataInsertListener listener) {
        this.onDataInsertListener = listener;
    }


    private final ActivityResultLauncher<Intent> filterActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    photouri = result.getData().getData();
                    try {
                        if (photouri != null) {
                            bitmap = (Build.VERSION.SDK_INT < 28) ?
                                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), photouri) :
                                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), photouri));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                binding.foodImageView.setImageBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), photouri)));
                            }
                            ;
                            bytemap = new BitmapUtils().toByteArray(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (result.getResultCode() == AppCompatActivity.RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("ActivityResult", "something wrong");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        observer = new MyObserver();
        getLifecycle().addObserver(observer);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("음식 추가");
        }

        binding.addressSpinner.setAdapter(ArrayAdapter.createFromResource(
                this,
                R.array.address,
                android.R.layout.simple_list_item_1
        ));

        binding.datebox.setOnClickListener(view -> showDateDialog());

        binding.timebox.setOnClickListener(view -> {
            if (date == null) {
                Toast.makeText(this, "날짜 먼저 입력해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                showTimeDialog();
            }
        });

        binding.foodImageView.setOnClickListener(view -> ImagecheckPermission());

        binding.UploadButton.setOnClickListener(view -> {
            if (bytemap == null) {
                Toast.makeText(this, "음식 사진을 업로드 해주세요", Toast.LENGTH_SHORT).show();
            } else if (date == null) {
                Toast.makeText(this, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (time == null) {
                Toast.makeText(this, "시간을 입력해 주세요", Toast.LENGTH_SHORT).show();
            } else if (binding.typeMeal.getCheckedChipId() == -1) {
                Toast.makeText(this, "조식,중식,석식 중 한가지를 고르세요", Toast.LENGTH_SHORT).show();
            } else if (binding.textFoodInputEditText.getText().toString().equals("")) {
                Toast.makeText(this, "음식을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (binding.foodExplainInputEditText.getText().toString().equals("")) {
                Toast.makeText(this, "음식 설명을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (binding.priceInputEditText.getText().toString().equals("")) {
                Toast.makeText(this, "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                uploadFood();
            }
        });

        initView();
    }

    private void initView() {
        String[] types = {"조식", "중식", "석식", "음료"};
        for (String type : types) {
            binding.typeMeal.addView(createChip(type));
        }
    }

    private void ImagecheckPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadImage();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showPermissionRationalDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    private void showPermissionRationalDialog() {
        new AlertDialog.Builder(this)
                .setMessage("사진을 불러오기 위해서 이미지 접근 권한이 필요합니다")
                .setPositiveButton("허용", (dialog, which) -> ActivityCompat.requestPermissions(
                        AddFoodActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_CODE
                ))
                .setNegativeButton("거부", null)
                .show();
    }

    private void showPermissionDisallowDialog() {
        new AlertDialog.Builder(this)
                .setMessage("사진을 불러오기 위해서 이미지 접근 권한이 필요합니다")
                .setPositiveButton("권한 변경하러 가기", (dialog, which) -> navigagteToAppsetting())
                .setNegativeButton("거부", null)
                .show();
    }

    private void navigagteToAppsetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean storagePermissionGranted = requestCode == REQUEST_STORAGE_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (storagePermissionGranted) {
            loadImage();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionRationalDialog();
            } else {
                showPermissionDisallowDialog();
            }
        }
    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filterActivityLauncher.launch(intent);
    }

    private Chip createChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        return chip;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            Log.d(TAG, binding.addressSpinner.getSelectedItem().toString());
            Log.d(TAG, binding.textFoodInputEditText.getText().toString());
            Log.d(TAG, String.valueOf(binding.typeMeal.getCheckedChipId()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Dialog DatePickerDialog = new Dialog(this);
        DatePickerDialog.setContentView(R.layout.datedialog);
        DatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DatePicker datePicker = DatePickerDialog.findViewById(R.id.fooddatepicker);
        Button dateokButton = DatePickerDialog.findViewById(R.id.dateokButton);

        datePicker.init(currentYear, currentMonth, currentDay, null);
        dateokButton.setOnClickListener(view -> {
            String year = String.valueOf(datePicker.getYear());
            String month = (datePicker.getMonth() < 9) ? "0" + (datePicker.getMonth() + 1) : String.valueOf(datePicker.getMonth() + 1);
            String day = (datePicker.getDayOfMonth() < 10) ? "0" + datePicker.getDayOfMonth() : String.valueOf(datePicker.getDayOfMonth());

            date = year + "년" + month + "월" + day + "일";
            loadmonth = year + "년" + month + "월";
            binding.dateText.setTextColor(Color.BLACK);
            binding.dateText.setText(date);
            DatePickerDialog.dismiss();
        });

        DatePickerDialog.show();
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        Dialog TimePickerDialog = new Dialog(this);
        TimePickerDialog.setContentView(R.layout.timedialog);
        TimePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TimePicker timePicker = TimePickerDialog.findViewById(R.id.foodtimepicker);
        Button timeokButton = TimePickerDialog.findViewById(R.id.timeokButton);

        timePicker.setIs24HourView(true);
        timePicker.setHour(currentHour);
        timePicker.setMinute(currentMinute);

        timeokButton.setOnClickListener(view -> {
            String hour = (timePicker.getHour() < 10) ? "0" + timePicker.getHour() : String.valueOf(timePicker.getHour());
            String min = (timePicker.getMinute() < 10) ? "0" + timePicker.getMinute() : String.valueOf(timePicker.getMinute());

            time = hour + "시" + min + "분";
            binding.timeText.setTextColor(Color.BLACK);
            binding.timeText.setText(time);
            TimePickerDialog.dismiss();
        });

        TimePickerDialog.show();
    }

    private long convertStringToMillis(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
            return format.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long convertStringToMillisMonth(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월", Locale.KOREA);
            return format.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long convertStringToMillis2(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일HH시mm분", Locale.KOREA);
            return format.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void uploadFood() {
        String address = binding.addressSpinner.getSelectedItem().toString();
        String typemeal = "";
        int checkedChipId = binding.typeMeal.getCheckedChipId();
        if (checkedChipId != View.NO_ID) {
            Chip checkedChip = findViewById(checkedChipId);
            if (checkedChip != null) {
                typemeal = checkedChip.getText().toString();
            }
        }

        String uploaddate = String.valueOf(convertStringToMillis(date));
        String uploaddatetime = String.valueOf(convertStringToMillis2(date + time));
        String foodname = binding.textFoodInputEditText.getText().toString();
        String month = String.valueOf(convertStringToMillisMonth(loadmonth));
        String review = binding.foodExplainInputEditText.getText().toString();
        int price = Integer.parseInt(binding.priceInputEditText.getText().toString());
        int cal = (50 + (int) (Math.random() * 100));
        int id = (int) (Math.random() * 100000);

        Food food = new Food(address, typemeal, uploaddate, uploaddatetime, month, foodname, bytemap, review, price, cal, id);

        new Thread(() -> {
            AppDataBase.getInstance(AddFoodActivity.this).foodDao().insert(food);
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
                if (onDataInsertListener != null) {
                    onDataInsertListener.onDataInserted();
                }
                Intent intent = new Intent().putExtra("Updated", true);
                Log.d(TAG, month);
                setResult(RESULT_OK, intent);
                finish();
            });
        }).start();
    }

    public class BitmapUtils {
        public byte[] toByteArray(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
    }
}
