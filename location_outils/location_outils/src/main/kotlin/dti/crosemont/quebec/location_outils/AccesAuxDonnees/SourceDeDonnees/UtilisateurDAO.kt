package dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees

import dti.crosemont.quebec.location_outils.Domaine.Modele.Utilisateur


interface UtilisateurDAO: DAO<Utilisateur> {            
    fun chercherParCourriel(courriel: String): Utilisateur?
    override fun chercherTous(): List<Utilisateur>
    override fun chercherParId(id:Int):Utilisateur?
    override fun chercherParNom(nom:String):List<Utilisateur>
    override fun ajouter(utilisateur: Utilisateur): Utilisateur?
    override fun modifier(id: Int, utilisateur: Utilisateur): Utilisateur? 
    override fun effacer(id: Int)
}
