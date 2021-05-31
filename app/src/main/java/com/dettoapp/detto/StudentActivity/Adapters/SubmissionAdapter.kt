package com.dettoapp.detto.StudentActivity.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dettoapp.detto.Models.githubModels.CommitActivity
import com.dettoapp.detto.Models.githubModels.SubmissionModel
import com.dettoapp.detto.databinding.SubmissionViewHolderBinding

class SubmissionAdapter(private val onShare : (String)->Unit) : RecyclerView.Adapter<SubmissionAdapter.SubmissionRecyclerViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<SubmissionModel>() {
        override fun areItemsTheSame(oldItem: SubmissionModel, newItem: SubmissionModel): Boolean {
            return oldItem.filelink == oldItem.filelink
        }

        override fun areContentsTheSame(oldItem: SubmissionModel, newItem: SubmissionModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionAdapter.SubmissionRecyclerViewHolder {
        return SubmissionRecyclerViewHolder(SubmissionViewHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SubmissionAdapter.SubmissionRecyclerViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class SubmissionRecyclerViewHolder(private val binding: SubmissionViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(submissionModel: SubmissionModel) {

            binding.submissiontitle.text = submissionModel.filename
            binding.submissionlink.text = submissionModel.filelink

            binding.submissionlinkshare.setOnClickListener {
               onShare(submissionModel.filelink)
            }
        }
    }

//    fun onClassLinkShare(link: String,activity: CommitActivity) {
//        ShareCompat.IntentBuilder.from(requ)
//                .setText(link)
//                .setType("text/plain")
//                .setChooserTitle("Game Details")
//                .startChooser()
//    }

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}
