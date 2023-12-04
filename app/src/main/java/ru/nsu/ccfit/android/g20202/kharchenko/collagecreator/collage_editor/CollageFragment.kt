package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collage_editor

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.CollageCreatorApplication
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.Collage
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModel
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModelFactory
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.databinding.FragmentCollageBinding

private const val COLUMN_COUNT = "columnCount"
private const val ROW_COUNT = "rowCount"
private const val NAME = "name"
private const val ID = "id"

class CollageFragment : Fragment() {
    private lateinit var binding: FragmentCollageBinding

    private var columnCount: Int = 3
    private var rowCount: Int = 3
    private var name: String? = null
    private var id: Int = 0

    private val collageViewModel: CollageViewModel by activityViewModels {
        CollageViewModelFactory((requireActivity().application as CollageCreatorApplication).repository)
    }

    private lateinit var dataSet: ArrayList<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(COLUMN_COUNT)
            if (columnCount == 0) columnCount = 1

            rowCount = it.getInt(ROW_COUNT)
            if (rowCount == 0) rowCount = 1

            name = it.getString("name")

            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollageBinding.inflate(layoutInflater)

        collageViewModel.getCollageById(id).observe(viewLifecycleOwner) {collage ->
            if (collage == null) return@observe

            binding.collageName.text = collage.name
            rowCount = collage.rowCount
            columnCount = collage.columnCount
            (binding.imagesRecyclerView.layoutManager as GridLayoutManager).spanCount = columnCount
            dataSet = collage.uriList as ArrayList<Uri?>
            (binding.imagesRecyclerView.adapter as ImageAdapter).submitDataset(dataSet)
        }

        setupNameEditor()

        setupCollageEditor()

        setupSaveButton()

        setupExportButton()

        return binding.root
    }

    private fun setupNameEditor() {
        binding.collageName.text = name

        binding.collageName.setOnClickListener {_ ->
            val dialogBuilder = AlertDialog.Builder(context)

            val editText = EditText(context)
            editText.text.insert(0, binding.collageName.text)
            editText.inputType = InputType.TYPE_CLASS_TEXT

            dialogBuilder.setView(editText)
            dialogBuilder.setPositiveButton("OK") { _, _ ->
                binding.collageName.text = editText.text
            }
            dialogBuilder.setNegativeButton("Cancel") {dialog, _ ->
                dialog.cancel()
            }

            dialogBuilder.create().show()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {_ ->
            val collage = Collage()

            if (id != 0)
                collage.id = id

            collage.rowCount = rowCount
            collage.columnCount = columnCount
            collage.name = binding.collageName.text.toString()
            collage.uriList = dataSet
            collage.image = CollageExporter(requireContext()).getMiniature(dataSet, rowCount, columnCount)

            collageViewModel.insert(collage)
        }
    }

    private fun setupExportButton() {
        val exporter = CollageExporter(requireContext())

        binding.exportButton.setOnClickListener {_ ->
            exporter.exportCollage(binding.collageName.text.toString(), dataSet, rowCount, columnCount)
        }
    }

    private fun setupCollageEditor() {
        dataSet = prepareImageDataset()
        val imageAdapter = setupImageAdapter()

        binding.imagesRecyclerView.adapter = imageAdapter
        binding.imagesRecyclerView.layoutManager = GridLayoutManager(context, columnCount)
    }

    private fun prepareImageDataset(): ArrayList<Uri?> {
        val dataSet = ArrayList<Uri?>()

        for (i in 0 until rowCount * columnCount)
            dataSet.add(null)

        return dataSet
    }

    private fun setupImageAdapter(): ImageAdapter {
        val customAdapter = ImageAdapter(dataSet)

        customAdapter.setOnClickListener(object : ImageAdapter.OnClickListener {
            var position: Int = 0

            val pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        context?.contentResolver?.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)
                        dataSet[position] = uri
                        customAdapter.notifyDataSetChanged()
                    }
                }

            override fun onClick(image: ImageView, position: Int) {
                this.position = position
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        })

        return customAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int, name: String, cCount: Int, rCount: Int) =
            CollageFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putInt(COLUMN_COUNT, cCount)
                    putInt(ROW_COUNT, rCount)
                    putInt(ID, id)
                }
            }
    }
}