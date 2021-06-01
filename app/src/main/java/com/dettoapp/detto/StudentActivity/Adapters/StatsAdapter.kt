package com.dettoapp.detto.StudentActivity.Adapters

import android.R.attr.data
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.*
import com.dettoapp.detto.Models.githubModels.GithubAllModel
import com.dettoapp.detto.databinding.CommitDistributionBinding
import com.dettoapp.detto.databinding.IndividulCommitDistributionBinding
import kotlinx.coroutines.*


class StatsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<GithubAllModel>() {
        override fun areItemsTheSame(oldItem: GithubAllModel, newItem: GithubAllModel): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: GithubAllModel, newItem: GithubAllModel): Boolean {
            return false
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> CommitDistribution(CommitDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            1 -> CommitsHistory(CommitDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> IndividualCommitDistribution(IndividulCommitDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
           else -> CommitDistribution(CommitDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as CommitDistribution).bind(differ.currentList[0])
            1 -> (holder as CommitsHistory).bind(differ.currentList[0])
            2 -> (holder as IndividualCommitDistribution).bind(differ.currentList[0])
            else -> (holder as CommitDistribution).bind(differ.currentList[0])
        }
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.size == 0)
            0
        else
            3
    }

    inner class CommitDistribution(private val binding: CommitDistributionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allModel: GithubAllModel) {
            val pie = AnyChart.pie()

            pie.data(allModel.githubCommitDistributionModel.dataEntries)
            pie.title("GitHub Commit Distribution(${allModel.githubCommitDistributionModel.totalCommits} Commits)")
            pie.labels().position("outside")

            pie.legend().title().enabled(true)
            pie.legend().title()
                    .text("Individual Contributors")
                    .padding(0.0, 0.0, 10.0, 0.0)

            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.VERTICAL_EXPANDABLE)
                    .align(Align.CENTER);

            binding.pieView.setChart(pie)

            Log.d("DDFFS", allModel.toString())
        }
    }

    inner class IndividualCommitDistribution(private val binding: IndividulCommitDistributionBinding) :RecyclerView.ViewHolder(binding.root)
    {
        fun bind(allModel: GithubAllModel)
        {
            GlobalScope.launch {
                delay(300)
                withContext(Dispatchers.Main)
                {
                    binding.innerStatsRecyclerVIew.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = InnerStatsAdapter(allModel.commitHistory)
                    }
                }
            }

        }
    }

    inner class CommitsHistory(private val binding: CommitDistributionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allModel: GithubAllModel) {
            val cartesian = AnyChart.column();

            val column: Column = cartesian.column(allModel.totalCommitHistory)

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0.0)
                    .offsetY(5.0)
                    .format("{%Value}{groupsSeparator: } Commits")

            cartesian.animation(true)
            cartesian.title("Commit History")

            cartesian.yScale().minimum(0.0)

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("Weeks")
            cartesian.yAxis(0).title("Commits")

            binding.pieView.setChart(cartesian)
        }
    }
}