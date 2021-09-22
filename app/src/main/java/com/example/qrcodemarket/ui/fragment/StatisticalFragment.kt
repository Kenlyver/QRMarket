package com.example.qrcodemarket.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcodemarket.R
import com.example.qrcodemarket.data.model.AccessAllUserAdapter
import com.example.qrcodemarket.data.model.getAccessAllUser
import com.example.qrcodemarket.data.network.QRApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_statistical.*
import kotlinx.android.synthetic.main.fragment_statistical.view.*
import java.util.*


class StatisticalFragment : Fragment() {


    lateinit var mView: View
    private lateinit var searchView: SearchView
    private lateinit var newData: List<getAccessAllUser.Data>
    var disposable: Disposable? = null
    val insertApi by lazy {
        QRApi.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_statistical, container, false)

        mView.imgBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        setHasOptionsMenu(true)
        val toolbar: Toolbar = mView.findViewById(R.id.toolbar_sta)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.show()

        getAllAccess()

        return mView.rootView
    }

    private fun showAll(dataAllAccess: List<getAccessAllUser.Data>) {

        recyAllAccess.layoutManager = LinearLayoutManager(context)
        recyAllAccess.adapter = AccessAllUserAdapter(dataAllAccess, object : AccessAllUserAdapter.OnAdapterListener {
            override fun onCLick(dataAllAccess: getAccessAllUser.Data) {
                val action =
                    StatisticalFragmentDirections.actionStatisticalFragmentToShowInfoUserFragment(dataAllAccess.fullName)
                findNavController().navigate(action)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistical, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun getAllAccess() {
        disposable = insertApi.accessAllUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    showAll(result.data)
                    newData = result.data
                    Log.i("abc", "abc: " + result.data.toString())
                },
                { error ->
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun getAccessData(fullName: String) {
        disposable = insertApi.allaccessbyfullname(fullName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    showAll(result.data)
                    Log.i("abc", "abc: " + result.data.toString())
                },
                { error ->
                    Log.i("abc", "abc: " + error.localizedMessage + error.message + error)
                }
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.All) {
            getAllAccess()
            return true
        } else if (item.getItemId() == R.id.Search) {
            searchView = item.actionView as SearchView
            searchView.setQueryHint("Tìm kiếm...")
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { getAccessData(it) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchText = newText!!.toLowerCase(Locale.getDefault())
                    if (searchText.isNotEmpty()) {

                    } else {

                    }
                    return false
                }
            })
            return true
        }
        return super.onOptionsItemSelected(item);
    }
}