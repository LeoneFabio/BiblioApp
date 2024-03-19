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
import com.pwm.biblioteca.databinding.LayoutLibroBinding
import com.pwm.biblioteca.fragment.OpenBookFragment

class BookAdapter(
    private var bookList: List<Book>,
    private var fragmentManager: FragmentManager,
    private var bottomNavigation:MeowBottomNavigation
    ): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var filteredBookList: List<Book> = bookList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutLibroBinding.inflate(layoutInflater, parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = filteredBookList[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return filteredBookList.size
    }

    fun filterBooks(searchText: String) {
        filteredBookList = if (searchText.isNotEmpty()) {
            bookList.filter { book ->
                book.titolo?.contains(searchText, true) == true || book.autore?.contains(searchText, true) == true
            }
        } else {
            bookList
        }
        notifyDataSetChanged()
    }



    inner class BookViewHolder(private val binding: LayoutLibroBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.ibCopertina.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ibCopertina.setImageBitmap(book.copertina)
            binding.tvTitolo.text = (book.titolo)


            binding.ibCopertina.setOnClickListener {
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
                    .addToBackStack("OpenbookFragment()")
                    .commit()
                bottomNavigation.show(-1, false)
                bottomNavigation.clearAllCounts()
            }

        }

    }
}