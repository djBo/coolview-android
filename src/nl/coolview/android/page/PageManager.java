package nl.coolview.android.page;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

/**
 * PageManager Class
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
public class PageManager extends ViewFlipper {

	private Activity mActivity;
	private Class<PageView> mPageClass;
	
	public PageManager(Context context) {
		super(context);

		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE | ViewGroup.PERSISTENT_SCROLLING_CACHE);
		
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}
	}
	
	public PageManager(Context context, AttributeSet attrs) {
		super(context, attrs);
		//setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE | ViewGroup.PERSISTENT_SCROLLING_CACHE);
		
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}
	}




	@SuppressLint("NewApi")
	public void animateNext() {
		setInAnimation(PageAnimation.inFromRightAnimation());
		setOutAnimation(PageAnimation.outToLeftAnimation());
		showNext();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			if (mActivity != null) mActivity.invalidateOptionsMenu();
	}

	@SuppressLint("NewApi")
	public void animatePrev() {
		View view = getCurrentView();
		setInAnimation(PageAnimation.inFromLeftAnimation());
		setOutAnimation(PageAnimation.outToRightAnimation());
		showPrevious();
		removeView(view);
		view = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			if (mActivity != null) mActivity.invalidateOptionsMenu();
	}
	
	public PageView getCurrentPage() {
		return (PageView) getCurrentView();
	}

	public View[] getViews() {
		View[] result = new View[getChildCount()];
		for (int i = 0; i < result.length; i++) {
			result[i] = getChildAt(i);
		}
		removeAllViews();
		return result;
	}

	public void setViews(View[] views) {
		if (views != null) {
			removeAllViews();
			for (int i = 0; i < views.length; i++) {

				// With a little help of some reflection
				try {
					// Each statement is written out loud for debugging purposes
					Constructor<PageView> mConstructor = mPageClass.getConstructor(Context.class);
					PageView mPage = mConstructor.newInstance(getContext());
					addView(mPage.cloneFrom((PageView) views[i]));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			// Restore last displayed child
			setDisplayedChild(views.length-1);
		}
	}

	public void setFirstView(View view) {
		addView(view, 0);
	}
}
