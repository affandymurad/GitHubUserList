package am.tiket.github.main

import am.tiket.github.base.BaseView
import am.tiket.github.model.User

interface MainContract {
    interface View : BaseView<Presenter> {

        fun showUsers(users: ArrayList<User>)

        fun showError(err: String)

        fun showLoading()

        fun dismissLoading()
    }

    interface Presenter {
        fun requestUserList(q: String, page :Int, perPage: Int)
    }
}