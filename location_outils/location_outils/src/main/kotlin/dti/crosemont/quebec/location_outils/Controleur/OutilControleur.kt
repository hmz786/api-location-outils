package dti.crosemont.quebec.location_outils.Controleur
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils
import dti.crosemont.quebec.location_outils.Domaine.Service.OutilsService
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal

@RestController
@RequestMapping("/outils")
class OutilControleur (private val outilsService: OutilsService) {
    
    @GetMapping
    fun obtenirOutils(): ResponseEntity<List<Outils>> = ResponseEntity.ok(outilsService.obtenirOutils())

    @GetMapping("/{id}")
    fun obtenirOutilParID(@PathVariable id: Int): ResponseEntity<Outils> = ResponseEntity.ok(outilsService.obtenirOutilParID(id))
 
    @GetMapping(params = ["nom"])
    fun obtenirOutilParNom(@RequestParam nom: String): ResponseEntity<List<Outils>> = ResponseEntity.ok(outilsService.obtenirOutilParNom(nom))
    
    @PostMapping
    fun creerOutil(@RequestBody outils: Outils, @AuthenticationPrincipal jeton : Jwt): ResponseEntity<Outils>{
        val OutilCree = outilsService.creerOutil(outils, jeton)
        return ResponseEntity.status(HttpStatus.CREATED).body(OutilCree)
    }

    @PutMapping("/{id}")
    fun modifierOutil(@PathVariable id: Int, @RequestBody outils: Outils, @AuthenticationPrincipal jeton : Jwt): ResponseEntity<Outils>{
        val outilUpdate = outilsService.changerOutil(id, outils, jeton)
        return if (outilUpdate != null) {
            ResponseEntity.ok(outilUpdate)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun supprimerOutil(@PathVariable id: Int, @AuthenticationPrincipal jeton : Jwt): ResponseEntity<Unit> {
        val outilEstSupprime = outilsService.supprimerOutil(id, jeton)
        return if (outilEstSupprime != null) {
            ResponseEntity.noContent().build() 
        }else{
            ResponseEntity.notFound().build() 
        }
    }
}