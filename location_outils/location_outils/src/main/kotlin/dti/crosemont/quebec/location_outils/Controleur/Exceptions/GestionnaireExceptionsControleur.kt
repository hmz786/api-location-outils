package dti.crosemont.quebec.location_outils.Controleur.Exceptions

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@RestControllerAdvice
class GestionnaireExceptionsControleur() {
    @ExceptionHandler(RessourceInexistanteException::class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    fun gererRessourceInexistanteException(exception: RessourceInexistanteException, requête: WebRequest) : MessageErreur = 
        MessageErreur(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.message, requête.getDescription(false) )


    @ExceptionHandler(RequeteMalFormuleeException::class)    
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)    
    fun gererRequêteMalFormuleeException(exception: RequeteMalFormuleeException, requête: WebRequest): MessageErreur =           
    MessageErreur(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), exception.message, requête.getDescription(false))

}