package com.example.nativeshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.nativeshop.Model.CategoryModel
import com.example.nativeshop.Model.ItemModel
import com.example.nativeshop.Model.SliderModel
import com.example.nativeshop.R
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
    val categoriesList = remember { mutableListOf<CategoryModel>() }
    val itemsList = remember { mutableListOf<ItemModel>() }

    var isLoadingCategories by remember { mutableStateOf(true) }
    var isLoadingBanners by remember { mutableStateOf(true) }
    var isLoadingItems by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchBanners()
        viewModel.banners.observeForever {
            bannersList.clear()
            bannersList.addAll(it)
            isLoadingBanners = false

        }
        viewModel.fetchCategories()
        viewModel.categories.observeForever {
            categoriesList.clear()
            categoriesList.addAll(it)
            isLoadingCategories = false
        }
        viewModel.fetchRecommended()
        viewModel.recommended.observeForever {
            itemsList.clear()
            itemsList.addAll(it)
            isLoadingItems = false
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
        if (!isLoadingBanners && !isLoadingCategories && !isLoadingItems) {
            BannersCarousel(bannersList)
            CategoriesItems(categoriesList)
            ListItems(itemsList)

        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
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
            .fillMaxWidth(),
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
    Spacer(modifier = Modifier.height(12.dp))
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

@Composable
fun CategoriesItems(categoriesList: List<CategoryModel>) {
    val currentCategory = remember { mutableIntStateOf(0) }

    fun selectItem(index: Int) {
        currentCategory.intValue = index
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Categories", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = "See All")
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(categoriesList) { item ->
                CategoryItem(item, currentCategory, { selectItem(item.id) })
            }
        }
    }
}

@Composable
fun CategoryItem(item: CategoryModel, currentCategory: MutableIntState, onItem: () -> Unit) {
    val isSelected = item.id == currentCategory.intValue

    Row(
        modifier = Modifier
            .widthIn(min = 50.dp)
            .height(50.dp)
            .background(
                color = if (isSelected) colorResource(R.color.purple) else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp, 0.dp)
            .clickable { onItem() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (isSelected) {
            Text(item.title, color = Color.White, fontSize = 16.sp)
        }
        AsyncImage(
            model = item.picUrl,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .padding(6.dp, 0.dp)
        )
    }
}

@Composable
fun ListItems(items: List<ItemModel>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Recommendation", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = "See All")
    }
    LazyVerticalGrid(
        modifier = Modifier.padding(8.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(items.size) { index ->
            Item(items[index])
        }
    }
}

@Composable
fun Item(item: ItemModel) {

    Column(
        modifier = Modifier
            .size(220.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.picUrl[0],
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .padding(12.dp)
                .clickable { }
        )
        Column(
            modifier = Modifier
                .width(160.dp)
                .padding(0.dp, 6.dp)
        ) {
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Image(painter = painterResource(R.drawable.star), contentDescription = null)
                    Text(item.rating.toString())
                }
                Text(
                    "$${item.price}",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.purple)
                )
            }
        }
    }
}