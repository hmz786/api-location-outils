package dti.crosemont.quebec.location_outils.Controleur.Exceptions
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class DroitAccèsInsuffisantException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
}