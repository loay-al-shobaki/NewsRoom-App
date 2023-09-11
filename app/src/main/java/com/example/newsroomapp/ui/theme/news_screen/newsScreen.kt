package com.example.newsroomapp.ui.theme.news_screen

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsroomapp.component.CategoryTabRow
import com.example.newsroomapp.component.NewsArticleCard
import com.example.newsroomapp.component.NewsScreenTopBar
import com.example.newsroomapp.component.SearchAppBar
import com.example.newsroomapp.domain.model.Article
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,

)
@Composable
fun NewsScreen(
    state: NewsScreenState,
    onEvent: (NewsScreenEvent) -> Unit,
    onReadFullStoryButtonClicked: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val coroutinScope = rememberCoroutineScope()
    val categories = listOf(
        "General", "Business", "Health", "Science", "Sports", "Technology", "Entertainment"
    )
    val pagerSatet = rememberPagerState { categories.size }
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var shouldBottomSheetShow by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            sheetState = modalSheetState,
            onDismissRequest = { shouldBottomSheetShow = false },
            content = {
                state.selectedArticle?.let {
                    BottomSheetContent(
                        article = it,
                        onReadFullStoryButtonClicked = {
                            onReadFullStoryButtonClicked(it.url ?: "")
                            Log.d("loay2", "NewsScreen: ${it.url}")
                            coroutinScope.launch { modalSheetState.hide() }.invokeOnCompletion {
                                if (!modalSheetState.isVisible) shouldBottomSheetShow = false
                            }
                        })
                }
            })
    }
    LaunchedEffect(key1 = pagerSatet) {
        snapshotFlow { pagerSatet.currentPage }.collect { page ->
            onEvent(NewsScreenEvent.onCategoryChange(category = categories[page]))
        }
    }
    LaunchedEffect(key1 = Unit) {
        if (state.searchQuery.isNotEmpty()) {
            onEvent(NewsScreenEvent.OnSearchQueryChanged(searchQuery = state.searchQuery))
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Crossfade(targetState = state.isSearchBarVisible, label = "") { isVisible ->
            if (isVisible) {
                Column {
                    SearchAppBar(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = state.searchQuery,
                        onInputValueChange = {newValue->
                            onEvent(NewsScreenEvent.OnSearchQueryChanged(newValue))
                        },
                        onCloseIconClicked = { onEvent(NewsScreenEvent.OnCloseIconClicked) },
                        onSearchIconClicked = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )

                    NewsArticlesList(
                        state = state,
                        onCardClicked = { article ->
                            shouldBottomSheetShow = true
                            onEvent(NewsScreenEvent.onNewsCardClicked(article = article))
                        },
                        onRetryContent = {
                            onEvent(NewsScreenEvent.onCategoryChange(state.category))
                        })
                }

            } else {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        NewsScreenTopBar(scrollBehavior = scrollBehavior,
                            onSearchIconClicked = {
                            coroutinScope.launch {
                                delay(500)
                                focusRequester.requestFocus()
                            }
                            onEvent(NewsScreenEvent.OnSerchIconClicked)
                        })
                    }
                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        CategoryTabRow(
                            pagerState = pagerSatet,
                            category = categories,
                            onTabSelected = { index ->
                                coroutinScope.launch {
                                    pagerSatet.animateScrollToPage(index)
                                }
                            }

                        )
                        HorizontalPager(state = pagerSatet) { page ->
                            NewsArticlesList(
                                state = state,
                                onCardClicked = { article ->
                                    shouldBottomSheetShow = true
                                    onEvent(NewsScreenEvent.onNewsCardClicked(article = article))
                                },
                                onRetryContent = {
                                    onEvent(NewsScreenEvent.onCategoryChange(state.category))
                                })

                        }


                    }
                }

            }
        }
    }


}


@Composable
fun NewsArticlesList(
    state: NewsScreenState,
    onCardClicked: (Article) -> Unit,
    onRetryContent: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(state.articles) { article ->
            NewsArticleCard(article = article, onCardClicked = onCardClicked)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }

        if (state.error != null) {
            RetryContent(error = state.error, onRetry = onRetryContent)
        }
    }
}