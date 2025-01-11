package dti.crosemont.quebec.location_outils.Domaine.Modele

data class Utilisateur(
    val id : Int,
	val imageUtilisateur : String?,
	val prenom : String,
	val nom : String,
	val numero_telephone : String,
	val courriel : String,
	val role : String
)
{
	init{
		require(nom.length > 2){"Le nom ne peut pas avoir moin de 3 caractere"}
		require(nom.length < 30){"Le nom ne doit pas contenir plus de 30 caractere"}

		require(nom.isNotEmpty()) {"Le nom ne peut pas etre vide "}

		require(!nom.startsWith(" ") && !nom.startsWith("-")&& !nom.startsWith("#")){
			"Le nom ne peut pas commencer par un espace ou des carctere invalides"
		}
		require(nom.first().isLetter() && nom.first().isUpperCase()){"Le premier lettre doit obligement etre une lettre"}

		require(prenom.length > 2){"Le prenom ne peut pas avoir moin de 3 caractere"}
		require(prenom.length < 30){"Le prenom ne peut pas avoir plus de 30 caractere"}
		require(prenom.isNotEmpty()){"Le prenom ne peut pas etre vide"}
		require(!prenom.startsWith(" ") && !prenom.startsWith("-") && !prenom.startsWith("#")){
			"Le nom ne peut pas commencer par une espace ou des carctere invalides"
		}
		require(prenom.first().isUpperCase() && prenom.first().isLetter()){
			"Le premier lettre doit obligement etre un majuscule et doit absolument etre une lettre"}

		require(courriel.matches(Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"))){"Addresse courriel Invalide"}
	}
}