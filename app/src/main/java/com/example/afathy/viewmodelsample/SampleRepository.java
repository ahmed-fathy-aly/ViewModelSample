package com.example.afathy.viewmodelsample;

import android.arch.lifecycle.LiveData;

import java.io.IOException;
import java.util.Random;

public class SampleRepository {

	/**
	 * delays a while then return a string with a random number
	 * @throws IOException one third of the times
	 */
	public String getText() throws IOException {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int x = new Random().nextInt(10000);
		if (x % 3 == 0)
			throw new IOException();
		String str = String.valueOf(x);
		return str;
	}

 }
