package com.hiramine.recyclerviewdraganddroptrial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
	static class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView m_textView;

		public ViewHolder( View view )
		{
			super( view );
			m_textView = view.findViewById( R.id.textview_name );
		}
	}

	private final List<String> m_listName;

	public ItemListAdapter( List<String> listName )
	{
		m_listName = listName;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType )
	{
		View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.listitem_item, parent, false );

		return new ViewHolder( view );
	}

	@Override
	public void onBindViewHolder( @NonNull ViewHolder holder, int position )
	{
		holder.m_textView.setText( m_listName.get( position ) );
	}

	@Override
	public int getItemCount()
	{
		return m_listName.size();
	}
}
