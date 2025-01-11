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
import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import dti.crosemont.quebec.location_outils.Domaine.Service.UtilisateurService
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal


@RestController
@RequestMapping("/utilisateurs")
class UtilisateurControleur (private val utilisateurService: UtilisateurService){

    @GetMapping
    fun obtenirUtilisateurs(): ResponseEntity<List<Utilisateur>> = ResponseEntity.ok(utilisateurService.obtenirtousUtilisateurs())

    @GetMapping("/{id}")
    fun obtenirUtilisateurParID(@PathVariable id: Int): ResponseEntity<Utilisateur> = ResponseEntity.ok(utilisateurService.obtenirUtilisateurParID(id))

    @GetMapping(params = ["nom"])
    fun obtenirUtilisateurParNom(@RequestParam nom: String): ResponseEntity<List<Utilisateur>> = ResponseEntity.ok(utilisateurService.obtenirUtilisateurParNom(nom))

    @PostMapping
    fun creerUtilisateur(@RequestBody utilisateur: Utilisateur,@AuthenticationPrincipal jeton:Jwt): ResponseEntity<Utilisateur>{
        val utilisateurCree = utilisateurService.creerUtilisateur(utilisateur, jeton)
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurCree)
    }

    @PutMapping("/{id}")
    fun majUtilisateur(@PathVariable id: Int, @RequestBody utilisateur: Utilisateur, @AuthenticationPrincipal jeton:Jwt): ResponseEntity<Utilisateur> {
        val utilisateurUpdate = utilisateurService.majUtilisateur(id, utilisateur, jeton)
        return if (utilisateurUpdate != null) {
            ResponseEntity.ok(utilisateurUpdate)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun supprimerUtilisateur(@PathVariable id: Int, @AuthenticationPrincipal jeton:Jwt): ResponseEntity<Unit> {
        val utilisateurCreer = utilisateurService.supprimerUtilisateur(id, jeton)
        return if (utilisateurCreer != null){
            ResponseEntity.noContent().build()
        }else{
            ResponseEntity.notFound().build()
        }
    }
}