package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collages_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.R
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.Collage

class CollageListAdapter : ListAdapter<Collage, CollageListAdapter.CollageViewHolder>(CollagesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageViewHolder {
        return CollageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CollageViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class CollageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val collageItemView: View = itemView.findViewById(R.id.collage_item)

        fun bind(collage: Collage?) {
            (collageItemView.findViewById(R.id.collage_name) as TextView).text = collage!!.name

            collageItemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", collage.id)
                collageItemView.findNavController().navigate(R.id.action_fragment_collage_list_to_fragment_collage_editor, bundle)
            }
        }

        companion object {
            fun create(parent: ViewGroup): CollageViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.collage_item, parent, false)
                return CollageViewHolder(view)
            }
        }
    }

    class CollagesComparator : DiffUtil.ItemCallback<Collage>() {
        override fun areItemsTheSame(oldItem: Collage, newItem: Collage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collage, newItem: Collage): Boolean {
            return oldItem.name == newItem.name
        }
    }
}