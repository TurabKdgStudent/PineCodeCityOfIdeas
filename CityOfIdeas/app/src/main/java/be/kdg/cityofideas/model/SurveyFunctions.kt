package be.kdg.cityofideas.model

import be.kdg.cityofideas.R
import java.util.*


fun getSurveys(): Array<Survey>{

    val s1 = Survey(1,1,"In welke thema's zou u het meest investeren met 1 miljoen euro? Vul de vragenlijst in!",
        GregorianCalendar(2019,4,29), GregorianCalendar(2019,4,30), R.mipmap.i1
    )
    val s2 = Survey(2,2,"Hoe geschikt bent u om een miljoen euro zelfs te kunnen inversteren in iets zinvols? Neem de test!",
        GregorianCalendar(2019,4,30), GregorianCalendar(2019,5,1),R.mipmap.i2
    )

    return arrayOf(s1,s2)
}