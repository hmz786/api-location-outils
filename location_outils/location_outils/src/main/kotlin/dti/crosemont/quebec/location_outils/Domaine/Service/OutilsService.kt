package dti.crosemont.quebec.location_outils.Domaine.Service
import org.springframework.stereotype.Service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.access.annotation.Secured
import dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees.OutilsDAO
import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RessourceInexistanteException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.OperationNonAutoriseeException
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RequeteMalFormuleeException

@Service
class OutilsService (private val outilsDAO: OutilsDAO){


    fun obtenirOutils(): List<Outils> = outilsDAO.chercherTous()


    fun obtenirOutilParID(id: Int): Outils? {
        val outils = outilsDAO.chercherParId(id)
        
        if(outils == null) {
            throw RessourceInexistanteException("L'outil avec l'id $id n'est pas trouvable")
        }
        return outils
    }

    fun obtenirOutilParNom(nom: String): List<Outils> = outilsDAO.chercherParNom(nom)


    @PreAuthorize("hasAuthority('modifier:outil')")
    fun changerOutil(id:Int, outils:Outils, jeton : Jwt):Outils? {
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("Élément du jeton non adéquat pour la modification d'un profil")
        val outilModifier = outilsDAO.chercherParId(id) //?: throw RessourceInexistanteException("L'utilisateur avec l'ID $id n'existe pas")
        if(outilModifier != null && outilModifier.fournisseur.courriel == courrielAuthentification){
            if(outilModifier.fournisseur != outils.fournisseur){
                throw RequeteMalFormuleeException("Vous ne pouvez pas modifer le fournisseur")
           }else{
                return outilsDAO.modifier(id, outils)
           }
        }
        throw RequeteMalFormuleeException("Vous pouvez seulement modifier vos outils")
    }


    @PreAuthorize("hasAuthority('créer:outil')")
    fun creerOutil(outils: Outils, jeton: Jwt): Outils? {
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("non adequant pour creer un autre compte sur courriel")
        if (outils.fournisseur.courriel == courrielAuthentification) {
            return outilsDAO.ajouter(outils)
        }

        throw RequeteMalFormuleeException("Vous pouvez seulement créer un outil avec votre compte")
    }

    @PreAuthorize("hasAuthority('supprimer:outil')")
    fun supprimerOutil(id: Int, jeton : Jwt): Unit {
        val courrielAuthentification = jeton.claims["courriel"] as? String ?: throw OperationNonAutoriseeException("Élément du jeton non adéquat pour la suppression d'un profil")
        val outilSupprimer = outilsDAO.chercherParId(id)
        if(outilSupprimer != null && outilSupprimer.fournisseur.courriel == courrielAuthentification){return outilsDAO.effacer(id)}
        throw RequeteMalFormuleeException("Vous ne pouvez seulement supprimer votre outil")
        
    }
}