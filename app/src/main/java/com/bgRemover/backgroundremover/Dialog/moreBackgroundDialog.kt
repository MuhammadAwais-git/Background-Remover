package com.bgRemover.backgroundremover.Dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bgRemover.backgroundremover.Background.pixabay.pixabayAdapter
import com.bgRemover.backgroundremover.MVVM.Hit
import com.bgRemover.backgroundremover.MVVMpixabay.DataViewModelPixabay
import com.bgRemover.backgroundremover.MVVMpixabay.MainRepositoryPixabay
import com.bgRemover.backgroundremover.MVVMpixabay.MyViewModelFactoryPixabay
import com.bgRemover.backgroundremover.databinding.MoreBackgroundDialogBinding

class moreBackgroundDialog(private val mContext: Context,val owner: ViewModelStoreOwner,val mLifecycleOwner : LifecycleOwner, val itemClickListener: pixabayAdapter.onItemClickListener): Dialog(mContext){

    private lateinit var binding: MoreBackgroundDialogBinding
    private lateinit var viewModel: DataViewModelPixabay
    private lateinit var imgListpixabay: ArrayList<Hit>

    var madapter: pixabayAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = MoreBackgroundDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(owner, MyViewModelFactoryPixabay(MainRepositoryPixabay()))[DataViewModelPixabay::class.java]
        viewModel.getData("Nature")
        viewModel.mLiveData.observe(mLifecycleOwner, Observer { mList ->
            Log.d("TAG", "onCreate: list ${mList.hits}")
            imgListpixabay=mList.hits as ArrayList<Hit>
            setpixabayAdapter()
        })

        binding.txtMainsearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.txtMainsearch.text!!.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Address", Toast.LENGTH_SHORT).show()
                } else {
                    val view = this.currentFocus
                    if (view != null) {
                        val imm: InputMethodManager =
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    val q=binding.txtMainsearch.text.toString()
                    viewModel.getData(q)


                }

                return@OnEditorActionListener true
            }
            false
        })
     /*   val q=binding.txtMainsearch.text.toString()
            Log.d("TAG", "onCreate: text ${binding.txtMainsearch.text.toString()}")
            viewModel.getData(q)

        viewModel.mLiveData.observe(mLifecycleOwner, Observer { mList ->
            Log.d("TAG", "onCreate: list ${mList.hits}")
            imgListpixabay=mList.hits as ArrayList<Hit>
            setpixabayAdapter()
        })*/




    }
    private fun setpixabayAdapter() {

        madapter =
            pixabayAdapter(context,imgListpixabay, object : pixabayAdapter.onItemClickListener {
                override fun onitemclick(model: Hit) {
                    itemClickListener.onitemclick(model)
                }

            })
        binding.morebgimgRecycler.apply {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.morebgimgRecycler.adapter = madapter
        }
    }

}