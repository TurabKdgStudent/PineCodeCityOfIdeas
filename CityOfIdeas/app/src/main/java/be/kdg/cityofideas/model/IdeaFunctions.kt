package be.kdg.cityofideas.model

import be.kdg.cityofideas.R

fun getIdeas():Array<Idea>{
    val id1 = Idea(1,"Nog zoveel mogelijk leuke dingen en uitstapjes doen met de mensen zodat ze het even vergeten of een plaats kunnen geven","Wat zou u willen veranderen aan het milieu in 't Stad?",1,1,8, R.mipmap.i4)
    val id2 = Idea(2,"Door zoveel mogelijk ziektes en kwetsbaarheden uit de taboesfeer te (blijven) halen","Wat zou u willen veranderen aan sociale voorzieningen in 't Stad?",2,7,4, R.mipmap.i3)

    return arrayOf(id1,id2)
}