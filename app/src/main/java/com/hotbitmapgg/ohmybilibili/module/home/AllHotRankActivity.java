package com.hotbitmapgg.ohmybilibili.module.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hotbitmapgg.ohmybilibili.R;
import com.hotbitmapgg.ohmybilibili.adapter.AllRankRecyclerAdapter;
import com.hotbitmapgg.ohmybilibili.base.RxAppCompatBaseActivity;
import com.hotbitmapgg.ohmybilibili.entity.index.Index;
import com.hotbitmapgg.ohmybilibili.network.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/7 14:12
 * 100332338@qq.com
 * <p/>
 * 9个热门视频排行榜界面
 */
public class AllHotRankActivity extends RxAppCompatBaseActivity
{

    @Bind(R.id.recycle)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    List<Index.FuckList> indexs = new ArrayList<>();

    private Index mTypeIndex;

    public static final int TYPE_CARTOON = 1, TYPE_MUSIC = 3,
            TYPE_GAME = 4, TYPE_ENTERTAINMENT = 5,
            TYPE_TV_SERIES = 11, TYPE_ANIME = 13,
            TYPE_MOVIE = 23, TYPE_TECHNOLOGY = 36,
            TYPE_DANCE = 129, TYPE_FUNNY = 119;

    @Override
    public int getLayoutId()
    {

        return R.layout.activity_all_hot_rank;
    }

    @Override
    public void initViews(Bundle savedInstanceState)
    {

        showProgressBar();
    }

    private void showProgressBar()
    {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                mSwipeRefreshLayout.setRefreshing(true);
                getIndex();
            }
        }, 500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {

            @Override
            public void onRefresh()
            {

                getIndex();
            }
        });
    }

    @Override
    public void initToolBar()
    {

        mToolbar.setTitle("全区排行榜");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void getIndex()
    {

        RetrofitHelper.getIndexApi()
                .getIndex("android")
                .compose(this.<Index> bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Index>()
                {

                    @Override
                    public void call(Index index)
                    {

                        mTypeIndex = index;
                        finishGetTask();
                    }
                }, new Action1<Throwable>()
                {

                    @Override
                    public void call(Throwable throwable)
                    {

                        mSwipeRefreshLayout.post(new Runnable()
                        {

                            @Override
                            public void run()
                            {

                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
    }


    private void finishGetTask()
    {

        mSwipeRefreshLayout.setRefreshing(false);
        if (mTypeIndex != null)
        {
            Index.FuckList typeAnime = mTypeIndex.typeAnime;
            Index.FuckList typeCartoon = mTypeIndex.typeCartoon;
            Index.FuckList typeMusic = mTypeIndex.typeMusic;
            Index.FuckList typeDance = mTypeIndex.typeDance;
            Index.FuckList typeEntertainment = mTypeIndex.typeEntertainment;
            Index.FuckList typeFunny = mTypeIndex.typeFunny;
            Index.FuckList typeGame = mTypeIndex.typeGame;
            Index.FuckList typeMovie = mTypeIndex.typeMovie;
            Index.FuckList typeTechnology = mTypeIndex.typeTechnology;
            Index.FuckList typeTvSeries = mTypeIndex.typeTvSeries;


            indexs.add(typeAnime);
            indexs.add(typeCartoon);
            indexs.add(typeMusic);
            indexs.add(typeDance);
            indexs.add(typeEntertainment);
            indexs.add(typeFunny);
            indexs.add(typeGame);
            indexs.add(typeMovie);
            indexs.add(typeTechnology);
            indexs.add(typeTvSeries);

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AllHotRankActivity.this));
            AllRankRecyclerAdapter allRankRecyclerAdapter = new AllRankRecyclerAdapter(mRecyclerView,
                    indexs, AllHotRankActivity.this);
            mRecyclerView.setAdapter(allRankRecyclerAdapter);
        }
    }
}
