package dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees

interface DAO<T> {
    fun chercherTous(): List<T>
    fun chercherParId(id: Int): T?
    fun ajouter(element: T): T?
    fun modifier(id: Int, element: T): T?
    fun effacer(id: Int)
    fun chercherParNom(nom: String): List<T>

}
