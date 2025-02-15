package com.example.nativeshop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.nativeshop.Model.SliderModel
import com.example.nativeshop.ViewModel.MainViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
@Preview
fun MainScreen() {
    val viewModel = MainViewModel()
    val bannersList = remember { mutableListOf<SliderModel>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchBanners()
        viewModel.banners.observeForever {
            bannersList.clear()
            bannersList.addAll(it)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(20.dp, 0.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom

        ) {
            Column {
                Text(text = "Welcome back", fontSize = 20.sp)
                Text(text = "Ido", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Row {
                Image(painter = painterResource(R.drawable.fav_icon), contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Image(painter = painterResource(R.drawable.search_icon), contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (!isLoading) {
                BannersCarousel(bannersList)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.purple))
            }
        }
    }
}

@Composable
fun BannersCarousel(banners: List<SliderModel>) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
            state = pagerState
        ) { page ->
            AsyncImage(
                model = banners[page].url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(14.dp, 0.dp),
                contentScale = ContentScale.Fit
            )
        }
        DotIndicator(bannersLength = banners.size, currentPage = pagerState.currentPage)
    }
}

@Composable
fun DotIndicator(bannersLength: Int, currentPage: Int) {
    LazyRow(modifier = Modifier.wrapContentWidth()) {
        items(bannersLength) { index ->
            Column(modifier = Modifier.padding(3.dp)) {
                Dot(currentPage, index)
            }
        }
    }
}

@Composable
fun Dot(isSelected: Int, index: Int) {
    Column(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color = colorResource(if (isSelected == index) R.color.purple else R.color.lightGrey))
    ) { }
}
