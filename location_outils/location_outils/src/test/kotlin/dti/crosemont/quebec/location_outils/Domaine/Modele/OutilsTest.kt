package dti.crosemont.quebec.location_outils

import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils
import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.time.LocalDate

class OutilsTest {

    private val utilisateurParDefaut = Utilisateur(
        id = 1,
        nom = "Hamza",
        prenom = "Ekram",
        numero_telephone = "123-456-7890",
        courriel = "hamza@example.com",
        role = "Client",
        imageUtilisateur = null
    )

    private fun creerOutilParDefaut(
        nom: String = "Perceuse",
        description: String = "Description par défaut",
        prix: Double = 50.0,
        disponibilite: Boolean = true,
        categorie: String = "Électrique",
        coordonne: Pair<Double, Double> = Pair(45.0, -73.0),
        etat: String = "Neuf"
    ): Outils {
        return Outils(
            id = 1,
            nom = nom,
            description = description,
            prix = prix,
            disponibilité = disponibilite,
            imageOutil = null,
            categorie = categorie,
            datePublication = LocalDate.now(),
            coordonné = coordonne,
            etat = etat,
            fournisseur = utilisateurParDefaut
        )
    }

    @Test
    fun `Étant donné un nom valide Perceuse, lorsqu'on crée un Outil avec ce nom, on obtient un Outil avec le nom Perceuse`() {
        val outil = creerOutilParDefaut(nom = "Perceuse")
        assertEquals("Perceuse", outil.nom)
    }

    @Test
    fun `Étant donné un nom vide, lorsqu'on crée un Outil, une erreur IllegalArgumentException est levée`() {
        assertFailsWith<IllegalArgumentException> {
            creerOutilParDefaut(nom = "")
        }
    }

    @Test
    fun `Étant donné un nom commençant par une minuscule, lorsqu'on crée un Outil, une erreur IllegalArgumentException est levée`() {
        assertFailsWith<IllegalArgumentException> {
            creerOutilParDefaut(nom = "perceuse")
        }
    }

    @Test
    fun `Étant donné un nom commençant par un tiret, lorsqu'on crée un Outil, une erreur IllegalArgumentException est levée`() {
        assertFailsWith<IllegalArgumentException> {
            creerOutilParDefaut(nom = "-Perceuse")
        }
    }

    @Test
    fun `Étant donné un nom contenant un caractère spécial, lorsqu'on crée un Outil, une erreur IllegalArgumentException est levée`() {
        assertFailsWith<IllegalArgumentException> {
            creerOutilParDefaut(nom = "?Perceuse")
        }
    }

    @Test
    fun `Étant donné tous les champs valides, lorsqu'on crée un Outil, on obtient un Outil valide`() {
        val outil = creerOutilParDefaut()
        assertEquals("Perceuse", outil.nom)
        assertEquals("Description par défaut", outil.description)
        assertEquals(50.0, outil.prix)
        assertEquals(true, outil.disponibilité)
        assertEquals("Électrique", outil.categorie)
        assertEquals(Pair(45.0, -73.0), outil.coordonné)
        assertEquals("Neuf", outil.etat)
    }
}
