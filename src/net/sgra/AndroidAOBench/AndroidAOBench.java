package net.sgra.AndroidAOBench;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import aobench.AmbientOcclusion;

public class AndroidAOBench extends Activity {

	private static final int WIDTH = 256;
	private static final int HEIGHT = 256;
	private static final int SUBSAMPLES = 2;
	private Thread thread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SampleView sampleView = new SampleView(this);
		AmbientOcclusion ao = new AmbientOcclusion();
		Benchmark benchmark = new Benchmark(ao, sampleView);

		setContentView(sampleView);

		thread = new Thread(benchmark);
		thread.start();
	}

	private static class Benchmark implements Runnable {
		AmbientOcclusion ao;
		SampleView sampleView;

		public Benchmark(AmbientOcclusion ao, SampleView sampleView) {
			this.ao = ao;
			this.sampleView = sampleView;
		}

		public void run() {
			android.util.Log.i("BENCH", "Start");

			long start = System.currentTimeMillis();
			int[] pixels = ao.render(WIDTH, HEIGHT, SUBSAMPLES);
			long end = System.currentTimeMillis();

			android.util.Log.i("BENCH", String.format("End: elapse time %dsec",
					(end - start) / 1000));

			sampleView.updateBitmap(pixels, WIDTH, HEIGHT);
		}
	}

	private static class SampleView extends View {
		private Bitmap _bitmap = null;

		public SampleView(Context context) {
			super(context);
			setFocusable(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFF444444);
			if (_bitmap != null) {
				canvas.drawBitmap(_bitmap, 0, 0, null);
			}
			invalidate();
		}

		public void updateBitmap(int[] pixels, int width, int height) {
			_bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
					Bitmap.Config.ARGB_8888);
			postInvalidate();
		}
	}

}