package com.rajit.samachaar.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import coil.load
import com.rajit.samachaar.R
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.ActivityDetailsBinding
import com.rajit.samachaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    var isFavourites = false
    var savedArticleId = -1

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = args.article

        setupData(article)

        mainViewModel.favouriteArticles.observe(this, { listOfArticles ->
            for (item in listOfArticles) {
                if (item.article.url == article.url) {
                    isFavourites = true
                    savedArticleId = item.id
                    binding.saveToFavourites.setImageResource(R.drawable.ic_bookmark_checked)
                }
            }
        })

        binding.saveToFavourites.setOnClickListener {
            checkIfFavourite()
        }

    }

    private fun checkIfFavourite() {
        Toast.makeText(applicationContext, "isFav: $isFavourites", Toast.LENGTH_SHORT).show()
        if (isFavourites) {
            isFavourites = false
            mainViewModel.deleteFavouriteArticle(
                FavouriteArticlesEntity(
                    savedArticleId,
                    args.categoryName,
                    args.article
                )
            )
            binding.saveToFavourites.setImageResource(R.drawable.ic_bookmark_unchecked)
        } else {
            mainViewModel.insertFavourites(
                FavouriteArticlesEntity(
                    0,
                    args.categoryName,
                    args.article
                )
            )
            binding.saveToFavourites.setImageResource(R.drawable.ic_bookmark_checked)
        }
    }

    private fun setupData(article: Article) {

        if (article.urlToImage == null) {
            binding.articleThumbnail.setImageResource(R.drawable.error_placeholder)
        } else {
            binding.articleThumbnail.load(article.urlToImage) {
                error(R.drawable.error_placeholder)
                placeholder(R.drawable.article_thumbnail_placeholder)
            }
        }

        binding.articleTitleTv.text = article.title
        binding.articleCategoryTv.text = args.categoryName

        val publishedAtFull = article.publishedAt
        if (publishedAtFull != null) {
            val publishedSplit = publishedAtFull.split("T")
            val publishedSplit2 = publishedSplit[1].split("Z")

            binding.authorTv.text =
                "${article.source?.name} / ${publishedSplit[0]}, ${publishedSplit2[0]} UTC"
        }

        binding.articleDescriptionTv.text = article.description

        binding.learnMoreBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(article.url)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}