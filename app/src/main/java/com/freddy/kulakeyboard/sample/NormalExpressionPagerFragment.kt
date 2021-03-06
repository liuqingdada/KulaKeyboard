package com.freddy.kulakeyboard.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freddy.kulakeyboard.library.util.DensityUtil
import com.freddy.kulakeyboard.sample.adapter.ExpressionListAdapter
import com.freddy.kulakeyboard.sample.bean.Expression
import com.freddy.kulakeyboard.sample.manager.ExpressionManager
import com.freddy.kulakeyboard.sample.widget.TopPaddingDecoration
import kotlinx.android.synthetic.main.fragment_normal_expression_pager.*
import java.io.Serializable

/**
 * @author  FreddyChen
 * @name
 * @date    2020/06/08 18:11
 * @email   chenshichao@outlook.com
 * @github  https://github.com/FreddyChen
 * @desc
 */
@Suppress("UNCHECKED_CAST")
class NormalExpressionPagerFragment : Fragment() {

    private lateinit var layoutManager: GridLayoutManager

    companion object {
        private const val TAG = "NormalExpressionPagerFragment"
        private const val KEY_EXPRESSION_LIST = "key_expression_list"

        fun newInstance(expressionList: ArrayList<Expression>): NormalExpressionPagerFragment {
            val args = Bundle()
            args.putSerializable(
                KEY_EXPRESSION_LIST,
                expressionList as Serializable?
            )
            val fragment = NormalExpressionPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var expressionList: ArrayList<Expression>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_normal_expression_pager, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun init() {
        val bundle = arguments!!
        expressionList = bundle.getSerializable(KEY_EXPRESSION_LIST) as ArrayList<Expression>
        recycler_view.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity, ExpressionManager.NORMAL_COUNT_BY_ROW)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
            TopPaddingDecoration(
                DensityUtil.dp2px(context!!, 24.0f)
            )
        )
        val adapter =
            ExpressionListAdapter(
                activity!!,
                expressionList!!
            )
        recycler_view.adapter = adapter
    }

    private var currentVisiblePercent = 0

    private fun setListeners() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                try {
                    val spanCount: Int = layoutManager.spanCount
                    val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()
                    if ((lastVisibleItemPosition + 1) % spanCount == 0 || (lastVisibleItemPosition + 2) % spanCount == 0) {
                        var view: View
                        if ((lastVisibleItemPosition + 1) % spanCount == 0) {
                            view = layoutManager.findViewByPosition(lastVisibleItemPosition)!!
                            view.visibility = View.GONE
                        }
                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - 1)!!
                        view.visibility = View.GONE
                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - spanCount)!!
                        view.visibility = View.GONE
                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - spanCount - 1)!!
                        view.visibility = View.GONE
                    }
                    val lastCompletelyVisibleItemPosition: Int = layoutManager.findLastCompletelyVisibleItemPosition()
                    if ((lastCompletelyVisibleItemPosition + 1) % spanCount == 0 || (lastCompletelyVisibleItemPosition + 2) % spanCount == 0) {
                        var view: View
                        if ((lastCompletelyVisibleItemPosition + 1) % spanCount == 0) {
                            view = layoutManager.findViewByPosition(lastVisibleItemPosition - spanCount * 2)!!
                            view.visibility = View.VISIBLE
                        }
                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - spanCount * 2 - 1)!!
                        view.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}