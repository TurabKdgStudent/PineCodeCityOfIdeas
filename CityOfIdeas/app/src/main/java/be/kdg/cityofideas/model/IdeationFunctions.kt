package be.kdg.cityofideas.model

import be.kdg.cityofideas.R
import java.util.*

fun getIdeations():Array<Ideation>{

    val i1 = Ideation(1,"Recreatie","Wat zou u willen veranderen aan recreatieve mogelijkheden van 't Stad?",1, GregorianCalendar(2019,4,24),
        GregorianCalendar(2019,5,14),2,5,2, R.mipmap.i1)
    val i2 = Ideation(2,"Mobiliteit","Wat zou u willen veranderen aan de mobiliteit in 't Stad?",1, GregorianCalendar(2019,4,24),
        GregorianCalendar(2019,5,28),10,6,2, R.mipmap.i2)
    val i3 = Ideation(3,"Sociaal","Wat zou u willen veranderen aan sociale voorzieningen in 't Stad?",1, GregorianCalendar(2019,4,24),
        GregorianCalendar(2019,5,30),4,7,4, R.mipmap.i3)
    val i4 = Ideation(3,"Milieu","Wat zou u willen veranderen aan het milieu in 't Stad?",1, GregorianCalendar(2019,4,24),
        GregorianCalendar(2019,5,31),8,1,8, R.mipmap.i4)
    val i5 = Ideation(3,"Reactoren","Hoe erg zou u het vinden als chernobyl in Belgie zou hebben plaatsgevonden?",2, GregorianCalendar(2019,6,29),
        GregorianCalendar(2019,12,12),1,12,8, R.mipmap.i5)

    return arrayOf(i1,i2,i3,i4,i5)
}