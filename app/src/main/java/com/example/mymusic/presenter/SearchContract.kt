package com.example.mymusic.presenter

import com.example.mymusic.data.Song

/**
 * 搜索页面契约接口
 */
interface SearchContract {
    interface View : BaseView {
        fun showSearchHistory(history: List<String>)
        fun showRecommendedSongs(songs: List<Song>)
        fun showHotSearchList(songs: List<Song>)
        fun showHotSongList(songs: List<Song>)
        fun showSearchResults(songs: List<Song>)
        fun playSong(song: Song)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onSearchTextChanged(query: String)
        fun onSearchHistoryItemClick(query: String)
        fun onClearSearchHistory()
        fun onSongClick(songId: String)
        fun onQuickEntryClick(entryType: String)
    }
}
