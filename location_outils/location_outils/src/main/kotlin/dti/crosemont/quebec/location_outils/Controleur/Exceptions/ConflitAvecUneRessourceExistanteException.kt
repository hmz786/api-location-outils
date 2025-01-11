package dti.crosemont.quebec.location_outils.Controleur.Exceptions
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class ConflitAvecUneRessourceExistanteException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
}