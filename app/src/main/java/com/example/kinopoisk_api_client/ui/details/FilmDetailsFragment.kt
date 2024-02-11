package com.example.kinopoisk_api_client.ui.details

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kinopoisk_api_client.R
import com.example.kinopoisk_api_client.databinding.FragmentFilmDetailsBinding
import com.example.kinopoisk_api_client.utils.resource.Status
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FilmDetailsFragment : Fragment(), MenuProvider {

    private var _binding: FragmentFilmDetailsBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: FilmDetailsViewModel.Factory

    private val viewModel: FilmDetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(arguments?.getInt("id") ?: 0) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this)
        setupView()
        setupObserver()
        if (viewModel.isEmpty()) {
            viewModel.getFilm()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.networkView.button.setOnClickListener {
            viewModel.getFilm()
        }
    }

    private fun setupObserver() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.film.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        showNetworking(false)
                        binding.networkView.root.visibility = View.GONE
                        binding.llDetails.visibility = View.VISIBLE
                        binding.ivPoster.visibility = View.VISIBLE
                        val countries = TextUtils.join(", ", it.data!!.countries)
                        binding.tvCountries.text = Html.fromHtml(
                            getString(R.string.countries, countries),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                        binding.tvName.text = it.data.nameRu
                        binding.tvDesc.text = it.data.description
                        val genres = TextUtils.join(", ", it.data.genres)
                        binding.tvGenres.text = Html.fromHtml(
                            getString(R.string.genres, genres),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                        binding.pbPoster.visibility = View.VISIBLE
                        Picasso.get().load(it.data.posterUrl)
                            .into(binding.ivPoster, object : Callback {
                                override fun onSuccess() {
                                    if (_binding != null) {
                                        binding.pbPoster.visibility = View.GONE
                                    }
                                }

                                override fun onError(e: Exception?) {
                                    if (_binding != null) {
                                        binding.pbPoster.visibility = View.GONE
                                    }
                                }
                            })
                    }

                    Status.LOADING -> {
                        showNetworking(true)
                        binding.llDetails.visibility = View.GONE
                        binding.ivPoster.visibility = View.GONE
                        binding.networkView.root.visibility = View.VISIBLE
                    }

                    Status.ERROR -> {
                        showNetworking(false)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        binding.llDetails.visibility = View.GONE
                        binding.ivPoster.visibility = View.GONE
                        binding.networkView.root.visibility = View.VISIBLE
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

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        searchItem.isVisible = false
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
}