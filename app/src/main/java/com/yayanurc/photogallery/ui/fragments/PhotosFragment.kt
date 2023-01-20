package com.yayanurc.photogallery.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yayanurc.photogallery.R
import com.yayanurc.photogallery.data.local.*
import com.yayanurc.photogallery.databinding.FragmentPhotosBinding
import com.yayanurc.photogallery.navigator.AppNavigator
import com.yayanurc.photogallery.navigator.Screens
import com.yayanurc.photogallery.ui.MainActivity
import com.yayanurc.photogallery.ui.adapters.PhotoAdapter
import com.yayanurc.photogallery.ui.adapters.PhotoLoadStateAdapter
import com.yayanurc.photogallery.utils.GridSpacingItemDecoration
import com.yayanurc.photogallery.utils.removeItemDecorations
import com.yayanurc.photogallery.ui.viewmodels.PhotosViewModel
import com.yayanurc.photogallery.utils.ViewType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment that displays all photos from Unsplash API with pagination.
 * It includes :
 * - Search by keyword Capability
 * - List and Grid Display Style
 */
@AndroidEntryPoint
class PhotosFragment: Fragment(R.layout.fragment_photos) {

    @Inject lateinit var navigator: AppNavigator
    @Inject lateinit var sharedPrefDataSource: SharedPrefDataSource

    private val viewModel by viewModels<PhotosViewModel>()

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private var currentViewType: Int? = null

    private lateinit var searchMenuItem: MenuItem
    private var mSearchString: String? = null

    companion object {
        private const val CURRENT_VIEW_TYPE = "currentViewType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                activity?.finish()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        currentViewType = sharedPrefDataSource.getSharedPref(CURRENT_VIEW_TYPE, SharedPrefTypes.INTEGER.ordinal) as Int
        if (currentViewType == -1) {
            sharedPrefDataSource.setSharedPref(CURRENT_VIEW_TYPE, ViewType.LIST.ordinal, SharedPrefTypes.INTEGER.ordinal)
            currentViewType = sharedPrefDataSource.getSharedPref(CURRENT_VIEW_TYPE, SharedPrefTypes.INTEGER.ordinal) as Int
        }

        val adapter = PhotoAdapter { photo, _ ->
            navigator.navigateTo(Screens.PHOTO_DETAIL, photo)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_photos, menu)
                val switchMenuItem = menu.findItem(R.id.menu_switch_view)
                searchMenuItem = menu.findItem(R.id.menu_search)

                searchMenuItem.setOnActionExpandListener(object :
                    MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                        switchMenuItem?.isVisible = false
                        return true
                    }
                    override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                        switchMenuItem?.isVisible = true
                        return true
                    }
                })

                if (currentViewType == ViewType.GRID.ordinal) {
                    switchMenuItem?.icon = AppCompatResources.getDrawable(
                        requireContext(), R.drawable.ic_list_white_24dp
                    ) as Drawable
                } else {
                    switchMenuItem?.icon = AppCompatResources.getDrawable(
                        requireContext(), R.drawable.ic_grid_white_24dp
                    ) as Drawable
                }
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val id = menuItem.itemId
                if (id == R.id.menu_search) {
                    // focus the SearchView
                    val searchView = menuItem.actionView as SearchView
                    if (mSearchString != null && mSearchString?.isNotEmpty() == true) {
                        searchMenuItem.expandActionView()
                        searchView.setQuery(mSearchString, false)
                    }
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (query != null) {
                                binding.rvPhotos.scrollToPosition(0)
                                viewModel.searchPhotos(query)
                                searchView.clearFocus()
                            }
                            return true
                        }
                        override fun onQueryTextChange(newText: String?): Boolean {
                            return true
                        }
                    })
                } else {
                    if (currentViewType == ViewType.GRID.ordinal) {
                        menuItem.icon = AppCompatResources.getDrawable(
                            requireContext(), R.drawable.ic_grid_white_24dp
                        ) as Drawable
                        currentViewType = ViewType.LIST.ordinal
                    } else {
                        menuItem.icon = AppCompatResources.getDrawable(
                            requireContext(), R.drawable.ic_list_white_24dp
                        ) as Drawable
                        currentViewType = ViewType.GRID.ordinal
                    }
                    sharedPrefDataSource.setSharedPref(CURRENT_VIEW_TYPE, currentViewType!!, SharedPrefTypes.INTEGER.ordinal)
                    updateRecyclerViewLayoutManager()
                    return true
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentPhotosBinding.bind(view)

        binding.apply {
            rvPhotos.setHasFixedSize(true)
            rvPhotos.itemAnimator = null
            // To simulate bad internet connection in emulator go to 3 dots -> Cellular -> Set up Network type to GPRS
            rvPhotos.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter { adapter.retry() },
                footer = PhotoLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        viewModel.currentQuery.observe(viewLifecycleOwner) {
            mSearchString = it
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvPhotos.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    rvPhotos.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
        updateRecyclerViewLayoutManager()
    }

    private fun updateRecyclerViewLayoutManager() {
        binding.apply {
            rvPhotos.layoutManager = if (currentViewType == ViewType.GRID.ordinal) {
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            } else {
                LinearLayoutManager(requireContext())
            }
            if (currentViewType == ViewType.GRID.ordinal) {
                rvPhotos.addItemDecoration(
                    GridSpacingItemDecoration(8)
                )
            } else {
                rvPhotos.removeItemDecorations()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}