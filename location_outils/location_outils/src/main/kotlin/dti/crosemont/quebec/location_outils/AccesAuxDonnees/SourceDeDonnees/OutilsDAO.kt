package dti.crosemont.quebec.location_outils.AccesAuxDonnees.SourcesDeDonnees
import dti.crosemont.quebec.location_outils.Domaine.Modele.Outils

interface OutilsDAO : DAO<Outils>{
    override fun chercherTous(): List<Outils>
    override fun chercherParId(id: Int): Outils?
    override fun chercherParNom(nom:String):List<Outils>
    override fun ajouter(outils: Outils): Outils?
    override fun modifier(id: Int, outils: Outils): Outils?
    override fun effacer(id: Int)
}