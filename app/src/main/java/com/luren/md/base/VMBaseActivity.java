package com.luren.md.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public class VMBaseActivity<VB extends ViewBinding, VM extends ViewModel> extends BaseActivity<VB> {

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = onCreateViewModel();
    }

    @SuppressWarnings("unchecked")
    protected VM onCreateViewModel() {
        var superClass = getClass().getGenericSuperclass();
        var viewModelClass = (Class<VM>) ((ParameterizedType) Objects.requireNonNull(superClass)).getActualTypeArguments()[1];
        return new ViewModelProvider(this).get(viewModelClass);
    }
}
