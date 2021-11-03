package com.example.qrcodemarket.ui.admin.statistical

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.qrcodemarket.R
import com.example.qrcodemarket.BR
import com.example.qrcodemarket.data.model_new.dataStatistical
import com.example.qrcodemarket.databinding.FragmentStatisticalBinding
import kotlinx.android.synthetic.main.fragment_statistical.view.*



class StatisticalFragment : Fragment() {


    lateinit var mView: View
    private lateinit var searchView: SearchView
    private val adapter = StatisticalAdapter()
    private lateinit var viewModel: StatisticalViewModel
    private lateinit var binding:FragmentStatisticalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_statistical,container,false)
        mView = binding.root

        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        setHasOptionsMenu(true)
        val toolbar: Toolbar = mView.findViewById(R.id.toolbar_sta)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()


        val viewModel = getDataAccess()
        setupBinding(viewModel)

        return mView
    }


    private fun setupBinding(viewModel: StatisticalViewModel) {
        binding.setVariable(BR.viewModel,viewModel)
        binding.executePendingBindings()
        binding.recyAllAccess.apply {
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(context,VERTICAL)
            addItemDecoration(decoration)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistical, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun getDataAccess():StatisticalViewModel{
        val viewModel = ViewModelProviders.of(this).get(StatisticalViewModel::class.java)
        viewModel.getRecyclerListDataObserver().observe(this, Observer<dataStatistical>{
            if(it != null){
                viewModel.setAdapterData(it.access)
                Log.i("abc","abc"+it.access)
            }else {
                Toast.makeText(context, "Error get data", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeAPICall()

        return viewModel
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.getItemId() == R.id.All) {
//            getAllAccess()
//            return true
//        } else if (item.getItemId() == R.id.Search) {
//            searchView = item.actionView as SearchView
//            searchView.setQueryHint("Tìm kiếm...")
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    query?.let { getAccessData(it) }
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    val searchText = newText!!.toLowerCase(Locale.getDefault())
//                    if (searchText.isNotEmpty()) {
//
//                    } else {
//
//                    }
//                    return false
//                }
//            })
//            return true
//        }
//        return super.onOptionsItemSelected(item);
//    }
}