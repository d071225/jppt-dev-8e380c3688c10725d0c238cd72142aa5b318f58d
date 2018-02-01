package com.hletong.hyc.ui.activity.cargoforecast2;

import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.BlockCargoPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.gallery.widget.PickerRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by dongdaqing on 2017/8/25.
 */

public class BlockCargo extends BlockBase<CargoForecastContract.BlockCargoPresenter> implements CargoForecastContract.BlockCargoView {
    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.measure_type)
    TextView mMeasureType;
    @BindView(R.id.transport_loss)
    TextView mTransportLoss;
    @BindView(R.id.volume)
    TextView mVolume;
    @BindView(R.id.cargoTotalValue)
    CommonInputView mCargoTotalValue;
    @BindView(R.id.bookRefType)
    CommonInputView mBookRefType;
    @BindView(R.id.cargoNumber)
    CommonInputView mCargoNumber;
    @BindView(R.id.cargoWeight)
    CommonInputView mCargoWeight;
    @BindView(R.id.gallery)
    PickerRecyclerView mRecyclerView;
    @BindView(R.id.unit)
    TextView mUnit;
    @BindView(R.id.carrier_model)
    TextView mCarrierModel;
    @BindView(R.id.carrier_length)
    TextView mCarrierLength;
    @BindView(R.id.hint)
    TextView hint;

    public BlockCargo(ViewStub viewStub) {
        super(viewStub);
    }

    @Override
    CargoForecastContract.BlockCargoPresenter createPresenter() {
        return new BlockCargoPresenter(this);
    }

    @Override
    protected String getTitle() {
        return "货物信息";
    }

    @Override
    public void showCargo(String cargoName, String measureType, String unit, String loss, String volume) {
        mTitleView.setText(cargoName);
        mMeasureType.setText(measureType);
        mUnit.setText(unit);
        mVolume.setText(volume);
    }

    @Override
    public void showCarrierModel(String model) {
        mCarrierModel.setText(model);
    }

    @Override
    public void showCarrierLength(String length) {
        mCarrierLength.setText(length);
    }

    @Override
    public void showImages(List<String> images) {
//        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                int oldHeight = oldBottom - oldTop;
//                int newHeight = bottom - top;
//                if (oldHeight != newHeight) {
//                    childViewVisibilityChanged();
//                    mRecyclerView.removeOnLayoutChangeListener(this);
//                }
//                Logger.d("oldHeight => " + oldHeight + ", newHeight => " + newHeight);
//            }
//        });
//        new PickerRecyclerView.Builder((Activity) mRecyclerView.getContext()).selectedPhotos(new ArrayList<>(images)).action(PreViewBuilder.PREVIEW).build(mRecyclerView);
//        setViewVisibility(mRecyclerView, View.VISIBLE);
    }

    @Override
    public void updateUnitMethod(int method) {
        mUnit.setInputType(method);
    }
}
