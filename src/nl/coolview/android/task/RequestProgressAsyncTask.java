package nl.coolview.android.task;

import android.app.Activity;
import android.view.View;

/**
 * RequestProgressAsyncTask Class
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
public class RequestProgressAsyncTask extends ProgressAsyncTask<Void, Void, RequestResponse> {

	protected RequestTask task;

	public RequestProgressAsyncTask(Activity activity, RequestTask task) {
		super(activity);
		this.task = task;
	}

	public RequestProgressAsyncTask(Activity activity, View progressView, RequestTask task) {
		super(activity, progressView);
		this.task = task;
	}

	@Override
	protected final RequestResponse doInBackground(Void... params) {
		RequestResponse response = task.doRequest();
		// Optionally perform re-authentication here
		return response;
	}

}
