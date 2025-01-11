package dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RessourceInexistanteException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class UtilisateurImplDAO(private val bd: JdbcTemplate) : UtilisateurDAO {

    private fun mapUtilisateur(réponse: ResultSet): Utilisateur {
        return Utilisateur(
            réponse.getInt("id"),
            if(réponse.getString("imageUtilisateur") != null){
                réponse.getString("imageUtilisateur")
            }else{
                null
            },
            réponse.getString("prenom"),
            réponse.getString("nom"),
            réponse.getString("numero_telephone"),
            réponse.getString("courriel"),
            réponse.getString("role")   
        )
    }

    override fun chercherTous(): List<Utilisateur> = bd.query("SELECT id, prenom, nom, numero_telephone, courriel,imageUtilisateur,role FROM utilisateurs") {
         réponse, _ -> mapUtilisateur(réponse)
    }

    override fun chercherParId(id: Int): Utilisateur? = bd.query("SELECT id, prenom, nom, numero_telephone, courriel, imageUtilisateur,role FROM utilisateurs WHERE id = ?", arrayOf(id)) { 
        réponse, _ -> mapUtilisateur(réponse)
    }.singleOrNull()

    override fun chercherParNom(nom: String): List<Utilisateur>  = bd.query("SELECT id, prenom, nom, numero_telephone, courriel,imageUtilisateur,role FROM utilisateurs WHERE nom = ?", arrayOf(nom)) { 
        réponse, _ -> mapUtilisateur(réponse)
    }

    override fun ajouter(utilisateur: Utilisateur): Utilisateur? {
        val insererDonnes = "INSERT INTO utilisateurs (prenom, nom, numero_telephone, courriel,imageUtilisateur,role) VALUES (?, ?, ?, ?, ?, ?)"
        bd.update(insererDonnes, utilisateur.prenom, utilisateur.nom, utilisateur.numero_telephone, utilisateur.courriel, utilisateur.imageUtilisateur, utilisateur.role)

        return bd.query("SELECT id, prenom, nom, numero_telephone, courriel, imageUtilisateur, role FROM utilisateurs WHERE prenom = ? AND nom = ?", arrayOf(utilisateur.prenom, utilisateur.nom)) { 
            réponse, _ -> mapUtilisateur(réponse)
            
        }.singleOrNull() ?: throw RessourceInexistanteException("Erreur lors de l'insertion de l'utilisateur.")
    }

    override fun modifier(id: Int, utilisateur: Utilisateur): Utilisateur {
        val updateQuery = "UPDATE utilisateurs SET prenom = ?, nom = ?, numero_telephone = ?, courriel = ?, imageUtilisateur = ?, role = ? WHERE id = ?"
        bd.update(updateQuery, utilisateur.prenom, utilisateur.nom, utilisateur.numero_telephone, utilisateur.courriel, utilisateur.imageUtilisateur, utilisateur.role, id)

        return chercherParId(id) ?: throw RessourceInexistanteException("Utilisateur avec id $id n'existe pas.")
    }

    override fun effacer(id: Int) {
        val deleteQuery = "DELETE FROM utilisateurs WHERE id = ?"
        val rowsAffected = bd.update(deleteQuery, id)
        if (rowsAffected == 0) {
            throw RessourceInexistanteException("Utilisateur avec id $id n'a pas été trouvé.")
        }
    }
    
    override fun chercherParCourriel(courriel: String): Utilisateur? = bd.query("SELECT id, prenom, nom, numero_telephone, courriel, imageUtilisateur,role FROM utilisateurs WHERE courriel = ?", arrayOf(courriel)) { 
        réponse, _ -> mapUtilisateur(réponse)
    }.singleOrNull()
    
}
