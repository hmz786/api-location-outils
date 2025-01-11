package dti.crosemont.quebec.location_outils.Domaine.Modele

import java.time.LocalDate

data class Outils(
             val id: Int,
             var nom: String,
             var description: String,
             var prix: Double,
             var disponibilité: Boolean, 
             var imageOutil: String?,
             var categorie : String,
             var datePublication : LocalDate,
             val coordonné : Pair<Double, Double>,
             val etat : String,
             val fournisseur : Utilisateur
            ){
                init {
                    require(nom.isNotEmpty()) {"Le nom d'outil ne peut pas etre vide "}
                    require(nom.first().isUpperCase()) {"Le nom d'outil ne peut pas commencer par miniscule"}
                    
                    require(!nom.startsWith(" ") && !nom.startsWith("-") && !nom.startsWith("#")){
                        "Le nom d'outil ne peut pas commencer par une espace ou des carctere invalides"
                    }
              }



             }
 
             

