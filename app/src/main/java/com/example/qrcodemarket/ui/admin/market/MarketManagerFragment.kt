package com.example.qrcodemarket.ui.admin.market

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodemarket.BR
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.DetailMarket
import com.example.qrcodemarket.data.model.MarketAdapter
import com.example.qrcodemarket.data.model.getMarket
import com.example.qrcodemarket.data.model_new.dataMarketManage
import com.example.qrcodemarket.data.model_new.dataStatistical
import com.example.qrcodemarket.data.network.QRApi
import com.example.qrcodemarket.databinding.FragmentMarketManagerBinding
import com.example.qrcodemarket.databinding.FragmentStatisticalBinding
import com.example.qrcodemarket.ui.admin.statistical.StatisticalViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_market_manager.view.*

class MarketManagerFragment : Fragment() {

    companion object{
        fun newInstance(): MarketManagerFragment {
            return MarketManagerFragment()
        }
    }
    lateinit var mView: View
    private lateinit var binding: FragmentMarketManagerBinding

    val insertApi by lazy {
        QRApi.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_market_manager,container,false)
        mView = binding.root


        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        mView.btnAddMarket.setOnClickListener {
            findNavController().navigate(R.id.action_marketManagerFragment_to_addMarketFragment)
        }

        val viewModel = getMarket()
        setupBinding(viewModel)

        return mView
    }


    fun getMarket():MarketManageViewModel{
        val viewModel = ViewModelProviders.of(this).get(MarketManageViewModel::class.java)
        viewModel.getRecyclerListDataObserver().observe(this, Observer<dataMarketManage>{
            if(it != null){
                viewModel.setAdapterData(it.markets)
                Log.i("abc","abc"+it.markets)
            }else {
                Toast.makeText(context, "Error get data", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeAPICall()

        return viewModel
    }

    private fun setupBinding(viewModel: MarketManageViewModel) {
        binding.setVariable(BR.viewModel,viewModel)
        binding.executePendingBindings()
        binding.recycleMarket.apply {
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
            addItemDecoration(decoration)

        }
    }

}