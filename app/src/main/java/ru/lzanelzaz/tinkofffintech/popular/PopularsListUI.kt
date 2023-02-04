package ru.lzanelzaz.tinkofffintech.popular

fun getRecyclerViewAdapter(state: PopularsViewModel.State): PopularsListAdapter {
    val myRecyclerViewAdapter = PopularsListAdapter()
    val dataset = if (state is PopularsViewModel.State.Loaded)
        state.films
    else
        emptyList()
    myRecyclerViewAdapter.submitList(dataset)
    return myRecyclerViewAdapter
}