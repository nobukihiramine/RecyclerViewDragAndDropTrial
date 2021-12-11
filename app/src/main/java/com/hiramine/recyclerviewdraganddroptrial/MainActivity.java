package com.hiramine.recyclerviewdraganddroptrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
	private static final String LOGTAG = "MainActivity";

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		// ビューの取得
		RecyclerView recyclerview = findViewById( R.id.recyclerview_main );

		recyclerview.setHasFixedSize( true );    // アダプタの変更がRecyclerViewのサイズに影響を与えない場合はtrueを設定する。結果、パフォーマンスが向上する。

		// LinearLayoutManagerの作成と登録
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
		recyclerview.setLayoutManager( layoutManager );

		// 表示データの構築
		List<String> listItem = new ArrayList<>();
		for( int i = 0; i < 20; i++ )
		{
			String str = String.format( Locale.US, "Data_%02d", i );
			listItem.add( str );
		}

		// アダプターの作成と登録
		ItemListAdapter adapter = new ItemListAdapter( listItem );
		recyclerview.setAdapter( adapter );

		// 境界線の設定（アイテム装飾の設定）
		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration( this, DividerItemDecoration.VERTICAL );
		recyclerview.addItemDecoration( itemDecoration );

		// タッチヘルパーの作成と登録
		ItemTouchHelper itemtouchhelper = new ItemTouchHelper(
				new ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP | ItemTouchHelper.DOWN,
													ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT )
				{
					@Override
					public boolean onMove( @NonNull RecyclerView recyclerView,
										   @NonNull RecyclerView.ViewHolder viewHolder,
										   @NonNull RecyclerView.ViewHolder target )
					{
						final int iFrom   = viewHolder.getAdapterPosition();
						final int iTo     = target.getAdapterPosition();
						String    strFrom = listItem.get( iFrom );
						String    strTo   = listItem.get( iTo );
						listItem.set( iFrom, strTo );
						listItem.set( iTo, strFrom );
						adapter.notifyItemMoved( iFrom, iTo );
						return true;    // true if moved, false otherwise*/
					}

					@Override
					public void onSwiped( @NonNull RecyclerView.ViewHolder viewHolder, int direction )
					{
						int iPos = viewHolder.getAdapterPosition();
						listItem.remove( iPos );
						adapter.notifyItemRemoved( iPos );
					}

					@Override
					public void onChildDraw( @NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
											 @NonNull RecyclerView.ViewHolder viewHolder,
											 float dX, float dY, int actionState, boolean isCurrentlyActive )
					{
						super.onChildDraw( canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive );

						if( ItemTouchHelper.ACTION_STATE_SWIPE == actionState )
						{
							// 項目のViewの取得
							View     viewItem           = viewHolder.itemView;
							Drawable drawableBackground;
							Drawable drawableDeleteIcon = ResourcesCompat.getDrawable( getResources(), R.drawable.ic_baseline_delete_32, null );
							if( null == drawableDeleteIcon )
							{
								Log.e( LOGTAG, "Failed to get DeleteIcon." );
								return;
							}
							int iDeleteIconMargin = ( viewItem.getHeight() - drawableDeleteIcon.getIntrinsicHeight() ) / 2;
							if( 0 < dX )
							{    // 右にスワイプ
								drawableBackground = new ColorDrawable( Color.parseColor( "#FF0000" ) );
								drawableBackground.setBounds( viewItem.getLeft(),
															  viewItem.getTop(),
															  viewItem.getLeft() + (int)dX,
															  viewItem.getBottom() );

								drawableDeleteIcon.setBounds( viewItem.getLeft() + iDeleteIconMargin,
															  viewItem.getTop() + iDeleteIconMargin,
															  viewItem.getLeft() + iDeleteIconMargin + drawableDeleteIcon.getIntrinsicHeight(),
															  viewItem.getBottom() - iDeleteIconMargin );
							}
							else
							{    // 左にスワイプ
								drawableBackground = new ColorDrawable( Color.parseColor( "#0000FF" ) );
								drawableBackground.setBounds( viewItem.getRight() + (int)dX,
															  viewItem.getTop(),
															  viewItem.getRight(),
															  viewItem.getBottom() );

								drawableDeleteIcon.setBounds( viewItem.getRight() - iDeleteIconMargin - drawableDeleteIcon.getIntrinsicHeight(),
															  viewItem.getTop() + iDeleteIconMargin,
															  viewItem.getRight() - iDeleteIconMargin,
															  viewItem.getBottom() - iDeleteIconMargin );
							}
							drawableBackground.draw( canvas );
							drawableDeleteIcon.draw( canvas );
						}
					}
				} );

		itemtouchhelper.attachToRecyclerView( recyclerview );
	}
}