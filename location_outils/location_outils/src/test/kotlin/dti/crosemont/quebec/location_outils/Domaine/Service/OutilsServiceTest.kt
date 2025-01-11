package dti.crosemont.quebec.location_outils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.test.context.support.WithMockUser
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.springframework.security.oauth2.jwt.Jwt
import dti.crosemont.quebec.location_outils.Domaine.Service.OutilsService
import java.time.LocalDate
import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils
import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
import dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees.OutilsDAO
import dti.crosemont.quebec.location_outils.Controleur.Exceptions.*

@ExtendWith(MockitoExtension::class)
class OutilsServiceTest {

    @Mock
    lateinit var mockOutilsDAO: OutilsDAO

    val fournisseur = Utilisateur(id = 1, imageUtilisateur = null, prenom = "Hamza", nom = "Test", numero_telephone = "12345678", courriel = "fournisseur@outlook.com", role = "fournisseur")
    val outil = Outils(id = 1, nom = "Perceuse", description = "Perceuse puissant", prix = 100.0, disponibilité = true, imageOutil = "image.jpg", categorie = "Bricolage", datePublication = LocalDate.now(), coordonné = Pair(45.5017, -73.5673), etat = "Neuf", fournisseur = fournisseur)

    @WithMockUser(authorities = ["accéeder:outil"])
    @Test
    fun `étant donné un utilisateur autorisé, lorsqu'on récupère tous les outils, on obtient la liste des outils`() {
        val résultat_attendu = listOf(outil)

        Mockito.`when`(mockOutilsDAO.chercherTous()).thenReturn(résultat_attendu)

        val serviceTest = OutilsService(mockOutilsDAO)

        val résultat_observé = serviceTest.obtenirOutils()

        assertEquals(résultat_attendu, résultat_observé)
    }

    @WithMockUser(authorities = ["modifier:outil"])
    @Test
    fun `étant donné un utilisateur autorisé, lorsqu'on modifie un outil, l'outil est mis à jour avec succès`() {
        val outilModifie = outil.copy(nom = "Perceuse modifiée", prix = 120.0)
        val jeton = Mockito.mock(Jwt::class.java)
        
        Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to fournisseur.courriel))
        Mockito.`when`(mockOutilsDAO.chercherParId(1)).thenReturn(outil)
        Mockito.`when`(mockOutilsDAO.modifier(1, outilModifie)).thenReturn(outilModifie)

        val serviceTest = OutilsService(mockOutilsDAO)

        val résultat_observé = serviceTest.changerOutil(1, outilModifie, jeton)

        assertEquals(outilModifie, résultat_observé)
    }

    @Test
    fun `étant donné un utilisateur non autorisé avec un jeton invalide, lorsqu'on tente de modifier un outil, une OperationNonAutoriseeException est levée`() {
        val outilModifie = outil.copy(nom = "Perceuse modifiée", prix = 120.0)
        val jeton = Mockito.mock(Jwt::class.java)

        Mockito.`when`(jeton.claims).thenReturn(emptyMap<String, Any>())

        val serviceTest = OutilsService(mockOutilsDAO)

        assertFailsWith<OperationNonAutoriseeException> {
            serviceTest.changerOutil(1, outilModifie, jeton)
        }
    }

    @WithMockUser(authorities = ["créer:outil"])
    @Test
    fun `étant donné un utilisateur non autorisé, lorsqu'il tente de créer un outil, une OperationNonAutoriseeException est levée`() {
        val nouvelOutil = Outils(id = 2, nom = "Tournevis", description = "Tournevis électrique", prix = 50.0, disponibilité = true, imageOutil = "tournevis.jpg", categorie = "Bricolage", datePublication = LocalDate.now(), coordonné = Pair(45.5017, -73.5673), etat = "Neuf", fournisseur = fournisseur)

        val jeton = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to fournisseur.courriel))
        Mockito.`when`(mockOutilsDAO.ajouter(nouvelOutil)).thenReturn(nouvelOutil)

        val serviceTest = OutilsService(mockOutilsDAO)

        val résultat_observé = serviceTest.creerOutil(nouvelOutil, jeton)

        assertEquals(nouvelOutil, résultat_observé)
    }

    @WithMockUser(authorities = ["supprimer:outil"])
    @Test
    fun `étant donné un utilisateur autorisé, lorsqu'il tente de supprimer un outil, l'outil est supprimé avec succès`() {
        val jeton = Mockito.mock(Jwt::class.java)

        Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to fournisseur.courriel))
        Mockito.`when`(mockOutilsDAO.chercherParId(1)).thenReturn(outil)

        val serviceTest = OutilsService(mockOutilsDAO)

        serviceTest.supprimerOutil(1, jeton)

        Mockito.verify(mockOutilsDAO).effacer(1)
    }

    @Test
    fun `étant donné un utilisateur non autorisé avec un jeton invalide, lorsqu'on tente de supprimer un outil, une OperationNonAutoriseeException est levée`() {
        val jeton = Mockito.mock(Jwt::class.java)

        Mockito.`when`(jeton.claims).thenReturn(emptyMap<String, Any>())

        val serviceTest = OutilsService(mockOutilsDAO)

        assertFailsWith<OperationNonAutoriseeException> {
            serviceTest.supprimerOutil(1, jeton)
        }
    }
}