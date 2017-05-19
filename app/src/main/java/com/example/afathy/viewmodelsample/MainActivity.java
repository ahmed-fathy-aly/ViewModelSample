package com.example.afathy.viewmodelsample;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends LifecycleActivity {

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		// reference views
		TextView textView = (TextView) findViewById(R.id.textView);
		Button button = (Button) findViewById(R.id.buttonLoad);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		// observe on the view model
		SampleViewModel viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		viewModel.getText().observe(this, text -> textView.setText((String) text));
		viewModel.getLoading().observe(this, isLoading -> progressBar.setVisibility((Boolean) isLoading ? View.VISIBLE : View.INVISIBLE));
		viewModel.getButtonEnabled().observe(this, enabled -> button.setEnabled((Boolean) enabled));
		viewModel.getError().observe(this, error -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show());
		button.setOnClickListener(v -> viewModel.onClick());
	}
}
