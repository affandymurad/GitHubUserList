package am.tiket.github.main

import am.tiket.github.model.Responses
import am.tiket.github.network.Networks
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class MainPresenter(private val mView: MainContract.View): MainContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    private val composite = CompositeDisposable()

    override fun requestUserList(q: String, page :Int, perPage: Int) {
        mView.showLoading()
        Networks.service().getUserList(q, page, perPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Responses> {
                override fun onSubscribe(d: Disposable) {
                    composite.add(d)
                }

                override fun onNext(result: Responses) {
                    mView.showUsers(result.items)
                }

                override fun onError(e: Throwable) {
                    mView.dismissLoading()
                    mView.showError(e.localizedMessage)
                }

                override fun onComplete() {
                    mView.dismissLoading()
                }
            })
    }

}