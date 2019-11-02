package pl.expert.mobilewzr.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:visibleIfSuccessState")
fun visibleIfSuccessState(view: View, resourceState: ResourceState<*>) {
    if (resourceState is ResourceState.Success)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("app:visibleIfLoadingState")
fun visibleIfLoadingState(view: View, resourceState: ResourceState<*>) {
    if (resourceState is ResourceState.Loading)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("app:visibleIfErrorState")
fun visibleIfErrorState(view: View, resourceState: ResourceState<*>) {
    if (resourceState is ResourceState.Error)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("app:goneIfLoadingState")
fun goneIfLoadingState(view: View, resourceState: ResourceState<*>) {
    if (resourceState is ResourceState.Loading)
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}