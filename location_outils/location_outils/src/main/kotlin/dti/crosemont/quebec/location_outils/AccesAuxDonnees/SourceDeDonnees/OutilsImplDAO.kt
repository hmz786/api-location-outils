package dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees
import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.RessourceInexistanteException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class OutilsImplDAO(private val bd: JdbcTemplate, private val utilisateurDAO: UtilisateurDAO) : OutilsDAO {

    private fun mapOutils(réponse: ResultSet): Outils {
        val fournisseurId = réponse.getInt("fournisseur")
        val fournisseur = utilisateurDAO.chercherParId(fournisseurId)
            ?: throw RessourceInexistanteException("Fournisseur avec l'id $fournisseurId introuvable.")
               
        val position = Pair(réponse.getDouble("longitude"), réponse.getDouble("latitude"))

        return Outils(
            id = réponse.getInt("id"),
            nom = réponse.getString("nom"),
            description = réponse.getString("description"),
            prix = réponse.getDouble("prix"),
            disponibilité = réponse.getBoolean("disponibilité"),
            if(réponse.getString("imageOutil") != null){
                réponse.getString("imageOutil")
            }else{
                null
            },
            categorie = réponse.getString("categorie"),
            datePublication = réponse.getDate("datePublication")?.toLocalDate()
                ?: throw IllegalArgumentException("Date de publication invalide."),
            coordonné = position, 
            etat = réponse.getString("etat"),
            fournisseur = fournisseur
        )
    }

    override fun chercherTous(): List<Outils> = bd.query(
        "SELECT id, nom, description, prix, disponibilité, imageOutil, categorie, datePublication, etat, fournisseur, ST_X(coordonné) AS latitude, ST_Y(coordonné) AS longitude FROM outils"
    ) { réponse, _ -> mapOutils(réponse) }

    override fun chercherParId(id: Int): Outils? = bd.query(
        "SELECT id, nom, description, prix, disponibilité, imageOutil, categorie, datePublication, etat, fournisseur, ST_X(coordonné) AS latitude, ST_Y(coordonné) AS longitude FROM outils WHERE id = ?",
        arrayOf(id)
    ) { réponse, _ -> mapOutils(réponse) }.singleOrNull()

    override fun chercherParNom(nom: String): List<Outils>  = bd.query(
        "SELECT id, nom, description, prix, disponibilité, imageOutil, categorie, datePublication, etat, fournisseur, ST_X(coordonné) AS latitude, ST_Y(coordonné) AS longitude FROM outils WHERE nom = ?",
        arrayOf(nom)
    ) { réponse, _ -> mapOutils(réponse) }

    override fun effacer(id: Int) {
        val deleteQuery = "DELETE FROM outils WHERE id = ?"
        val rowsAffected = bd.update(deleteQuery, id)
        if (rowsAffected == 0) {
            throw RessourceInexistanteException("Outil avec id $id n'a pas été trouvé.")
        }
    }

    override fun ajouter(outils: Outils): Outils? {
        val insererDonnes = """
            INSERT INTO outils (nom, description, prix, disponibilité, imageOutil, categorie, datePublication, coordonné , etat, fournisseur) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ST_GeomFromText(?), ?, ?)
        """.trimIndent()

        val coordonne = "POINT(${outils.coordonné.first} ${outils.coordonné.second})"
        bd.update(
            insererDonnes,
            outils.nom,
            outils.description,
            outils.prix,
            outils.disponibilité,
            outils.imageOutil,
            outils.categorie,
            outils.datePublication,
            coordonne, 
            outils.etat,
            outils.fournisseur.id
        )

        return bd.query(
            "SELECT id, nom, description, prix, disponibilité, imageOutil, categorie, datePublication, etat, fournisseur, ST_X(coordonné) AS latitude, ST_Y(coordonné) AS longitude FROM outils WHERE nom = ?",
            arrayOf(outils.nom)
        ) { réponse, _ -> mapOutils(réponse) }.singleOrNull()
            ?: throw RessourceInexistanteException("Erreur lors de l'insertion de l'outil.")
    }

    override fun modifier(id: Int, outils: Outils): Outils {
        val updateQuery = """
            UPDATE outils 
            SET nom = ?, description = ?, prix = ?, disponibilité = ?, imageOutil = ?, categorie = ?, datePublication = ?, coordonné = ST_GeomFromText(?), etat = ?, fournisseur = ? 
            WHERE id = ?
        """.trimIndent()
        val coordonne = "POINT(${outils.coordonné.first} ${outils.coordonné.second})"
        bd.update(
            updateQuery,
            outils.nom,
            outils.description,
            outils.prix,
            outils.disponibilité,
            outils.imageOutil,
            outils.categorie,
            outils.datePublication,
            coordonne, 
            outils.etat,
            outils.fournisseur.id,
            id
        )
        return chercherParId(id)
            ?: throw RessourceInexistanteException("L'outil avec l'id $id n'existe pas.")
    }

    private fun Pair<Double, Double>.toSQLArray(jdbcTemplate: JdbcTemplate): java.sql.Array {
        return jdbcTemplate.dataSource?.connection?.createArrayOf("DOUBLE", arrayOf(this.first, this.second))
            ?: throw IllegalStateException("Unable to convert Pair to SQL Array.")
    }
}
