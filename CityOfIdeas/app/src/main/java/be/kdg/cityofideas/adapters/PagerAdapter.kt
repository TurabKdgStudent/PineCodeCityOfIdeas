package be.kdg.cityofideas.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import be.kdg.cityofideas.fragments.projectPageFragments.SurveysFragment
import be.kdg.cityofideas.fragments.projectPageFragments.DetailFragment
import be.kdg.cityofideas.fragments.projectPageFragments.IdeationsFragment

class PagerAdapter(manager : FragmentManager?, private val numOfTabs : Int, private var Index: Int) : FragmentPagerAdapter(manager) {

    private val detailFragment = DetailFragment()
    private val ideationsFragment = IdeationsFragment()
    private val surveysFragment = SurveysFragment()

    override fun getItem(position: Int): Fragment? {



        return when (position) {
            0 -> detailFragment.apply {
                projectIndex = Index
            }
            1 -> ideationsFragment.apply {
                projectIndex = Index
            }
            2 -> surveysFragment.apply {
                projectIndex = Index
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return numOfTabs
    }
}