package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collages_list

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.CollageCreatorApplication
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModel
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageViewModelFactory
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

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CollageListFragment()
    }
}