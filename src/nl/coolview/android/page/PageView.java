package nl.coolview.android.page;

import java.util.LinkedHashMap;

import org.w3c.dom.Element;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * PageView Class
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
public abstract class PageView extends RelativeLayout implements OnItemClickListener {

	private ImageView image;
	private ListView list;
	private TextView text;

	protected PageActivity activity;

	private String title;

	private final LinkedHashMap<String, Element> items = new LinkedHashMap<String, Element>();
	private String[] keys;

	public PageView(Context context) {
		super(context);

		activity = (PageActivity) context;

		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		image.setScaleType(ScaleType.CENTER_INSIDE);

		list = new ListView(context);
		list.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		text = new TextView(context);
		text.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public Element getElement(int position) {
		return items.get(keys[position]);
	}

	public ImageView getImage() {
		return image;
	}

	public String getKey(int position) {
		return keys[position];
	}

	public ListView getList() {
		return list;
	}

	public int getListItemCount() {
		return items.size();
	}

	public TextView getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public void setImage(int resource) {
		removeAllViews();
		image.setImageResource(resource);
		addView(image);
	}

	public void setItemClickListener(AdapterView.OnItemClickListener listener) {
		list.setOnItemClickListener(listener);
	}

	public void setList(ListAdapter adapter) {
		removeAllViews();
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		addView(list);
	}

	public void setText(int resource) {
		setText(getContext().getString(resource));
	}

	public void setText(String string) {
		removeAllViews();
		text.setText(string);
		addView(text);
	}

	public void setupListParams() {
		list.setFastScrollEnabled(true);
		list.setPersistentDrawingCache(PERSISTENT_ANIMATION_CACHE|PERSISTENT_SCROLLING_CACHE);
		list.setAnimationCacheEnabled(true);
		list.setScrollingCacheEnabled(true);
	}

	public void refresh() {}

	public PageView cloneFrom(PageView page) {
		try {
			setList(new PageListAdapter(activity, this));
		} catch (Exception e) {
			setText(e.getMessage());
		}
		return this;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		// TODO Auto-generated method stub
		
	}

}
