package inter.storymemet.view.story_detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import inter.storymemet.ListStoryAdapter
import inter.storymemet.ListStoryItem

import inter.storymemet.databinding.ActivityStoryDetailBinding
import inter.storymemet.dateFormatter

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var detailStory: ListStoryItem
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            detailStory = intent.getParcelableExtra(ListStoryAdapter.PARCEL_NAME, ListStoryItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            detailStory = intent.getParcelableExtra(ListStoryAdapter.PARCEL_NAME)!!
        }

        Glide.with(binding.root)
            .load(detailStory.photoUrl)
            .into(binding.ivStoryDt)
        binding.tvNamaDt.text = detailStory.name
        binding.tvDeskripsiDt.text = detailStory.description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvCreatedAt.text = dateFormatter(detailStory.createdAt)
        }
    }
}