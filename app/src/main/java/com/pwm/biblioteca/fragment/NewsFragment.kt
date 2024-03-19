package com.pwm.biblioteca.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.utils.News
import com.pwm.biblioteca.databinding.FragmentNewsBinding

class NewsFragment: Fragment() {

    private lateinit var binding: FragmentNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)
        val news = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            arguments?.getParcelable("newsKey", News::class.java)
        }else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("newsKey")
        }

        if (news != null){
            binding.ivFotoNews.setImageBitmap(news.image)
            binding.tvDescrizioneNews.text = news.descrizione
            binding.tvTitoloNews.text = news.titolo
            binding.tvDataPublicazioneNews.text = news.dataPubblicazione
        }
    }
    private fun closeFragment(tag: String){
        val existingFragment = parentFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }
}