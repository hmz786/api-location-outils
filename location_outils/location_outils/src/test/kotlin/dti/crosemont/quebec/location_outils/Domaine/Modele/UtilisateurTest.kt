package dti.crosemont.quebec.location_outils

import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class UtilisateurTest {

    private fun creerUtilisateur(
        id: Int = 1,
        nom: String = "Hamza",
        prenom: String = "Ekram",
        numero_telephone: String = "123-435-2223",
        courriel: String = "hamza@gmail.com",
        role: String = "Client",
        imageUtilisateur: String = "image.jpg"
    ): Utilisateur {
        return Utilisateur(
            id = id,
            nom = nom,
            prenom = prenom,
            numero_telephone = numero_telephone,
            courriel = courriel,
            role = role,
            imageUtilisateur = imageUtilisateur
        )
    }

    //-----------------------TEST POUR NOM--------------------------------------

    @Test
    fun `Etant donner un nom valide Hamza, lorsqu'on creer un utilisateur avec ce nom on obtient un utilisateur avec le nom Hamza`() {
        val nom_valide = "Hamza"
        val utilisateur = creerUtilisateur(nom = nom_valide)
        assert(utilisateur.nom == nom_valide)
    }

    @Test
    fun `Etant donner un nom valide Ham za, lorsqu'on creer un utilisateur avec ce nom on obtient un utilisateur avec le nom Ham za`() {
        val nom_valide = "Ham za"
        val utilisateur = creerUtilisateur(nom = nom_valide)
        assert(utilisateur.nom == nom_valide)
    }

    @Test
    fun `Etant donner un nom valide Ham-za avec -, lorsqu'on creer un utilisateur avec ce nom on obtient un utilisateur avec le nom Ham-za`() {
        val nom_valide = "Ham-za"
        val utilisateur = creerUtilisateur(nom = nom_valide)
        assert(utilisateur.nom == nom_valide)
    }

    @Test
    fun `Etant donner un nom invalide H, lorsqu'on creer un utilisateur avec un nom d'un caractere, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "H")
        }
    }

    @Test
    fun `Etant donner un nom invalide de plus de 30 caracteres, lorsqu'on creer un utilisateur avec ce nom, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "Hhhhhhhammzajbzjjjjjjjjjjjjjjjjjjjjjjjjjj")
        }
    }

    @Test
    fun `Etant donner un nom commencant par un tiret (-Hamza), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "-Hamza")
        }
    }

    @Test
    fun `Etant donner un nom commencant par un espace ( Hamza), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = " Hamza")
        }
    }

    @Test
    fun `Etant donner un nom commencant par une lettre minuscule (hamza), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "hamza")
        }
    }

    @Test
    fun `Etant donner un nom vide, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "")
        }
    }

    @Test
    fun `Etant donner un nom commencant par un caractere invalide #Hamza, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(nom = "#Hamza")
        }
    }

    //-----------------------TEST POUR PRENOM-----------------------------------

    @Test
    fun `Etant donner un prenom valide Ekram, lorsqu'on creer un utilisateur avec ce prenom, on obtient un utilisateur avec le prenom Ekram`() {
        val prenom_valide = "Ekram"
        val utilisateur = creerUtilisateur(prenom = prenom_valide)
        assert(utilisateur.prenom == prenom_valide)
    }

    @Test
    fun `Etant donner un prenom invalide d'un caractere (E), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "E")
        }
    }

    @Test
    fun `Etant donner un prenom de plus de 30 caracteres, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "Ekkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
        }
    }

    @Test
    fun `Etant donner un prenom commencant par un tiret (-Ekram), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "-Ekram")
        }
    }

    @Test
    fun `Etant donner un prenom commencant par un espace ( Ekram), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = " Ekram")
        }
    }

    @Test
    fun `Etant donner un prenom commencant par une lettre minuscule (ekram), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "ekram")
        }
    }

    @Test
    fun `Etant donner un prenom vide, on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "")
        }
    }

    @Test
    fun `Etant donner un prenom commencant par un caractere invalide (#Ekram), on obtient une erreur IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(prenom = "#Ekram")
        }
    }

    //-----------------------TEST COMPLET--------------------------------------

    @Test
    fun `Etant donner tous les champs valides, lorsqu'on cree un utilisateur, on obtient un utilisateur valide`() {
        val id = 1
        val image = "image.jpg"
        val nom_valide = "Hamza"
        val prenom_valide = "Ekram"
        val numero = "123-435-2223"
        val addresse = "hamza@gmail.com"
        val role = "Client"

        val utilisateur = creerUtilisateur(
            id = id,
            nom = nom_valide,
            prenom = prenom_valide,
            numero_telephone = numero,
            courriel = addresse,
            role = role,
            imageUtilisateur = image
        )

        assert(utilisateur.id == id)
        assert(utilisateur.imageUtilisateur == image)
        assert(utilisateur.prenom == prenom_valide)
        assert(utilisateur.nom == nom_valide)
        assert(utilisateur.numero_telephone == numero)
        assert(utilisateur.courriel == addresse)
        assert(utilisateur.role == role)
    }

    @Test
    fun `Étant donné un email valide 'jcontant@outlookcom', lorsqu'on crée un utilisateur avec cet email, on obtient un utilisateur avec cet email`() {
        val email_valide = "jcontant@outlook.com"
    
        val utilisateur = creerUtilisateur(courriel = email_valide)
    
        assert(utilisateur.courriel == email_valide)
    }
    
    @Test
    fun `Étant donné un email invalide 'jcontantoutlookcom', lorsqu'on crée un utilisateur avec cet email, on obtient une erreur IllegalArgumentException`() {
        val email_invalide = "jcontantoutlook.com"
    
        assertFailsWith<IllegalArgumentException> {
            creerUtilisateur(courriel = email_invalide)
        }
    }    
}
