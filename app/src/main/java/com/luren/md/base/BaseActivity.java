package com.luren.md.base;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewbinding.ViewBinding;
import com.dylanc.viewbinding.base.ViewBindingUtil;
import com.luren.md.App;

public class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

  protected VB binding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    EdgeToEdge.enable(this);
    super.onCreate(savedInstanceState);
    App.getApp().addActivity(this);
    binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
    ViewCompat.setOnApplyWindowInsetsListener(
        binding.getRoot(),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
    setContentView(binding.getRoot());
  }

  public void shortToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void longToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App.getApp().removeActivity(this);
    binding = null;
  }
}
