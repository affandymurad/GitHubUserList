package am.tiket.github.adapter

import am.tiket.github.R
import am.tiket.github.model.User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var userList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class ViewHolder (view: View) :RecyclerView.ViewHolder(view) {
        val ivUser = view.ivUser
        val tvUser = view.tvUser

        fun bind (user: User) {
            tvUser.text = user.login
            Glide.with(itemView.context).load(user.avatarUrl).apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder)).into(ivUser)
        }
    }

}