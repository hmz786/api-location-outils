package dti.crosemont.quebec.location_outils.Domaine.Service

import org.springframework.stereotype.Service
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.access.annotation.Secured



import dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees.UtilisateurDAO
import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RessourceInexistanteException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.OperationNonAutoriseeException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RequeteMalFormuleeException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.ConflitAvecUneRessourceExistanteException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.DroitAccèsInsuffisantException

@Service
class UtilisateurService (private val utilisateurDAO: UtilisateurDAO){
    @PreAuthorize("hasAuthority('accéder:utilisateur')")
    fun obtenirtousUtilisateurs(): List<Utilisateur> = utilisateurDAO.chercherTous()

    

    @PreAuthorize("hasAuthority('accéder:utilisateur')")
    fun obtenirUtilisateurParID(id: Int): Utilisateur? {
        val utilisateur = utilisateurDAO.chercherParId(id)
        
        if(utilisateur == null) {
            throw RessourceInexistanteException("L'Utilisateur avec l'id $id n'est pas inscrit au service")
        }
        return utilisateur
    }

    @PreAuthorize("hasAuthority('accéder:utilisateur')")
    fun obtenirUtilisateurParNom(nom: String): List<Utilisateur> {
        val utilisateur = utilisateurDAO.chercherParNom(nom)
        if(utilisateur == null){
            throw RessourceInexistanteException("Tu na pas droit de connecter")
        }
        return utilisateur
    }

    @PreAuthorize("hasAuthority('créer:utilisateur')")
    fun creerUtilisateur(utilisateur: Utilisateur, jeton : Jwt): Utilisateur? {
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("non adequant pour creer un autre compte sur courriel")
        val utilisateurExistant = utilisateurDAO.chercherParCourriel(courrielAuthentification)
        
        if (utilisateurExistant != null) {throw ConflitAvecUneRessourceExistanteException("Un utilisateur existe déjà pour l'adresse $courrielAuthentification.")}
        if(utilisateur != null && utilisateur.courriel == courrielAuthentification){return utilisateurDAO.ajouter(utilisateur)}
        throw RequeteMalFormuleeException("Vous pouvez seulement créer un compte avec un email dans notre BD")
    }  

    
    @PreAuthorize("hasAuthority('modifier:utilisateur')")
    fun majUtilisateur(id:Int, utilisateur:Utilisateur, jeton:Jwt):Utilisateur? {
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("Élément du jeton non adéquat pour la modification d'un profil")
        val utilisateurModifier = utilisateurDAO.chercherParId(id)
        if(utilisateurModifier != null && utilisateurModifier.courriel == courrielAuthentification){
            if(utilisateurModifier.courriel != utilisateur.courriel){
                throw RequeteMalFormuleeException("Vous ne pouvez pas modifer votre email")
           }else{
                return utilisateurDAO.modifier(id, utilisateur)
           }
        }
        throw RequeteMalFormuleeException("Vous pouvez seulement modifier votre compte")
    }

    
    @PreAuthorize("hasAuthority('supprimer:utilisateur')")
    fun supprimerUtilisateur(id:Int, jeton:Jwt) : Unit{
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("Élément du jeton non adéquat pour la suppression d'un profil")
        val utilisateurSupprimer = utilisateurDAO.chercherParId(id)
        if(utilisateurSupprimer != null && utilisateurSupprimer.courriel == courrielAuthentification){return utilisateurDAO.effacer(id)}
        throw RequeteMalFormuleeException("Vous ne pouvez seulement supprimer votre compte")
   }
}

