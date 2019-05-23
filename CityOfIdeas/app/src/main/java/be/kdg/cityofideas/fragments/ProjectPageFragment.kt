    package be.kdg.cityofideas.fragments

import android.os.Bundle
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.kdg.cityofideas.adapters.PagerAdapter
import be.kdg.cityofideas.R


    class ProjectPageFragment : Fragment(){

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager
    private lateinit var pagerAdapter : PagerAdapter


     var projectIndex : Int = 0




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view =  inflater.inflate(R.layout.fragment_projectpage, container,false)

        //InitialiseViews()
        tabLayout = view.findViewById(R.id.TablayoutProject)
        viewPager = view.findViewById(R.id.ViewPager)

        pagerAdapter = PagerAdapter(childFragmentManager,tabLayout.tabCount,projectIndex)
        viewPager.adapter = pagerAdapter

        //addEventHandlers()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position

            }override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        return view
    }




}