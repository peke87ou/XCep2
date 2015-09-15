package com.irina.xcep;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.irina.xcep.adapters.MenuAdapter;
import com.irina.xcep.adapters.MergeAdapter;
import com.irina.xcep.menu.navitems.NavDrawerItem;
import com.irina.xcep.menu.navitems.NavMenuItem;
import com.irina.xcep.menu.navitems.NavTitleItem;
import com.irina.xcep.model.Lista;
import com.irina.xcep.utils.ShareSocialMediaActivity;
import com.irina.xcep.utils.Utils;

@SuppressLint("DefaultLocale")
public class MenuActivity extends ShareSocialMediaActivity implements MenuAdapter.SelectedListButton, AdapterView.OnItemClickListener {

	public static final int FRAGMENT_HOME = 101;
	public static final int FRAGMENT_CATALOG = 102;
	public static final int FRAGMENT_SCAN = 103;
	public static final int FRAGMENT_LIST = 104;
	public static final int FACEBOOK = 201;
	public static final int TWITTER = 202;
	public static final int LANGUAGE = 203;
	public static final int EMAIL = 206;
	public static final int HELP = 205;

	public int mCurrentFragmentIndex;
	private static final String CURRENT_FRAGMENT_INDEX = "current_fragment";

	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public String mNameList = "";
	public Lista mListSelected = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		mTitle = this.mDrawerTitle = getTitle();

		/* Setting up all views */
		initViews();

		mDrawerLayout.setDrawerListener(this.mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		if (savedInstanceState == null) {
			loadFragment(FRAGMENT_HOME);
			mCurrentFragmentIndex = FRAGMENT_HOME;
			mDrawerLayout.setSelected(true);

		} else {

			mCurrentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT_INDEX);
			loadFragment(mCurrentFragmentIndex);
			mDrawerLayout.setSelected(true);
		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
	}

	/**
	 * method use to initialize all views and listeners
	 */
	public void initViews() {

		this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.mListView = (ListView) findViewById(R.id.list_drawer);
		this.mListView.setOnItemClickListener(this);

		MenuAdapter menuAdapter = new MenuAdapter(this, R.layout.item_list_menu, menu, this);
		MergeAdapter mergeAdapter = new MergeAdapter();
		mergeAdapter.addAdapter(menuAdapter);

		this.mListView.setAdapter(mergeAdapter);

		this.mDrawerToggle = new ActionBarDrawerToggle(MenuActivity.this, this.mDrawerLayout, R.drawable.ic_menu_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(mTitle);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					invalidateOptionsMenu();
				}

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					invalidateOptionsMenu();
				}
			}

		};
	}

	/*
	 * @param title: action's bar title depends on current fragment
	 */
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(title);

	}

	/* onItemClick del navigation drawer */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (((NavDrawerItem) parent.getItemAtPosition(position)).isClickable()
				&& NavDrawerItem.SECTION_MENU_ITEM == ((NavDrawerItem) parent.getItemAtPosition(position)).getType()) {
			loadFragment(menu[position].getId());
		}
	}

	/* Static Navigation Drawer items */
	private NavDrawerItem[] menu = new NavDrawerItem[] { NavTitleItem.create(100, R.string.app_name),
			NavMenuItem.create(FRAGMENT_HOME, R.string.my_list, R.drawable.list, true, this),
			NavMenuItem.create(FRAGMENT_CATALOG, R.string.catalog, R.drawable.ic_action_description, true, this),
			NavMenuItem.create(FRAGMENT_SCAN, R.string.scan, R.drawable.ic_navigation_fullscreen, true, this), NavTitleItem.create(200, R.string.setting),
			NavMenuItem.create(FACEBOOK, R.string.facebook, R.drawable.facebook, true, this),
			NavMenuItem.create(TWITTER, R.string.twitter, R.drawable.twitter, true, this),
			NavMenuItem.create(EMAIL, R.string.sugerencias, R.drawable.ic_email, true, this),
			NavMenuItem.create(LANGUAGE, R.string.language, R.drawable.comments, true, this),
			NavMenuItem.create(HELP, R.string.help, R.drawable.help, true, this) };

	public void loadFragment(int index) {

		Fragment fragment = null;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		switch (index) {

		case FRAGMENT_HOME:
			fragment = HomeFragment.newInstance(FRAGMENT_HOME);
			break;

		case FRAGMENT_LIST:
			fragment = DetailListFragment.newInstance(FRAGMENT_LIST);
			break;

		case FRAGMENT_CATALOG:
			fragment = GeneralCatalogFragment.newInstance(FRAGMENT_CATALOG);
			break;

		case FRAGMENT_SCAN:
			fragment = GeneralScanFragment.newInstance(FRAGMENT_SCAN);
			break;

		case FACEBOOK:
			shareFacebookApp(getString(R.string.aplicacion_de_xestion_de_compra), getResources().getString(R.string.app_name), Utils.urlAppXecp);
			return;

		case TWITTER:
			shareTwitterPost(getString(R.string.aplicacion_de_xestion_de_compra), getResources().getString(R.string.app_name), Utils.urlAppXecp);
			return;

		case EMAIL:
			sendEmail();
			return;

		case HELP:
			showHelp();
			return;
			
		case LANGUAGE:
			showLanguage();
			return;	

		default:
			Toast.makeText(this, getString(R.string.funcionalidade_en_cosntruccion), Toast.LENGTH_LONG).show();
			return;

		}

		// Add fragment to layout
		// Store current index
		mCurrentFragmentIndex = index;
		// Add fragment to layout
		transaction.replace(R.id.container, fragment);
		if (index != FRAGMENT_HOME) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
		mListView.setItemChecked(index, true);
		mDrawerLayout.closeDrawer(mListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mDrawerToggle.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CURRENT_FRAGMENT_INDEX, mCurrentFragmentIndex);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSelectedButton(int type) {

	}

	/**
	 * About and help
	 */

	@SuppressLint("InflateParams")
	public void showHelp() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.dialog_about, null);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setCancelable(true).setPositiveButton(getString(R.string.pechar), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void sendEmail() {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "irinaourense@gmail.com", null));
		intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\nEnviado desde XceP.");
		startActivity(Intent.createChooser(intent, "Enviar suxerencia"));
	}

	
	public void showLanguage(){
		
		String[] idiomas = {"Español", "Galego"};
		final String[] idiomasCodigo = {"es", "gl"};
		
		Configuration config = getBaseContext().getResources().getConfiguration();
		if(config.locale != null){
			switch (config.locale.getLanguage()) {
				case "es":
					idiomas[0] = "Español*";
				break;
			
				case "gl":
					idiomas[1]= "Galego*";
				break;
				
				default:
				break;
			}
			
		}
        
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Idioma").setItems(idiomas, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int nLenguaje) {

						Locale locale = new Locale(idiomasCodigo[nLenguaje]); 
			            Locale.setDefault(locale);
			            Configuration config = new Configuration();
			            config.locale = locale;
			            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

			            Intent intent = getIntent();
			            finish();
			            startActivity(intent);
					}
				});

		AlertDialog dialogoIdioma = builder.create();
		dialogoIdioma.show();
	}
}
