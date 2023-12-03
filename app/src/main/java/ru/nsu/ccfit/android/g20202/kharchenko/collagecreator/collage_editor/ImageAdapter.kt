package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collage_editor

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.R

class ImageAdapter(private var dataSet: ArrayList<Uri?>):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView

        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.image_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tile_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (dataSet[position] != null)
            viewHolder.imageView.setImageURI(dataSet[position])
        else
            viewHolder.imageView.setImageResource(R.drawable.add_photo_symbol)

        viewHolder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(viewHolder.imageView, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun submitDataset(dataSet: ArrayList<Uri?>) {
        this.dataSet = dataSet
        this.notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick(image: ImageView, position: Int)
    }
}