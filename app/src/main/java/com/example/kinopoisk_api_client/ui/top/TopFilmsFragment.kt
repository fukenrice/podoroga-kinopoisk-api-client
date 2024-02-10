package com.example.kinopoisk_api_client.ui.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinopoisk_api_client.data.model.FilmListItem
import com.example.kinopoisk_api_client.databinding.FragmentTopFilmsBinding
import com.example.kinopoisk_api_client.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopFilmsFragment : Fragment() {

    private var _binding: FragmentTopFilmsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<TopFilmsViewModel>()

    private lateinit var adapter: FilmsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTopFilmsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
        if (viewModel.isEmpty()) {
            viewModel.getFilms()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        adapter = FilmsAdapter(requireContext(), { film -> onItemClick(film) }, listOf() )
        binding.apply {
            networkView.button.setOnClickListener {
                viewModel.getFilms()
            }
            rvFilms.layoutManager = LinearLayoutManager(context)
            rvFilms.adapter = adapter
        }
    }

    private fun setupObserver() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.films.collect {
                when (it.status) {
                    Status.ERROR -> {
                        showNetworking(false)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        binding.rvFilms.visibility = View.GONE
                        binding.networkView.root.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        showNetworking(true)
                        binding.rvFilms.visibility = View.GONE
                        binding.networkView.root.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        showNetworking(false)
                        binding.networkView.root.visibility = View.GONE
                        binding.rvFilms.visibility = View.VISIBLE
                        it.data?.let { it1 -> reloadData(it1) }
                    }
                }
            }
        }
    }

    private fun showNetworking(loading: Boolean) {
        binding.apply {
        if (loading) {
                networkView.button.visibility = View.GONE
                networkView.ivNetwork.visibility = View.GONE
                networkView.tvMessage.visibility = View.GONE
                networkView.progressBar.visibility = View.VISIBLE
            } else {
                networkView.button.visibility = View.VISIBLE
                networkView.ivNetwork.visibility = View.VISIBLE
                networkView.tvMessage.visibility = View.VISIBLE
                networkView.progressBar.visibility = View.GONE
            }
        }
    }

    private fun onItemClick(film: FilmListItem) {

    }

    private fun reloadData(films: List<FilmListItem>) {
        adapter.clear()
        adapter.addData(films)
        adapter.notifyDataSetChanged()
    }
}