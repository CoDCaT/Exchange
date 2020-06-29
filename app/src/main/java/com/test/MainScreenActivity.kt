package com.test

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.test.adapter.IRateListListener
import com.test.adapter.RateListAdapter
import com.test.adapter.RateListItemVH
import com.test.api.model.Rates
import com.test.di.ActivityScope
import com.test.utils.DialogManagerUtil
import com.test.utils.onClick
import com.test.utils.visibleOrGone
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main_screen.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@ActivityScope
class MainScreenActivity : DaggerAppCompatActivity(), MainScreenContract.View {

    @Inject
    lateinit var presenter: MainScreenContract.Presenter

    @Inject
    lateinit var adapter: Provider<RateListAdapter>

    @Inject
    @field:Named("from")
    lateinit var adapterFrom: RateListAdapter

    @Inject
    @field:Named("to")
    lateinit var adapterTo: RateListAdapter

    private lateinit var viewPagerToListener: ViewPager2.OnPageChangeCallback
    private lateinit var viewPagerFromListener: ViewPager2.OnPageChangeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        initRateFrom()
        initRateTo()

        buttonExchange.onClick {
            DialogManagerUtil.showConfirmExchangeDialog(this) {
                presenter.onExchangeConfirm()
            }
        }
        presenter.onViewInitialized()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    private fun initRateTo() {
        var scroll = false
        viewPagerToListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (scroll) {
                    presenter.onValueToChanged(position)
                    scroll = false
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                scroll = true
            }
        }
        adapterTo.apply {
            setListener(object : IRateListListener {
                override fun onExchangeValueChanged(value: Double) {
                    presenter.onExchangeValueChangedTo(value)
                }
            })
        }
        viewPagerTo.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            setUpTransformer()
            adapter = adapterTo
            registerOnPageChangeCallback(viewPagerToListener)
        }
    }

    private fun initRateFrom() {
        var scroll = false
        viewPagerFromListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (scroll) {
                    presenter.onValueFromChanged(position)
                    scroll = false
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                scroll = true
            }
        }
        adapterFrom.apply {
            setListener(object : IRateListListener {
                override fun onExchangeValueChanged(value: Double) {
                    presenter.onExchangeValueChangedFrom(value)
                }
            })
        }
        viewPagerFrom.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            setUpTransformer()
            adapter = adapterFrom
            registerOnPageChangeCallback(viewPagerFromListener)
        }
    }

    override fun setFromRateList(rateList: List<Rates>) {
        updateCurrentRate(rateList[viewPagerFrom.currentItem])
        adapterFrom.setRateList(rateList)
    }

    override fun setToRateList(rateList: List<Rates>) {
        adapterTo.setRateList(rateList)
    }

    override fun showProgress(show: Boolean) {
        progress.visibleOrGone(show)
    }

    private fun updateCurrentRate(currentRare: Rates) {
        textCurrentRate.text =
            "1 ${currentRare.name} = ${currentRare.exchangeValue} ${currentRare.exchangeName}"
    }

    override fun updateToRates(toList: MutableList<Rates>) {
        viewPagerTo.updateCurrentItem(toList[viewPagerTo.currentItem])
    }

    override fun showSuccessState() {
        DialogManagerUtil.showSuccessExchangeAlert(this)
    }

    override fun showEqualsRatesState() {
        DialogManagerUtil.showErrorEqualsRateAlert(this)
    }

    override fun showTotalValueState() {
        DialogManagerUtil.showErrorTotalValueAlert(this)
    }

    override fun updateFromRates(fromList: MutableList<Rates>) {
        updateCurrentRate(fromList[viewPagerFrom.currentItem])
        viewPagerFrom.updateCurrentItem(fromList[viewPagerFrom.currentItem])
    }

    private fun ViewPager2.setUpTransformer() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
        this.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
    }

    private fun ViewPager2.updateCurrentItem(item: Rates) {
        val rv = this[0] as RecyclerView
        (rv.layoutManager as LinearLayoutManager).findViewByPosition(currentItem)
            ?.let {
                val holder = rv.getChildViewHolder(it) as RateListItemVH
                holder.bind(item)
            }
    }
}
