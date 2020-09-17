package org.d3if1071.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import org.d3if1071.helloworld.adapter.MainAdapter
import org.d3if1071.helloworld.api.ApiStatus
import org.d3if1071.helloworld.view.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //set adapter
        val adapter = MainAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        //set view model
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            //data dari list di adapter di set menjadi data dari view model
            adapter.setData(it)
        })
        //untuk view model progress bar nya
        viewModel.getStatus().observe(this, Observer {
            //it adalah data dari view model
            updateProgress(it)
        })

        //char init
        with(chart) {
            setNoDataText(getString(R.string.belum_ada_data))
            description.text = ""
            //mengatur letak posisi chart
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.setDrawInside(false)
        }

        //view model untuk char
        viewModel.getEntries().observe(this, Observer { updateChart(it) })

        //menuliskan date pada sumbu x
        val formatter = SimpleDateFormat("dd MMMM", Locale("ID", "id"))
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val pos = value.toInt() - 1
                val isValidPosition = pos >= 0 && pos < adapter.itemCount
                return if (isValidPosition) formatter.format(adapter.getDate(pos))
                else ""
            }
        }

        //data akan bisa discroll
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                recyclerView.scrollToPosition(h!!.x.toInt())
            }

            override fun onNothingSelected() {}
        })

    }

    //method untuk update chart
    private fun updateChart(entries: List<Entry>) {
        val dataset = LineDataSet(entries, getString(R.string.jumlah_kasus_positif))
        dataset.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataset.fillColor = dataset.color
        dataset.setDrawFilled(true)
        dataset.setDrawCircles(false)

        chart.data = LineData(dataset)
        chart.invalidate()
    }


    //system loading
    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                progressBar
                    .visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                progressBar.visibility = View.GONE
                networkError.visibility = View.VISIBLE
            }
        }
    }
}