package com.dettoapp.detto.StudentActivity.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.core.cartesian.series.Column
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.*
import com.dettoapp.detto.databinding.CommitDistributionBinding


class InnerStatsAdapter(private val finalList: ArrayList<Pair<String, ArrayList<DataEntry>>>) : RecyclerView.Adapter<InnerStatsAdapter.InnerCommitDistribution>() {
    inner class InnerCommitDistribution(private val binding: CommitDistributionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(individualData: Pair<String, ArrayList<DataEntry>>, position: Int) {

            val cartesian = AnyChart.column()
            val column: Line = cartesian.line(individualData.second)

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0.0)
                    .offsetY(5.0)
                    .format("{%Value}{groupsSeparator: } Commits")

            cartesian.animation(true)
            cartesian.title("${individualData.first} commits")

            cartesian.yScale().minimum(0.0)

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("Weeks")
            cartesian.yAxis(0).title("Commits")

            binding.pieView.setChart(cartesian)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerCommitDistribution {
        return InnerCommitDistribution(CommitDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: InnerCommitDistribution, position: Int) {
        holder.bind(finalList[position], position)
    }

    override fun getItemCount(): Int {
        return finalList.size
    }
}