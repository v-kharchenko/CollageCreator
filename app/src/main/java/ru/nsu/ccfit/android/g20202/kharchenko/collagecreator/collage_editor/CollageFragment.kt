package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collage_editor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.CollageCreatorApplication
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collages_list.CollageListAdapter
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModel
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModelFactory
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.databinding.FragmentCollageBinding

private const val COLUMN_COUNT = "columnCount"
private const val ROW_COUNT = "rowCount"
private const val ID = "id"

class CollageFragment : Fragment() {
    private lateinit var binding: FragmentCollageBinding

    private var columnCount: Int = 3
    private var rowCount: Int = 3
    private var id: Int = 0

    private lateinit var dataSet: ArrayList<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(COLUMN_COUNT)
            rowCount = it.getInt(ROW_COUNT)
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollageBinding.inflate(layoutInflater)

        val collageViewModel: CollageViewModel by activityViewModels {
            CollageViewModelFactory((requireActivity().application as CollageCreatorApplication).repository)
        }

        collageViewModel.getCollageById(id).observe(viewLifecycleOwner) {collage ->
            binding.collageName.text = collage.name
            rowCount = collage.rowCount
            columnCount = collage.columnCount
            (binding.imagesRecyclerView.layoutManager as GridLayoutManager).spanCount = columnCount

        }

        setupCollageEditor()

        setupSaveButton()

        setupExportButton()

        return binding.root
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {_ ->
            Toast.makeText(context, "Pressed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupExportButton() {
        val exporter = CollageExporter(requireContext())

        binding.exportButton.setOnClickListener {_ ->
            exporter.exportCollage(dataSet, rowCount, columnCount)
        }
    }

    private fun setupCollageEditor() {
        dataSet = prepareImageDataset()
        val imageAdapter = setupImageAdapter(dataSet)

        binding.imagesRecyclerView.adapter = imageAdapter
        binding.imagesRecyclerView.layoutManager = GridLayoutManager(context, columnCount)
    }

    private fun prepareImageDataset(): ArrayList<Uri?> {
        val dataSet = ArrayList<Uri?>()

        for (i in 0 until rowCount * columnCount)
            dataSet.add(null)

        return dataSet
    }

    private fun setupImageAdapter(dataSet: ArrayList<Uri?>): ImageAdapter {
        val customAdapter = ImageAdapter(dataSet)

        customAdapter.setOnClickListener(object : ImageAdapter.OnClickListener {
            var imageView: ImageView? = null
            var position: Int = 0

            val pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        imageView?.setImageURI(uri)
                        dataSet[position] = uri
                    }
                }

            override fun onClick(image: ImageView, position: Int) {
                this.imageView = image
                this.position = position
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        })

        return customAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int, cCount: Int, rCount: Int) =
            CollageFragment().apply {
                arguments = Bundle().apply {
                    putInt(COLUMN_COUNT, cCount)
                    putInt(ROW_COUNT, rCount)
                    putInt(ID, id)
                }
            }
    }
}