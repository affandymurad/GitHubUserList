package am.tiket.github.main

import am.tiket.github.R
import am.tiket.github.adapter.UserAdapter
import am.tiket.github.model.User
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainContract.View{

    private var mPresenter: MainContract.Presenter? = null
    private val adapter by lazy { UserAdapter() }
    private var userList = ArrayList<User>()
    private var isFinish = false

    private var searchKey = ""
    private var page = 1
    private var perPage = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPresenter(MainPresenter(this))

        val layoutManager = LinearLayoutManager(this)
        rvUser.layoutManager = layoutManager
        rvUser.adapter = adapter

        srUser.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))
        srUser.setOnRefreshListener {
            etUser.onEditorAction(EditorInfo.IME_ACTION_DONE)
            if (userList.size != 0) cleanRefreshItem()
            page = 1
            if (!searchKey.isBlank()) mPresenter?.requestUserList(searchKey, page, perPage)
            else showError(getString(R.string.error))
        }

        etUser.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchKey = s.toString()
            }
        })

        etUser.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                etUser.onEditorAction(EditorInfo.IME_ACTION_DONE)
                if (userList.size != 0) cleanRefreshItem()
                page = 1
                if (!searchKey.isBlank()) mPresenter?.requestUserList(searchKey, page, perPage)
                else showError(getString(R.string.error))
                return@OnEditorActionListener true
            }
            false
        })

        rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page++
                    if (!isFinish) mPresenter?.requestUserList(searchKey, page, perPage)
                }
            }
        })

    }

    private fun cleanRefreshItem() {
        userList.clear()
        adapter.userList = userList
        adapter.notifyDataSetChanged()
    }

    override fun showUsers(users: ArrayList<User>) {
            isFinish = users.size < perPage
            userList.addAll(users)
            adapter.userList = userList
            adapter.notifyDataSetChanged()

            if (isFinish && userList.size == 0) {
                rvUser.visibility = View.GONE
                tvError.visibility = View.VISIBLE
            } else {
                rvUser?.visibility = View.VISIBLE
                tvError?.visibility = View.GONE
            }
    }

    override fun showError(err: String) {
        srUser.isRefreshing = false
        val snack = Snackbar.make(clUser, err, Snackbar.LENGTH_SHORT)
        val text = snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snack.show()
    }

    override fun showLoading() {
        srUser.isRefreshing = true
    }

    override fun dismissLoading() {
        srUser.isRefreshing = false
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }
}