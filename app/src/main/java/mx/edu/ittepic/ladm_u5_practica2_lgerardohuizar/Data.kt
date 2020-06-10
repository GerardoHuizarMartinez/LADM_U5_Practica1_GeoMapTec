package mx.edu.ittepic.ladm_u5_practica2_lgerardohuizar

import com.google.firebase.firestore.GeoPoint

class Data {
    var name : String = ""
    var position1: GeoPoint = GeoPoint(0.0, 0.0)
    var position2: GeoPoint = GeoPoint(0.0, 0.0)
    var aux: GeoPoint = GeoPoint(0.0, 0.0)

    override fun toString(): String {
        return name + "\n" + position1.latitude + "," + position1.longitude + "\n"+
                position2.latitude + "," + position2.longitude
    }

    fun imHere(posAct: GeoPoint): Boolean{
        //Logica es similar a la clase figurs geometrica de canvas
        //imHere() Similar al metodo estoy en area dde figura geometrica
        if (posAct.latitude>= position1.latitude && posAct.latitude<= position2.latitude)
            if(invert(posAct.longitude)>= invert(position1.longitude) && invert(posAct.longitude) <= invert(position2.longitude)){
                return true
            }
        return false
    }

    private fun invert(valor:Double):Double{
        return valor * -1
    }

}//end class data