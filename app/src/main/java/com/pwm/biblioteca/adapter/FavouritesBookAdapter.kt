package com.pwm.biblioteca.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.R
import com.pwm.biblioteca.databinding.LayoutLibroPreferitiBinding
import com.pwm.biblioteca.fragment.OpenBookFragment

class FavouritesBookAdapter (
    private var favList: List<Book>,
    private var fragmentManager: FragmentManager,
    private var bottomNavigation: MeowBottomNavigation
): RecyclerView.Adapter<FavouritesBookAdapter.FavouritesBookViewHolder>() {

    inner class FavouritesBookViewHolder(private val binding: LayoutLibroPreferitiBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book){
            binding.ibCopertina.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ibCopertina.setImageBitmap(book.copertina)
            binding.tvAutoreLibroPreferiti.text = "Autore: ${book.autore}"
            binding.tvNomeLibroPreferiti.text = "Titolo: ${book.titolo}"

            binding.ibCopertina.setOnClickListener{
                val openBookFragment = OpenBookFragment()
                val bundle = Bundle().apply {
                    putParcelable("bookKey", book)
                }
                openBookFragment.arguments = bundle
                fragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        openBookFragment
                    )
                    .addToBackStack("OpenBookFragment()")
                    .commit()
                bottomNavigation.show(-1, false)
                bottomNavigation.clearAllCounts()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutLibroPreferitiBinding.inflate(layoutInflater, parent, false)
        return FavouritesBookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: FavouritesBookViewHolder, position: Int) {
        val book = favList[position]
        holder.bind(book)
    }
}