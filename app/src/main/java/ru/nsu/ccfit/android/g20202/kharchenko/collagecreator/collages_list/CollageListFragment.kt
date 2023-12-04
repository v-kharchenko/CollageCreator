package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collages_list

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.CollageCreatorApplication
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.R
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModel
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModelFactory
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.databinding.CreateCollageDialogBinding
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.databinding.FragmentCollageBinding
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.databinding.FragmentCollageListBinding

class CollageListFragment : Fragment() {
    private lateinit var binding: FragmentCollageListBinding

    private var columnCount: Int = 3
    private var rowCount: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollageListBinding.inflate(layoutInflater)

        val adapter = CollageListAdapter()
        binding.collagesRecyclerView.adapter = adapter
        binding.collagesRecyclerView.layoutManager = LinearLayoutManager(context)

        val collageViewModel: CollageViewModel by activityViewModels {
            CollageViewModelFactory((requireActivity().application as CollageCreatorApplication).repository)
        }

        collageViewModel.allCollages.observe(viewLifecycleOwner) { collages ->
            collages.let { (binding.collagesRecyclerView.adapter as CollageListAdapter).submitList(collages) }
        }

        setupCreateButton()

        return binding.root
    }

    private fun setupCreateButton() {
        binding.createNewCollageButton.setOnClickListener {_ ->
            val dialogBuilder = AlertDialog.Builder(context)

            val dialogBinding = CreateCollageDialogBinding.inflate(layoutInflater)

            dialogBuilder.setView(dialogBinding.root)

            dialogBinding.rowSpinner.adapter = context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_spinner_dropdown_item, arrayOf(3, 4, 5)) }
            dialogBinding.columnSpinner.adapter = context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_spinner_dropdown_item, arrayOf(3, 4, 5)) }

            dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val bundle = Bundle()
                bundle.putString("name", dialogBinding.collageName.text.toString())
                bundle.putInt("rowCount", dialogBinding.rowSpinner.selectedItem as Int)
                bundle.putInt("columnCount", dialogBinding.columnSpinner.selectedItem as Int)
                findNavController().navigate(R.id.action_fragment_collage_list_to_fragment_collage_editor, bundle)
            }
            dialogBuilder.setNegativeButton("Cancel") {dialog, _ ->
                dialog.cancel()
            }

            dialogBuilder.create().show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CollageListFragment()
    }
}