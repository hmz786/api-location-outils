package dti.crosemont.quebec.location_outils.Controleur.Exceptions

import java.time.LocalDateTime

data class MessageErreur(val code: Int, val date: LocalDateTime, val message: String?, val chemin: String)
