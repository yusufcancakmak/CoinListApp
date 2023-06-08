package com.hera.coinlistapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.hera.coinlistapp.R
import com.hera.coinlistapp.databinding.FragmentDetailsBinding
import com.hera.coinlistapp.utils.*
import com.hera.coinlistapp.utils.Constants.animationDuration
import com.hera.coinlistapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

@AndroidEntryPoint
class DetailsFragment : Fragment() {



private var _binding: FragmentDetailsBinding? = null
private val binding get() = _binding!!
    private val args:DetailsFragmentArgs by navArgs()

    private val viewmodel:MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentDetailsBinding.inflate(layoutInflater)
        viewmodel.getCoinDetailList(args.id)
        return _binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            binding.apply {
                viewmodel.coinDetailList.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DataStatus.Status.LOADING -> {
                            pBarLoading.isVisible(true, mainLayout)
                        }
                        DataStatus.Status.SUCCESS -> {
                            it.data?.let { data ->
                                pBarLoading.isVisible(false, mainLayout)
                                tvCoinNameSymbol.text = "${data.name} [ ${data.symbol?.uppercase()} ]"
                                tvCurrentPrice.text = data.marketData?.currentPrice?.eur?.roundToThreeDecimals()

                                val number = data.marketData?.priceChangePercentage24h?.roundToTwoDecimals()?.toDouble()!!
                                tvChangePercentage.text = "$number%"

                                when {
                                    number > 0 -> {
                                        tvChangePercentage.setTextColor(Color.GREEN)
                                        imgArrow.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_arrow_drop_up_24))
                                    }
                                    number < 0 -> {
                                        tvChangePercentage.setTextColor(Color.RED)
                                        imgArrow.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_arrow_drop_down_24))
                                    }
                                    else -> {
                                        tvChangePercentage.setTextColor(Color.LTGRAY)
                                        imgArrow.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_minimize_24))
                                    }
                                }

                                //Logo
                                imgCoinLogo.load(data.image?.large) {
                                    crossfade(true)
                                    crossfade(500)
                                    placeholder(R.drawable.baseline_currency_bitcoin_24)
                                    error(R.drawable.baseline_currency_bitcoin_24)
                                }

                                //Chart
                                lineChart.gradientFillColors = intArrayOf(
                                    Color.parseColor("#2a9085"), Color.TRANSPARENT
                                )
                                lineChart.animation.duration = animationDuration
                                val listData = data.marketData.sparkline7d?.price?.toDoubletoFloat()
                                lineChart.animate(listData!!)

                                //Categories
                                tvCategories.text = data.categories?.get(0)!!

                                //GenesisDate
                                tvGenesisDate.text = if (data.genesisDate != null && data.genesisDate.isNotEmpty()) data.genesisDate else "-"

                                //Link
                                tvLink.text = data.links?.homepage?.get(0)
                                tvLink.setOnClickListener {
                                    val uri = Uri.parse(data.links?.homepage?.get(0))
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    requireContext().startActivity(intent)
                                }

                                //Description
                                tvDescription.text = if (data.description?.en != null && data.description.en.isNotEmpty()) Jsoup.parse(data.description.en).text() else "-"
                                tvDescription.movementMethod = ScrollingMovementMethod()

                            }

                        }
                        DataStatus.Status.ERROR -> {
                            pBarLoading.isVisible(true, mainLayout)
                            Toast.makeText(requireContext(), "There is something wrong!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}