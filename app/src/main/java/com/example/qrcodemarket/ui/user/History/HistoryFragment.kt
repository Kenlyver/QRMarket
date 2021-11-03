package com.example.qrcodemarket.ui.user.History

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.qrcodemarket.BR
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model_new.dataHistory
import com.example.qrcodemarket.data.network.QRApi
import com.example.qrcodemarket.databinding.FragmentHistoryBinding
import com.example.qrcodemarket.ui.auth.AppPreferences
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*

import java.util.*


class HistoryFragment : Fragment() {

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    private lateinit var mView: View
    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var citizenId: String
    private lateinit var date: String
    private lateinit var disposable: Disposable

    val insertApi by lazy {
        QRApi.create()
    }
    init {
        historyAdapter = HistoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false
        )
        mView = binding.root

        activity?.bottomNavigation?.visibility = View.VISIBLE
        activity?.floatingActionButton?.visibility = View.VISIBLE

        setHasOptionsMenu(true)
        val toolbar: Toolbar = mView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()
        val viewModel = makeApiCall()
        setupBinding(viewModel)


        return mView
    }

    private fun setupBinding(viewModel: HistoryViewModel) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        binding.recycleHistory.apply {
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(context, VERTICAL)
            addItemDecoration(decoration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.All) {
            val viewModel = makeApiCall()
            setupBinding(viewModel)
            return true
        } else if (item.getItemId() == R.id.Search) {
            searchView = item.actionView as SearchView
            searchView.setQueryHint("Tìm kiếm...")
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    historyAdapter.filter.filter(query)
                    historyAdapter.notifyDataSetChanged()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchText = newText!!.toLowerCase(Locale.getDefault())
                    if (searchText.isNotEmpty()) {

                    } else {
                        historyAdapter.filter.filter(newText)
                        historyAdapter.notifyDataSetChanged()
                    }
                    return false
                }
            })
            return true
        }

        return super.onOptionsItemSelected(item);
    }

    fun makeApiCall(): HistoryViewModel {
        val viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        viewModel.getRecyclerListDataObserver().observe(this, Observer<dataHistory> {
            if (it != null) {
                viewModel.setAdapterData(it.access)
            } else {
                Toast.makeText(context, "Error get data", Toast.LENGTH_SHORT).show()
            }
        })
        historyAdapter = viewModel.getAdapter()
        citizenId = AppPreferences.citizenId
        val intCitizenId = citizenId.toInt()
        viewModel.makeAPICall(intCitizenId)

        return viewModel
    }
}