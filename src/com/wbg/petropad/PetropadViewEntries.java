package com.wbg.petropad;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class PetropadViewEntries extends ListActivity {
    private static final int MENU_EDIT_ENTRY = 0;
    private static final int MENU_DELETE_ENTRY = 1;

    private static final int MENU_GROUP_DEFAULT = 0;
    private int _selected_entry_id;
    private int _vehicle_index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_entries);
        registerForContextMenu(getListView());
        _vehicle_index = getIntent().getExtras().getInt("vehicle_index");
        EntriesAdapter adapter = new EntriesAdapter(Petropad.vehicles.get(_vehicle_index).entries);
        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(longClickListener);
        setTitle(getString(R.string.title_viewing_entries_for, Petropad.vehicles.get(_vehicle_index).name));
    }

    public AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int item_index, long entry_id) {
            _selected_entry_id = (int) entry_id;
            return false;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(MENU_GROUP_DEFAULT, MENU_EDIT_ENTRY, 0, R.string.menu_edit_entry);
        menu.add(MENU_GROUP_DEFAULT, MENU_DELETE_ENTRY, 1, R.string.menu_delete_entry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MENU_GROUP_DEFAULT, Petropad.MENU_CREATE_ENTRY, 0, R.string.menu_add_entry);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Petropad.MENU_CREATE_ENTRY:
                Intent i = new Intent(PetropadViewEntries.this, PetropadNewEntry.class);
                i.putExtra("mode", PetropadNewEntry.ENTRY_MODE_NEW);
                i.putExtra("vehicle_index", _vehicle_index);
                PetropadViewEntries.this.startActivityForResult(i, Petropad.RESULT_NEW_ENTRY);
                return true;
        }
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        switch (item.getItemId()) {
            case MENU_EDIT_ENTRY:
                Intent i = new Intent(PetropadViewEntries.this, PetropadNewEntry.class);
                i.putExtra("mode", PetropadNewEntry.ENTRY_MODE_EDIT);
                i.putExtra("vehicle_index", _vehicle_index);
                i.putExtra("entry_id", _selected_entry_id);
                PetropadViewEntries.this.startActivityForResult(i, Petropad.RESULT_EDIT_ENTRY);
                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Petropad.RESULT_NEW_ENTRY:
            case Petropad.RESULT_EDIT_ENTRY:
                //refresh the adapter
                EntriesAdapter adapter = new EntriesAdapter(Petropad.vehicles.get(_vehicle_index).entries);
                setListAdapter(adapter);
                break;
        }

    }

}
