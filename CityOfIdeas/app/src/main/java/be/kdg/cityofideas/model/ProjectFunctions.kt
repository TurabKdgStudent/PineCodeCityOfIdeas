package be.kdg.cityofideas.model

import be.kdg.cityofideas.R
import java.util.*

fun getProjects():Array<Project>{

    val p1 = Project(1,"BurgerBegroting","Antwerpen", GregorianCalendar(2019,1,1),
        GregorianCalendar(2019,7,14), R.mipmap.p1)
    val p2 = Project(2,"Nationaal Nucleair Plan","Belgie", GregorianCalendar(2019,4,16),
        GregorianCalendar(2020,2,28),R.mipmap.p2)

    return arrayOf(p1,p2)
}