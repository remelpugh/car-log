package com.dabay6.android.apps.carlog.ui.fuel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment.OnEntityListListener;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryListFragment;
import com.utils.android.ui.BaseFragmentActivity;

import java.util.List;

/**
 * FuelHistoryListActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class FuelHistoryListActivity extends BaseFragmentActivity implements OnEntityListListener {
    //    /**
    //     * {@inheritDoc}
    //     */
    //    @Override
    //    public Long getVehicleId() {
    //        return null;
    //    }

    @Override
    public void onEntityAdd() {
    }

    /**
     *
     * @param ids
     */
    @Override
    public void onEntityDelete(final List<Long> ids) {
    }

    @Override
    public void onEntitySelected(int position, long id) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.fragment_container, FuelHistoryListFragment.newInstance(), "fuelhistory");
            transaction.commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        return R.layout.activity_fragment_container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResource() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isHomeAsUpEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTitleEnabled() {
        return true;
    }
}