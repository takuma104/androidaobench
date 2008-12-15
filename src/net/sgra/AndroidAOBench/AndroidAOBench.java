package net.sgra.AndroidAOBench;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import aobench.AmbientOcclusion;

public class AndroidAOBench extends Activity {

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AmbientOcclusion ao = new AmbientOcclusion();
		android.util.Log.i("BENCH", "start");
		// bench start
		int[] pixels = ao.render(WIDTH, HEIGHT, 2);
		// bench end
		android.util.Log.i("BENCH", "end");
		setContentView(new SampleView(this, pixels, WIDTH, HEIGHT));
	}

	private static class SampleView extends View {
		private Bitmap _bitmap;

		public SampleView(Context context, int[] pixels, int width, int height) {
			super(context);
			setFocusable(true);

			_bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
					Bitmap.Config.ARGB_8888);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFF444444);
			canvas.drawBitmap(_bitmap, 0, 0, null);
			invalidate();
		}
	}
}