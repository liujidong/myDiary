package com.eoeAndroid.contentProvider;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.eoeAndroid.contentProvider.Diary.DiaryColumns;

public class ActivityMain extends ListActivity {

	// ����һ���¼�¼
	public static final int MENU_ITEM_INSERT = Menu.FIRST;
	// �༭����
	public static final int MENU_ITEM_EDIT = Menu.FIRST + 1;
	public static final int MENU_ITEM_DELETE = Menu.FIRST + 2;

	private static final String[] PROJECTION = new String[] { DiaryColumns._ID,
			DiaryColumns.TITLE, DiaryColumns.CREATED };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_list);
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(DiaryColumns.CONTENT_URI);
		}
		Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null,
				null, DiaryColumns.DEFAULT_SORT_ORDER);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.diary_row, cursor, new String[] { DiaryColumns.TITLE,
						DiaryColumns.CREATED }, new int[] { R.id.text1,
						R.id.created });
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ITEM_INSERT, 0, R.string.menu_insert);
		return true;
	}

	@Override
	/*
	 * ��ÿһ��menu���ɵ�ʱ��ǰ���������������������������߿��Զ�̬���޸����ɵ�menu��
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		final boolean haveItems = getListAdapter().getCount() > 0;
		if (haveItems) {
			// ���ѡ��һ��Item�Ļ�
			if (getListView().getSelectedItemId() > 0) {
				menu.removeGroup(1);
				Uri uri = ContentUris.withAppendedId(getIntent().getData(),
						getSelectedItemId());
				Intent intent = new Intent(null, uri);
				menu.add(1, MENU_ITEM_EDIT, 1, "�༭����").setIntent(intent);
				menu.add(1, MENU_ITEM_DELETE, 1, "ɾ����ǰ�ռ�");
			}

		}else{
			menu.removeGroup(1);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// ����һ������
		case MENU_ITEM_INSERT:
			Intent intent0 = new Intent(this, ActivityDiaryEditor.class);
			intent0.setAction(ActivityDiaryEditor.INSERT_DIARY_ACTION);
			intent0.setData(getIntent().getData());
			startActivity(intent0);
			return true;
			// �༭��ǰ��������
		case MENU_ITEM_EDIT:
			Intent intent = new Intent(this, ActivityDiaryEditor.class);
			intent.setData(item.getIntent().getData());
			intent.setAction(ActivityDiaryEditor.EDIT_DIARY_ACTION);
			startActivity(intent);
			return true;
			// ɾ����ǰ����
		case MENU_ITEM_DELETE:
			Uri uri = ContentUris.withAppendedId(getIntent().getData(),
					getListView().getSelectedItemId());
			getContentResolver().delete(uri, null, null);
			renderListView();

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
		startActivity(new Intent(ActivityDiaryEditor.EDIT_DIARY_ACTION, uri));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		//renderListView();
	}

	private void renderListView() {
		Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null,
				null, DiaryColumns.DEFAULT_SORT_ORDER);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.diary_row, cursor, new String[] { DiaryColumns.TITLE,
						DiaryColumns.CREATED }, new int[] { R.id.text1,
						R.id.created });
		setListAdapter(adapter);
	}
}
