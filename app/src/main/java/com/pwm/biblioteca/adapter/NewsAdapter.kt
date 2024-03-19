package com.pwm.biblioteca.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.pwm.biblioteca.utils.News
import com.pwm.biblioteca.R
import com.pwm.biblioteca.databinding.LayoutImmagineNewsBinding
import com.pwm.biblioteca.fragment.NewsFragment

class NewsAdapter(private var newsList: List<News>, private var fragmentManager: FragmentManager, private var bottomNavigation: MeowBottomNavigation): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutImmagineNewsBinding.inflate(layoutInflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    inner class NewsViewHolder(private var binding: LayoutImmagineNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News){
            binding.newsImage.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.newsImage.setImageBitmap(news.image)

            binding.newsImage.setOnClickListener{
                val newsFragment = NewsFragment()
                val bundle = Bundle().apply {
                    putParcelable("newsKey", news)
                }
                newsFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        newsFragment
                    )
                    .addToBackStack("NewsFragment()")
                    .commit()
                bottomNavigation.show(-1, false)
                bottomNavigation.clearAllCounts()
            }
        }

    }
}