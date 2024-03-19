package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.adapter.BookAdapter
import com.pwm.biblioteca.databinding.FragmentLibriCartaceiBinding
import com.pwm.biblioteca.server.ClientNetwork
import android.widget.SearchView.OnQueryTextListener
import com.pwm.biblioteca.activity.MainActivity


class PaperBooksFragment: Fragment() {
    private lateinit var binding: FragmentLibriCartaceiBinding
    private lateinit var bookAdapter: BookAdapter
    private var bookList: List<Book> = emptyList()
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibriCartaceiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)
        mainActivity = activity as MainActivity

        ClientNetwork.getPaperBooks() { fetchedBookList ->
            bookList = fetchedBookList
            if (bookList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Caricamento libri non andato a buon fine",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                binding.rvGriglia.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    bookAdapter = BookAdapter(bookList, parentFragmentManager, mainActivity.showBottomNavigation())
                    adapter = bookAdapter
                }
            }
        }

        val genres = listOf("Avventura", "Fiaba", "Giallo", "Horror", "Romantico")

        val genreAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genereSpinner.adapter = genreAdapter


        binding.genereSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = parent?.getItemAtPosition(position) as String
                when (selectedGenre) {
                    "Avventura", "Fiaba", "Giallo", "Horror", "Romantico" -> ClientNetwork.getPaperBooks(
                        selectedGenre
                    ) { bookList ->
                        if (bookList.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Caricamento libri non andato a buon fine",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            binding.searchView.setQuery("", false)

                            binding.rvGriglia.apply {
                                layoutManager = GridLayoutManager(requireContext(), 2)
                                bookAdapter = BookAdapter(bookList, parentFragmentManager, mainActivity.showBottomNavigation())
                                adapter = bookAdapter
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nessuna particolare implementazione
            }
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.trim() ?: ""
                if (::bookAdapter.isInitialized) {
                    bookAdapter.filterBooks(searchText)
                }
                return true
            }
        })

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