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
    import dti.crosemont.quebec.location_outils.Domaine.Service.UtilisateurService
    import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur
    import dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees.UtilisateurDAO
    import dti.crosemont.quebec.location_outils.Controleur.Exceptions.*

    @ExtendWith(MockitoExtension::class)
    class UtilisateurServiceTest{

        @Mock
        lateinit var mockUtilisateurDAO : UtilisateurDAO

        val hamza = Utilisateur(id = 1, imageUtilisateur =  null, prenom = "Hamza", nom = "Ekram", numero_telephone = "12345678", courriel = "hamzaekram@outlook.com", role = "client")
        val Rabbi = Utilisateur(id = 2, imageUtilisateur =  null, prenom = "Rabii", nom =  "Maillette", numero_telephone = "12555678", courriel =  "Rabbi@outlook.com", role = "fournisseur")


        @WithMockUser(authorities = ["accéeder:utilisateur"])
        @Test
        fun `étant donné un utilisateur autorisé, lorsqu'un Admin avec les droits demande tous les utilisateurs, on obtient deux utilisateurs`(){
            val résultat_attendu = listOf(hamza, Rabbi)

            Mockito.`when`(mockUtilisateurDAO.chercherTous()).thenReturn(résultat_attendu)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            val résultat_observé = serviceTest.obtenirtousUtilisateurs()

            assertEquals(résultat_attendu, résultat_observé)

        }

        @Test
        fun `étant donne un ID inexistant, une RessourceInexistanteException est levee`() {
            Mockito.`when`(mockUtilisateurDAO.chercherParId(10)).thenReturn(null)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            val exception = assertFailsWith<RessourceInexistanteException> {
                serviceTest.obtenirUtilisateurParID(10)
            }

            assertEquals("L'Utilisateur avec l'id 10 n'est pas inscrit au service", exception.message)
        }

        @WithMockUser(authorities = ["accéeder:utilisateur"])
        @Test
        fun `étant donné un utilisate d'ID 1 lorsqu'on cherche l'utilisateur par ID, on obtient le Joueur complet`(){
            Mockito.`when`( mockUtilisateurDAO.chercherParId(1)).thenReturn(hamza)
            val cobaye = UtilisateurService( mockUtilisateurDAO)

            val résultat_obtenu = cobaye.obtenirUtilisateurParID(1)

            val résultat_attendu = hamza

            assertEquals( résultat_attendu, résultat_obtenu )
        }
        @Test
        fun `étant donne un nom inexistant, lorsqu'on recherche par son nom sans jeton, une RessourceInexistanteException est levee`() {
            Mockito.`when`(mockUtilisateurDAO.chercherParNom("Inexistant")).thenReturn(null)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            assertFailsWith<RessourceInexistanteException> {
                serviceTest.obtenirUtilisateurParNom("Inexistant")
            }
        }
        @Test
        @WithMockUser(authorities = ["créer:utilisateur"]) 
        fun `lorsqu'un utilisateur avec le même email existe déjà, une exception ConflitAvecUneRessourceExistanteException est levée`() {
            
            val utilisateur = Utilisateur(id = 1, courriel = "hamzaekram@outlook.com", nom = "Hamza", prenom = "Ekram",imageUtilisateur =  null, numero_telephone = "12345678", role = "client")
            
            val jeton = Mockito.mock(Jwt::class.java)
            Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to "hamzaekram@outlook.com"))
            Mockito.`when`(mockUtilisateurDAO.chercherParCourriel("hamzaekram@outlook.com")).thenReturn(utilisateur)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            assertFailsWith<ConflitAvecUneRessourceExistanteException> {
                serviceTest.creerUtilisateur(utilisateur, jeton)
            }
        }

        @Test
        @WithMockUser(authorities = ["créer:utilisateur"]) 
        fun `lorsqu'un utilisateur n'existe pas encore avec le même email, l'utilisateur est créé`() {
            val utilisateur = Utilisateur(id = 2, courriel = "hamzaekram@outlook.com", nom = "Hamza", prenom = "Ekram",imageUtilisateur =  null, numero_telephone = "12345678", role = "client")
            
            val jeton = Mockito.mock(Jwt::class.java)
            Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to "hamzaekram@outlook.com"))
            Mockito.`when`(mockUtilisateurDAO.chercherParCourriel("hamzaekram@outlook.com")).thenReturn(null)
            Mockito.`when`(mockUtilisateurDAO.ajouter(utilisateur)).thenReturn(utilisateur)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            val utilisateurCree = serviceTest.creerUtilisateur(utilisateur, jeton)

            assertEquals(utilisateur, utilisateurCree)
        }


        @WithMockUser(authorities = ["modifier:utilisateur"])
        @Test
        fun `étant donne un utilisateur autorise avec un jeton valide, lorsqu'on met a jour l'utilisateur, la mise a jour est effectuee avec succes`() {
            val utilisateurMisAJour = hamza.copy(nom = "EkramU")
            val jeton = Mockito.mock(Jwt::class.java)

            Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to hamza.courriel))
            Mockito.`when`(mockUtilisateurDAO.chercherParId(1)).thenReturn(hamza)
            Mockito.`when`(mockUtilisateurDAO.modifier(1, utilisateurMisAJour)).thenReturn(utilisateurMisAJour)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            val resultatObserve = serviceTest.majUtilisateur(1, utilisateurMisAJour, jeton)

            assertEquals(utilisateurMisAJour, resultatObserve)
        }


        @Test
        fun `étant donne un utilisateur non autorise avec un jeton invalide, lorsqu'on tente une mise a jour, une OperationNonAutoriseeException est levee`() {
            val utilisateurMisAJour = hamza.copy(nom = "EkramUpdated")
            val jeton = Mockito.mock(Jwt::class.java)

            Mockito.`when`(jeton.claims).thenReturn(emptyMap<String, Any>())

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            assertFailsWith<OperationNonAutoriseeException> {
                serviceTest.majUtilisateur(1, utilisateurMisAJour, jeton)
            }
        }
        @WithMockUser(authorities = ["supprimer:utilisateur"])
        @Test
        fun `étant donne un utilisateur autorise avec un jeton valide, lorsqu'on supprime l'utilisateur, la suppression est effectuee avec succes`() {
            val jeton = Mockito.mock(Jwt::class.java)

            Mockito.`when`(jeton.claims).thenReturn(mapOf("courriel" to hamza.courriel))
            Mockito.`when`(mockUtilisateurDAO.chercherParId(1)).thenReturn(hamza)

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            serviceTest.supprimerUtilisateur(1, jeton)

            Mockito.verify(mockUtilisateurDAO).effacer(1)
        }

        @Test
        fun `étant donne un utilisateur non autorise avec un jeton invalide, lorsqu'on tente une suppression, une OperationNonAutoriseeException est levee`() {
            val jeton = Mockito.mock(Jwt::class.java)

            Mockito.`when`(jeton.claims).thenReturn(emptyMap<String, Any>())

            val serviceTest = UtilisateurService(mockUtilisateurDAO)

            assertFailsWith<OperationNonAutoriseeException> {
                serviceTest.supprimerUtilisateur(1, jeton)
            }
        }

    }