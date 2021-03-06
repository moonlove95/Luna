package com.mm.luna.ui.zhihu;

import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mm.luna.R;
import com.mm.luna.base.BaseFragment;
import com.mm.luna.bean.ZhiHuEntity;
import com.mm.luna.ui.adapter.ZhiHuAdapter;
import com.mm.luna.view.statusLayoutView.StatusLayoutManager;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ZMM on 2018/2/5.
 */

public class ZhiHuFragment extends BaseFragment<ZhiHuContract.Presenter> implements ZhiHuContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cl_content)
    ConstraintLayout content;
    @BindView(R.id.fab_top)
    FloatingActionButton fabTop;

    private List<ZhiHuEntity.StoriesBean> listData = new ArrayList<>();
    private String currentDate;
    private ZhiHuAdapter mAdapter;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private boolean isTop;
    private StatusLayoutManager statusLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zhihu;
    }

    @Override
    protected ZhiHuContract.Presenter initPresenter() {
        return new ZhiHuPresenter(this);
    }

    @Override
    protected void initView(View view) {
        statusLayoutManager = new StatusLayoutManager.Builder(content)
                .setOnStatusChildClickListener(v -> {
                    presenter.getTodayData();
                }).build();
        statusLayoutManager.showLoadingLayout();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        presenter.getTodayData();
        mAdapter = new ZhiHuAdapter(R.layout.item_zhihu, listData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnItemClickListener((adapter, v, position) -> {
            Log.d("", "onItemClick: " + position);
            startActivity(new Intent(mContext, ZhiHuDetailActivity.class).putExtra("id", listData.get(position).getId()));
        });
        mAdapter.setOnLoadMoreListener(() -> presenter.getBeforeData(currentDate, false), recyclerView);
        refreshLayout.setRefreshHeader(new PhoenixHeader(mContext));
        refreshLayout.setOnRefreshListener(refreshLayout -> presenter.getTodayData());

        setRVListener();
        fab.setOnClickListener(v -> selectData());
        fabTop.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));
    }

    /**
     * 监听recyclerView滚动
     */
    private void setRVListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isTop = !recyclerView.canScrollVertically(-1);
                fabTop.setVisibility(isTop ? View.GONE : View.VISIBLE);
                fab.setVisibility(isTop ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setData(ZhiHuEntity zhiHuEntity, boolean isClear) {
        currentDate = zhiHuEntity.getDate();
        if (isClear) {
            listData.clear();
            mAdapter.setNewData(zhiHuEntity.getStories());
        } else {
            mAdapter.addData(zhiHuEntity.getStories());
        }
        listData.addAll(zhiHuEntity.getStories());
        mAdapter.loadMoreComplete();
        refreshLayout.finishRefresh(true);
    }

    private void selectData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Calendar temp = Calendar.getInstance();
            temp.clear();
            temp.set(year, monthOfYear, dayOfMonth);

            String date = new SimpleDateFormat("yyyyMMdd").format(new Date(temp.getTimeInMillis() + 24 * 60 * 60 * 1000));
            Log.d("date======", date);
            presenter.getBeforeData(date, true);
            recyclerView.smoothScrollToPosition(0);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // 设置日历可选区间
        datePickerDialog.setMaxDate(Calendar.getInstance());
        Calendar minDate = Calendar.getInstance();
        minDate.set(2013, 5, 20);
        datePickerDialog.setMinDate(minDate);
        datePickerDialog.vibrate(false);
        datePickerDialog.show(mActivity.getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onFinish() {
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onError() {
        statusLayoutManager.showErrorLayout();
    }
}
