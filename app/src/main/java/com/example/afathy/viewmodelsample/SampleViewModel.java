package com.example.afathy.viewmodelsample;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SampleViewModel extends android.arch.lifecycle.ViewModel {
	private MutableLiveData<String> text;
	private MutableLiveData<Boolean> isLoading;
	private MutableLiveData<Boolean> buttonEnabled;
	private MutableLiveData<Boolean> error;

	private SampleRepository repository;

	private Disposable disposable;

	public SampleViewModel() {
		text = new MutableLiveData<>();
		text.setValue("");

		isLoading = new MutableLiveData<>();
		isLoading.setValue(false);

		buttonEnabled = new MutableLiveData<>();
		buttonEnabled.setValue(true);

		error = new MutableLiveData<>();

		this.repository = new SampleRepository(); // should be injected some way
		this.disposable = Disposables.disposed();
	}

	public LiveData getText() {
		return text;
	}

	public LiveData getLoading() {
		return isLoading;
	}

	public LiveData getError() {
		return error;
	}

	public LiveData getButtonEnabled() {
		return buttonEnabled;
	}

	public void onClick() {
		if (buttonEnabled.getValue() == false) {
			return;
		}

		buttonEnabled.setValue(false);
		isLoading.setValue(true);

		disposable = Single
				.fromCallable(repository::getText)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(s -> {
					text.setValue(s);
					isLoading.setValue(false);
					buttonEnabled.setValue(true);
				}, e -> {
					error.setValue(true);
					isLoading.setValue(false);
					buttonEnabled.setValue(true);
				});
	}

	@Override
	protected void onCleared() {
		disposable.dispose();
	}
}
