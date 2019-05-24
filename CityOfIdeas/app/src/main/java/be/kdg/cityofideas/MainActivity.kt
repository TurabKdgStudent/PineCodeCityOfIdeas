package be.kdg.cityofideas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import be.kdg.cityofideas.adapters.*
import be.kdg.cityofideas.drawer.AboutFragment
import be.kdg.cityofideas.drawer.QRFragment
import be.kdg.cityofideas.drawer.ProjectsFragment
import be.kdg.cityofideas.drawer.PlatformFragment
import be.kdg.cityofideas.fragments.ideasFragments.IdeasFragment
import be.kdg.cityofideas.fragments.ideasFragments.SingleIdeaFragment
import be.kdg.cityofideas.fragments.ProjectPageFragment
import be.kdg.cityofideas.fragments.surveysFragments.SingleSurveyFragment
import be.kdg.cityofideas.identity.fragments.LoginFragment
import be.kdg.cityofideas.rest.NukeSSLCerts
import kotlinx.android.synthetic.main.activity_main.*

/**
 * In dit project is er gebruik gemaakt van "Volley" om de data op te halen.
 * Dit komt omdat we een back-end hadden gemaakt waarbij we met graphQl werkten en dus niet
 * de gebruikelijke Web-API(Behalve voor de POST-methodes).
 *
 * Dit zorgde ervoor dat we grapghQl queries moesten versturen om data op te halen en er dus steeds
 * zeer grote blokken met meerdere geneste objecten binnenkwamen die dan moesten worden omgezet,
 * wat ervoor zorgde dat we niet met rxJava konden werken.
 *
 * We hebben door dat er op canvas staat dat we met RxJava zouden moeten werken, maar in ons
 * geval was dit praktisch onmogelijk door graphQl. Dit hadden we ook gevraagd tijdens de coaching
 * en we kregen als antwoord dat dit geen probleem was. Voor de zekerheid wouden we dit toch nog hier mededelen.
 *
 * Voor de rest zijn alle functionaliteiten inbegrepen
 * en volgt het ook volledig de Single-Activity pattern.
 *
 */

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    RvProjectsAdapter.OnProjectSelectedListener,
    RvIdeationsAdapter.OnIdeationSelectedListener,
    RvIdeasAdapter.OnIdeaSelectedListener,
    RvSurveyAdapter.OnSurveySelectedListener,
    RvPlatformsAdapter.OnPlatformSelectedListener{


    private lateinit var toolbar : Toolbar
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var navigationView : NavigationView
    private lateinit var headerView : View

    private var count : Int = supportFragmentManager.backStackEntryCount


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NukeSSLCerts.nuke()
        initialiseViews()
        addEventHandlers()

        if (savedInstanceState == null)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,LoginFragment()).commit()
        toggle.setToolbarNavigationClickListener {
            Toast.makeText(this,"Log in om te verkennen!",Toast.LENGTH_LONG).show()
        }
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref.getString("username","") == ""){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,LoginFragment()).commitAllowingStateLoss()
        } else{
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,PlatformFragment()).commitAllowingStateLoss()
        }
    }

    override fun onResume() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        headerView = navigationView.getHeaderView(0)
        val navUserName =  headerView.findViewById<TextView>(R.id.usernameNavhead)
        navUserName.text = sharedPref.getString("username","")
        val navLogBtn = navigationView.findViewById<Button>(R.id.nav_login)
        navLogBtn.text = sharedPref.getString("logbutton","LOGIN")
        super.onResume()
    }


    private fun initialiseViews(){
        //nav & default Toolbar
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = drawer_layout
        toggle  = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun addEventHandlers(){
        navigationView.setNavigationItemSelectedListener(this)
        nav_login.setOnClickListener {
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref.getString("username","") != ""){
                sharedPref.edit().clear().apply()
                headerView = navigationView.getHeaderView(0)
                val navUserName =  headerView.findViewById<TextView>(R.id.usernameNavhead)
                navUserName.text = ""
                Toast.makeText(this,"U bent succesvol uitgelogd!",Toast.LENGTH_LONG).show()
                nav_login.text = sharedPref.getString("logbutton","LOGIN")
            }
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,LoginFragment()).addToBackStack("").commit()
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    fun hideSoftKeyboard(v : View) {
        val inputMethodManager : InputMethodManager = v.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onBackPressed() {
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> drawer.closeDrawer(GravityCompat.START)
            count == 0 -> super.onBackPressed()
            else -> supportFragmentManager.popBackStack()
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.nav_projects -> supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container, ProjectsFragment()).addToBackStack("").commit()
            item.itemId == R.id.nav_platforms -> supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container, PlatformFragment()).addToBackStack("").commit()
            item.itemId == R.id.nav_QR -> supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container, QRFragment()).addToBackStack("").commit()
            item.itemId == R.id.nav_about -> supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
                .replace(R.id.fragment_container,AboutFragment()).addToBackStack("").commit()
        }
        drawer.closeDrawer(GravityCompat.START)

        return true
    }
    override fun onPlatformSelected(platformIndex: Int) {
        val projectsFragment = ProjectsFragment()
        projectsFragment.platformIndex = platformIndex
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,projectsFragment)
            .addToBackStack("").commit()
    }


    override fun onProjectSelected(projectIndex: Int) {

        val projectPageFragment = ProjectPageFragment()
        projectPageFragment.projectIndex=projectIndex
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,
            projectPageFragment).addToBackStack("").commit()

    }

    override fun onIdeationSelected(ideationIndex: Int) {
        val ideasFragment = IdeasFragment()
        ideasFragment.ideationIndex = ideationIndex
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,ideasFragment)
            .addToBackStack("").commit()
    }

    override fun onIdeaSelected(ideaIndex: Int) {
        val singleIdeaFragment = SingleIdeaFragment()
        singleIdeaFragment.ideaIndex = ideaIndex
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,singleIdeaFragment)
            .addToBackStack("").commit()
    }

    override fun onSurveySelected(surveyIndex: Int,projectIndex: Int) {
        val singleSurveyFragment = SingleSurveyFragment()
        singleSurveyFragment.surveyindex = surveyIndex
        singleSurveyFragment.projectindex = projectIndex
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.left_in_back,R.anim.left_out_back)
            .replace(R.id.fragment_container,singleSurveyFragment)
            .addToBackStack("").commit()
    }

}
