package com.example.afathy.viewmodelsample;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SampleViewModelTest {
	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	@Mock
	SampleRepository repository;
	@Mock
	Observer textObserver;
	@Mock
	Observer loadingObserver;
	@Mock
	Observer errorObserver;
	@Mock
	Observer buttonEnabledObserver;

	SampleViewModel viewModel;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());
		viewModel = new SampleViewModel(repository);
		viewModel.getText().observeForever(textObserver);
		viewModel.getLoading().observeForever(loadingObserver);
		viewModel.getError().observeForever(errorObserver);
		viewModel.getButtonEnabled().observeForever(buttonEnabledObserver);
	}

	@Test
	public void testClickSuccessful() throws Exception {
		when(repository.getText()).thenReturn("123");

		viewModel.onClick();

		InOrder inorder = Mockito.inOrder(buttonEnabledObserver, textObserver, errorObserver, loadingObserver);
		inorder.verify(buttonEnabledObserver).onChanged(eq(false));
		inorder.verify(loadingObserver).onChanged(true);
		inorder.verify(textObserver).onChanged(eq("123"));
		inorder.verify(loadingObserver).onChanged(eq(false));
		inorder.verify(buttonEnabledObserver).onChanged(eq(true));
		inorder.verifyNoMoreInteractions();
	}

	@Test
	public void testClickFail() throws Exception {
		when(repository.getText()).thenThrow(new IOException());

		viewModel.onClick();

		InOrder inorder = Mockito.inOrder(buttonEnabledObserver, textObserver, errorObserver, loadingObserver);
		inorder.verify(buttonEnabledObserver).onChanged(eq(false));
		inorder.verify(loadingObserver).onChanged(true);
		inorder.verify(errorObserver).onChanged(true);
		inorder.verify(loadingObserver).onChanged(eq(false));
		inorder.verify(buttonEnabledObserver).onChanged(eq(true));
		inorder.verifyNoMoreInteractions();
	}

}