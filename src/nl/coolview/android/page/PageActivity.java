package nl.coolview.android.page;

import nl.coolview.android.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * PageActivity Class
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
public class PageActivity extends Activity {

	public static Boolean isRefreshing = false;
	private static View[] views;

	private PageManager pageManager;
	
	private Toolbar toolBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Set theme
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = prefs.getString("theme", "system");
		if (theme.equals("light")) {
			setTheme(R.style.LightTheme);
		} else if (theme.equals("default")) {
			setTheme(R.style.DefaultTheme);
		} else {
			setTheme(android.R.style.Theme);
		}
		
		// Instantiate self
		super.onCreate(savedInstanceState);

		// Enable the use of a rotating progress icon in the title bar
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.page_activity);
		
		toolBar = (Toolbar)findViewById(R.id.page_toolbar);
		toolBar.setTitle("test");
		
		pageManager = new PageManager(this);//(PageManager)findViewById(R.id.page_manager);//
		pageManager.setViews(views);
		if (pageManager.getChildCount() > 0) {
			setTitle(pageManager.getCurrentPage().getTitle());
		}
		views = null;
		//setContentView(pageManager);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pageManager.getChildCount() <= 1) {
				// Root page. Fade out, and finish.
				PageAnimation.performFadeOutAnimationAndFinish(this, pageManager);
			} else {
				pageManager.animatePrev();
				//setTitle(pageManager.getCurrentPage().getTitle());
			}
			return true;
		} else {
			super.onKeyDown(keyCode, event);
			return false;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Perform the initial stuff here when needed...
		// This is easy to determine, as the PageManager will have no children yet.

		if (pageManager.getChildCount() == 0) {
			//if (isNetworkAvailable()) {
//			pageManager.addView(new PageView(this));
			//  new DisplayRequest(this, pageManager).execute("index.php");
			//} else {
			//	DisplayUtils.showLongMessage(this, getString(R.string.err_msg_network_unavailable));
			//}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isRefreshing = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isRefreshing) {
			// Reset isRefreshing
			isRefreshing = false;
		} else {
			// Final chance!
			// Perform an immediate removal of the application from the process stack.
			// This reserves memory, keeping your battery healhty and keeps your droid
			// responsive at all times.
			// This application will never need to be on any task-killer list :P
			// Apart from the future service part... ;)
			// Anyways, when we say exit, we mean:
			
			//mTracker.dispatch();
			//mTracker.stop();
			
			System.exit(0);
		}
	}

	public PageManager getPageManager() {
		return pageManager;
	}
	
}
