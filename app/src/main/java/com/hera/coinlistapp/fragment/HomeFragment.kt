package com.hera.coinlistapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hera.coinlistapp.R
import com.hera.coinlistapp.adapter.CryptosAdapter
import com.hera.coinlistapp.databinding.FragmentHomeBinding
import com.hera.coinlistapp.utils.DataStatus
import com.hera.coinlistapp.utils.initRecyclerView
import com.hera.coinlistapp.utils.isVisible
import com.hera.coinlistapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var cryptosAdapter: CryptosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {

            binding.apply {
                viewModel.getCoinList("eur")
                viewModel.coinList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            pBarLoading.isVisible(true,rvCoinRv)
                        }
                        DataStatus.Status.SUCCESS -> {
                            pBarLoading.isVisible(false,rvCoinRv)
                            cryptosAdapter.differ.submitList(it.data)
                            cryptosAdapter.setOnItemClickListener {
                                val direction=HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                                    it.id!!
                                )
                                findNavController().navigate(direction)
                            }

                        }
                        DataStatus.Status.ERROR -> {
                            pBarLoading.isVisible(true,rvCoinRv)
                            Toast.makeText(requireContext(), "There is something wrong!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCoinRv.initRecyclerView(LinearLayoutManager(requireContext()), cryptosAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}