package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.adapter.BookAdapter
import com.pwm.biblioteca.adapter.NewsAdapter
import com.pwm.biblioteca.databinding.FragmentHomeBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var mainActivity: MainActivity

    private lateinit var handler: Handler
    private lateinit var viewPager: ViewPager2
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private var currentPage = 0

    private var isFragmentAttached = false //utile per evitare che le query asincrone avvengano prima che il fragment sia attaccato alla mainActivity



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation().show(2,true)

        isFragmentAttached = true

        ClientNetwork.getTop10 { bookList ->
            if (isFragmentAttached) {
                if (bookList.isEmpty()) {
                    Toast.makeText(requireContext(), "Caricamento libri non andato a buon fine", Toast.LENGTH_SHORT).show()
                } else {
                    binding.rvTop10.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        bookAdapter = BookAdapter(bookList, parentFragmentManager, mainActivity.showBottomNavigation())
                        adapter = bookAdapter
                    }
                }
            }
        }

        ClientNetwork.getNuoveUscite { bookList ->
            if (isFragmentAttached) {
                if (bookList.isEmpty()) {
                    Toast.makeText(requireContext(), "Caricamento libri non andato a buon fine", Toast.LENGTH_SHORT).show()
                } else {
                    binding.rvNuoveUscite.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        bookAdapter = BookAdapter(bookList, parentFragmentManager, mainActivity.showBottomNavigation())
                        adapter = bookAdapter
                    }
                }
            }
        }

        ClientNetwork.getNews { newsList ->
            if (isFragmentAttached) {
                if (newsList.isEmpty()) {
                    Toast.makeText(requireContext(), "Caricamento news non andato a buon fine", Toast.LENGTH_SHORT).show()
                } else {
                    binding.viewPagerNews.apply {
                        adapter = NewsAdapter(newsList, parentFragmentManager, mainActivity.showBottomNavigation())
                        orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    }
                    binding.indicator.setViewPager(binding.viewPagerNews)

                    handler = Handler(Looper.getMainLooper())
                    viewPager = binding.viewPagerNews

                    timer = Timer()
                    timerTask = object : TimerTask() {
                        override fun run() {
                            handler.post {
                                viewPager.currentItem = currentPage
                                currentPage++
                                if (currentPage == newsList.size) {
                                    currentPage = 0
                                }
                            }
                        }
                    }
                    timer.schedule(timerTask, 1500, 1500)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Imposta il flag isFragmentAttached su false quando il frammento viene distrutto
        isFragmentAttached = false
        // Ferma il timer e annulla il timerTask
        if (::timerTask.isInitialized && ::timer.isInitialized) {
            timerTask.cancel()
            timer.cancel()
        }
    }
}
