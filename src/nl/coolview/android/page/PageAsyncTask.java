package nl.coolview.android.page;

import nl.coolview.android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

/**
 * PageAsyncTask Class
 * 
 * <p>Copyright (c) Rory Slegtenhorst
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Rory Slegtenhorst <rory.slegtenhorst@gmail.com>
 */
public abstract class PageAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected Activity activity;
	protected View progressView;
	protected ProgressDialog progressDialog;

	/**
	 * Constructor to use of you want a transparent "please wait" dialog on top of your activity.
	 * This disables user input while the task is running.
	 * @param activity
	 */
	public PageAsyncTask(Activity activity) {
		this(activity, null);
		setProgressDialog();
	}

	/**
	 * Constructor to use if you have a progress view to be shown and hidden.
	 * Also use this constructor if there should be no progress at all (view = null).
	 * @param activity
	 * @param progressView
	 */
	public PageAsyncTask(Activity activity, View progressView) {
		super();
		this.activity = activity;
		setProgressView(progressView);
	}

	private void setProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this.activity);
			progressDialog.setMessage(activity.getString(R.string.dialog_wait_message));
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}
	}

	public void setProgressView(View v) {
		progressView = v;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progressView != null) {
			progressView.setVisibility(View.VISIBLE);
		} else if (progressDialog != null) {
			progressDialog.show();
		}

		// Set up a delayed "time-out" task to cancel this one
		@SuppressWarnings("rawtypes")
		final AsyncTask self = this;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!self.getStatus().equals(AsyncTask.Status.FINISHED)) {
					self.cancel(true);
				}
			}
		}, 30 * 1000); // 30 seconds
	}

	@Override
	protected void onPostExecute(Result result) {
		if (progressView != null) {
			progressView.setVisibility(View.GONE);
		} else if (progressDialog != null) {
			progressDialog.dismiss();
		}
		super.onPostExecute(result);
	}

}
